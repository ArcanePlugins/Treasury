/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import com.mrivanplays.process.Process;
import com.mrivanplays.process.ProcessException;
import com.mrivanplays.process.ProcessesCompletion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.common.service.Service;
import me.lokka30.treasury.api.common.service.ServicePriority;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import me.lokka30.treasury.plugin.core.command.CommandSource;
import me.lokka30.treasury.plugin.core.command.Subcommand;
import me.lokka30.treasury.plugin.core.config.messaging.Message;
import me.lokka30.treasury.plugin.core.config.messaging.MessageKey;
import me.lokka30.treasury.plugin.core.debug.DebugCategory;
import me.lokka30.treasury.plugin.core.debug.DebugHandler;
import me.lokka30.treasury.plugin.core.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.lokka30.treasury.plugin.core.config.messaging.MessagePlaceholder.placeholder;

public class EconomyMigrateSub implements Subcommand {

    /*
    inf: Migrates accounts from one economy plugin to another
    cmd: /treasury economy migrate <providerFrom> <providerTo>
     */

    @Override
    public void execute(
            @NotNull CommandSource sender, @NotNull String label, @NotNull String[] args
    ) {
        final boolean debugEnabled = DebugHandler.isCategoryEnabled(DebugCategory.MIGRATE_SUBCOMMAND);

        if (!Utils.checkPermissionForCommand(sender, "treasury.command.treasury.economy.migrate")) {
            return;
        }

        Set<Service<EconomyProvider>> serviceProviders = ServiceRegistry.INSTANCE.allServicesFor(
                EconomyProvider.class);

        if (args.length != 2) {
            sender.sendMessage(Message.of(MessageKey.MIGRATE_INVALID_USAGE,
                    placeholder("label", label),
                    placeholder("providers",
                            serviceProviders.isEmpty()
                                    ? "No providers found "
                                    : Utils.formatListMessage(serviceProviders
                                            .stream()
                                            .map(Service::registrarName)
                                            .collect(Collectors.toList()))
                    )
            ));
            return;
        }

        Service<EconomyProvider> from = null;
        Service<EconomyProvider> to = null;

        if (serviceProviders.size() < 2) {
            sender.sendMessage(Message.of(MessageKey.MIGRATE_REQUIRES_TWO_PROVIDERS));
            return;
        }

        final Set<String> serviceProvidersNames = new HashSet<>();

        for (Service<EconomyProvider> serviceProvider : serviceProviders) {
            serviceProvidersNames.add(serviceProvider.registrarName());
            if (debugEnabled) {
                DebugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND,
                        "Found service provider: " + serviceProvider.registrarName()
                );
            }
        }

        if (args[0].equalsIgnoreCase(args[1])) {
            sender.sendMessage(Message.of(MessageKey.MIGRATE_PROVIDERS_MATCH,
                    placeholder("providers", Utils.formatListMessage(serviceProvidersNames))
            ));
            return;
        }

        for (Service<EconomyProvider> serviceProvider : serviceProviders) {
            final String serviceProviderPluginName = serviceProvider.registrarName();

            if (args[0].equalsIgnoreCase(serviceProviderPluginName)) {
                from = serviceProvider;
            } else if (args[1].equalsIgnoreCase(serviceProviderPluginName)) {
                to = serviceProvider;
            }
        }

        if (from == null) {
            sender.sendMessage(Message.of(MessageKey.MIGRATE_REQUIRES_VALID_FROM,
                    placeholder("providers", Utils.formatListMessage(serviceProvidersNames))
            ));
            return;
        }

        if (to == null) {
            sender.sendMessage(Message.of(MessageKey.MIGRATE_REQUIRES_VALID_TO,
                    placeholder("providers", Utils.formatListMessage(serviceProvidersNames))
            ));
            return;
        }

        if (debugEnabled) {
            DebugHandler.log(DebugCategory.MIGRATE_SUBCOMMAND,
                    "Migrating from '&b" + from.registrarName() + "&7' to '&b" + to.registrarName() + "&7'."
            );
        }

        sender.sendMessage(Message.of(MessageKey.MIGRATE_STARTING_MIGRATION));

        // Override economies with dummy economy that doesn't support any operations.
        MigrationEconomy dummyEconomy = new MigrationEconomy();
        ServiceRegistry.INSTANCE.registerService(EconomyProvider.class,
                dummyEconomy,
                "Treasury",
                ServicePriority.HIGH
        );

        // Re-register economies to ensure target economy will override migrated economy.
        ServiceRegistry.INSTANCE.unregister(EconomyProvider.class, from.get());
        ServiceRegistry.INSTANCE.registerService(EconomyProvider.class,
                from.get(),
                from.registrarName(),
                ServicePriority.LOW
        );
        ServiceRegistry.INSTANCE.unregister(EconomyProvider.class, to.get());
        ServiceRegistry.INSTANCE.registerService(EconomyProvider.class,
                to.get(),
                to.registrarName(),
                ServicePriority.HIGH
        );

        MigrationData migration = new MigrationData(from.get(), to.get(), debugEnabled);

