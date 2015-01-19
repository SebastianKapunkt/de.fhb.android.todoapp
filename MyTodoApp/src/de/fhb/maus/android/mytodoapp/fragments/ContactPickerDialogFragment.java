package de.fhb.maus.android.mytodoapp.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import de.fhb.maus.android.mytodoapp.R;

public class ContactPickerDialogFragment extends DialogFragment {
	
	private ArrayList<Integer> mSelectedItems;
	private String[] contactNames;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
	    mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
	    
	    ArrayList<String> contactNamesList = getArguments().getStringArrayList("names");
	    contactNames = contactNamesList.toArray(new String[contactNamesList.size()]);
	    Log.i("contactNames", "Names: " + contactNames);
	    
	    ArrayList<String> checkedNamesList = getArguments().getStringArrayList("checked");
	    boolean[] checkedNames = new boolean[checkedNamesList.size()];
	    for(int i = 0; i < checkedNamesList.size();i++){
	    	if(checkedNamesList.get(i) == "1"){
	    		checkedNames[i] = true;
	    	} else {
	    		checkedNames[i] = false;
	    	}
	    }
	    Log.i("contactNames", "Checked Names: " + checkedNames);
	    
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle(R.string.pick_contacts)
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	           .setMultiChoiceItems(contactNames, checkedNames,
	                      new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       mSelectedItems.add(which);
	                   } else if (mSelectedItems.contains(which)) {
	                       // Else, if the item is already in the array, remove it 
	                       mSelectedItems.remove(Integer.valueOf(which));
	                   }
	               }
	           })
	    // Set the action buttons
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // User clicked OK, so save the mSelectedItems results somewhere
	                   // or return them to the component that opened the dialog

	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {

	               }
	           });

	    return builder.create();
	}
}
