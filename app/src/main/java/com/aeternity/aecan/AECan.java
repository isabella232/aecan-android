package com.aeternity.aecan;
import android.app.Application;

import com.aeternity.aecan.network.RestClient;
import com.aeternity.aecan.persistence.SessionPersistence;
import com.jakewharton.threetenabp.AndroidThreeTen;

import io.paperdb.Paper;

public class AECan extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        RestClient.init(this);
        Paper.init(this);
        SessionPersistence.initObserver();
    }
}
