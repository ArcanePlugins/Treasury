/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bungeecord;

import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class TreasuryCommand extends Command implements TabExecutor {

    public static void register(TreasuryBungee plugin) {
        CommandSources sources = new CommandSources();
        plugin.getProxy().getPluginManager().registerListener(plugin, sources);
        plugin.getProxy().getPluginManager().registerCommand(plugin, new TreasuryCommand(sources));
    }

    private final TreasuryBaseCommand base;
    private final CommandSources sources;

    public TreasuryCommand(CommandSources sources) {
        super("treasury", "treasury.command.treasury");
        this.sources = sources;
        this.base = new TreasuryBaseCommand();
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        base.execute(sources.obtainSource(sender), "treasury", args);
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args) {
        return base.complete(sources.obtainSource(sender), "treasury", args);
    }

}
