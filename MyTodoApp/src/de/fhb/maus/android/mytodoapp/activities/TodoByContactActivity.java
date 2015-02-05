package de.fhb.maus.android.mytodoapp.activities;

import java.util.ArrayList;

import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.ContactArrayAdapter;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.data.ContactsAccessor;
import de.fhb.maus.android.mytodoapp.comparator.ContactNameComparator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class TodoByContactActivity extends Activity {
	
	private ListView list;
	private ContactArrayAdapter adapter;
	MySQLiteHelper db = new MySQLiteHelper(this);
	
	private ArrayList<Contact> contacts;
	private ContactsAccessor conAcc;
	
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.todo_by_contact);

		// get the ListView
		list = (ListView) findViewById(R.id.todo_by_contact_list);

		// get custom adapter
		conAcc = new ContactsAccessor(getContentResolver());
		contacts = (ArrayList<Contact>)conAcc.readContacts(db.getAllContacts());
		adapter = new ContactArrayAdapter(this, contacts);
		adapter.sort(new ContactNameComparator());

		// set the custom adapter to the list View
		list.setAdapter(adapter);

	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.todo_by_contact_menu, menu);
		return true;
	}
	
	public void gotoTodoOverview(MenuItem item) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

}
