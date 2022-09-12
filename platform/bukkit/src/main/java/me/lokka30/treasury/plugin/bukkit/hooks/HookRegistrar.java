/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.hooks;

import java.util.HashMap;
import java.util.Map;
import me.lokka30.treasury.plugin.bukkit.TreasuryBukkit;
import me.lokka30.treasury.plugin.bukkit.hooks.papi.TreasuryPapiHookRegistrar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class HookRegistrar implements Listener {

    private final Map<String, Hook> hooks = new HashMap<>();
    private final TreasuryBukkit treasuryBukkit;

    public HookRegistrar(TreasuryBukkit plugin) {
        this.treasuryBukkit = plugin;
        registerHook(new TreasuryPapiHookRegistrar());
    }

    void registerHook(Hook hook) {
        hooks.put(hook.getPlugin(), hook);
    }

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        Plugin plugin = event.getPlugin();
        if (!hooks.containsKey(plugin.getName())) {
            return;
        }
        for (Hook hook : hooks.values()) {
            if (hook.register(treasuryBukkit)) {
                treasuryBukkit
                        .getLogger()
                        .info(plugin.getName() + " hook registered successfully");
            }
        }
    }

}
