/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account.accessor;

import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;

/**
 * This is a buffer interface designed to instantiate accessors for
 * {@link PlayerAccount player accounts} and
 * {@link NonPlayerAccount non-player accounts}, through the
 * {@link PlayerAccountAccessor} and
 * {@link NonPlayerAccountAccessor} classes respectively.
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
public interface AccountAccessor {

    /**
     * Creates a new instance of a {@link PlayerAccountAccessor player account accesor}
     *
     * @return player account accessor
     */
    @NotNull PlayerAccountAccessor player();

    /**
     * Creates a new instance of a {@link NonPlayerAccountAccessor non-player account accessor}
     *
     * @return non player account accessor
     */
    @NotNull NonPlayerAccountAccessor nonPlayer();

}
