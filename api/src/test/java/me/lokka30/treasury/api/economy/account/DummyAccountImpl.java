package me.lokka30.treasury.api.economy.account;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.common.response.Response;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DummyAccountImpl implements Account {

    private final String id;
    private final Collection<UUID> memberIds;
    private final Map<UUID, Set<Map.Entry<AccountPermission, TriState>>> permissionsMap;

    public DummyAccountImpl(String id, Collection<UUID> memberIds) {
        this.id = id;
        this.memberIds = memberIds;
        this.permissionsMap = new HashMap<>();
        // set some random values
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (UUID member : memberIds) {
            Set<Map.Entry<AccountPermission, TriState>> perms = new HashSet<>();
            for (int i = 0; i < 2; i++) {
                AccountPermission permission = AccountPermission.values()[random.nextInt(0, 3)];
                TriState value = TriState.values()[random.nextInt(0, 2)];
                perms.add(new AbstractMap.SimpleImmutableEntry<>(permission, value));
            }
            this.permissionsMap.put(member, perms);
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.id;
    }

    @Override
    public Optional<String> getName() {
        return Optional.empty();
    }

    @Override
    public CompletableFuture<Response<TriState>> setName(@Nullable final String name) {
        return null;
    }

    @Override
    public CompletableFuture<Response<BigDecimal>> retrieveBalance(@NotNull final Currency currency) {
        return null;
    }

    @Override
    public CompletableFuture<Response<BigDecimal>> doTransaction(@NotNull final EconomyTransaction economyTransaction) {
        return null;
    }

    @Override
    public CompletableFuture<Response<TriState>> deleteAccount() {
        return null;
    }

    @Override
    public CompletableFuture<Response<Collection<String>>> retrieveHeldCurrencies() {
        return null;
    }

    @Override
    public CompletableFuture<Response<Collection<EconomyTransaction>>> retrieveTransactionHistory(
            final int transactionCount, @NotNull final Temporal from, @NotNull final Temporal to
    ) {
        return null;
    }

    @Override
    public CompletableFuture<Response<Collection<UUID>>> retrieveMemberIds() {
        return CompletableFuture.completedFuture(Response.success(this.memberIds));
    }

    @Override
    public CompletableFuture<Response<TriState>> isMember(@NotNull final UUID player) {
        return null;
    }

    @Override
    public CompletableFuture<Response<TriState>> setPermission(
            @NotNull final UUID player,
            @NotNull final TriState permissionValue,
            final @NotNull AccountPermission @NotNull ... permissions
    ) {
        return null;
    }

    @Override
    public CompletableFuture<Response<Map<AccountPermission, TriState>>> retrievePermissions(@NotNull final UUID player) {
        Map<AccountPermission, TriState> map = new HashMap<>();
        Set<Map.Entry<AccountPermission, TriState>> perms = this.permissionsMap.get(player);
        for (Map.Entry<AccountPermission, TriState> perm : perms) {
            map.put(perm.getKey(), perm.getValue());
        }
        return CompletableFuture.completedFuture(Response.success(map));
    }

    @Override
    public CompletableFuture<Response<TriState>> hasPermission(
            @NotNull final UUID player, final @NotNull AccountPermission @NotNull ... permissions
    ) {
        return null;
    }

}
