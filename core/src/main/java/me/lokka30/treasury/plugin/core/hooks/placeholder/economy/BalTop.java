/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks.placeholder.economy;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.hooks.PlayerData;
import me.lokka30.treasury.plugin.core.hooks.placeholder.PlaceholdersExpansion;
import me.lokka30.treasury.plugin.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BalTop extends Scheduler.ScheduledTask {

    private final PlaceholdersExpansion base;
    private final boolean enabled;
    private final int topSize;
    private final int taskDelay;
    private final AtomicReference<EconomyProvider> providerRef;
    private final BalanceCache balanceCache;

    private final Multimap<String, TopPlayer> baltop;

    public BalTop(
            PlaceholdersExpansion base,
            boolean enabled,
            int topSize,
            int taskDelay,
            BalanceCache balanceCache,
            AtomicReference<EconomyProvider> provider
    ) {
        super(TreasuryPlugin.getInstance().scheduler());
        this.base = base;
        this.enabled = enabled;
        this.topSize = topSize;
        this.taskDelay = taskDelay;
        this.balanceCache = balanceCache;
        this.providerRef = provider;
        this.baltop = Multimaps.newSortedSetMultimap(new HashMap<>(),
                () -> new TreeSet<>(TopPlayer::compareTo)
        );
    }

    public void start() {
        start(1, taskDelay, TimeUnit.SECONDS);
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
            try {
                balanceCache.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!balanceCache.available()) {
                // We'll just wait for the next cycle to update the baltop
                // Also print a warning that it failed to update the baltop
                TreasuryPlugin
                        .getInstance()
                        .logger()
                        .warn("Couldn't update baltop placeholders for PlaceholderAPI!");
                return;
            }
        }
        baltop.clear();

        for (PlayerData player : base.requestPlayerData()) {
            String playerName = player.name();
            if (playerName == null) {
                continue;
            }
            for (Currency currency : provider.getCurrencies()) {
                BigDecimal balance = balanceCache.getBalance(player.uniqueId(),
                        currency.getIdentifier()
                );
                if (balance == null) {
                    continue;
                }
                baltop.put(currency.getIdentifier(), new TopPlayer(playerName, balance));
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

        private final @NotNull String name;
        private final @NotNull BigDecimal balance;

        public TopPlayer(@NotNull String name, @NotNull BigDecimal balance) {
            this.name = name;
            this.balance = balance;
        }

        public @NotNull String getName() {
            return name;
        }

        public @NotNull BigDecimal getBalance() {
            return balance;
        }

        @Override
        public int compareTo(@NotNull BalTop.TopPlayer o) {
            return balance.compareTo(o.getBalance());
        }

    }

}
