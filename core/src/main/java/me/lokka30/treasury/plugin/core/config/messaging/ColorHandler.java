package me.lokka30.treasury.plugin.core.config.messaging;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a handler for minecraft colors, which is to be implemented by the platform depending on the
 * Treasury's core.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface ColorHandler {

    /**
     * Should parse the colors of the specified message, so the message can be sent
     * to a {@link me.lokka30.treasury.plugin.core.command.CommandSource}
     *
     * @param message the message to colorize
     * @return colorized message
     */
    @NotNull
    String colorize(@NotNull String message);
}
