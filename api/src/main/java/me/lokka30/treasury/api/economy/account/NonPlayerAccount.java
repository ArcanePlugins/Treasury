/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import me.lokka30.treasury.api.common.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * This is used to differentiate between a {@link PlayerAccount player account} and an
 * {@link Account account} that is not associated with a player.
 *
 * @author lokka30, MrNemo64, MrIvanPlays
 * @see Account
 * @since v1.0.0
 */
public interface NonPlayerAccount extends Account {

    /**
     * Returns the {@link NamespacedKey} identifier of this {@code NonPlayerAccount}
     *
     * @return identifier
     * @see NamespacedKey
     * @since 2.0.0
     */
    @NotNull NamespacedKey getIdentifier();

}
