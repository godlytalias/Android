package com.example.gtacampus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class alarmnotif extends Activity{
	NotificationManager alarmnotifier;
	PendingIntent alarmnotification;
	private int ALARM_NOTIFICATION = 0;
	private int SNOOZE_NOTIFICATION = 1;
	private LinearLayout rootlayout;
	private KeyguardLock lock;
	private Button bt1,bt2;
	private SensorManager shakesensor;
	private Sensor shake;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		rootlayout=(LinearLayout)findViewById(R.id.rootlayout);
		setContentView(R.layout.alarm);
		Intent i = getIntent();
		shakesensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		shake = shakesensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		shakesensor.registerListener(shakelistener, shake, SensorManager.SENSOR_DELAY_UI);
		alarmnotification = PendingIntent.getActivity(getBaseContext(), 0, i, 0);
		bt1=(Button)findViewById(R.id.albutton1);
		bt2=(Button)findViewById(R.id.albutton2);
		bt2.setSoundEffectsEnabled(true);
		bt1.setOnClickListener(snoozealarm);
		bt2.setOnClickListener(stopalarm);
		KeyguardManager keyguard = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
		alarmnotifier = (NotificationManager)getSystemService(getBaseContext().NOTIFICATION_SERVICE);
		Notification alarmnotify = new Notification(R.drawable.ic_launcher, getString(R.string.alarm_title), System.currentTimeMillis());
		if(!i.getBooleanExtra("snoozingstat", false))
		{
		alarmnotify.setLatestEventInfo(getBaseContext(), "GTAcampuS", getText(R.string.alarm_title), alarmnotification);
		alarmnotify.deleteIntent = alarmnotification;
		alarmnotify.flags=Notification.FLAG_SHOW_LIGHTS | Notification.DEFAULT_VIBRATE ;
		alarmnotifier.notify("GTAcampuS", ALARM_NOTIFICATION, alarmnotify);}
		lock = keyguard.newKeyguardLock("GTAcampuS");
		
}
	
	SensorEventListener shakelistener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if(event.values[0]>10.0)
			{
				Intent i = new Intent(getBaseContext(),MyAlarm.class);
				i.setAction("snooze");
				startService(i);
				alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
				if(!getIntent().getBooleanExtra("snoozingstat", false))
				{
					Notification snoozenotify = new Notification(R.drawable.ic_launcher,getString(R.string.alarm_title),System.currentTimeMillis());
					snoozenotify.setLatestEventInfo(getBaseContext(), getString(R.string.alarm_title), "snoozing...", alarmnotification);
					alarmnotifier.notify("GTACampuS",SNOOZE_NOTIFICATION, snoozenotify);
				}
				finish();
			}
		}
		
		@Override
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
		lock.reenableKeyguard();
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		lock.disableKeyguard();
		super.onResume();
	}
	
	private View.OnClickListener snoozealarm = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(getBaseContext(),MyAlarm.class);
			i.setAction("snooze");
			startService(i);
			alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
			if(!getIntent().getBooleanExtra("snoozingstat", false))
			{
				Notification snoozenotify = new Notification(R.drawable.ic_launcher,getString(R.string.alarm_title),System.currentTimeMillis());
				snoozenotify.setLatestEventInfo(getBaseContext(), getString(R.string.alarm_title), "snoozing...", alarmnotification);
				alarmnotifier.notify("GTACampuS",SNOOZE_NOTIFICATION, snoozenotify);
			}
			finish();
		}
	};
	
	private View.OnClickListener stopalarm = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(getBaseContext(),MyAlarm.class);
			i.setAction("stop");
			startService(i);
			alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
			if(!getIntent().getBooleanExtra("snoozingstat", false))
			{
				alarmnotifier.cancel("GTACampuS",SNOOZE_NOTIFICATION);
			}
			finish();
		}
	};
	
	
}