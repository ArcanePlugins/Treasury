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

package me.lokka30.treasury.plugin.core.config.messaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a key of a message.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public enum MessageKey {
    // Common
    PREFIX("common.prefix"),
    NO_PERMISSION("common.no-permission"),
    STATE_YES("common.states.does"),
    STATE_NO("common.states.does-not"),
    LIST_DELIMITER("common.list-delimiter"),

    // Commands
    INVALID_USAGE_UNSPECIFIED("commands.treasury.invalid-usage-unspecified"),
    INVALID_USAGE_SPECIFIED("commands.treasury.invalid-usage-specified"),

    // Subcommands
    HELP_INVALID_USAGE("commands.treasury.subcommands.help.invalid-usage"),
    HELP_AVAILABLE_COMMANDS("commands.treasury.subcommands.help.available-commands"),
    INFO_INVALID_USAGE("commands.treasury.subcommands.info.invalid-usage"),
    INFO_TREASURY("commands.treasury.subcommands.info.treasury"),
    INFO_ECONOMY_PROVIDER_UNAVAILABLE("commands.treasury.subcommands.info.economy-provider-unavailable"),
    INFO_ECONOMY_PROVIDER_AVAILABLE("commands.treasury.subcommands.info.economy-provider-available"),
    INFO_MISC_INFO("commands.treasury.subcommands.info.misc-info"),
    MIGRATE_INVALID_USAGE("commands.treasury.subcommands.migrate.invalid-usage"),
    MIGRATE_REQUIRES_TWO_PROVIDERS("commands.treasury.subcommands.migrate.requires-two-providers"),
    MIGRATE_PROVIDERS_MATCH("commands.treasury.subcommands.migrate.providers-match"),
    MIGRATE_REQUIRES_VALID_FROM("commands.treasury.subcommands.migrate.requires-valid-from"),
    MIGRATE_REQUIRES_VALID_TO("commands.treasury.subcommands.migrate.requires-valid-to"),
    MIGRATE_STARTING_MIGRATION("commands.treasury.subcommands.migrate.starting-migration"),
    MIGRATE_INTERNAL_ERROR("commands.treasury.subcommands.migrate.internal-error"),
    MIGRATE_FINISHED_MIGRATION("commands.treasury.subcommands.migrate.finished-migration"),
    RELOAD_INVALID_USAGE("commands.treasury.subcommands.reload.invalid-usage"),
    RELOAD_START("commands.treasury.subcommands.reload.reload-start"),
    RELOAD_COMPLETE("commands.treasury.subcommands.reload.reload-complete");

    private static final Map<String, MessageKey> BY_CONFIG_KEY = new HashMap<>();

    static {
        for (MessageKey key : MessageKey.values()) {
            BY_CONFIG_KEY.put(key.asConfigKey(), key);
        }
    }

    /**
     * Returns a message key by the specified config {@code key}
     *
     * @param key the config key you want a message key for
     * @return message key or null
     */
    @Nullable
    public static MessageKey getByConfigKey(@NotNull String key) {
        Objects.requireNonNull(key, "key");
        return BY_CONFIG_KEY.get(key);
    }

    private final String configKey;

    MessageKey(@NotNull String configKey) {
        this.configKey = Objects.requireNonNull(configKey, "configKey");
    }

    /**
     * Returns the config key of this message key.
     *
     * @return config key
     */
    @NotNull
    public String asConfigKey() {
        return configKey;
    }

}
