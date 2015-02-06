package de.fhb.maus.android.mytodoapp.activities;

import java.util.ArrayList;
import java.util.Collections;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.ContextContactArrayAdapter;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.data.ContactsAccessor;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;
import de.fhb.maus.android.mytodoapp.fragments.ContactPickerDialogFragment;
import de.fhb.maus.android.mytodoapp.fragments.ContactPickerDialogFragment.AddRemoveContactsDialogListener;

/**
 * Bearbeiten der Details eines Todos
 * 
 * @author Sebastian Kindt, Daniel Weis
 *
 */
public class TodoContextActivity extends Activity implements AddRemoveContactsDialogListener{

	private Todo todo;
	private EditText todoname;
	private EditText tododesc;
	private CheckBox isDone;
	private CheckBox isImportant;
	private TextView datetime;
	private TextView locationName;
	private Button delcancel;
	private ListView contactsList;
	
	private ContactsAccessor conAcc;
	private ContextContactArrayAdapter contactAdapter;
	private ArrayList<Contact> contacts;
	private ArrayList<Contact> allContactsList;

	private MySQLiteHelper db;


	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_context);
		
		// ID des Todos aus Intent holen
		Intent intent = getIntent();
		long id = intent.getLongExtra("todo", -1);

		db = new MySQLiteHelper(context);

		// Holder mit View-Elementen fuellen
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
		locationName = (TextView) findViewById(R.id.location_name);
		
		delcancel = (Button) findViewById(R.id.delete_button);

		// Ueberpruefe, ob Todo existiert, ansonsten erstelle neues mit Default Werten
		if (id != -1) {
			todo = db.getTodo(id);
			db.close();
			todoname.setText(todo.getName());
			tododesc.setText(todo.getDescription());
			isDone.setChecked(todo.isDone());
			isImportant.setChecked(todo.isImportant());
			datetime.setText(todo.getMaturityDateAsString());
			locationName.setText(todo.getLocationName());
		} else {
			todo = new Todo();
			// Aktuelle Zeit als Default
			todo.setMaturityDate(System.currentTimeMillis());
			datetime.setText(todo.getMaturityDateAsString());
			todo.setId(-1);
			// Button Text "Cancel" anstatt "Delete" beim Erstellen eines neuen Todos
			delcancel.setText("Cancel");
		}
		

		contactsList = (ListView) findViewById(R.id.context_contacts_list);
		
		conAcc = new ContactsAccessor(this, getContentResolver());
		
		contacts = new ArrayList<Contact>();
		
		// Kontakte aus DB holen, falls bereits bestehendes Todo
		if (id != -1){
			MySQLiteHelper db = new MySQLiteHelper(this);
			ArrayList<Long> contactIds = db.getContactsFromTodo(id);
			for(long contactId : contactIds){
				contacts.add(conAcc.readContact(contactId));
			}
			Collections.sort(contacts);
		}
		

		contactAdapter = new ContextContactArrayAdapter(this, contacts, todo);
		contactsList.setAdapter(contactAdapter);
		

		
	}

	/**
	 * Liest die Werte des Todos aus den Feldern aus und speichert sie in die
	 * Datenbank
	 * 
	 * @param v
	 */
	public void saveTodoItem(View v) {

		fillTodoItem();

		// Neues oder existierendes Todo?
		if (todo.getId() != -1) {
			db.updateTodo(todo);
		} else {
			// Todo hinzufuegen und ID holen, wird für Speicherung der Kontakte gebraucht
			todo.setId(db.addTodo(todo));
		}
		
		// Anstatt Abgleich der Kontakte Löschen und Wiedereinfügen
		db.deleteTodoContacts(todo.getId());
		for (Contact c : contacts){
			db.addContact(todo.getId(), c.getId());
		}

		db.close();

		// Beendet Context Aktivitaet und geht zur Overview Aktivitaet zurueck
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
	
	public void fillTodoItem(){
		// Todo-Objekt aus eingegebenen Daten erstellen
		todo.setName(todoname.getText().toString());
		todo.setDescription(tododesc.getText().toString());
		todo.setDone(isDone.isChecked());
		todo.setImportant(isImportant.isChecked());
		todo.setMaturityDateFromString(datetime.getText().toString());
		todo.setLocationName(locationName.getText().toString());
	}

	/**
	 * Listener fuer den Delete oder Cancel Button bricht Erstellen eines Todos
	 * ab oder loescht ein bestehendes
	 * 
	 * @param v
	 */
	public void deleteTodoItem(View v) {
		// Wenn Cancel Button, also das Todo noch nicht persistiert wurde dann
		// breche einfach die Bearbeitung ab und gehe zur vorigen Aktivitaet
		if (delcancel.getText().toString().equals("Cancel")) {
			startActivity(new Intent(context, TodoOverviewActivity.class));
		} else {
			// Sonst Loesche Todo aus der Datenbank

			// Alert, ob wirklich geloescht werden soll
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			alertDialogBuilder.setTitle("Delete Todo");

			alertDialogBuilder
					.setMessage("Do you want to delete this Todo?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Todo nur loeschen, falls es existiert
									if (todo.getId() != -1) {
										db.deleteTodoContacts(todo.getId());
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
									// Nichts tun und Dialogbox schliessen
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	/**
	 * Aufruf der Aktivitaet zum Einstellen des Datums
	 * 
	 * @param v
	 */
	public void editDateTime(View v) {
		Intent intent = new Intent(context, DateTimeActivity.class);
		intent.putExtra("time", todo.getMaturityDate());
		startActivityForResult(intent, 1);
	}
	
	public void editLocation(View v) {
		Intent intent = new Intent(context, LocationActivity.class);
		intent.putExtra("locationName", todo.getLocationName());
		intent.putExtra("locationLatitude", todo.getLocationLatitude());
		intent.putExtra("locationLongitude", todo.getLocationLongitude());
		startActivityForResult(intent, 1);
		//startActivity(new Intent(this, LocationActivity.class));
	}

	/**
	 * Persistieren der Aenderung des Datums
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				todo.setMaturityDate(data.getLongExtra("time",
						System.currentTimeMillis()));
				todo.setLocationName(data.getStringExtra("locationName"));
				todo.setLocationCoordinates(new LatLng(
						data.getDoubleExtra("locationLatitude", 52.41192),
						data.getDoubleExtra("locationLongitude", 12.53126)));
				
				datetime.setText(todo.getMaturityDateAsString());
				locationName.setText(todo.getLocationName());
				
			}
		}
	}
	
	/**
	 * Oeffnet ContactPicker zum Hinzufuegen und Entfernen von Kontakten zu einem Todo
	 * @param v
	 */
	public void addRemoveContacts(View v) {
		ContactPickerDialogFragment contactPicker = new ContactPickerDialogFragment();
		ContactsAccessor conAcc = new ContactsAccessor(this, getContentResolver());
		allContactsList = (ArrayList<Contact>) conAcc.readAllContactsNames();
		//Alphabetische Sortierung der Kontakte
		Collections.sort(allContactsList);
		ArrayList<String> namesList = new ArrayList<String>();
		ArrayList<String> checkedList = new ArrayList<String>();
		for (Contact c : allContactsList){
			namesList.add(c.getName());
			boolean check = false;
			//Falls der Kontakt dem aktuellen Todo schon zugeordnet ist, markiere ihn
			for (Contact c2 : contacts){
				if(c.getId() == c2.getId()){
					check = true;
				}
			}
			checkedList.add(check ? "1" : "0");
		}
		Bundle names = new Bundle();
		names.putStringArrayList("names", namesList);
		names.putStringArrayList("checked", checkedList);
		contactPicker.setArguments(names);
		contactPicker.show(getFragmentManager(), "contact_picker");
	}

	/**
	 * Callback-Methode, die durch Klick auf Okay im ContactPicker aufgerufen wird und dessen Daten uebernimmt
	 */
	@Override
	public void onFinishAddRemoveContactsDialog(ArrayList<Integer> added, ArrayList<Integer> removed) {
		Log.i("Contacts","Added Contacts: " + added);
		Log.i("Contacts","Removed Contacts: " + removed);
		
		for(Integer a : added){
			contacts.add(conAcc.readContact(allContactsList.get(a).getId()));
		}
		for(Integer r : removed){
			contacts.remove(conAcc.readContact(allContactsList.get(r).getId()));
		}
		Collections.sort(contacts);
		contactAdapter.notifyDataSetChanged();
		
	}
}
