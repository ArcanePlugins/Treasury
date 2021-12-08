/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.api.economy.currency;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a manager of {@link Currency currencies}.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public enum CurrencyManager {
    INSTANCE;

    private Map<String, UUID> BY_NAME = new ConcurrentHashMap<>();
    private Map<Character, UUID> BY_CHAR = new ConcurrentHashMap<>();
    private Map<UUID, Currency> BY_ID = new ConcurrentHashMap<>();

    /**
     * Registers a new {@link Currency}.
     *
     * @param currency the currency you want to register
     * @throws IllegalArgumentException if the currency is already registered in at least one of the 3 types of identification.
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public void registerCurrency(@NotNull Currency currency) {
        Objects.requireNonNull(currency, "currency");

        if (currency.getCurrencyCharacter() != null) {
            if (hasCurrency(currency.getCurrencyCharacter())) {
                throw new IllegalArgumentException(
                        "Currency already registered with currency char '" + currency.getCurrencyCharacter() + "'"
                );
            }
        }

        boolean foundRegistration = false;
        String name = "";
        for (String acceptableName : currency.getCurrencyNames()) {
            if (hasCurrency(acceptableName)) {
                foundRegistration = true;
                name = acceptableName;
                break;
            }
        }
        if (foundRegistration) {
            throw new IllegalArgumentException("Currency already registered with currency name '" + name + "'");
        }

        if (hasCurrency(currency.getCurrencyId())) {
            throw new IllegalArgumentException("Currency already registered with currency id '" + currency.getCurrencyId() + "'");
        }

        BY_ID.put(currency.getCurrencyId(), currency);
        for (String acceptableName : currency.getCurrencyNames()) {
            BY_NAME.put(acceptableName, currency.getCurrencyId());
        }
        if (currency.getCurrencyCharacter() != null) {
            BY_CHAR.put(currency.getCurrencyCharacter(), currency.getCurrencyId());
        }
    }

    /**
     * @param currencies currencies to register
     * @see #registerCurrency(Currency)
     */
    public void registerCurrencies(@NotNull Currency @NotNull ... currencies) {
        Objects.requireNonNull(currencies, "currencies");
        for (Currency currency : currencies) {
            registerCurrency(currency);
        }
    }

    /**
     * @param currencies currencies to register
     * @see #registerCurrency(Currency)
     */
    public void registerCurrencies(@NotNull Iterable<@NotNull Currency> currencies) {
        Objects.requireNonNull(currencies, "currencies");
        for (Currency currency : currencies) {
            registerCurrency(currency);
        }
    }

    /**
     * Unregisters the specified {@link Currency} {@code currency} if it has been registered.
     *
     * @param currency the currency you want unregistered
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public void unregisterCurrency(@NotNull Currency currency) {
        Objects.requireNonNull(currency, "currency");
        BY_ID.remove(currency.getCurrencyId());
        for (String acceptableName : currency.getCurrencyNames()) {
            BY_NAME.remove(acceptableName);
        }
        if (currency.getCurrencyCharacter() != null) {
            BY_CHAR.remove(currency.getCurrencyCharacter());
        }
    }

    /**
     * @param currencies the currencies you want unregistered
     * @see #unregisterCurrency(Currency)
     */
    public void unregisterCurrencies(@NotNull Currency @NotNull ... currencies) {
        Objects.requireNonNull(currencies, "currencies");
        for (Currency currency : currencies) {
            unregisterCurrency(currency);
        }
    }

    /**
     * @param currencies the currencies you want unregistered
     * @see #unregisterCurrency(Currency)
     */
    public void unregisterCurrencies(@NotNull Iterable<@NotNull Currency> currencies) {
        Objects.requireNonNull(currencies, "currencies");
        for (Currency currency : currencies) {
            unregisterCurrency(currency);
        }
    }

    /**
     * Unregisters the {@link Currency} which has been registered with the specified {@link UUID} {@code currencyId} if
     * it is present.
     *
     * @param currencyId the id of the currency you want unregistered
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public void unregisterCurrency(@NotNull UUID currencyId) {
        getCurrency(currencyId).ifPresent(this::unregisterCurrency);
    }

    /**
     * @param currencyIds currency ids to unregister
     * @see #unregisterCurrency(UUID)
     */
    public void unregisterCurrencies(@NotNull UUID @NotNull ... currencyIds) {
        Objects.requireNonNull(currencyIds, "currencyIds");
        for (UUID currencyId : currencyIds) {
            unregisterCurrency(currencyId);
        }
    }

    /**
     * @param currencyIds currency ids to unregister
     * @see #unregisterCurrency(UUID)
     */
    public void unregisterCurrenciesById(@NotNull Iterable<@NotNull UUID> currencyIds) {
        Objects.requireNonNull(currencyIds, "currencyIds");
        for (UUID currencyId : currencyIds) {
            unregisterCurrency(currencyId);
        }
    }

    /**
     * Returns whether there is a {@link Currency} registered with the specified {@link UUID} {@code currencyId}.
     *
     * @param currencyId currency id you want to check if busy
     * @return boolean value
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public boolean hasCurrency(@NotNull UUID currencyId) {
        Objects.requireNonNull(currencyId, "currencyId");
        return BY_ID.containsKey(currencyId);
    }

    /**
     * Returns whether there is a {@link Currency} registered with the specified {@link String} {@code currencyName}.
     *
     * @param currencyName currency name to check if busy
     * @return boolean value
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public boolean hasCurrency(@NotNull String currencyName) {
        Objects.requireNonNull(currencyName, "currencyName");
        return BY_NAME.containsKey(currencyName);
    }

    /**
     * Returns whether there is a {@link Currency} registered with the specified {@link Character} {@code currencyCharacter}.
     *
     * @param currencyCharacter currency character to check if busy
     * @return boolean value
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public boolean hasCurrency(@NotNull Character currencyCharacter) {
        Objects.requireNonNull(currencyCharacter, "currencyCharacter");
        return BY_CHAR.containsKey(currencyCharacter);
    }

    /**
     * Returns the {@link Currency} bound to the specified {@link UUID} {@code currencyId}, if present.
     *
     * @param currencyId currency id you want the currency of
     * @return currency if present, empty optional otherwise
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public Optional<Currency> getCurrency(@NotNull UUID currencyId) {
        Objects.requireNonNull(currencyId, "currencyId");
        return Optional.ofNullable(BY_ID.get(currencyId));
    }

    /**
     * Returns the {@link Currency} bound to the specified {@link String} {@code currencyName}, if present.
     *
     * @param currencyName the name of the currency needed
     * @return currency if present, empty optional otherwise
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public Optional<Currency> getCurrency(@NotNull String currencyName) {
        if (!hasCurrency(currencyName)) {
            return Optional.empty();
        }
        UUID currencyId = null;
        for (String key : BY_NAME.keySet()) {
            if (key.equalsIgnoreCase(currencyName)) {
                currencyId = BY_NAME.get(key);
                break;
            }
        }
        if (currencyId != null) {
            return getCurrency(currencyId);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the {@link Currency} bound to the specified {@link Character} {@code currencyCharacter}, if present.
     *
     * @param currencyCharacter the character of the currency needed
     * @return currency if present, empty optional otherwise
     * @author MrIvanPlays
     * @since v1.0.0
     */
    public Optional<Currency> getCurrency(@NotNull Character currencyCharacter) {
        if (!hasCurrency(currencyCharacter)) {
            return Optional.empty();
        }
        return Optional.of(BY_ID.get(BY_CHAR.get(currencyCharacter)));
    }

    /**
     * Converts the specified {@code value} from the {@link Currency} {@code from} to the  {@link Currency} {@code to} by
     * using their {@link Currency#getConversionCoefficient()}.
     *
     * @param from  the currency to convert from
     * @param to    the currency to convert to
     * @param value the value to convert
     * @return converted value
     * @throws IllegalArgumentException if one or both of the currencies are not registered
     * @author MrIvanPlays, MrNemo64
     * @since v1.0.0
     */
    public double convertCurrency(@NotNull Currency from, @NotNull Currency to, double value) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (!hasCurrency(from.getCurrencyId()) || !hasCurrency(to.getCurrencyId())) {
            throw new IllegalArgumentException("Specified currencies are unknown to the currency manager");
        }
        if (from.getConversionCoefficient() == 0 || to.getConversionCoefficient() == 0 || value == 0) {
            return 0;
        }
        // thanks MrNemo64 for the formula
        // see https://github.com/lokka30/Treasury/issues/49#issuecomment-986255592 for an explanation
        double generalAmount = value * from.getConversionCoefficient();
        return generalAmount / to.getConversionCoefficient();
    }

    /**
     * Converts the specified {@code value} from the {@link UUID} {@code fromId}, associated with a {@link Currency} to
     * the {@link UUID} {@code toId}, associated with a {@link Currency} by using their {@link Currency#getConversionCoefficient()}.
     *
     * @param fromId the currency id to convert from
     * @param toId   the currency id to convert to
     * @param value  the value to convert
     * @return converted value
     * @throws IllegalArgumentException if one or both of the currencies are not registered
     * @author MrNemo64, MrIvanPlays
     * @since v1.0.0
     */
    public double convertCurrency(@NotNull UUID fromId, @NotNull UUID toId, double value) {
        if (value == 0) {
            return 0;
        }
        Optional<Currency> fromCurrency = getCurrency(fromId);
        Optional<Currency> toCurrency = getCurrency(toId);
        if (!fromCurrency.isPresent() || !toCurrency.isPresent()) {
            throw new IllegalArgumentException(
                    "Specified currency id(s) is/are not registered with any currency in the currency manager."
            );
        }
        return convertCurrency(fromCurrency.get(), toCurrency.get(), value);
    }

    /**
     * Tries to extract the {@link Currency} and the value encapsulated into the specified {@code input}.
     *
     * @param input the string to parse.
     * @return parse result
     * @author MrIvanPlays, MrNemo64
     * @since v1.0.0
     */
    @NotNull
    public CurrencyParseResult parseCurrencyAndValue(@NotNull String input) {
        Objects.requireNonNull(input, "input");
        StringBuilder currency = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean hadSeparator = false;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (!Character.isDigit(c) && !isSeparator(c)) {
                currency.append(c);
            } else if (Character.isDigit(c)) {
                value.append(c);
            } else if (isSeparator(c)) {
                boolean nowChanged = false;
                if (!hadSeparator) {
                    hadSeparator = true;
                    nowChanged = true;
                }
                if (!nowChanged) {
                    value = new StringBuilder();
                    break;
                }
                value.append(c);
            }
        }
        Optional<Currency> currencyOpt;
        if (currency.length() == 1) {
            currencyOpt = getCurrency(currency.charAt(0));
        } else {
            currencyOpt = getCurrency(currency.toString());
        }

        if (!currencyOpt.isPresent()) {
            return CurrencyParseResult.failure(CurrencyParseResult.State.UNKNOWN_CURRENCY);
        }
        if (value.length() == 0) {
            return CurrencyParseResult.failure(CurrencyParseResult.State.INVALID_VALUE);
        }

        double val;
        try {
            val = Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return CurrencyParseResult.failure(CurrencyParseResult.State.INVALID_VALUE);
        }
        return CurrencyParseResult.success(val, currencyOpt);
    }

    private boolean isSeparator(char c) {
        return c == '.' || c == ',' || c == '\'';
    }
}
