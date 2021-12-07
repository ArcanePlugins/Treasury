package me.lokka30.treasury.plugin.core.config.messaging;

import com.mrivanplays.annotationconfig.core.annotations.ConfigObject;
import com.mrivanplays.annotationconfig.core.annotations.Ignore;
import com.mrivanplays.annotationconfig.core.annotations.Key;
import com.mrivanplays.annotationconfig.core.annotations.comment.Comment;
import com.mrivanplays.annotationconfig.core.utils.AnnotationUtils;
import com.mrivanplays.annotationconfig.yaml.YamlConfig;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * All Treasury plugin messages.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
@Comment("## Treasury")
@Comment("Treasury is a modern code library for plugins.")
@Comment("GitHub Repository: <https://github.com/lokka30/Treasury/>")
@Comment(" ")
@Comment("## About this File")
@Comment("Welcome to the messages.yml file, here you may translate")
@Comment("and customise all of Treasury's messages (except for those")
@Comment("logged to the console). Standard color codes are supported")
@Comment("(e.g &a, &b, &c) and also hex color codes (e.g.")
@Comment("&#FF0000, &#ABCDEF). All messages are configured in terms")
@Comment("of lines, so you can add multiple lines to most messages")
@Comment("if you wish.")
@Comment(" ")
@Comment("## Applying changes")
@Comment("Whenever you have finished making your changes to this")
@Comment("configuration file, please save it, then run")
@Comment("`/treasury reload` if your server is already running.")
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class Messages {

    @ConfigObject
    private CommonMessages common = new CommonMessages();

    @Comment("These messages are used across multiple other messages,")
    @Comment("so they have been grouped together in the 'common' category.")
    public static class CommonMessages {

        @Comment("This text replaces the `%prefix%` placeholder in all applicable messages.")
        private String prefix = "&b&lTreasury:&7";

        @Comment("This message is sent when a user does not have permission to access something within Treasury,")
        @Comment("i.e., a command like `/treasury migrate`.")
        @Comment(" ")
        @Comment("Placeholders: %prefix%, %permission%")
        @Key("no-permission")
        private List<String> noPermission = Collections.singletonList(
                "%prefix% You don't have access to that &8(&7requires permission &b%permission%&8)&7."
        );

        @ConfigObject
        private States states = new States();

        @Comment("These states are used in various messages.")
        public static class States {

            private String does = "&aYes";

            @Key("does-not")
            private String doesNot = "&cNo";
        }

        @Comment("The delimiter used in lists of things - this separates each list term.")
        @Comment("For example, the non-colored version of a list can look like:")
        @Comment("'List item 1&7, &bList item 2&7, &bList item 3'")
        @Comment("Notice how the list delimiter is used to separate each term in the list.")
        @Key("list-delimiter")
        private String listDelimiter = "&7, &b";
    }

    @ConfigObject
    private Commands commands = new Commands();

    @Comment("These messages are sent by running certain commands.")
    public static class Commands {

        @ConfigObject
        private TreasuryCommand treasury = new TreasuryCommand();

        @Comment("Messages from `/treasury`")
        public static class TreasuryCommand {

            @Comment("Placeholders: %prefix%, %label%")
            @Key("invalid-usage-unspecified")
            private List<String> invalidUsageUnspecified = Arrays.asList(
                    "%prefix% Invalid usage - please specify a subcommand.",
                    "%prefix% For a list of available subcommands, try ''&b/%label% help&7''."
            );

            @Comment("Placeholders: %prefix%, %label%, %subcomand%")
            @Key("invalid-usage-specified")
            private List<String> invalidUsageSpecified = Arrays.asList(
                    "%prefix% Invalid subcommand ''&b%subcommand%&7''.",
                    "%prefix% For a list of available subcommands, try ''&b/%label% help&7''."
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.help.invalid-usage")
            private List<String> helpInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try ''&b/%label% help&7''."
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.help.available-commands")
            private List<String> helpAvailableCommands = Arrays.asList(
                    "%prefix% Available commands:",
                    " &8&m->&b /treasury help &8- &7view a list of Treasury''s commands.",
                    " &8&m->&b /treasury info &8- &7view info about Treasury.",
                    " &8&m->&b /treasury migrate &8- &7migrate from one economy provider to another.",
                    " &8&m->&b /treasury reload &8- &7re-load all of Treasury''s configuration files.'"
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.info.invalid-usage")
            private List<String> infoInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try ''&b/%label% info&7''."
            );

            @Comment("Placeholders: %prefix%, %version%, %description%, %credits%,")
            @Comment("              %latest-api-version%, %repository%")
            @Key("subcommands.info.treasury")
            private List<String> infoTreasury = Arrays.asList(
                    "&f&nAbout Treasury",
                    "&8 &m->&7 Running &bTreasury v%version%",
                    "&8 &m->&7 Description: &b%description%",
                    "&8 &m->&7 Made possible by: &bSee &n%credits%",
                    "&8 &m->&7 Current API Version: &b%current-api-version%",
                    "&8 &m->&7 Learn more at: &b&n%repository%",
                    " "
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.info.economy-provider-unavailable")
            private List<String> infoEconomyProviderUnavailable = Arrays.asList(
                    "&f&nEconomy Provider",
                    "&8 &m->&7 You don''t have an Economy Provider installed.'",
                    " "
            );

            @Comment("Placeholders: %prefix, %name%, %priority%, %api-version%")
            @Comment("              %supports-bank-accounts%, %primary-currency%,")
            @Comment("              %supports-transaction-events%")
            @Key("subcommands.info.economy-provider-available")
            private List<String> infoEconomyProviderAvailable = Arrays.asList(
                    "&f&nEconomy Provider",
                    "&8 &m->&7 Name: &b%name%",
                    "&8 &m->&7 Priority: &b%priority%",
                    "&8 &m->&7 API Version: &b%api-version%",
                    "&8 &m->&7 Supports bank accounts: &b%supports-bank-accounts%",
                    "&8 &m->&7 Supports transaction events: &b%supports-transaction-events%",
                    "&8 &m->&7 Primary currency: &b%primary-currency%",
                    " "
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.info.misc-info")
            private List<String> infoMiscInfo = Arrays.asList(
                    "&f&nMiscellaneous Info:",
                    "&8 &m->&7 For a list of commands, run ''&b/treasury help&7''."
            );

            @Comment("Placeholders: %prefix%, %label%, %providers%")
            @Key("subcommands.migrate.invalid-usage")
            private List<String> migrateInvalidUsage = Arrays.asList(
                    "%prefix% Invalid usage, try ''&b/%label% migrate <providerFrom> <providerTo>&7''.",
                    "%prefix% Valid economy providers: &b%providers%&7."
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.migrate.requires-two-providers")
            private List<String> migrateRequiresTwoProviders = Collections.singletonList(
                    "%prefix% You can''t use this subcommand unless you have 2 economy providers set up."
            );

            @Comment("Placeholders: %prefix%, %providers%")
            @Key("subcommands.migrate.providers-match")
            private List<String> migrateProvidersMatch = Arrays.asList(
                    "%prefix% You must specify two different economy providers.",
                    "%prefix% Valid economy providers: &b%providers%&7."
            );

            @Comment("Placeholders: %prefix%, %providers%")
            @Key("subcommands.migrate.requires-valid-from")
            private List<String> migrateRequiresValidFrom = Arrays.asList(
                    "%prefix% You must specify a valid economy provider to migrate from.",
                    "%prefix% Valid economy providers: &b%providers%&7."
            );

            @Comment("Placeholders: %prefix%, %providers%")
            @Key("subcommands.migrate.requires-valid-to")
            private List<String> migrateRequiresValidTo = Arrays.asList(
                    "%prefix% You must specify a valid economy provider to migrate to.'",
                    "%prefix% Valid economy providers: &b%providers%&7."
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.migrate.starting-migration")
            private List<String> migrateStartingMigration = Arrays.asList(
                    "%prefix% Starting migration, please wait...",
                    "%prefix% (This may briefly freeze the server)"
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.migrate.internal-error")
            private List<String> migrateInternalError = Collections.singletonList(
                    "%prefix% An internal error occurred whilst attempting to migrate. Please check console for more information."
            );

            @Comment("Placeholders: %prefix%, %time%, %player-accounts%,")
            @Comment("              %migrated-currencies%, %non-migrated-currencies%")
            @Key("subcommands.migrate.finished-migration")
            private List<String> migrateFinishedMigration = Arrays.asList(
                    "%prefix% Migration complete! Statistics:",
                    "&8 &m->&7 Took &b%time%ms&7.",
                    "&8 &m->&7 Processed &b%player-accounts%&7 player accounts.",
                    "&8 &m->&7 Processed &b%bank-accounts%&7 bank accounts.",
                    "&8 &m->&7 Migrated currencies: &b%migrated-currencies%&7.",
                    "&8 &m->&7 Non-migrated currencies: &b%non-migrated-currencies%&7."
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.reload.invalid-usage")
            private List<String> reloadInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try '&b/%label% reload&7'."
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.reload.reload-start")
            private List<String> reloadStart = Collections.singletonList(
                    "%prefix% Reloading Treasury..."
            );

            @Comment("Placeholders: %prefix%, %time%")
            @Key("subcommands.reload.reload-complete")
            private List<String> reloadComplete = Collections.singletonList(
                    "%prefix% Reload successful &8(&7took &b%time%ms&8)&7."
            );

        }
    }

    // now for accessing them all easily
    private static final class MessageHolder {

        private final List<String> message;

        public MessageHolder(@NotNull List<String> message) {
            this.message = Objects.requireNonNull(message, "message");
        }

        public MessageHolder(@NotNull String message) {
            this.message = Collections.singletonList(Objects.requireNonNull(message, "message"));
        }

        @NotNull
        public List<String> getMessage() {
            return message;
        }
    }

    public static Messages load(File file) {
        Messages messages = new Messages();
        YamlConfig.getConfigResolver().loadOrDump(messages, file);
        messages.initMessagesMap();
        return messages;
    }

    @Ignore
    private Map<MessageKey, MessageHolder> messagesMap = new HashMap<>();

    /**
     * Returns the messages held by the specified {@link MessageKey} {@code key}
     *
     * @param key the key you want the messages for
     * @return messages of the specified key
     */
    @NotNull
    public List<String> getMessage(@NotNull MessageKey key) {
        Objects.requireNonNull(key, "key");
        if (messagesMap.isEmpty()) {
            throw new IllegalArgumentException("initMessagesMap not called!");
        }
        return messagesMap.get(key).getMessage();
    }

    /**
     * Returns the single message held by the specified {@link MessageKey} {@code key}
     *
     * @param key the key you want the message of
     * @return message of the specified key
     */
    @NotNull
    public String getSingleMessage(@NotNull MessageKey key) {
        Objects.requireNonNull(key, "key");
        if (messagesMap.isEmpty()) {
            throw new IllegalArgumentException("initMessagesMap not called!");
        }
        return messagesMap.get(key).getMessage().get(0);
    }

    /**
     * Inits the messages map. The messages map is the easier way of accessing all the messages.
     */
    public void initMessagesMap() {
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (AnnotationUtils.isIgnored(field)) {
                continue;
            }
            if (AnnotationUtils.isConfigObject(field)) {
                try {
                    Map<String, MessageHolder> deep = getDeepMessages(field, null);
                    for (Map.Entry<String, MessageHolder> entry : deep.entrySet()) {
                        MessageKey key = MessageKey.getByConfigKey(entry.getKey());
                        if (key == null) {
                            TreasuryPlugin.getInstance().logger().error(
                                    "The key '" + entry.getKey() + "' has not been registered "
                                            + "into MessageKey! Tell a developer right now!!!"
                            );
                            continue;
                        }
                        messagesMap.put(key, entry.getValue());
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("A field became inaccessible.");
                }
                continue;
            }
            String configKey = AnnotationUtils.getKey(field);
            MessageKey key = MessageKey.getByConfigKey(configKey);
            if (key == null) {
                TreasuryPlugin.getInstance().logger().error(
                        "The key '" + configKey + "' has not been registered "
                        + "into MessageKey! Tell a developer right now!!!"
                );
                continue;
            }
            messagesMap.put(key, getMessageHolder(field, null));
        }
    }

    private Map<String, MessageHolder> getDeepMessages(Field field, Object parent) throws IllegalAccessException {
        String key = AnnotationUtils.getKey(field);
        Object toAccess = field.get(parent == null ? this : parent);
        Map<String, MessageHolder> map = new HashMap<>();
        for (Field fToAccess : toAccess.getClass().getDeclaredFields()) {
            fToAccess.setAccessible(true);
            if (AnnotationUtils.isIgnored(fToAccess)) {
                continue;
            }
            if (AnnotationUtils.isConfigObject(fToAccess)) {
                Map<String, MessageHolder> obj = getDeepMessages(fToAccess, toAccess);
                for (Map.Entry<String, MessageHolder> entry : obj.entrySet()) {
                    map.put(key + "." + entry.getKey(), entry.getValue());
                }
                continue;
            }
            map.put(key + "." + AnnotationUtils.getKey(fToAccess), getMessageHolder(fToAccess, toAccess));
        }
        return map;
    }

    private MessageHolder getMessageHolder(Field field, Object toAccess) {
        Object getPass = toAccess == null ? this : toAccess;
        MessageHolder holder;
        if (String.class.isAssignableFrom(field.getType())) {
            try {
                holder = new MessageHolder(String.class.cast(field.get(getPass)));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Somehow field became inaccessible ; " + field.getName());
            }
        } else {
            try {
                holder = new MessageHolder((List<String>) field.get(getPass));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Somehow field became inaccessible ; " + field.getName());
            }
        }
        return holder;
    }

}
