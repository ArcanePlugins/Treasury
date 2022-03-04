package me.lokka30.treasury.api.economy.events;

import me.lokka30.treasury.api.common.event.EventBus;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.events.account.EventCallingAccount;
import me.lokka30.treasury.api.economy.events.account.EventCallingPlayerAccount;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class EventEconomyProvider implements EconomyProvider {

    private final EconomyProvider originalProvider;
    private final EventBus eventBus;

    public EventEconomyProvider(final EconomyProvider originalProvider, EventBus eventBus) {
        this.originalProvider = originalProvider;
        this.eventBus = eventBus;
    }


    @Override
    public @NotNull EconomyAPIVersion getSupportedAPIVersion() {
        return originalProvider.getSupportedAPIVersion();
    }

    @Override
    public @NotNull Set<OptionalEconomyApiFeature> getSupportedOptionalEconomyApiFeatures() {
        return originalProvider.getSupportedOptionalEconomyApiFeatures();
    }

    @Override
    public void hasPlayerAccount(
            @NotNull final UUID accountId,
            @NotNull final EconomySubscriber<Boolean> subscription
    ) {
        originalProvider.hasPlayerAccount(accountId, subscription);
    }

    @Override
    public void retrievePlayerAccount(
            @NotNull final UUID accountId,
            @NotNull final EconomySubscriber<PlayerAccount> subscription
    ) {
        originalProvider.retrievePlayerAccount(accountId, wrapPlayerAccount(subscription));
    }

    @Override
    public void createPlayerAccount(
            @NotNull final UUID accountId,
            @NotNull final EconomySubscriber<PlayerAccount> subscription
    ) {
        originalProvider.createPlayerAccount(accountId, wrapPlayerAccount(subscription));
    }

    @Override
    public void retrievePlayerAccountIds(@NotNull final EconomySubscriber<Collection<UUID>> subscription) {
        originalProvider.retrievePlayerAccountIds(subscription);
    }

    @Override
    public void hasAccount(
            @NotNull final String identifier,
            @NotNull final EconomySubscriber<Boolean> subscription
    ) {
        originalProvider.hasAccount(identifier, subscription);
    }

    @Override
    public void retrieveAccount(
            @NotNull final String identifier,
            @NotNull final EconomySubscriber<Account> subscription
    ) {
        originalProvider.retrieveAccount(identifier, wrapAccount(subscription));
    }

    @Override
    public void createAccount(
            @Nullable final String name,
            @NotNull final String identifier,
            @NotNull final EconomySubscriber<Account> subscription
    ) {
        originalProvider.createAccount(identifier, wrapAccount(subscription));
    }

    @Override
    public void retrieveAccountIds(@NotNull final EconomySubscriber<Collection<String>> subscription) {
        originalProvider.retrieveAccountIds(subscription);
    }

    @Override
    public void retrieveNonPlayerAccountIds(@NotNull final EconomySubscriber<Collection<String>> subscription) {
        originalProvider.retrieveNonPlayerAccountIds(subscription);
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        return originalProvider.getPrimaryCurrency();
    }

    @Override
    public Optional<Currency> findCurrency(@NotNull final String identifier) {
        return originalProvider.findCurrency(identifier);
    }

    @Override
    public Set<Currency> getCurrencies() {
        return originalProvider.getCurrencies();
    }

    @Override
    public void registerCurrency(
            @NotNull final Currency currency,
            @NotNull final EconomySubscriber<Boolean> subscription
    ) {
        originalProvider.registerCurrency(currency, subscription);
    }

    private EconomySubscriber<PlayerAccount> wrapPlayerAccount(EconomySubscriber<PlayerAccount> original) {
        return new EconomySubscriber<PlayerAccount>() {
            @Override
            public void succeed(@NotNull final PlayerAccount playerAccount) {
                original.succeed(new EventCallingPlayerAccount(playerAccount, eventBus));
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                original.fail(exception);
            }
        };
    }

    private EconomySubscriber<Account> wrapAccount(EconomySubscriber<Account> original) {
        return new EconomySubscriber<Account>() {
            @Override
            public void succeed(@NotNull final Account account) {
                original.succeed(new EventCallingAccount<>(account, eventBus));
            }

            @Override
            public void fail(@NotNull final EconomyException exception) {
                original.fail(exception);
            }
        };
    }

}
