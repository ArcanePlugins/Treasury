package me.lokka30.treasury.plugin.bukkit.fork;

/**
 * Represents a handler for determining on what fork we're running. This is in order
 * to optimise some stuff which can be optimised.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface BukkitFork {

    static BukkitFork get() {
        return new BukkitFork() {

            private boolean paper = false;
            private boolean ranPaperCheck = false;
            private boolean spigot = false;
            private boolean ranSpigotCheck = false;

            @Override
            public boolean isPaper() {
                if (!ranPaperCheck) {
                    try {
                        Class.forName("com.destroystokyo.paper.PaperConfig");
                        paper = true;
                    } catch (ClassNotFoundException e) {
                        paper = false;
                    }
                    ranPaperCheck = true;
                }
                return paper;
            }

            @Override
            public boolean isSpigot() {
                if (!ranSpigotCheck) {
                    try {
                        Class.forName("net.md_5.bungee.api.chat.ChatColor");
                        spigot = true;
                    } catch (ClassNotFoundException e) {
                        spigot = false;
                    }
                    ranSpigotCheck = true;
                }
                return spigot;
            }
        };
    }

    /**
     * Returns whether we're running paper.
     *
     * @return paper?
     */
    boolean isPaper();

    // it is ridiculous if someone's using bukkit nowadays, but let's be on the safe side.
    /**
     * Returns whether we're running spigot.
     *
     * @return spigot?
     */
    boolean isSpigot();
}
