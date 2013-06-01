package org.govhack.vespene;

import org.govhack.vespene.atlas.Product;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AttractionDetailFragment extends Fragment {
  
  private static final int[] SADS = {
    R.drawable.sad1, R.drawable.sad2, R.drawable.sad3, R.drawable.sad4 
  };

  private Product product = null;
  private Typeface tfBold;
  private Typeface tfReg;
  private Typeface tfThin;
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tfReg = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");
    tfThin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.attraction_detail_layout, container, false);
  }
  
  @Override
  public void onDestroyView() {
    try {
      FragmentTransaction transaction = getFragmentManager()
          .beginTransaction();
      transaction.remove(mapFragment());
      transaction.commit();
    } catch (RuntimeException e) {
    }
    super.onDestroyView();
  }
  
  @Override
  public void onStart() {
    super.onStart();  
    final LinearLayout sectionContainer = (LinearLayout)getV(R.id.detail_sections);
    final LinearLayout detailContainer = (LinearLayout)getV(R.id.detail_container);
    final LinearLayout mapContainer = (LinearLayout)getV(R.id.detail_map_container);
    ActionBar actionBar = getActivity().getActionBar();
    actionBar.setTitle(product.name);
    actionBar.setDisplayHomeAsUpEnabled(true);
    
    getTv(R.id.detail_place_name).setText(product.name);
    getTv(R.id.detail_place_name).setTypeface(tfReg);

    getTv(R.id.detail_place_address).setText(product.address.fullAddress);
    getTv(R.id.detail_place_address).setTypeface(tfThin);

    getTv(R.id.detail_description).setText(product.description);
    getTv(R.id.detail_description).setTypeface(tfThin);

    getTv(R.id.detail_phone_label).setTypeface(tfBold);
    getTv(R.id.detail_email_label).setTypeface(tfBold);
    
    getTv(R.id.detail_place_address).setText(product.address.address);
    getTv(R.id.detail_place_address).setTypeface(tfThin);
    
    if (product.phoneNumber != null) {
      getV(R.id.detail_layout_phone).setVisibility(View.VISIBLE);
      TextView tv = getTv(R.id.detail_phone);
      tv.setTypeface(tfThin);
      tv.setText(callLink(product.phoneNumber));
      tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          String uri = "tel:" + product.phoneNumber.trim() ;
          Intent intent = new Intent(Intent.ACTION_DIAL);
          intent.setData(Uri.parse(uri));
          startActivity(intent);
        }
      });
    } else {
      getV(R.id.detail_layout_phone).setVisibility(View.GONE);
    }
    
    if (product.emailAddress != null) {
      getV(R.id.detail_layout_email).setVisibility(View.VISIBLE);
      TextView tv = getTv(R.id.detail_email);
      tv.setText(emailLink(product.emailAddress));
      tv.setTypeface(tfThin);
      tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto",product.emailAddress, null));
          startActivity(intent);
        }
      });
    } else {
      getV(R.id.detail_layout_email).setVisibility(View.GONE);
    }
    
    GoogleMap map = mapFragment().getMap();
    mapFragment().getView().setBackgroundColor(Color.WHITE);
    LatLng ll = product.location.realLatLng();
    map.addMarker(new MarkerOptions().position(ll).title(product.name));
    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(ll, 16);
    map.moveCamera(cu);
    map.setMyLocationEnabled(true);
    map.setOnMapClickListener(new OnMapClickListener() {
      @Override
      public void onMapClick(LatLng arg0) {
        expandMap();
      }
    });
    mapContainer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        expandMap();
      }
    });
    detailContainer.setOnClickListener(new OnClickListener() {
      @Override 
      public void onClick(View v) {
        shrinkMap();
      }
    });
    
    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
      @Override
      public void onInfoWindowClick(Marker m) {
        Intent intent = new Intent(Intent.ACTION_VIEW, 
            Uri.parse("http://maps.google.com/maps?daddr=" + m.getPosition().latitude
                + "," + m.getPosition().longitude + "&dirflg=w"));
        intent.setComponent(new ComponentName("com.google.android.apps.maps", 
            "com.google.android.maps.MapsActivity"));
        startActivity(intent);
      }
    });
  }

  private MapFragment mapFragment() {
    return (MapFragment) getFragmentManager().findFragmentById(R.id.detail_map);
  }
  
  private void expandMap() {
    LinearLayout sectionContainer = (LinearLayout)getV(R.id.detail_sections);
    LinearLayout mapContainer = (LinearLayout)getV(R.id.detail_map_container);
    LinearLayout.LayoutParams lp = 
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4);
    mapContainer.setLayoutParams(lp);
    sectionContainer.setWeightSum(6);
    sectionContainer.requestLayout();
  }

  private void shrinkMap() {
    LinearLayout sectionContainer = (LinearLayout)getV(R.id.detail_sections);
    LinearLayout mapContainer = (LinearLayout)getV(R.id.detail_map_container);
    LinearLayout.LayoutParams lp = 
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
    mapContainer.setLayoutParams(lp);
    sectionContainer.setWeightSum(3);
    sectionContainer.requestLayout();
  }

  public void setProduct(Product product) {
    this.product = product;
  }
  
  private View getV(int id) {
    return getView().findViewById(id);
  }
  
  private TextView getTv(int id) {
    return (TextView)getV(id);
  }
  
  private Spanned callLink(String number) {
    return Html.fromHtml(String.format("<a href=\"tel:%s\">%s</a>", number, number));
  }
  
  private Spanned emailLink(String addr) {
    return Html.fromHtml(String.format("<a href=\"mailto:%s\">%s</a>", addr, addr));
  }
}
