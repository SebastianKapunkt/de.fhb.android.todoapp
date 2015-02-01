package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;
import de.fhb.maus.android.mytodoapp.server.ServerCommunication;

public class LoginActivity extends Activity {

	private EditText email;
	private EditText password;
	private String hintEmail;
	private String hintPassword;
	private Button login;
	private boolean passwordlenght;
	private boolean emaillenght;
	private Context context = this;
	private MySQLiteHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		db = new MySQLiteHelper(context);

		if (ServerCommunication.checkConnection()) {
			if (ServerCommunication.makeDataSynch(db)) {
				Log.d("Server", "sync success");
				Toast.makeText(getApplicationContext(), "matched todos with server",
						Toast.LENGTH_LONG).show();
			} else {
				Log.d("Server", "sync failed");
			}
		} else {
			Log.d("Server", "no connection");
			logIn(getCurrentFocus());
			Toast.makeText(getApplicationContext(), "Server not avaible",
					Toast.LENGTH_LONG).show();
		}

		email = (EditText) findViewById(R.id.email_address);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login_button);
		hintEmail = "not a valid email";
		hintPassword = "password need 6 numbers";
		login.setEnabled(false);

		email.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (isValidEmail(s)) {
					emaillenght = true;
					email.setError(null);

				} else {
					email.setError(hintEmail);
					emaillenght = false;
				}
				checkLoginButton();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				for (int i = s.length(); i > 0; i--) {

					if (s.subSequence(i - 1, i).toString().equals("\n"))
						s.replace(i - 1, i, "");

				}
			}
		});

		password.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 6) {
					password.setError(null);
					passwordlenght = true;
				} else {
					password.setError(hintPassword);
					passwordlenght = false;
				}
				checkLoginButton();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				for (int i = s.length(); i > 0; i--) {

					if (s.subSequence(i - 1, i).toString().equals("\n"))
						s.replace(i - 1, i, "");

				}
			}
		});
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public void checkLoginButton() {
		if (emaillenght && passwordlenght) {
			login.setEnabled(true);
		} else {
			login.setEnabled(false);
		}
	}

	public void logIn(View view) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		finish();
	}
}
