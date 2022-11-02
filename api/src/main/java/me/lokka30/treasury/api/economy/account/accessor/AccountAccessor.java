/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account.accessor;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a buffer interface, which shall create new instances of either the
 * {@link PlayerAccountAccessor player account accessor} or the {@link NonPlayerAccountAccessor non
 * player account accessor}.
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
     * Creates a new instance of a {@link NonPlayerAccountAccessor non player account accessor}
     *
     * @return non player account accessor
     */
    @NotNull NonPlayerAccountAccessor nonPlayer();

}