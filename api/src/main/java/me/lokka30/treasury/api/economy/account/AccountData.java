/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Objects of this class represent the identity of an Account, which may or may not exist.
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
public final class AccountData {

    /**
     * Creates an {@link AccountData} object representing the identity of a {@link PlayerAccount player account}.
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
     * Creates an {@link AccountData} object representing the identity of a {@link NonPlayerAccount non-player account}.
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

    private AccountData(
            boolean player, @NotNull Optional<String> identifier, @NotNull Optional<UUID> uniqueId
    ) {
        this.player = player;
        this.identifier = identifier;
        this.uniqueId = uniqueId;
    }

    /**
     * Returns whether this account data holds a {@link PlayerAccount player account} or {link NonPlayerAccount non-player account} data.
     *
     * @return whether held data references a player account
     * @since 2.0.0
     */
    public boolean isPlayerAccount() {
        return this.player;
    }

    /**
     * If the current {@link AccountData} object represents a
     * {@link NonPlayerAccount non-player account}, this method
     * will return the identifier of the account. Otherwise, an empty
     * {@link Optional} will be returned.
     *
     * @return identifier or empty optional if the data is a player account data
     * @since 2.0.0
     */
    @NotNull
    public Optional<String> getIdentifier() {
        return this.identifier;
    }

    /**
     * If the current {@link AccountData} object represents a
     * {@link PlayerAccount player account}, this method
     * will return the {@link UUID} of the account. Otherwise, an
     * empty {@link Optional} will be returned.
     *
     * @return unique id or empty optional if the data is a non player account data
     */
    @NotNull
    public Optional<UUID> getUniqueId() {
        return this.uniqueId;
    }

}
