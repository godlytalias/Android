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

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.project.gtacampus.R;

public class alarmnotif extends Activity{
	NotificationManager alarmnotifier;
	PendingIntent alarmnotification;
	SharedPreferences alarmpref,settings;
	AudioManager alarm;
	private Boolean courseflag;
	private final int ALARM_NOTIFICATION = 0,MATH_ALARM=1,SOUND_NOTIFICATION = 3,SNOOZE_NOTIFICATION = 88;
	private int bunkc;
	private LinearLayout rootlayout;
	private KeyguardLock lock;
	private Button bt1,bt2;
	private SensorManager shakesensor;
	private Sensor shake;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		settings = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
		alarm = (AudioManager)getSystemService(AUDIO_SERVICE);
		rootlayout=(LinearLayout)findViewById(R.id.rootlayout);
		alarmpref = getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		Intent i = getIntent();
		shakesensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		shake = shakesensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		TextView altitle = (TextView)findViewById(R.id.alarmtitle);
		altitle.setText(alarmpref.getString("alarmtitle", "Custom Alert"));
		alarmnotification = PendingIntent.getActivity(getBaseContext(), 0, i, 0);
		bt1=(Button)findViewById(R.id.albutton1);
		bt2=(Button)findViewById(R.id.albutton2);
		bt2.setSoundEffectsEnabled(true);
		bt2.setOnClickListener(stopalarm);
		courseflag=false;
		if(alarmpref.getString("alarmtype", "alarm").equals("course")){
			DataManipulator db = new DataManipulator(this);
			TextView alerts = (TextView)findViewById(R.id.alerts);
			bunkc = db.getbunk(alarmpref.getString("alarmtitle", "Custom Alert"));
			alerts.setText("Bunked " + bunkc + " Class/es");
			alerts.setVisibility(View.VISIBLE);
			db.close();
			bt2.setText("BUNK");
			courseflag=true;
			Button bt3 = (Button)findViewById(R.id.albutton3);
			bt3.setVisibility(View.VISIBLE);
			bt3.setText("ON THE WAY");
			bt3.setOnClickListener(stopalarm);
			bt2.setOnClickListener(bunkit);
		}

		if(alarmpref.getInt("alarmsnooze", 5)>0)
			{bt1.setOnClickListener(snoozealarm);
		shakesensor.registerListener(shakelistener, shake, SensorManager.SENSOR_DELAY_UI);}
		KeyguardManager keyguard = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
		alarmnotifier = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		if(settings.getBoolean("notifications", true)){
		Notification alarmnotify = new Notification(R.drawable.ic_launcher, alarmpref.getString("alarmtitle", "Custom Alert"), System.currentTimeMillis());
		if(!i.getBooleanExtra("snoozingstat", false))
		{
		alarmnotify.setLatestEventInfo(getBaseContext(), "GTAcampuS", alarmpref.getString("alarmtitle", "Custom Alert"), alarmnotification);
		alarmnotify.deleteIntent = alarmnotification;
		alarmnotify.flags=Notification.FLAG_SHOW_LIGHTS | Notification.DEFAULT_VIBRATE ;
		alarmnotifier.notify("GTAcampuS", ALARM_NOTIFICATION, alarmnotify);}}
		lock = keyguard.newKeyguardLock("GTAcampuS");
}
	
	SensorEventListener shakelistener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if((event.values[0]>11.0||((event.values[1]>11.0)&&(event.values[2]>11.0))) && alarmpref.getBoolean("alarmshake", true))
			{
			snoozealarm();
			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};

	
	@Override
	public void onConfigurationChanged(Configuration config)
	{
		setContentView(rootlayout);
		super.onConfigurationChanged(config);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		lock.reenableKeyguard();
		super.onDestroy();
	}
	
	@Override
	protected void onResume(){
		lock.disableKeyguard();
		super.onResume();
	}
	
	private View.OnClickListener snoozealarm = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			snoozealarm();
		}
	};
	
	private void snoozealarm(){
		if((!courseflag)||(courseflag&&alarmpref.getLong("coursetime", 0)>System.currentTimeMillis()))
		{
		alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
		Intent i = new Intent(getBaseContext(),MyAlarm.class);
		i.setAction("snooze");
		startService(i);
		if(!getIntent().getBooleanExtra("snoozingstat", false))
			snoozeit();
		else
		{
			shakesensor.unregisterListener(shakelistener, shake);}
		finish();
		}
	}
	
	private void snoozeit()
	{
		alarmpref = getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		Notification snoozenotify = new Notification(R.drawable.ic_launcher,"Snoozing!.. " + alarmpref.getString("alarmtitle", "Custom Alert"),System.currentTimeMillis());
		snoozenotify.setLatestEventInfo(getBaseContext(), alarmpref.getString("alarmtitle", "Custom Alert"), " Snoozing for " + alarmpref.getInt("alarmsnooze", 5) + " min...", alarmnotification);
		alarmnotifier.notify("GTAcampuS",SNOOZE_NOTIFICATION, snoozenotify);
		SharedPreferences.Editor alarmdet = getSharedPreferences("GTAcampuS_alarmdet", MODE_PRIVATE).edit();
		alarmdet.putString("alarmdetails", "snoozing...");
		alarmdet.commit();
		shakesensor.unregisterListener(shakelistener, shake);
	}
	
	private void stoppingalarm(){
		Intent i = new Intent(getBaseContext(),MyAlarm.class);
		i.setAction("stop");
		startService(i);
		alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
		if(!getIntent().getBooleanExtra("snoozingstat", false))
		{
			alarmnotifier.cancel("GTAcampuS",SNOOZE_NOTIFICATION);
		}
		if(alarmpref.getString("alarmtype", "alarm").equals("task")){
		DataManipulator db = new DataManipulator(this);
		db.disablealarm(alarmpref.getInt("alarmid", -1));
		db.close();}
		finish();
	}
	
	private Runnable volup = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			int cur_vol,max_vol;
			max_vol=alarm.getStreamMaxVolume(AudioManager.STREAM_ALARM);
			if(alarm.getStreamVolume(AudioManager.STREAM_ALARM)!=1)
			{
			cur_vol=2;
			do{
				alarm.setStreamVolume(AudioManager.STREAM_ALARM, cur_vol, AudioManager.FLAG_VIBRATE);
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cur_vol = alarm.getStreamVolume(AudioManager.STREAM_ALARM)+1;
		}while(cur_vol<=max_vol && cur_vol>2);
			}}
	};
	
	private View.OnClickListener stopalarm = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			shakesensor.unregisterListener(shakelistener, shake);
			if(courseflag)
			{
				alarm.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
				alarm.setStreamVolume(AudioManager.STREAM_ALARM, 1, AudioManager.FLAG_VIBRATE);
				alarm.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,AudioManager.FLAG_SHOW_UI);
				alarm.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_PLAY_SOUND);
				alarm.setStreamVolume(AudioManager.STREAM_DTMF, 0, 0);
				alarm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				SharedPreferences.Editor settingedit = settings.edit();
				settingedit.putBoolean("coursealerts", false);
				settingedit.putBoolean("userflag", false);
				settingedit.commit();
				Intent soundsettingintent = new Intent(android.provider.Settings.ACTION_SOUND_SETTINGS);
				PendingIntent soundsettings = PendingIntent.getActivity(getBaseContext(), 0, soundsettingintent, 0);
				Notification sounds = new Notification(R.drawable.ic_launcher, "Turning off the alert volumes", System.currentTimeMillis());
				sounds.setLatestEventInfo(getBaseContext(), "Alert Volumes", "GTAcampuS turned off your alert volumes for avoiding your device making disturbances in class. Click here to set it back", soundsettings);
			    alarmnotifier.notify("GTAcampuS", SOUND_NOTIFICATION, sounds);
			    Intent sintent = new Intent(getBaseContext(),MyAlarmBrdcst.class);
			    sintent.setAction("setbacksounds");
			    PendingIntent setbacksound = PendingIntent.getBroadcast(getBaseContext(), 1, sintent, 0);
			    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			    am.set(AlarmManager.RTC_WAKEUP, alarmpref.getLong("endtime", System.currentTimeMillis()+4800), setbacksound);
			}
			if(alarmpref.getBoolean("alarmmath", false)&&(!courseflag))
			{
				Thread volume = new Thread(volup);
				 volume.start();
				showDialog(MATH_ALARM);}
			else
				stoppingalarm();
		}
	};
	
	
