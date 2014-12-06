package de.fhb.maus.android.mytodoapp.database;

import java.util.ArrayList;

import de.fhb.maus.android.mytodoapp.data.Todo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// database version
	private static final int DATABASE_VERIOSN = 1;
	// database name
	private static final String DATABASE_NAME = "TodoDB";

	// Todo table name
	private static final String TABLE_TODO = "todos";

	// Todo table columns names
	private static final String KEY_ID = "id";
	private static final String KEY_Name = "name";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_ISDONE = "isdone";
	private static final String KEY_ISIMPORTANT = "isimportant";
	private static final String KEY_MATURITYDATE = "maturitydate";

	// table columns
	private static final String[] COLUMNS = { KEY_ID, KEY_Name,
			KEY_DESCRIPTION, KEY_ISDONE, KEY_ISIMPORTANT, KEY_MATURITYDATE };

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERIOSN);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_Name
				+ " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_ISDONE
				+ " INTEGER, " + KEY_ISIMPORTANT + " INTEGER, " + KEY_MATURITYDATE
				+ " INTEGER )";

		db.execSQL(CREATE_TODO_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop older todos table if exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);

		// create fresh todo table
		this.onCreate(db);
	}

	// ---------------------------------------------------------------------

	/**
	 * CRUD operations (create "add", read "get", update, delete) book + get all
	 * books
	 */

	// C - create
	public void addTodo(Todo todo) {
		// get reference to writable database
		SQLiteDatabase db = this.getWritableDatabase();

		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_Name, todo.getName());
		values.put(KEY_DESCRIPTION, todo.getDescription());
		values.put(KEY_ISDONE, (todo.isDone()) ? 1 : 0);
		values.put(KEY_ISIMPORTANT, (todo.isImportant()) ? 1 : 0);
		values.put(KEY_MATURITYDATE, todo.getMaturityDate());

		// insert
		db.insert(TABLE_TODO, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names

		// close
		db.close();
	}

	// R - read
	public Todo getTodo(int id) {
		// get reference to readable database
		SQLiteDatabase db = this.getReadableDatabase();

		// build query
		Cursor cursor = db.query(TABLE_TODO, // table
				COLUMNS, // columns names
				" id = ?", // selections
				new String[] { String.valueOf(id) }, // select
				null, // group by
				null, // having
				null, // order by
				null); // limit

		// if we got results get the first one
		if (cursor != null) {
			cursor.moveToFirst();
		}else{
			Log.d("error", "Todo not found");
		}

		// build todo object
		Todo todo = new Todo();
		todo.setId(Integer.parseInt(cursor.getString(0)));
		todo.setName(cursor.getString(1));
		todo.setDescription(cursor.getString(2));
		todo.setDone(cursor.getInt(3) > 0);
		todo.setImportant(cursor.getInt(4) > 0);
		todo.setMaturityDate(cursor.getLong(5));

		// close
		db.close();

		// return todo
		return todo;
	}

	public ArrayList<Todo> getAllTodos() {
		ArrayList<Todo> todos = new ArrayList<Todo>();

		// build the query
		String query = "SELECT * FROM " + TABLE_TODO;

		// get reference to writable database
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// go over each row, build todo and add it to list
		Todo todo = null;
		if (cursor.moveToFirst()) {
			do {
				todo = new Todo();
				todo.setId(Integer.parseInt(cursor.getString(0)));
				todo.setName(cursor.getString(1));
				todo.setDescription(cursor.getString(2));
				todo.setDone(cursor.getInt(3) > 0);
				todo.setImportant(cursor.getInt(4) > 0);
				todo.setMaturityDate(cursor.getLong(5));

				// Add todo to todos
				todos.add(todo);
			} while (cursor.moveToNext());
		}

		// close
		db.close();

		// return todos
		return todos;
	}

	// U - update
	public int updateTodo(Todo todo) {
		// get reference to writable database
		SQLiteDatabase db = this.getWritableDatabase();

		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_Name, todo.getName());
		values.put(KEY_DESCRIPTION, todo.getDescription());
		values.put(KEY_ISDONE, (todo.isDone()) ? 1 : 0);
		values.put(KEY_ISIMPORTANT, (todo.isImportant()) ? 1 : 0);
		values.put(KEY_MATURITYDATE, todo.getMaturityDate());

		// updating row
		int i = db.update(TABLE_TODO, // table
				values, KEY_ID, // column/value
				new String[] { String.valueOf(todo.getId()) }); // selection
																// args

		// close
		db.close();

		return i;
	}

	// D - delete
	public void deleteTodo(Todo todo) {
		// get reference to writable database
		SQLiteDatabase db = this.getWritableDatabase();

		// delete
		db.delete(TABLE_TODO, // table
				KEY_ID + " = ?", // selection
				new String[] { String.valueOf(todo.getId()) }); // selection
																// args
		// close
		db.close();
	}
}
