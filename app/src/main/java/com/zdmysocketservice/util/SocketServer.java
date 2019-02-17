package com.zdmysocketservice.util;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

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
                Log.e("zhengdan", " startSocketServer  123456");
                try {
                    // 创建ServerSocket
                    ServerSocket serverSocket = new ServerSocket(9999);
                    Log.e("zhengdan", "--开启服务器，监听端口 9999--");

                    // 监听端口，等待客户端连接

                    while (true) {
                        Log.e("zhengdan", "--等待客户端连接--");
                        mSocket = serverSocket.accept(); //等待客户端连接
                        Log.e("zhengdan", "得到客户端连接：" + mSocket);
//                        if(RECEVICE_MESSAGE.equals(type)){
                        startReader(mSocket, callBack);
//                        }else if(SEND_MESSAGE.equals(type)){
//                            sendMessagetoClient(socket, callBack);
//                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 从参数的Socket里获取最新的消息
     */
    public static void startReader(final Socket socket, final clientMessageCallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                //接收客户端消息
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    while (true) {
                        Log.e("zhengdan", "*等待客户端输入*");
                        // 读取数据
                        String msg = reader.readUTF();
                        Log.e("zhengdan", "获取到客户端的信息：" + msg);
                        clientMessage = msg;
                        callBack.getMessage(msg);
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 发送数据给客户端
     */
    public static void sendMessagetoClient(final String messageStr) {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("123");
                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                        writer.writeUTF(messageStr);
                        writer.close();
//                    socket.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }


    public interface clientMessageCallBack {
        void getMessage(String message);
    }
}
