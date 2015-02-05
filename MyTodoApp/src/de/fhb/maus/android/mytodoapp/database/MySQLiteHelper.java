package de.fhb.maus.android.mytodoapp.database;

import java.util.ArrayList;

import de.fhb.maus.android.mytodoapp.data.Todo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Datenbankmanager
 * 
 * @author Sebastian Kindt, Daniel Weis
 * 
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	// Datenbank-Version
	private static final int DATABASE_VERSION = 2;
	// Datenbank-Name
	private static final String DATABASE_NAME = "TodoDB";

	// Tabellen-Namen
	private static final String TABLE_TODO = "todos";
	private static final String TABLE_CONTACTS = "contacts";

	// Spalten-Namen der Todo-Tabelle
	private static final String KEY_ID = "id";
	private static final String KEY_Name = "name";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_ISDONE = "isdone";
	private static final String KEY_ISIMPORTANT = "isimportant";
	private static final String KEY_MATURITYDATE = "maturitydate";
	private static final String KEY_LOCATIONNAME = "locationName";
	private static final String KEY_LOCATIONLATITUDE = "locationLatitude";
	private static final String KEY_LOCATIONLONGITUDE = "locationLongitude";

	// Spalten-Namen der Contact-Tabelle
	private static final String KEY_TODOID = "todoid";
	private static final String KEY_CONTACTID = "contactid";
	
	// Namen aller Spalten
	private static final String[] COLUMNS_TODO = { KEY_ID, KEY_Name,
			KEY_DESCRIPTION, KEY_ISDONE, KEY_ISIMPORTANT, KEY_MATURITYDATE, 
			KEY_LOCATIONNAME, KEY_LOCATIONLATITUDE, KEY_LOCATIONLONGITUDE };
	private static final String[] COLUMNS_CONTACTS = { KEY_TODOID, KEY_CONTACTID };

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_Name
				+ " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_ISDONE
				+ " INTEGER, " + KEY_ISIMPORTANT + " INTEGER, "
				+ KEY_MATURITYDATE + " INTEGER, " + KEY_LOCATIONNAME + "TEXT, "
				+ KEY_LOCATIONLATITUDE + "DOUBLE, " + KEY_LOCATIONLONGITUDE + "DOUBLE )";
		
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + " ( "
				+ KEY_TODOID + " INTEGER, "
				+ KEY_CONTACTID + " INTEGER, "
				+ "PRIMARY KEY (" + KEY_TODOID + ", " + KEY_CONTACTID + "))";

		db.execSQL(CREATE_TODO_TABLE);
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	/**
	 * Upgradet Datenbank bei Versions-Aenderungen
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Loesche bereits existierende Daten
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Erstelle neue Datenbank
		this.onCreate(db);
	}

	// ---------------------------------------------------------------------

	/**
	 * CRUD Operationen
	 */

	// C - create
	public long addTodo(Todo todo) {
		// get reference to writable database
		SQLiteDatabase db = this.getWritableDatabase();

		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_Name, todo.getName());
		values.put(KEY_DESCRIPTION, todo.getDescription());
		values.put(KEY_ISDONE, (todo.isDone()) ? 1 : 0);
		values.put(KEY_ISIMPORTANT, (todo.isImportant()) ? 1 : 0);
		values.put(KEY_MATURITYDATE, todo.getMaturityDate());
		values.put(KEY_LOCATIONNAME, todo.getLocationName());
		values.put(KEY_LOCATIONLATITUDE, todo.getLocationLatitude());
		values.put(KEY_LOCATIONLONGITUDE, todo.getLocationLongitude());

		// insert
		long newId = db.insert(TABLE_TODO, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names

		// close
		db.close();
		return newId;
	}
	
	/**
	 * Kontakt einfuegen
	 * @param todoId
	 * @param contactId
	 */
	public void addContact(long todoId, long contactId){
		// Datenbank-Referenz holen
		SQLiteDatabase db = this.getWritableDatabase();

		// ContentValues mit einzufuegenden Werten erstellen
		ContentValues values = new ContentValues();
		values.put(KEY_TODOID, todoId);
		values.put(KEY_CONTACTID, contactId);

		// Einfuegen
		db.insert(TABLE_CONTACTS, // Tabelle
				null, // nullColumnHack
				values); // Werte

		// Schliessen
		db.close();
	}

	// R - read
	public Todo getTodo(long id) {
		// get reference to readable database
		SQLiteDatabase db = this.getReadableDatabase();

		// build query
		Cursor cursor = db.query(TABLE_TODO, // table
				COLUMNS_TODO, // columns names
				" id = ?", // selections
				new String[] { String.valueOf(id) }, // select
				null, // group by
				null, // having
				null, // order by
				null); // limit

		// if we got results get the first one
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
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
		todo.setLocationName(cursor.getString(6));
		todo.setLocationLatitude(cursor.getDouble(7));
		todo.setLocationLongitude(cursor.getDouble(8));

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
				todo.setLocationName(cursor.getString(6));
				// WAS IST DAMIT NICHT IN ORDNUNG?
				/*todo.setLocationLatitude(cursor.getDouble(7));
				todo.setLocationLongitude(cursor.getDouble(8));*/

				// Add todo to todos
				todos.add(todo);
			} while (cursor.moveToNext());
		}

		// close
		db.close();

		// return todos
		return todos;
	}
	
	/**
	 * Alle dem gewaehlten Todo zugeordneten Kontakt-IDs holen
	 * @param id Todo-ID
	 * @return Liste der zugeordneten Kontakt-IDs
	 */
	public ArrayList<Long> getContactsFromTodo(long id) {
		ArrayList<Long> contacts = new ArrayList<Long>();
		// Datenbank-Referenz holen
		SQLiteDatabase db = this.getReadableDatabase();

		// Query bauen
		Cursor cursor = db.query(TABLE_CONTACTS, // Tabelle
				COLUMNS_CONTACTS, // Spalten-Namen
				KEY_TODOID + " = ?", // Selection
				new String[] { String.valueOf(id) }, // Selection-Argumente
				null, // group by
				null, // having
				null, // order by
				null); // limit

		// Falls Kontakte gefunden wurden, fuege sie der Liste hinzu
		if (cursor != null && cursor.getCount()>0) {
			cursor.moveToFirst();
			do {
				contacts.add(cursor.getLong(1));
			} while (cursor.moveToNext());
			cursor.close();
		} else {
			Log.d("Error", "Contacts from Todo not found");
		}

		db.close();

		return contacts;
	}
	
	/**
	 * Alle einem beliebigen Todo zugeordneten Kontakte holen
	 * @return
	 */
	public ArrayList<Long> getAllContacts() {
		ArrayList<Long> contacts = new ArrayList<Long>();
		// Datenbank-Referenz holen
		SQLiteDatabase db = this.getReadableDatabase();

		// Query bauen
		Cursor cursor = db.query(true, //distinct, keine IDs mehrfach
				TABLE_CONTACTS, // Tabelle
				new String[]{ KEY_CONTACTID }, // Spalten-Namen
				null, // Selection
				null, // Selection-Argumente
				null, // group by
				null, // having
				null, // order by
				null); // limit

		// Falls Kontakte gefunden wurden, fuege sie der Liste hinzu
		if (cursor != null && cursor.getCount()>0) {
			cursor.moveToFirst();
			do {
				contacts.add(cursor.getLong(0));
			} while (cursor.moveToNext());
			cursor.close();
		} else {
			Log.d("Error", "No contacts found");
		}

		db.close();

		return contacts;
	}
	
	/**
	 * Alle dem gewaehlten Kontakt zugeordneten Todo-IDs holen
	 * @param id Kontakt-ID
	 * @return Liste der zugeordneten Todo-IDs
	 */
	public ArrayList<Todo> getTodosFromContact(long id) {
		ArrayList<Todo> todos = new ArrayList<Todo>();
		// Datenbank-Referenz holen
		SQLiteDatabase db = this.getReadableDatabase();

		// Query bauen
		Cursor cursor = db.query(TABLE_CONTACTS, // Tabelle
				COLUMNS_CONTACTS, // Spalten-Namen
				KEY_CONTACTID + " = ?", // Selection
				new String[] { String.valueOf(id) }, // Selection-Argumente
				null, // group by
				null, // having
				null, // order by
				null); // limit

		// Falls Todos gefunden wurden, fuege sie der Liste hinzu
		if (cursor != null && cursor.getCount()>0) {
			cursor.moveToFirst();
			do {
				todos.add(getTodo(cursor.getLong(0)));
			} while (cursor.moveToNext());
			cursor.close();
		} else {
			Log.d("error", "Todos from Contact not found");
		}

		db.close();

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
		values.put(KEY_LOCATIONNAME, todo.getLocationName());
		values.put(KEY_LOCATIONLATITUDE, todo.getLocationLatitude());
		values.put(KEY_LOCATIONLONGITUDE, todo.getLocationLongitude());

		// updating row
		int i = db.update(TABLE_TODO, // table
				values, // column/value
				KEY_ID + " = ?", // selections
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
	
	/**
	 * Zuordnung eines Kontakts zu einem Todo entfernen
	 * @param todoId
	 * @param contactId
	 */
	public void deleteContact(long todoId, long contactId) {
		// Datenbank-Referenz holen
		SQLiteDatabase db = this.getWritableDatabase();

		// Loeschen
		db.delete(TABLE_CONTACTS, // Tabelle
				KEY_TODOID + " = ? AND "
				+ KEY_CONTACTID + " = ?", // Selection
				new String[] { String.valueOf(todoId),
					String.valueOf(contactId)}); // Selection-Argumente

		db.close();
	}
	
	/**
	 * Alle einem Todo zugeordneten Kontakte entfernen
	 * @param todoId
	 */
	public void deleteTodoContacts(long todoId) {
		// Datenbank-Referenz holen
		SQLiteDatabase db = this.getWritableDatabase();

		// Loeschen
		db.delete(TABLE_CONTACTS, // Tabelle
				KEY_TODOID + " = ?", // Selection
				new String[] { String.valueOf(todoId)}); // Selection-Argumente

		db.close();
	}
}
