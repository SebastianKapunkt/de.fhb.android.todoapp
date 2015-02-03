package de.fhb.maus.android.mytodoapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.activities.TodoContextActivity;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

/**
 * Adapter fuer die Listview in der Uebersicht
 * 
 * @author Sebastian Kindt
 * 
 */
public class TodoArrayAdapter extends ArrayAdapter<Todo> {

	protected static final String SELECTED_TODO = "todo";
	private final Activity context;
	private final ArrayList<Todo> todos;

	// ViewHolder for viewholder pattern from listview
	static class ViewHolder {
		private TextView todoName;
		private TextView todoDate;
		private ImageView isImportant;
		private CheckBox isDoneCheckbox;
		private TextView todoLocationName;
		private LinearLayout linearLayout;
	}

	// default constructor
	public TodoArrayAdapter(Activity context, ArrayList<Todo> todos) {
		super(context, R.layout.rowlayout, todos);
		this.context = context;
		this.todos = todos;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// create db for interacting with checkbox and imageview
		final MySQLiteHelper db = new MySQLiteHelper(context);

		// initialize elements of holder if first time called
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getLayoutInflater();
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.todoName = (TextView) rowView.findViewById(R.id.todo_name);
			holder.isDoneCheckbox = (CheckBox) rowView
					.findViewById(R.id.isdone);
			holder.isImportant = (ImageView) rowView
					.findViewById(R.id.isImportant);
			holder.linearLayout = (LinearLayout) rowView
					.findViewById(R.id.row_layout);
			holder.todoDate = (TextView) rowView.findViewById(R.id.todo_date);
			holder.todoLocationName = (TextView) rowView
					.findViewById(R.id.locationName);
			// set Tag to find holder
			rowView.setTag(holder);

		}
		// get the holder applied to the tag
		final ViewHolder holder = (ViewHolder) rowView.getTag();

		// OnClick listerner: to switch to Context if item is selected
		holder.linearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, TodoContextActivity.class);
				intent.putExtra(SELECTED_TODO, todos.get(position).getId());
				context.startActivity(intent);
			}
		});

		// add listener to edit important stance and persist changes
		holder.isImportant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Todo todo = todos.get(position);
				todo.setImportant(!todo.isImportant());
				db.updateTodo(todo);
				// set image of importants indicator
				switchImage(holder, position);
			}
		});

		// add listener to checkbox and persist changes
		holder.isDoneCheckbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Todo todo = todos.get(position);
				todo.setDone(!todo.isDone());
				db.updateTodo(todo);
				// change behavior if todo is done
				switchBackground(holder, position);
			}
		});

		holder.todoName.setText(todos.get(position).getName());
		holder.isDoneCheckbox.setChecked(todos.get(position).isDone());
		holder.todoDate.setText(todos.get(position).getMaturityDateAsString());
		holder.todoLocationName.setText(todos.get(position).getLocationName());

		// change behavior if todo is done
		switchBackground(holder, position);
		// set image of importants indicator
		switchImage(holder, position);

		return rowView;
	}

	/**
	 * Aendert das Bild entsprechend des Status der Wichtigkeit
	 * 
	 * @param holder
	 * @param position
	 */
	protected void switchImage(ViewHolder holder, int position) {
		if (todos.get(position).isImportant()) {
			holder.isImportant.setImageResource(R.drawable.is_fav);
		} else {
			holder.isImportant.setImageResource(R.drawable.is_not_fav);
		}
	}

	/**
	 * Veraendert die Hintergrund farbe eines Todos wenn es abgelaufen ist
	 * 
	 * @param holder
	 * @param position
	 */
	protected void switchBackground(ViewHolder holder, int position) {
		if (holder.isDoneCheckbox.isChecked()) {
			holder.linearLayout.setBackgroundColor(0x55FFFFFF);
		} else if (todos.get(position).getMaturityDate() < System
				.currentTimeMillis()) {
			holder.linearLayout.setBackgroundColor(0xCCFFAAAA);
		} else {
			holder.linearLayout.setBackgroundColor(0xCCFFFFFF);
		}
	}
}