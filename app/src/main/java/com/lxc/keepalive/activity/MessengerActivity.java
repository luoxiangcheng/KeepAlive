package com.lxc.keepalive.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lxc.keepalive.R;
import com.lxc.keepalive.services.MyService3;

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MessengerActivity";

    private Context mContext;
    private Messenger mMessenger;
    private Messenger mReplyMessenger = new Messenger(new ReceiverReplyMsgHandler());
    private ServiceConnection mServiceConnection;

    private Button mBtnBind, mBtnUnbind, mBtnSendMsg;

    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        mContext = this;

        mBtnBind = findViewById(R.id.btn_bind);
        mBtnUnbind = findViewById(R.id.btn_unbind);
        mBtnSendMsg = findViewById(R.id.btn_send_msg);

        mBtnBind.setOnClickListener(this);
        mBtnUnbind.setOnClickListener(this);
        mBtnSendMsg.setOnClickListener(this);

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                mMessenger = new Messenger(service);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
                mMessenger = null;
                mBound = false;
            }
        };
    }

    private class ReceiverReplyMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 接收服务端回复
                case MyService3.MSG_SAY_HELLO:
                    Log.d(TAG, "收到了服务端的回复");
                    String reply = msg.getData().getString(MyService3.REPLY_SAY_HELLO);
                    Log.d(TAG, "我已经收到服务端的回复啦 >>> " + reply);
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
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
                Intent intent = new Intent(mContext, MyService3.class);
                mContext.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
                break;

            case R.id.btn_unbind:
                unBind();
                break;

            case R.id.btn_send_msg:
                if (!mBound) {
                    Log.d(TAG, "服务未连接");
                    return;
                }

                // 注意：只要连接过服务一次，mMessenger不为null，
                // 那么即使调用unbindService(前提不把mMessenger置为null，并且上面的mBound判断条件去掉)，
                // 那么下面代码还能正常通信
                // 创建与服务端交互的消息实体Message
                Message message = Message.obtain(null, MyService3.MSG_SAY_HELLO, 0, 0);
                // 把接收服务器端的回复的Messenger通过Message的replyTo参数传递给服务端
                message.replyTo = mReplyMessenger;
                try {
                    mMessenger.send(message);
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
            mMessenger = null;
            mBound = false;
        }
    }
}
