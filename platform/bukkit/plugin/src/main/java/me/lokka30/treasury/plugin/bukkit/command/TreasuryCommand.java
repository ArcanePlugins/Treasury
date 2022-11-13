/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.command;

import java.util.List;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.core.command.TreasuryBaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreasuryCommand implements TabExecutor {

    public static void register(TreasuryBukkit plugin) {
        PluginCommand cmd = plugin.getCommand("treasury");
        CommandSources sources = new CommandSources();
        plugin.getServer().getPluginManager().registerEvents(sources, plugin);
        TreasuryCommand executor = new TreasuryCommand(sources);
        cmd.setExecutor(executor);
        cmd.setTabCompleter(executor);
    }

    private final TreasuryBaseCommand base;
    private final CommandSources sources;

    private TreasuryCommand(CommandSources sources) {
        base = new TreasuryBaseCommand();
        this.sources = sources;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        base.execute(sources.obtainSource(sender), label, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        return base.complete(sources.obtainSource(sender), label, args);
    }

}
