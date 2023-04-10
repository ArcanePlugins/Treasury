/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.context;

import java.util.Objects;
import java.util.function.BiPredicate;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.permission.node.holder.NodeHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a context, in which a specific thing, a {@link me.lokka30.treasury.api.permission.node.Node} for example, applies.
 *
 * @author <a href="ivan@mrivanplays.com">Ivan Pekov</a>
 */
public interface Context {

    /**
     * Returns the global context. If only this context is applied to a
     * {@link me.lokka30.treasury.api.permission.node.Node} for example, that node will always
     * apply without any other interference.
     */
    Context GLOBAL = Context.of(NamespacedKey.of("treasury", "global"), ($, $1) -> true);

    /**
     * Creates a new context.
     *
     * @param key       context key
     * @param condition context condition
     * @return new context
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static Context of(
            @NotNull NamespacedKey key, @NotNull BiPredicate<NodeHolder, String> condition
    ) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(condition, "condition");
        return new Context() {
            @Override
            public @NotNull NamespacedKey getKey() {
                return key;
            }

            @Override
            public @NotNull BiPredicate<NodeHolder, String> getCondition() {
                return condition;
            }
        };
    }

    /**
     * Returns the {@link NamespacedKey key} of this context.
     *
     * @return key
     */
    @NotNull NamespacedKey getKey();

    /**
     * Returns the condition under which this context will apply.
     *
     * @return condition
     */
    @NotNull BiPredicate<NodeHolder, String> getCondition();

}
