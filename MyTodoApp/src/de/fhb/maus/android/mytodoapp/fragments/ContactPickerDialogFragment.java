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
	
	public interface AddRemoveContactsDialogListener {
	    void onFinishAddRemoveContactsDialog(ArrayList<Integer> added, ArrayList<Integer> removed);
	}
	private ArrayList<Integer> addedItems;
	private ArrayList<Integer> removedItems;
	private String[] contactNames;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
	    addedItems = new ArrayList<Integer>();
	    removedItems = new ArrayList<Integer>();
	    
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
	                       if (removedItems.contains(which)) {
	                    	   removedItems.remove(Integer.valueOf(which));
	                       } else {
	                    	   addedItems.add(which);
	                       }
	                   } else {
	                	   if (addedItems.contains(which)) {
	                    	   addedItems.remove(Integer.valueOf(which));
	                       } else {
	                    	   removedItems.add(which);
	                       }
	                   }
	               }
	           })
	    // Set the action buttons
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   AddRemoveContactsDialogListener activity = (AddRemoveContactsDialogListener) getActivity();
	            	   activity.onFinishAddRemoveContactsDialog(addedItems, removedItems);
	            	   dismiss();

	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   dismiss();
	               }
	           });

	    return builder.create();
	}
}
