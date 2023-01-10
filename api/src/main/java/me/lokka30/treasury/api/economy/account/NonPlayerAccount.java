/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import me.lokka30.treasury.api.common.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a non-player account. A non-player account is an account that is not owned by a
 * player. Such account might be, for example, a bank account.
 *
 * @author lokka30, MrNemo64, MrIvanPlays
 * @see Account
 * @see NonPlayerAccount
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
