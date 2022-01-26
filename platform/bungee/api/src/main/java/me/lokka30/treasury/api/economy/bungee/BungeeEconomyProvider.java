package me.lokka30.treasury.api.economy.bungee;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import me.lokka30.treasury.api.economy.EconomyProvider;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BungeeEconomyProvider {

    private static final class ProviderData implements Comparable<ProviderData> {

        private final String pluginName;
        private final Priority priority;

        public ProviderData(final String pluginName, final Priority priority) {
            this.pluginName = pluginName;
            this.priority = priority;
        }

        public String getPluginName() {
            return pluginName;
        }

        public Priority getPriority() {
            return priority;
        }

        @Override
        public int compareTo(@NotNull final BungeeEconomyProvider.ProviderData o) {
            return priority.compareTo(o.getPriority());
        }

    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH
    }

    private static Map<ProviderData, EconomyProvider> providers = new TreeMap<>(Comparator.reverseOrder());

    public static void registerProvider(
            @NotNull Plugin plugin, @NotNull Priority priority, @NotNull EconomyProvider provider
    ) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(priority, "priority");
        Objects.requireNonNull(provider, "provider");

        if (contains(plugin)) {
            unregisterProvider(plugin, priority);
            providers.put(new ProviderData(plugin.getDescription().getName(), priority), provider);
        } else {
            providers.put(new ProviderData(plugin.getDescription().getName(), priority), provider);
        }
    }

    public static void unregisterProvider(@NotNull Plugin plugin, @Nullable Priority priority) {
        Objects.requireNonNull(plugin, "plugin");
        providers.entrySet().stream().filter(e -> e
                .getKey()
                .getPluginName()
                .equalsIgnoreCase(plugin.getDescription().getName())).forEach(provider -> {
            ProviderData data = provider.getKey();
            if (priority != null && data.getPriority() == priority) {
                providers.remove(data);
            } else if (priority == null) {
                providers.remove(data);
            }
        });
    }

    public static Optional<EconomyProvider> obtainProvider() {
        return providers.isEmpty()
                ? Optional.empty()
                : providers.entrySet().stream().findFirst().map(Map.Entry::getValue);
    }

    public static Optional<EconomyProvider> obtainProvider(@NotNull Priority priority) {
        Objects.requireNonNull(priority, "priority");
        return providers.isEmpty()
                ? Optional.empty()
                : providers.entrySet().stream().filter(entry -> entry
                        .getKey()
                        .getPriority() == priority).findFirst().map(Map.Entry::getValue);
    }

    private static boolean contains(Plugin plugin) {
        return providers.entrySet().stream().anyMatch(e -> e
                .getKey()
                .getPluginName()
                .equalsIgnoreCase(plugin.getDescription().getName()));
    }

}
