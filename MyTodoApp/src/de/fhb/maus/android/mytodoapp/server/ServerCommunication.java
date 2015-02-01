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

public class ServerCommunication {

	private static StringBuilder itemsString;
	private static String url = "http://192.168.2.101:8080";
	private static MySQLiteHelper db;

	public static boolean makeDataSynch(MySQLiteHelper db) {
		itemsString = new StringBuilder();
		ServerCommunication.db = db;
		boolean isSuccessfull = false;

		if (readItemsFromServer()) {
			isSuccessfull = true;

			try {
				if (!fetchWithDatabase()) {
					isSuccessfull = false;
				}
			} catch (Exception e) {
				isSuccessfull = false;
			}
		}

		return isSuccessfull;
	}

	private static boolean fetchWithDatabase() throws Exception {
		boolean isDone = false;
		db.getReadableDatabase();

		Log.d("Server check Empty", db.getAllTodos() + "");

		if (db.getAllTodos().isEmpty()) {
			putDataToDatabase();
			if (!db.getAllTodos().isEmpty()) {
				isDone = true;
			}
		} else {
			if (putTodoToWebserver()) {
				isDone = true;
			}
		}

		db.close();

		return isDone;
	}

	private static boolean putTodoToWebserver() throws Exception {

		JSONArray items = new JSONArray(itemsString.toString());
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

		return true;
	}

	private static void putDataToDatabase() throws JSONException {
		JSONArray items = new JSONArray(itemsString.toString());
		JSONObject item;
		Todo todo;
		db.getWritableDatabase();

		for (int i = 0; i < items.length(); i++) {
			item = items.getJSONObject(i);
			todo = new Todo(item.getString("name"),
					item.getString("description"), item.getBoolean("done"),
					item.getBoolean("important"), Long.parseLong(
							item.getString("maturityDate"), 10));
			db.addTodo(todo);
		}

		db.close();
	}

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
					itemsString.append(line);
				}
				Log.d("Server der String", itemsString.toString());
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

	private static void deleteTodoOnWebserver(long id) throws Exception {
		InputStream inputStream = null;
		String result = "";

		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(url
				+ "/DataAccessRemoteWebapp/rest/dataitem/" + id);
		HttpResponse httpResponse = httpclient.execute(httpDelete);
		// 9. receive response as inputStream
		inputStream = httpResponse.getEntity().getContent();

		// 10. convert inputstream to string
		if (inputStream != null) {
			result = convertInputStreamToString(inputStream);
		} else {
			result = "Did not work!";
		}
		Log.d("delete", result);
	}

	private static void createTodoOnWebserver(Todo todo) {
		InputStream inputStream = null;
		String result = "";
		try {

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

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		Log.d("Post request", result.toString());
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

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
}
