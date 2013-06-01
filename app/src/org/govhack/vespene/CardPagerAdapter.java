package org.govhack.vespene;

import java.util.List;

import org.govhack.vespene.ImageFetcher.ImageUpdater;
import org.govhack.vespene.atlas.LatLng;
import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.util.Lists;
import org.govhack.vespene.util.Preconditions;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CardPagerAdapter extends BaseAdapter {
  private static final String TAG = "CardPagerAdapter";
  final Typeface tfBold;
  final Typeface tfThin;


  private final MainActivity activity;
  private final ImageFetcher images;
  private List<Product> products = Lists.newArrayList();
  
  public CardPagerAdapter(MainActivity activity, ImageFetcher images) {
    this.activity = activity;
    this.images = images;
    
    tfBold = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Bold.ttf");
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
    // TODO: reuse convertView where possible, check if this code is ok
//    if (convertView != null) {  // if it's not recycled, initialize some attributes
//      return populateView((ViewGroup) convertView);
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
        Toast.makeText(activity, "Clicked on Attraction", Toast.LENGTH_SHORT).show();
        AttractionDetailFragment detailFragment = new AttractionDetailFragment();
        
        detailFragment.setProduct(product);
        activity.getFragmentManager().beginTransaction()
        	.add(android.R.id.content, detailFragment)
        	.hide(activity.getFragmentManager().findFragmentById(R.id.fragment_gallery))
        	.addToBackStack("cards")
        	.commit();
      }
    });

    TextView titleText = (TextView) cardView.findViewById(R.id.card_title);
    titleText.setTypeface(tfBold);
    titleText.setTextSize(20.0f);
    titleText.setMaxLines(1);
    titleText.setText(product.name);
    
    FrameLayout thumbnailHolder = (FrameLayout) cardView.findViewById(R.id.thumbnail_holder);
    ImageView thumbnail = new ImageView(activity);
    if (product.imageUrl != null) {
      images.fetchImage(product.imageUrl, new ImageUpdater(thumbnail));
    }
    thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
    thumbnailHolder.addView(thumbnail);
    
    ImageView map = (ImageView) cardView.findViewById(R.id.map_preview);
    images.fetchImage(mapPreviewUrl(product.location), new ImageUpdater(map));
    
    TextView addressText = (TextView) cardView.findViewById(R.id.card_address);
    addressText.setTypeface(tfThin);
//    addressText.setTextSize(16.0f);
    addressText.setText("123 Clarence St, Circular Quay");
    
    TextView distanceText = (TextView) cardView.findViewById(R.id.distance_text);
    distanceText.setTypeface(tfThin);
    distanceText.setText( ((int) (product.locationKms * 1000)) + "m");
    
    TextView description =  (TextView) cardView.findViewById(R.id.description_text);
    description.setTypeface(tfThin);
    description.setTextSize(14.0f);
    description.setMaxLines(7);
    description.setText(product.description);
    
    return cardView;
  }
  
  static String mapPreviewUrl(LatLng at) {
    String loc = at.toAtlasString();
    return "http://maps.googleapis.com/maps/api/staticmap?center=" + loc 
        + "&zoom=15&size=180x180&maptype=roadmap&markers=color:blue%7Clabel:X%7C" + loc 
        + "&sensor=false";
  }
}