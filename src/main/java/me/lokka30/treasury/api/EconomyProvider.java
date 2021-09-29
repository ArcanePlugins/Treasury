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

package me.lokka30.treasury.api;

import com.sun.source.util.Plugin;
import me.lokka30.treasury.api.account.BankAccount;
import me.lokka30.treasury.api.account.NonPlayerAccount;
import me.lokka30.treasury.api.account.PlayerAccount;
import me.lokka30.treasury.api.currency.Currency;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface EconomyProvider {

    Plugin getProvider();

    short getSupportedAPIVersion();

    boolean hasNonPlayerAccountSupport();

    boolean hasBankAccountSupport();

    boolean hasPerWorldBalanceSupport();

    boolean hasTransactionEventSupport();

    boolean hasPlayerAccount(UUID accountId);

    PlayerAccount getPlayerAccount(UUID accountId);

    void createPlayerAccount(UUID accountId);

    List<UUID> getPlayerAccountIds();

    boolean hasNonPlayerAccount(UUID accountId);

    NonPlayerAccount getNonPlayerAccount(UUID accountId);

    void createNonPlayerAccount(UUID accountId);

    List<UUID> getNonPlayerAccountIds();

    boolean hasBankAccount(UUID accountId);

    BankAccount getBankAccount(UUID accountId);

    void createBankAccount(UUID accountId, UUID owningPlayerId);

    List<UUID> getBankAccountIds();

    List<String> getCurrencyIds();

    Currency getCurrency(String currencyId);

    Currency getPrimaryCurrency();

}
