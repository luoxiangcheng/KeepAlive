package com.lxc.keepalive.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lxc.keepalive.PersonManager;
import com.lxc.keepalive.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoxiangcheng on 2018/11/7 10:49
 */

public class MyService4 extends Service {

    private static final String TAG = "MyService4";

    private List<Person> mPersonList = new ArrayList<>();

    private IBinder mIBinder = new PersonManager.Stub() {

        @Override
        public int add(int x, int y) throws RemoteException {
            return x + y;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {
            mPersonList.add(person);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return mPersonList;
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
