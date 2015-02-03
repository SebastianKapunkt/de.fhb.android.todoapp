package de.fhb.maus.android.mytodoapp.activities;

import java.io.IOException;
import java.util.ArrayList;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

/**
 * Uebersicht ueber die Orte, an denen es Todos gibt
 * 
 * @author Ferdinand Flamenco
 *
 */
public class TodoLocationOverviewActivity extends FragmentActivity 
								implements OnMapReadyCallback, OnMarkerClickListener {
	
	private MarkerOptions mOptions;
	private GoogleMap map;
	MySQLiteHelper db = new MySQLiteHelper(this);
	private ArrayList<Todo> todoList;
	private ArrayList<Marker> markerList;
	String locationName = "";
	Intent markerIntent = new Intent(this, TodoContextActivity.class);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_overview);
		
		// Erstelle Map
		FragmentManager fManager = getFragmentManager();
		MapFragment mapFragment = (MapFragment) fManager.findFragmentById(R.id.map);
		map = mapFragment.getMap();
		mapFragment.getMapAsync(this);
		
		// Konfiguriere Map
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL)
						.compassEnabled(false)
						.rotateGesturesEnabled(false)
						.tiltGesturesEnabled(false);
		MapFragment.newInstance(options);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		// Konfiguriere Marker-Optionen und fuege der Map Marker hinzu
		todoList = db.getAllTodos();
		System.out.println("ANZAHL LOCATIONS: " + todoList.size());
		markerList = new ArrayList<Marker>();
		
		for(Todo todo : todoList) {
			mOptions = new MarkerOptions()
						.position(todo.getLocationCoordinates())
						.draggable(false)
						.title(todo.getLocationName());
			
			markerList.add(map.addMarker(mOptions));
			
			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					
				}
			});
		}
	}
	
	
	@Override
	public void onMapReady(GoogleMap map) {		
		//marker = map.addMarker(mOptions);
	}
	
	// listen to the toggle button
	public void toggleOverview(View view) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}
}
