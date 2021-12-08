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

package me.lokka30.treasury.plugin.core.config.settings;

import com.google.common.reflect.TypeToken;
import com.mrivanplays.annotationconfig.core.annotations.ConfigObject;
import com.mrivanplays.annotationconfig.core.annotations.Ignore;
import com.mrivanplays.annotationconfig.core.annotations.Key;
import com.mrivanplays.annotationconfig.core.annotations.comment.Comment;
import com.mrivanplays.annotationconfig.core.serialization.SerializerRegistry;
import com.mrivanplays.annotationconfig.yaml.YamlConfig;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;
import me.lokka30.treasury.plugin.core.debug.DebugCategoryMode;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the settings file of treasury.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
@Comment("## Treasury")
@Comment("Treasury is a modern code library for plugins.")
@Comment("GitHub Repository: <https://github.com/lokka30/Treasury/>")
@Comment(" ")
@Comment("## About this File")
@Comment("Welcome to the settings.yml file, here you may configure")
@Comment("parts of the plugin. For most servers, this file can be")
@Comment("left alone as it usually contains settings tailored to")
@Comment("more experienced server owners.")
@Comment(" ")
@Comment("## Applying Changes")
@Comment("Whenever you have finished making your changes to this")
@Comment("configuration file, please save it, then run")
@Comment("`/treasury reload` if your server is already running.")
@SuppressWarnings("FieldMayBeFinal")
public class Settings {

    public static Settings load(File file) {
        Type debugCategoryList = new TypeToken<List<DebugCategory>>() {}.getType();
        if (!SerializerRegistry.INSTANCE.hasSerializer(debugCategoryList)) {
            SerializerRegistry.INSTANCE.registerSerializer(debugCategoryList, DebugCategorySerializer.INSTANCE);
        }
        Settings settings = new Settings();
        YamlConfig.getConfigResolver().loadOrDump(settings, file);
        return settings;
    }

    @Key("update-checker")
    @ConfigObject
    private UpdateCheckerSettings updateChecker = new UpdateCheckerSettings();

    @Comment("## Settings regarding the update checker.")
    @Comment("It is recommended you leave this enabled.")
    @Comment("The update checker makes a single check to the SpigotMC")
    @Comment("website to see if you are running the latest version of")
    @Comment("Treasury. It is asynchronous, and only sends a message")
    @Comment("if there is a new version available.")
    public static class UpdateCheckerSettings {

        @Comment("## Should the update checker be enabled?")
        @Comment("This setting allows you to enable/disable the update checker.")
        @Comment("Type: Boolean | Default: `true`")
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }
    }

    @ConfigObject
    private DebugSettings debug = new DebugSettings();

    @Comment("## Settings regarding the debug logger.")
    @Comment("The debug logger is a system in Treasury only accessible")
    @Comment("to server administrators by default. It is used by Treasury")
    @Comment("developers on their test servers to assist in diagnosing any")
    @Comment("issues that may be present. It is recommended that all server")
    @Comment("owners leave this area alone as it will send a lot of spam to")
    @Comment("your console. Developers can configure what categories of debug")
    @Comment("logs they want to see to filter out all the messages.")
    public static class DebugSettings {

        @ConfigObject
        @Key("enabled-categories")
        private EnabledCategories enabledCategories = new EnabledCategories();

        @Comment("## What debug-categories should be sent to the console?")
        @Comment("This setting allows you to tune what categories of debug messages")
        @Comment("will be sent to the server's console. The list functions as a")
        @Comment("blacklist or whitelist, as set by the `mode`.")
        public static class EnabledCategories {

            @Comment("## What list mode should the debug logger use?")
            @Comment("`WHITELIST` - only the specified categories in the list")
            @Comment("              will be enabled.")
            @Comment("`BLACKLIST` - only the non-specified categories in the list")
            @Comment("              will be enabled.")
            @Comment("Type: `String` (DebugCategoryMode constants) | Default: `WHITELIST`")
            private DebugCategoryMode mode = DebugCategoryMode.WHITELIST;

            @Comment("## Contents of the debug logger categories list.")
            @Comment("Add entries to the whitelist/blacklist here.")
            @Comment("Use debug categories from the `DebugCategory` enum, available at GitHub.")
            @Comment("Type: `List<String>` (DebugCategory constants) | Default: `[]` (empty)")
            private List<DebugCategory> list = Collections.emptyList();

            public DebugCategoryMode getMode() {
                return mode;
            }

            public List<DebugCategory> getList() {
                return list;
            }
        }

        public EnabledCategories getEnabledCategories() {
            return enabledCategories;
        }
    }

    public boolean checkForUpdates() {
        return updateChecker.isEnabled();
    }

    @Ignore
    private List<DebugCategory> enabledCategories;

    public List<DebugCategory> getDebugCategories() {
        if (enabledCategories != null) {
            return enabledCategories;
        }
        enabledCategories = new ArrayList<>();
        List<DebugCategory> specified = debug.getEnabledCategories().getList();
        DebugCategoryMode mode = debug.getEnabledCategories().getMode();
        switch (mode) {
            case WHITELIST:
                enabledCategories.addAll(specified);
                break;
            case BLACKLIST:
                enabledCategories.addAll(Arrays.asList(DebugCategory.values()));
                enabledCategories.removeAll(specified);
                break;
            default:
                TreasuryPlugin.getInstance().logger().error(
                        "Invalid mode specified in &bsettings.yml&7 at location "
                                + "'&bdebug.enabled-categories.mode&7'! "
                                + "You can only use '&bWHITELIST&7' or '&bBLACKLIST&7'. Please fix this ASAP"
                );
        }
        return enabledCategories;
    }
}
