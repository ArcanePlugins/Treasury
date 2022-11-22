/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.Response;
import org.jetbrains.annotations.NotNull;

public interface NodeHolder {

    @NotNull UUID getUniqueId();

    @NotNull CompletableFuture<Response<Collection<Node<?>>>> allNodes();

    @NotNull CompletableFuture<Response<TriState>> hasNode(@NotNull Node<?> node);

    @NotNull <Data> CompletableFuture<Response<Collection<Node<Data>>>> allNodesWithType(@NotNull NodeType<Data> nodeType);

    @NotNull CompletableFuture<Response<Collection<Node<?>>>> allNodesWithContext(@NotNull Context context);

    @NotNull <Data> CompletableFuture<Response<TriState>> modifyNode(
            @NotNull Node<Data> node, @NotNull Data data
    );

}
