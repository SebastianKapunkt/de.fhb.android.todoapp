package de.fhb.maus.android.mytodoapp.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

public class TodoOverviewActivity extends ListActivity {

	public static final String SELECTED_TODO = "todo";

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.todo_overview);

		MySQLiteHelper db = new MySQLiteHelper(this);

		db.addTodo(new Todo("one", "one", false, true, 1231232));
		db.addTodo(new Todo("two", "two", true, true, 1231232));
		db.addTodo(new Todo("three", "three", true, false, 1231232));
		db.addTodo(new Todo("four", "four", true, true, 1231232));

		Log.d("Todos", db.getAllTodos().toString());
		db.close();

		TodoArrayAdapter adapter = new TodoArrayAdapter(this, db.getAllTodos());
		setListAdapter(adapter);

	}

	public void createNewTodo(View view) {
		startActivity(new Intent(this, TodoContextActivity.class));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Todo todo = (Todo) this.getListAdapter().getItem(position);

		Intent intent = new Intent(this, TodoContextActivity.class);
		intent.putExtra(SELECTED_TODO, todo.getId());
		startActivity(intent);

	}
}
