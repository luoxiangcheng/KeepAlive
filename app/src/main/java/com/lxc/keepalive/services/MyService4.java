package com.lxc.keepalive.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lxc.keepalive.PersonManager;

/**
 * Created by luoxiangcheng on 2018/11/7 10:49
 */

public class MyService4 extends Service {

    private static final String TAG = "MyService4";

    private IBinder mIBinder = new PersonManager.Stub() {

        @Override
        public int add(int x, int y) throws RemoteException {
            return x + y;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind...");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate...");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy...");
        super.onDestroy();
    }
}
