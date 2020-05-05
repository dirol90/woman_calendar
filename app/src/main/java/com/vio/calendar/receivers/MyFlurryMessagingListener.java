package com.vio.calendar.receivers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.flurry.android.marketing.messaging.FlurryMessaging;
import com.flurry.android.marketing.messaging.FlurryMessagingListener;
import com.flurry.android.marketing.messaging.notification.FlurryMessage;
import com.google.firebase.messaging.RemoteMessage;
import com.vio.calendar.ui.main.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

public class MyFlurryMessagingListener implements FlurryMessagingListener {

    final static String LOG_TAG = MyFlurryMessagingListener.class.getCanonicalName();
    Context context;

    public MyFlurryMessagingListener(Context context) {
        this.context = context;
    }

    @Override

    public boolean onNotificationReceived(FlurryMessage flurryMessage) {
        boolean handled = false;
        if (FlurryMessaging.isAppInForeground()) {
            handled = true;
        }
        return handled;

    }

    @Override

    public boolean onNotificationClicked(FlurryMessage flurryMessage) {
        final Intent deeplinkIntent = new Intent(context, MainActivity.class);
        deeplinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        deeplinkIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        deeplinkIntent.putExtra("push", "isOpenNewsFragment");
        FlurryMessaging.addFlurryMessageToIntentExtras(deeplinkIntent, flurryMessage);
        if (deeplinkIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(deeplinkIntent);
            return true;
        }

        return false;

    }

    @Override

    public void onNotificationCancelled(FlurryMessage flurryMessage) {
        Log.d(LOG_TAG, "Notification cancelled!");
    }

    @Override

    public void onTokenRefresh(String refreshedToken) {
        Log.d(LOG_TAG, "Token refreshed - " + refreshedToken);
    }

    @Override

    public void onNonFlurryNotificationReceived(Object nonFlurryMessage) {
        if (nonFlurryMessage instanceof RemoteMessage) {
            RemoteMessage firebaseMessage = (RemoteMessage) nonFlurryMessage;
        }

    }

}