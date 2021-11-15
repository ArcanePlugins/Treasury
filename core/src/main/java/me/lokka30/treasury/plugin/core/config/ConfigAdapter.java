package me.lokka30.treasury.plugin.core.config;

import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.settings.Settings;
import org.jetbrains.annotations.NotNull;

/**
 * A config adapter, providing {@link Messages} and {@link Settings}
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface ConfigAdapter {

    @NotNull Messages getMessages();

    @NotNull Settings getSettings();
}
