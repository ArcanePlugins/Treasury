/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.BankAccount;
import me.lokka30.treasury.api.economy.account.BankAccountPermission;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;

/**
 * Implementors providing and managing economy data create a class
 * which implements this interface to be registered in
 * the specific platform they're implementing it for.
 *
 * @author lokka30, Jikoo, MrIvanPlays, NoahvdAa
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface EconomyProvider {

    /**
     * Get the version of the Treasury API the {@code EconomyProvider} is based on.
     *
     * <p>Warning: The Treasury API version is completely different to any other platform version,
     * such as the 'api-version' value in the Bukkit implementation's plugin.yml file.
     * <p>Warning: Do not use {@link EconomyAPIVersion#getCurrentAPIVersion()}, this is for internal Treasury use only.
     * <b>You must only use the constants provided.</b>
     *
     * @return the API version
     * @author lokka30, MrIvanPlays
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    EconomyAPIVersion getSupportedAPIVersion();

    /**
     * Check which optional Treasury Economy API features the Economy Provider supports.
     *
     * <p>There are a few features which Treasury allows Economy Providers to support
     * at their option. This is because certain features within Treasury may not be
     * suitable or appealing for some Economy Providers to implement. We try to enforce
     * as much of the API to be implemented as possible, however, few features can be
     * justified as being optional to implement.
     *
     * <p>The Economy Provider should return a Set of constants representing which of
     * Treasury's optional Economy API features are supported by the Economy Provider.
     * This set can be empty if none of the optional features are supported by the
     * Economy Provider.
     *
     * @return the set of optional supported features from the economy provider/
     * @author lokka30
     * @see OptionalEconomyApiFeature
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    Set<OptionalEconomyApiFeature> getSupportedOptionalEconomyApiFeatures();

    /**
     * Request whether a user has an associated {@link PlayerAccount}.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void hasPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request an existing {@link PlayerAccount} for a user.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrievePlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    /**
     * Request the creation of a {@link PlayerAccount} for a user.
     *
     * @param accountId    the {@link UUID} of the account owner
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void createPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription);

    /**
     * Request all {@link UUID UUIDs} with associated {@link PlayerAccount PlayerAccounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrievePlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request whether a {@link UUID} has an associated {@link BankAccount}.
     *
     * @param accountId    the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void hasBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription);

    /**
     * Request an existing {@link BankAccount} for a {@link UUID}.
     *
     * @param accountId    the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    /**
     * Request the creation of a {@link BankAccount} for a {@link UUID}.
     *
     * @param accountId    the {@code UUID} of the account
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void createBankAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<BankAccount> subscription);

    /**
     * Request all {@link UUID UUIDs} with associated {@link BankAccount BankAccounts}.
     *
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveBankAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription);

    /**
     * Request all {@link Account accounts} the given player is a member of.
     *
     * @param playerId     the player
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author MrNemo64
     * @see #retrieveAllAccountsIdPlayerIsMemberOf(UUID, EconomySubscriber)
     * @see #retrieveAllAccountsPlayerHasPermission(UUID, BankAccountPermission[], EconomySubscriber)
     * @see #retrieveAllAccountsIdPlayerHasPermission(UUID, BankAccountPermission[], EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveAllAccountsPlayerIsMemberOf(
            @NotNull UUID playerId,
            @NotNull EconomySubscriber<Collection<? extends Account>> subscription
    );

    /**
     * Request all {@link Account accounts ids} the given player is a member of.
     *
     * @param playerId     the player
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author MrNemo64
     * @see #retrieveAllAccountsPlayerIsMemberOf(UUID, EconomySubscriber)
     * @see #retrieveAllAccountsPlayerHasPermission(UUID, BankAccountPermission[], EconomySubscriber)
     * @see #retrieveAllAccountsIdPlayerHasPermission(UUID, BankAccountPermission[], EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void retrieveAllAccountsIdPlayerIsMemberOf(
            @NotNull UUID playerId,
            @NotNull EconomySubscriber<Collection<UUID>> subscription
    ) {
        retrieveAllAccountsPlayerIsMemberOf(playerId, new EconomySubscriber<Collection<? extends Account>>() {
            @Override
            public void succeed(@NotNull final Collection<? extends Account> accounts) {
                List<UUID> uuids = new ArrayList<>(accounts.size());
                accounts.forEach((a) -> uuids.add(a.getUniqueId()));
                subscription.succeed(uuids);
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    /**
     * Request all the {@link BankAccount bank accounts} where the given player has the given permissions.
     *
     * @param playerId     the player
     * @param permissions  the permissions that the given player has to have on the {@link BankAccount account}
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author MrNemo64
     * @see #retrieveAllAccountsPlayerIsMemberOf(UUID, EconomySubscriber)
     * @see #retrieveAllAccountsIdPlayerIsMemberOf(UUID, EconomySubscriber)
     * @see #retrieveAllAccountsIdPlayerHasPermission(UUID, BankAccountPermission[], EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    void retrieveAllAccountsPlayerHasPermission(
            @NotNull UUID playerId,
            @NotNull BankAccountPermission[] permissions,
            @NotNull EconomySubscriber<Collection<? extends BankAccount>> subscription
    );

    /**
     * Request all the {@link BankAccount bank accounts ids} where the given player has the given permissions.
     *
     * @param playerId     the player
     * @param permissions  the permissions that the given player has to have on the {@link BankAccount account}
     * @param subscription the {@link EconomySubscriber} accepting the resulting value
     * @author MrNemo64
     * @see #retrieveAllAccountsPlayerIsMemberOf(UUID, EconomySubscriber)
     * @see #retrieveAllAccountsIdPlayerIsMemberOf(UUID, EconomySubscriber)
     * @see #retrieveAllAccountsPlayerHasPermission(UUID, BankAccountPermission[], EconomySubscriber)
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    default void retrieveAllAccountsIdPlayerHasPermission(
            @NotNull UUID playerId,
            @NotNull BankAccountPermission[] permissions,
            @NotNull EconomySubscriber<Collection<UUID>> subscription
    ) {
        retrieveAllAccountsPlayerHasPermission(playerId, permissions, new EconomySubscriber<Collection<? extends BankAccount>>() {
            @Override
            public void succeed(@NotNull final Collection<? extends BankAccount> bankAccounts) {
                List<UUID> uuids = new ArrayList<>(bankAccounts.size());
                bankAccounts.forEach((ba) -> uuids.add(ba.getUniqueId()));
                subscription.succeed(uuids);
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                subscription.fail(exception);
            }
        });
    }

    /**
     * Get the primary or main {@link Currency} of the economy.
     *
     * @return the primary currency
     * @author Jikoo
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    Currency getPrimaryCurrency();

    /**
     * Get the {@link UUID} of the primary or main {@link Currency} of the economy.
     *
     * @return the {@code UUID} identifying the primary currency
     * @author lokka30
     * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
     */
    @NotNull
    default UUID getPrimaryCurrencyId() {
        return getPrimaryCurrency().getCurrencyId();
    }

}