private View.OnClickListener bunkit = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			shakesensor.unregisterListener(shakelistener, shake);
				Thread volume = new Thread(volup);
				 volume.start();
				showDialog(MATH_ALARM);		}
	};
	
	public View makeprblm()
	{
		LinearLayout dialog = new LinearLayout(this);
		dialog.setOrientation(1);
		LinearLayout mathprblm = new LinearLayout(this);
		final int no1 = Math.round((float)Math.random()*10);
		final int no2 = Math.round((float)Math.random()*10);
		TextView num1 = new TextView(this);
		TextView opn = new TextView(this);
		TextView num2 = new TextView(this);
		TextView eq = new TextView(this);
		num1.setTextSize(30);
		opn.setTextSize(30);
		eq.setTextSize(30);
		num2.setTextSize(30);
		final EditText result = new EditText(this);
		result.setWidth(50);
		result.setTextSize(30);
		result.requestFocus();
		result.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_DATETIME_VARIATION_NORMAL);
		num1.setText(no1 + "  ");
		num2.setText(no2 + "  ");
		eq.setText(" =   ");
		opn.setText("  X    ");
		mathprblm.addView(num1);
		mathprblm.addView(opn);
		mathprblm.addView(num2);
		mathprblm.addView(eq);
		mathprblm.addView(result);
		dialog.addView(mathprblm);
		Button done = new Button(this);
		done.setText("Submit");
		dialog.addView(done);
		final Integer ans = no1*no2;
		View.OnClickListener calculate = new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(result.getText().toString().equals(ans.toString()))
				{
				if(courseflag) {
					DataManipulator db = new DataManipulator(alarmnotif.this);
					db.update(alarmpref.getString("alarmtitle", "Custom Alert"));
					db.close();
					if(settings.getBoolean("notifications", true)){
						bunkc+=1;
					Notification notifydet = new Notification (R.drawable.alert,"ALERT!!",System.currentTimeMillis());	
					NotificationManager mNotificationManager =
						    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notifydet.setLatestEventInfo(getBaseContext(), "Alert!!", "You had bunked " +bunkc+" classes of "+ alarmpref.getString("alarmtitle", "Custom Alert") , null);
					mNotificationManager.notify(10000,notifydet);}
					SharedPreferences.Editor alarmdetedit = getBaseContext().getSharedPreferences("GTAcampuS", MODE_PRIVATE).edit();
					alarmdetedit.clear();
					alarmdetedit.commit();
				}
				stoppingalarm();}
				}
		};
		done.setOnClickListener(calculate);
		return dialog;
	}
	
	protected Dialog onCreateDialog(final int id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case MATH_ALARM : 
			builder.setView(makeprblm());
			break;
		}
		
		return builder.create();
	}
}