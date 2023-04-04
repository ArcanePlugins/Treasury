/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import java.util.UUID;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import org.spongepowered.api.service.economy.account.UniqueAccount;

public class UniqueAccountImpl extends AbstractAccountImpl implements UniqueAccount {

    public UniqueAccountImpl(
            EconomyProvider delegateProvider,
            MappedCurrenciesCache currenciesCache,
            PlayerAccount delegatePlayerAccount
    ) {
        super(delegateProvider, currenciesCache, delegatePlayerAccount);
    }

    @Override
    public UUID uniqueId() {
        return ((PlayerAccount) this.delegateAccount).getUniqueId();
    }

    @Override
    public String identifier() {
        return this.uniqueId().toString();
    }

}
