/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node.holder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.permission.context.ContextSet;
import me.lokka30.treasury.api.permission.node.Node;
import me.lokka30.treasury.api.permission.node.PermissionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NodeHolder {

    @Nullable NodeHolder parentNodeHolder();

    @NotNull CompletableFuture<Collection<Node>> allNodes();

    @NotNull CompletableFuture<Map<String, Node.Type>> allNodesKeys();

    @NotNull
    default <T extends Node> CompletableFuture<Collection<T>> allNodesWithType(@NotNull Class<T> type) {
        return allNodes().thenApply(nodes -> {
            Set<T> ret = new HashSet<>();
            for (Node node : nodes) {
                if (node.type().getNodeClass().isAssignableFrom(type)) {
                    ret.add((T) node);
                }
            }
            return ret;
        });
    }

    @NotNull CompletableFuture<Boolean> hasNode(@NotNull String key);

    @NotNull CompletableFuture<Optional<Node>> retrieveNode(@NotNull String key);

    @NotNull CompletableFuture<Boolean> insertOrModifyNode(@NotNull Node node);

    @NotNull CompletableFuture<Boolean> removeNode(@NotNull String nodeKey);

    @NotNull CompletableFuture<TriState> retrievePermissionValue(@NotNull String permissionNodeKey);

    @NotNull CompletableFuture<TriState> retrievePermissionValue(
            @NotNull String permissionNodeKey, @NotNull ContextSet specificContextSet
    );

    @NotNull
    default CompletableFuture<Boolean> hasPermission(@NotNull String permissionNodeKey) {
        return retrievePermissionValue(permissionNodeKey).thenApply(state -> state == TriState.TRUE);
    }

    @NotNull
    default CompletableFuture<Boolean> hasPermission(
            @NotNull String permissionNodeKey, @NotNull ContextSet specificContextSet
    ) {
        return retrievePermissionValue(
                permissionNodeKey,
                specificContextSet
        ).thenApply(state -> state == TriState.TRUE);

    }

    @NotNull
    default CompletableFuture<Boolean> hasPermission(@NotNull PermissionNode permissionNode) {
        return hasPermission(permissionNode.key(), permissionNode.contextSet());
    }

}
