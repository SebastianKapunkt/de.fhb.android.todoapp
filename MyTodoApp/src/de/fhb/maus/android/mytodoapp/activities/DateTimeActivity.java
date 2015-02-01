package de.fhb.maus.android.mytodoapp.activities;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import de.fhb.maus.android.mytodoapp.R;

public class DateTimeActivity extends Activity {

	private TimePicker timePicker;
	private DatePicker datePicker;
	private Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_time);
		Intent intent = getIntent();
		
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(intent.getLongExtra("time", System.currentTimeMillis()));

		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

		datePicker = (DatePicker) findViewById(R.id.datePicker);
		datePicker.updateDate(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
	}

	public void saveDateTimeChange(View v) {
		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());

		Intent returnIntent = new Intent();
		returnIntent.putExtra("time", calendar.getTimeInMillis());
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void cancelDateTimeChange(View v) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
}
