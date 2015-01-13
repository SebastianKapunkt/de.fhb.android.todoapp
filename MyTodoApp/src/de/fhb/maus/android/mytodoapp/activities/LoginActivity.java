package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
		hintPassword.setText("no Password set");
		login.setEnabled(false);

		boolean isServerAvaible = true;

		if (isServerAvaible) {

		} else {
			logIn(getCurrentFocus());
		}

		email.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (isValidEmail(s)) {
					emaillenght = true;
					hintEmail.setText("");
					hintEmail.setHeight(0);

				} else if (s.length() > 0) {
					hintEmail.setText("is not a valid email");
					hintEmail.setHeight(70);
					emaillenght = false;
				} else {
					hintEmail.setText("no Email set");
					hintEmail.setHeight(70);
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
					hintPassword.setText("");
					hintPassword.setHeight(0);
					passwordlenght = true;
				} else if (s.length() == 0) {
					hintPassword.setText("no Password set");
					hintPassword.setHeight(70);
					passwordlenght = false;
				} else {
					hintPassword.setText("Password is to short");
					hintPassword.setHeight(70);
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
