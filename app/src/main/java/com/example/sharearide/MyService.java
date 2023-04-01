package com.example.sharearide;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.my.kizzyrpc.KizzyRPC;
import com.my.kizzyrpc.model.Activity;
import com.my.kizzyrpc.model.Assets;
import com.my.kizzyrpc.model.Metadata;
import com.my.kizzyrpc.model.Timestamps;

import java.util.Arrays;

public class MyService extends Service {
    private KizzyRPC kizzyRPC;
    private String token;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        token = intent.getStringExtra("Token");
        kizzyRPC = new KizzyRPC(token);
        if (intent.getAction().equals("START_ACTIVITY_ACTION"))
        {
            kizzyRPC.setActivity(
                    new Activity(
                            "hi",
                            "STATEEE",
                            "details",
                            0,
                            new Timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 500000),
                            new Assets(
                                    "mp:attachments/973256105515974676/983674644823412798/unknown.png",
                                    "mp:attachments/973256105515974676/983674644823412798/unknown.png",
                                    "large-image-text",
                                    "small-image-text"
                            ),
                            Arrays.asList("Button1", "Button2"),
                            new Metadata(Arrays.asList(
                                    "https://youtu.be/1yVm_M1sKBE",
                                    "https://youtu.be/1yVm_M1sKBE"
                            )),
                            "962990036020756480"
                    ), "online", System.currentTimeMillis());
        }
        notification();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        kizzyRPC.closeRPC();
        super.onDestroy();
    }

    private void notification() {
        NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(new NotificationChannel("Discord RPC", "Background Service", NotificationManager.IMPORTANCE_LOW));
        Notification.Builder builder = new Notification.Builder(this, "Discord RPC");
        //builder.setSmallIcon(1500001);
        builder.setContentText("Rpc Running");
        builder.setUsesChronometer(true);
        this.startForeground(11234, builder.build());
    }
}
