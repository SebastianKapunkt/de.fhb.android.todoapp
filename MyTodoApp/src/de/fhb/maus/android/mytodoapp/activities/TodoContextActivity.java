package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

public class TodoContextActivity extends Activity{

	private Todo todo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_context);
		Intent intent = getIntent();
		long id = intent.getLongExtra("todo", -1);
		Log.d("Get Intent", id+"");
		
		if(id != -1){
			MySQLiteHelper db = new MySQLiteHelper(this);
			todo = db.getTodo(id);
			db.close();
			
			EditText todoname = (EditText) findViewById(R.id.todo_edit_name);
			todoname.setText(todo.getName());
			EditText tododesc = (EditText) findViewById(R.id.todo_edit_desc);
			tododesc.setText(todo.getDescription());
			CheckBox isDone = (CheckBox) findViewById(R.id.isDone_context);
			isDone.setChecked(todo.isDone());
			CheckBox isImportant = (CheckBox) findViewById(R.id.isImportant_context);
			isImportant.setChecked(todo.isImportant());
			TextView datetime = (TextView) findViewById(R.id.datetime);
			datetime.setText(todo.convertTime(todo.getMaturityDate()));
		}
	}
	
}
