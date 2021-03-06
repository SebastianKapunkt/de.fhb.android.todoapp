package de.fhb.maus.android.mytodoapp.activities;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import de.fhb.maus.android.mytodoapp.R;

/**
 * Aktivitaet fuer die Bearbeitung des Orts eines Todos
 * 
 * @author Per Militzer
 *
 */
public class LocationActivity extends FragmentActivity 
								implements OnMapReadyCallback, OnMarkerDragListener {

	private MarkerOptions mOptions;
	private Marker marker;
	private GoogleMap map;
	private EditText locInfo;
	private static final String TAG = "LocationAddress";
	private String geoCodeResult = "";
	private String locationName = "";
	private boolean markerWasDragged;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		Intent intent = getIntent();
		
		// Eingabefeld fuer die Location
		locInfo = (EditText)findViewById(R.id.locinfo);
		if(intent.hasExtra("locationName")) {
			locInfo.setText(intent.getStringExtra("locationName"));
			locationName = locInfo.getText().toString();
		}
		locInfo.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				locationName = locInfo.getText().toString();
			}
		});
		
		// Konfiguriere Marker-Optionen
		if(intent.hasExtra("locationLatitude") && intent.hasExtra("locationLongitude")) {
			mOptions = new MarkerOptions()
				.position(new LatLng(
						intent.getDoubleExtra("locationLatitude", 52.41192),
						intent.getDoubleExtra("locationLongitude", 12.53126)))
				.draggable(true);
		}
		else {
			mOptions = new MarkerOptions()
				.position(new LatLng(102.41192, 102.53126))
				.draggable(true);
		}
		
		// Erstelle Map
		FragmentManager fManager = getFragmentManager();
		MapFragment mapFragment = (MapFragment) fManager.findFragmentById(R.id.map);
		map = mapFragment.getMap();
		mapFragment.getMapAsync(this);
		
		// Konfiguriere Map
		markerWasDragged = false;
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL)
						.compassEnabled(false)
						.rotateGesturesEnabled(false)
						.tiltGesturesEnabled(false);
		MapFragment.newInstance(options);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);		
		map.setOnMarkerDragListener(this);
		
		// Fuege der Map einen Marker hinzu
		marker = map.addMarker(mOptions);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void onMapReady(GoogleMap map) {	
		
	}
	
	/**
	 * Fuegt dem Intent Koordinaten und Location-Name hinzu und beendet die Activity.
	 * 
	 * @param v
	 */
	public void saveLocationChange(View v) {
		Intent returnIntent = new Intent();
		
		if(markerWasDragged) {
			geoCode();
		}
		
		returnIntent.putExtra("locationName", locationName);
		returnIntent.putExtra("locationLatitude", marker.getPosition().latitude);
		returnIntent.putExtra("locationLongitude", marker.getPosition().longitude);
		setResult(RESULT_OK, returnIntent);
		finish();
	}
	
	/**
	 * Bestimmt aus den Koordinaten des Markers die entsprechende Adresse
	 */
	public void geoCode() {
		Geocoder gCoder = new Geocoder(this);
		
		try {
			List<Address> addressList = gCoder.getFromLocation(
										marker.getPosition().latitude, 
										marker.getPosition().longitude,
										1);
			if(addressList != null && addressList.size() > 0) {
				Address address = addressList.get(0);
				StringBuilder sb = new StringBuilder();
				
				if(address.getLocality() == null)  {
					sb.append(address.getCountryName());
				}
				else {
					if(address.getLocality().contains("Unnamed")) {
						String str = address.getLocality();
						String[] strparts = str.split("Unnamed ");
						sb.append(strparts[1]);
					}
					else {
						sb.append(address.getLocality());
					}
				}
				
				geoCodeResult = sb.toString();
				locationName = geoCodeResult;
			}
		}
		catch(IOException e) {
			Log.e(TAG, "Unable to connect to Geocoder", e);
		}
	}
	
	/**
	 * Bricht das Bearbeiten der Location ab.
	 * 
	 * @param v
	 */
	public void cancelLocationChange(View v) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void onMarkerDrag(Marker marker) {
	}

	/**
	 * Schreibt den Adresse ins Textfeld, nachdem der Marker bewegt wurde
	 * 
	 * @param marker
	 */
	@Override
	public void onMarkerDragEnd(Marker marker) {
		geoCode();
		locInfo.setText(locationName);
		markerWasDragged = true;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void onMarkerDragStart(Marker marker) {
	}
}
