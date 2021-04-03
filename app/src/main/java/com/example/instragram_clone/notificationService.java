package com.example.instragram_clone;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import java.util.Objects;

import static android.content.ContentValues.TAG;
import static android.util.Log.*;
import static android.widget.Toast.*;


public class notificationService extends Service {


 //   private final class handler extends Handler{
//        public  handler(Looper looper){
//            super(looper);
//
//
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            if(Objects.equals(msg.getData().getString("hork"), "work")){
//                int i;
//                for(i = 0;i<=300000; i++){
//                    d(TAG, "onStartCommand: "+i);
//                    //makeText(getApplicationContext(),Integer.toString(i), Toast.LENGTH_SHORT).show();
//                }stopSelf();
//            }
//        }
//    }
   Looper looper;
    Handler handler;
    HandlerThread thread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//         Message msg=handler.obtainMessage();
//        Bundle bundle=new Bundle();
//        bundle.putString("hork","work");
//        msg.setData(bundle);
//        handler.sendMessage(msg);
         Runnable runnable=new Runnable() {
            @Override
            public void run() {
                int i;
                for(i = 0;i<=300000; i++){
                    d(TAG, "onStartCommand: "+i);
                    //makeText(getApplicationContext(),Integer.toString(i), Toast.LENGTH_SHORT).show();
                    }stopSelf();
            }
        };
        Thread tthread=new Thread(runnable);
        tthread.start();


        //Toast.makeText(getApplicationContext(),"onStartcommand",Toast.LENGTH_SHORT).show();
        return START_REDELIVER_INTENT;

    }

    @Override
    public void onCreate() {

//        thread = new HandlerThread("hellow", Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//        looper=thread.getLooper();
////        Handler=new handler(looper);
//        handler=new Handler(looper){
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                if(Objects.equals(msg.getData().getString("hork"), "work")) {
//                    int i;
//                    for (i = 0; i <= 300000; i++) {
//                        d(TAG, "onStartCommand: " + i);
//                        //makeText(getApplicationContext(),Integer.toString(i), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//        };



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        d(TAG, "onDestroy: service");
    }
}
