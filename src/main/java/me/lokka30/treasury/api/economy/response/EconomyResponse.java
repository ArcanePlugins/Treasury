/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lokka30.treasury.api.economy.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO Javadoc
@SuppressWarnings("unused")
public class EconomyResponse<T> {

    // TODO Javadoc
    public enum Result {

        /**
         * @since v1.0.0
         * Use this constant if the method ran without any issues.
         */
        SUCCESS,

        /**
         * @since v1.0.0
         * Use this constant if the method can't be ran in any capacity
         * as the economy provider does not provide support for the method.
         * It is paramount that plugins ensure that economy providers support
         * certain methods (e.g. bank accounts) before attempting to access them.
         */
        FEATURE_NOT_SUPPORTED,

        /* Accounts */

        // TODO Javadoc
        ACCOUNT_NOT_FOUND,

        // TODO Javadoc
        ACCOUNT_DELETION_NOT_SUPPORTED,

        /* BankAccounts */

        // TODO Javadoc
        ALREADY_MEMBER_OF_BANK_ACCOUNT,

        // TODO Javadoc
        ALREADY_OWNER_OF_BANK_ACCOUNT,

        // TODO Javadoc
        ALREADY_NOT_MEMBER_OF_BANK_ACCOUNT,

        // TODO Javadoc
        ALREADY_NOT_OWNER_OF_BANK_ACCOUNT,

        /* Balances */

        // TODO Javadoc
        NEGATIVE_BALANCES_NOT_SUPPORTED,

        // TODO Javadoc
        NEGATIVE_AMOUNT_SPECIFIED,

        /* Currencies */

        // TODO Javadoc
        CURRENCY_NOT_FOUND,

        /**
         * @since v1.0.0
         * Use this constant if the method resulted in a complete failure,
         * AND no other constant in this enum is applicable to the issue
         * that occured. In this case, use this constant, and please
         * submit a pull request or issue so that a future Treasury version
         * can accomodate for this issue.
         */
        OTHER_FAILURE

    }

    @Nullable private final T value;
    @NotNull private final Result result;
    @Nullable private final String errorMessage;

    public EconomyResponse(@Nullable final T value, @NotNull final Result result, @Nullable final String errorMessage) {
        this.value = value;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    // TODO Javadoc
    @Nullable
    public final T getValue() { return value; }

    // TODO Javadoc
    @NotNull
    public final Result getResult() { return result; }

    // TODO Javadoc
    @Nullable
    public final String getErrorMessage() { return errorMessage; }

}
