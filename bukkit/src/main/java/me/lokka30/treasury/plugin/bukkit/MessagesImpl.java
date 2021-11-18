package me.lokka30.treasury.plugin.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.function.Supplier;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.config.messaging.Messages;
import me.lokka30.treasury.plugin.core.config.messaging.MessagesConfigAccessor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class MessagesImpl extends Messages {

    private final Treasury plugin;

    public MessagesImpl(Treasury plugin) {
        super(supplyMessages(plugin).get());
        this.plugin = plugin;
    }

    private static Supplier<MessagesConfigAccessor> supplyMessages(Treasury plugin) {
        return () -> {
            File file = new File(plugin.getDataFolder(), "messages.yml");
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try (InputStream in = plugin.getClass().getClassLoader().getResourceAsStream("messages.yml")) {
                    Files.copy(in, file.getAbsoluteFile().toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return MessagesConfigAccessor.EMPTY;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return (MessagesConfigAccessor) config::get;
        };
    }

    @Override
    public void generateMissingEntries(@NotNull Collection<MessageKey> keys) {
        // todo: find another way!!!
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (file.exists()) {
            file.delete();
        }
        try (InputStream in = plugin.getClass().getClassLoader().getResourceAsStream("messages.yml")) {
            Files.copy(in, file.getAbsoluteFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
