/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi.economy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.TreasuryException;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class BalanceCache extends BukkitRunnable {

    private final Multimap<UUID, Map.Entry<String, BigDecimal>> balances = HashMultimap.create();
    private final int delay;
    private final AtomicReference<EconomyProvider> providerRef;
    private final AtomicReference<CountDownLatch> doneLatch = new AtomicReference<>(new CountDownLatch(
            1));

    public BalanceCache(int delay, AtomicReference<EconomyProvider> providerRef) {
        this.delay = delay;
        this.providerRef = providerRef;
    }

    public void start(TreasuryBukkit plugin) {
        runTaskTimerAsynchronously(plugin, 20, delay * 20L);
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
        this.proceed(0, Arrays.asList(Bukkit.getOfflinePlayers()), provider);
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
            int currentIndex, List<OfflinePlayer> players, EconomyProvider provider
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
        OfflinePlayer player = players.get(currentIndex);
        provider.hasAccount(AccountData.forPlayerAccount(player.getUniqueId())).thenCompose(res -> {
            if (res) {
                return provider.accountAccessor().player().withUniqueId(player.getUniqueId()).get();
            }
            return FutureHelper.failedFuture(new TreasuryException("accountNotExists"));
        }).whenComplete((account, ex) -> {
            if (ex != null) {
                if (ex instanceof TreasuryException) {
                    if (!ex.getMessage().equalsIgnoreCase("accountNotExists")) {
                        // log the problem and proceed with next entry
                        TreasuryPlugin.getInstance().logger().error(
                                "Error whilst trying to update balance cache for " + (player.getName() != null
                                        ? player.getName()
                                        : player
                                                .getUniqueId()
                                                .toString()) + ": " + ex.getMessage());
                    }
                    proceed(currentIndex + 1, players, provider);
                    return;
                }
                throw new RuntimeException("An error occurred whilst updating balance cache", ex);
            }

            List<CompletableFuture<Map.Entry<String, BigDecimal>>> balanceFutures = new ArrayList<>();
            for (Currency currency : provider.getCurrencies()) {
                balanceFutures.add(account
                        .retrieveBalance(currency)
                        .exceptionally(e -> {
                            if (e instanceof TreasuryException) {
                                TreasuryPlugin.getInstance().logger().error(
                                        "Error whilst trying to update balance cache for " + (player.getName() != null
                                                ? player.getName()
                                                : player
                                                        .getUniqueId()
                                                        .toString()) + ": " + e.getMessage());
                                return null;
                            }
                            throw new RuntimeException(e);
                        })
                        .thenApply(balance -> new AbstractMap.SimpleImmutableEntry<>(currency.getIdentifier(),
                                balance
                        )));
            }

            FutureHelper
                    .joinAndFilter(bal -> CompletableFuture.completedFuture(TriState.fromBoolean(bal.getValue() != null && !bal
                            .getValue()
                            .equals(BigDecimal.ZERO))), balanceFutures)
                    .whenComplete((balances, ex1) -> {
                        if (ex1 != null) {
                            throw new RuntimeException("An error occurred whilst updating balance cache",
                                    ex1
                            );
                        }
                        this.balances.putAll(player.getUniqueId(), balances);
                        proceed(currentIndex + 1, players, provider);
                    });
        });
    }

}
