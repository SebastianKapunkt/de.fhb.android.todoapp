package de.fhb.maus.android.mytodoapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.activities.ContactTodosActivity;
import de.fhb.maus.android.mytodoapp.activities.TodoByContactActivity;
import de.fhb.maus.android.mytodoapp.activities.TodoContextActivity;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.R;

public class ContextContactArrayAdapter extends ArrayAdapter<Contact>{
	
	private final Activity context;
	private final ArrayList<Contact> contacts;
	private final Drawable smsIcon;
	private final Drawable smsIconGrey;
	private final Drawable emailIcon;
	private final Drawable emailIconGrey;
	private final Todo todo;
	
	// ViewHolder for viewholder pattern from listview
	static class ViewHolder {
		private TextView contactName;
		private ImageView contactEmail;
		private ImageView contactSMS;
	}
	
	public ContextContactArrayAdapter(Activity context, ArrayList<Contact> contacts, Todo todo){
		super(context, R.layout.context_contact_rowlayout, contacts);
		this.context = context;
		this.contacts = contacts;
		this.todo = todo;
		this.smsIcon = context.getResources().getDrawable(R.drawable.message);
		this.smsIconGrey = context.getResources().getDrawable(R.drawable.message_grey);
		this.emailIcon = context.getResources().getDrawable(R.drawable.mail);
		this.emailIconGrey = context.getResources().getDrawable(R.drawable.mail_grey);
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
		
		if (contacts.get(position).getPhoneNumbers().size() > 0)
			holder.contactSMS.setImageDrawable(smsIcon);
		else
			holder.contactSMS.setImageDrawable(smsIconGrey);
		
		if (contacts.get(position).getEmails().size() > 0)
			holder.contactEmail.setImageDrawable(emailIcon);
		else
			holder.contactEmail.setImageDrawable(emailIconGrey);
		
		holder.contactName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Contact contact = contacts.get(position);
				Log.i("Contact",contact.toString());
				Intent intent = new Intent(context, ContactTodosActivity.class);
				intent.putExtra("contact_id", contact.getId());
				context.startActivity(intent);
				
			}
		});
		
		
		// OnClick listener: to send email
		holder.contactEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> emails = contacts.get(position).getEmails();
				if (emails.size() > 0){
					Intent intent = new Intent(Intent.ACTION_SENDTO);
					String uriText = "mailto:" + Uri.encode(emails.get(0)) + 
					          "?subject=" + Uri.encode("Todo: " + todo.getName()) + 
					          "&body=" + Uri.encode(todo.getDescription());
					Uri uri = Uri.parse(uriText);

					intent.setData(uri);
					context.startActivity(Intent.createChooser(intent, "Send email..."));
				}
				
			}
		});
		
		// OnClick listener: to send SMS
		holder.contactSMS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> phoneNumbers = contacts.get(position).getPhoneNumbers();
				if (phoneNumbers.size() > 0){
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
					intent.setType("vnd.android-dir/mms-sms");
					intent.putExtra("address", phoneNumbers.get(0));         
					intent.putExtra("sms_body","Todo: " + todo.getName() + "\n" + todo.getDescription());
					context.startActivity(Intent.createChooser(intent, "Send SMS..."));
				}
				
			}
		});

		holder.contactName.setText(contacts.get(position).getName());
		

		return rowView;
	}

}
