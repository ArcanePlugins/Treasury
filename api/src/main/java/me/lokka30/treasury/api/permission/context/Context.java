/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.context;

import java.util.Objects;
import java.util.function.BiPredicate;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Context {

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static Context of(@NotNull String key, @NotNull BiPredicate<NodeHolder, String> condition) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(condition, "condition");
        return new Context() {
            @Override
            public @NotNull String getKey() {
                return key;
            }

            @Override
            public @NotNull BiPredicate<NodeHolder, String> getCondition() {
                return condition;
            }
        };
    }

    @NotNull String getKey();

    @NotNull BiPredicate<NodeHolder, String> getCondition();

}
