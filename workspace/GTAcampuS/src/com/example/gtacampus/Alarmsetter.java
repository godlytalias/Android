package com.example.gtacampus;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Alarmsetter extends ListActivity {
	
	Calendar myCal;
	int Year,month,day,hour,Minute,count=0;
	final int ALARM_DIALOG_TIME=0, ALARM_DIALOG_DATE=1,FINALIZE=2;
	Button newalarm;
	DataManipulator db;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmslayout);
		db = new DataManipulator(this);
		Cursor alarms = db.fetchalarms();
		String[] alarmlist = new String[alarms.getCount()];
		if(alarms!=null)
		{
			alarms.moveToFirst();
			count=0;
			while(!alarms.isAfterLast())
			{
				alarmlist[count]=alarms.getString(6);
				count++;
				alarms.moveToNext();
			}
		ArrayAdapter<String> alarmadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,alarmlist);
		this.setListAdapter(alarmadapter);
		alarms.close();
		}
		myCal = Calendar.getInstance();
		Year  = myCal.get(Calendar.YEAR);
	    month= myCal.get(Calendar.MONTH);
		day = myCal.get(Calendar.DAY_OF_MONTH);
		hour = myCal.get(Calendar.HOUR_OF_DAY);
		Minute = myCal.get(Calendar.MINUTE);
		newalarm=(Button)findViewById(R.id.add_new);
		newalarm.setOnClickListener(setalarm);
	};
	
	private View.OnClickListener setalarm = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(ALARM_DIALOG_TIME);
		}
	};
	
	private DatePickerDialog.OnDateSetListener dateselector = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			Year=year;
			month=monthOfYear;
			day=dayOfMonth;
			showDialog(FINALIZE);
		}
	};
	
	private TimePickerDialog.OnTimeSetListener timeselector = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			hour=hourOfDay;
			Minute=minute;
			showDialog(ALARM_DIALOG_DATE);
		}
			
	};
	
	protected final Dialog onCreateDialog(final int id)
	{
		switch(id)
		{
		case ALARM_DIALOG_TIME:
			return new TimePickerDialog(this, timeselector, hour, Minute, false);
	
		case ALARM_DIALOG_DATE:
			/*
			LayoutInflater inflate = this.getLayoutInflater();
			AlertDialog.Builder alarmDialog = new AlertDialog.Builder(this);
			alarmDialog.setTitle(R.string.alarmtitle)
			.setView(inflate.inflate(R.layout.alarm, null))
			.setPositiveButton("Set", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent alintent=new Intent(CampusActivity.this,MyAlarm.class);
					startActivity(alintent);});*/
			return new DatePickerDialog(this,dateselector,Year,month,day);
			
		case FINALIZE:
			AlertDialog.Builder confirm = new AlertDialog.Builder(this);
			confirm.setTitle("Setting new alarm")
			
			.setMessage("\nTIME    " + hour +" : " + Minute + "\n\nDATE    " + day + " / " + month + " / " +Year+"\n\n")
			
			.setCancelable(false)
			
			.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent alintent=new Intent(Alarmsetter.this,MyAlarm.class);
					alintent.putExtra("year", Year);
					alintent.putExtra("month", month);
					alintent.putExtra("day", day);
					alintent.putExtra("hour", hour);
					alintent.putExtra("minute", Minute);
					alintent.putExtra("title", "Custom Alarm");
					alintent.setAction("setalarm");
					startService(alintent);
					finish();
				}
			})
			
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					closeOptionsMenu();
				}
			});
			return confirm.create();
		}
		return null;
	}

}
