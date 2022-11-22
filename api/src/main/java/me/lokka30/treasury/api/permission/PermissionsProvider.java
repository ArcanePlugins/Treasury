/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.response.Response;
import org.jetbrains.annotations.NotNull;

public interface PermissionsProvider {

    @NotNull
    NodeFactory obtainNodeFactory();

    @NotNull
    CompletableFuture<Response<NodeHolder>> retrieveNodeHolder(@NotNull UUID uuid);
}
