package me.lokka30.treasury.plugin.core.command;

import java.util.Collections;
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
     * @since v1.0.0
     */
    void execute(@NotNull CommandSource source, @NotNull String label, @NotNull String[] args);

    /**
     * @param source who asked for tab completion
     * @param label subcommand label
     * @param args subcommand args
     * @return list of completions, can be null
     * @since v1.0.0
     */
    @Nullable
    default List<String> complete(@NotNull CommandSource source, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
