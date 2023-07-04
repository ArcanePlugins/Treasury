/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.util.HashMap;
import java.util.Map;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.hooks.miniplaceholders.MiniPlaceholdersHook;
import me.lokka30.treasury.plugin.bukkit.hooks.papi.TreasuryPapiHook;
import me.lokka30.treasury.plugin.core.hooks.Hook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class HookRegistrar implements Listener {

    private final Map<String, Hook> hooks = new HashMap<>();
    private final TreasuryBukkit treasuryBukkit;

    public HookRegistrar(TreasuryBukkit plugin) {
        this.treasuryBukkit = plugin;
        registerHook(new TreasuryPapiHook(plugin));
        registerHook(new MiniPlaceholdersHook());
    }

    void registerHook(Hook hook) {
        hooks.put(hook.getPlugin(), hook);
    }

    public void shutdownHooks() {
        for (Hook hook : this.hooks.values()) {
            hook.shutdown();
        }
    }

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        Plugin plugin = event.getPlugin();
        if (!hooks.containsKey(plugin.getName())) {
            return;
        }
        Hook hook = this.hooks.get(plugin.getName());
        if (hook.register()) {
            treasuryBukkit.getLogger().info(plugin.getName() + " hook registered successfully");
        }
    }

}
