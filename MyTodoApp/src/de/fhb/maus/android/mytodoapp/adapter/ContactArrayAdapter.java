package de.fhb.maus.android.mytodoapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.data.Contact;

/**
 * Adapter fuer Liste aller Kontakte, denen Todos zugeordnet sind
 * @author Daniel Weis
 *
 */
public class ContactArrayAdapter extends ArrayAdapter<Contact>{
	
	private final Activity context;
	private final ArrayList<Contact> contacts;
	
	// ViewHolder fuer das ViewHolder-Pattern
	static class ViewHolder {
		private ImageView contactPicture;
		private TextView contactName;
	}
	
	public ContactArrayAdapter(Activity context, ArrayList<Contact> contacts){
		super(context, R.layout.todo_by_c_contacts_rowlayout, contacts);
		this.context = context;
		this.contacts = contacts;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		// Holder-Elemente bei erstem Aufruf initialisieren
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getLayoutInflater();
			rowView = inflater.inflate(R.layout.todo_by_c_contacts_rowlayout, parent, false);

			ViewHolder holder = new ViewHolder();
			
			holder.contactName = (TextView) rowView.findViewById(R.id.todo_by_c_contact_name);
			holder.contactPicture = (ImageView) rowView.findViewById(R.id.todo_by_c_contact_picture);
			// Tag setzen, um Holder zu finden
			rowView.setTag(holder);

		}
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		
		//Kontaktdaten einfuegen
		holder.contactPicture.setImageBitmap(contacts.get(position).getThumbnail());
		holder.contactName.setText(contacts.get(position).getName());
		
		return rowView;
	}

}
