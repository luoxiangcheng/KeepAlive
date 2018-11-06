package com.lxc.keepalive.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by luoxiangcheng on 2018/10/30 14:48
 * 绑定服务方式(扩展Binder类)
 */

public class MyService2 extends Service {

    private static final String TAG = "MyService2";

    private Context mContext;
    private LocalBinder mBinder = new LocalBinder();
    private Handler mHandler = new Handler();

    private int mCount = 0;
    private boolean mIsDestroy = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind...");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind...");
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        // 声明一个方法getService，提供给客户端调用
        public MyService2 getService() {
            // 返回当前对象MyService2,这样我们就可在客户端调用Service的公共方法了
            return MyService2.this;
        }
    }

    @Override
    public void onCreate() {
        mContext = this;
        Log.d(TAG, "onCreate...");
        super.onCreate();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mIsDestroy) {
                    return;
                }

                mCount++;
                Log.d(TAG, "count = " + mCount);
                if (mCount % 7 == 0) {
                    Toast.makeText(mContext, "alive", Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy...");
        mIsDestroy = true;
        super.onDestroy();
    }

    public int getCount() {
        return mCount;
    }
}
