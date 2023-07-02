/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor;

import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.vendor.all.DefaultBukkitLogger;
import me.lokka30.treasury.plugin.bukkit.vendor.paper.PaperLogger;
import me.lokka30.treasury.plugin.core.logging.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class CoreLogger {

    private final Logger loggerImpl;

    public CoreLogger(@NotNull TreasuryBukkit plugin) {
        this.loggerImpl = BukkitVendor.isPaper() ? PaperLogger.getLogger(plugin) :
                new DefaultBukkitLogger(plugin.getLogger());
    }

    @Contract(pure = true)
    @NotNull
    public Logger getImpl() {
        return this.loggerImpl;
    }

}
