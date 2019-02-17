package com.zdmysocketservice.util;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static String clientMessage;
    /**
     * 启动服务监听，等待客户端连接
     */
    public static void startService( final clientMessageCallBack callBack) {
        Log.e("zhengdan"," startSocketServer  123456");
        try {
            // 创建ServerSocket
            ServerSocket serverSocket = new ServerSocket(9999);
            Log.e("zhengdan","--开启服务器，监听端口 9999--");

            // 监听端口，等待客户端连接

            while (true) {
                Log.e("zhengdan","--等待客户端连接--");
                Socket socket = serverSocket.accept(); //等待客户端连接
                Log.e("zhengdan","得到客户端连接：" + socket);

                startReader(socket,callBack);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private static void startReader(final Socket socket, final clientMessageCallBack callBack) {

//        new Thread(){
//            @Override
//            public void run() {
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream( socket.getInputStream());
                    while (true) {
                        Log.e("zhengdan","*等待客户端输入*");
                        // 读取数据
                        String msg = reader.readUTF();
                        Log.e("zhengdan","获取到客户端的信息：" + msg);
                        clientMessage = msg;
                        callBack.getClientMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            }
//        }.start();
    }

    public interface clientMessageCallBack{
        void getClientMessage(String message);
    }
}
