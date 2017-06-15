package com.apps.sancorp.paywithmobilemoney.offline;

import android.provider.BaseColumns;

/**
 * Created by norrisboateng on 1/3/17.
 */

class DatabaseInfo {

    DatabaseInfo(){}

    static abstract class DatabaseVariables implements BaseColumns {
        static final String DATABASE_NAME = "offline_storage";

        static final String TABLE_INVOICE = "invoice_table";
        static final String INVOICE_NUMBER = "inovice_number";
        static final String TRANSACTION_NAME = "name";
        static final String IS_VALID = "is_valid";
        static final String STATUS = "status";
    }
}
