package com.ashwin.example.directreplynotification;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ReceiverActivity extends AppCompatActivity {

    private static final String TAG = ReceiverActivity.class.getSimpleName();

    private TextView mReplyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        Intent intent = getIntent();

        Utils.logIntent(TAG, intent);

        handleDirectReply(intent);
    }

    private void handleDirectReply(Intent intent) {
        mReplyTextView = (TextView) findViewById(R.id.replyTextView);

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null) {
            String reply = remoteInput.getCharSequence(Constants.KEY_DIRECT_REPLY).toString();

            Log.w(Constants.DEBUG_LOGGING, TAG + " > handleDirectReply() > reply: " + reply);
            mReplyTextView.setText(reply);

            // Update notification
            NotificationCompat.Builder repliedNotificationBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.stat_notify_chat)
                            .setContentText("Direct Reply Received")
                            .setAutoCancel(true);

            PendingIntent dismissIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            repliedNotificationBuilder.setContentIntent(dismissIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(Constants.NOTIFICATION_ID, repliedNotificationBuilder.build());

            // OR simply dismiss the notification
            //notificationManager.cancel(Constants.NOTIFICATION_ID);
        } else {
            mReplyTextView.setText("No Reply Received!");
        }

    }
}
