/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Baltop extends BukkitRunnable {

    private final boolean enabled;
    private final int topSize, taskDelay;
    private final EconomyProvider provider;

    private Multimap<String, TopPlayer> baltop;

    public Baltop(
            boolean enabled, int topSize, int taskDelay, EconomyProvider provider
    ) {
        this.enabled = enabled;
        this.topSize = topSize;
        this.taskDelay = taskDelay;
        this.provider = provider;
        this.baltop = MultimapBuilder.hashKeys().treeSetValues(TopPlayer::compareTo).build();
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
        int positionInt = 0;
        String name = null;
        for (TopPlayer player : baltop.get(currencyId)) {
            positionInt++;
            if (positionInt == position) {
                name = player.getName();
                break;
            }
        }
        if (name != null) {
            return name;
        } else {
            return "";
        }
    }

    public BigDecimal getTopBalance(String currencyId, int position) {
        int positionInt = 0;
        BigDecimal balance = null;
        for (TopPlayer player : baltop.get(currencyId)) {
            positionInt++;
            if (positionInt == position) {
                balance = player.getBalance();
                break;
            }
        }
        return balance;
    }

    @Override
    public void run() {
        if (!enabled) {
            cancel();
            return;
        }
        List<Throwable> errors = handlePlayers(0, 0, new ArrayList<>());
        if (!errors.isEmpty()) {
            Logger logger = TreasuryPlugin.getInstance().logger();
            logger.error("A few errors encountered whilst retreiving baltop");
            for (Throwable e : errors) {
                logger.error(e.getMessage(), e);
            }
            return;
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

    private List<Throwable> handlePlayers(int index, int posIndex, List<Throwable> errorsToThrow) {
        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        for (int i = index; i < players.length; i++) {
            OfflinePlayer player = players[i];
            if (player == null || player.getName() == null) {
                continue;
            }

            final int nextIndex = i + 1;
            EconomySubscriber.<Boolean>asFuture(s -> provider.hasPlayerAccount(player.getUniqueId(),
                    s
            )).thenCompose(val -> {
                if (val) {
                    return EconomySubscriber.<PlayerAccount>asFuture(s -> provider.retrievePlayerAccount(player.getUniqueId(),
                            s
                    ));
                } else {
                    return null;
                }
            }).whenComplete((account, errors) -> {
                if (errors != null) {
                    errorsToThrow.add(errors);
                    return;
                }
                if (account != null) {
                    for (Currency currency : provider.getCurrencies()) {
                        EconomySubscriber
                                .<BigDecimal>asFuture(s -> account.retrieveBalance(currency, s))
                                .whenComplete((bal, error) -> {
                                    if (error != null) {
                                        errorsToThrow.add(error);
                                        return;
                                    }

                                    if (bal != null && !bal.equals(BigDecimal.ZERO)) {
                                        baltop.put(currency.getIdentifier(),
                                                new TopPlayer(player.getName(), bal)
                                        );
                                    }

                                    handlePlayers(nextIndex, posIndex + 1, errorsToThrow);
                                });
                    }
                } else {
                    handlePlayers(nextIndex, posIndex + 1, errorsToThrow);
                }
            });
            break;
        }
        return errorsToThrow;
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
        public int compareTo(@NotNull Baltop.TopPlayer o) {
            return balance.compareTo(o.getBalance());
        }

    }

}
