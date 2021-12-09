/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.utils;

import com.google.gson.Gson;
import java.util.Objects;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import org.jetbrains.annotations.NotNull;

public class Utils {

    public static final Gson GSON = new Gson();

    /**
     * Checks if the player has permission for the command.
     * If the player does not have permission then a notification
     * will be sent to them regarding their lack of permission.
     *
     * @param source     who ran a command and is being checked for the permission.
     * @param permission to check.
     * @return whether the sender has the specified permission.
     * @author lokka30
     * @since v1.0.0
     */
    public static boolean checkPermissionForCommand(@NotNull CommandSource source, @NotNull String permission) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(permission, "permission");
        if (source.hasPermission(permission)) {
            return true;
        } else {
            source.sendMessage(Message.of(
                            MessageKey.NO_PERMISSION,
                            MessagePlaceholder.placeholder("%permission%", permission)
                    )
            );
            return false;
        }
    }

    @NotNull
    public static String getYesNoStateMessage(final boolean state) {
        Messages messages = TreasuryPlugin.getInstance().configAdapter().getMessages();
        return state
                ? messages.getSingleMessage(MessageKey.STATE_YES)
                : messages.getSingleMessage(MessageKey.STATE_NO);
    }

    @NotNull
    public static String formatListMessage(@NotNull final Iterable<String> list) {
        Objects.requireNonNull(list, "list");
        final String delimiter = TreasuryPlugin
                .getInstance()
                .configAdapter()
                .getMessages()
                .getSingleMessage(MessageKey.LIST_DELIMITER);

        return String.join(delimiter, list);
    }

    public static PluginVersion.ComparisonResult compareAPIVersions(EconomyAPIVersion version1, EconomyAPIVersion version2) {
        if (version1.getMajorRevision() < version2.getMajorRevision()) {
            return PluginVersion.ComparisonResult.OLDER;
        } else if (version1.getMajorRevision() > version2.getMajorRevision()) {
            return PluginVersion.ComparisonResult.NEWER;
        } else {
            if (version1.getMinorRevision() < version2.getMinorRevision()) {
                return PluginVersion.ComparisonResult.OLDER;
            } else if (version1.getMinorRevision() > version2.getMinorRevision()) {
                return PluginVersion.ComparisonResult.NEWER;
            } else {
                return PluginVersion.ComparisonResult.EQUAL;
            }
        }
    }

    private Utils() {
        throw new IllegalArgumentException("Initialization of utility-type class.");
    }

}
