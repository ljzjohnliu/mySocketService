package com.socket.service;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.socket.service.util.TcpServer;
import com.socket.service.util.WifiInfoUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesTestActivity extends AppCompatActivity {

    private TextView mWifiName;
    private TextView mIpAddress;
    private EditText mSendMessage;
    private boolean mIsWifiEnable;
    private Button mSendBtn;
    ExecutorService exec = Executors.newCachedThreadPool();
    private static TcpServer tcpServer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_test_layout);
        mWifiName = findViewById(R.id.wifi_name);
        mIpAddress = findViewById(R.id.ip_address);
        mSendMessage = findViewById(R.id.message_edit);
        mIsWifiEnable = WifiInfoUtil.isWifiEnabled(this);
        mSendBtn = findViewById(R.id.send_recevice_message_btn);
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
//                tcpServer = new TcpServer(getHost(mSendMessage.getText().toString()));
//                exec.execute(tcpServer);
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        tcpServer.SST.get(0).send(mSendMessage.getText().toString());
                    }
                });


            }
        });

    }


    private int getHost(String msg) {
        if (msg.equals("")) {
            msg = "1234";
        }
        return Integer.parseInt(msg);
    }
}
