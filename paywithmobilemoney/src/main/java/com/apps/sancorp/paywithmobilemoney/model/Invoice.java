package com.apps.sancorp.paywithmobilemoney.model;

/**
 * This class a module that helps handles saving and retrieval of invoice numbers
 * offline(i.e without internet).
 * @author Sancorp Ltd.
 */

public class Invoice {

    private String invoiceNumber,name;
    private boolean isValid;
    private boolean isStatusChecked;

    /**
     * This is an initialization method for the invoice class
     * @see String#toLowerCase()
     * @see Boolean
     * @param name The name of the transaction. It should be something to help you easily identify a transaction
     * @param invoiceNumber The invoice number of the transaction
     */
    public Invoice(String name, String invoiceNumber) {
        this.name = name;
        this.invoiceNumber = invoiceNumber;
        this.isValid = false;
        this.isStatusChecked = false;
    }

    public Invoice() {
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isStatusChecked() {
        return isStatusChecked;
    }

    public void isStatusChecked(boolean status) {
        this.isStatusChecked = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
