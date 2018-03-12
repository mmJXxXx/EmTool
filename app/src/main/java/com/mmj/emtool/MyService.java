package com.mmj.emtool;
/**
 服务类
 */

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MyService extends Service {
    BufferedWriter writer = null;
    BufferedReader reader = null;
    static String valuebuff=null;
    static String valueT = null;
    static String valueV = null;

    NotificationManager manager;
    Notification notification;
    static int checknum=0;
//    SimpleDateFormat  formatter  =  new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
//    Date curDate = new Date(System.currentTimeMillis());
    static String msgdatanum="0";
    static Socket socket;
    private  MessageBinder mBinder = new MessageBinder();
    public static int getnumback(){
        return checknum;
    }
    public static void setnumback(){
        checknum=1;
    }


    class MessageBinder extends Binder{
      public void startmsg(){

      }
      public String msgback(){
          return "";
      }
    }
    public MyService() {
    }
    @Override//onCreate 服务创建时调用
    public void onCreate(){
        super.onCreate();
    }
    @SuppressLint("StaticFieldLeak")
    @Override//onCreate 服务启动时调用
    public int onStartCommand(Intent intent,int flags,int startId){
        new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    socket = new Socket("119.23.110.11", 8087);
                    writer = new BufferedWriter( new OutputStreamWriter(
                            socket.getOutputStream(), "utf-8" ) );
                    reader = new BufferedReader( new InputStreamReader(
                            socket.getInputStream(), "utf-8" ) );
                    publishProgress( "@Sucess" ); // 链接成功
                } catch (IOException e) {
                    publishProgress( "@Failure" ); // 链接失败
                    Toast.makeText(MyService.this, "与服务器连接失败", Toast.LENGTH_LONG).show();
                    stopSelf();
                    e.printStackTrace();
                }
                // 监听服务器发来的数据
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {//死循环
                        publishProgress( line );
                    }
                } catch (IOException e) {
                    Toast.makeText(MyService.this, "接收数据失败！",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(String... values){
                switch (values[0]) {
                    case "@Sucess":
                        Toast.makeText(MyService.this, "建立连接成功！",
                                Toast.LENGTH_SHORT).show();
                        setnumback();
                        // listAdapter.add("建立连接成功！");
                        break;
                    case "@Failure":
                        Toast.makeText(MyService.this, "建立连接失败！",
                                Toast.LENGTH_SHORT).show();
                        // listAdapter.add("建立连接失败！");
                        break;
                    default:
//                        Toast.makeText(mainactivity.this, "收到数据！",
//                                Toast.LENGTH_SHORT).show();
                        //  listAdapter.add("别人说：" + values[0]);
                        //String msghead = mainactivity.msghead;c
                       // valuebuff = values[0];
//                        if (manager != null) {
//                            manager.notify(0, notification);
//                        }
                        if(values[0].contains("t"))
                        {
                            valueT = values[0];
                            valueT=valueT.substring(1,valueT.length());
                            Message message = new Message();
                            message.what = MainActivity.UPDATE_T;
                            MainActivity.hander.sendMessage(message);
                        }
                        if(values[0].contains("voc"))
                        {
                            valueV = values[0];
                            valueV=valueV.substring(3,valueV.length());
                            Message message = new Message();
                            message.what = MainActivity.UPDATE_V;
                            MainActivity.hander.sendMessage(message);
                        }
                break;
                }
            }
        }.execute();
        return super.onStartCommand(intent,flags,startId);
    }
    @Override//服务销毁时调用
    public void onDestroy(){
        super.onDestroy();
        try {
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
      return mBinder;
    }
}
