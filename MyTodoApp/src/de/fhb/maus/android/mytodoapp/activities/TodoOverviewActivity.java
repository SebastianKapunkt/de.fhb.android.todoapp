package de.fhb.maus.android.mytodoapp.activities;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.comparator.TodoDateComparator;
import de.fhb.maus.android.mytodoapp.comparator.TodoImportantComparator;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

/**
 * Ueberblick ueber die Todos
 * 
 * @author Sebastian Kindt
 * 
 */
public class TodoOverviewActivity extends Activity {

	public static final String SELECTED_TODO = "todo";
	private ListView list;
	private TodoArrayAdapter adapter;
	private MySQLiteHelper db = new MySQLiteHelper(this);
	private boolean sortByDate;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.todo_overview);
		
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");

			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			// presumably, not relevant
		}

		// get the ListView
		list = (ListView) findViewById(R.id.todo_list);

		// get custom adapter
		adapter = new TodoArrayAdapter(this, db.getAllTodos());

		// set comparator to adapter
		adapter.sort(new TodoImportantComparator());
		sortByDate = false;

		// set the custom adapter to the list View
		list.setAdapter(adapter);

	}

	// listen to the create Button
	public void createNewTodo(View view) {
		startActivity(new Intent(this, TodoContextActivity.class));
	}
	

	public void gotoTodoByContact(MenuItem item) {
		startActivity(new Intent(this, TodoByContactActivity.class));
	}

	/**
	 * Setzt den Entprechenden Comperator auf den Adapter fuer das Sortieren
	 * nach Datum
	 * 
	 * @param view
	 */

	public void sortByDate(MenuItem item) {
		adapter.sort(new TodoDateComparator());
		list.setAdapter(adapter);
		sortByDate = true;
	}

	/**
	 * Setzt den Entsprechenden Comperator auf den Adapter fuer das Sortieren
	 * nach Wichtigkeit
	 * 
	 * @param item
	 */
	public void sortByImportance(MenuItem item) {
		adapter.sort(new TodoImportantComparator());
		list.setAdapter(adapter);
		sortByDate = false;
	}

	public void updateList(MenuItem item) {
		if (sortByDate) {
			adapter.sort(new TodoDateComparator());
			list.setAdapter(adapter);
		} else {
			adapter.sort(new TodoImportantComparator());
			list.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.todo_overview_menu, menu);
		return true;
	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		startActivity(new Intent(this, LoginActivity.class));
	}
	
	/**
	 * Wechselt auf die Kartenansicht
	 * 
	 * @param item
	 */
	public void gotoLocationOverview(MenuItem item) {
		startActivity(new Intent(this, TodoLocationOverviewActivity.class));
	}
}
