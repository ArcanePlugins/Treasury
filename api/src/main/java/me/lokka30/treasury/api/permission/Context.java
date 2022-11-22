/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import me.lokka30.treasury.api.common.misc.TriState;
import org.jetbrains.annotations.NotNull;

public final class Context {

    @NotNull
    public static Context of(@NotNull String context) {
        return new Context(context, TriState.UNSPECIFIED);
    }

    @NotNull
    public static Context of(@NotNull String context, @NotNull TriState world) {
        return new Context(context, world);
    }

    private final String context;
    private final TriState world;

    private Context(@NotNull String context, @NotNull TriState world) {
        this.context = context;
        this.world = world;
    }

    @NotNull
    public String context() {
        return this.context;
    }

    @NotNull
    public TriState isWorldContext() {
        return this.world;
    }

}
