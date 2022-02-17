/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Optional;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EconomyHook implements TreasuryPAPIHook {

    private EconomyProvider provider;
    private final DecimalFormat format = new DecimalFormat("#,###");
    private final String k, m, b, t, q;
    private final Baltop baltop;

    public EconomyHook(PAPIExpansion expansion) {
        this.baltop = new Baltop(
                expansion.getBoolean("baltop.enabled", false),
                expansion.getInt("baltop.cache_size", 100),
                expansion.getInt("baltop.cache_delay", 30)
        );
        this.k = expansion.getString("formatting.thousands", "k");
        this.m = expansion.getString("formatting.millions", "m");
        this.b = expansion.getString("formatting.billions", "b");
        this.t = expansion.getString("formatting.trillions", "t");
        this.q = expansion.getString("formatting.quadrillions", "q");
    }

    @Override
    public boolean setup() {
        Optional<Service<EconomyProvider>> serviceOpt = ServiceRegistry.INSTANCE.serviceFor(
                EconomyProvider.class);
        if (serviceOpt.isPresent()) {
            provider = serviceOpt.get().get();
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
        // todo: put baltop here
        if (player == null) {
            return "";
        }
        if (provider != null) {
            if (param.contains("balance")) {
                String currencyId;
                if (param.startsWith("balance_") && (!param.contains("fixed") && !param.contains(
                        "formatted") && !param.contains("commas"))) {
                    currencyId = param.replace("balance_", "");
                } else {
                    currencyId = null;
                }
                Currency currency;
                int precision;
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
        }
        return null;
    }

    private String fixMoney(BigDecimal decimal, Currency currency, Locale locale, int precision) {
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
