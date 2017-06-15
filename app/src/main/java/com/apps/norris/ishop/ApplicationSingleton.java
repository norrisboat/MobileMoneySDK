package com.apps.norris.ishop;

import android.app.Application;

import com.apps.sancorp.paywithmobilemoney.core.MobileMoney;

/**
 * Created by norrisboateng on 6/15/17.
 */

public class ApplicationSingleton extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final MobileMoney mobileMoney = new MobileMoney(this);
        //replace it with your credentials
        mobileMoney.initCredentials("lphcQhsLy","USadV47GB");
    }
}
