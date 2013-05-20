package com.example.gtacampus;

import java.sql.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class CampusActivity extends Activity {
	
	private static final int DIALOG_ID = 0;
	MediaPlayer mediaplayer;
	private Thread splashscreen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	/*	 mediaplayer= MediaPlayer.create(this, R.raw.sundaychurch);
		 mediaplayer.start();
		while(mediaplayer.getCurrentPosition()<1600)
		{
			
		}
			mediaplayer.stop();
			mediaplayer.release();*/
		
		splashscreen = new Thread() {
		@Override
		public void run()
		{
			try{
				synchronized(this){
					
					wait(3000);
				}
			}
			catch(Exception e){}
			finally{
			finish();
			}
		}
		};
		setContentView(R.layout.activity_gtacal_c);
		splashscreen.start();
		setContentView(R.layout.activity_campus);
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
			System.exit(0);
			break;
		
		case R.id.about:
			showDialog(DIALOG_ID);
			break;
		default:			break;
			}
		
		return true;
	}
	
	
	protected final	Dialog onCreateDialog(final int id){
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
}

