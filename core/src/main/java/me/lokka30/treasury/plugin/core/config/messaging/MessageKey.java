/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.config.messaging;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

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
    LIST_DELIMITER("common.list-delimiter"),

    // Commands
    INVALID_USAGE_UNSPECIFIED("commands.treasury.invalid-usage-unspecified"),
    INVALID_USAGE_SPECIFIED("commands.treasury.invalid-usage-specified"),

    // Subcommands
    ECONOMY_INVALID_USAGE_UNSPECIFIED(
            "commands.treasury.subcommands.economy.invalid-usage-unspecified"),
    ECONOMY_INVALID_USAGE_SPECIFIED("commands.treasury.subcommands.economy.invalid-usage-specified"),
    ECONOMY_HELP_INVALID_USAGE("commands.treasury.subcommands.economy.help.invalid-usage"),
    ECONOMY_HELP_AVAILABLE_COMMANDS("commands.treasury.subcommands.economy.help.available-commands"),
    ECONOMY_INFO_INVALID_USAGE("commands.treasury.subcommands.economy.info.invalid-usage"),
    ECONOMY_INFO_ECONOMY_PROVIDER_UNAVAILABLE(
            "commands.treasury.subcommands.economy.info.economy-provider-unavailable"),
    ECONOMY_INFO_ECONOMY_PROVIDER_AVAILABLE(
            "commands.treasury.subcommands.economy.info.economy-provider-available"),
    HELP_INVALID_USAGE("commands.treasury.subcommands.help.invalid-usage"),
    HELP_AVAILABLE_COMMANDS("commands.treasury.subcommands.help.available-commands"),
    INFO_INVALID_USAGE("commands.treasury.subcommands.info.invalid-usage"),
    INFO_TREASURY("commands.treasury.subcommands.info.treasury"),
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
    RELOAD_COMPLETE("commands.treasury.subcommands.reload.reload-complete"),
    DOWNLOAD_LATEST_INVALID_USAGE("commands.treasury.subcommands.downloadLatest.invalid-usage"),
    DOWNLOAD_LATEST_STARTED("commands.treasury.subcommands.downloadLatest.started"),
    DOWNLOAD_LATEST_ERROR("commands.treasury.subcommands.downloadLatest.error-occurred"),
    DOWNLOAD_LATEST_COULDNT_PARSE("commands.treasury.subcommands.downloadLatest.couldnt-parse"),
    DOWNLOAD_LATEST_ALREADY_LATEST("commands.treasury.subcommands.downloadLatest.already-latest"),
    DOWNLOAD_LATEST_SUCCESS("commands.treasury.subcommands.downloadLatest.success");

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
