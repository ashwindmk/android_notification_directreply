package com.ashwin.example.directreplynotification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ashwin on 05/01/18.
 */
class Utils {
    static void logIntent(Intent intent) {
        String extras = getExtras(intent);
        String content = String.valueOf(intent) + "\nExtras: " + extras;
        Log.w(Constants.DEBUG_LOG, content);
    }

    private static String getExtras(Intent intent) {
        if (intent == null) {
            return "null";
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return "null";
        }

        String data = "";
        for (String key : bundle.keySet()) {
            if (!data.isEmpty()) {
                data += ", ";
            }
            data += key + ": " + bundle.get(key);
        }
        return "{" + data + "}";
    }
}
