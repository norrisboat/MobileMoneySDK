package com.apps.sancorp.paywithmobilemoney.core;

/**
 * Mobile transaction initiation listener class
 */

public interface OnTransactionInitiated{
    void OnTransactionStarted();
    void OnTransactionSuccessful(String invoiceNumber);
    void OnTransactionFailed(String errorCode);
    void onInvoiceMade(String invoiceNumber);
}
