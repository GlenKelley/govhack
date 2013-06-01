package org.govhack.vespene;

import org.govhack.vespene.atlas.Atlas;
import org.govhack.vespene.atlas.LatLng;
import org.govhack.vespene.atlas.Search;
import org.govhack.vespene.util.AsyncUrlFetcher;
import org.joda.time.DateMidnight;
import org.joda.time.Days;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

public class MainActivity extends Activity implements OnInitListener, LocationListener {

  public static final String ACTION_LATEST = "latest";
  public static final int ALARM_CODE = 192837;

  private static final DateMidnight BEGINNING = new DateMidnight(2013, 4, 14);
//  private static final String MP_API_TOKEN = "b84f696d81a182f5d327547dfa382648";

  private static final String TAG = "Main";

//  private MixpanelAPI mp;

  private Atlas atlas = new Atlas(new AsyncUrlFetcher());
  private ProductList products = new ProductList(atlas);
  private TextToSpeech tts = null;
  private LocationTracker locationTracker = null;
  private Favourites favourites = new Favourites();

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
    getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    getActionBar().setHomeButtonEnabled(true);
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
    CardPagerAdapter cardAdapter = new CardPagerAdapter(this, favourites, images);
    getCardsFragment().setAdapter(cardAdapter);

    //  maybe just make this a method that binds them together instead of a ctor...
    new SearchController(getApplicationContext(), products, cardAdapter);


    Log.d(TAG, "onStart");
    super.onStart();
    track("app-start");
    products.doSearch(new Search(LatLng.SYDNEY_CBD));

    locationTracker = new LocationTracker(this, this);

//	tts.setLanguage(Locale.US);
  }

  @Override
  public void onLocationChanged(Location location) {
	 LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
	 products.doSearch(new Search(latLng));
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
    
    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
    searchView.setOnSearchClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.e("Oleg", "onClick in search");
		}
	});
    
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
      case R.id.menu_search:
//    	PopupWindow searchPopup = new PopupWindow(this);
//    	  
//    	EditText searchBox = new EditText(this);
//    	searchBox.setTextSize(14.0f);
//    	  
//    	LinearLayout popupLayout = new LinearLayout(this);
//    	popupLayout.addView(searchBox, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//    	popupLayout.setPadding(30, 30, 30, 30);
//    	
//    	searchPopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_background));
//    	searchPopup.setWidth((int) getResources().getDimension(R.dimen.search_width));
//    	searchPopup.setHeight((int) getResources().getDimension(R.dimen.search_height));
//    	searchPopup.setFocusable(true);
//    	searchPopup.setContentView(popupLayout);
//    	searchPopup.showAtLocation(findViewById(R.id.menu_search), Gravity.TOP, 0, 
//    			(int) getResources().getDimension(R.dimen.search_vertical_offset));
    	  
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
