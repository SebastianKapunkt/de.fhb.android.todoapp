package de.fhb.maus.android.mytodoapp.activities;

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
	private StringBuilder time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_time);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);
		datePicker = (DatePicker) findViewById(R.id.datePicker);

	}

	// "HH:mm dd.MM.yyyy"
	public void saveDateTimeChange(View v) {
		time = new StringBuilder();

		if (timePicker.getCurrentHour() < 10) {
			time.append("0");
		}

		time.append(timePicker.getCurrentHour());
		time.append(":");

		if (timePicker.getCurrentMinute() < 10) {
			time.append("0");
		}

		time.append(timePicker.getCurrentMinute() + " ");

		if(datePicker.getDayOfMonth() < 10){
			time.append("0");
		}
		
		time.append(datePicker.getDayOfMonth()+".");
		
		if(datePicker.getMonth() < 9){
			time.append("0");
		}
		
		time.append(datePicker.getMonth()+1+".");
		time.append(datePicker.getYear());
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra("time", time.toString());
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void cancelDateTimeChange(View v) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
}
