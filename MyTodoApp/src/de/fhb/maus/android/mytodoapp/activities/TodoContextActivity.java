package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

public class TodoContextActivity extends Activity {

	private Todo todo;
	private ViewHolder holder = new ViewHolder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_context);
		// get the id of todo that was send to the activity via intent
		Intent intent = getIntent();
		long id = intent.getLongExtra("todo", -1);

		// fill the holder with the view elements
		holder.todoname = (EditText) findViewById(R.id.todo_edit_name);
		holder.tododesc = (EditText) findViewById(R.id.todo_edit_desc);
		holder.isDone = (CheckBox) findViewById(R.id.isDone_context);
		holder.isImportant = (CheckBox) findViewById(R.id.isImportant_context);
		holder.datetime = (TextView) findViewById(R.id.datetime);

		// check if the todo exists and then fill the view elements or create
		// new and set default values
		if (id != -1) {
			MySQLiteHelper db = new MySQLiteHelper(this);
			todo = db.getTodo(id);
			db.close();
			holder.todoname.setText(todo.getName());
			holder.tododesc.setText(todo.getDescription());
			holder.isDone.setChecked(todo.isDone());
			holder.isImportant.setChecked(todo.isImportant());
			holder.datetime.setText(todo.getMaturityDateAsString());
		} else {
			todo = new Todo();
			// Set current time as default
			todo.setMaturityDate(System.currentTimeMillis());
			holder.datetime.setText(todo.getMaturityDateAsString());
			todo.setId(-1);
			// set Button text to cancel when creating a Todo
			Button deleteBtn = (Button) findViewById(R.id.delete_button);
			deleteBtn.setText("cancel");
		}
	}

	public void saveTodoItem(View v) {
		MySQLiteHelper db = new MySQLiteHelper(v.getContext());

		// Build a Todo for persist
		todo.setName(holder.todoname.getText().toString());
		todo.setDescription(holder.tododesc.getText().toString());
		todo.setDone(holder.isDone.isChecked());
		todo.setImportant(holder.isImportant.isChecked());
		todo.setMaturityDateFromString(holder.datetime.getText().toString());

		// decide if update or add (todo exists or not)
		if (todo.getId() != -1) {
			db.updateTodo(todo);
		} else {
			db.addTodo(todo);
		}

		db.close();

		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	public void deleteTodoItem(View v) {

		// only delete a todo if it exists
		if (todo.getId() != -1) {
			MySQLiteHelper db = new MySQLiteHelper(v.getContext());
			db.deleteTodo(todo);
			db.close();
		}

		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	// used like the ViewHolder pattern from ListAdapter (for easier use)
	static class ViewHolder {
		private EditText todoname;
		private EditText tododesc;
		private CheckBox isDone;
		private CheckBox isImportant;
		private TextView datetime;
	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
