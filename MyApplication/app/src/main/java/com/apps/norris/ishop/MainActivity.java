package com.apps.norris.ishop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apps.sancorp.paywithmobilemoney.core.MobileMoney;
import com.apps.sancorp.paywithmobilemoney.core.OnInvoiceStatusRequested;
import com.apps.sancorp.paywithmobilemoney.core.OnInvoicesRequested;
import com.apps.sancorp.paywithmobilemoney.core.OnTransactionInitiated;
import com.apps.sancorp.paywithmobilemoney.model.Invoice;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MobileMoney mobileMoney = new MobileMoney(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileMoney.makePurchaseWithDialog(MainActivity.this, "iPhone 6 64GB", " ", 3, 0.01, "233553311070", "  ", new OnTransactionInitiated() {
                    @Override
                    public void onInvoiceMade(String invoiceNumber) {

                    }

                    @Override
                    public void OnTransactionStarted() {
                        Toast.makeText(MainActivity.this, "Transaction started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnTransactionSuccessful(String invoiceNumber) {
                        Toast.makeText(MainActivity.this, "Payment complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnTransactionFailed(String errorCode) {
                        Toast.makeText(MainActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
