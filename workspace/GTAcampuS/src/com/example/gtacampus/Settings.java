package com.example.gtacampus;

import com.project.gtacampus.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
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
	EditText pwd;
	SeekBar advtime,snoozetime,intervaltime;
	TextView alerttime, snoozetimetxt,intrvltime;
	int finaladvtime, finalsnoozetime;
	static final int PASSWORD=0;
	
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
			changesettings.commit();	
			resetalarm();}
		
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
				resetalarm();
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
			resetalarm();
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
	
	CompoundButton.OnCheckedChangeListener c_notifs = new CompoundButton.OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(!isChecked)
				Toast.makeText(getBaseContext(), "Turned off notifications!", Toast.LENGTH_LONG).show();
				
				changesettings.putBoolean("notifications", isChecked);
				changesettings.commit();

				resetalarm();
		}
	};
	
	CompoundButton.OnCheckedChangeListener c_alert = new CompoundButton.OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(!isChecked)
			Toast.makeText(getBaseContext(), "Turned off course alerts!", Toast.LENGTH_LONG).show();
			
			changesettings.putBoolean("coursealerts", isChecked);
			changesettings.commit();
			resetalarm();
		}
	};
	
	protected final Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case PASSWORD:
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		TextView text = new TextView(this);
		text.setText("Enter the new password    ");
		text.setClickable(false);
		text.setTextColor(getResources().getColor(android.R.color.white));
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
		
		default: break;
		}
		return builder.create();
	};

}
