package me.lokka30.treasury.api.permission;

import java.util.Collection;
import java.util.UUID;
import me.lokka30.treasury.api.common.misc.TriState;
import org.jetbrains.annotations.NotNull;

public interface NodeHolder {

    @NotNull UUID getUniqueId();

    @NotNull Collection<Node<?>> nodes();

    @NotNull TriState hasNode(@NotNull Node<?> node);

}
