package com.example.Rzesny.shoppinglistapp.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.Rzesny.shoppinglistapp.R;

public class NotificationService extends Service {

    private static int notificationId = 2137;
    private static int requestCode = 0;
    public String shopName;
    public String typeOfTransition;

    public void startNotificationListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                showNotification();
                stopSelf();
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        shopName="";
        typeOfTransition="";
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.hasExtra("ShopName")){
            shopName = intent.getExtras().getString("ShopName");
        }
        if(intent.hasExtra("TransitionType")){
            typeOfTransition = intent.getExtras().getString("TransitionType");
        }

        startNotificationListener();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NotificationService","ServiceDestroyed");
    }

    public void showNotification()
    {
        String title = "";
        String contentText = "";
        if(typeOfTransition.equals("ENTER")){
            title = getResources().getString(R.string.Welcome);
            contentText = getResources().getString(R.string.SubtitleWelcome);
        }
        if(typeOfTransition.equals("EXIT")){
            title = getResources().getString(R.string.Goodbye);
            contentText = getResources().getString(R.string.SubtitleGoodbye);
        }

        Intent activityIntent = new Intent(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, ++requestCode, activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.ChannelID))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title + " " + shopName)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(++notificationId, builder.build());
    }
}
