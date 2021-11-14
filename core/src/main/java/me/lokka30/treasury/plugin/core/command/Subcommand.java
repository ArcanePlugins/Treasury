package me.lokka30.treasury.plugin.core.command;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a treasury subcommand.
 *
 * @author lokka30, MrIvanPlays
 * @since v1.0.0
 */
public interface Subcommand {

    /**
     * @param source who ran the command
     * @param label subcommand label
     * @param args subcommand arguments
     * @return whether successful
     * @since v1.0.0
     */
    boolean execute(@NotNull CommandSource source, @NotNull String label, @NotNull String[] args);

    @Nullable
    default List<String> complete(@NotNull CommandSource source, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
