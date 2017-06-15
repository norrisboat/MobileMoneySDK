package com.apps.sancorp.paywithmobilemoney.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.apps.sancorp.paywithmobilemoney.model.Invoice;
import com.apps.sancorp.paywithmobilemoney.offline.DatabaseOperations;
import com.apps.sancorp.paywithmobilemoney.offline.PrefsManager;
import com.apps.sancorp.paywithmobilemoney.ui.MMDialog;
import com.apps.sancorp.paywithmobilemoney.utils.ConnectivityUtils;
import com.apps.sancorp.paywithmobilemoney.utils.Constants;
import com.apps.sancorp.paywithmobilemoney.utils.XMLParser;

import java.util.ArrayList;

/**
 * The Mobile Money library simplifies basic mobile money operations
 * for android mobile developers.
 * @author  Norris Aboagye Boateng.
 * @version 1.0
 * @since   2017-06-14
 */

public class MobileMoney {
    private Context context;
    private OnTransactionInitiated onTransactionInitiated;
    private OnInvoicesRequested onInvoicesRequested;
    private OnInvoiceStatusRequested onInvoiceStatusRequested;
    private final DatabaseOperations dbo;
    private int count = 0;
    private ArrayList<Invoice> invoices;

    public MobileMoney(Context context){
        this.context = context;
        dbo = new DatabaseOperations(context);
        invoices = new ArrayList<>();
    }

    /**
     * This method is used to initialize the mobile money sdk with
     * the credentials provided by MTN that verifies you to make
     * sure you have the authorization to use this adk
     * @param username This is the username provided by MTN
     * @param password  This is the password provided by MTN
     * @see String#toLowerCase()
     */
    public void initCredentials(String username,String password){
        new PrefsManager(context).setUser(username,password);
    }

    /**
     * This method is set a third party id if applicable.
     * A default one is provided by the sdk.
     * @param thirdPartyId This is your third party id to process transactions
     * @see String#toLowerCase()
     */
    public void setThirdPartyId(String thirdPartyId){
        new PrefsManager(context).setThirdPartyId(thirdPartyId);
    }

    /**
     * This method is used to start the transaction process.
     * sure you have the authorization to use this adk
     * @see String#toLowerCase()
     * @see Integer
     * @see Double
     * @see Context
     * @see OnTransactionInitiated
     * @param itemName The name of the recipient of the bill. Basically the user who created the bill. Example: Mr. Kojo Bediako
     * @param description  The particulars or description of the item(s) the invoice covers. Example:Application fee for reservation of hostel room 88 at Maame Donkor Student Hostel. This must be encoded using the MIME base64 encoding algorithm.
     * @param billPrompt Values are 0,2 and 3. If set to 2 asynchronous billprompt will be initiated. If set to 3, an sms containing the invoiceNo is sent automatically to the reciepient should the bill payment fail due to time out or insufficient funds or when an invalid mobile money number is supplied.
     * @param amount The amount (face value of the invoice) in the format XXXXXX.XX (floating point, two decimal places with a maximum value of 999999.99. Example 12.34. All amounts are regarded as Ghana Cedis. All transactions are real since there are not test servers.If in development move we suggest you use 0.01 as the amount.
     * @param number The phone number of the user who will receive the bill prompt. Example: 233241234567.
     * @param message The message you want sent to the recipient. One merge field is available for the invoice number( {inv} ). This will insert the invoice number when generated. This is required if mobile is provided.
     */
    public void makePurchase(Context context,String itemName, String description,int billPrompt, double amount, String number, String message,OnTransactionInitiated onTransactionInitiated){
        this.context = context;
        this.onTransactionInitiated = onTransactionInitiated;
        this.onTransactionInitiated.OnTransactionStarted();

        if (new ConnectivityUtils(context).isNetworkConnected()) {
            new MakePayment(itemName, description, String.valueOf(amount), billPrompt, number, message, false).execute();

        }else
            onTransactionInitiated.OnTransactionFailed(Constants.NO_INTERNET);
    }

    //Another purchase method used by the dialog to make transactions
    public void doPurchase(Context context, String itemName, String description, int billPrompt, double amount, String number, String message, OnTransactionInitiated onTransactionInitiated){
        this.context = context;
        this.onTransactionInitiated = onTransactionInitiated;
        this.onTransactionInitiated.OnTransactionStarted();

        if (new ConnectivityUtils(context).isNetworkConnected()) {
            new MakePayment(itemName, description, String.valueOf(amount), billPrompt, number, message, true).execute();
        }else
            onTransactionInitiated.OnTransactionFailed(Constants.NO_INTERNET);
    }


    /**
     * This method is used to start the transaction process with the ui provided by the library.
     * sure you have the authorization to use this adk
     * @see String#toLowerCase()
     * @see Integer
     * @see Double
     * @see Context
     * @see OnTransactionInitiated
     * @param itemName The name of the recipient of the bill. Basically the user who created the bill. Example: Mr. Kojo Bediako
     * @param description  The particulars or description of the item(s) the invoice covers. Example:Application fee for reservation of hostel room 88 at Maame Donkor Student Hostel. This must be encoded using the MIME base64 encoding algorithm.
     * @param billPrompt Values are 0,2 and 3. If set to 2 asynchronous billprompt will be initiated. If set to 3, an sms containing the invoiceNo is sent automatically to the reciepient should the bill payment fail due to time out or insufficient funds or when an invalid mobile money number is supplied.
     * @param amount The amount (face value of the invoice) in the format XXXXXX.XX (floating point, two decimal places with a maximum value of 999999.99. Example 12.34. All amounts are regarded as Ghana Cedis. All transactions are real since there are not test servers.If in development move we suggest you use 0.01 as the amount.
     * @param number The phone number of the user who will receive the bill prompt. Example: 233241234567.
     * @param message The message you want sent to the recipient. One merge field is available for the invoice number( {inv} ). This will insert the invoice number when generated. This is required if mobile is provided.
     */
    public void makePurchaseWithDialog(Context context,String itemName, String description,int billPrompt, double amount, String number, String message,OnTransactionInitiated onTransactionInitiated){
        this.context = context;
        this.onTransactionInitiated = onTransactionInitiated;
        this.onTransactionInitiated.OnTransactionStarted();

        if (new ConnectivityUtils(context).isNetworkConnected()) {

            MMDialog mmDialog = new MMDialog(context);
            mmDialog.MakePayment(itemName,description,amount,billPrompt,number,message,this.onTransactionInitiated);

        }else
            onTransactionInitiated.OnTransactionFailed(Constants.NO_INTERNET);
    }


