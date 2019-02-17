package com.zdmysocketservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.zd.mysocketservice.R;
import com.zdmysocketservice.util.SocketServer;
import com.zdmysocketservice.util.WifiInfoUtil;

public class ServicesTestTwoActivity extends AppCompatActivity {

    private TextView mWifiName;
    private TextView mIpAddress;
    private EditText mSendMessage;
    private boolean mIsWifiEnable;
    private Button mSendBtn;
    private Button mStartConnectBtn;
    private TextView mRecevierClientMessText;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("zhengdan", "handleMessage  ***********");
            int what = msg.what;
            if (what == 1) {
                String info = (String) msg.obj;
                if (!TextUtils.isEmpty(info)) {
                    Log.e("zhengdan", "handleMessage  ***********  收到客户端消息   " + info);
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
        mSendMessage = findViewById(R.id.message_edit);
        mIsWifiEnable = WifiInfoUtil.isWifiEnabled(this);
        mSendBtn = findViewById(R.id.send_recevice_message_btn);

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

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mStartConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new Thread() {
                    @Override
                    public void run() {
                        final SocketServer mSocketServer = new SocketServer();
                        mSocketServer.startService(new SocketServer.clientMessageCallBack() {
                            @Override
                            public void getClientMessage(String message) {
                                Log.d("zhengdan", "getClientMessage: message = " + message);
                                Log.d("zhengdan", "getClientMessage: myHandler.getLooper() = " + myHandler.getLooper());
                                if (!TextUtils.isEmpty(message)) {
                                    Message message1 = myHandler.obtainMessage();
                                    message1.what = 1;
                                    message1.arg1 = 1;
                                    message1.obj = message;
                                    Log.d("zhengdan", "getClientMessage: message1.obj = " + message1.obj);
                                    myHandler.sendMessage(message1);
                                }
                            }
                        });
                    }
                }.start();
            }
        });

    }
}
