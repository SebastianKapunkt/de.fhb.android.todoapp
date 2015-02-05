package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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

/**
 * Aktivitaet fuer das Einloggen in die APP
 * 
 * @author Sebastian Kindt
 * 
 */
public class LoginActivity extends Activity {

	private EditText email;
	private EditText password;
	private final String hintEmail = "not a valid email";
	private final String hintPassword = "password to short";
	private Button login;
	private boolean passwordflag;
	private boolean emailflag;
	private Context context = this;
	private MySQLiteHelper db;
	private boolean islogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Einrichten der Datenbankverbindung
		db = new MySQLiteHelper(context);

		// Überprüfung ob Server erreichbar ist
		if (ServerCommunication.checkConnection()) {
			// Wenn erreichbar dann gleiche Daten mit dem Server ab
			if (ServerCommunication.makeDataSynch(db)) {
				// Und melde es dem Nutzer
				Toast.makeText(getApplicationContext(),
						"matched todos with server", Toast.LENGTH_LONG).show();
			} else {
				// Falls die synchronisation fehlt schlägt
				Log.d("Server", "sync failed");
				Toast.makeText(getApplicationContext(), "matching failed",
						Toast.LENGTH_LONG).show();
			}
		} else {
			// Wenn der Server nicht erreichbar ist
			Log.d("Server", "no connection");

			// Melde es dem Nutzer
			Toast.makeText(getApplicationContext(), "Server not available",
					Toast.LENGTH_LONG).show();

			// dann gehe zur Overview ueber
			startActivity(new Intent(this, TodoOverviewActivity.class));
		}

		email = (EditText) findViewById(R.id.email_address);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login_button);
		login.setEnabled(false);

		// Listener für das bearbeiten des Email Eingabefeldes
		email.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// pruefe ob eine valide Email
				if (isValidEmail(s)) {
					// setze flag das die Email okay ist
					emailflag = true;
					// loescht die Fehlermeldung
					email.setError(null);
				} else {
					// Wenn nicht valid gebe error message aus
					email.setError(hintEmail);
					// und setze flag das Email nicht stimmt
					emailflag = false;
				}
				// ueberpruefe den Status des Loginbuttons
				checkLoginButton();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			// Loesche jedes Enter falls eingegeben
			public void afterTextChanged(Editable s) {
				for (int i = s.length(); i > 0; i--) {

					if (s.subSequence(i - 1, i).toString().equals("\n"))
						s.replace(i - 1, i, "");

				}
			}
		});

		// Listener für das bearbeiten des Passwort Eingabefeldes
		password.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Wenn die Mindeslaenge des Passworts erreicht wurde
				if (s.length() >= 6) {
					// loesche Fehlermeldung
					password.setError(null);
					// setze passwortflag
					passwordflag = true;
				} else {
					// Wenn nicht dann gebe Fehlermeldung aus
					password.setError(hintPassword);
					// und setze flag entsprechend
					passwordflag = false;
				}
				// ueberpruefe status des Loginbuttons
				checkLoginButton();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			// Loesche jedes Enter falls eingegeben
			public void afterTextChanged(Editable s) {
				for (int i = s.length(); i > 0; i--) {

					if (s.subSequence(i - 1, i).toString().equals("\n"))
						s.replace(i - 1, i, "");

				}
			}
		});
	}

	/**
	 * Ueberprueft ob Uebergabeparameter eine email ist. Wenn ja dann gebe
	 * "true" zurueck wenn nicht dann "false"
	 * 
	 * @param email
	 * @return
	 */
	public final static boolean isValidEmail(CharSequence email) {
		if (TextUtils.isEmpty(email)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
	}

	/**
	 * Ueberprueft den status des Loginbuttons nur aktiv wenn beide
	 * Eingabefelder(Email und Passwort) valid sind Und setzt den Fehlerhinweis
	 * zurueck sobald ein Zeichen veraendert wurde
	 */
	public void checkLoginButton() {
		if (emailflag && passwordflag) {
			login.setEnabled(true);
		} else {
			login.setEnabled(false);
		}
		login.setError(null);
	}

	/**
	 * Der Login vorgang mit Validierung ueber Webserver
	 * 
	 * @param view
	 */
	public void logIn(View view) {
		// Progressdialog fuer den Fortschritt des Validationsvorgangs
		final ProgressDialog progress = ProgressDialog.show(this,
				"authentication", "pending...", true);

		// Asynchrone Ueberpruefung
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Uebergibt die Werte der Eingabefelder per post an den
				// Webserver
				if (ServerCommunication.authenticate(password.getText()
						.toString(), email.getText().toString())) {
					// wenn Validierung erfolgreich war
					islogin = true;
				} else {
					// wenn sie fehlgeschlagen ist
					islogin = false;
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// blendet den Dialog aus
						progress.dismiss();
						// Entscheidet je nach dem wie die Validierung
						// ausgefallen ist
						if (islogin) {
							// meldet dem Nutzer den Erfolgreichen login
							Toast.makeText(getApplicationContext(),
									"login successfull", Toast.LENGTH_LONG)
									.show();
							// und start den Overview Aktivitaet
							startActivity(new Intent(context,
									TodoOverviewActivity.class));
						} else {
							// meldet das Passwort oder Email nicht Valid ist
							Toast.makeText(getApplicationContext(),
									"wrong password or email",
									Toast.LENGTH_LONG).show();
							// setz Fehlerhinweis auf Loginbutton
							login.setError("email or password wrogn");
						}
					}
				});
			}
		}).start();

	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
