/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node.holder;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface NodeHolderPlayer extends NodeHolder {

    @NotNull UUID getUniqueId();

    @Override
    @NotNull
    default String getIdentifier() {
        return this.getUniqueId().toString();
    }

}
