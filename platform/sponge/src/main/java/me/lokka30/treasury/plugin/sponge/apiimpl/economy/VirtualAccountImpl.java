/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.sponge.apiimpl.economy;

import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

public class VirtualAccountImpl extends AbstractAccountImpl implements VirtualAccount {

    public VirtualAccountImpl(
            final EconomyProvider delegateProvider,
            final MappedCurrenciesCache mappedCurrenciesCache,
            final NonPlayerAccount delegateAccount
    ) {
        super(delegateProvider, mappedCurrenciesCache, delegateAccount);
    }

    @Override
    public String identifier() {
        return ((NonPlayerAccount) this.delegateAccount).getIdentifier().toString();
    }

}
