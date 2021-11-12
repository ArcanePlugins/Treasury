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

package me.lokka30.treasury.plugin.bukkit;

import me.lokka30.microlib.files.YamlConfigFile;
import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.treasury.api.economy.currency.conversion.CurrencyConverter;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.bukkit.command.CommandHandler;
import me.lokka30.treasury.plugin.bukkit.debug.DebugHandler;
import me.lokka30.treasury.plugin.bukkit.file.FileHandler;
import me.lokka30.treasury.plugin.bukkit.misc.UpdateCheckerHandler;
import me.lokka30.treasury.plugin.bukkit.misc.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
     * This is Treasury's API version. (Not the same as api-version from plugin.yml!)
     * Any major changes to the API should make this number increase.
     * This allows Treasury to warn server owners if their Provider
     * does not support the latest Treasury API version.
     */
    @NotNull public static final EconomyAPIVersion ECONOMY_API_VERSION = EconomyAPIVersion.VERSION_1;

    @NotNull private final CurrencyConverter currencyConverter = new CurrencyConverter();
    @NotNull public CurrencyConverter getCurrencyConverter() { return currencyConverter; }

    @NotNull public final FileHandler fileHandler = new FileHandler(this);
    @NotNull public final DebugHandler debugHandler = new DebugHandler(this);
    @NotNull public final CommandHandler commandHandler = new CommandHandler(this);
    @NotNull public final UpdateCheckerHandler updateCheckerHandler = new UpdateCheckerHandler(this);

    @NotNull public final YamlConfigFile settingsCfg = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    @NotNull public final YamlConfigFile messagesCfg = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));

    /**
     * @author lokka30
     * @since v1.0.0
     * Run the start-up procedure for the plugin.
     * This is called by Bukkit's plugin manager.
     */
    @Override
    public void onEnable() {
        final QuickTimer startupTimer = new QuickTimer();

        fileHandler.loadFiles();
        debugHandler.loadEnabledCategories();
        commandHandler.registerCommands();

        updateCheckerHandler.checkForUpdates();
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

        // Add onDisable code here if required.

        Utils.logger.info("&fShut-down complete (took &b" + shutdownTimer.getTimer() + "ms&f).");
    }
}
