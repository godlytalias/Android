/*
 * GTAcampuS v1 Copyright (c) 2013 Godly T.Alias
 * 
   This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package com.example.gtacampus;

import com.project.gtacampus.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Settings extends Activity {
	
	SharedPreferences settings;
	SharedPreferences.Editor changesettings;
	EditText pwd,path;
	String addr;
	SeekBar advtime,snoozetime,intervaltime;
	TextView alerttime, snoozetimetxt,intrvltime;
	int finaladvtime, finalsnoozetime;
	static final int PASSWORD=0,SERVER=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appsettings);
		settings = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
		changesettings = settings.edit();
		ToggleButton coursealerts = (ToggleButton)findViewById(R.id.coursealerts);
		coursealerts.setChecked(settings.getBoolean("coursealerts", true));
		coursealerts.setOnCheckedChangeListener(c_alert);
		ToggleButton notifications = (ToggleButton) findViewById(R.id.notifications);
		notifications.setChecked(settings.getBoolean("notifications", true));
		notifications.setOnCheckedChangeListener(c_notifs);
		LinearLayout setpwd = (LinearLayout) findViewById(R.id.ll_masterpwd);
		setpwd.setOnClickListener(chngpwd);
		LinearLayout serv = (LinearLayout) findViewById(R.id.ll_serverconfig);
		serv.setOnClickListener(configserv);
		advtime = (SeekBar) findViewById(R.id.courseadvtime);
		advtime.setOnSeekBarChangeListener(setadvtime);
		snoozetime = (SeekBar) findViewById(R.id.coursesnoozetime);
		snoozetime.setOnSeekBarChangeListener(chngsnoozetime);
		alerttime = (TextView) findViewById(R.id.coursealerttime);
		snoozetimetxt = (TextView) findViewById(R.id.coursesnoozetimesel);
		advtime.setProgress(settings.getInt("alerttime", 10)-5);
		snoozetime.setProgress(settings.getInt("snoozetime", 3)-1);
		intervaltime = (SeekBar) findViewById(R.id.seekintervaltime);
		intervaltime.setOnSeekBarChangeListener(setintrvtime);
		intrvltime = (TextView) findViewById(R.id.intervaltime);
		intervaltime.setProgress(settings.getInt("interval", 5));
	}

	
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
       resetalarm();
       startActivity(new Intent(Settings.this,CampusActivity.class));
       super.onStop();
	}
	
	
	private void resetalarm(){
		Intent setalarm = new Intent(Settings.this,MyAlarm.class);
		setalarm.setAction("setalarm");
		startService(setalarm);
	}
	
	SeekBar.OnSeekBarChangeListener setintrvtime = new SeekBar.OnSeekBarChangeListener() {
		int prg=0;
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			changesettings.putInt("interval", prg);
			changesettings.commit();}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			intrvltime.setText(progress+" mins");
			prg=progress;
		}
	};
	
	SeekBar.OnSeekBarChangeListener chngsnoozetime = new SeekBar.OnSeekBarChangeListener() {
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			if(finalsnoozetime>finaladvtime){
				advtime.setProgress(finalsnoozetime-5);
			}
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
						progress+=1;
						finalsnoozetime=progress;
						snoozetimetxt.setText(progress+" mins");
						changesettings.putInt("snoozetime", finalsnoozetime);
						changesettings.commit();
						
		}
	};
	
	SeekBar.OnSeekBarChangeListener setadvtime = new SeekBar.OnSeekBarChangeListener() {
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			if(finaladvtime<finalsnoozetime){
				snoozetime.setProgress(finaladvtime-1);
			}
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			}
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			progress+=5;
			finaladvtime = progress;
			alerttime.setText(progress+ " mins");		
			changesettings.putInt("alerttime", finaladvtime);
			changesettings.commit();
		}
	};
	
	View.OnClickListener chngpwd = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(PASSWORD);
		}
	};
	
	
	View.OnClickListener configserv = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(SERVER);
		}
	};
	
	CompoundButton.OnCheckedChangeListener c_notifs = new CompoundButton.OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(!isChecked)
				Toast.makeText(getBaseContext(), "Turned off notifications!", Toast.LENGTH_LONG).show();
				
				changesettings.putBoolean("notifications", isChecked);
				changesettings.commit();
		}
	};
	
	CompoundButton.OnCheckedChangeListener c_alert = new CompoundButton.OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(!isChecked)
			Toast.makeText(getBaseContext(), "Turned off course alerts!", Toast.LENGTH_LONG).show();
			
			changesettings.putBoolean("coursealerts", isChecked);
			changesettings.commit();
		}
	};
	
	protected final Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		TextView text = new TextView(this);
		text.setClickable(false);
		text.setPadding(2, 2, 2, 2);
		text.setTextColor(getResources().getColor(android.R.color.white));
		
		switch(id){
		case PASSWORD:		
		text.setText("Enter the new password    ");
		pwd = new EditText(this);
		pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		pwd.setEms(15);
		pwd.setFocusable(true);
		pwd.setTextColor(getResources().getColor(android.R.color.darker_gray));
		ll.addView(text);
		ll.addView(pwd);
		builder.setView(ll)
		.setCancelable(false)
		.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
				SharedPreferences.Editor settingseditor = settings.edit();
				settingseditor.putString("Password", pwd.getText().toString());
				settingseditor.commit();
				removeDialog(PASSWORD);
			}
		});
		
		break;
		
		case SERVER:
			
			settings = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
			text.setText("Enter the address where you hosted the web-services ");
			path = new EditText(this);
			if(settings.getString("server", "NULL").equals("NULL"))
			path.setText("http://");
			else
			path.setText(settings.getString("server", "http://"));
			path.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
			path.setFocusable(true);
			addr = new String();
			ll.addView(text);
			ll.addView(path);
			builder.setView(ll)
			.setCancelable(false)
			.setPositiveButton("Set", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor settingseditor = settings.edit();
					addr = path.getText().toString().trim();
					if (addr.endsWith("/")) 
						settingseditor.putString("server",addr );
						else settingseditor.putString("server", addr+"/");
					settingseditor.commit();
					removeDialog(SERVER);
				}
			});
			
			break;
		
		
		default: break;
		}
		return builder.create();
	};

}
