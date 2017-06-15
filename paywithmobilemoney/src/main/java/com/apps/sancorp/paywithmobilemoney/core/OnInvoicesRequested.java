package com.apps.sancorp.paywithmobilemoney.core;

import com.apps.sancorp.paywithmobilemoney.model.Invoice;

import java.util.ArrayList;

/**
 * Invoice transaction listener class
 */

public interface OnInvoicesRequested {
    void OnRequestStarted();
    void OnTransactionSuccessful(ArrayList<Invoice> invoices);
    void OnNoInvoicesFound();
}
