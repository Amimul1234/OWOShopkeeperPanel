package com.shopKPR.pushNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.RemoteMessage;
import com.shopKPR.R;
import com.shopKPR.welcomeScreens.SplashScreenActivity;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ExecutionException;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Log.d("msg", "onMessageReceived: " + remoteMessage.getNotification().getImageUrl());

        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";


        NotificationCompat.Builder builder = null;

        try {

            builder = new  NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setLargeIcon(Glide.
                            with(this)
                            .asBitmap()
                            .load(remoteMessage.getNotification().getImageUrl().toString())
                            .into(200, 200). // Width and height
                            get())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(0, builder.build());

    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
