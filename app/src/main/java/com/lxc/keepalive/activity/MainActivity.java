package com.lxc.keepalive.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lxc.keepalive.R;
import com.lxc.keepalive.services.MyService1;
import com.lxc.keepalive.services.MyService2;
import com.lxc.keepalive.utils.AppUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String TARGET_APP_PACKAGENAME = "com.example.lxc.multidemo";
    private static final String TARGET_APP_SPECIAL_ACTIVITY_NAME = "com.example.lxc.multidemo.activity.ChangeLayoutActivity";

    private Context mContext;

    private Button mBtnToMainActivity, mBtnToSpecialActivity, mBtnStartService, mBtnStopService,
            mBtnBindService, mBtnUnbindService, mBtnGetData, mBtnToMessenger,mBtnAidl;

    private MyService2 mService2;
    private ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mBtnToMainActivity = findViewById(R.id.btn_to_main_activity);
        mBtnToSpecialActivity = findViewById(R.id.btn_to_special_activity);
        mBtnStartService = findViewById(R.id.btn_start_service);
        mBtnStopService = findViewById(R.id.btn_stop_service);
        mBtnBindService = findViewById(R.id.btn_bind_service);
        mBtnUnbindService = findViewById(R.id.btn_unbind_service);
        mBtnGetData = findViewById(R.id.btn_get_data);
        mBtnToMessenger = findViewById(R.id.btn_to_messenger);
        mBtnAidl = findViewById(R.id.btn_aidl);

        mBtnToMainActivity.setOnClickListener(this);
        mBtnToSpecialActivity.setOnClickListener(this);
        mBtnStartService.setOnClickListener(this);
        mBtnStopService.setOnClickListener(this);
        mBtnBindService.setOnClickListener(this);
        mBtnUnbindService.setOnClickListener(this);
        mBtnGetData.setOnClickListener(this);
        mBtnToMessenger.setOnClickListener(this);
        mBtnAidl.setOnClickListener(this);

        mServiceConnection = new ServiceConnection() {
            /**
             * 与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象，
             * 通过这个IBinder对象，实现宿主和Service的交互。
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                MyService2.LocalBinder binder = (MyService2.LocalBinder) service;
                mService2 = binder.getService();
            }

            /**
             * 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时，
             * 例如内存的资源不足时这个方法才被自动调用。
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
                mService2 = null;
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_main_activity:
                boolean b = AppUtil.isAppAvilibleSimple(mContext, TARGET_APP_PACKAGENAME);
                if (b) {
                    Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(TARGET_APP_PACKAGENAME);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "找不到该应用", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_to_special_activity: {
                Intent intent = new Intent();
                try {
                    // 方法一
                    ComponentName cn = new ComponentName(TARGET_APP_PACKAGENAME, TARGET_APP_SPECIAL_ACTIVITY_NAME);
                    intent.setComponent(cn);

                    // 方法二
//                    intent.setClassName(TARGET_APP_PACKAGENAME, TARGET_APP_SPECIAL_ACTIVITY_NAME);

                    // 添加附带参数
                    intent.putExtra("test", "intent1");
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "找不到该应用或指定页面", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.btn_start_service: {
                Intent intent = new Intent(mContext, MyService1.class);
                mContext.startService(intent);
                break;
            }

            case R.id.btn_stop_service: {
                // 也可以在开启服务的时候保存一个全局变量，下面直接调用stopService传入该全局变量即可
                Intent intent = new Intent(mContext, MyService1.class);
                mContext.stopService(intent);
                break;
            }

            case R.id.btn_bind_service:
                Intent intent = new Intent(mContext, MyService2.class);
                mContext.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
                break;

            case R.id.btn_unbind_service:
                if (mService2 != null) {
                    mService2 = null;
                    mContext.unbindService(mServiceConnection);
                }
                break;

            case R.id.btn_get_data:
                if (mService2 == null) {
                    Log.d(TAG, "服务未连接");
                } else {
                    Log.d(TAG, "拿到数据为：" + mService2.getCount());
                }
                break;

            case R.id.btn_to_messenger:
                startActivity(new Intent(mContext, MessengerActivity.class));
                break;

            case R.id.btn_aidl:
                startActivity(new Intent(mContext, AIDLActivity.class));
                break;
        }
    }
}
