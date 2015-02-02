package de.fhb.maus.android.mytodoapp.comperator;

import java.util.Comparator;

import de.fhb.maus.android.mytodoapp.data.Todo;

/**
 * Comperator zum Sortieren von Todos nach Datum
 * 
 * @author Sebastian Kindt
 * 
 */
public class TodoDateComperator implements Comparator<Todo> {

	@Override
	public int compare(Todo lhs, Todo rhs) {

		boolean lhsdone = lhs.isDone();
		boolean rhsdone = rhs.isDone();
		long lhstime = lhs.getMaturityDate();
		long rhstime = rhs.getMaturityDate();

		if (lhsdone && rhsdone) {
			if (lhstime < rhstime) {
				return 1;
			} else {
				return -1;
			}
		} else if (!lhsdone && rhsdone) {
			return -1;
		} else if (lhsdone && !rhsdone) {
			return 1;
		} else if (!lhsdone && !rhsdone) {
			if (lhstime < rhstime) {
				return -1;
			} else {
				return 1;
			}
		}

		return 0;
	}

}
