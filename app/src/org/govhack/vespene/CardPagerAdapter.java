package org.govhack.vespene;

import java.util.List;

import org.govhack.vespene.ImageFetcher.ImageUpdater;
import org.govhack.vespene.atlas.LatLng;
import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.util.Lists;
import org.govhack.vespene.util.Preconditions;
import org.govhack.vespene.util.Util;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.hardware.GeomagneticField;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CardPagerAdapter extends BaseAdapter {
  private static final String TAG = "CardPagerAdapter";
  final Typeface tfReg;
  final Typeface tfThin;


  private final MainActivity activity;
  private final Favourites favourites;
  private final ImageFetcher images;
  private List<Product> products = Lists.newArrayList();
  private AttractionDetailFragment activeDetailFragment = null;

  public CardPagerAdapter(MainActivity activity, Favourites favourites, ImageFetcher images) {
    this.activity = activity;
    this.favourites = favourites;
    this.images = images;

    tfReg = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Medium.ttf");
    tfThin = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Thin.ttf");
  }

  @Override
  public int getCount() {
    return products.size();
  }

  @Override
  public Object getItem(int position) {
      return null;
  }

  @Override
  public long getItemId(int position) {
      return 0;
  }

  public void setData(List<Product> products) {

    Log.w("DAN", "setData " + products.size());
    this.products = Preconditions.checkNotNull(products);
    notifyDataSetChanged();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    Log.w("DAN", "getView " + position);
//    // TODO: reuse convertView where possible, check if this code is ok
//    if (convertView != null) {  // if it's not recycled, initialize some attributes
//      return populateView((ViewGroup) convertView, products.get(position));
//    }

    return inflateCard(parent, products.get(position));
  }

  private ViewGroup inflateCard(ViewGroup parent, Product product) {
    ViewGroup cardView =
        (ViewGroup) activity.getLayoutInflater().inflate(R.layout.card, parent, false);

    return populateView(cardView, product);
  }

  private ViewGroup populateView(ViewGroup cardView, final Product product) {
    cardView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        AttractionDetailFragment detailFragment = new AttractionDetailFragment();
        activeDetailFragment = detailFragment;
        detailFragment.setFavourites(favourites);

        detailFragment.setProduct(product);
        activity.getFragmentManager().beginTransaction()
        	.add(android.R.id.content, detailFragment)
        	.hide(activity.getFragmentManager().findFragmentById(R.id.fragment_gallery))
        	.addToBackStack("cards")
        	.commit();
        Instrumentation.t("card-touched", "product", product.id, "distance", product.locationKms,
            "category", product.categoryId.name());
      }
    });

    final ImageView fav = (ImageView) cardView.findViewById(R.id.favourite_image);
    favourites.registerFavouriteToggle(fav, product.header);

    TextView titleText = (TextView) cardView.findViewById(R.id.card_title);
    titleText.setTypeface(tfReg);
    titleText.setTextSize(20.0f);
    titleText.setMaxLines(1);
    titleText.setText(product.name);

    FrameLayout thumbnailHolder = (FrameLayout) cardView.findViewById(R.id.thumbnail_holder);
    ImageView thumbnail = new ImageView(activity);
//    clearImage(thumbnail); // clear to prevent old junk from appearing
    if (product.imageUrl != null) {
      images.fetchImage(product.imageUrl, new ImageUpdater(thumbnail));
    }
    thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
    thumbnailHolder.addView(thumbnail);

    ImageView map = (ImageView) cardView.findViewById(R.id.map_preview);
//    clearImage(map);
    images.fetchImage(mapPreviewUrl(product.location), new ImageUpdater(map));

    TextView addressText = (TextView) cardView.findViewById(R.id.card_address);
    addressText.setTypeface(tfThin);
    addressText.setMaxLines(1);
//    addressText.setTextSize(16.0f);
    String address = product.address.address;
    addressText.setText(address);

    TextView distanceText = (TextView) cardView.findViewById(R.id.distance_text);
    distanceText.setTypeface(tfThin);
    updateLocation(40.0, product.locationKms * 1000, cardView);

    TextView description =  (TextView) cardView.findViewById(R.id.description_text);
    description.setTypeface(tfThin);
    description.setTextSize(14.0f);
    description.setMaxLines(7);
    description.setText(product.description);

    return cardView;
  }

  private void clearImage(ImageView view) {
    // TODO: figure out how to do this properly
    view.setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
  }

  public void updateLocation(double bearingDegrees, double distanceMs, View cardView) {
	    TextView distanceText = (TextView) cardView.findViewById(R.id.distance_text);
	    distanceText.setTypeface(tfThin);
	    distanceText.setText(Util.formatDistance(distanceMs));

	    ImageView compass = (ImageView) cardView.findViewById(R.id.compass);
	    compass.setRotation((float)bearingDegrees);
  }
  
  public void updateDetailLocation(LatLng loc, float bearing) {
    if (activeDetailFragment != null) {
    	activeDetailFragment.updateLocation(loc, bearing);
    }
  }

  static String mapPreviewUrl(LatLng at) {
    String loc = at.toAtlasString();
    return "http://maps.googleapis.com/maps/api/staticmap?center=" + loc
        + "&zoom=15&size=180x180&maptype=roadmap&markers=color:blue%7Clabel:X%7C" + loc
        + "&sensor=false";
  }
}