package com.apps.sancorp.paywithmobilemoney.offline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apps.sancorp.paywithmobilemoney.model.Invoice;

import java.util.ArrayList;

/**
 * Created by norrisboateng on 1/3/17.
 */

public class DatabaseOperations extends SQLiteOpenHelper {

    private static final int database_version = 1;

    public DatabaseOperations(Context context) {
        super(context, DatabaseInfo.DatabaseVariables.DATABASE_NAME, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        
        String CREATE_INVOICE_TABLE = "CREATE TABLE " + DatabaseInfo.DatabaseVariables.TABLE_INVOICE + "(" + DatabaseInfo.DatabaseVariables.INVOICE_NUMBER + " TEXT PRIMARY KEY," +
                DatabaseInfo.DatabaseVariables.TRANSACTION_NAME + " TEXT,"+DatabaseInfo.DatabaseVariables.IS_VALID + " INTEGER," + DatabaseInfo.DatabaseVariables.STATUS + " INTEGER);";
        sqLiteDatabase.execSQL(CREATE_INVOICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void setInvoice(DatabaseOperations dbo, Invoice invoice){
        SQLiteDatabase SQ = dbo.getWritableDatabase();
        ContentValues cv = getContentValuesForInvoiceTable(invoice);

        SQ.insert(DatabaseInfo.DatabaseVariables.TABLE_INVOICE, null, cv);
    }

    private ContentValues getContentValuesForInvoiceTable(Invoice invoice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.DatabaseVariables.INVOICE_NUMBER, invoice.getInvoiceNumber());
        values.put(DatabaseInfo.DatabaseVariables.TRANSACTION_NAME, invoice.getName());
        values.put(DatabaseInfo.DatabaseVariables.STATUS, invoice.isStatusChecked());
        values.put(DatabaseInfo.DatabaseVariables.IS_VALID, invoice.isValid());
        return values;
    }

    public void setIsValid(DatabaseOperations dbo,String invoiceNumber,int is_valid){
        SQLiteDatabase sqLiteDatabase = dbo.getWritableDatabase();

        String condition = DatabaseInfo.DatabaseVariables.INVOICE_NUMBER + " LIKE ?";
        String[] conditionArgs={invoiceNumber};
        Cursor cursor = sqLiteDatabase.query(DatabaseInfo.DatabaseVariables.TABLE_INVOICE, null, condition, conditionArgs, null,null,null);

        if (cursor.getCount() > 0){
            cursor.close();

            ContentValues values = new ContentValues();
            values.put(DatabaseInfo.DatabaseVariables.IS_VALID, is_valid);

            sqLiteDatabase.update(DatabaseInfo.DatabaseVariables.TABLE_INVOICE,values,condition,conditionArgs);
        }
        cursor.close();
    }

    public void setIsStatusChecked(DatabaseOperations dbo,String invoiceNumber){
        SQLiteDatabase sqLiteDatabase = dbo.getWritableDatabase();

        String condition = DatabaseInfo.DatabaseVariables.INVOICE_NUMBER + " LIKE ?";
        String[] conditionArgs={invoiceNumber};
        Cursor cursor = sqLiteDatabase.query(DatabaseInfo.DatabaseVariables.TABLE_INVOICE, null, condition, conditionArgs, null,null,null);

        if (cursor.getCount() > 0){
            cursor.close();

            ContentValues values = new ContentValues();
            values.put(DatabaseInfo.DatabaseVariables.STATUS, 1);

            sqLiteDatabase.update(DatabaseInfo.DatabaseVariables.TABLE_INVOICE,values,condition,conditionArgs);
        }
        cursor.close();
    }

    public ArrayList<Invoice> getInvoices(DatabaseOperations dbo){
        SQLiteDatabase sqLiteDatabase = dbo.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DatabaseInfo.DatabaseVariables.TABLE_INVOICE, null, null, null, null, null,null, null);
        ArrayList<Invoice> invoices = new ArrayList<>(cursor.getCount());

        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                invoices.add(getInvoiceFromCursor(cursor));
            }while (cursor.moveToNext());
        }
        cursor.close();

        return invoices;
    }

    private static Invoice getInvoiceFromCursor(Cursor cursor) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(DatabaseInfo.DatabaseVariables.INVOICE_NUMBER)));
        invoice.setName(cursor.getString(cursor.getColumnIndex(DatabaseInfo.DatabaseVariables.TRANSACTION_NAME)));
        invoice.isStatusChecked(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.DatabaseVariables.STATUS)) > 0);
        invoice.setValid(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.DatabaseVariables.IS_VALID)) > 0);

        return invoice;
    }

    public void deleteInvoice(DatabaseOperations dbo,String id){
        SQLiteDatabase sqLiteDatabase = dbo.getWritableDatabase();

        String condition = DatabaseInfo.DatabaseVariables.INVOICE_NUMBER+" LIKE ?";
        String conditionArgs[] = {id};
        sqLiteDatabase.delete(DatabaseInfo.DatabaseVariables.TABLE_INVOICE,condition,conditionArgs);
    }

    public void clearDatabase(DatabaseOperations dbo){
        SQLiteDatabase SQ = dbo.getWritableDatabase();
        SQ.delete(DatabaseInfo.DatabaseVariables.TABLE_INVOICE,null,null);
    }
}
