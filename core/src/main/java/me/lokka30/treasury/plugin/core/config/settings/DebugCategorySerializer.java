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
