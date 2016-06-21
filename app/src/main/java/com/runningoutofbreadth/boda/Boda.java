package com.runningoutofbreadth.boda;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by SandD on 6/20/2016.
 */
public class Boda extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
