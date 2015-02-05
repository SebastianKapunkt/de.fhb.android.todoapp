package de.fhb.maus.android.mytodoapp.activities;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

/**
 * Uebersicht ueber die Orte, an denen es Todos gibt
 * 
 * @author Per Militzer
 *
 */
public class TodoLocationOverviewActivity extends FragmentActivity 
								implements OnMapReadyCallback, OnMarkerClickListener {
	
	private MarkerOptions mOptions;
	private GoogleMap map;
	private MySQLiteHelper db = new MySQLiteHelper(this);
	private ArrayList<Todo> todoList;
	private ArrayList<Marker> markerList;
	private Todo markerTodo;
	
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
		markerList = new ArrayList<Marker>();
		
		// Erstelle fuer jedes Todo einen Marker auf der Map
		for(Todo todo : todoList) {
			mOptions = new MarkerOptions()
						.position(todo.getLocationCoordinates())
						.draggable(false)
						.title(todo.getLocationName());
			
			markerList.add(map.addMarker(mOptions));
			markerTodo = todo;
			map.setOnMarkerClickListener(this);
		}
	}
	
	/**
	 * Wechselt in das Kontextmenue der dem Marker entsprechenden Todo
	 * 
	 * @param marker
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		Intent intent = new Intent(this, TodoContextActivity.class);
		intent.putExtra("todo", markerTodo.getId());
		startActivity(intent);
		return true;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void onMapReady(GoogleMap map) {		
		
	}
	
	/**
	 * Wechselt auf die Todoansicht
	 * 
	 * @param item
	 */
	public void gotoTodoOverview(View view) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
