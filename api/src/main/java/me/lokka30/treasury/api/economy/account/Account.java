/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionImportance;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An Account is something that holds a balance and is associated with
 * something bound by a UUID. For example, a PlayerAccount is bound to
 * a Player on a server by their UUID.
 *
 * @author lokka30, Geolykt, creatorfromhell
 * @see EconomyProvider
 * @see PlayerAccount
 * @see NonPlayerAccount
 * @since v1.0.0
 */
public interface Account {

    /**
     * Gets the string-based unique identifier for this account.
     *
     * @return The String unique identifier for this account.
     * @since v1.0.0
     */
    @NotNull String getIdentifier();

    /**
     * Returns the name of this {@link Account}, if specified. Empty optional otherwise.
     *
     * <p>A economy provider may choose not to provide a name.
     *
     * @return an optional fulfilled with a name or an empty optional
     * @since v1.0.0
     */
    Optional<String> getName();

    /**
     * Sets a new name for this {@link Account}, which may be null.
     *
     * @param name the new name for this account.
     * @return future with a {@link Response} which states whether the name change was successful
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> setName(@Nullable String name);

    /**
     * Request the balance of the {@code Account}.
     *
     * @param currency the {@link Currency} of the balance being requested
     * @return future with a {@link Response} which result's the balance if successful
     * @since v1.0.0
     */
    CompletableFuture<Response<BigDecimal>> retrieveBalance(@NotNull Currency currency);

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * @param amount    the amount the balance will be reduced by
     * @param initiator the one who initiated the transaction
     * @param currency  the {@link Currency} of the balance being modified
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @see Account#doTransaction(EconomyTransaction)
     * @since v1.0.0
     */
    default CompletableFuture<Response<BigDecimal>> withdrawBalance(
            @NotNull BigDecimal amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency
    ) {
        return withdrawBalance(
                amount,
                initiator,
                currency,
                EconomyTransactionImportance.NORMAL,
                null
        );
    }

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * @param amount     the amount the balance will be reduced by
     * @param initiator  the one who initiated the transaction
     * @param currency   the {@link Currency} of the balance being modified
     * @param importance how important is the transaction
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @see Account#doTransaction(EconomyTransaction)
     * @since v1.0.0
     */
    default CompletableFuture<Response<BigDecimal>> withdrawBalance(
            @NotNull BigDecimal amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomyTransactionImportance importance
    ) {
        return withdrawBalance(amount, initiator, currency, importance, null);
    }

    /**
     * Withdraw an amount from the {@code Account} balance.
     *
     * @param amount     the amount the balance will be reduced by
     * @param initiator  the one who initiated the transaction
     * @param currency   the {@link Currency} of the balance being modified
     * @param importance how important is the transaction
     * @param reason     the reason of why the balance is modified
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @see Account#doTransaction(EconomyTransaction)
     * @since v1.0.0
     */
    default CompletableFuture<Response<BigDecimal>> withdrawBalance(
            @NotNull BigDecimal amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomyTransactionImportance importance,
            @Nullable String reason
    ) {
        return doTransaction(EconomyTransaction
                .newBuilder()
                .withCurrency(currency)
                .withInitiator(initiator)
                .withReason(reason)
                .withTransactionAmount(amount)
                .withImportance(importance)
                .withTransactionType(EconomyTransactionType.WITHDRAWAL)
                .build());
    }

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * @param amount    the amount the balance will be increased by
     * @param initiator the one who initiated the transaction
     * @param currency  the {@link Currency} of the balance being modified
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @see Account#doTransaction(EconomyTransaction)
     * @since v1.0.0
     */
    default CompletableFuture<Response<BigDecimal>> depositBalance(
            @NotNull BigDecimal amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency
    ) {
        return depositBalance(
                amount,
                initiator,
                currency,
                EconomyTransactionImportance.NORMAL,
                null
        );
    }

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * @param amount     the amount the balance will be increased by
     * @param initiator  the one who initiated the transaction
     * @param currency   the {@link Currency} of the balance being modified
     * @param importance how important is the transaction
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @see Account#doTransaction(EconomyTransaction)
     * @since v1.0.0
     */
    default CompletableFuture<Response<BigDecimal>> depositBalance(
            @NotNull BigDecimal amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomyTransactionImportance importance
    ) {
        return depositBalance(amount, initiator, currency, importance, null);
    }