    /**
     * This method is used to manually check the status of an invoice number.
     * Because of timeout issues, the system might be down for sometime.
     * All transactions are saved by the sdk and can manually be checked for its status at anytime.
     * @param invoice_number The invoice number you wish to check for its status
     * @see String#toLowerCase()
     * @see OnTransactionInitiated
     */
    public void checkInvoiceStatus(String invoice_number,OnInvoiceStatusRequested onInvoiceStatusRequested){
        this.onInvoiceStatusRequested = onInvoiceStatusRequested;

        if (new ConnectivityUtils(context).isNetworkConnected())
            new CheckInvoiceStatus(invoice_number).execute();
        else
            onInvoiceStatusRequested.OnTransactionFailed(Constants.NO_INTERNET);
    }

    /**
     * This method is used to retrieve all of the users invoices along with their statuses.
     * Useful when manual verification of invoice numbers is needed to cater for time out errors.
     * NB: all this data will be lost if the user clears the app data. Server solutions should be
     * put in place to avoid this.
     * Results will be an {@link ArrayList} objects of class {@link Invoice}
     * @see OnInvoicesRequested
     */
    public void getInvoices(OnInvoicesRequested onInvoicesRequested){
        this.onInvoicesRequested = onInvoicesRequested;
        new GetUserInvoices().execute();
    }

    /**
     * This method deletes a particular invoice number from the users transactions
     * @param invoice_number The invoice number to be deleted
     * @see String#toLowerCase()
     */
    public void deleteInvoice(String invoice_number){
        dbo.deleteInvoice(dbo,invoice_number);
    }

    /**
     * This method deletes all invoices of the user
     */
    public void clearTransactions(){
        dbo.clearDatabase(dbo);
    }

    private class GetUserInvoices extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onInvoicesRequested.OnRequestStarted();
        }

        @Override
        protected String doInBackground(String... strings) {
            invoices = dbo.getInvoices(dbo);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (invoices.size()>0)
                onInvoicesRequested.OnTransactionSuccessful(invoices);
            else
                onInvoicesRequested.OnNoInvoicesFound();
        }
    }

    private class MakePayment extends AsyncTask<String, Void, String> {
        final String name;
        final String description;
        final String amount;
        final String number;
        final String message;
        final int billprompt;
        final boolean showDialog;

        MakePayment(String name, String description, String amount, int billprompt,String number, String message, boolean showDialog){
            this.name = name;
            this.description = description;
            this.amount = amount;
            this.number = number;
            this.message = message;
            this.billprompt = billprompt;
            this.showDialog = showDialog;
        }

        @Override
        protected String doInBackground(String... params) {
            MobileMoneyPayment pc = new MobileMoneyPayment(context);
            return pc.postInvoice(name, description,billprompt, amount, number ,message);
        }

        @Override
        protected void onPostExecute(String result){
            String invoice_number="";
            try {
                invoice_number = XMLParser.getInvoiceNumber(result);
                Invoice invoice = new Invoice(name,invoice_number);
                dbo.setInvoice(dbo,invoice);
            }catch (Exception e){
                onTransactionInitiated.OnTransactionFailed(e.toString());
            }

            final String finalInvoice_number = invoice_number;
            if (!showDialog){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new CheckInvoiceStatus(finalInvoice_number).execute();
                    }
                },8000);
            }else
                onTransactionInitiated.onInvoiceMade(finalInvoice_number);

        }
    }

    private class CheckInvoiceStatus extends AsyncTask<String, Void, String> {

        final String invoice_number;

        CheckInvoiceStatus(String invoice_number){
            this.invoice_number = invoice_number;
        }

        @Override
        protected String doInBackground(String... params) {
            MobileMoneyPayment pc = new MobileMoneyPayment(context);
            if (!invoice_number.equalsIgnoreCase(""))
                return pc.checkInvStatus(Integer.parseInt(invoice_number));
            else
                return Constants.NO_INVOICE_NUMBER;
        }

        @Override
        protected void onPostExecute(String result){
            String status = XMLParser.transactionStatus(result);
            if (status.equalsIgnoreCase(Constants.TRANSACTION_COMPLETED)){
                onInvoiceStatusRequested.OnTransactionSuccessful();
                dbo.setIsStatusChecked(dbo,invoice_number);
                dbo.setIsValid(dbo,invoice_number,1);
            }else {
                if (count == 3){
                    onInvoiceStatusRequested.OnTransactionFailed(status);
                    dbo.setIsStatusChecked(dbo,invoice_number);
                    dbo.setIsValid(dbo,invoice_number,0);
                }else {
                    new CheckInvoiceStatus(invoice_number).execute();
                    count++;
                }
            }
        }
    }
}
