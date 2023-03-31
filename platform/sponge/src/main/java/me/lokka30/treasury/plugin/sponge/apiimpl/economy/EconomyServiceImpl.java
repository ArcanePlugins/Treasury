/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.AccountDeletionResultTypes;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

public class EconomyServiceImpl implements EconomyService {

    private final MappedCurrenciesCache cache = new MappedCurrenciesCache(getHandle());

    @Override
    public Currency defaultCurrency() {
        return new SpongeCurrencyImpl(getHandle().getPrimaryCurrency());
    }

    @Override
    public boolean hasAccount(final UUID uuid) {
        return getHandle().hasAccount(AccountData.forPlayerAccount(uuid)).join();
    }

    @Override
    public boolean hasAccount(final String identifier) {
        return getHandle().hasAccount(AccountData.forNonPlayerAccount(NamespacedKey.fromString(
                identifier))).join();
    }

    @Override
    public Optional<UniqueAccount> findOrCreateAccount(final UUID uuid) {
        return Optional.of(new UniqueAccountImpl(
                getHandle(),
                cache,
                getHandle().accountAccessor().player().withUniqueId(uuid).get().join()
        ));
    }

    @Override
    public Optional<Account> findOrCreateAccount(final String identifier) {
        return Optional.of(new VirtualAccountImpl(
                getHandle(),
                cache,
                getHandle().accountAccessor().nonPlayer().withIdentifier(NamespacedKey.fromString(
                        identifier)).get().join()
        ));
    }

    @Override
    public Stream<UniqueAccount> streamUniqueAccounts() {
        return uniqueAccounts().stream();
    }

    @Override
    public Collection<UniqueAccount> uniqueAccounts() {
        return getHandle().retrievePlayerAccountIds().thenCompose(uuids -> {
            Collection<CompletableFuture<PlayerAccount>> accounts = new ArrayList<>();
            for (UUID id : uuids) {
                accounts.add(getHandle().accountAccessor().player().withUniqueId(id).get());
            }
            return FutureHelper.mapJoinFilter(
                    s -> CompletableFuture.completedFuture(TriState.TRUE),
                    account -> (UniqueAccount) new UniqueAccountImpl(getHandle(), cache, account),
                    accounts
            );
        }).join();
    }

    @Override
    public Stream<VirtualAccount> streamVirtualAccounts() {
        return virtualAccounts().stream();
    }

    @Override
    public Collection<VirtualAccount> virtualAccounts() {
        return getHandle().retrieveNonPlayerAccountIds().thenCompose(ids -> {
            Collection<CompletableFuture<NonPlayerAccount>> accounts = new ArrayList<>();
            for (NamespacedKey id : ids) {
                accounts.add(getHandle().accountAccessor().nonPlayer().withIdentifier(id).get());
            }
            return FutureHelper.mapJoinFilter(
                    s -> CompletableFuture.completedFuture(TriState.TRUE),
                    account -> (VirtualAccount) new VirtualAccountImpl(getHandle(), cache, account),
                    accounts
            );
        }).join();
    }

    @Override
    public AccountDeletionResultType deleteAccount(final UUID uuid) {
        PlayerAccount account = getHandle()
                .accountAccessor()
                .player()
                .withUniqueId(uuid)
                .get()
                .join();
        boolean result = account.deleteAccount().join();
        return result
                ? AccountDeletionResultTypes.SUCCESS.get()
                : AccountDeletionResultTypes.FAILED.get();
    }

    @Override
    public AccountDeletionResultType deleteAccount(final String identifier) {
        NonPlayerAccount account = getHandle().accountAccessor().nonPlayer().withIdentifier(
                NamespacedKey.fromString(identifier)).get().join();
        boolean result = account.deleteAccount().join();
        return result
                ? AccountDeletionResultTypes.SUCCESS.get()
                : AccountDeletionResultTypes.FAILED.get();
    }

    private EconomyProvider getHandle() {
        EconomyProvider provider = EconomyServiceImplProvider.PROVIDER.get();
        if (provider == null) {
            throw new IllegalArgumentException(
                    "Too early usage of Economy API. Provider unavailable");
        }
        return EconomyServiceImplProvider.PROVIDER.get();
    }

}
