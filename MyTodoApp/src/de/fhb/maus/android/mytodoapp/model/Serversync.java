package de.fhb.maus.android.mytodoapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.content.Context;
import android.util.Log;
import de.fhb.maus.android.mytodoapp.data.Todo;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

public class Serversync {

	private static StringBuilder itemsString;
	private static Context context;
	private static MySQLiteHelper db;

	public static boolean makeDataSynch(Context context) {
		itemsString = new StringBuilder();
		Serversync.context = context;
		db = new MySQLiteHelper(Serversync.context);

		boolean isSuccessfull = false;

		if (readItemsFromServer()) {
			isSuccessfull = true;

			try {
				if (!fetchWithDatabase()) {
					isSuccessfull = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return isSuccessfull;
	}

	private static boolean fetchWithDatabase() throws JSONException {
		boolean isDone = false;
		db.getReadableDatabase();

		Log.d("Server check Empty", db.getAllTodos() + "");

		if (db.getAllTodos().isEmpty()) {
			putDataToDatabase();
			if (!db.getAllTodos().isEmpty()) {
				isDone = true;
			}
		} else {
			if (putDataToWebserver()) {
				isDone = true;
			}
		}

		db.close();

		return isDone;
	}

	private static boolean putDataToWebserver() {

		try {
			deleteTodoOnWebserver(1);
			deleteTodoOnWebserver(2);
			deleteTodoOnWebserver(3);
			deleteTodoOnWebserver(4);
		} catch (Exception e) {
			e.printStackTrace();
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
					item.getBoolean("important"), item.getInt("maturityDate"));
			db.addTodo(todo);
		}

		db.close();
	}

	private static boolean readItemsFromServer() {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://192.168.2.101:8080/DataAccessRemoteWebapp/rest/dataitem");
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
		HttpDelete httpDelete = new HttpDelete(
				"http://192.168.2.101:8080/DataAccessRemoteWebapp/rest/dataitem/itemId="
						+ id);
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
			HttpPost httpPost = new HttpPost(
					"http://192.168.2.101:8080/DataAccessRemoteWebapp/rest/dataitem");

			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("name", todo.getName());
			jsonObject.accumulate("description", todo.getDescription());
			jsonObject.accumulate("done", todo.isDone());
			jsonObject.accumulate("important", todo.isImportant());
			jsonObject.accumulate("maturityDate", todo.getMaturityDate());

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
}
