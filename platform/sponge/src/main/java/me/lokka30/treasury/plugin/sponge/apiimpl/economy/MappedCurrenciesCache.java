/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.lokka30.treasury.api.economy.EconomyProvider;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.api.service.economy.Currency;

public class MappedCurrenciesCache {

    private final EconomyProvider delegateProvider;
    private Map<Currency, String> identifiersMap;

    public MappedCurrenciesCache(
            final EconomyProvider delegateProvider
    ) {
        this.delegateProvider = delegateProvider;
        this.identifiersMap = new ConcurrentHashMap<>();
    }

    public static class MigrationResult {

        public enum State {
            JUST_CREATED,
            FOUND
        }

        private final State state;
        private final me.lokka30.treasury.api.economy.currency.Currency currency;

        public MigrationResult(
                final State state, final me.lokka30.treasury.api.economy.currency.Currency currency
        ) {
            this.state = state;
            this.currency = currency;
        }

        public State getState() {
            return this.state;
        }

        public me.lokka30.treasury.api.economy.currency.Currency getCurrency() {
            return currency;
        }

    }

    public MigrationResult migrateCurrency(
            Currency currency
    ) {
        if (currency instanceof SpongeCurrencyImpl) {
            return new MigrationResult(
                    MigrationResult.State.FOUND,
                    ((SpongeCurrencyImpl) currency).getDelegate()
            );
        }
        if (identifiersMap.containsKey(currency)) {
            return new MigrationResult(
                    MigrationResult.State.FOUND,
                    delegateProvider.findCurrency(identifiersMap.get(currency)).get()
            );
        }
        SpongeToTreasuryCurrencyImpl curr = new SpongeToTreasuryCurrencyImpl(
                getCurrencyIdentifier(
                        PlainTextComponentSerializer.plainText().serialize(currency.symbol()),
                        "treasury:sponge_migrated"
                ),
                currency
        );
        delegateProvider.registerCurrency(curr);
        identifiersMap.put(currency, curr.getIdentifier());
        return new MigrationResult(MigrationResult.State.JUST_CREATED, curr);
    }

    private String getCurrencyIdentifier(
            String currencySymbol, String currentProposal
    ) {
        if (delegateProvider.findCurrency(currentProposal).isPresent()) {
            return getCurrencyIdentifier(currencySymbol, currentProposal + currencySymbol);
        }
        return currentProposal;
    }

}
