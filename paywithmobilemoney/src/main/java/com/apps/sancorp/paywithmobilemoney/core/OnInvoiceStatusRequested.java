package com.apps.sancorp.paywithmobilemoney.core;

/**
 * Created by norrisboateng on 6/15/17.
 */

public interface OnInvoiceStatusRequested {
    void OnTransactionStarted();
    void OnTransactionSuccessful();
    void OnTransactionFailed(String errorCode);
}