    /**
     * Deposit an amount into the {@code Account} balance.
     *
     * @param amount     the amount the balance will be increased by
     * @param initiator  the one who initiated the transaction
     * @param currency   the {@link Currency} of the balance being modified
     * @param importance how important is the transaction
     * @param reason     the reason of why the balance is modified
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @see Account#doTransaction(EconomyTransaction)
     * @since v1.0.0
     */
    default CompletableFuture<Response<BigDecimal>> depositBalance(
            @NotNull BigDecimal amount,
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomyTransactionImportance importance,
            @Nullable String reason
    ) {
        return doTransaction(EconomyTransaction
                .newBuilder()
                .withCurrency(currency)
                .withInitiator(initiator)
                .withTransactionAmount(amount)
                .withReason(reason)
                .withImportance(importance)
                .withTransactionType(EconomyTransactionType.DEPOSIT)
                .build());
    }

    /**
     * Does a {@link EconomyTransaction} on this account.
     *
     * @param economyTransaction the transaction that should be done
     * @return future with a {@link Response} which, if successful, contains the new balance
     * @since v1.0.0
     */
    CompletableFuture<Response<BigDecimal>> doTransaction(@NotNull EconomyTransaction economyTransaction);

    /**
     * Reset the {@code Account} balance to its starting amount.
     *
     * <p>Certain implementations, such as the {@link PlayerAccount}, may default to non-zero
     * starting balances.
     *
     * @param initiator  the one who initiated the transaction
     * @param currency   the {@link Currency} of the balance being reset
     * @param importance the reset transaction importance
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @see #resetBalance(EconomyTransactionInitiator, Currency, EconomyTransactionImportance, String)
     * @since v2.0.0
     */
    default CompletableFuture<Response<BigDecimal>> resetBalance(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomyTransactionImportance importance
    ) {
        return resetBalance(initiator, currency, importance, null);
    }

    /**
     * Reset the {@code Account} balance to its starting amount.
     *
     * <p>Certain implementations, such as the {@link PlayerAccount}, may default to non-zero starting balances.
     *
     * @param initiator  the one who initiated the transaction
     * @param currency   the {@link Currency} of the balance being reset
     * @param importance the reset transaction importance
     * @param reason     the reset reason
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @since v1.0.0 (modified in 2.0.0)
     */
    default CompletableFuture<Response<BigDecimal>> resetBalance(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Currency currency,
            @NotNull EconomyTransactionImportance importance,
            @Nullable String reason
    ) {
        Objects.requireNonNull(initiator, "initiator");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(importance, "importance");

        return doTransaction(EconomyTransaction
                .newBuilder()
                .withCurrency(currency)
                .withInitiator(initiator)
                .withTransactionAmount(BigDecimal.ZERO)
                .withReason(reason)
                .withImportance(importance)
                .withTransactionType(EconomyTransactionType.SET)
                .build()
        );
    }

    /**
     * Check if the {@code Account} can afford a withdrawal of a certain amount.
     *
     * @param amount   the amount the balance must meet or exceed
     * @param currency the {@link Currency} of the balance being queried
     * @return future with {@link Response} which if successful returns whether the balance is
     *         high enough
     * @see Account#retrieveBalance(Currency)
     * @since v1.0.0
     */
    default CompletableFuture<Response<TriState>> canAfford(
            @NotNull BigDecimal amount, @NotNull Currency currency
    ) {
        return retrieveBalance(currency).thenApply(resp -> {
            if (resp.isSuccessful()) {
                return Response.success(TriState.fromBoolean(resp
                    .getResult()
                    .compareTo(amount) >= 0));
            } else {
                return Response.failure(resp.getFailureReason());
            }
        });
    }

