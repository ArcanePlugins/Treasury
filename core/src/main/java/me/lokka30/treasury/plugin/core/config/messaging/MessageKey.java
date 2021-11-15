package me.lokka30.treasury.plugin.core.config.messaging;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import me.lokka30.treasury.plugin.core.config.ConfigVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a key of a message.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public enum MessageKey {
    // Common
    PREFIX(
            "common.prefix",
            new Messages.MessageHolder("&b&lTreasury:&7"),
            ConfigVersion.V1
    ),
    NO_PERMISSION(
            "common.no-permission",
            new Messages.MessageHolder("%prefix% You don''t have access to that &8(&7requires permission &b%permission%&8)&7."),
            ConfigVersion.V1
    ),
    STATE_YES(
            "common.states.yes",
            new Messages.MessageHolder("&aYes"),
            ConfigVersion.V1
    ),
    STATE_NO(
            "common.states.no",
            new Messages.MessageHolder("&cNo"),
            ConfigVersion.V1
    ),
    LIST_DELIMITER(
            "common.list-delimiter",
            new Messages.MessageHolder("&7, &b"),
            ConfigVersion.V1
    ),
    // Commands
    INVALID_USAGE_UNSPECIFIED(
            "commands.treasury.invalid-usage-unspecified",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% Invalid usage - please specify a subcommand.",
                            "%prefix% For a list of available subcommands, try ''&b/%label% help&7''."
                    )
            ),
            ConfigVersion.V1
    ),
    INVALID_USAGE_SPECIFIED(
            "commands.treasury.invalid-usage-specified",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% Invalid subcommand ''&b%subcommand%&7''.",
                            "%prefix% For a list of available subcommands, try ''&b/%label% help&7''."
                    )
            ),
            ConfigVersion.V1
    ),
    // Subcommands
    HELP_INVALID_USAGE(
            "commands.treasury.subcommands.help.invalid-usage",
            new Messages.MessageHolder("%prefix% Invalid usage, try ''&b/%label% help&7''."),
            ConfigVersion.V1
    ),
    HELP_AVAILABLE_COMMANDS(
            "commands.treasury.subcommands.help.available-commands",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% Available commands:",
                            " &8&m->&b /treasury help &8- &7view a list of Treasury''s commands.",
                            " &8&m->&b /treasury info &8- &7view info about Treasury.",
                            " &8&m->&b /treasury migrate &8- &7migrate from one economy provider to another.",
                            " &8&m->&b /treasury reload &8- &7re-load all of Treasury''s configuration files."
                    )
            ),
            ConfigVersion.V1
    ),
    INFO_INVALID_USAGE(
            "commands.treasury.subcommands.info.invalid-usage",
            new Messages.MessageHolder("%prefix% Invalid usage, try ''&b/%label% info&7''."),
            ConfigVersion.V1
    ),
    INFO_TREASURY(
            "commands.treasury.subcommands.info.treasury",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "&f&nAbout Treasury",
                            "&8 &m->&7 Running &bTreasury v%version%",
                            "&8 &m->&7 Description: &b%description%",
                            "&8 &m->&7 Made possible by: &bSee &n%credits%",
                            "&8 &m->&7 Latest API Version: &b%latest-api-version%",
                            "&8 &m->&7 Learn more at: &b&n%repository%",
                            " "
                    )
            ),
            ConfigVersion.V1
    ),
    INFO_ECONOMY_PROVIDER_UNAVAILABLE(
            "commands.treasury.subcommands.info.economy-provider-unavailable",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "&f&nEconomy Provider",
                            "&8 &m->&7 You don''t have an Economy Provider installed.",
                            " "
                    )
            ),
            ConfigVersion.V1
    ),
    INFO_ECONOMY_PROVIDER_AVAILABLE(
            "commands.treasury.subcommands.info.economy-provider-available",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "&f&nEconomy Provider",
                            "&8 &m->&7 Name: &b%name%",
                            "&8 &m->&7 Priority: &b%priority%",
                            "&8 &m->&7 API Version: &b%api-version%",
                            "&8 &m->&7 Supports bank accounts: &b%supports-bank-accounts%",
                            "&8 &m->&7 Supports transaction events: &b%supports-transaction-events%",
                            "&8 &m->&7 Primary currency: &b%primary-currency%",
                            " "
                    )
            ),
            ConfigVersion.V1
    ),
    INFO_MISC_INFO(
            "commands.treasury.subcommands.info.misc-info",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "&f&nMiscellaneous Info:",
                            "&8 &m->&7 For a list of commands, run ''&b/treasury help&7''."
                    )
            ),
            ConfigVersion.V1
    ),
    MIGRATE_INVALID_USAGE(
            "commands.treasury.subcommands.migrate.invalid-usage",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% Invalid usage, try ''&b/%label% migrate <providerFrom> <providerTo>&7''.",
                            "%prefix% Valid economy providers: &b%providers%&7."
                    )
            ),
            ConfigVersion.V1
    ),
    MIGRATE_REQUIRES_TWO_PROVIDERS(
            "commands.treasury.subcommands.migrate.requires-two-providers",
            new Messages.MessageHolder(
                    "%prefix% You can''t use this subcommand unless you have 2 economy providers running."
            ),
            ConfigVersion.V1
    ),
    MIGRATE_PROVIDERS_MATCH(
            "commands.treasury.subcommands.migrate.providers-match",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% You must specify two different economy providers.",
                            "%prefix% Valid economy providers: &b%providers%&7."
                    )
            ),
            ConfigVersion.V1
    ),
    MIGRATE_REQUIRES_VALID_FROM(
            "commands.treasury.subcommands.migrate.requires-valid-from",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% You must specify a valid economy provider to migrate from.",
                            "%prefix% Valid economy providers: &b%providers%&7."
                    )
            ),
            ConfigVersion.V1
    ),
    MIGRATE_REQUIRES_VALID_TO(
            "commands.treasury.subcommands.migrate.requires-valid-to",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% You must specify a valid economy provider to migrate to.",
                            "%prefix% Valid economy providers: &b%providers%&7."
                    )
            ),
            ConfigVersion.V1
    ),
    MIGRATE_STARTING_MIGRATION(
            "commands.treasury.subcommands.migrate.starting-migration",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% Starting migration, please wait...",
                            "%prefix% (This may briefly freeze the server)"
                    )
            ),
            ConfigVersion.V1
    ),
    MIGRATE_INTERNAL_ERROR(
            "commands.treasury.subcommands.migrate.internal-error",
            new Messages.MessageHolder(
                    "%prefix% An internal error occurred whilst attempting to migrate. Please check console for more information."
            ),
            ConfigVersion.V1
    ),
    MIGRATE_FINISHED_MIGRATION(
            "commands.treasury.subcommands.migrate.finished-migration",
            new Messages.MessageHolder(
                    Arrays.asList(
                            "%prefix% Migration complete! Statistics:",
                            "&8 &m->&7 Took &b%time%ms&7.",
                            "&8 &m->&7 Processed &b%player-accounts%&7 player accounts.",
                            "&8 &m->&7 Processed &b%bank-accounts%&7 bank accounts.",
                            "&8 &m->&7 Migrated currencies: &b%migrated-currencies%&7.",
                            "&8 &m->&7 Non-migrated currencies: &b%non-migrated-currencies%&7."
                    )
            ),
            ConfigVersion.V1
    ),
    RELOAD_INVALID_USAGE(
            "commands.treasury.subcommands.reload.invalid-usage",
            new Messages.MessageHolder("%prefix% Invalid usage, try ''&b/%label% reload&7''."),
            ConfigVersion.V1
    ),
    RELOAD_START(
            "commands.treasury.subcommands.reload.reload-start",
            new Messages.MessageHolder("%prefix% Reloading Treasury..."),
            ConfigVersion.V1
    ),
    RELOAD_COMPLETE(
            "commands.treasury.subcommands.reload.reload-complete",
            new Messages.MessageHolder("%prefix% Reload successful &8(&7took &b%time%ms&8)&7."),
            ConfigVersion.V1
    )
    ;

    private static final Map<String, MessageKey> BY_CONFIG_KEY = new HashMap<>();
    private static final Multimap<ConfigVersion, MessageKey> BY_CONFIG_VERSION = HashMultimap.create();

    static {
        for (MessageKey messageKey : MessageKey.values()) {
            BY_CONFIG_KEY.put(messageKey.asConfigKey(), messageKey);
            BY_CONFIG_VERSION.put(messageKey.getVersionAddedIn(), messageKey);
        }
    }

    /**
     * Returns all the message keys, assigned to the specified {@link ConfigVersion} {@code configVersion}
     *
     * @param configVersion the config version you want the message keys for
     * @return message keys, or null
     */
    @Nullable
    public static Collection<MessageKey> getAllWithConfigVersion(@NotNull ConfigVersion configVersion) {
        Objects.requireNonNull(configVersion, "configVersion");
        return BY_CONFIG_VERSION.get(configVersion);
    }

    /**
     * Returns the message key, which corresponds to the specified config key.
     *
     * @param configKey the config key you want the message key for
     * @return message key or null
     */
    @Nullable
    public static MessageKey fromConfigKey(@NotNull String configKey) {
        Objects.requireNonNull(configKey, "configKey");
        return BY_CONFIG_KEY.get(configKey);
    }

    private final String configKey;
    private final Messages.MessageHolder defaultMessage;
    private final ConfigVersion versionAddedIn;

    MessageKey(@NotNull String configKey, @NotNull Messages.MessageHolder defaultMessage, @NotNull ConfigVersion versionAddedIn) {
        this.configKey = Objects.requireNonNull(configKey, "configKey");
        this.defaultMessage = Objects.requireNonNull(defaultMessage, "defaultMessage");
        this.versionAddedIn = Objects.requireNonNull(versionAddedIn, "versionAddedIn");
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

    /**
     * Returns the default message of this message key
     *
     * @return default message
     */
    @NotNull
    public Messages.MessageHolder getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * Returns the {@link ConfigVersion} this message key was added in.
     *
     * @return config version
     */
    @NotNull
    public ConfigVersion getVersionAddedIn() {
        return versionAddedIn;
    }

}
