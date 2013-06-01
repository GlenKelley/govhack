package org.govhack.vespene;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationTracker implements ConnectionCallbacks, OnConnectionFailedListener {

	private boolean connected = false;
	private LocationClient client = null;
	private LocationRequest request;
	private LocationListener locationListener;
	
	public LocationTracker(Context context, LocationListener locationListener) {
		request = LocationRequest.create();
	    request.setInterval(10000); //30 seconds
	    request.setFastestInterval(5000); //10 seconds
	    
		this.locationListener = locationListener;
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	    if (ConnectionResult.SUCCESS == resultCode) {
			client = new LocationClient(context, this, this);
	    	client.connect();
	    } else {
	    	//TODO: handle case of play services disabled
	    }
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		connected = false;
	}

	@Override
	public void onConnected(Bundle bundle) {
		connected = true;
	    client.requestLocationUpdates(request, locationListener);
	}

	@Override
	public void onDisconnected() {
		connected = false;
	}

}
