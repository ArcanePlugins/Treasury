/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EconomyHook implements TreasuryPAPIHook {

    /*
     * Pattern matching various top balance formats.
     *
     * For specific formatting types, the named group "type" defines expected behavior.
     * For specific rank positions, the named group "rank" defines the 1-indexed ranking from
     * most to least.
     * For specific currencies, the named group "currency" defines the currency ID.
     *
     * Valid format examples:
     * "top_balance": balance of 1st ranked player for default currency
     * "top_balance_4_dollars": balance of 4th ranked player for dollars
     * "top_balance_formatted_2dp_1_euros": balance of 1st ranked player for euros formatted with
     * 2 decimal place precision
     */
    // TODO needs testing, specifically lookaheads. Lookaheads not strictly necessary but prevent
    //  bad formats falling through.
    static final Pattern TOP_BALANCE = Pattern.compile(
            // All top balances start with "top balance"
            "^top_balance"
                    // Optional group "type": top balance formatting type
                    + "(_(?<type>[a-z]+)(?=(_|$)))?"
                    // Optional group "precision": number of decimal places
                    // Currently only used by type "formatted"
                    + "(_(?<precision>\\d+)dp(?=(_|$)))?"
                    // Optional group "rank": top balance ranking
                    + "(_(?<rank>\\d+)(?=(_|$)))?"
                    // Optional group "currency": currency ID
                    + "(_(?<currency>.*))?");
    // TODO player stuff

    private EconomyProvider provider;
    private final DecimalFormat format = new DecimalFormat("#,###");
    private final PAPIExpansion expansion;
    private final TreasuryBukkit plugin;
    private final String k;
    private final String m;
    private final String b;
    private final String t;
    private final String q;
    private BalTop baltop;

    public EconomyHook(@NotNull PAPIExpansion expansion, @NotNull TreasuryBukkit plugin) {
        this.expansion = expansion;
        this.plugin = plugin;
        this.k = expansion.getString("formatting.thousands", "k");
        this.m = expansion.getString("formatting.millions", "M");
        this.b = expansion.getString("formatting.billions", "B");
        this.t = expansion.getString("formatting.trillions", "T");
        this.q = expansion.getString("formatting.quadrillions", "Q");
    }

    @Override
    public String getPrefix() {
        return "eco_";
    }

    @Override
    public boolean setup() {
        // Cancel existing baltop task.
        if (this.baltop != null) {
            try {
                this.baltop.cancel();
            } catch (IllegalStateException ignored) {
                // Ignore scheduler having failed to start task.
            }
        }

        Optional<Service<EconomyProvider>> serviceOpt = ServiceRegistry.INSTANCE.serviceFor(
                EconomyProvider.class);
        if (serviceOpt.isPresent()) {
            provider = serviceOpt.get().get();
            this.baltop = new BalTop(
                    expansion.getBoolean("baltop.enabled", false),
                    expansion.getInt("baltop.cache_size", 100),
                    expansion.getInt("baltop.cache_delay", 60),
                    provider
            );
            this.baltop.start(plugin);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canRegister() {
        return provider != null;
    }

    @Override
    public @Nullable String onRequest(
            @Nullable OfflinePlayer player, @NotNull String param
    ) {
        // If provider is not present, return quickly.
        if (provider == null) {
            return null;
        }

        // If baltop is disabled and this is a baltop request, return quickly.
        if (!baltop.isEnabled() && param.startsWith("top_")) {
            return (param.startsWith("top_balance")) ? "0" : "";
        }

        // Delegate top balance request.
        if (param.startsWith("top_balance")) {
            return requestTopBalance(player, param);
        }

        if (param.startsWith("top_player_")) {
            String contestant = param.replace("top_player_", "0");
            String currencyId;
            int position;
            if (contestant.isEmpty()) {
                currencyId = provider.getPrimaryCurrencyId();
                position = 1;
            } else {
                if (contestant.indexOf('_') == -1) {
                    currencyId = contestant;
                    position = 1;
                } else {
                    String[] split = contestant.split("_");
                    if (split.length == 1) {
                        currencyId = contestant;
                        position = 1;
                    } else if (split.length != 0) {
                        currencyId = split[0];
                        try {
                            position = Integer.parseInt(split[1]);
                        } catch (NumberFormatException e) {
                            position = 1;
                        }
                    } else {
                        currencyId = provider.getPrimaryCurrencyId();
                        position = 1;
                    }
                }
            }
            return baltop.getTopPlayer(currencyId, position);
        }

        if (player == null) {
            return "";
        }

        if (param.startsWith("top_rank_")) {
            return baltop.getPositionAsString(param.replace("top_rank_", ""), player.getName());
        } else if (param.equalsIgnoreCase("top_rank")) {
            return baltop.getPositionAsString(
                    provider.getPrimaryCurrencyId(),
                    player.getName()
            );
        }

        if (param.contains("balance")) {
            String currencyId;
            if (param.startsWith("balance_")) {
                currencyId = param.replace("balance_", "");
            } else {
                currencyId = null;
            }
            Currency currency;
            int precision;
            boolean precisionSetByPlaceholder = false;
            if (currencyId != null) {
                Optional<Currency> currencyOpt = EconomySubscriber.<Optional<Currency>>asFuture(
                        s -> provider.findCurrency(currencyId)).join();
                if (currencyOpt.isPresent()) {
                    currency = currencyOpt.get();
                    precision = 2;
                } else {
                    currency = provider.getPrimaryCurrency();
                    if (currencyId.endsWith("dp")) {
                        try {
                            precision = Integer.parseInt(currencyId.replace("dp", ""));
                            precisionSetByPlaceholder = true;
                        } catch (NumberFormatException e) {
                            precision = 2;
                        }
                    } else {
                        precision = 2;
                    }
                }
            } else {
                currency = provider.getPrimaryCurrency();
                precision = 2;
            }
            Locale locale = player.isOnline() ? Locale.forLanguageTag(player
                    .getPlayer()
                    .getLocale()) : Locale.ENGLISH;
            BigDecimal balance = EconomySubscriber
                    .<Boolean>asFuture(s -> provider.hasPlayerAccount(player.getUniqueId(), s))
                    .thenCompose(val -> {
                        if (val) {
                            return EconomySubscriber.<PlayerAccount>asFuture(s -> provider.retrievePlayerAccount(
                                    player.getUniqueId(),
                                    s
                            ));
                        } else {
                            return EconomySubscriber.<PlayerAccount>asFuture(s -> provider.createPlayerAccount(
                                    player.getUniqueId(),
                                    s
                            ));
                        }
                    })
                    .thenCompose(account -> EconomySubscriber.<BigDecimal>asFuture(s -> account.retrieveBalance(
                            currency,
                            s
                    )))
                    .join();

            if (precisionSetByPlaceholder) {
                return currency.format(balance, locale, precision);
            }

            if (param.equalsIgnoreCase("balance") || param.equalsIgnoreCase("balance_commas")) {
                return format.format(balance);
            }
            if (param.equalsIgnoreCase("balance_fixed")) {
                return String.valueOf(balance.longValue());
            }
            if (param.equalsIgnoreCase("balance_formatted")) {
                return fixMoney(balance, currency, locale, precision);
            }
        }
        return null;
    }

    // TODO maybe break up into smaller helper methods per-type
    private @Nullable String requestTopBalance(@Nullable OfflinePlayer player, @NotNull String param) {
        // Exactly "top_balance" - no parsing required.
        if (param.length() == 11) {
            BigDecimal topBalance = baltop.getTopBalance(provider.getPrimaryCurrencyId(), 0);
            if (topBalance == null) {
                return "0";
            }
            return topBalance.toString();
        }


        Matcher matcher = TOP_BALANCE.matcher(param);
        if (!matcher.matches()) {
            // Invalid format
            return null;
        }

        String type = matcher.group("type");
        int rank = parseInt(matcher.group("rank"), 0);
        String currencyId = getCurrencyId(matcher.group("currency"));
        BigDecimal balance = baltop.getTopBalance(currencyId, rank);

        // Formatting mode "fixed" yields a raw number with no decimal places.
        if ("fixed".equals(type)) {
            if (balance == null) {
                return "0";
            }
            return String.valueOf(balance.longValue());
        }

        // Formatting mode "formatted" yields a number using localizable multiples of 1000.
        if ("formatted".equals(type)) {
            if (balance == null) {
                return "0";
            }
            Locale locale = Locale.ENGLISH;
            if (player != null && player.isOnline()) {
                locale = Locale.forLanguageTag(player.getPlayer().getLocale().replace('_', '-'));
            }
            int precision = parseInt(matcher.group("precision"), -1);
            Currency currency;
            String specifiedCurrency = matcher.group("currency");
            if (specifiedCurrency == null || specifiedCurrency.isEmpty()) {
                currencyId = provider.getPrimaryCurrencyId();
                currency = provider.getPrimaryCurrency();
            } else {
                Optional<Currency> currencyOpt = provider.findCurrency(currencyId);
                if (currencyOpt.isPresent()) {
                    currency = currencyOpt.get();
                } else {
                    currency = provider.getPrimaryCurrency();
                    currencyId = provider.getPrimaryCurrencyId();
                }
            }
            BigDecimal topBalanceFormatted = baltop.getTopBalance(currencyId, rank);
            if (topBalanceFormatted == null) {
                return "0";
            } else {
                return fixMoney(topBalanceFormatted, currency, locale, precision);
            }
        }

        if ("commas".equals(type)) {
            BigDecimal topBalanceCommas = baltop.getTopBalance(currencyId, rank);
            if (topBalanceCommas == null) {
                return "0";
            } else {
                return format.format(topBalanceCommas);
            }
        }

        // Unsupported type argument, don't fall through silently.
        if (type != null && !type.isEmpty()) {
            return null;
        }

        BigDecimal topBalance = baltop.getTopBalance(currencyId, rank);
        if (topBalance == null) {
            return "0";
        } else {
            return String.valueOf(topBalance.doubleValue());
        }
    }

    private @NotNull String getCurrencyId(@Nullable String currencyId) {
        if (currencyId ==  null || currencyId.isEmpty()) {
            return provider.getPrimaryCurrencyId();
        }

        return currencyId;
    }

    private int parseInt(@Nullable String string, int defaultVal) {
        if (string == null || string.isEmpty()) {
            return defaultVal;
        }

        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return defaultVal;
        }
    }

    // TODO: support more multiples? Treasury does use BD, numbers go up.
    private String fixMoney(BigDecimal decimal, Currency currency, Locale locale, int precision) {
        if (precision < 0) {
            precision = currency.getPrecision();
        }

        double val = decimal.doubleValue();
        if (val < 1000) {
            return currency.format(decimal, locale, precision);
        }
        if (val < 1000000) {
            String format = currency.format(BigDecimal.valueOf(val / 1000), locale, precision);
            if (format.endsWith(k) || format.endsWith("k")) {
                return format;
            } else {
                return format + k;
            }
        }
        if (val < 1000000000) {
            String format = currency.format(BigDecimal.valueOf(val / 1000000), locale, precision);
            if (format.endsWith(m) || format.endsWith("M")) {
                return format;
            } else {
                return format + m;
            }
        }
        if (val < 1000000000000L) {
            String format = currency.format(
                    BigDecimal.valueOf(val / 1000000000L),
                    locale,
                    precision
            );
            if (format.endsWith(b) || format.endsWith("B")) {
                return format;
            } else {
                return format + b;
            }
        }
        if (val < 1000000000000000L) {
            String format = currency.format(
                    BigDecimal.valueOf(val / 1000000000000L),
                    locale,
                    precision
            );
            if (format.endsWith(t) || format.endsWith("T")) {
                return format;
            } else {
                return format + t;
            }
        }
        if (val < 1000000000000000000L) {
            String format = currency.format(
                    BigDecimal.valueOf(val / 1000000000000000L),
                    locale,
                    precision
            );
            if (format.endsWith(q) || format.endsWith("Q")) {
                return format;
            } else {
                return format + q;
            }
        }

        return String.valueOf((long) val);
    }

}
