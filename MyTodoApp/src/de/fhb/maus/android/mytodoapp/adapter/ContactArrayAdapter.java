package de.fhb.maus.android.mytodoapp.adapter;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.data.ContactsAccessor;

public class ContactArrayAdapter extends ArrayAdapter<Contact>{
	
	private final Activity context;
	private final ArrayList<Contact> contacts;
	private Drawable contactPlaceholder;
	private ContactsAccessor conAcc;
	
	// ViewHolder for viewholder pattern from listview
	static class ViewHolder {
		private ImageView contactPicture;
		private TextView contactName;
	}
	
	public ContactArrayAdapter(Activity context, ArrayList<Contact> contacts){
		super(context, R.layout.todo_by_c_contacts_rowlayout, contacts);
		this.context = context;
		this.contacts = contacts;
		this.conAcc = new ContactsAccessor(context, context.getContentResolver());
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		// initialize elements of holder if first time called
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getLayoutInflater();
			rowView = inflater.inflate(R.layout.todo_by_c_contacts_rowlayout, parent, false);

			ViewHolder holder = new ViewHolder();
			
			holder.contactName = (TextView) rowView.findViewById(R.id.todo_by_c_contact_name);
			holder.contactPicture = (ImageView) rowView.findViewById(R.id.todo_by_c_contact_picture);
			// set Tag to find holder
			rowView.setTag(holder);

		}
		// get the holder applied to the tag
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		

		Bitmap photo = conAcc.readContactPicture(false, contacts.get(position).getId());
		holder.contactPicture.setImageBitmap(photo);
		holder.contactName.setText(contacts.get(position).getName());
		
		return rowView;
	}

}
