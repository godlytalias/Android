package com.example.gtacampus;

import java.util.Calendar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

public class CampusActivity extends ListActivity {
	
	private static final int DIALOG_ID = 0;
	private static final int ALARM_DIALOG_TIME=1;
	private static final int ALARM_DIALOG_DATE = 2;
	Calendar myCal;
	public int Year,month,day,hour,Minute;
	
	//MediaPlayer mediaplayer;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus);
		String[] functions = new String[]{"Calculator","Slots","Courses","Add Courses","Notes","Bunk-O-Meter","Alarm"};
		ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,functions);
		this.setListAdapter(listadapter);

	/*	 mediaplayer= MediaPlayer.create(this, R.raw.sundaychurch);
		 mediaplayer.start();
		while(mediaplayer.getCurrentPosition()<1600)
		{
			
		}
			mediaplayer.stop();
			mediaplayer.release();*/

		if(isUpgraded())
		{
			Intent i = new Intent(CampusActivity.this,Initialize.class);
			startActivity(i);
		}
		}
		
			
	
	public void Calculator(View v)
	{
		Intent i= new Intent(CampusActivity.this,GTAcalC.class);
		startActivity(i);
	}
	
	public void slotdisp(View v)
	{
		Intent slots = new Intent (CampusActivity.this,Slot.class);
		startActivity(slots);
	}
	
	public void coursefn(View v)
	{
		Intent courseintent=new Intent (CampusActivity.this,courses.class);
		startActivity(courseintent);
	}
	
	public void coursecheck(View v)
	{
		Intent check=new Intent(CampusActivity.this,CheckData.class);
		startActivity(check);
	}
	
	public void notes(View v)
	{
		Intent noteint=new Intent(CampusActivity.this,notedata.class);
		startActivity(noteint);
	}
	
	public void bunk(View v)
	{
		Intent bunkmeter=new Intent(CampusActivity.this,bunkom.class);
		startActivity(bunkmeter);
	}
	
	public void alarm(View v)
	{
		myCal = Calendar.getInstance();
		Year  = myCal.get(Calendar.YEAR);
	    month= myCal.get(Calendar.MONTH);
		day = myCal.get(Calendar.DAY_OF_MONTH);
		hour = myCal.get(Calendar.HOUR_OF_DAY);
		Minute = myCal.get(Calendar.MINUTE);
		showDialog(ALARM_DIALOG_TIME);
							}
	private DatePickerDialog.OnDateSetListener dateselector = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			Year=year;
			month=monthOfYear;
			day=dayOfMonth;
			Intent alintent=new Intent(CampusActivity.this,MyAlarm.class);
			alintent.putExtra("year", Year);
			alintent.putExtra("month", month);
			alintent.putExtra("day", day);
			alintent.putExtra("hour", hour);
			alintent.putExtra("minute", Minute);
			alintent.setAction("setalarm");
			startService(alintent);
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
	
	public boolean isUpgraded(){
		try{
			SharedPreferences ver = getSharedPreferences("AutoAppLauncherPrefs", Context.MODE_PRIVATE);
			int lastVer = ver.getInt("last_version", 0);
			int curVer = getVer();
			if(lastVer!=curVer){
				SharedPreferences.Editor verEdit = ver.edit();
				verEdit.putInt("last_version", curVer);
				verEdit.commit();
				return true;
			}
		}
		catch(Exception e){e.printStackTrace();	}
		return false;
	}
	
	public int getVer(){
		int version = 0;
		try{
			version=getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			}
		catch(NameNotFoundException e){}
		return version;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem menu)
	{
		switch(menu.getItemId())
		{
		case R.id.exit:
			this.finish();
			break;
		
		case R.id.about:
			showDialog(DIALOG_ID);
			break;
		default:			break;
			}
		
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		switch(position)
		{
		case 0:Calculator(v);
				break;
		case 1:slotdisp(v);
				break;
		case 2:coursecheck(v);
				break;
		case 3:coursefn(v);
				break;
		case 4:notes(v);
				break;
		case 5:bunk(v);
				break;
		case 6:alarm(v);
				break;
		}
	}
	
	
	protected final	Dialog onCreateDialog(final int id){
		switch(id){
		case DIALOG_ID:
		Dialog dialog=null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("        GTAcampuS v1.1\n        Copyright © 2013\n\nThis application comes with absolutely NO WARRANTY\nThis is free Android Application: you are welcome to redistribute and modify this.\n\nhttp://github.com/godlytalias/Android/workspace/GTAcampuS \n\nDeveloped by:\nGodly T.Alias\nDepartment of Computer Science & Engineering\nNIT Calicut\n")
		.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				closeOptionsMenu();
			}
		});
		AlertDialog alert=builder.create();
	 dialog=alert;
		return dialog;
		
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
				}
			
		return null;
}
}

