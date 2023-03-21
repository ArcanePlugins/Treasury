/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

public class EconomyServiceImpl implements EconomyService {

    @Override
    public Currency defaultCurrency() {
        return null;
    }

    @Override
    public boolean hasAccount(final UUID uuid) {
        return false;
    }

    @Override
    public boolean hasAccount(final String identifier) {
        return false;
    }

    @Override
    public Optional<UniqueAccount> findOrCreateAccount(final UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> findOrCreateAccount(final String identifier) {
        return Optional.empty();
    }

    @Override
    public Stream<UniqueAccount> streamUniqueAccounts() {
        return null;
    }

    @Override
    public Collection<UniqueAccount> uniqueAccounts() {
        return null;
    }

    @Override
    public Stream<VirtualAccount> streamVirtualAccounts() {
        return null;
    }

    @Override
    public Collection<VirtualAccount> virtualAccounts() {
        return null;
    }

    @Override
    public AccountDeletionResultType deleteAccount(final UUID uuid) {
        return null;
    }

    @Override
    public AccountDeletionResultType deleteAccount(final String identifier) {
        return null;
    }

}
