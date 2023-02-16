/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.command.subcommand.economy.migrate;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.FailureReason;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import org.jetbrains.annotations.NotNull;

class NonPlayerAccountMigrationProcess extends PlayerAccountMigrationProcess {

    public NonPlayerAccountMigrationProcess(
            @NotNull EconomyTransactionInitiator<?> initiator,
            @NotNull String accountId,
            @NotNull MigrationData migration
    ) {
        super(initiator, accountId, migration);
    }

    @Override
    protected void run() throws Throwable {
        super.run();

        // migrate permissions
        Response<Map<UUID, Map<AccountPermission, TriState>>> permissionsMapResponse = fromAccount
                .retrievePermissionsMap()
                .get();
        if (!handleUnsuccessfulResponse(permissionsMapResponse)) {
            return;
        }

        Map<UUID, Map<AccountPermission, TriState>> permissionsMap = permissionsMapResponse.getResult();

        for (Map.Entry<UUID, Map<AccountPermission, TriState>> entry : permissionsMap.entrySet()) {
            Response<TriState> setPermissionResponse = toAccount.setPermissions(entry.getKey(),
                    entry.getValue()).get();
            handleUnsuccessfulResponse(setPermissionResponse);
        }

        this.migration.nonPlayerAccountsProcessed().incrementAndGet();
    }

    @Override
    @NotNull String getInitLog() {
        return "Migrating non player account of ID '&b" + this.accountId + "&7'.";
    }

    @Override
    @NotNull String getErrorLog(@NotNull FailureReason failureReason) {
        return "Error migrating non player account ID '&b" + this.accountId + "&7': &b" + failureReason.getDescription();
    }

    @Override
    @NotNull CompletableFuture<Response<Account>> requestOrCreateAccount(
            @NotNull EconomyProvider provider, @NotNull String identifier
    ) {
        return provider.accountAccessor().nonPlayer().withIdentifier(NamespacedKey.fromString(
                identifier)).genericGet();
    }

    @Override
    boolean nonPlayer() {
        return true;
    }

}
