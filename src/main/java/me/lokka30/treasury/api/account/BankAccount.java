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

package me.lokka30.treasury.api.account;

import me.lokka30.treasury.api.currency.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface BankAccount {

    UUID getUniqueId();

    BigDecimal getBalance(Currency currency);

    void setBalance(BigDecimal amount, Currency currency);

    void withdrawBalance(BigDecimal amount, Currency currency);

    void depositBalance(BigDecimal amount, Currency currency);

    default void resetBalance(String worldName, Currency currency) {
        setBalance(BigDecimal.ZERO, currency);
    }

    default boolean canAfford(BigDecimal amount, String worldName, Currency currency) {
        return getBalance(currency).compareTo(amount) >= 0;
    }

    void deleteAccount();

    UUID getOwningPlayerId();

    default boolean isBankOwner(UUID uuid) {
        return getOwningPlayerId() == uuid;
    }

    List<UUID> getBankMembersIds();

    default boolean isBankMember(UUID uuid) {
        return getBankMembersIds().contains(uuid);
    }
}
