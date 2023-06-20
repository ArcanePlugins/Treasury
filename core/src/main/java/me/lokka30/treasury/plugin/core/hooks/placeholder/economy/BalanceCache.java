/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks.placeholder.economy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.TreasuryException;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.jetbrains.annotations.Nullable;

public class BalanceCache extends Scheduler.ScheduledTask {

    private final Multimap<UUID, Map.Entry<String, BigDecimal>> balances = HashMultimap.create();
    private Map<UUID, String> playerDataNames = new ConcurrentHashMap<>();
    private final int delay;
    private final AtomicReference<EconomyProvider> providerRef;
    private final AtomicReference<CountDownLatch> doneLatch = new AtomicReference<>(new CountDownLatch(
            1));

    public BalanceCache(
            int delay, AtomicReference<EconomyProvider> providerRef
    ) {
        super(TreasuryPlugin.getInstance().scheduler());
        this.delay = delay;
        this.providerRef = providerRef;
    }

    public void start() {
        start(1, delay, TimeUnit.SECONDS);
    }

    public @Nullable BigDecimal getBalance(UUID uuid, String currencyId) {
        Collection<Map.Entry<String, BigDecimal>> collection = balances.get(uuid);
        // optimise
        if (collection.isEmpty()) {
            return null;
        }
        return collection
                .stream()
                .filter(e -> e.getKey().equalsIgnoreCase(currencyId))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public Map<UUID, String> getPlayerDataNames() {
        return this.playerDataNames;
    }

    public boolean available() {
        return this.doneLatch.get().getCount() == 0;
    }

    public void await() throws InterruptedException {
        this.doneLatch.get().await();
    }

    @Override
    public void run() {
        EconomyProvider provider = providerRef.get();
        if (provider == null) {
            return;
        }
        balances.clear();
        provider.retrievePlayerAccountIds().whenComplete((ids, ex) -> {
            if (ex != null) {
                TreasuryPlugin.getInstance().logger().error(
                        "Error retrieving player account ids",
                        ex
                );
                return;
            }
            List<CompletableFuture<PlayerAccount>> accountsFutures = new ArrayList<>();
            for (UUID uuid : ids) {
                accountsFutures.add(provider.accountAccessor().player().withUniqueId(uuid).get());
            }
            FutureHelper
                    .joinAndFilter(
                            $ -> CompletableFuture.completedFuture(TriState.TRUE),
                            accountsFutures
                    )
                    .whenComplete((accounts, ex1) -> {
                        if (ex1 != null) {
                            TreasuryPlugin.getInstance().logger().error(
                                    "Error retrieving player accounts",
                                    ex1
                            );
                            return;
                        }
                        // fill out names
                        for (PlayerAccount account : accounts) {
                            if (account.getName().isPresent()) {
                                playerDataNames.put(account.identifier(), account.getName().get());
                            }
                        }
                        this.proceed(0, new ArrayList<>(accounts), provider);
                    });
        });
        CountDownLatch latch = this.doneLatch.get();
        if (latch.getCount() != 0) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void proceed(
            int currentIndex, List<PlayerAccount> players, EconomyProvider provider
    ) {
        CountDownLatch latch = doneLatch.get();
        if (latch.getCount() != 1) {
            latch = new CountDownLatch(1);
            doneLatch.set(latch);
        }
        if (currentIndex == players.size()) {
            // java can be weird sometimes
            this.doneLatch.get().countDown();
            return;
        }
        PlayerAccount account = players.get(currentIndex);
        String name = account.getName().orElse(null);

        List<CompletableFuture<Map.Entry<String, BigDecimal>>> balanceFutures = new ArrayList<>();
        for (Currency currency : provider.getCurrencies()) {
            balanceFutures.add(account
                    .retrieveBalance(currency)
                    .exceptionally(e -> {
                        if (e instanceof TreasuryException) {
                            TreasuryPlugin.getInstance().logger().error(
                                    "Error whilst trying to update balance cache for " + (name != null
                                            ? name
                                            : account
                                                    .identifier()
                                                    .toString()) + ": " + e.getMessage());
                            return null;
                        }
                        throw new RuntimeException(e);
                    })
                    .thenApply(balance -> new AbstractMap.SimpleImmutableEntry<>(
                            currency.getIdentifier(),
                            balance
                    )));
        }

        FutureHelper
                .joinAndFilter(bal -> CompletableFuture.completedFuture(TriState.fromBoolean(bal.getValue() != null && !bal
                        .getValue()
                        .equals(BigDecimal.ZERO))), balanceFutures)
                .whenComplete((balances, ex1) -> {
                    if (ex1 != null) {
                        throw new RuntimeException(
                                "An error occurred whilst updating balance cache",
                                ex1
                        );
                    }
                    this.balances.putAll(account.identifier(), balances);
                    proceed(currentIndex + 1, players, provider);
                });
    }

}
