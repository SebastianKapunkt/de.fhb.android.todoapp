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

public class TodoArrayAdapter extends ArrayAdapter<Todo> {

	protected static final String SELECTED_TODO = "todo";
	private final Activity context;
	private final ArrayList<Todo> todos;

	static class ViewHolder {
		private TextView todoName;
		private ImageView isImportant;
		private CheckBox isDoneCheckbox;
		private LinearLayout linearLayout;
	}

	public TodoArrayAdapter(Activity context, ArrayList<Todo> todos) {
		super(context, R.layout.rowlayout, todos);
		this.context = context;
		this.todos = todos;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final MySQLiteHelper db = new MySQLiteHelper(context);

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
			rowView.setTag(holder);
			
		}
		final ViewHolder holder = (ViewHolder) rowView.getTag();

		// OnClick Listerner to switch to Context for the selected Item
		holder.linearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, TodoContextActivity.class);
				intent.putExtra(SELECTED_TODO, todos.get(position).getId());
				context.startActivity(intent);
			}
		});
		
		holder.isImportant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Todo todo = todos.get(position);
				todo.setImportant(!todo.isImportant());
				db.updateTodo(todo);
				if (todo.isImportant()) {
					holder.isImportant.setImageResource(R.drawable.is_fav);
				} else {
					holder.isImportant.setImageResource(R.drawable.is_not_fav);
				}
			}
		});
		
		holder.isDoneCheckbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Todo todo = todos.get(position);
				todo.setDone(!todo.isDone());
				db.updateTodo(todo);
			}
		});
		
		holder.todoName.setText(todos.get(position).getName());
		holder.isDoneCheckbox.setChecked(todos.get(position).isDone());
		
		// Set Image of Favorite indicator
		if (todos.get(position).isImportant()) {
			holder.isImportant.setImageResource(R.drawable.is_fav);
		} else {
			holder.isImportant.setImageResource(R.drawable.is_not_fav);
		}

		return rowView;
	}
}