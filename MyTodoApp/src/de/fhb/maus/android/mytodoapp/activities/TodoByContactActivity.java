package de.fhb.maus.android.mytodoapp.activities;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.ContactArrayAdapter;
import de.fhb.maus.android.mytodoapp.comparator.ContactNameComparator;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.data.ContactsAccessor;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;


/**
 * Liste aller Kontakte, denen Todos zugeordnet sind
 * @author Daniel Weis
 *
 */
public class TodoByContactActivity extends Activity {
	
	private ListView list;
	private ContactArrayAdapter adapter;
	MySQLiteHelper db = new MySQLiteHelper(this);
	
	private ArrayList<Contact> contacts;
	private ContactsAccessor conAcc;
	
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.todo_by_contact);
		
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");

			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			// presumably, not relevant
		}

		list = (ListView) findViewById(R.id.todo_by_contact_list);

		conAcc = new ContactsAccessor(this, getContentResolver());
		contacts = (ArrayList<Contact>)conAcc.readContacts(db.getAllContacts());
		adapter = new ContactArrayAdapter(this, contacts);
		adapter.sort(new ContactNameComparator());

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			
			/**
			 * Ruft Liste aller Todos des gewaehlten Kontakts auf
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Contact contact = contacts.get(position);
				Log.i("Contact",contact.toString());
				Intent intent = new Intent(TodoByContactActivity.this, ContactTodosActivity.class);
				intent.putExtra("contact_id", contact.getId());
				startActivity(intent);
			}
			
		});

	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.todo_by_contact_menu, menu);
		return true;
	}
	
	/**
	 * Zurueck zur Todo-Uebersicht
	 * @param item
	 */
	public void gotoTodoOverview(MenuItem item) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

}
