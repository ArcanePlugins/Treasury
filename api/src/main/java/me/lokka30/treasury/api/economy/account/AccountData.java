/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data object, holding an account data.
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
public final class AccountData {

    /**
     * Creates a new {@link PlayerAccount player account} data.
     *
     * @param uniqueId the unique id of the player account owner
     * @return new account data instance
     * @since 2.0.0
     */
    @NotNull
    public static AccountData forPlayerAccount(@NotNull UUID uniqueId) {
        return new AccountData(
                true,
                Optional.empty(),
                Optional.of(Objects.requireNonNull(uniqueId, "uniqueId"))
        );
    }

    /**
     * Creates a new {@link NonPlayerAccount non player account} data.
     *
     * @param identifier the identifier of the non player account
     * @return new account data instance
     * @since 2.0.0
     */
    @NotNull
    public static AccountData forNonPlayerAccount(@NotNull String identifier) {
        return new AccountData(
                false,
                Optional.of(Objects.requireNonNull(identifier, "identifier")),
                Optional.empty()
        );
    }

    private final boolean player;
    private final Optional<String> identifier;
    private final Optional<UUID> uniqueId;

    private AccountData(boolean player, Optional<String> identifier, Optional<UUID> uniqueId) {
        this.player = player;
        this.identifier = identifier;
        this.uniqueId = uniqueId;
    }

    /**
     * Returns whether this account data holds a {@link PlayerAccount player account} data.
     *
     * @return whether held data is about player account
     * @since 2.0.0
     */
    public boolean isPlayerAccount() {
        return this.player;
    }

    /**
     * Returns the identifier of the account this account data holds, empty {@link Optional} if the
     * account data holds player account information.
     *
     * @return identifier or empty optional if the data is a player account data
     * @since 2.0.0
     */
    @NotNull
    public Optional<String> getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns the {@link UUID unique id} of the account this account data holds, empty
     * {@link Optional} if the account data held is
     *
     * @return unique id or empty optional if the data is a non player account data
     */
    @NotNull
    public Optional<UUID> getUniqueId() {
        return this.uniqueId;
    }

}
