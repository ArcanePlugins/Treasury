/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.event.bukkit2treasury;

import me.lokka30.treasury.api.event.EventBus;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.event.AccountTransactionEvent;
import me.lokka30.treasury.api.economy.events.NonPlayerAccountTransactionEvent;
import me.lokka30.treasury.api.economy.events.PlayerAccountTransactionEvent;
import me.lokka30.treasury.plugin.bukkit.event.EventMigrationManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class B2TEventMigrator implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTransaction(AccountTransactionEvent event) {
        if (EventMigrationManager.INSTANCE.getCurrentlyCalledEventFromTreasury() != null) {
            return;
        }

        Account account = event.getAccount();
        if (account instanceof PlayerAccount) {
            // player account
            EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromBukkit(EventMigrationManager.CurrentlyCalledEvent.PLAYER);

            PlayerAccountTransactionEvent pate = new PlayerAccountTransactionEvent(event.getTransaction(),
                    (PlayerAccount) account
            );
            pate.setCancelled(event.isCancelled());

            EventBus.INSTANCE.fire(pate).whenCompleteBlocking((e, errors) -> {
                if (!errors.isEmpty()) {
                    for (Throwable error : errors) {
                        error.printStackTrace();
                    }
                    return;
                }

                if (e == null) {
                    return;
                }

                event.setCancelled(e.isCancelled());
                EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromBukkit(null);
            });
        }

        if (account instanceof NonPlayerAccount) {
            // non player account
            EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromBukkit(EventMigrationManager.CurrentlyCalledEvent.NON_PLAYER);

            NonPlayerAccountTransactionEvent npate = new NonPlayerAccountTransactionEvent(event.getTransaction(),
                    (NonPlayerAccount) account
            );
            npate.setCancelled(event.isCancelled());

            EventBus.INSTANCE.fire(npate).whenCompleteBlocking((e, errors) -> {
                if (!errors.isEmpty()) {
                    for (Throwable error : errors) {
                        error.printStackTrace();
                    }
                    return;
                }

                if (e == null) {
                    return;
                }

                event.setCancelled(e.isCancelled());
                EventMigrationManager.INSTANCE.setCurrentlyCalledEventFromBukkit(null);
            });
        }
    }

}
