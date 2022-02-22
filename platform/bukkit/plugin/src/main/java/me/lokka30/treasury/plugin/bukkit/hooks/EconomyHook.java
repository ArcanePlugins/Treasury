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
    static final Pattern TOP_BALANCE = Pattern.compile(
            // All top balances start with "top balance"
            "^top_balance"
                    // Optional group "type": top balance formatting type
                    // This is always "fixed" "formatted" or "commas" - other words are a currency
                    + "(_(?<type>(fixed|formatted|commas))(?=(_|$)))?"
                    // Optional group "precision": number of decimal places
                    // Currently only used by type "formatted"
                    + "(_(?<precision>\\d+)dp(?=(_|$)))?"
                    // Optional group "rank": top balance ranking
                    + "(_(?<rank>\\d+)(?=(_|$)))?"
                    // Optional group "currency": currency ID
                    + "(_(?<currency>.*?))?");

    /*
     * Pattern matching the top player formats.
     *
     * For specific rank positions, the named group "rank" defines the 1-indexed ranking from
     * 1st to last.
     * For specific currencies, the named group "currency" defines the currency ID.
     *
     * Valid format examples:
     * "top_player": the 1st top player in the primary currency.
     * "top_player_4_dollars": 4th top player in the currency dollars.
     */
    static final Pattern TOP_PLAYER = Pattern.compile(
            "^top_player(_(?<rank>[0-9]+)(?=(_|$)))?(_(?<currency>.*))?");

    /*
     * Pattern matching various balance formats.
     * The balance placeholder is always bound to a player.
     *
     * For specific formatting types, the named group "type" defines expected behavior.
     * For specific currencies, the named group "currency" defines the currency ID.
     *
     * Valid format examples:
     * "balance": balance of the player in the primary currency
     * "balance_formatted_dollars": formatted balance of the player for dollars
     * "balance_formatted_1dp_libra": formatted balance of the player for libra formatted with
     * 1 decimal pace precision
     */
    static final Pattern BALANCE = Pattern.compile(
            // All balances start with "balance"
            "^balance"
                    // Optional group "type": balance formatting type
                    // This is always "fixed" "formatted" or "commas" - other words are a currency
                    + "(_(?<type>(fixed|formatted|commas))(?=(_|$)))?"
                    // Optional group "precision": number of decimal places
                    + "(_(?<precision>[0-9]+)dp(?=(_|$)))?"
                    // Optional group "currency": currency ID
                    + "(_(?<currency>.*?))?");

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
            this.baltop = new BalTop(expansion.getBoolean("baltop.enabled", false),
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

        // Delegate top player request.
        if (param.startsWith("top_player")) {
            return requestTopPlayer(param);
        }

        if (player == null) {
            return null;
        }

        if (param.startsWith("top_rank_")) {
            return baltop.getPositionAsString(param.replace("top_rank_", ""), player.getName());
        } else if (param.equalsIgnoreCase("top_rank")) {
            return baltop.getPositionAsString(provider.getPrimaryCurrencyId(), player.getName());
        }

        // Delegate balance request.
        if (param.startsWith("balance")) {
            return requestBalance(player, param);
        }
        return null;
    }

    private @Nullable String requestTopBalance(
            @Nullable OfflinePlayer player, @NotNull String param
    ) {
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
        int rank = parseInt(matcher.group("rank"), 1);
        String currencyId = getCurrencyId(matcher.group("currency"));
        BigDecimal balance = baltop.getTopBalance(currencyId, rank);

        if (balance == null) {
            return "0";
        }

        // Formatting mode "fixed" yields a raw number with no decimal places.
        if ("fixed".equals(type)) {
            return String.valueOf(balance.longValue());
        }

        // Formatting mode "formatted" yields a number using localizable multiples of 1000.
        if ("formatted".equals(type)) {
            Locale locale = Locale.ENGLISH;
            if (player != null && player.isOnline()) {
                locale = Locale.forLanguageTag(player.getPlayer().getLocale().replace('_', '-'));
            }
            int precision = parseInt(matcher.group("precision"), -1);
            Currency currency = provider.findCurrency(currencyId).orElse(provider.getPrimaryCurrency());
            return fixMoney(balance, currency, locale, precision);
        }

        if ("commas".equals(type)) {
            return format.format(balance);
        }

        // Unsupported type argument, don't fall through silently.
        if (type != null && !type.isEmpty()) {
            return null;
        }

        return String.valueOf(balance.longValue());
    }

    private @Nullable String requestTopPlayer(@NotNull String param) {
        // Exactly "top_player" - no need for parsing
        if (param.length() == 9) {
            return baltop.getTopPlayer(provider.getPrimaryCurrencyId(), 1);
        }

        Matcher matcher = TOP_PLAYER.matcher(param);
        if (!matcher.matches()) {
            // Invalid format
            return null;
        }
        int rank = parseInt(matcher.group("rank"), 1);
        String currencyId = getCurrencyId(matcher.group("currency"));
        return baltop.getTopPlayer(currencyId, rank);
    }

    private @Nullable String requestBalance(@NotNull OfflinePlayer player, @NotNull String param) {
        Matcher matcher = BALANCE.matcher(param);
        if (!matcher.matches()) {
            // Invalid format
            return null;
        }
        String type = matcher.group("type");
        Currency currency = provider.findCurrency(getCurrencyId(matcher.group("currency"))).orElse(
                provider.getPrimaryCurrency());
        BigDecimal balance = EconomySubscriber
                .<Boolean>asFuture(s -> provider.hasPlayerAccount(
                        player.getUniqueId(),
                        s
                ))
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
                .thenCompose(account -> EconomySubscriber.<BigDecimal>asFuture(s -> account.retrieveBalance(currency,
                        s
                )))
                .join();

        if (type == null || type.equalsIgnoreCase("commas")) {
            return format.format(balance);
        }

        if (type.equalsIgnoreCase("fixed")) {
            return String.valueOf(balance.longValue());
        }

        if (type.equalsIgnoreCase("formatted")) {
            int precision = parseInt(matcher.group("precision"), -1);
            Locale locale = player.isOnline() ? Locale.forLanguageTag(player
                    .getPlayer()
                    .getLocale()
                    .replace('_', '-')) : Locale.ENGLISH;
            return fixMoney(balance, currency, locale, precision);
        }

        return null;
    }

    private @NotNull String getCurrencyId(@Nullable String currencyId) {
        if (currencyId == null || currencyId.isEmpty()) {
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
            String format = currency.format(BigDecimal.valueOf(val / 1000000000L),
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
            String format = currency.format(BigDecimal.valueOf(val / 1000000000000L),
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
            String format = currency.format(BigDecimal.valueOf(val / 1000000000000000L),
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
