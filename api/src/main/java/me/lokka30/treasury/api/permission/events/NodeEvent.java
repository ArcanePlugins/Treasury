/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.events;

import me.lokka30.treasury.api.common.Cause;
import me.lokka30.treasury.api.permission.node.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class NodeEvent {

    private Result result;

    public NodeEvent(@NotNull Result initialResult) {
        this.result = initialResult;
    }

    @NotNull
    public Node getNode() {
        return result.getNode();
    }

    @NotNull
    public Cause<?> getCause() {
        return result.getCause();
    }

    @NotNull
    public Result asResult() {
        return this.result;
    }

    public void withNewResult(@NotNull Result result) {
        this.result = result;
    }

    public static class Result {

        @NotNull
        @Contract(value = "_, _ -> new", pure = true)
        public static Result of(@NotNull Node node, @NotNull Cause<?> cause) {
            return new Result(node, cause);
        }

        private final Node node;
        private final Cause<?> cause;

        private Result(@NotNull Node node, @NotNull Cause<?> cause) {
            this.node = node;
            this.cause = cause;
        }

        @NotNull
        public Node getNode() {
            return this.node;
        }

        @NotNull
        public Cause<?> getCause() {
            return this.cause;
        }
    }

}
