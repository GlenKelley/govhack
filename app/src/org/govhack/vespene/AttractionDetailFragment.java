package org.govhack.vespene;

import org.govhack.vespene.atlas.Product;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AttractionDetailFragment extends Fragment {
  
  private static final int[] SADS = {
    R.drawable.sad1, R.drawable.sad2, R.drawable.sad3, R.drawable.sad4 
  };

  private Product product = null;
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.attraction_detail_layout, container, false);
  }
  
  @Override
  public void onStart() {
    super.onStart(); 
    ActionBar actionBar = getActivity().getActionBar();
    actionBar.setTitle(product.name);
    actionBar.setDisplayHomeAsUpEnabled(true);
    
    getTv(R.id.detail_place_name).setText(product.name);
    getTv(R.id.detail_place_address).setText(product.address.address);
    getTv(R.id.detail_description).setText(product.description);
    
    if (product.phoneNumber != null) {
      getV(R.id.detail_layout_phone).setVisibility(View.VISIBLE);
      TextView tv = getTv(R.id.detail_phone);
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
