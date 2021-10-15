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

package me.lokka30.treasury.plugin.command.treasury.subcommand;

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.command.Subcommand;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class InfoSubcommand implements Subcommand {

    /*
    inf: Prints generic information about the plugin.
    cmd: /treasury info
    arg:         |    0
    len:         0    1
     */

    @NotNull private final Treasury main;
    public InfoSubcommand(@NotNull final Treasury main) { this.main = main; }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(!Utils.checkPermissionForCommand(main, sender, "treasury.command.treasury.info")) return;

        if(args.length != 1) {
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.info.invalid-usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ));
            return;
        }

        final RegisteredServiceProvider<EconomyProvider> registeredServiceProvider = main.getServer().getServicesManager().getRegistration(EconomyProvider.class);

        new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.info.treasury"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                new MultiMessage.Placeholder("version", main.getDescription().getVersion(), false),
                new MultiMessage.Placeholder("description", main.getDescription().getDescription(), false),
                new MultiMessage.Placeholder("credits", "https://github.com/lokka30/Treasury/wiki/Credits", false),
                new MultiMessage.Placeholder("latest-api-version", Treasury.ECONOMY_API_VERSION.getNumber() + "", false),
                new MultiMessage.Placeholder("repository", "https://github.com/lokka30/Treasury/", false)
        ));



        if(registeredServiceProvider == null) {
            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.info.economy-provider-unavailable"), Collections.singletonList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true)
            ));
        } else {
            final EconomyProvider provider = registeredServiceProvider.getProvider();

            new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.info.economy-provider-available"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true),
                    new MultiMessage.Placeholder("name", provider.getProvider().getName(), false),
                    new MultiMessage.Placeholder("priority", registeredServiceProvider.getPriority().toString(), false),
                    new MultiMessage.Placeholder("api-version", provider.getSupportedAPIVersion() + "", false),
                    new MultiMessage.Placeholder("supports-bank-accounts", Utils.getYesNoStateMessage(main, provider.hasBankAccountSupport()), true),
                    new MultiMessage.Placeholder("supports-transaction-events", Utils.getYesNoStateMessage(main, provider.hasTransactionEventSupport()), true),
                    new MultiMessage.Placeholder("primary-currency", provider.getPrimaryCurrency().getCurrencyName(), true)
            ));
        }

        new MultiMessage(main.messagesCfg.getConfig().getStringList("commands.treasury.subcommands.info.misc-info"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messagesCfg.getConfig().getString("common.prefix"), true)
        ));
    }
}