        EconomyTransactionInitiator<?> initiator = sender.getAsTransactionInitiator();
        TreasuryPlugin.getInstance().scheduler().runAsync(() -> {
            // Run the currencies migrator first before doing anything else.
            try {
                CurrenciesMigrator currenciesMigrator = new CurrenciesMigrator(migration);
                currenciesMigrator.run();

                // whenDone consumer
                Consumer<Set<ProcessException>> callback = (errors) -> {
                    if (!errors.isEmpty()) {
                        sender.sendMessage(Message.of(MessageKey.MIGRATE_INTERNAL_ERROR));
                        TreasuryPlugin.getInstance().logger().error("Errors whilst migrating:");
                        for (ProcessException e : errors) {
                            e.printStackTrace();
                        }
                    }
                    // Unregister economy override.
                    ServiceRegistry.INSTANCE.unregister(EconomyProvider.class, dummyEconomy);

                    // Send migration message
                    List<String> nonMigratedCurrencies = new ArrayList<>();
                    for (Map.Entry<String, Collection<String>> entry : migration
                            .nonMigratedCurrencies()
                            .asMap()
                            .entrySet()) {
                        nonMigratedCurrencies.add(entry.getKey() + " - " + Utils.formatListMessage(
                                entry.getValue()));
                    }
                    sender.sendMessage(Message.of(MessageKey.MIGRATE_FINISHED_MIGRATION,
                            placeholder("time", migration.timer().getTimer()),
                            placeholder("player-accounts",
                                    migration.playerAccountsProcessed().toString()
                            ),
                            placeholder("nonplayer-accounts",
                                    migration.nonPlayerAccountsProcessed().toString()
                            ),
                            placeholder("migrated-currencies",
                                    Utils.formatListMessage(migration.migratedCurrencies())
                            ),
                            placeholder("non-migrated-currencies",
                                    nonMigratedCurrencies.isEmpty()
                                            ? ""
                                            : Utils.formatListMessage(nonMigratedCurrencies)
                            )
                    ));
                };

                ProcessesCompletion playerCompletion = this.migratePlayerAccounts(initiator,
                        migration
                );

                if (playerCompletion == null) {
                    // Weird. No player accounts to migrate.
                    // let's try to migrate just non player accounts
                    ProcessesCompletion nonPlayerCompletion = this.migrateNonPlayerAccounts(initiator,
                            migration
                    );
                    if (nonPlayerCompletion == null) {
                        // Also weird. just call done with no errors
                        callback.accept(Collections.emptySet());
                    } else {
                        nonPlayerCompletion.whenDone(callback);
                    }
                    return;
                }

                ProcessesCompletion nonPlayerCompletion = this.migrateNonPlayerAccounts(initiator,
                        migration
                );

                if (nonPlayerCompletion == null) {
                    // No non player accounts to migrate.
                    playerCompletion.whenDone(callback);
                    return;
                }

                ProcessesCompletion.whenAllDone(false,
                        callback,
                        playerCompletion,
                        nonPlayerCompletion
                );
            } catch (Throwable e) {
                sender.sendMessage(Message.of(MessageKey.MIGRATE_INTERNAL_ERROR));
                e.printStackTrace();
            }
        });
    }

    @Override
    @Nullable
    public List<String> complete(
            @NotNull CommandSource source, @NotNull String label, @NotNull String[] args
    ) {
        if (args.length == 0) {
            return Collections.emptyList();
        }
        if ((args.length == 1 || args.length == 2) && source.hasPermission(
                "treasury.command.treasury.migrate")) {
            String lastArg = args[args.length - 1].toLowerCase(Locale.ROOT);
            return TreasuryPlugin
                    .getInstance()
                    .pluginsListRegisteringEconomyProvider()
                    .stream()
                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(lastArg))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private ProcessesCompletion migratePlayerAccounts(
            @NotNull EconomyTransactionInitiator<?> initiator, @NotNull MigrationData migration
    ) {
        try {
            Response<Collection<UUID>> accountIdResp = migration
                    .from()
                    .retrievePlayerAccountIds()
                    .get();
            if (!accountIdResp.isSuccessful()) {
                throw new RuntimeException("Unable to fetch player account UUIDs for migration: " + accountIdResp
                        .getFailureReason()
                        .getDescription());
            }
            Collection<UUID> accountIds = accountIdResp.getResult();
            if (accountIds.isEmpty()) {
                return null;
            }
            List<Process> processes = new ArrayList<>(accountIds.size());
            for (UUID uuid : accountIds) {
                processes.add(new PlayerAccountMigrationProcess(initiator,
                        uuid.toString(),
                        migration
                ));
            }

            return TreasuryPlugin.getInstance().processScheduler().runProcesses(processes.toArray(
                    new Process[0]));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error during migration of player accounts", e);
        }
    }

    private ProcessesCompletion migrateNonPlayerAccounts(
            @NotNull EconomyTransactionInitiator<?> initiator, @NotNull MigrationData migration
    ) {
        try {
            Response<Collection<String>> accountIdResp = migration
                    .from()
                    .retrieveNonPlayerAccountIds()
                    .get();
            if (!accountIdResp.isSuccessful()) {
                throw new RuntimeException("Unable to fetch non player account ids for migration: " + accountIdResp
                        .getFailureReason()
                        .getDescription());
            }
            Collection<String> accountIds = accountIdResp.getResult();
            if (accountIds.isEmpty()) {
                return null;
            }
            List<Process> processes = new ArrayList<>(accountIds.size());
            for (String id : accountIds) {
                processes.add(new NonPlayerAccountMigrationProcess(initiator, id, migration));
            }

            return TreasuryPlugin.getInstance().processScheduler().runProcesses(processes.toArray(
                    new Process[0]));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error during migration of non player accounts", e);
        }
    }

}
