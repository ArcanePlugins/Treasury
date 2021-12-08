/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

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

    /**
     * Returns a {@link Messages} object, which contains all the messages.
     *
     * @return messages
     */
    @NotNull Messages getMessages();

    /**
     * Returns a {@link Settings} object, which contains all the settings.
     *
     * @return settings
     */
    @NotNull Settings getSettings();
}
