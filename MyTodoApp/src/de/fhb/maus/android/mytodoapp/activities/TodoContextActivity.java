package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		Intent intent = getIntent();
		long id = intent.getLongExtra("todo", -1);
		Log.d("Get Intent", id + "");

		holder.todoname = (EditText) findViewById(R.id.todo_edit_name);
		holder.tododesc = (EditText) findViewById(R.id.todo_edit_desc);
		holder.isDone = (CheckBox) findViewById(R.id.isDone_context);
		holder.isImportant = (CheckBox) findViewById(R.id.isImportant_context);
		holder.datetime = (TextView) findViewById(R.id.datetime);

		if (id != -1) {
			MySQLiteHelper db = new MySQLiteHelper(this);
			todo = db.getTodo(id);
			db.close();
			holder.todoname.setText(todo.getName());
			holder.tododesc.setText(todo.getDescription());
			holder.isDone.setChecked(todo.isDone());
			holder.isImportant.setChecked(todo.isImportant());
			holder.datetime.setText(todo.convertTime(todo.getMaturityDate()));
		} else {
			todo = new Todo();
			// Set current time as default
			holder.datetime.setText(todo.convertTime(System.currentTimeMillis()));
			todo.setId(-1);
			// set Button text to cancel when creating a Todo
			Button deleteBtn = (Button) findViewById(R.id.delete_button);
			deleteBtn.setText("cancel");
		}
	}

	public void saveTodoItem(View v) {
		MySQLiteHelper db = new MySQLiteHelper(v.getContext());

		todo.setName(holder.todoname.getText().toString());
		todo.setDescription(holder.tododesc.getText().toString());
		todo.setDone(holder.isDone.isChecked());
		todo.setImportant(holder.isImportant.isChecked());
		todo.setMaturityDate(todo.convertTime(holder.datetime.getText()
				.toString()));

		if (todo.getId() != -1) {
			db.updateTodo(todo);
		} else {
			db.addTodo(todo);
		}

		db.close();

		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	public void deleteTodoItem(View v) {

		if (todo.getId() != -1) {
			MySQLiteHelper db = new MySQLiteHelper(v.getContext());
			db.deleteTodo(todo);
			db.close();
		}


		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	static class ViewHolder {
		private EditText todoname;
		private EditText tododesc;
		private CheckBox isDone;
		private CheckBox isImportant;
		private TextView datetime;
	}
	
	public void onBackPressed() {    
	    startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
