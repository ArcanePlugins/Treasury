package me.lokka30.treasury.plugin.core.config.messaging;

import org.jetbrains.annotations.NotNull;

public interface ColorHandler {

    @NotNull
    String colorize(@NotNull String message);
}
