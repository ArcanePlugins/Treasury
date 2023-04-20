/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.util;

import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.spongepowered.api.Sponge;

public class SpongeUtil {

    public static void checkMainThread(String methodName, String blame) {
        if (Sponge.server().onMainThread()) {
            TreasuryPlugin
                    .getInstance()
                    .logger()
                    .error("'" + methodName + "', called by '" + blame + "' is called on Sponge's" +
                            " main thread! Whilst Treasury will allow this behaviour, you will " +
                            "most likely experience lag. In order to fix this, you might want to " +
                            "consider contacting the mod/plugin developer, which wrote this class. " +
                            "TIP: You can probably find the mod/plugin by the package name " +
                            "(the package name is before the last dot in the 'called by' quotes).");
        }
    }

}
