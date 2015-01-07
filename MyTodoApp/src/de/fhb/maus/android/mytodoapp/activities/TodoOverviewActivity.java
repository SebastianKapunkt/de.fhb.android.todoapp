package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.comperator.TodoDateComperator;
import de.fhb.maus.android.mytodoapp.comperator.TodoImportantComperator;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

public class TodoOverviewActivity extends Activity {

	public static final String SELECTED_TODO = "todo";
	private ListView list;
	private TodoArrayAdapter adapter;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.todo_overview);

		MySQLiteHelper db = new MySQLiteHelper(this);

		// db.addTodo(new Todo("one", "one", false, true, 1231232));
		// db.addTodo(new Todo("two", "two", true, true, 1231232));
		// db.addTodo(new Todo("three", "three", true, false, 1231232));
		// db.addTodo(new Todo("four", "four", true, true, 1231232));

		Log.d("Todos", db.getAllTodos().toString());
		db.close();

		// get the ListView
		list = (ListView) findViewById(R.id.todo_list);

		// get custom adapter
		adapter = new TodoArrayAdapter(this, db.getAllTodos());

		// set comperator to adapter
		adapter.sort(new TodoImportantComperator());

		// set the custome adapter to the list View
		list.setAdapter(adapter);

	}

	// listen to the create Button
	public void createNewTodo(View view) {
		startActivity(new Intent(this, TodoContextActivity.class));
	}

	public void sortByDate(View view) {
		adapter.sort(new TodoDateComperator());
		list.setAdapter(adapter);
	}

	public void sortByImportance(View view) {
		adapter.sort(new TodoImportantComperator());
		list.setAdapter(adapter);
	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		startActivity(new Intent(this, LoginActivity.class));
	}
}
