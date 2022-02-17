/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EconomyHook implements TreasuryPAPIHook {

    private EconomyProvider provider;
    private final DecimalFormat format = new DecimalFormat("#,###");

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
        if (provider != null && player != null) {
            if (param.equalsIgnoreCase("balance")) {
                return format.format(EconomySubscriber
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
                                provider.getPrimaryCurrency(),
                                s
                        )))
                        .join()
                        .doubleValue());
            }
        }
        return null;
    }

}
