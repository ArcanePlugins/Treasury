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

package me.lokka30.treasury.plugin;

import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.treasury.plugin.command.treasury.TreasuryCommand;
import me.lokka30.treasury.plugin.misc.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author lokka30
 * @since v1.0.0
 * This is the plugin's main class, loaded by Bukkit's plugin manager.
 * It contains direct and indirect links to everything accessed within
 * the plugin.
 */
@SuppressWarnings("unused")
public class Treasury extends JavaPlugin {

    /**
     * Any contributors to Treasury may add their name here! :)
     */
    public static final ArrayList<String> contributors = new ArrayList<>(Arrays.asList("lokka30 (Author, main developer)", "ExampleUser (Example description)"));

    /**
     * This is Treasury's API version. This is NOT api-version from plugin.yml.
     * Any major changes to the API should make this number increase.
     * This allows Treasury to warn server owners if their Provider
     * does not support the latest Treasury API version.
     */
    public static final short apiVersion = 1;

    /**
     * @author lokka30
     * @since v1.0.0
     * Run the start-up procedure for the plugin.
     * This is called by Bukkit's plugin manager.
     */
    @Override
    public void onEnable() {
        final QuickTimer startupTimer = new QuickTimer();

        new Metrics(this, 12927);

        Utils.logger.info("&fStart-up complete (took &b" + startupTimer.getTimer() + "ms&f).");
    }

    /**
     * @author lokka30
     * @since v1.0.0
     * Run the shut-down procedure for the plugin.
     * This is called by Bukkit's plugin manager.
     */
    @Override
    public void onDisable() {
        final QuickTimer shutdownTimer = new QuickTimer();

        Utils.logger.info("&fShut-down complete (took &b" + shutdownTimer.getTimer() + "ms&f).");
    }

    /**
     * @author lokka30
     * @since v1.0.0
     * @see Treasury#registerCommand(String, TabExecutor)
     * Registers all commands from Treasury.
     */
    private void registerCommands() {
        Utils.logger.info("Registering commands...");
        registerCommand("treasury", new TreasuryCommand(this));
    }

    /**
     * @author lokka30
     * @since v1.0.0
     * Attempts to register the specified command.
     * If unsuccessful, console is notified.
     * The base command is likely case-sensitive.
     * @param baseCommand defined in plugin.yml.
     * @param executor handling the execution of the command.
     */
    private void registerCommand(@SuppressWarnings("SameParameterValue") @NotNull final String baseCommand, @NotNull final TabExecutor executor) {
        assert !baseCommand.isEmpty();

        final PluginCommand pluginCommand = getCommand(baseCommand);
        if(pluginCommand == null) {
            Utils.logger.error("Unable to register command '&b/" + baseCommand + "&7'. Please notify a Treasury developer.");
        } else {
            pluginCommand.setExecutor(executor);
            Utils.logger.info("Registered command '&b/" + baseCommand + "&7' successfully.");
        }
    }
}
