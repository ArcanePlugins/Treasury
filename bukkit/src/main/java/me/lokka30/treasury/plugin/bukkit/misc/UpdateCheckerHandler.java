/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.plugin.bukkit.misc;

import me.lokka30.microlib.exceptions.OutdatedServerVersionException;
import me.lokka30.microlib.other.UpdateChecker;
import me.lokka30.treasury.plugin.bukkit.Treasury;
import org.jetbrains.annotations.NotNull;

public class UpdateCheckerHandler {

    @NotNull private final Treasury main;
    public UpdateCheckerHandler(@NotNull final Treasury main) {
        this.main = main;
    }

    public void checkForUpdates() {
        if(!main.settingsCfg.getConfig().getBoolean("update-checker.enabled", true)) return;

        //TODO This method requires a Spigot Resource ID which can't be obtained before the resource is released. This will be modified later.
        //noinspection ConstantConditions
        if(true) return;

        final UpdateChecker updateChecker = new UpdateChecker(main, 12345);

        try {
            updateChecker.getLatestVersion(latestVersion -> {
                if(latestVersion == null) {
                    Utils.logger.error("Unable to check for updates.");
                    return;
                }

                if(latestVersion.equals(updateChecker.getCurrentVersion())) return;

                Utils.logger.warning("A new Treasury update is available - '&bv" + latestVersion + "&7' - please update as soon as possible." +
                        " &8(&7You're running '&bv" + updateChecker.getCurrentVersion() + "&7'&8)");
            });
        } catch(OutdatedServerVersionException ex) {
            Utils.logger.warning("Treasury's update checker only functions on &b1.11+&7 servers, as older versions lack the code required to run it. " +
                    "Please disable the update checker in Treasury's &bsettings.yml&7 file.");
        }
    }
}
