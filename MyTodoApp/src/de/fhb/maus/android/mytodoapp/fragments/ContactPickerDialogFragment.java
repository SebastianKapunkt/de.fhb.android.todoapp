package de.fhb.maus.android.mytodoapp.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import de.fhb.maus.android.mytodoapp.R;

/**
 * ContactPicker, der Auswahl der einem Todo zugeordneten Kontakte ermoeglicht
 * @author Daniel Weis
 *
 */
public class ContactPickerDialogFragment extends DialogFragment {
	
	// Aufrufende Activity muss dieses Interface mit der Callback-Methode implementieren
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
	    
	    //Uebergebene Argumente holen
	    ArrayList<String> contactNamesList = getArguments().getStringArrayList("names");
	    contactNames = contactNamesList.toArray(new String[contactNamesList.size()]);
	    Log.i("contactNames", "Names: " + contactNames);
	    
	    ArrayList<String> checkedNamesList = getArguments().getStringArrayList("checked");
	    //Umwandlung von ArrayList<String> zu boolean[]
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
	    // Dialog-Titel setzen
	    builder.setTitle(R.string.pick_contacts)
	    // MultiChoice-Listener einrichten
	           .setMultiChoiceItems(contactNames, checkedNames,
	                      new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   //Aenderungen in die Listen fuer hinzugefuegte/entfernte Kontakte uebernehmen
	            	   if (isChecked) {
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
	    // Action Buttons setzen
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               
	        	   /**
	        	    * Rufe Callback-Methode in der aufrufenden Activity auf und schliesse Dialog
	        	    */
	        	   @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   AddRemoveContactsDialogListener activity = (AddRemoveContactsDialogListener) getActivity();
	            	   activity.onFinishAddRemoveContactsDialog(addedItems, removedItems);
	            	   dismiss();

	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               
	        	   /**
	        	    * Schliesse Dialog und tu nichts
	        	    */
	        	   @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   dismiss();
	               }
	           });

	    return builder.create();
	}
}
