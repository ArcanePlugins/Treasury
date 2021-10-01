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

package me.lokka30.treasury.api.exception;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author lokka30
 * @since v1.0.0
 * @see me.lokka30.treasury.api.EconomyProvider#hasPlayerAccount(UUID)
 * @see me.lokka30.treasury.api.EconomyProvider#hasNonPlayerAccount(UUID)
 * @see me.lokka30.treasury.api.EconomyProvider#hasBankAccount(UUID)
 * This Exception is thrown when a plugin attempts to
 * create an Account of specified UUID, but it already
 * exists. Plugins should check 'has...Account' before
 * attempting to create accounts.
 */
@SuppressWarnings("unused")
public class AccountAlreadyExistsException extends Exception {

    @NotNull private final UUID uuid;
    public AccountAlreadyExistsException(@NotNull final UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public UUID getUuid() { return uuid; }

    @Override
    public String getMessage() {
        return "The account of UUID '" + uuid + "' already exists.";
    }
}
