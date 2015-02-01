package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;
/**
 * Bearbeiten der Details eines Todos
 * 
 * @author Sebastian Kindt
 *
 */
public class TodoContextActivity extends Activity {

	private Todo todo;
	private EditText todoname;
	private EditText tododesc;
	private CheckBox isDone;
	private CheckBox isImportant;
	private TextView datetime;
	private Button delcancel;
	MySQLiteHelper db;

	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_context);
		// get the id of todo that was send to the activity via intent
		Intent intent = getIntent();
		long id = intent.getLongExtra("todo", -1);

		db = new MySQLiteHelper(context);

		// fill the holder with the view elements
		todoname = (EditText) findViewById(R.id.todo_edit_name);
		todoname.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {
				for (int i = s.length(); i > 0; i--) {

					if (s.subSequence(i - 1, i).toString().equals("\n"))
						s.replace(i - 1, i, "");

				}
			}
		});

		tododesc = (EditText) findViewById(R.id.todo_edit_desc);
		isDone = (CheckBox) findViewById(R.id.isDone_context);
		isImportant = (CheckBox) findViewById(R.id.isImportant_context);
		datetime = (TextView) findViewById(R.id.datetime);
		delcancel = (Button) findViewById(R.id.delete_button);

		// check if the todo exists and then fill the view elements or create
		// new and set default values
		if (id != -1) {
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
			delcancel.setText("Cancel");
		}
	}

	/**
	 * Liest die Werte des Todos aus den Feldern aus und speichert sie in die
	 * Datenbank
	 * 
	 * @param v
	 */
	public void saveTodoItem(View v) {
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

		// Beendet Context Aktivitaet und geht zur Overview Aktivitaet zurueck
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	/**
	 * Listener fuer den Delete oder Cancel Button Bricht erstellen eines Todos
	 * ab oder loescht ein besthendes
	 * 
	 * @param v
	 */
	public void deleteTodoItem(View v) {
		// Wenn Canel button ist, also das Todo noch nicht persitiert wurde dann
		// breche einfach die Bearbeitung ab und gehe zur fohrigen Aktivitaet
		if (delcancel.getText().toString().equals("Cancel")) {
			startActivity(new Intent(context, TodoOverviewActivity.class));
		} else {
			// Sonst Loesche Todo aus der Datenbank

			// build a Alert Dialog
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			// set title
			alertDialogBuilder.setTitle("Delete Todo");

			// set dialog message
			alertDialogBuilder
					.setMessage("Do you want to delete this Todo?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// only delete a todo if it exists
									if (todo.getId() != -1) {
										db.deleteTodo(todo);
										db.close();
										startActivity(new Intent(context,
												TodoOverviewActivity.class));
									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
	}

	/**
	 * Aufruf der Aktivitaet zum einstellen des Datums
	 * 
	 * @param v
	 */
	public void editDateTime(View v) {
		Intent intent = new Intent(context, DateTimeActivity.class);
		intent.putExtra("time", todo.getMaturityDate());
		startActivityForResult(intent, 1);
	}

	/**
	 * Persitieren der Aenderung des Datums
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				todo.setMaturityDate(data.getLongExtra("time",
						System.currentTimeMillis()));
				datetime.setText(todo.getMaturityDateAsString());
			}
		}
	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
