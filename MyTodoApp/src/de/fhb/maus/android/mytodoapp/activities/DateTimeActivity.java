package de.fhb.maus.android.mytodoapp.activities;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import de.fhb.maus.android.mytodoapp.R;

/**
 * Aktivitaet fuer die Bearbeitung des Datum eines Todos
 * 
 * @author Sebastian Kindt
 * 
 */
public class DateTimeActivity extends Activity {

	private TimePicker timePicker;
	private DatePicker datePicker;
	private Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_time);
		Intent intent = getIntent();

		// Calender Objekt zur leichteren Verarbeitung
		calendar = Calendar.getInstance();
		// Einlesen der Zeit aus dem Intent
		calendar.setTimeInMillis(intent.getLongExtra("time",
				System.currentTimeMillis()));

		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);
		datePicker = (DatePicker) findViewById(R.id.datePicker);

		// Setzen der Werte der Picker aus dem Calender Objekt
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		datePicker.updateDate(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Liest die Zeit aus dem Date- und Timepicker. Wandelt diese in
	 * Millisekunden um, fuegt es dem Intent hinzu und beendet die Aktivität.
	 * 
	 * @param v
	 */
	public void saveDateTimeChange(View v) {
		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());

		Intent returnIntent = new Intent();
		returnIntent.putExtra("time", calendar.getTimeInMillis());
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	/**
	 * Bricht das Bearbeiten der Zeit ab.
	 * 
	 * @param v
	 */
	public void cancelDateTimeChange(View v) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
}
