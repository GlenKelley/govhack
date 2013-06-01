package org.govhack.vespene;

import java.io.IOException;
import java.util.List;

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
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
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
  
  private boolean locationOverride = false;
  private LatLng myLastlatLng = null;

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
    
    showWarmWelcome();
//    mp = MixpanelAPI.getInstance(this, MP_API_TOKEN);
//    mp.identify(Installation.id(this));
//    if (Installation.wasNewInstallation()) {
//      track("new-install");
//    }

    track("app-create");
//    tts = new TextToSpeech(this, this);
  }

  private void showWarmWelcome() {
	  View view = getLayoutInflater().inflate(R.layout.warm_welcome, null);
	  if (view == null) {
		  return;
	  }
	  
	  ImageView splashImage = (ImageView) view.findViewById(R.id.splash_image);
	  splashImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
	  
	  Typeface tfReg = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");
	  Typeface tfThin = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
	  
	  TextView topText = (TextView) view.findViewById(R.id.warm_text1);
	  topText.setTypeface(tfThin);
	  topText.setTextSize(12.0f);
	  topText.setText(
	      "Oz Explore helps you uncover the hidden gems of Australia. " +
        "As you explore on foot, bicycle or by car, information about the " +
	      "attractions near you will rise to the top.");
	  
	  TextView bottomText = (TextView) view.findViewById(R.id.warm_text2);
	  bottomText.setTypeface(tfReg);
	  bottomText.setTextSize(14.0f);
	  bottomText.setText(
        "- Tap a card to see more information.\n\n" +
	      "- Search for attractions near other locations.\n");

	  new AlertDialog.Builder(this)
	    .setTitle("Welcome to Oz Explore")
	    .setView(view)
	    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	     .show();
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
    locationTracker = new LocationTracker(this, this);

//	tts.setLanguage(Locale.US);
  }

  @Override
  public void onLocationChanged(Location location) {
	 myLastlatLng = new LatLng(location.getLatitude(), location.getLongitude());
	 if (!locationOverride) {
		 products.doSearch(new Search(myLastlatLng));
	 }
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
    searchView.setQueryHint("Search Location");
    
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextChange(String newText) {
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			Geocoder gc = new Geocoder(MainActivity.this);
			try {
				List<Address> address = gc.getFromLocationName(query, 1);
				if (address.size() > 0) {
					products.doSearch(new Search(new LatLng(address.get(0).getLatitude(), 
							address.get(0).getLongitude())));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			InputMethodManager im = (InputMethodManager) MainActivity.this
		            .getSystemService(Context.INPUT_METHOD_SERVICE);
		    im.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus()
		            .getWindowToken(), 0);
		    locationOverride = true;
		    MainActivity.this.getActionBar().setDisplayHomeAsUpEnabled(true);
			return true;
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
    	if (getFragmentManager().getBackStackEntryCount() < 1) {
    		locationOverride = false;
    		MainActivity.this.getActionBar().setDisplayHomeAsUpEnabled(false);
    		MainActivity.this.getActionBar().setTitle("Oz Explore");
    		if (myLastlatLng != null) {
    			products.doSearch(new Search(myLastlatLng));
    		}
    	} else {
    		getFragmentManager().popBackStack();
    	}
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
