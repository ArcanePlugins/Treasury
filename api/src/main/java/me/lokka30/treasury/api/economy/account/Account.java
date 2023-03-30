/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.common.misc.TriState;
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
 * an identifier, which is specific to the account type. For example,
 * {@link PlayerAccount player accounts} use {@link UUID uuids} for
 * identifier/identification, whereas {@link NonPlayerAccount non player accounts}
 * use {@link me.lokka30.treasury.api.common.NamespacedKey namespaced keys}.
 * <br>
 * <b>WARNING:</b> This is a raw type. It does not hold any identifier/identification
 * information.
 *
 * @author lokka30, Geolykt, creatorfromhell
 * @see EconomyProvider
 * @see PlayerAccount
 * @see NonPlayerAccount
 * @since v1.0.0
 */
public interface Account {

    /**
     * Returns the name of this {@link Account}, if specified. Otherwise, an empty
     * {@link Optional} is returned.
     *
     * @return an optional, fulfilled with either a name or an empty optional.
     * @since v1.0.0
     */
    @NotNull Optional<String> getName();

    /**
     * Sets a new name for this {@link Account}, which may be null.
     *
     * @param name the new name for this account.
     * @return whether the name was changed
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Boolean> setName(@Nullable String name);

    /**
     * Request the balance of the {@code Account}.
     *
     * @param currency the {@link Currency} of the balance being requested
     * @return a {@link BigDecimal} value representation of the balance
     * @since v1.0.0
     */
    @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency);

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
    @NotNull
    default CompletableFuture<BigDecimal> withdrawBalance(
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
    @NotNull
    default CompletableFuture<BigDecimal> withdrawBalance(
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
    @NotNull
    default CompletableFuture<BigDecimal> withdrawBalance(
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
                .withAmount(amount)
                .withImportance(importance)
                .withType(EconomyTransactionType.WITHDRAWAL)
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
    @NotNull
    default CompletableFuture<BigDecimal> depositBalance(
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
    @NotNull
    default CompletableFuture<BigDecimal> depositBalance(
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
    @NotNull
    default CompletableFuture<BigDecimal> depositBalance(
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
                .withAmount(amount)
                .withReason(reason)
                .withImportance(importance)
                .withType(EconomyTransactionType.DEPOSIT)
                .build());
    }

    /**
     * Does a {@link EconomyTransaction} on this account.
     *
     * @param economyTransaction the transaction that should be done
     * @return a {@link BigDecimal} value representing the new balance resulting from the transaction
     * @since v1.0.0
     */
    @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction economyTransaction);

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
    @NotNull
    default CompletableFuture<BigDecimal> resetBalance(
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
     * @param initiator  the initiator of the transaction
     * @param currency   the {@link Currency} of the balance being reset
     * @param importance the reset transaction importance
     * @param reason     the reset reason
     * @return see {@link #doTransaction(EconomyTransaction)}
     * @since v1.0.0 (modified in 2.0.0)
     */
    @NotNull
    default CompletableFuture<BigDecimal> resetBalance(
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
                .withAmount(currency.getStartingBalance(this))
                .withReason(reason)
                .withImportance(importance)
                .withType(EconomyTransactionType.SET)
                .build());
    }

    /**
     * Delete data stored for the {@code Account}.
     *
     * <p>Providers should consider storing backups of deleted accounts.
     *
     * @return whether the account was deleted
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Boolean> deleteAccount();

    /**
     * Returns the {@link Currency#getIdentifier()  Currencies} this {@code Account} holds balance for.
     *
     * @return a collection of held currencies in the form of currency ids
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies();

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
     * @return a collection of transaction entries
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(
            int transactionCount, @NotNull Temporal from, @NotNull Temporal to
    );

    /**
     * Request the {@link EconomyTransaction} history, limited by the {@code transactionCount}, of this {@code Account}.
     *
     * <p>If the specified {@code transactionCount} is higher than the known transactions, then this will return all the
     * transactions.
     *
     * @param transactionCount the count of the transactions wanted
     * @return a collection of transaction entries
     * @since v1.0.0
     */
    @NotNull
    default CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(
            int transactionCount
    ) {
        return retrieveTransactionHistory(transactionCount, Instant.EPOCH, Instant.now());
    }

    /**
     * Request a listing of all member players of the account.
     *
     * @return a collection of member {@link UUID unique-ids}, members
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Collection<UUID>> retrieveMemberIds();

    /**
     * Check if the specified user is a member of the account.
     *
     * <p>A member is any player with at least one allowed permission.
     *
     * @param player the {@link UUID} of the potential member
     * @return whether the specified user is a member
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Boolean> isMember(@NotNull UUID player);

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
     * @return whether the permissions of the member were adjusted
     * @since v1.0.0
     */
    @NotNull
    default CompletableFuture<Boolean> setPermissions(
            @NotNull UUID player,
            @NotNull TriState permissionValue,
            @NotNull AccountPermission @NotNull ... permissions
    ) {
        return setPermissions(
                player,
                Arrays.stream(permissions).collect(Collectors.toMap(k -> k, k -> permissionValue))
        );
    }

    /**
     * Modifies the permissions, specified in the inputted {@code permissionMap} for the
     * specified {@link UUID} {@code player}. Just a reminder: a member is any player with at
     * least one allowed permission.
     *
     * @param player         the player id you want to modify the permissions of
     * @param permissionsMap the permissions to modify
     * @return whether the permissions of the member were adjusted
     */
    @NotNull CompletableFuture<Boolean> setPermissions(
            @NotNull UUID player, @NotNull Map<AccountPermission, TriState> permissionsMap
    );

    /**
     * Request the {@link AccountPermission AccountPermissions} for the specified {@link UUID}
     * {@code player}.
     *
     * @param player the player {@link UUID} to get the permissions for
     * @return an immutable map of permissions and their values for the specified member.
     * @since v1.0.0
     */
    @NotNull CompletableFuture<Map<AccountPermission, TriState>> retrievePermissions(@NotNull UUID player);

    /**
     * Returns a nested {@link Map}, with the permissions of each account member.
     *
     * @return a map of member id as a key and permissions map as value.
     * @since v2.0.0
     */
    @NotNull CompletableFuture<Map<UUID, Map<AccountPermission, TriState>>> retrievePermissionsMap();

    /**
     * Checks whether given player has the given permissions on this account.
     *
     * <p>Just a reminder: a member is any player with at least one allowed permission.
     *
     * @param player      the {@link UUID} of the player to check if they have the permission
     * @param permissions the permissions to check
     * @return a {@link TriState} value, indicating the permission(s) value(s) for the specified
     *         permission(s). {@link TriState#TRUE} represents that the specified member has (all) the
     *         specified permission(s), {@link TriState#FALSE} explicitly says that the specified member
     *         does not have (all) the specified permission(s), whilst {@link TriState#UNSPECIFIED}
     *         represents that there is/are no value(s) held for the specified permission(s).
     * @see AccountPermission
     * @since v1.0.0
     */
    @NotNull CompletableFuture<TriState> hasPermissions(
            @NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions
    );

}
