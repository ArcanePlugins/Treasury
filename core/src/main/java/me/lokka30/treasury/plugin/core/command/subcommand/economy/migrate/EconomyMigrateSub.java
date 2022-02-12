/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import me.lokka30.treasury.api.service.Service;
import me.lokka30.treasury.api.service.ServicePriority;
import me.lokka30.treasury.api.service.ServiceProvider;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.response.EconomyException;
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

        Set<Service<EconomyProvider>> serviceProviders = ServiceProvider.INSTANCE.allServicesFor(
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
        ServiceProvider.INSTANCE.registerService(EconomyProvider.class,
                dummyEconomy,
                "Treasury",
                ServicePriority.HIGH
        );

        // Re-register economies to ensure target economy will override migrated economy.
        ServiceProvider.INSTANCE.unregister(EconomyProvider.class, from.get());
        ServiceProvider.INSTANCE.registerService(EconomyProvider.class,
                from.get(),
                from.registrarName(),
                ServicePriority.LOW
        );
        ServiceProvider.INSTANCE.unregister(EconomyProvider.class, to.get());
        ServiceProvider.INSTANCE.registerService(EconomyProvider.class,
                to.get(),
                to.registrarName(),
                ServicePriority.HIGH
        );

        MigrationData migration = new MigrationData(from, to, debugEnabled);

        TreasuryPlugin.getInstance().scheduler().runAsync(() -> {

            // Initialize account migration.
            Phaser playerMigration = migrateAccounts(sender.getAsTransactionInitiator(),
                    migration,
                    new PlayerAccountMigrator()
            );
            Phaser nonPlayerMigration = migrateAccounts(sender.getAsTransactionInitiator(),
                    migration,
                    new NonPlayerAccountMigrator()
            );
            nonPlayerMigration.arriveAndAwaitAdvance();

            // Block until migration is complete.
            playerMigration.arriveAndAwaitAdvance();

            // Unregister economy override.
            ServiceProvider.INSTANCE.unregister(EconomyProvider.class, dummyEconomy);

            sendMigrationMessage(sender, migration);
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
                    .pluginsListRegisteringProvider()
                    .stream()
                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(lastArg))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void sendMigrationMessage(
            @NotNull CommandSource sender, @NotNull MigrationData migration
    ) {
        sender.sendMessage(Message.of(MessageKey.MIGRATE_FINISHED_MIGRATION,
                placeholder("time", migration.timer().getTimer()),
                placeholder("player-accounts", migration.playerAccountsProcessed().toString()),
                placeholder("nonplayer-accounts",
                        migration.nonPlayerAccountsProcessed().toString()
                ),
                placeholder("non-migrated-currencies",
                        Utils.formatListMessage(migration.nonMigratedCurrencies())
                )
        ));
    }

    private <T extends Account> Phaser migrateAccounts(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull MigrationData migration,
            @NotNull AccountMigrator<T> migrator
    ) {
        // Initialize phaser with a single party: migration completion.
        Phaser phaser = new Phaser(1);

        migrator.requestAccountIds().accept(migration.from().get(),
                new PhasedSubscriber<Collection<String>>(phaser) {
                    @Override
                    public void phaseAccept(@NotNull Collection<String> identifiers) {
                        for (String identifier : identifiers) {
                            migrateAccount(initiator, phaser, identifier, migration, migrator);
                        }
                    }

                    @Override
                    public void phaseFail(@NotNull EconomyException exception) {
                        migration.debug(() -> migrator.getBulkFailLog(exception));
                    }
                }
        );

        return phaser;
    }

    private <T extends Account> void migrateAccount(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull Phaser phaser,
            @NotNull String identifier,
            @NotNull MigrationData migration,
            @NotNull AccountMigrator<T> migrator
    ) {
        migration.debug(() -> migrator.getInitLog(identifier));

        // Set up logging for failure.
        // Because from and to accounts are requested in parallel, guard against duplicate failure logging.
        AtomicBoolean failed = new AtomicBoolean();
        BiConsumer<T, Throwable> failureConsumer = (account, throwable) -> {
            if (throwable != null && failed.compareAndSet(false, true)) {
                migration.debug(() -> migrator.getErrorLog(identifier, throwable));
            }
        };

        CompletableFuture<T> fromAccountFuture = new CompletableFuture<>();
        migrator.requestAccount().accept(migration.from().get(),
                identifier,
                new PhasedFutureSubscriber<>(phaser, fromAccountFuture)
        );
        fromAccountFuture.whenComplete(failureConsumer);

        CompletableFuture<T> toAccountFuture = new CompletableFuture<>();
        migrator.checkAccountExistence().accept(migration.to().get(),
                identifier,
                new PhasedSubscriber<Boolean>(phaser) {
                    @Override
                    public void phaseAccept(@NotNull Boolean hasAccount) {
                        PhasedFutureSubscriber<T> subscription = new PhasedFutureSubscriber<>(phaser,
                                toAccountFuture
                        );
                        if (hasAccount) {
                            migrator.requestAccount().accept(migration.to().get(),
                                    identifier,
                                    subscription
                            );
                        } else {
                            migrator.createAccount().accept(migration.to().get(),
                                    identifier,
                                    subscription
                            );
                        }
                    }

                    @Override
                    public void phaseFail(@NotNull EconomyException exception) {
                        toAccountFuture.completeExceptionally(exception);
                    }
                }
        );
        toAccountFuture.whenComplete(failureConsumer);

        fromAccountFuture.thenAcceptBoth(toAccountFuture, (fromAccount, toAccount) -> {
            migrator.migrate(initiator, phaser, fromAccount, toAccount, migration);
            migrator.getSuccessfulMigrations(migration).incrementAndGet();
        });
    }

}
