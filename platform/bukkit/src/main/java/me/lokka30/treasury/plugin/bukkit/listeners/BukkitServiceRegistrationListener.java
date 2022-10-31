package me.lokka30.treasury.plugin.bukkit.listeners;

import java.util.logging.Logger;
import me.lokka30.treasury.api.economy.EconomyProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.PluginDescriptionFile;

public class BukkitServiceRegistrationListener implements Listener {

    private final Logger logger;

    public BukkitServiceRegistrationListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServiceRegister(ServiceRegisterEvent event) {
        if (!event.getProvider().getService().isAssignableFrom(EconomyProvider.class)) {
            return;
        }

        PluginDescriptionFile desc = event.getProvider().getPlugin().getDescription();

        logger.severe(
                "Nag author(s) "
                        + desc.getAuthors()
                        + " of plugin "
                        + desc.getName()
                        + " for registering EconomyProvider in the Bukkit service manager!"
        );
        logger.severe(
                "Since 2.0.0 ALL plugins should've migrated to registering providers into the "
                        + "Treasury Service API"
        );
        logger.severe("Plugins utilising the Treasury API MIGHT NOT WORK PROPERLY!");
        logger.severe("FIX THIS ASAP!!!!");
    }

}
