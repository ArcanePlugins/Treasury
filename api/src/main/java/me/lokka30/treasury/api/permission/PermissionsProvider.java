/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.permission.node.NodeFactory;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import me.lokka30.treasury.api.permission.node.holder.NodeHolderPlayer;
import org.jetbrains.annotations.NotNull;

public interface PermissionsProvider {

    @NotNull NodeFactory nodeFactory();

    @NotNull CompletableFuture<Response<NodeHolderPlayer>> retrievePlayerNodeHolder(@NotNull UUID uuid);

    @NotNull CompletableFuture<Response<NodeHolder>> retrieveNodeHolder(@NotNull String identifier);

    @NotNull CompletableFuture<Response<Collection<UUID>>> allPlayerNodeHolders();

    @NotNull CompletableFuture<Response<Collection<String>>> allNodeHolders();

}
