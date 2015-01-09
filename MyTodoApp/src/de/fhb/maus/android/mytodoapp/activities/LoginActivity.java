package de.fhb.maus.android.mytodoapp.activities;

import de.fhb.maus.android.mytodoapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		boolean isServerAvaible = true;

		if (isServerAvaible) {

		} else {
			logIn(getCurrentFocus());
		}

	}

	public void logIn(View view) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
