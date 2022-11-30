/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.permission.context;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ContextSet {

    private static final ContextSet EMPTY = new ContextSet(Collections.emptyMap());

    @NotNull
    public static ContextSet empty() {
        return EMPTY;
    }

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

    @NotNull
    public Optional<Context> getContext(@NotNull String contextKey) {
        Objects.requireNonNull(contextKey, "contextKey");
        return Optional.ofNullable(this.contexts.get(contextKey));
    }

    public void registerContext(@NotNull Context context) {
        Objects.requireNonNull(context, "context");
        if (!this.contexts.containsKey(context.getKey())) {
            this.contexts.put(context.getKey(), context);
            return;
        }
        throw new IllegalArgumentException("Context already registered in this context set");
    }

    public boolean hasContext(@NotNull String contextKey) {
        Objects.requireNonNull(contextKey, "contextKey");
        return contexts.containsKey(contextKey);
    }

    @NotNull
    public Set<Context> asSet() {
        if (this.contexts.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Context> ret = ConcurrentHashMap.newKeySet();
        ret.addAll(this.contexts.values());
        return ret;
    }

}
