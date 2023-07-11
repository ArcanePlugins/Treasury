/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.krypton;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.kryptonmc.api.Server;
import org.kryptonmc.api.event.Listener;
import org.kryptonmc.api.event.server.ServerStartEvent;

public class TreasuryKrypton {

    private final Server server;
    private final Logger logger;

    @Inject
    public TreasuryKrypton(Server server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Listener
    public void onStart(ServerStartEvent event) {

    }

    public Server server() {
        return server;
    }

    public Logger logger() {
        return logger;
    }

}
