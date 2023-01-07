/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node.holder;

import me.lokka30.treasury.api.common.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface NodeHolderGroup extends NodeHolder {

    @NotNull NamespacedKey getIdentifier();

}