    /**
     * Delete data stored for the {@code Account}.
     *
     * <p>Providers should consider storing backups of deleted accounts.
     *
     * @return future with {@link Response} which if successful returns whether deletion was
     *         successful
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> deleteAccount();

    /**
     * Returns the {@link Currency#getIdentifier()  Currencies} this {@code Account} holds balance for.
     *
     * @return future with {@link Response} which if successful returns the held currencies
     * @since v1.0.0
     */
    CompletableFuture<Response<Collection<String>>> retrieveHeldCurrencies();

    /**
     * Request the {@link EconomyTransaction} history, limited by the {@code transactionCount} and the {@link Temporal}
     * {@code from} and {@link Temporal} {@code to}, of this {@code Account}.
     *
     * <p>If the specified {@code transactionCount} is higher than the known transactions, then this will return all the
     * transactions.
     * <p>If this account does not have transactions, dating back to the specified {@code from}, it will start returning
     * transactions from the oldest one.
     *
     * @param transactionCount the count of the transactions wanted
     * @param from             the timestamp to get the transactions from
     * @param to               the timestamp to get the transactions to
     * @return future with {@link Response} which if successful returns the transaction history
     * @since v1.0.0
     */
    CompletableFuture<Response<Collection<EconomyTransaction>>> retrieveTransactionHistory(
            int transactionCount, @NotNull Temporal from, @NotNull Temporal to
    );

    /**
     * Request the {@link EconomyTransaction} history, limited by the {@code transactionCount}, of this {@code Account}.
     *
     * <p>If the specified {@code transactionCount} is higher than the known transactions, then this will return all the
     * transactions.
     *
     * @param transactionCount the count of the transactions wanted
     * @return future with {@link Response} which if successful returns the transaction history
     * @since v1.0.0
     */
    default CompletableFuture<Response<Collection<EconomyTransaction>>> retrieveTransactionHistory(
            int transactionCount
    ) {
        return retrieveTransactionHistory(transactionCount, Instant.EPOCH, Instant.now());
    }

    /**
     * Request a listing of all member players of the account.
     *
     * @return future with {@link Response} which if successful returns the members
     * @since v1.0.0
     */
    CompletableFuture<Response<Collection<UUID>>> retrieveMemberIds();

    /**
     * Check if the specified user is a member of the account.
     *
     * <p>A member is any player with at least one allowed permission.
     *
     * @param player the {@link UUID} of the potential member
     * @return future with {@link Response} which if successful returns whether the user is a member
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> isMember(@NotNull UUID player);

    /**
     * Modifies the state of the specified {@link AccountPermission} {@code permissions} for the
     * specified {@link UUID} {@code player}.
     * The state of the permission is specified via the {@code permissionValue} boolean, where
     * {@code TRUE} is 'has permission', and {@code FALSE} is 'does not have permission'.
     * Just a reminder: a member is any player with at least one allowed permission.
     *
     * @param player          the player id you want to modify the permissions of
     * @param permissionValue the permission value you want to set
     * @param permissions     the permissions to modify
     * @return future with {@link Response} which if successful returns whether the permissions
     *         of the member were adjusted
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> setPermission(
            @NotNull UUID player,
            @NotNull TriState permissionValue,
            @NotNull AccountPermission @NotNull ... permissions
    );

    /**
     * Request the {@link AccountPermission AccountPermissions} for the specified {@link UUID}
     * {@code player}.
     *
     * @param player the player {@link UUID} to get the permissions for
     * @return future with {@link Response} which if successful returns an immutable map of
     *         permissions and their values.
     * @since v1.0.0
     */
    CompletableFuture<Response<Map<AccountPermission, TriState>>> retrievePermissions(@NotNull UUID player);

    /**
     * Checks whether given player has the given permission on this account.
     *
     * <p>Just a reminder: a member is any player with at least one allowed permission.
     *
     * @param player      the {@link UUID} of the player to check if they have the permission
     * @param permissions the permissions to check
     * @return future with {@link Response} which if successful returns whether the player has all the specified permissions
     * @see AccountPermission
     * @since v1.0.0
     */
    CompletableFuture<Response<TriState>> hasPermission(
            @NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions
    );

}
