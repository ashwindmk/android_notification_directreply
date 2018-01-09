package com.ashwin.example.directreplynotification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by ashwin on 05/01/18.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static void logIntent(String tag, Intent intent) {
        Log.w(Constants.DEBUG_LOGGING, tag + " > " + TAG + " > logIntent() > intent: " + intent.toString());
        logIntentExtras(tag, intent);
    }

    public static void logIntentExtras(String tag, Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String data = "";
                for (String key : bundle.keySet()) {
                    if (!data.isEmpty()) {
                        data += ", ";
                    }
                    data += key + ": " + bundle.get(key);
                }
                Log.w(Constants.DEBUG_LOGGING, tag + " > " + TAG + " > logIntentExtras() > extras: " + data);
            } else {
                Log.w(Constants.DEBUG_LOGGING, tag + " > " + TAG + " > logIntentExtras() > extras: null");
            }
        } else {
            Log.w(Constants.DEBUG_LOGGING, tag + " > " + TAG + " > logIntentExtras() > intent: null");
        }
    }

}
