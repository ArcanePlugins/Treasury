/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.event.treasury2bukkit;

import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.common.event.EventPriority;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.event.NonPlayerAccountTransactionEvent;
import me.lokka30.treasury.api.economy.event.PlayerAccountTransactionEvent;
import me.lokka30.treasury.api.economy.events.AccountTransactionEvent;
import me.lokka30.treasury.plugin.bukkit.event.EventMigrationManager;
import org.bukkit.Bukkit;

public class T2BEventMigrator {

    public static void registerListener() {
        EventBus bus = EventBus.INSTANCE;
        bus.subscribe(bus
                .subscriptionFor(AccountTransactionEvent.class)
                .withPriority(EventPriority.HIGH)
                .whenCalled(event -> {
                    if (EventMigrationManager.INSTANCE.getCurrentlyCalledEventFromBukkit() != null) {
                        return;
                    }

                    Account account = event.getAccount();
                    if (account instanceof PlayerAccount) {
                        // player event
                        EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromTreasury(
                                EventMigrationManager.CurrentlyCalledEvent.PLAYER);

                        PlayerAccountTransactionEvent pate = new PlayerAccountTransactionEvent(event.getTransaction(),
                                (PlayerAccount) account,
                                true
                        );
                        pate.setCancelled(event.isCancelled());

                        Bukkit.getPluginManager().callEvent(pate);

                        event.setCancelled(pate.isCancelled());

                        EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromTreasury(null);
                    }

                    if (account instanceof NonPlayerAccount) {
                        // non player event
                        EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromTreasury(
                                EventMigrationManager.CurrentlyCalledEvent.NON_PLAYER);

                        NonPlayerAccountTransactionEvent npate = new NonPlayerAccountTransactionEvent(event.getTransaction(),
                                (NonPlayerAccount) account,
                                true
                        );
                        npate.setCancelled(event.isCancelled());

                        Bukkit.getPluginManager().callEvent(npate);

                        event.setCancelled(npate.isCancelled());

                        EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromTreasury(null);
                    }
                })
                .completeSubscription());
    }

}
