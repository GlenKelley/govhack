package org.govhack.vespene;

import java.util.concurrent.atomic.AtomicReference;

import org.govhack.vespene.util.Preconditions;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class Instrumentation {

  private static final String TAG = "Instrumentation";
  private static final String MP_API_TOKEN = "326a339341dcd3a95f5b46b41ade014b";
  private static AtomicReference<Instrumentation> INSTANCE = new AtomicReference<Instrumentation>();
  
  /** Initialises instrumentation. Call once before instance() */
  public static Instrumentation initialise(Context context) {
    if (INSTANCE.get() == null) {
      INSTANCE.set(new Instrumentation(context));
    }
    return instance();
  }
  
  public static Instrumentation instance() {
    final Instrumentation i = INSTANCE.get();
    Preconditions.checkNotNull(i, "Instrumentation not yet initialised");
    return i;
  }
  
  public static void t(String event, Object...objects) {
    instance().track(event, objects);
  }
  
  
  private final MixpanelAPI mp;

  private Instrumentation(Context context) {
    mp = MixpanelAPI.getInstance(context, MP_API_TOKEN);
    mp.identify(Installation.id(context));
    if (Installation.wasNewInstallation()) {
      track("new-install");
    }
  }
  
  public void track(String event, Object...objects) {
    JSONObject j = new JSONObject();
    for (int i = 0; i < objects.length - 1; i += 2) {
      try {
        j.put((String)objects[i], objects[i+1]);
      } catch (JSONException e) {
        Log.e(TAG, "Exception serialising event property " + objects[i], e);
      }
    }
    if (BuildConfig.DEBUG) {
      Log.d(TAG, event + ": " + j);
    } else {
      mp.track(event, j);
    }
  }
  
  public void flush() {
    mp.flush();
  }
}
