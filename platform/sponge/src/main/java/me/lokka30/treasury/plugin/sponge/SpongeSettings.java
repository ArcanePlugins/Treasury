/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge;

import com.mrivanplays.annotationconfig.core.annotations.Key;
import com.mrivanplays.annotationconfig.core.annotations.comment.Comment;
import com.mrivanplays.annotationconfig.core.resolver.settings.ACDefaultSettings;
import com.mrivanplays.annotationconfig.yaml.YamlConfig;
import java.io.File;
import me.lokka30.treasury.plugin.core.config.settings.Settings;

public class SpongeSettings extends Settings {

    public static SpongeSettings loadSponge(File file) {
        registerSerializers();
        SpongeSettings settings = new SpongeSettings();
        YamlConfig.getConfigResolver().loadOrDump(
                settings,
                file,
                ACDefaultSettings
                        .getDefault()
                        .copy()
                        .put(ACDefaultSettings.FIND_PARENT_FIELDS, true)
                        .put(ACDefaultSettings.SHOULD_REVERSE_FIELDS, false)
        );
        return settings;
    }

    @Comment("======SPONGE SPECIFIC OPTIONS======")
    @Comment("Whether to sync/wrap implementations of Treasury's Economy API to")
    @Comment("Sponge's Economy API")
    @Key("enable-economy-api-sync")
    private boolean enableEconomyApiSync = true;

    public boolean shouldSyncEcoApi() {
        return enableEconomyApiSync;
    }

}
