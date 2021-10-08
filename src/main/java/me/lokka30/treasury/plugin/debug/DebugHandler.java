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

package me.lokka30.treasury.plugin.debug;

import me.lokka30.treasury.plugin.Treasury;
import me.lokka30.treasury.plugin.misc.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

public class DebugHandler {

    @NotNull
    private final Treasury main;

    public DebugHandler(@NotNull final Treasury main) { this.main = main; }

    @NotNull
    private final HashSet<DebugCategory> enabledCategories = new HashSet<>();

    public void loadEnabledCategories() {
        final HashSet<DebugCategory> listedCategories = new HashSet<>();
        for(String listedCategoryStr : main.settingsCfg.getConfig().getStringList("debug.enabled-categories.list")) {
            try {
                listedCategories.add(DebugCategory.valueOf(listedCategoryStr));
            } catch(IllegalArgumentException ex) {
                Utils.logger.error("Invalid DebugCategory '&b" + listedCategoryStr + "&7' specified in &bsettings.yml&7 at location '&bdebug.enabled-categories.list&7'! Please fix this ASAP.");
            }
        }

        switch(main.settingsCfg.getConfig().getString("debug.enabled-categories.mode", "NOT_SPECIFIED").toUpperCase(Locale.ROOT)) {
            case "WHITELIST":
                enabledCategories.addAll(listedCategories);
                break;
            case "BLACKLIST":
                enabledCategories.addAll(Arrays.asList(DebugCategory.values()));
                enabledCategories.removeAll(listedCategories);
                break;
            default:
                Utils.logger.error("Invalid mode specified in &bsettings.yml&7 at location '&bdebug.enabled-categories.mode&7'! You can only use '&bWHITELIST&7' or '&bBLACKLIST&7'. Please fix this ASAP.");
                break;
        }
    }

    public boolean isCategoryEnabled(@NotNull final DebugCategory debugCategory) {
        return enabledCategories.contains(debugCategory);
    }
}
