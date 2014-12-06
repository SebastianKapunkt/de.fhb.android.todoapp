package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

public class TodoOverviewActivity extends Activity {

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.todo_overview);

		ListView listview = (ListView) findViewById(R.id.todo_list);

		MySQLiteHelper db = new MySQLiteHelper(this);
		
		db.addTodo(new Todo("one","one",false,true,1231232));
		db.addTodo(new Todo("two","two",true,true,1231232));
		db.addTodo(new Todo("three","three",true,false,1231232));
		db.addTodo(new Todo("four","four",true,true,1231232));
		
		Log.d("Todos", db.getAllTodos().toString());

		TodoArrayAdapter adapter = new TodoArrayAdapter(this, db.getAllTodos());

		db.close();
		
		listview.setAdapter(adapter);

	}

	public void createNewTodo(View view) {
		Intent intent = new Intent(this, TodoContextActivity.class);

		startActivity(intent);
	}
}
