package li.cutie.cutedaily;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class GalleryFragment extends Fragment implements OnMenuVisibilityListener {

  private static final float OVERLAY_OPACITY = 0.7f;

  private static final String TAG = "GalleryFragment";
  
  private MainActivity activity;
  private ImageFetcher fetcher;
  private RelativeLayout root;
  private ViewPager pager;
  private ImagePagerAdapter pagerAdapter;
  private ObjectAnimator overlayAnimator;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    setMenuVisibility(true);
    activity = (MainActivity) getActivity();    
    
    activity.getActionBar().addOnMenuVisibilityListener(this);
  }
 
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    fetcher = new ImageFetcher(getActivity());
    pagerAdapter = new ImagePagerAdapter(activity, fetcher, MainActivity.dayCount());

    root = (RelativeLayout) inflater.inflate(R.layout.gallery, container, false);
    pager = new ViewPager(activity);
    pager.setId(R.id.viewpager);
    pager.setAdapter(pagerAdapter);
    pager.setPageTransformer(false, new ZoomOutPageTransformer());
    pager.setCurrentItem(pagerAdapter.getCount() - 2);
    root.addView(pager);
    
    View overlay = root.findViewById(R.id.overlay);
    overlay.bringToFront();

    pager.setOnPageChangeListener(new OnPageChangeListener() {
      int lastScrollState = ViewPager.SCROLL_STATE_IDLE;
      
      @Override
      public void onPageSelected(int position) {
        if (position == 0) {
          activity.track("first-page-viewed");
        } else if (position == pagerAdapter.getCount() - 1) {
          activity.track("last-page-viewed");
        } else {
          activity.track("page-viewed", "page-number", position);          
        }
      }
      
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }
      
      @Override
      public void onPageScrollStateChanged(int newState) {
        if (newState == ViewPager.SCROLL_STATE_IDLE && 
                lastScrollState == ViewPager.SCROLL_STATE_DRAGGING) {
          // No settling time means the user just tapped.
          showOverlay();
          activity.track("page-tap", "current-page", pager.getCurrentItem());
        }
        lastScrollState = newState;
      }
    });
    
    return root;
  }
  
  @Override
  public void onStart() {
    super.onStart();
    Log.d(TAG, "onStart");
    pagerAdapter.refresh(MainActivity.dayCount());
    showOverlay();
//    pagerAdapter.advance();
  }
  
  @Override
  public void onResume () {
    super.onResume();
    Log.d(TAG, "onResume");
    if (root.hasFocus()) {
      showOverlay();
    }
  }
  
  @Override
  public void onPause () {
    super.onPause();
  }
  
  @Override
  public void onStop() {
    super.onStop();
    Log.d(TAG, "onStop");
  }
  
  @Override
  public void onMenuVisibilityChanged(boolean isVisible) {
    if (isVisible && overlayAnimator != null) {
      overlayAnimator.cancel();
      overlayAnimator = null;
    } else {
      showOverlay(); // Trigger eventual re-hiding.
    }
  }

  public void showLast() {
    pager.setCurrentItem(pagerAdapter.getCount() - 2);
  }
  
  private void showOverlay() {
    if (overlayAnimator != null) {
      overlayAnimator.cancel();
      overlayAnimator = null;
    }    
    
    View leftArrow = root.findViewById(R.id.arrowLeft);
    View rightArrow = root.findViewById(R.id.arrowRight);
    View older = root.findViewById(R.id.older);
    View newer = root.findViewById(R.id.newer);
    
    int moreToLeft = pager.getCurrentItem() > 0 ? View.VISIBLE : View.INVISIBLE;
    int moreToRight = pager.getCurrentItem() < pagerAdapter.getCount() - 2 ? View.VISIBLE : View.INVISIBLE;
    leftArrow.setVisibility(moreToLeft);
    rightArrow.setVisibility(moreToRight);
    older.setVisibility(moreToLeft);
    newer.setVisibility(moreToRight);
    
    View overlay = root.findViewById(R.id.overlay);
    overlay.setAlpha(OVERLAY_OPACITY);
    final ActionBar actionBar = activity.getActionBar();
    if (!actionBar.isShowing()) {
      actionBar.show();      
    }
    overlayAnimator = ObjectAnimator.ofFloat(overlay, "alpha", 0.0f);
    overlayAnimator.setStartDelay(1800);
    overlayAnimator.setDuration(500);
    overlayAnimator.start();
    overlayAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator a) {
        if (a.isRunning()) {
//          Log.d(TAG, "Hiding action bar");
          activity.getActionBar().hide();                  
        }
      }
    });
  }
}
