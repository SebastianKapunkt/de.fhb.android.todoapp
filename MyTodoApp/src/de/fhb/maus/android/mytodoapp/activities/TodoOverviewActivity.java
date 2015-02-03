package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.comperator.TodoDateComperator;
import de.fhb.maus.android.mytodoapp.comperator.TodoImportantComperator;
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
	MySQLiteHelper db = new MySQLiteHelper(this);
	private boolean sortByDate;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.todo_overview);

		// get the ListView
		list = (ListView) findViewById(R.id.todo_list);

		// get custom adapter
		adapter = new TodoArrayAdapter(this, db.getAllTodos());

		// set comperator to adapter
		adapter.sort(new TodoImportantComperator());
		sortByDate = false;

		// set the custome adapter to the list View
		list.setAdapter(adapter);

	}

	// listen to the create Button
	public void createNewTodo(View view) {
		startActivity(new Intent(this, TodoContextActivity.class));
	}

	/**
	 * Setzt den Entprechenden Comperator auf den Adapter fuer das Sortieren
	 * nach Datum
	 * 
	 * @param view
	 */
	public void sortByDate(View view) {
		adapter.sort(new TodoDateComperator());
		list.setAdapter(adapter);
		sortByDate = true;
	}

	/**
	 * Setzt den Entsprechenden Comperator auf den Adapter fuer das Sortieren
	 * nach Wichtigkeit
	 * 
	 * @param view
	 */
	public void sortByImportance(View view) {
		adapter.sort(new TodoImportantComperator());
		list.setAdapter(adapter);
		sortByDate = false;
	}

	public void updateList(MenuItem item) {
		if (sortByDate) {
			adapter.sort(new TodoDateComperator());
			list.setAdapter(adapter);
		} else {
			adapter.sort(new TodoImportantComperator());
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
	
	// listen to the toggle button
	public void toggleOverview(View view) {
		startActivity(new Intent(this, TodoLocationOverviewActivity.class));
	}
}
