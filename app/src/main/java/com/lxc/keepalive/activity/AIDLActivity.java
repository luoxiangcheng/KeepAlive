package com.lxc.keepalive.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lxc.keepalive.PersonManager;
import com.lxc.keepalive.R;
import com.lxc.keepalive.bean.Person;
import com.lxc.keepalive.services.MyService4;

import java.util.List;

public class AIDLActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AIDLActivity";

    private Context mContext;

    private Button mBtnBind, mBtnUnbind, mBtnCalculate, mBtnAddPerson;
    private EditText mEditA, mEditB;
    private TextView mTvContent;

    private PersonManager mPersonManager;
    private ServiceConnection mServiceConnection;

    private boolean mBound = false;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        mContext = this;

        mBtnBind = findViewById(R.id.btn_bind);
        mBtnUnbind = findViewById(R.id.btn_unbind);
        mBtnCalculate = findViewById(R.id.btn_calculate);
        mBtnAddPerson = findViewById(R.id.btn_add_person);

        mEditA = findViewById(R.id.edit_a);
        mEditB = findViewById(R.id.edit_b);
        mTvContent = findViewById(R.id.text_content);

        mBtnBind.setOnClickListener(this);
        mBtnUnbind.setOnClickListener(this);
        mBtnCalculate.setOnClickListener(this);
        mBtnAddPerson.setOnClickListener(this);

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
    protected void onDestroy() {
        super.onDestroy();
        // 注意：通过绑定服务方式，在页面销毁时需判断解绑，
        // 否则会报android.app.ServiceConnectionLeaked错误，类似于Dialog
        unBind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind:
                Intent intent = new Intent(mContext, MyService4.class);
                mContext.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
                break;

            case R.id.btn_unbind:
                unBind();
                break;

            case R.id.btn_calculate:
                if (!mBound) {
                    Toast.makeText(mContext, "服务未连接...", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "服务未连接");
                    return;
                }

                String a = mEditA.getText().toString();
                String b = mEditB.getText().toString();
                if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b)) {
                    Toast.makeText(mContext, "请输入正确的值", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 注意：只要连接过服务一次，mPersonManager不为null，
                // 那么即使调用unbindService(前提不把mPersonManager置为null，并且上面的mBound判断条件去掉)，
                // 那么下面代码还能继续取到值
                int result = 0;
                try {
                    result = mPersonManager.add(Integer.parseInt(a), Integer.parseInt(b));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "计算结果为：" + result);
                mTvContent.setText("计算结果为：" + result);
                break;

            case R.id.btn_add_person:
                if (!mBound) {
                    Toast.makeText(mContext, "服务未连接...", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "服务未连接...");
                    return;
                }

                mCount++;
                Person person = new Person("小样" + mCount);
                try {
                    mPersonManager.addPerson(person);
                    // 注意：mPersonManager.getPersonList()指向的对象是同一个，比如在KeepAlive中添加了Person，
                    // 在MuitlDemo中拿到的list里也包含，反之亦然，这也证明了跨进程通讯
                    List<Person> personList = mPersonManager.getPersonList();
                    mTvContent.setText(personList.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 解绑服务
     */
    private void unBind() {
        if (mBound) {
            mContext.unbindService(mServiceConnection);
            mPersonManager = null;
            mBound = false;
        }
    }
}
