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

	@Override
	public void onSensorChanged(SensorEvent event) {		
		float[] I = new float[9];
		float[] R = new float[9];
		float[] orientation = new float[3];
		if (SensorManager.getRotationMatrix(R, I, acc, mag)) {
			float inclination = SensorManager.getInclination(I);
			SensorManager.getOrientation(R, orientation);
//			Log.w("glen", String.format("mag %f %f %f", mag[0], mag[1], mag[2]));
//			Log.w("glen", String.format("acc %f %f %f", acc[0], acc[1], acc[2]));
//			Log.w("glen", String.format("I %f %f %f %f %f %f %f %f %f", I[0], I[1], I[2], I[3], I[4], I[5], I[6], I[7], I[8]));
//			Log.w("glen", String.format("R %f %f %f %f %f %f %f %f %f", R[0], R[1], R[2], R[3], R[4], R[5], R[6], R[7], R[8]));
//			Log.w("glen", String.format("inclination %f", inclination));
//			Log.w("glen", String.format("orient %f %f %f", orientation[0] / Math.PI * 180, orientation[1] / Math.PI * 180, orientation[2] / Math.PI * 180));
			bearing = orientation[0];
		} else {
			Log.w("glen", "no inclination");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		locationListener.onLocationChanged(location, bearing);
	}

}
