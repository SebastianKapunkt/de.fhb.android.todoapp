package de.fhb.maus.android.mytodoapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.data.Todo;

public class TodoArrayAdapter extends ArrayAdapter<Todo> {

	private final Activity context;
	private final ArrayList<Todo> todos;

	static class ViewHolder {
		private TextView todoName;
		private ImageButton isImportant;
		private CheckBox isDoneCheckbox;
	}

	public TodoArrayAdapter(Activity context, ArrayList<Todo> todos) {
		super(context, R.layout.rowlayout, todos);
		this.context = context;
		this.todos = todos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getLayoutInflater();
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);
			
			ViewHolder holder = new ViewHolder();
			holder.todoName = (TextView) rowView.findViewById(R.id.todo_name);
			holder.isDoneCheckbox = (CheckBox) rowView
					.findViewById(R.id.isdone);
			holder.isImportant = (ImageButton) rowView
					.findViewById(R.id.isImportant);
			rowView.setTag(holder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.todoName.setText(todos.get(position).getName());
		holder.isDoneCheckbox.setChecked(todos.get(position).isDone());
		if (todos.get(position).isImportant()) {
			holder.isImportant.setImageResource(R.drawable.is_fav);
		} else {
			holder.isImportant.setImageResource(R.drawable.is_not_fav);
		}

		return rowView;
	}
}