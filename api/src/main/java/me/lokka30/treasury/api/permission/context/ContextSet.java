/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.context;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a set of {@link Context contexts}, which can be applied for a
 * {@link me.lokka30.treasury.api.permission.node.Node}.
 * <p>Class is immutable. Creating it via {@link #of(Context...)}, the returned object is always
 * the same.
 *
 * @author <a href="mailto:ivan@mrivanplays.com">Ivan Pekov</a>
 * @since 2.0.0
 */
public final class ContextSet implements Iterable<Context> {

    private static final ContextSet EMPTY = new ContextSet(Collections.emptyMap());

    /**
     * Returns the empty context set constant.
     *
     * @return empty context set
     */
    @NotNull
    public static ContextSet empty() {
        return EMPTY;
    }

    /**
     * Creates a new context set of the specified {@link Context contexts}
     *
     * @param contexts contexts the created context set will contain
     * @return new context set
     */
    @NotNull
    public static ContextSet of(@NotNull Context @NotNull ... contexts) {
        Objects.requireNonNull(contexts, "contexts");
        if (contexts.length == 0) {
            return empty();
        }
        Map<String, Context> map = new ConcurrentHashMap<>();
        for (Context context : contexts) {
            map.put(context.getKey(), context);
        }
        return new ContextSet(map);
    }

    private Map<String, Context> contexts;

    @Contract(pure = true)
    private ContextSet(Map<String, Context> contexts) {
        this.contexts = contexts;
    }

    /**
     * Returns a {@link Optional}, which will be fulfilled with a {@link Context} if this context
     * set holds a context, identified by the specified {@code contextKey}.
     *
     * @param contextKey context key
     * @return context if such is found, empty optional otherwise
     */
    @NotNull
    public Optional<Context> getContext(@NotNull String contextKey) {
        Objects.requireNonNull(contextKey, "contextKey");
        return Optional.ofNullable(this.contexts.get(contextKey));
    }

    /**
     * Returns whether a {@link Context} is held under the specified {@code contextKey} in this
     * context set.
     *
     * @param contextKey context key
     * @return true if context is held, false otherwise
     */
    public boolean hasContext(@NotNull String contextKey) {
        Objects.requireNonNull(contextKey, "contextKey");
        return contexts.containsKey(contextKey);
    }

    /**
     * Returns this context set as a mutable {@link Set}. Modifying the returned set will not
     * affect the context set object this method is called from.
     *
     * @return set copy representation of this context set
     */
    @NotNull
    public Set<Context> asSet() {
        if (this.contexts.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Context> ret = ConcurrentHashMap.newKeySet();
        ret.addAll(this.contexts.values());
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<Context> iterator() {
        Iterator<Context> handle = this.contexts.values().iterator();
        // Iterator#remove default impl throws a UnsupportedOperationException which suits our needs
        // we don't return this.contexts.values().iterator() directly as it is likely to have a
        // remove() impl
        return new Iterator<Context>() {
            @Override
            public boolean hasNext() {
                return handle.hasNext();
            }

            // NotNull contract because per Iterator javadoc, Iterator#next throws
            // NoSuchElementException if !hasNext() = true
            @NotNull
            @Override
            public Context next() {
                return handle.next();
            }
        };
    }

}
