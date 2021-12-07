/*
 * Copyright (c) 2021-2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.core.command.subcommand;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
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
                placeholder("current-api-version", EconomyAPIVersion.getCurrentAPIVersion()),
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
