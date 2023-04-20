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
import me.lokka30.treasury.plugin.sponge.util.SpongeUtil;
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
        SpongeUtil.checkMainThread("hasAccount", getCallerClassName());
        return getHandle().hasAccount(AccountData.forPlayerAccount(uuid)).join();
    }

    @Override
    public boolean hasAccount(final String identifier) {
        SpongeUtil.checkMainThread("hasAccount", getCallerClassName());
        return getHandle().hasAccount(AccountData.forNonPlayerAccount(NamespacedKey.fromString(
                identifier))).join();
    }

    @Override
    public Optional<UniqueAccount> findOrCreateAccount(final UUID uuid) {
        SpongeUtil.checkMainThread("findOrCreateAccount", getCallerClassName());
        return Optional.of(new UniqueAccountImpl(
                getHandle(),
                cache,
                getHandle().accountAccessor().player().withUniqueId(uuid).get().join()
        ));
    }

    @Override
    public Optional<Account> findOrCreateAccount(final String identifier) {
        SpongeUtil.checkMainThread("findOrCreateAccount", getCallerClassName());
        return Optional.of(new VirtualAccountImpl(
                getHandle(),
                cache,
                getHandle().accountAccessor().nonPlayer().withIdentifier(NamespacedKey.fromString(
                        identifier)).get().join()
        ));
    }

    @Override
    public Stream<UniqueAccount> streamUniqueAccounts() {
        SpongeUtil.checkMainThread("streamUniqueAccounts", getCallerClassName());
        return uniqueAccounts().stream();
    }

    @Override
    public Collection<UniqueAccount> uniqueAccounts() {
        SpongeUtil.checkMainThread("uniqueAccounts", getCallerClassName());
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
        SpongeUtil.checkMainThread("streamVirtualAccounts", getCallerClassName());
        return virtualAccounts().stream();
    }

    @Override
    public Collection<VirtualAccount> virtualAccounts() {
        SpongeUtil.checkMainThread("virtualAccounts", getCallerClassName());
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
        SpongeUtil.checkMainThread("deleteAccount", getCallerClassName());
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
        SpongeUtil.checkMainThread("deleteAccount", getCallerClassName());
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

    private String getCallerClassName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i = 1; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            if (!element.getClassName().equals(NamespacedKey.class.getName()) && element
                    .getClassName()
                    .indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = element.getClassName();
                } else if (!callerClassName.equals(element.getClassName())) {
                    return callerClassName;
                }
            }
        }
        return null;
    }

}
