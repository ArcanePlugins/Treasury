/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.util;

import org.spongepowered.api.Sponge;

public class SpongeUtil {

    public static void checkMainThread(String methodName) {
        if (Sponge.server().onMainThread()) {
            throw new UnsupportedOperationException("Cannot call '" + methodName + "' synchronously!");
        }
    }

}
