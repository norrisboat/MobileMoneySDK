package com.apps.sancorp.paywithmobilemoney.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.sancorp.paywithmobilemoney.R;
import com.apps.sancorp.paywithmobilemoney.core.MobileMoney;
import com.apps.sancorp.paywithmobilemoney.core.OnInvoiceStatusRequested;
import com.apps.sancorp.paywithmobilemoney.core.OnTransactionInitiated;
import com.apps.sancorp.paywithmobilemoney.utils.Constants;

/**
 * Created by norrisboateng on 6/13/17.
 */

public class MMDialog extends Dialog {

    private OnTransactionInitiated onTransactionInitiated;
    private String name,description,getInvoiceNumber;
    private double amount;
    private String number;
    private String message;
    private int billPrompt;
    private final Context mContext;
    private EditText phoneNumber;
    private MaterialProgressBar progressBar;
    private TextView title;
    private TextInputLayout textInputEditText;
    private ImageView close,responseIcon;
    private Button actions;
    private int progress = Constants.PHONE_NUMBER;
    private MobileMoney mobileMoney;

    public MMDialog(@NonNull Context context) {
        super(context,R.style.mm_dialog);
        mContext=context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mm_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        initView();
        initListeners();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        close = (ImageView) findViewById(R.id.close);
        responseIcon = (ImageView) findViewById(R.id.response_icon);
        phoneNumber = (EditText) findViewById(R.id.phone);
        progressBar = (MaterialProgressBar) findViewById(R.id.progress_bar);
        title = (TextView) findViewById(R.id.title);
        actions = (Button) findViewById(R.id.actions);
        textInputEditText = (TextInputLayout) findViewById(R.id.text_input);

        mobileMoney = new MobileMoney(mContext);

        if (TextUtils.isEmpty(number))
            phoneNumber.setText("233");
        else
            phoneNumber.setText(number);

    }

    private void initListeners() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTransactionInitiated.OnTransactionFailed(Constants.DIALOG_CLOSED);
                dismiss();
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (progress == Constants.PHONE_NUMBER){
                    boolean isValid = s.toString().trim().length() == 12;
                    actions.setEnabled(isValid);
                    if (!isValid)
                        textInputEditText.setError("Phone number invalid");
                    else
                        textInputEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progress == Constants.PHONE_NUMBER){
                    if (!actions.isEnabled())
                        Toast.makeText(mContext, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    else
                        waitForPrompt();
                }else if (progress == Constants.CONFIRM){
                    CheckInvoiceStatus();
                }else if (progress == Constants.CONFIRM_RESPONSE){
                    dismiss();
                }
            }
        });
    }

    private void waitForPrompt() {
        progress = Constants.WAITING_FOR_BILL;
        actions.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        phoneNumber.setVisibility(View.GONE);
        textInputEditText.setVisibility(View.GONE);

        title.setText(mContext.getResources().getString(R.string.bill_prompt));

        mobileMoney.doPurchase(mContext, name, description, billPrompt, amount, number, message, new OnTransactionInitiated() {
            @Override
            public void OnTransactionStarted() {

            }

            @Override
            public void OnTransactionSuccessful(String invoiceNumber) {

            }

            @Override
            public void OnTransactionFailed(String errorCode) {

            }

            @Override
            public void onInvoiceMade(String invoiceNumber) {
                getInvoiceNumber = invoiceNumber;
                confirmTransaction();
            }
        });

    }

    private void confirmTransaction() {
        progress = Constants.CONFIRM;
        title.setText(mContext.getResources().getString(R.string.warning));
        progressBar.setVisibility(View.GONE);
        if (actions.getVisibility() == View.GONE)
            actions.setVisibility(View.VISIBLE);

        actions.setText(mContext.getResources().getString(R.string.confirm));
    }

    private void CheckInvoiceStatus(){
        title.setText(mContext.getResources().getString(R.string.confirming));
        actions.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        mobileMoney.checkInvoiceStatus(getInvoiceNumber, new OnInvoiceStatusRequested() {
            @Override
            public void OnTransactionStarted() {

            }

            @Override
            public void OnTransactionSuccessful() {
                onTransactionInitiated.OnTransactionSuccessful(getInvoiceNumber);
                displayResults(String.format(mContext.getResources().getString(R.string.results),name,amount),R.drawable.success);

            }

            @Override
            public void OnTransactionFailed(String errorCode) {
                displayResults(getError(errorCode),R.drawable.error);
                onTransactionInitiated.OnTransactionFailed(errorCode);
            }
        });
    }

    private void displayResults(String description,int icon) {
        progress = Constants.CONFIRM_RESPONSE;

        title.setText(description);

        responseIcon.setVisibility(View.VISIBLE);
        responseIcon.setImageResource(icon);

        if (actions.getVisibility() == View.GONE)
            actions.setVisibility(View.VISIBLE);

        actions.setText(mContext.getResources().getString(R.string.done));

        if (progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.GONE);
    }

    private String getError(String errorCode){
        if (TextUtils.equals(errorCode,Constants.BILL_NOT_PAID))
            return "Bill Not Paid";
        else
            return "Transaction Error";
    }

    public void MakePayment(String name, String description, double amount, int billprompt,String number, String message,OnTransactionInitiated onTransactionInitiated){
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.message = message;
        this.number = number;
        this.billPrompt = billprompt;
        this.onTransactionInitiated = onTransactionInitiated;
        show();
    }
}
