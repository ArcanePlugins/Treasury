/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.listener;

import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.economy.event.NonPlayerAccountTransactionEvent;
import me.lokka30.treasury.api.economy.event.PlayerAccountTransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TreasuryEventMigration implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNonPlayerTransaction(NonPlayerAccountTransactionEvent event) {
        me.lokka30.treasury.api.economy.events.NonPlayerAccountTransactionEvent newEvent = new me.lokka30.treasury.api.economy.events.NonPlayerAccountTransactionEvent(event.getTransaction(),
                event.getAccount()
        );

        newEvent.setCancelled(event.isCancelled());
        EventBus.INSTANCE.fire(newEvent).whenCompleteBlocking((retEvent, errors) -> {
            if (!errors.isEmpty()) {
                for (Throwable e : errors) {
                    e.printStackTrace();
                }
                return;
            }

            if (retEvent == null) {
                return;
            }

            event.setCancelled(retEvent.isCancelled());
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTransaction(PlayerAccountTransactionEvent event) {
        me.lokka30.treasury.api.economy.events.PlayerAccountTransactionEvent newEvent = new me.lokka30.treasury.api.economy.events.PlayerAccountTransactionEvent(event.getTransaction(),
                event.getAccount()
        );

        newEvent.setCancelled(event.isCancelled());
        EventBus.INSTANCE.fire(newEvent).whenCompleteBlocking((retEvent, errors) -> {
            if (!errors.isEmpty()) {
                for (Throwable e : errors) {
                    e.printStackTrace();
                }
                return;
            }

            if (retEvent == null) {
                return;
            }

            event.setCancelled(retEvent.isCancelled());
        });
    }

}
