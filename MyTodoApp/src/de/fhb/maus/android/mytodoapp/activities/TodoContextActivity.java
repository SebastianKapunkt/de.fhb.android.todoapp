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
	private EditText todoname;
	private EditText tododesc;
	private CheckBox isDone;
	private CheckBox isImportant;
	private TextView datetime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_context);
		// get the id of todo that was send to the activity via intent
		Intent intent = getIntent();
		long id = intent.getLongExtra("todo", -1);

		// fill the holder with the view elements
		todoname = (EditText) findViewById(R.id.todo_edit_name);
		tododesc = (EditText) findViewById(R.id.todo_edit_desc);
		isDone = (CheckBox) findViewById(R.id.isDone_context);
		isImportant = (CheckBox) findViewById(R.id.isImportant_context);
		datetime = (TextView) findViewById(R.id.datetime);

		// check if the todo exists and then fill the view elements or create
		// new and set default values
		if (id != -1) {
			MySQLiteHelper db = new MySQLiteHelper(this);
			todo = db.getTodo(id);
			db.close();
			todoname.setText(todo.getName());
			tododesc.setText(todo.getDescription());
			isDone.setChecked(todo.isDone());
			isImportant.setChecked(todo.isImportant());
			datetime.setText(todo.getMaturityDateAsString());
		} else {
			todo = new Todo();
			// Set current time as default
			todo.setMaturityDate(System.currentTimeMillis());
			datetime.setText(todo.getMaturityDateAsString());
			todo.setId(-1);
			// set Button text to cancel when creating a Todo
			Button deleteBtn = (Button) findViewById(R.id.delete_button);
			deleteBtn.setText("Cancel");
		}
	}

	public void saveTodoItem(View v) {
		MySQLiteHelper db = new MySQLiteHelper(v.getContext());

		// Build a Todo for persist
		todo.setName(todoname.getText().toString());
		todo.setDescription(tododesc.getText().toString());
		todo.setDone(isDone.isChecked());
		todo.setImportant(isImportant.isChecked());
		todo.setMaturityDateFromString(datetime.getText().toString());

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

	public void editDateTime(View v) {
		startActivityForResult(new Intent(this, DateTimeActivity.class), 1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				datetime.setText(data.getStringExtra("time"));
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	// used like the ViewHolder pattern from ListAdapter (for easier use)
	static class ViewHolder {

	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
