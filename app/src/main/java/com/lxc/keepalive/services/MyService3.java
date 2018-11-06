package com.lxc.keepalive.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by luoxiangcheng on 2018/10/30 16:06
 * 绑定服务方式(使用Messenger)
 * 注意：当在AndroidManifest中添加了android:process=":remote"代码后，
 * MyService3中的log在当前项目过滤状态下看不到了，起初还以为写法没生效，
 * 将log过滤规则设为No Filters即可(配上关键字更清晰)
 */

public class MyService3 extends Service {

    private static final String TAG = "MyService3";
    public static final int MSG_SAY_HELLO = 1;
    public static final String REPLY_SAY_HELLO = "reply_say_hello";

    private Messenger mMessenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind...");
        return mMessenger.getBinder();
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

    /**
     * 用于接收从客户端传递过来的数据
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Log.d(TAG, "收到了客户端的消息");
                    // 回复客户端信息,该对象由客户端传递过来
                    Messenger messenger = msg.replyTo;
                    // 获取回复信息的消息实体
                    Message message = Message.obtain(null, MSG_SAY_HELLO);
                    Bundle bundle = new Bundle();
                    bundle.putString(REPLY_SAY_HELLO, "我已经收到客户端的消息啦");
                    message.setData(bundle);
                    // 向客户端发送消息
                    try {
                        messenger.send(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
