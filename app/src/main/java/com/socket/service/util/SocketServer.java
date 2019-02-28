package com.socket.service.util;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private static final String TAG = "SocketServer";
    public static String clientMessage;
    public static String RECEVICE_MESSAGE = "recevice_message";
    public static String SEND_MESSAGE = "send_message";
    public static Socket mSocket;

    /**
     * 启动服务监听，等待客户端连接
     */
    public static void startService(final clientMessageCallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "SocketServer, startSocketServer!");
                try {
                    // 创建ServerSocket
                    ServerSocket serverSocket = new ServerSocket(9999);
                    Log.d(TAG, "--开启服务器，监听端口 9999--");

                    // 监听端口，等待客户端连接

                    while (true) {
                        Log.d(TAG, "--等待客户端连接--");
                        mSocket = serverSocket.accept(); //等待客户端连接
                        Log.d(TAG, "得到客户端连接：" + mSocket);
//                        if(RECEVICE_MESSAGE.equals(type)){
                        startReader(mSocket, callBack);
//                        }else if(SEND_MESSAGE.equals(type)){
//                            sendMessagetoClient(socket, callBack);
                        }
//                    }
                } catch (IOException e) {
                    Log.e(TAG, "startService, run: e = " + e);
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 从参数的Socket里获取最新的消息
     */
    public static void startReader(final Socket socket, final clientMessageCallBack callBack) {
        //接收客户端消息
        DataInputStream reader;
        try {
            // 获取读取流
            reader = new DataInputStream(socket.getInputStream());
            while (true) {
                Log.d(TAG, "startReader, *等待客户端输入*");
                // 读取数据
                String msg = reader.readUTF();
                Log.d(TAG, "startReader, 获取到客户端的信息：" + msg);
                clientMessage = msg;
                callBack.getMessage(msg);
//                reader.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "startReader: e = " + e);
            e.printStackTrace();
        }
    }

    // 网上抄来的，将 int 转成字节
    public static byte[] i2b(int i) {
        return new byte[]{
                (byte) ((i >> 24) & 0xFF),
                (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) (i & 0xFF)
        };
    }

    /**
     * 发送数据给客户端
     */
    public static void sendMessagetoClient(final String messageStr) {
        new Thread() {
            @Override
            public void run() {
                try {
//                    while (true) {
                        Log.d(TAG, "sendMessagetoClient, run: 1111");
                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                    Log.d(TAG, "sendMessagetoClient, run: 2222");
                        writer.writeUTF(messageStr);
                    Log.d(TAG, "sendMessagetoClient, run: 3333");
//                        writer.close();
                    Log.d(TAG, "sendMessagetoClient, run: 4444");
//                    socket.close();
//                    }
                } catch (IOException e) {
                    Log.e(TAG, "sendMessagetoClient, run: e = " + e);
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 发送数据给客户端
     */
    public static void sendLotMessagetoClient(final String messageStr) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //发送方式1
//                    Log.d(TAG, "sendMessagetoClient, run: 1111");
//                    DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
//                    Log.d(TAG, "sendMessagetoClient, run: 2222");
//                    writer.writeUTF(messageStr);
//                    Log.d(TAG, "sendMessagetoClient, run: 3333");
////                        writer.close();
//                    Log.d(TAG, "sendMessagetoClient, run: 4444");

                    //发送方式2
//                    OutputStream os = mSocket.getOutputStream();
//                    os.write(i2b(messageStr.length()));         // 输出文件名长度
//                    os.write(messageStr.getBytes());
//                    os.close();

//                    for(int i=0;i<10;i++){
//                        OutputStream os = mSocket.getOutputStream();
//                        os.write(messageStr.getBytes());
////                        System.out.println("send message " + i + " " + messageStr);
//                    }

                    for (int i=0; i<10; i++) {
                        Log.d(TAG, "sendLotMessagetoClient, run: 1111");
                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                        Log.d(TAG, "sendLotMessagetoClient, run: 2222");
                        writer.writeUTF(messageStr);
                        Log.d(TAG, "sendLotMessagetoClient, run: 3333");
//                        writer.close();
                    }

                } catch (IOException e) {
                    Log.e(TAG, "sendMessagetoClient, run: e = " + e);
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public interface clientMessageCallBack {
        void getMessage(String message);
    }
}
