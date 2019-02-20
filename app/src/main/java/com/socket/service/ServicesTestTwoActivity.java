package com.socket.service;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socket.service.util.SocketServer;
import com.socket.service.util.WifiInfoUtil;

/**
 * @author ljz
 *         服务端界面
 */
public class ServicesTestTwoActivity extends AppCompatActivity {

    private static final String TAG = "ServicesTestTwoActivity";

    private SocketServer mSocketServer;
    private TextView mWifiName;
    private TextView mIpAddress;
    private EditText mMessageEdit;
    private boolean mIsWifiEnable;
    private Button mSendtoClientBtn;
    private Button mStartConnectBtn;
    private TextView mRecevierClientMessText;

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "ServicesTestTwoActivity, handleMessage  ***********");
            int what = msg.what;
            if (what == 1) {
                String info = (String) msg.obj;
                if (!TextUtils.isEmpty(info)) {
                    Log.d(TAG, "ServicesTestTwoActivity, handleMessage  ***********  收到客户端消息   " + info);
                    Toast.makeText(ServicesTestTwoActivity.this, "收到客户端消息  " + info, Toast.LENGTH_LONG).show();
                    mRecevierClientMessText.setText(info);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_test_two_layout);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mWifiName = findViewById(R.id.wifi_name);
        mIpAddress = findViewById(R.id.ip_address);
        mMessageEdit = findViewById(R.id.message_edit);
        mIsWifiEnable = WifiInfoUtil.isWifiEnabled(this);
        mSendtoClientBtn = findViewById(R.id.send_message_toclient_btn);

        mStartConnectBtn = findViewById(R.id.start_connect_btn);
        mRecevierClientMessText = findViewById(R.id.receve_client_message_text);
        if (mIsWifiEnable) {
            String ssidStr = WifiInfoUtil.getSSID(this);
            if (!TextUtils.isEmpty(ssidStr)) {
                mWifiName.setText(ssidStr);
            }

            int wifiAdress = WifiInfoUtil.getWifiIp(this);
            if (wifiAdress != -1) {
                String ipStr = WifiInfoUtil.intToIp(wifiAdress);
                mIpAddress.setText(ipStr);
            }
        }

        mSocketServer = new SocketServer();

        mStartConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mSocketServer == null) {
                    return;
                }
                mSocketServer.startService(new SocketServer.clientMessageCallBack() {
                    @Override
                    public void getMessage(String msg) {
                        Log.d(TAG, "getMessage: message = " + msg);
                        if (!TextUtils.isEmpty(msg)) {
                            Message message = myHandler.obtainMessage();
                            message.what = 1;
                            message.arg1 = 1;
                            message.obj = msg;
                            Log.d(TAG, "getMessage: message1.obj = " + message.obj);
                            myHandler.sendMessage(message);
                        }
                    }
                });
            }
        });

        mSendtoClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesStr = mMessageEdit.getText().toString();
                Log.d(TAG, "ServicesTestTwoActivity, mSendtoClientBtn 要发送的数据：" + mesStr);
                Log.d(TAG, "ServicesTestTwoActivity, mSendtoClientBtn mSocketServer：" + mSocketServer);
                if (mSocketServer != null) {
                    mSocketServer.sendMessagetoClient(mesStr);
                }
            }
        });
    }
}
