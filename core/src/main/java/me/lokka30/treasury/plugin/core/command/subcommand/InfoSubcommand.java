/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.core.ProviderEconomy;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

import static me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder.placeholder;

public class InfoSubcommand implements Subcommand {

    /*
    inf: Prints generic information about the plugin.
    cmd: /treasury info
    arg:         |    0
    len:         0    1
     */

    @Override
    public void execute(@NotNull CommandSource sender, @NotNull String label, @NotNull String[] args) {
        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury.info")) {
            return;
        }

        if (args.length != 0) {
            sender.sendMessage(
                    Message.of(MessageKey.INFO_INVALID_USAGE, MessagePlaceholder.placeholder("label", label))
            );
            return;
        }

        TreasuryPlugin main = TreasuryPlugin.getInstance();

        sender.sendMessage(Message.of(
                MessageKey.INFO_TREASURY,
                placeholder("version", main.getVersion()),
                placeholder("description", main.getDescription()),
                placeholder("credits", "https://github.com/lokka30/Treasury/wiki/Credits"),
                placeholder("latest-api-version", main.getEconomyAPIVersion()),
                placeholder("repository", "https://github.com/lokka30/Treasury/"))
        );

        ProviderEconomy providerProvider = main.economyProviderProvider();
        if (providerProvider == null) {
            sender.sendMessage(Message.of(MessageKey.INFO_ECONOMY_PROVIDER_UNAVAILABLE));
        } else {
            EconomyProvider provider = providerProvider.provide();
            sender.sendMessage(Message.of(
                    MessageKey.INFO_ECONOMY_PROVIDER_AVAILABLE,
                    placeholder("name", providerProvider.registrar().getName()),
                    placeholder("priority", providerProvider.getPriority()),
                    placeholder("api-version", provider.getSupportedAPIVersion()),
                    placeholder("supports-bank-accounts", Utils.getYesNoStateMessage(provider.hasBankAccountSupport())),
                    placeholder("supports-transaction-events", Utils.getYesNoStateMessage(provider.hasTransactionEventSupport())),
                    placeholder("primary-currency", provider.getPrimaryCurrency().getPrimaryCurrencyName()))
            );
        }

        sender.sendMessage(Message.of(MessageKey.INFO_MISC_INFO));
    }
}
