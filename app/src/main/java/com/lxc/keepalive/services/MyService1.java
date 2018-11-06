package com.lxc.keepalive.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by luoxiangcheng on 2018/10/30 13:59
 * 启动服务方式
 */

public class MyService1 extends Service {

    private static final String TAG = "MyService1";

    private Context mContext;
    private Handler mHandler = new Handler();
    private int mCount = 0;
    private boolean mIsDestroy = false;

    /**
     * 该方法在绑定服务方式时才要实现，启动服务方式直接返回null
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind...");
        return null;
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）,
     * 如果服务已在运行，则不会调用此方法，该方法只被调用一次。
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate...");
        mContext = this;
        super.onCreate();
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand...");
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
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy...");
        mIsDestroy = true;
        super.onDestroy();
    }
}
