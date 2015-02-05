package de.fhb.maus.android.mytodoapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.R;

public class ContextContactArrayAdapter extends ArrayAdapter<Contact>{
	
	private final Activity context;
	private final ArrayList<Contact> contacts;
	
	// ViewHolder for viewholder pattern from listview
	static class ViewHolder {
		private TextView contactName;
		private ImageView contactEmail;
		private ImageView contactSMS;
	}
	
	public ContextContactArrayAdapter(Activity context, ArrayList<Contact> contacts){
		super(context, R.layout.context_contact_rowlayout, contacts);
		this.context = context;
		this.contacts = contacts;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		// initialize elements of holder if first time called
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getLayoutInflater();
			rowView = inflater.inflate(R.layout.context_contact_rowlayout, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.contactName = (TextView) rowView.findViewById(R.id.context_contact_name);
			holder.contactEmail = (ImageView) rowView.findViewById(R.id.sendEmail);
			holder.contactSMS = (ImageView) rowView.findViewById(R.id.sendSMS);
			// set Tag to find holder
			rowView.setTag(holder);

		}
		// get the holder applied to the tag
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		
		//TODO OnClick listener when clicking on name
		
		//TODO OnClick listener to send email
		
		//TODO OnClick listener to send SMS

		holder.contactName.setText(contacts.get(position).getName());
		
		//TODO gray out pictures

		return rowView;
	}

}
