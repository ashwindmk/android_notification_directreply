package com.ashwin.example.directreplynotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(Constants.DEBUG_LOGGING, TAG + " > onCreate() > build version: " + Build.VERSION.SDK_INT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearExistingNotifications();

        onNewIntent(getIntent());
    }

    private void clearExistingNotifications() {
        Log.w(Constants.DEBUG_LOGGING, TAG + " > clearExistingNotifications()");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(Constants.NOTIFICATION_ID);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Utils.logIntentExtras(TAG, intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int notificationId = bundle.getInt(Constants.KEY_NOTIFICATION_ID,  -1);
                if (notificationId == Constants.NOTIFICATION_ID) {
                    ((TextView) findViewById(R.id.replyTextView)).setText("Notification Dismissed");
                }
            }
        }
    }

    public void notify(View view) {
        Log.w(Constants.DEBUG_LOGGING, TAG + " > notify()");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            // Notification body
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("Direct Reply Notification")
                    .setContentText("Give us a quick feedback");

            String replyLabel = "Type here";

            // Remote input
            RemoteInput remoteInput = new RemoteInput.Builder(Constants.KEY_DIRECT_REPLY)
                    .setLabel(replyLabel)
                    .build();

            // Direct reply pending intent
            Intent directReplyIntent = new Intent(this, ReceiverActivity.class);
            directReplyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent directReplyPendingIntent = PendingIntent.getActivity(this, 0, directReplyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Notification action with remote input
            NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(android.R.drawable.sym_action_chat, "REPLY", directReplyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .setAllowGeneratedReplies(true)
                    .build();

            builder.addAction(replyAction);

            // Dismiss pending intent
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.KEY_NOTIFICATION_ID, Constants.NOTIFICATION_ID);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent dismissIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "DISMISS", dismissIntent);

        } else {

            // Notification body
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("Direct Reply Not Supported!")
                    .setContentText("You need a device with Android N");

            // Pending intent
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.KEY_NOTIFICATION_ID, Constants.NOTIFICATION_ID);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());

        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());

        }
    }

}
