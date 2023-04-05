/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node.holder;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.permission.context.ContextSet;
import me.lokka30.treasury.api.permission.node.type.NodeType;
import org.jetbrains.annotations.NotNull;

public interface NodeHolderPlayer extends NodeHolder, Cause.Player {

    /**
     * Returns whether the specified {@link ContextSet context set} applies for this player node
     * holder.
     * <p>By "applies" it is meant that all the
     * {@link me.lokka30.treasury.api.permission.context.Context contexts} defined in the
     * specified context set's conditions are met.
     *
     * @param contextSet context set to probe
     * @return applies or not.
     */
    boolean contextSetApplies(@NotNull ContextSet contextSet);

    @NotNull
    default CompletableFuture<TriState> hasPermission(@NotNull String nodeKey) {
        Objects.requireNonNull(nodeKey, "nodeKey");
        return retrieveNode(nodeKey, NodeType.PERMISSION).thenApply(opt -> opt
                .map(triStateNode -> TriState.fromBoolean(this.contextSetApplies(triStateNode.contexts())))
                .orElse(TriState.UNSPECIFIED));
    }

}
