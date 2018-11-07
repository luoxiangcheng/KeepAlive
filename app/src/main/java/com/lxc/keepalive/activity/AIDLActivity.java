package com.lxc.keepalive.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lxc.keepalive.PersonManager;
import com.lxc.keepalive.R;
import com.lxc.keepalive.services.MyService4;

public class AIDLActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AIDLActivity";

    private Context mContext;

    private Button mBtnBind, mBtnUnbind, mBtnCalculate;

    private PersonManager mPersonManager;
    private ServiceConnection mServiceConnection;

    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        mContext = this;

        mBtnBind = findViewById(R.id.btn_bind);
        mBtnUnbind = findViewById(R.id.btn_unbind);
        mBtnCalculate = findViewById(R.id.btn_calculate);

        mBtnBind.setOnClickListener(this);
        mBtnUnbind.setOnClickListener(this);
        mBtnCalculate.setOnClickListener(this);

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected...");
                mPersonManager = PersonManager.Stub.asInterface(service);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected...");
                mPersonManager = null;
                mBound = false;
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind:
                Intent intent = new Intent(mContext, MyService4.class);
                mContext.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
                break;

            case R.id.btn_unbind:
                if (mBound) {
                    mContext.unbindService(mServiceConnection);
                    mBound = false;
                }
                break;

            case R.id.btn_calculate:
                if (!mBound) {
                    Log.d(TAG, "服务未连接");
                    return;
                }

                int result = 0;
                try {
                    result = mPersonManager.add(1, 3);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "计算结果为：" + result);
                break;
        }
    }
}