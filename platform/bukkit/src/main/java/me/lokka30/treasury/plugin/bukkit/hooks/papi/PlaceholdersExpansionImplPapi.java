/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks.papi;

import java.util.function.BiFunction;
import me.lokka30.treasury.plugin.core.hooks.placeholder.PlaceholdersExpansion;
import org.jetbrains.annotations.NotNull;

public class PlaceholdersExpansionImplPapi extends PlaceholdersExpansion {

    private final BiFunction<String, String, String> getStringFunction;
    private final BiFunction<String, Integer, Integer> getIntFunction;
    private final BiFunction<String, Boolean, Boolean> getBooleanFunction;

    public PlaceholdersExpansionImplPapi(
            final BiFunction<String, String, String> getStringFunction,
            final BiFunction<String, Integer, Integer> getIntFunction,
            final BiFunction<String, Boolean, Boolean> getBooleanFunction
    ) {
        this.getStringFunction = getStringFunction;
        this.getIntFunction = getIntFunction;
        this.getBooleanFunction = getBooleanFunction;
    }

    @Override
    public String pluginName() {
        return "PlaceholderAPI";
    }

    @Override
    public String getString(@NotNull final String key, final String def) {
        return getStringFunction.apply(key, def);
    }

    @Override
    public int getInt(@NotNull final String key, final int def) {
        return getIntFunction.apply(key, def);
    }

    @Override
    public boolean getBoolean(@NotNull final String key, final boolean def) {
        return getBooleanFunction.apply(key, def);
    }

}
