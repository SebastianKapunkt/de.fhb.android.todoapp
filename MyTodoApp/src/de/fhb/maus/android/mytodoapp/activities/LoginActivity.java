package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;

public class LoginActivity extends Activity {

	private EditText email;
	private EditText password;
	private TextView hintEmail;
	private TextView hintPassword;
	private Button login;
	boolean passwordlenght;
	boolean emaillenght;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		email = (EditText) findViewById(R.id.email_address);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login_button);
		hintEmail = (TextView) findViewById(R.id.hintemail);
		hintEmail.setText("no Email set");
		hintPassword = (TextView) findViewById(R.id.hintpassword);
		hintPassword.setText("no password set");
		login.setEnabled(false);

		boolean isServerAvaible = true;

		if (isServerAvaible) {

		} else {
			logIn(getCurrentFocus());
		}

		email.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					hintEmail.setText("");
					emaillenght = true;
					if (passwordlenght) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}
				} else {
					hintEmail.setText("no Email set");
					emaillenght = false;
				}
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
				if (s.length() > 0) {
					passwordlenght = true;
					hintPassword.setText("");
					if (emaillenght) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}

				} else {
					hintPassword.setText("no Password set");
					passwordlenght = false;
				}
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

	public void logIn(View view) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
