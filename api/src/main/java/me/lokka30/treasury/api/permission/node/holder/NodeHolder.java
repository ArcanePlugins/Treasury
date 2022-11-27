/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.node.holder;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.permission.context.Context;
import me.lokka30.treasury.api.permission.node.Node;
import me.lokka30.treasury.api.permission.node.type.NodeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NodeHolder {

    @NotNull String getIdentifier();

    @Nullable NodeHolder parentNodeHolder();

    @NotNull CompletableFuture<Response<Collection<Node<?>>>> allNodes();

    @NotNull CompletableFuture<Response<Map<String, NodeType<?>>>> allNodesKeys();

    @NotNull <Data> CompletableFuture<Response<Collection<Node<Data>>>> allNodesWithType(@NotNull NodeType<Data> nodeType);

    @NotNull CompletableFuture<Response<Collection<Node<?>>>> allNodesWithContext(@NotNull Context context);

    @NotNull CompletableFuture<Response<TriState>> hasNode(@NotNull Node<?> node);

    @NotNull CompletableFuture<Response<TriState>> hasNode(@NotNull String nodeKey);

    @NotNull <Data> CompletableFuture<Response<Optional<Node<Data>>>> retrieveNodeWithKey(@NotNull String key);

    /**
     * Retrieves all the {@link Context contexts} this {@code NodeHolder} holds {@link Node nodes}
     * under.
     *
     * @return
     */
    @NotNull CompletableFuture<Response<Collection<NodeHolder>>> retrieveHeldContexts();

    @NotNull <Data> CompletableFuture<Response<TriState>> insertOrModifyNode(@NotNull Node<Data> node);

    @NotNull CompletableFuture<Response<TriState>> removeNode(@NotNull String nodeKey);

}
