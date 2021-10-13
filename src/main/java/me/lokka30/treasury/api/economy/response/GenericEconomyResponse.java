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

import me.lokka30.treasury.api.economy.EconomyProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericEconomyResponse {

    public enum Type {

        /**
         * Method ran with no issues.
         */
        SUCCESS,

        /**
         * Method resulted with somewhat success,
         * although some issues occured.
         * An error message should be provided.
         */
        WARNING,

        /**
         * Method resulted in a complete failure.
         * An error message should be provided.
         */
        FAILURE,

        /**
         * Method resulted in a complete failure
         * as the method is not supported by the
         * economy provider. This should have
         * already been indicated by the various
         * 'isSupported' methods, such as
         * {@link EconomyProvider#hasBankAccountSupport()}.
         */
        UNSUPPORTED
    }

    @NotNull private final Type type;
    @Nullable private final String errorMessage;

    public GenericEconomyResponse(@NotNull final Type type, @Nullable final String errorMessage) {
        this.type = type;
        this.errorMessage = errorMessage;
    }

    @NotNull
    public final Type getType() { return type; }

    @Nullable
    public final String getErrorMessage() { return errorMessage; }

}
