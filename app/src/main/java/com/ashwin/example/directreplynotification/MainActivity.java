package com.ashwin.example.directreplynotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(Constants.DEBUG_LOG, "Build version: " + Build.VERSION.SDK_INT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onNewIntent(getIntent());
    }

    public void notify(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("Direct Reply Notification")
                    .setContentText("Give us a quick feedback");

            String replyLabel = "Type here...";

            // Remote input
            RemoteInput remoteInput = new RemoteInput.Builder(Constants.DIRECT_REPLY)
                    .setLabel(replyLabel)
                    .build();

            // Direct reply pending intent
            Intent replyIntent = new Intent(this, ReceiverActivity.class);
            replyIntent.setAction(Constants.DIRECT_REPLY);
            replyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent directReplyPendingIntent = PendingIntent.getActivity(this, Constants.REQUEST_CODE, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Notification action with remote input
            NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(android.R.drawable.sym_action_chat, Constants.DIRECT_REPLY, directReplyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .setAllowGeneratedReplies(true)
                    .build();

            builder.addAction(replyAction);

            // Dismiss pending intent
            Intent dismissIntent = new Intent(this, ReceiverActivity.class);
            dismissIntent.setAction(Constants.DISMISS);
            dismissIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent dismissPendingIntent = PendingIntent.getActivity(getBaseContext(), Constants.REQUEST_CODE, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, Constants.DISMISS, dismissPendingIntent);
            builder.setDeleteIntent(dismissPendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(Constants.CHANNEL_ID);
            }
            notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());
        } else {
            Toast.makeText(getBaseContext(), "Direct reply is not supported below N.", Toast.LENGTH_LONG).show();
        }
    }
}
