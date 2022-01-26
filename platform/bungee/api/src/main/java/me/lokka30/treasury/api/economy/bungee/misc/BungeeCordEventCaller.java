package me.lokka30.treasury.api.economy.bungee.misc;

import java.util.Objects;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.event.AccountTransactionEvent;
import me.lokka30.treasury.api.economy.event.NonPlayerAccountTransactionEvent;
import me.lokka30.treasury.api.economy.event.PlayerAccountTransactionEvent;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * A class which makes it easier with calling Treasury events.
 * <p>
 * Story goes the following: BungeeCord does not have any event hierarchy such as Bukkit has. If
 * you call an event extending another event, what would happen in Bukkit is that the event which
 * the called event extends will also be called, whilst in BungeeCord it would not! That's why we
 * have this class ; to call 2 events at the same time easily and combine their outcomes.
 *
 * @author MrIvanPlays
 */
public final class BungeeCordEventCaller {

    /**
     * Calls {@link AccountTransactionEvent} and {@link PlayerAccountTransactionEvent}
     *
     * @param account     the player account that transaction is made onto
     * @param transaction the transaction
     * @param callback    the callback of the event outcome - whether at least one of the events is
     *                    cancelled.
     * @author MrIvanPlays
     */
    public static void callPlayerTransactionEvent(
            @NotNull PlayerAccount account,
            @NotNull EconomyTransaction transaction,
            @NotNull Callback<Boolean> callback
    ) {
        Objects.requireNonNull(account, "account");
        Objects.requireNonNull(transaction, "transaction");
        Objects.requireNonNull(callback, "callback");

        Callback<AccountTransactionEvent> ateCallback = (event, ex) -> {
            if (ex != null) {
                callback.done(null, ex);
                return;
            }

            Callback<PlayerAccountTransactionEvent> pateCallback = (patEvent, ex1) -> {
                if (ex1 != null) {
                    callback.done(null, ex1);
                    return;
                }

                callback.done(isCancelled(event, patEvent), null);
            };

            ProxyServer
                    .getInstance()
                    .getPluginManager()
                    .callEvent(new PlayerAccountTransactionEvent(account,
                            transaction,
                            pateCallback
                    ));
        };

        ProxyServer.getInstance().getPluginManager().callEvent(new AccountTransactionEvent(account,
                transaction,
                ateCallback
        ));
    }

    /**
     * Calls {@link AccountTransactionEvent} and {@link NonPlayerAccountTransactionEvent}
     *
     * @param account     the non player account that transaction is made onto
     * @param transaction the transaction
     * @param callback    the callback of the event outcome - whether at least one of the events is
     *                    cancelled.
     * @author MrIvanPlays
     */
    public static void callNonPlayerTransactionEvent(
            @NotNull NonPlayerAccount account,
            @NotNull EconomyTransaction transaction,
            @NotNull Callback<Boolean> callback
    ) {
        Objects.requireNonNull(account, "account");
        Objects.requireNonNull(transaction, "transaction");
        Objects.requireNonNull(callback, "callback");

        Callback<AccountTransactionEvent> ateCallback = (event, ex) -> {
            if (ex != null) {
                callback.done(null, ex);
                return;
            }

            Callback<NonPlayerAccountTransactionEvent> pateCallback = (patEvent, ex1) -> {
                if (ex1 != null) {
                    callback.done(null, ex1);
                    return;
                }

                callback.done(isCancelled(event, patEvent), null);
            };

            ProxyServer
                    .getInstance()
                    .getPluginManager()
                    .callEvent(new NonPlayerAccountTransactionEvent(account,
                            transaction,
                            pateCallback
                    ));
        };

        ProxyServer.getInstance().getPluginManager().callEvent(new AccountTransactionEvent(account,
                transaction,
                ateCallback
        ));
    }

    private static boolean isCancelled(Cancellable event, Cancellable event1) {
        boolean cancelled;
        if (event.isCancelled() && event1.isCancelled()) {
            cancelled = true;
        } else if (!event.isCancelled() && event1.isCancelled()) {
            cancelled = true;
        } else if (event.isCancelled() && !event1.isCancelled()) {
            cancelled = true;
        } else if (!event.isCancelled() && !event1.isCancelled()) {
            cancelled = false;
        } else {
            cancelled = false;
        }
        return cancelled;
    }

}
