/*
 * Copyright (c) 2021 lokka30.
 * This code is part of Treasury, an Economy API for Minecraft servers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You have received a copy of the GNU Affero General Public License
 * with this program - please see the LICENSE.md file. Alternatively,
 * please visit the <https://www.gnu.org/licenses/> website.
 *
 * Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 */

package me.lokka30.treasury.api.economy.account;

/**
 * @author lokka30
 * @since v1.0.0
 * @see Account
 * A Non-Player Account is an Account which contains balances
 * for a subject which is not a Player. For example, if a plugin
 * wanted to store the balance of an entity through Treasury.
 * they must use the Non-Player Account functionality. To ensure
 * conflicts are near impossible, UUIDs are also used with
 * Non-Player Accounts, as with Player Accounts. The Non-Player
 * interface does not make any modifications to the Account
 * interface, it simply extends it with no new or overriden methods.
 */
@SuppressWarnings("unused")
public interface NonPlayerAccount extends Account { }
