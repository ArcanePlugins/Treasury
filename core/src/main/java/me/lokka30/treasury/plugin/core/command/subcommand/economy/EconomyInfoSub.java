/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy;

import java.util.Locale;
import java.util.Optional;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

import static me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder.placeholder;

// "/treasury economy info"
public class EconomyInfoSub implements Subcommand {

    @Override
    public void execute(
            @NotNull final CommandSource sender,
            @NotNull final String label,
            final @NotNull String[] args
    ) {
        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury.economy.info")) {
            return;
        }

        if (args.length != 0) {
            sender.sendMessage(Message.of(
                    MessageKey.ECONOMY_INFO_INVALID_USAGE,
                    MessagePlaceholder.placeholder("label", label)
            ));
            return;
        }

        Optional<Service<EconomyProvider>> economyProvider = ServiceRegistry.INSTANCE.serviceFor(
                EconomyProvider.class);
        if (!economyProvider.isPresent()) {
            sender.sendMessage(Message.of(MessageKey.ECONOMY_INFO_ECONOMY_PROVIDER_UNAVAILABLE));
        } else {
            Service<EconomyProvider> service = economyProvider.get();
            EconomyProvider provider = service.get();
            sender.sendMessage(Message.of(
                    MessageKey.ECONOMY_INFO_ECONOMY_PROVIDER_AVAILABLE,
                    placeholder("name", service.registrarName()),
                    placeholder("priority", service.priority().name().toLowerCase(Locale.ROOT)),
                    placeholder("primary-currency", provider.getPrimaryCurrency().getIdentifier())
            ));
        }
    }

}
