package com.example.gtacampus;

import java.util.Calendar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CampusActivity extends ListActivity {	
	private static final int DIALOG_ID = 0;
	Calendar myCal;
	public int Year,month,day,hour,Minute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
		setContentView(R.layout.activity_campus);
		Animation fade = AnimationUtils.loadAnimation(this, R.anim.fadein);
		findViewById(R.id.mainlayout).startAnimation(fade);
		if(isUpgraded())
		{	Intent i = new Intent(CampusActivity.this,Initialize.class);
			startActivity(i);		}
		
		initalertnotif();
		ListView list = (ListView)findViewById(android.R.id.list);
		list.setFocusable(false);
		list.setScrollbarFadingEnabled(true);
		String[] functions = new String[]{"Alerts","TimeTable","Courses","Notes","Bunk-O-Meter","Convertor","Calculator"};
		ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,functions);
		this.setListAdapter(listadapter);
		this.setTitle("Welcome to GTAcampuS!");
		}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initalertnotif();}
	
	public void initalertnotif(){
		SharedPreferences alarmdet = getSharedPreferences("GTAcampuS_alarmdet", MODE_PRIVATE);
		String details = alarmdet.getString("alarmdetails", "none");
		if(!details.equals("none"))
		{	TextView alarmdet1 = (TextView) findViewById(R.id.alarm_title1);
			alarmdet1.setVisibility(View.VISIBLE);
			alarmdet = getSharedPreferences("GTAcampuS", MODE_PRIVATE);
			TextView alarmdet2 = (TextView) findViewById(R.id.alarm_title2);
			alarmdet2.setText(alarmdet.getString("alarmtitle", "Custom Alert"));
			TextView alarmdet3 = (TextView) findViewById(R.id.alarm_title3);
			alarmdet3.setText(details);
			View div = (View) findViewById(R.id.alertdivider);
			div.setVisibility(View.VISIBLE);	} }
		
	public void exit(View v)
	{		this.finish();	}
	
	public void about(View v)
	{		showDialog(DIALOG_ID);	}
	
	public void Calculator(View v)
	{	Intent i= new Intent(CampusActivity.this,GTAcalC.class);
		startActivity(i);	}
	
	public void slotdisp(View v)
	{	Intent slots = new Intent (CampusActivity.this,Slot.class);
		startActivity(slots);	}
	
	public void coursefn(View v)
	{	Intent courseintent=new Intent (CampusActivity.this,courses.class);
		startActivity(courseintent);	}
	
	public void coursecheck(View v)
	{	Intent check=new Intent(CampusActivity.this,CheckData.class);
		startActivity(check);	}
	
	public void notes(View v)
	{	Intent noteint=new Intent(CampusActivity.this,notedata.class);
		startActivity(noteint);	}
	
	public void bunk(View v)
	{	Intent bunkmeter=new Intent(CampusActivity.this,bunkom.class);
		startActivity(bunkmeter);	}
	
	public void gtaconvertor(View v)
	{	Intent conv=new Intent(CampusActivity.this,gtaconvertor.class);
		startActivity(conv);	}
	
	public void alarm(View v)
	{	Intent alarmintent = new Intent(CampusActivity.this,Alarmsetter.class);
		startActivity(alarmintent);			}

	
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
			}	}
		catch(Exception e){e.printStackTrace();	}
		return false;
	}
	
	public int getVer(){	int version = 0;
		try{
			version=getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			}
		catch(NameNotFoundException e){}
		return version;	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;	}
	
	public boolean onOptionsItemSelected(MenuItem menu)
	{switch(menu.getItemId())
		{
		case R.id.exit:
			this.finish();
			break;
		
		case R.id.about:
			showDialog(DIALOG_ID);
			break;
		default:			break;
			}		
		return true;	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		switch(position)
		{
		case 0:alarm(v);
				break;
		case 1:slotdisp(v);
				break;
		case 2:coursecheck(v);
				break;
		case 3:notes(v);
				break;
		case 4:bunk(v);
				break;
		case 5: gtaconvertor(v);
				break;
		case 6:Calculator(v);
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
				}
			return null;
}
}