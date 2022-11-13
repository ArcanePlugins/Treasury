/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.config.messaging;

import com.mrivanplays.annotationconfig.core.annotations.ConfigObject;
import com.mrivanplays.annotationconfig.core.annotations.Ignore;
import com.mrivanplays.annotationconfig.core.annotations.Key;
import com.mrivanplays.annotationconfig.core.annotations.RawConfig;
import com.mrivanplays.annotationconfig.core.annotations.comment.Comment;
import com.mrivanplays.annotationconfig.core.serialization.DataObject;
import com.mrivanplays.annotationconfig.yaml.YamlConfig;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * All Treasury plugin messages.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
@Comment("## Treasury")
@Comment("Treasury is a modern code library for plugins.")
@Comment("GitHub Repository: <https://github.com/ArcanePlugins/Treasury/>")
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
public class Messages {

    @ConfigObject
    private CommonMessages common = new CommonMessages();

    @Comment("These messages are used across multiple other messages,")
    @Comment("so they have been grouped together in the \"common\" category.")
    public static class CommonMessages {

        @Comment("This text replaces the `%prefix%` placeholder in all applicable messages.")
        private String prefix = "&b&lTreasury:&7";

        @Comment("This message is sent when a user does not have permission to access something within Treasury,")
        @Comment("i.e., a command like `/treasury migrate`.")
        @Comment(" ")
        @Comment("Placeholders: %prefix%, %permission%")
        @Key("no-permission")
        private List<String> noPermission = Collections.singletonList(
                "%prefix% You don't have access to that &8(&7requires permission &b%permission%&8)&7.");

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
        @Comment("\"List item 1&7, &bList item 2&7, &bList item 3\"")
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
                    "%prefix% For a list of available subcommands, try '&b/%label% help&7'."
            );

