package me.lokka30.treasury.api.common.response;

public class TreasuryException extends Exception{
    public TreasuryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
