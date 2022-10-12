/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class BalanceCache extends BukkitRunnable {

    private final Multimap<UUID, Map.Entry<String, BigDecimal>> balances = HashMultimap.create();
    private final int delay;
    private final AtomicReference<EconomyProvider> providerRef;
    private AtomicBoolean available;

    public BalanceCache(int delay, AtomicReference<EconomyProvider> providerRef) {
        this.delay = delay;
        this.providerRef = providerRef;
        this.available = new AtomicBoolean(false);
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
        return this.available.get();
    }

    @Override
    public void run() {
        this.available.set(false);
        EconomyProvider provider = providerRef.get();
        if (provider == null) {
            return;
        }
        balances.clear();
        this.proceed(0, Arrays.asList(Bukkit.getOfflinePlayers()), provider);
        this.available.set(true);
    }

    private void proceed(
            int currentIndex, List<OfflinePlayer> players, EconomyProvider provider
    ) {
        if (currentIndex == players.size()) {
            return;
        }
        OfflinePlayer player = players.get(currentIndex);
        EconomySubscriber
                .<Boolean>asFuture(s -> provider.hasPlayerAccount(player.getUniqueId(), s))
                .thenCompose(val -> {
                    if (val) {
                        return EconomySubscriber.<PlayerAccount>asFuture(s -> provider.retrievePlayerAccount(
                                player.getUniqueId(),
                                s
                        ));
                    } else {
                        return CompletableFuture.completedFuture(null);
                    }
                })
                .whenComplete((account, ex) -> {
                    if (ex != null) {
                        throw new RuntimeException(
                                "An error occurred whilst updating balance cache",
                                ex
                        );
                    }

                    if (account == null) {
                        // proceed with next entry
                        proceed(currentIndex + 1, players, provider);
                        return;
                    }

                    List<CompletableFuture<Map.Entry<String, BigDecimal>>> balanceFutures = new ArrayList<>();
                    for (Currency currency : provider.getCurrencies()) {
                        balanceFutures.add(EconomySubscriber
                                .<BigDecimal>asFuture(s -> account.retrieveBalance(currency, s))
                                .thenApply(dec -> new AbstractMap.SimpleImmutableEntry<>(
                                        currency.getIdentifier(),
                                        dec
                                )));
                    }
                    FutureHelper
                            .joinAndFilter(bal -> CompletableFuture.completedFuture(bal.getValue() != null && !bal
                                    .getValue()
                                    .equals(BigDecimal.ZERO)), balanceFutures)
                            .whenComplete((
                                    balances, ex1
                            ) -> {
                                if (ex1 != null) {
                                    throw new RuntimeException(
                                            "An error occurred whilst updating balance cache",
                                            ex1
                                    );
                                }
                                this.balances.putAll(player.getUniqueId(), balances);
                                proceed(currentIndex + 1, players, provider);
                            });
                });
    }

}
