package org.govhack.vespene;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
  private MainActivity activity;

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences);
      activity = (MainActivity) getActivity();
  }
  
  @Override
  public void onStart() {
    super.onStart();
    activity.getActionBar().hide();
    activity.track("settings-shown");
  }
  
  @Override
  public void onStop() {
    super.onStop();
  }
  
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Object newValue = sharedPreferences.getAll().get(key);
    activity.track("setting-changed", key, newValue);
  }
}
