package de.fhb.maus.android.mytodoapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

/**
 * Alle Operationen zur Interaktion mit dem Webserver - Ablgeichen der Daten -
 * Erstellen von Todos auf dem Server - Lesen von Todos vom Server - Loeschen
 * von Todos vom Server - Ueberpreufen der Verdingung - Authentifizieren
 * 
 * @author TheDeveloper
 * 
 */
public class ServerCommunication {

	// Json objekt das alle Todos vom Server enthealt
	private static StringBuilder todoJsonObjektString;
	// IP des Servers
	private static String url = "http://192.168.2.101:8080";
	private static MySQLiteHelper db;

	/**
	 * Gleicht die Daten von der App und vom Webserver ab. Regel: Wenn Todos in
	 * der App verhanden sind dann werden alle Todos vom Servergeloescht und von
	 * der App auf den Server uebertragen. Wenn keine Todos in der App sind dann
	 * werden die Todos vom Server uebernommen
	 * 
	 * @param db
	 * @return
	 */
	public static boolean makeDataSynch(MySQLiteHelper db) {
		todoJsonObjektString = new StringBuilder();
		ServerCommunication.db = db;

		boolean isSuccessfull = false;
		// Lesen der Todos vom Server
		if (readItemsFromServer()) {
			isSuccessfull = true;

			try {
				// Abgleichen der daten von Server und APP
				if (!fetchWithDatabase()) {
					isSuccessfull = false;
				}
			} catch (Exception e) {
				isSuccessfull = false;
			}
		}

		return isSuccessfull;
	}

	/**
	 * Testet ob eine Verbindung zum Webserver besteht
	 * 
	 * @return
	 */
	public static boolean checkConnection() {
		try {
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			connection.setConnectTimeout(500);
			connection.connect();
			Log.d("Server", "online");
			return true;
		} catch (Exception e) {
			Log.d("Server", "offline");
			return false;
		}
	}

	/**
	 * Sendet Logininformationen an den Server zum Validieren
	 * 
	 * @param password
	 * @param email
	 * @return
	 */
	public static boolean authenticate(String password, String email) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url
					+ "/DataAccessRemoteWebapp/rest/authenticate");

			String json = "";
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("email", email);
			jsonObject.accumulate("password", password);
			json = jsonObject.toString();

			Log.d("Json", json);

			StringEntity se = new StringEntity(json);

			httpPost.setEntity(se);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpclient.execute(httpPost);
			int code = httpResponse.getStatusLine().getStatusCode();

			Log.d("Statuscode", code + "");

			if (code == 200) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Setz die Regeln fuer den Abgleich der Todos um
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean fetchWithDatabase() throws Exception {
		boolean isDone = false;
		db.getReadableDatabase();

		Log.d("Server check Empty", db.getAllTodos() + "");

		// Ueberpreuft ob Daten vorhanden sind
		if (db.getAllTodos().isEmpty()) {
			// wenn nicht dann werden die werden die Todos vom Server an die
			// Lokale Datenbank uebergeben
			putTodosToLokalDatabase();
			// Wenn die Datenbank danach nicht mehr leer ist dann war der
			// ablgeich erfolgreich
			if (!db.getAllTodos().isEmpty()) {
				isDone = true;
			}
		} else {
			// Wenn Daten in der Lokel Datenbank enthalten sind
			if (putTodosToWebserver()) {
				isDone = true;
			}
		}

		db.close();

		return isDone;
	}

	/**
	 * Uebertragen der Todos an den Webserver mittel Rest-Schnittstelle
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean putTodosToWebserver() {
		JSONArray items;
		try {
			items = new JSONArray(todoJsonObjektString.toString());

			JSONObject item;

			for (int i = 0; i < items.length(); i++) {
				item = items.getJSONObject(i);
				deleteTodoOnWebserver(item.getInt("id"));
			}

			db.getReadableDatabase();
			List<Todo> todos = db.getAllTodos();
			for (Todo todo : todos) {
				createTodoOnWebserver(todo);
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Speichert alle Todos aus dem Uebertragenen JsonObjekt in die Lokale
	 * Datenbank
	 * 
	 * @throws JSONException
	 */
	private static void putTodosToLokalDatabase() throws JSONException {
		JSONArray items = new JSONArray(todoJsonObjektString.toString());
		JSONObject item;
		Todo todo;

		db.getWritableDatabase();

		for (int i = 0; i < items.length(); i++) {
			item = items.getJSONObject(i);
			todo = new Todo(item.getString("name"),
					item.getString("description"), 
					item.getBoolean("done"),
					item.getBoolean("important"), 
					Long.parseLong(item.getString("maturityDate"), 10),
					item.getString("locationName"),
					item.getDouble("locationLatitude"),
					item.getDouble("locationLongitude"));
			db.addTodo(todo);
		}

		db.close();
	}

	/**
	 * Stellt eine Get-Anfrage an den Webserver und fuellt itemsString
	 * 
	 * @return
	 */
	private static boolean readItemsFromServer() {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url
				+ "/DataAccessRemoteWebapp/rest/dataitem");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					todoJsonObjektString.append(line);
				}
				Log.d("Server der String", todoJsonObjektString.toString());
			} else {
				Log.d("Server jsonobject", "failed");
				return false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Loescht das Todo mit der entsprechenden id vom Webserver ueber HTTP
	 * Delete method
	 * 
	 * @param id
	 * @throws Exception
	 */
	private static void deleteTodoOnWebserver(long id) throws Exception {
		int code = 404;

		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(url
				+ "/DataAccessRemoteWebapp/rest/dataitem/" + id);
		HttpResponse httpResponse = httpclient.execute(httpDelete);
		code = httpResponse.getStatusLine().getStatusCode();

		if (code == 200) {
			Log.d("Server Delete", "Todo was successfull deleted");
		} else {
			Log.d("Server Delete", "Todo was not deleted");
		}
	}

	/**
	 * Erstellt ein Todo auf dem Webserver ueber HTTP POST
	 * 
	 * @param todo
	 * @throws Exception
	 */
	private static void createTodoOnWebserver(Todo todo) throws Exception {
		int code = 404;

		// 1. create HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		// 2. make POST request to the given URL
		HttpPost httpPost = new HttpPost(url
				+ "/DataAccessRemoteWebapp/rest/dataitem");

		String json = "";

		// 3. build jsonObject
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("name", todo.getName());
		jsonObject.accumulate("description", todo.getDescription());
		jsonObject.accumulate("done", todo.isDone());
		jsonObject.accumulate("important", todo.isImportant());
		jsonObject.accumulate("maturityDate", todo.getMaturityDate() + "");

		// 4. convert JSONObject to JSON to String
		json = jsonObject.toString();

		// 5. set json to StringEntity
		StringEntity se = new StringEntity(json);

		// 6. set httpPost Entity
		httpPost.setEntity(se);

		// 7. Set some headers to inform server about the type of the
		// content
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		// 8. Execute POST request to the given URL
		HttpResponse httpResponse = httpclient.execute(httpPost);

		// 9. Read status code
		code = httpResponse.getStatusLine().getStatusCode();

		if (code == 200) {
			Log.d("Server Create", "Todo was successfull created");
		} else {
			Log.d("Server Create", "Todo was not created");
		}
	}
}
