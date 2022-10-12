/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi.economy;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BalTop extends BukkitRunnable {

    private final boolean enabled;
    private final int topSize;
    private final int taskDelay;
    private final AtomicReference<EconomyProvider> providerRef;
    private final BalanceCache balanceCache;

    private final Multimap<String, TopPlayer> baltop;

    public BalTop(
            boolean enabled,
            int topSize,
            int taskDelay,
            BalanceCache balanceCache,
            AtomicReference<EconomyProvider> provider
    ) {
        this.enabled = enabled;
        this.topSize = topSize;
        this.taskDelay = taskDelay;
        this.balanceCache = balanceCache;
        this.providerRef = provider;
        this.baltop = Multimaps.newSortedSetMultimap(new HashMap<>(),
                () -> new TreeSet<>(TopPlayer::compareTo)
        );
    }

    public void start(TreasuryBukkit plugin) {
        runTaskTimerAsynchronously(plugin, 20, taskDelay * 20L);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPositionAsString(String currencyId, String playerName) {
        int positionInt = 0;
        boolean found = false;
        for (TopPlayer player : baltop.get(currencyId)) {
            positionInt++;
            if (player.getName().equalsIgnoreCase(playerName)) {
                found = true;
                break;
            }
        }
        if (found) {
            return String.valueOf(positionInt);
        } else {
            return "";
        }
    }

    public String getTopPlayer(String currencyId, int position) {
        position = normalizePosition(position);
        return baltop
                .get(currencyId)
                .stream()
                .skip(position)
                .findFirst()
                .map(TopPlayer::getName)
                .orElse("");
    }

    public @Nullable BigDecimal getTopBalance(String currencyId, int position) {
        position = normalizePosition(position);
        return baltop
                .get(currencyId)
                .stream()
                .skip(position)
                .findFirst()
                .map(TopPlayer::getBalance)
                .orElse(null);
    }

    private int normalizePosition(int position) {
        if (position == 0) {
            return position;
        }
        return position - 1;
    }

    @Override
    public void run() {
        if (!enabled) {
            cancel();
            return;
        }
        EconomyProvider provider = providerRef.get();
        if (provider == null) {
            return;
        }
        if (!balanceCache.available()) {
            // yes I know busy waiting, but I don't know how to integrate it without busy waiting
            // open on ideas here
            while (!balanceCache.available()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        baltop.clear();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            for (Currency currency : provider.getCurrencies()) {
                BigDecimal balance = balanceCache.getBalance(player.getUniqueId(),
                        currency.getIdentifier()
                );
                baltop.put(currency.getIdentifier(), new TopPlayer(player.getName(), balance));
            }
        }

        for (String key : baltop.keys()) {
            Collection<TopPlayer> currentPlayers = baltop.get(key);
            if (currentPlayers.isEmpty() || currentPlayers.size() <= topSize) {
                continue;
            }
            List<TopPlayer> newPlayers = new ArrayList<>();
            int count = 0;
            for (TopPlayer player : currentPlayers) {
                newPlayers.add(player);
                count++;
                if (count == topSize) {
                    break;
                }
            }
            baltop.replaceValues(key, newPlayers);
        }
    }

    public static class TopPlayer implements Comparable<TopPlayer> {

        private final String name;
        private final BigDecimal balance;

        public TopPlayer(String name, BigDecimal balance) {
            this.name = name;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        @Override
        public int compareTo(@NotNull BalTop.TopPlayer o) {
            return balance.compareTo(o.getBalance());
        }

    }

}
