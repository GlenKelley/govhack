package org.govhack.vespene;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.govhack.vespene.R;

public class AlarmReceiver extends BroadcastReceiver {

  private static int NOTIFICATION_ID = 1;
  
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.i("AlarmReceiver", "alarm");
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String key = context.getResources().getString(R.string.pref_notify_key);
    if (prefs.getBoolean(key, false)) {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
      builder.setContentTitle("New cuteness");
      builder.setContentText("Squeee!");
      builder.setSmallIcon(R.drawable.icon);
      builder.setAutoCancel(true);
      
      Intent launchIntent = new Intent(context, MainActivity.class);
      launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      launchIntent.setAction(MainActivity.ACTION_LATEST);
      PendingIntent pi = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
      builder.setContentIntent(pi);
      
      NotificationManager notificationManager = (NotificationManager) context
              .getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
  }
}
