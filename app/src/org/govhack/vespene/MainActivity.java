package org.govhack.vespene;

import org.govhack.vespene.atlas.Atlas;
import org.govhack.vespene.atlas.LatLng;
import org.govhack.vespene.atlas.Search;
import org.govhack.vespene.util.AsyncUrlFetcher;
import org.joda.time.DateMidnight;
import org.joda.time.Days;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements OnInitListener {  
  
  public static final String ACTION_LATEST = "latest";
  public static final int ALARM_CODE = 192837;
  
  private static final DateMidnight BEGINNING = new DateMidnight(2013, 4, 14);
//  private static final String MP_API_TOKEN = "b84f696d81a182f5d327547dfa382648";
  
  private static final String TAG = "Main";

//  private MixpanelAPI mp;
  
  private Atlas atlas = new Atlas(new AsyncUrlFetcher());
  private ProductList products = new ProductList(atlas);
  private TextToSpeech tts = null;
  
  private ImageFetcher images;
  
  public static int dayCount() {
    DateMidnight today = new DateMidnight();
    return Days.daysBetween(BEGINNING, today).getDays();
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "onCreate");
    images = new ImageFetcher(getApplicationContext());
    setContentView(R.layout.activity_main);
    
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    final FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
      @Override
      public void onBackStackChanged() {
        if (fragmentManager.getBackStackEntryCount() < 1) {
          getActionBar().setDisplayHomeAsUpEnabled(false);
        }
      }
    });
    
//    mp = MixpanelAPI.getInstance(this, MP_API_TOKEN);
//    mp.identify(Installation.id(this));
//    if (Installation.wasNewInstallation()) {
//      track("new-install");
//    }
    
    track("app-create");
    
//    tts = new TextToSpeech(this, this);
  }

  @Override
  protected void onNewIntent(Intent i) {
    Log.i(TAG, "New intent: " + i.getAction());
    if (ACTION_LATEST.equals(i.getAction())) {
      FragmentManager fm = getFragmentManager();
      while (fm.getBackStackEntryCount() > 0) {
        fm.popBackStackImmediate();
      }
      //CardsFragment gallery = (CardsFragment) fm.findFragmentById(R.id.fragment_gallery);
      //gallery.showLast();
      track("app-launch-from-notification");
    }
  }
  
  CardsFragment getCardsFragment() {
    FragmentManager fm = getFragmentManager();
//    while (fm.getBackStackEntryCount() > 0) {
//      fm.popBackStackImmediate();
//    }
    return (CardsFragment) fm.findFragmentById(R.id.fragment_gallery);
    //gallery.showLast();
  }
  
  @Override
  protected void onDestroy() {
    Log.d(TAG, "onDestroy");
    super.onDestroy();
  }
  
  @Override
  protected void onStart() {
    CardPagerAdapter cardAdapter = new CardPagerAdapter(this, images);
    getCardsFragment().setAdapter(cardAdapter);
    
    //  maybe just make this a method that binds them together instead of a ctor...
    new SearchController(getApplicationContext(), products, cardAdapter);
    
    
    Log.d(TAG, "onStart");
    super.onStart();
    track("app-start");
    products.doSearch(new Search(LatLng.SYDNEY_CBD));
//	tts.setLanguage(Locale.US);
  }

  @Override
  protected void onStop() {
    Log.d(TAG, "onStop");
//    mp.flush();
    super.onStop();
  }
    
  @Override
  public void onResume() {
    Log.d(TAG, "onResume");
    super.onResume();
  }
  
  @Override
  protected void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    // Inflate the menu; this adds items to the action bar if it is present.
    Log.d(TAG, "onCreateOptionsMenu");
    getMenuInflater().inflate(R.menu.main, menu);
    track("options-menu-shown");
    return true;
  }
  
  @Override
  public void onInit(int status) {
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        getFragmentManager().popBackStack();
        return true;
      case R.id.action_settings:
        // Display the fragment as the main content.
      SettingsFragment settings = new SettingsFragment();
      PreferenceManager.getDefaultSharedPreferences(this)
          .registerOnSharedPreferenceChangeListener(settings);
      getFragmentManager().beginTransaction()
                .add(android.R.id.content, settings)
                .hide(getFragmentManager().findFragmentById(R.id.fragment_gallery))
                .addToBackStack("prefs")
                .commit();
        return true;
      case R.id.search_test:
        products.doSearch(new Search(LatLng.SYDNEY_CBD));
        return true;
      case R.id.search_test2:
        products.doSearch(new Search(LatLng.CANBERRA));
        return true;
      default:
        return super.onOptionsItemSelected(item);        
    }
  }
  
  void track(String event, Object...objects) {
    JSONObject j = new JSONObject();
    try {
      for (int i = 0; i < objects.length - 1; i += 2) {
        j.put((String)objects[i], objects[i+1]);
      }
    } catch (JSONException e) {
      Log.e(TAG, "Exception in analytics", e);
    }
    if (BuildConfig.DEBUG) {
      Log.d("MP", event + ": " + j);
    } else {
//      mp.track(event, j);            
    }
  }
}
