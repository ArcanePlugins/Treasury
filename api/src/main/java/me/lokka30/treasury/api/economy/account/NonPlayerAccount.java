/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.account;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.misc.TriState;
import org.jetbrains.annotations.NotNull;

/**
 * This is used to differentiate between a {@link PlayerAccount player account} and an
 * {@link Account account} that is not associated with a player.
 *
 * @author lokka30, MrNemo64
 * @see Account
 * @since {@link me.lokka30.treasury.api.economy.misc.EconomyAPIVersion#v1_0 v1.0}
 */
public interface NonPlayerAccount extends Account {
}
