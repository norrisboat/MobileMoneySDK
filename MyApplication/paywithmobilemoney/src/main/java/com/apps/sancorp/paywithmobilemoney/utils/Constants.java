package com.apps.sancorp.paywithmobilemoney.utils;

public class Constants {

    //mobile money variables
    public static final String TRANSACTION_COMPLETED="0000";
    static final String INVOICE_NUMBER="ax21:invoiceNo";
    static final String RESPONSE_CODE="ax21:responseCode";
    public static final String LOG_TAG="MobileMoney";

    //error codes
    public static final String BILL_NOT_PAID="21VD";
    public static final String NO_INTERNET="NIC";
    public static final String NO_INVOICE_NUMBER ="NIN";
    public static final String DIALOG_CLOSED ="DC";

    //payment progression
    public static final int PHONE_NUMBER=1;
    public static final int WAITING_FOR_BILL=2;
    public static final int CONFIRM=3;
    public static final int CONFIRM_RESPONSE=4;

}
