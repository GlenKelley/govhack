package org.govhack.vespene;

import java.util.Arrays;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationTracker implements ConnectionCallbacks, OnConnectionFailedListener, SensorEventListener, LocationListener {

	private boolean connected = false;
	private LocationClient client = null;
	private LocationRequest request;
	private MainActivity locationListener;
	
    private final SensorManager sensorManager;
	final float[] mag = new float[3];
	final float[] acc = new float[3];
	float bearing = 0;
	
	public LocationTracker(Context context, MainActivity locationListener) {
		sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

		sensorManager.registerListener(new SensorEventListener() {
				@Override
				public void onSensorChanged(SensorEvent event) {
					for (int i = 0; i < event.values.length; ++i) {
						mag[i] = event.values[i];
					}
					LocationTracker.this.onSensorChanged(null);
				}
				
				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
				}
			}, 
			sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL);
		
		sensorManager.registerListener(new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				for (int i = 0; i < event.values.length; ++i) {
					acc[i] = event.values[i];
				}
				LocationTracker.this.onSensorChanged(null);
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		}, 
		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL);
		
		request = LocationRequest.create();
	    request.setInterval(100);
	    request.setFastestInterval(100);
//	    request.setSmallestDisplacement(50); //50 meters
	    
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
	    client.requestLocationUpdates(request, this);
	}

	@Override
	public void onDisconnected() {
		connected = false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	long lastSent = 0;
	float[] I = new float[9];
	float[] R = new float[9];
	float[] orientation = new float[3];
	@Override
	public void onSensorChanged(SensorEvent event) {	
		long now = System.currentTimeMillis();
		if (now > lastSent + 600) {	
			if (SensorManager.getRotationMatrix(R, I, acc, mag)) {
				SensorManager.getOrientation(R, orientation);
				bearing = (float)((orientation[0] / Math.PI + 1) * 180);
				locationListener.onBearingChanged(bearing);
				lastSent = now;
			} else {
				Log.w("glen", "no inclination");
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		locationListener.onLocationChanged(location, bearing);
	}

}
