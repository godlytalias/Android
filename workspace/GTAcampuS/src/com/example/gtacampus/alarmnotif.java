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
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.renderscript.Type;
import android.text.Editable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class alarmnotif extends Activity{
	NotificationManager alarmnotifier;
	PendingIntent alarmnotification;
	SharedPreferences alarmpref;
	private final int ALARM_NOTIFICATION = 0,MATH_ALARM=1;
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
		alarmpref = getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		Intent i = getIntent();
		shakesensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		shake = shakesensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		shakesensor.registerListener(shakelistener, shake, SensorManager.SENSOR_DELAY_UI);
		TextView altitle = (TextView)findViewById(R.id.alarmtitle);
		altitle.setText(alarmpref.getString("alarmtitle", "Custom Alert"));
		alarmnotification = PendingIntent.getActivity(getBaseContext(), 0, i, 0);
		bt1=(Button)findViewById(R.id.albutton1);
		bt2=(Button)findViewById(R.id.albutton2);
		bt2.setSoundEffectsEnabled(true);
		if(alarmpref.getInt("alarmsnooze", 5)>0)
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
			if(event.values[0]>12.0 && alarmpref.getBoolean("alarmshake", true))
			{
				Intent i = new Intent(getBaseContext(),MyAlarm.class);
				i.setAction("snooze");
				startService(i);
				alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
				if(!getIntent().getBooleanExtra("snoozingstat", false))
				snoozeit();
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
			snoozeit();
			finish();
		}
	};
	
	private void snoozeit()
	{
		Notification snoozenotify = new Notification(R.drawable.ic_launcher,getString(R.string.alarm_title),System.currentTimeMillis());
		snoozenotify.setLatestEventInfo(getBaseContext(), getString(R.string.alarm_title), "snoozing...", alarmnotification);
		alarmnotifier.notify("GTACampuS",SNOOZE_NOTIFICATION, snoozenotify);
	}
	
	private void stoppingalarm(){
		Intent i = new Intent(getBaseContext(),MyAlarm.class);
		i.setAction("stop");
		startService(i);
		alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
		if(!getIntent().getBooleanExtra("snoozingstat", false))
		{
			alarmnotifier.cancel("GTACampuS",SNOOZE_NOTIFICATION);
		}
		DataManipulator db = new DataManipulator(this);
		db.disablealarm(alarmpref.getInt("alarmid", -1));
		finish();
	}
	
	private View.OnClickListener stopalarm = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(alarmpref.getBoolean("alarmmath", false))
				showDialog(MATH_ALARM);
			else
				stoppingalarm();
		}
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
		result.setTextSize(30);
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
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(result.getText().toString().equals(ans.toString()))
				stoppingalarm();
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