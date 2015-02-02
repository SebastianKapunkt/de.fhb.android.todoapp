package de.fhb.maus.android.mytodoapp.comparator;

import java.util.Comparator;

import de.fhb.maus.android.mytodoapp.data.Todo;

/**
 * Comparator zum Sortieren von Todos nach Wichtigkeit
 * 
 * @author Sebastian Kindt
 * 
 */
public class TodoImportantComparator implements Comparator<Todo> {

	@Override
	public int compare(Todo lhs, Todo rhs) {

		Boolean lhsdone = lhs.isDone();
		Boolean rhsdone = rhs.isDone();
		Boolean lhsimp = lhs.isImportant();
		Boolean rhsimp = rhs.isImportant();

		if (lhsdone && rhsdone) {
			if (lhsimp && rhsimp) {
				return 0;
			}
			if (!lhsimp && rhsimp) {
				return 1;
			}
			if (lhsimp && !rhsimp) {
				return -1;
			}
			return 0;
		}

		if (!lhsdone && rhsdone) {
			return -1;
		}

		if (lhsdone && !rhsdone) {
			return 1;
		}

		if (!lhsdone && !rhsdone) {
			if (lhsimp && rhsimp) {
				return 0;
			}
			if (!lhsimp && rhsimp) {
				return 1;
			}
			if (lhsimp && !rhsimp) {
				return -1;
			}
			return 0;
		}

		return 0;
	}

}
