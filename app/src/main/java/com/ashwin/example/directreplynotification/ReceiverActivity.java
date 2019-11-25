package com.ashwin.example.directreplynotification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ReceiverActivity extends AppCompatActivity {
    private TextView mReplyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        mReplyTextView = (TextView) findViewById(R.id.replyTextView);

        Intent intent = getIntent();
        if (intent != null) {
            Utils.logIntent(intent);
            String action = intent.getAction();
            if (Constants.DIRECT_REPLY.equals(action)) {
                handleDirectReply(intent);
            } else if (Constants.DISMISS.equals(action)) {
                mReplyTextView.setText("Dismissed");
                dismiss(this);
            }
        }
    }

    private void handleDirectReply(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String reply = String.valueOf(remoteInput.getCharSequence(Constants.DIRECT_REPLY));
            Log.w(Constants.DEBUG_LOG,"Reply: " + reply);
            mReplyTextView.setText(reply);

            dismiss(this);
        } else {
            mReplyTextView.setText("No Reply Received!");
        }
    }

    private void dismiss(Context context) {
        Log.w(Constants.DEBUG_LOG, "Dismissing notification");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(Constants.NOTIFICATION_ID);
    }
}
