package de.fhb.maus.android.mytodoapp.comparator;

import de.fhb.maus.android.mytodoapp.data.Contact;
import java.util.Comparator;


/**
 * Vergleiche Kontakte anhand ihres Namens
 * @author Daniel Weis
 *
 */
public class ContactNameComparator implements Comparator<Contact>{
	
	@Override
	public int compare(Contact lhs, Contact rhs){
		return lhs.compareTo(rhs);
	}

}
