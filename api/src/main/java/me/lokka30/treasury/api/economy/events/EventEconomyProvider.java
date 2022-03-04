package me.lokka30.treasury.api.economy.events;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class EventEconomyProvider implements EconomyProvider {

    private final EconomyProvider originalProvider;

    public EventEconomyProvider(final EconomyProvider originalProvider) {
        this.originalProvider = originalProvider;
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
        // todo return wrapped
        originalProvider.retrievePlayerAccount(accountId, subscription);
    }

    @Override
    public void createPlayerAccount(
            @NotNull final UUID accountId,
            @NotNull final EconomySubscriber<PlayerAccount> subscription
    ) {
        // todo return wrapped
        originalProvider.createPlayerAccount(accountId, subscription);
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
        // todo return wrapped
        originalProvider.retrieveAccount(identifier, subscription);
    }

    @Override
    public void createAccount(
            @Nullable final String name,
            @NotNull final String identifier,
            @NotNull final EconomySubscriber<Account> subscription
    ) {
        // todo return wrapped
        originalProvider.createAccount(identifier, subscription);
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

}
