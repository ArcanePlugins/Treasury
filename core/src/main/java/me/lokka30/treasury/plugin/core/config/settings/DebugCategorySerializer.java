/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.config.settings;

import com.mrivanplays.annotationconfig.core.serialization.DataObject;
import com.mrivanplays.annotationconfig.core.serialization.FieldTypeSerializer;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A serializer of debug categories
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public class DebugCategorySerializer implements FieldTypeSerializer<List<DebugCategory>> {

    public static final DebugCategorySerializer INSTANCE = new DebugCategorySerializer();

    private DebugCategorySerializer() {}

    @Override
    public List<DebugCategory> deserialize(DataObject data, Field field) {
        List<String> stringList = data.getList(String.class);
        List<DebugCategory> ret = new ArrayList<>();
        for (String val : stringList) {
            try {
                ret.add(DebugCategory.valueOf(val.toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException e) {
                TreasuryPlugin.getInstance().logger().error(
                        "Invalid DebugCategory '&b" + val + "&7' specified in &bsettings.yml&7 at location "
                                + "'&bdebug.enabled-categories.list&7'! Please fix this ASAP."
                );
            }
        }
        return ret;
    }

    @Override
    public DataObject serialize(List<DebugCategory> value, Field field) {
        List<String> values = new ArrayList<>();
        if (value.isEmpty()) {
            return new DataObject(values);
        }
        for (DebugCategory category : value) {
            values.add(category.name().toLowerCase(Locale.ROOT));
        }
        return new DataObject(values);
    }
}
