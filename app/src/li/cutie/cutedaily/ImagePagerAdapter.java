package li.cutie.cutedaily;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImagePagerAdapter extends PagerAdapter {
  
  private static class Page {
    ViewGroup frame;    
  }
  
  private static final String TAG = "ImagePagerAdapter";
  private static final int PAGES = 9;
  
  private static final int[] SADS = {
    R.drawable.sad1, R.drawable.sad2, R.drawable.sad3, R.drawable.sad4 
  };
  
  private final MainActivity activity;
  private final ImageFetcher fetcher;
  private final List<Page> pages; // The last page is today's
  private int today;
  
  public ImagePagerAdapter(MainActivity activity, ImageFetcher fetcher, int today) {
    this.activity = activity;
    this.fetcher = fetcher;
    this.today = today;
    this.pages = new ArrayList<Page>(PAGES);

    for (int i = 0; i < PAGES; ++i) {
      pages.add(null);
    }
  }
  
  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Log.d(TAG, "Instantiate pos " + position);
    
    ViewGroup layout = loadLayout(container, position);
    Page p = buildPage(layout, position);
    container.addView(layout, RelativeLayout.LayoutParams.MATCH_PARENT);

    return p;    
  }
  
  private ViewGroup loadLayout(ViewGroup container, int position) {
    int resource = R.layout.image_frame;
    if (isFirstPosition(position)) {
      resource = R.layout.first_page;
    } else if (isLastPosition(position)) {
      resource = R.layout.last_page;
    }
    ViewGroup layout = (ViewGroup) activity.getLayoutInflater().inflate(resource, container, false);
    
    if (isFirstPosition(position)) {
      Button b = (Button) layout.findViewById(R.id.thanksButton);
      b.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          activity.track("say-thanks-clicked");
          String appPackageName = activity.getPackageName();
          Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName));
          marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
          activity.startActivity(marketIntent);
        }
      });
    } else if (isPhotoPosition(position)) {
      ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
      int daysAgo = pages.size() - position - 1;
      String token = tagForDay(today - daysAgo);
      if (imageView != null) {
        fetcher.fetchImage(imageView, token);
      }      
    } else if (isLastPosition(position)) {
      ImageView imageView = (ImageView) layout.findViewById(R.id.sadImage);
      int sadResource = SADS[new Random().nextInt(SADS.length)];
      imageView.setImageResource(sadResource);
    }
    
    return layout;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    Log.d(TAG, "Destroy pos " + position);
    Page page = (Page) object;
    if (pages.get(position) == page) {
      pages.set(position, null);      
    } else {
      // Destroyed already in refresh.
      assert pages.get(position) == null;
    }
    container.removeView(page.frame);
  }
  
  @Override
  public boolean isViewFromObject(View view, Object obj) {
    Page page = (Page) obj;
    return view == page.frame;
  }
  
  @Override
  public int getCount() {
//    Log.d(TAG, "Get count: " + pages.size());
    return pages.size();
  }
  
  @Override
  public int getItemPosition (Object object) {
    // Don't attempt to preserve positions across data set changes.
    return POSITION_NONE;
  }
  
  public void refresh(int newDay) {
    if (today != newDay) {
      today = newDay;
      Collections.fill(pages, null);
      notifyDataSetChanged();
    }
  }

  // For testing
  public void advance() {
    refresh(today + 1);
  }
  
  private boolean isPhotoPosition(int position) {
    return position != 0 && position != PAGES - 1;
  }

  private boolean isFirstPosition(int position) {
    return position == 0;
  }
  
  private boolean isLastPosition(int position) {
    return position == PAGES - 1;
  }

  private Page buildPage(ViewGroup layout, int position) {
    assert pages.get(position) == null;
    Page p = new Page();
    pages.set(position, p);
    p.frame = layout;
    return p;
  }
  
  private String tagForDay(int day) {
    String tag = String.format("%08d", day);
    Log.d(TAG, "Date: " + tag);
    return tag;
    
//    try {
//      MessageDigest digester = MessageDigest.getInstance("SHA1");
//      digester.update(dateString.getBytes());
//      byte[] hashBytes = digester.digest();
//      String token = Base64.encodeToString(hashBytes, Base64.URL_SAFE | Base64.NO_WRAP);
//      Log.d(TAG, token);
//      return token;
//    } catch (NoSuchAlgorithmException e) {
//      throw new RuntimeException(e);
//    }
  }
  
//private Page instantiateProgrammatically(ViewGroup container, int position) {
//RelativeLayout frame = new RelativeLayout(container.getContext());
//// frame.setBackgroundColor(Color.RED);
//
//ProgressBar spinner = new ProgressBar(container.getContext());
//RelativeLayout.LayoutParams spinnerParams = new RelativeLayout.LayoutParams(
//        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//spinnerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//frame.addView(spinner, spinnerParams);
//
//ImageView v = new ImageView(container.getContext());
//// v.setBackgroundColor(Color.BLUE);
//RelativeLayout.LayoutParams imgLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
//        LayoutParams.MATCH_PARENT);
//imgLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
//frame.addView(v, imgLayout);
//
//Page p = pages.get(position);
//p.frame = frame;
//p.imageView = v;
//return p;
//}
}