            @Comment("Placeholders: %prefix%, %label%, %subcomand%")
            @Key("invalid-usage-specified")
            private List<String> invalidUsageSpecified = Arrays.asList(
                    "%prefix% Invalid subcommand '&b%subcommand%&7'.",
                    "%prefix% For a list of available subcommands, try '&b/%label% help&7'."
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.downloadLatest.invalid-usage")
            private String downloadLatestInvalidUsage = "%prefix% Invalid usage, try '&b/%label% downloadLatest&7'.";

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.downloadLatest.started")
            private String downloadLatestDownloadStarted = "%prefix% &7Download process started.";

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.downloadLatest.error-occurred")
            private String downloadLatestIOException = "%prefix% &7Unable to retrieve information/download jar. See console for more details.";

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.downloadLatest.couldnt-parse")
            private String downloadLatestCouldntParse = "%prefix% &7Something went wrong whilst parsing latest build download data. Please notify a Treasury developer ASAP!";

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.downloadLatest.already-latest")
            private String downloadLatestAlreadyLatest = "%prefix% &7You are already running the latest version of Treasury!";

            @Comment("Placeholders: %prefix%, %time%")
            @Key("subcommands.downloadLatest.success")
            private String downloadLatestSuccess = "%prefix% &7Successfully downloaded the latest Treasury jar (took &b%time%ms&7). Please restart your server in order for it to take effect.";

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.economy.invalid-usage-unspecified")
            private List<String> economyInvalidUsageUnspecified = Arrays.asList(
                    "%prefix% Invalid usage - please specify a subcommand.",
                    "%prefix% For a list of available subcommands, try '&b/%label% economy help&7'."
            );

            @Comment("Placeholders: %prefix%, %label%, %subcomand%")
            @Key("subcommands.economy.invalid-usage-specified")
            private List<String> economyInvalidUsageSpecified = Arrays.asList(
                    "%prefix% Invalid subcommand '&b%subcommand%&7'.",
                    "%prefix% For a list of available subcommands, try '&b/%label% economy help&7'."
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.economy.info.invalid-usage")
            private List<String> infoEconomyInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try '&b/%label% economy help&7'.");

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.economy.info.economy-provider-unavailable")
            private List<String> infoEconomyProviderUnavailable = Arrays.asList(
                    "&f&nEconomy Provider",
                    "&8 &m->&7 You don't have an Economy Provider installed.",
                    " "
            );

            @Comment("Placeholders: %prefix, %name%, %priority%, %primary-currency%")
            @Key("subcommands.economy.info.economy-provider-available")
            private List<String> infoEconomyProviderAvailable = Arrays.asList(
                    "&f&nEconomy Provider",
                    "&8 &m->&7 Name: &b%name%",
                    "&8 &m->&7 Priority: &b%priority%",
                    "&8 &m->&7 Primary currency: &b%primary-currency%",
                    " "
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.economy.help.invalid-usage")
            private List<String> helpEconomyInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try '&b/%label% economy help&7'.");

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.economy.help.available-commands")
            private List<String> helpEconomyAvailableCommands = Arrays.asList(
                    "%prefix% Available commands:",
                    " &8&m->&b /treasury economy help &8- &7view a list of Treasury's commands.",
                    " &8&m->&b /treasury economy info &8- &7view info about Treasury.",
                    " &8&m->&b /treasury economy migrate &8- &7migrate from one economy provider to another."
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.help.invalid-usage")
            private List<String> helpInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try '&b/%label% help&7'.");

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.help.available-commands")
            private List<String> helpAvailableCommands = Arrays.asList(
                    "%prefix% Available commands:",
                    " &8&m->&b /treasury help &8- &7view a list of Treasury's commands.",
                    " &8&m->&b /treasury info &8- &7view info about Treasury.",
                    " &8&m->&b /treasury reload &8- &7re-load all of Treasury's configuration files.",
                    " &8&m->&b /treasury downloadLatest &8- &7downloads the latest Treasury plugin jar.",
                    " &8&m->&b /treasury economy &8 - &7economy specific commands"
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.info.invalid-usage")
            private List<String> infoInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try '&b/%label% info&7'.");

            @Comment("Placeholders: %prefix%, %version%, %description%,")
            @Comment("              %credits%, %repository%")
            @Key("subcommands.info.treasury")
            private List<String> infoTreasury = Arrays.asList(
                    "&f&nAbout Treasury",
                    "&8 &m->&7 Running &bTreasury v%version%",
                    "&8 &m->&7 Description: &b%description%",
                    "&8 &m->&7 Made possible by: &bSee &n%credits%",
                    "&8 &m->&7 Learn more at: &b&n%repository%",
                    " "
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.info.misc-info")
            private List<String> infoMiscInfo = Arrays.asList(
                    "&f&nMiscellaneous Info:",
                    "&8 &m->&7 For a list of commands, run '&b/treasury help&7'."
            );

            @Comment("Placeholders: %prefix%, %label%, %providers%")
            @Key("subcommands.migrate.invalid-usage")
            private List<String> migrateInvalidUsage = Arrays.asList(
                    "%prefix% Invalid usage, try '&b/%label% migrate <providerFrom> <providerTo>&7'.",
                    "%prefix% Valid economy providers: &b%providers%&7."
            );

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.migrate.requires-two-providers")
            private List<String> migrateRequiresTwoProviders = Collections.singletonList(
                    "%prefix% You can't use this subcommand unless you have 2 economy providers set up.");

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
                    "%prefix% You must specify a valid economy provider to migrate to.",
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
                    "%prefix% An internal error occurred whilst attempting to migrate. Please check console for more information.");

            @Comment("Placeholders: %prefix%, %time%, %player-accounts%,")
            @Comment("              %non-migrated-currencies%")
            @Key("subcommands.migrate.finished-migration")
            private List<String> migrateFinishedMigration = Arrays.asList(
                    "%prefix% Migration complete! Statistics:",
                    "&8 &m->&7 Took &b%time%ms&7.",
                    "&8 &m->&7 Processed &b%player-accounts%&7 player accounts.",
                    "&8 &m->&7 Processed &b%nonplayer-accounts%&7 non player accounts.",
                    "&8 &m->&7 Non-migrated currencies: &b%non-migrated-currencies%&7."
            );

            @Comment("Placeholders: %prefix%, %label%")
            @Key("subcommands.reload.invalid-usage")
            private List<String> reloadInvalidUsage = Collections.singletonList(
                    "%prefix% Invalid usage, try '&b/%label% reload&7'.");

            @Comment("Placeholders: %prefix%")
            @Key("subcommands.reload.reload-start")
            private List<String> reloadStart = Collections.singletonList(
                    "%prefix% Reloading Treasury...");

            @Comment("Placeholders: %prefix%, %time%")
            @Key("subcommands.reload.reload-complete")
            private List<String> reloadComplete = Collections.singletonList(
                    "%prefix% Reload successful &8(&7took &b%time%ms&8)&7.");

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

    @RawConfig
    private DataObject raw;

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
            throw new IllegalArgumentException("initMessagesMap was not called.");
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
            throw new IllegalArgumentException("initMessagesMap was not called.");
        }
        return messagesMap.get(key).getMessage().get(0);
    }

    /**
     * Inits the messages map. The messages map is the easier way of accessing all the messages.
     */
    public void initMessagesMap() {
        for (MessageKey key : MessageKey.values()) {
            String[] parts = key.asConfigKey().split("\\.");
            DataObject dataObject = parts.length == 1
                    ? this.raw.get(parts[0])
                    : getDataObject(parts);
            Object object = dataObject.getAsObject();
            MessageHolder holder;
            if (String.class.isAssignableFrom(object.getClass())) {
                holder = new MessageHolder((String) object);
            } else {
                holder = new MessageHolder((List<String>) object);
            }
            this.messagesMap.put(key, holder);
        }
    }

    private DataObject getDataObject(String[] parts) {
        DataObject ret = this.raw.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            ret = ret.get(parts[i]);
        }
        return ret;
    }

}
