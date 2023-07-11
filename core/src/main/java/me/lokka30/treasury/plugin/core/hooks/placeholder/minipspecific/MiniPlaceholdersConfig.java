/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.hooks.placeholder.minipspecific;

import com.mrivanplays.annotationconfig.core.annotations.Key;
import com.mrivanplays.annotationconfig.core.annotations.RawConfig;
import com.mrivanplays.annotationconfig.core.annotations.comment.Comment;
import com.mrivanplays.annotationconfig.core.serialization.DataObject;
import com.mrivanplays.annotationconfig.yaml.YamlConfig;
import java.io.File;
import me.lokka30.treasury.plugin.core.hooks.placeholder.PlaceholdersConfig;
import org.jetbrains.annotations.NotNull;

@Comment("Configuration for the MiniPlaceholders Hook")
@Comment("If you've got MiniPlaceholders installed,")
@Comment("you can configure how Treasury hooks to it here.")
public class MiniPlaceholdersConfig implements PlaceholdersConfig {

    public static MiniPlaceholdersConfig load(File file) {
        MiniPlaceholdersConfig config = new MiniPlaceholdersConfig();
        YamlConfig.getConfigResolver().loadOrDump(config, file);
        return config;
    }

    @Key("baltop.enabled")
    private boolean baltopEnabled = false;

    @Key("baltop.cache_size")
    private int baltopCacheSize = 100;

    @Key("baltop.check_delay")
    private int baltopCheckDelay = 30;

    @Key("balance.cache_check_delay")
    private int balanceCheckDelay = 60;

    @Key("formatting.thousands")
    private String thousandsFormatting = "k";

    @Key("formatting.millions")
    private String millionsFormatting = "M";

    @Key("formatting.billions")
    private String billionsFormatting = "B";

    @Key("formatting.trillions")
    private String trillionsFormatting = "T";

    @Key("formatting.quadrillions")
    private String quadrillionsFormatting = "Q";

    @RawConfig
    private DataObject rawConfig;


    @Override
    public String getString(@NotNull final String key, final String def) {
        if (key.indexOf('.') != -1) {
            String[] split = key.split("\\.");
            return rawConfig.get(split[0]).get(split[1]).getAsString();
        } else {
            return rawConfig.get(key).getAsString();
        }
    }

    @Override
    public int getInt(@NotNull final String key, final int def) {
        if (key.indexOf('.') != -1) {
            String[] split = key.split("\\.");
            return rawConfig.get(split[0]).get(split[1]).getAsInt();
        } else {
            return rawConfig.get(key).getAsInt();
        }
    }

    @Override
    public boolean getBoolean(@NotNull final String key, final boolean def) {
        if (key.indexOf('.') != -1) {
            String[] split = key.split("\\.");
            return rawConfig.get(split[0]).get(split[1]).getAsBoolean();
        } else {
            return rawConfig.get(key).getAsBoolean();
        }
    }

}
