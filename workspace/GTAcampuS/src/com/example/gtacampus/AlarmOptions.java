package com.example.gtacampus;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import com.project.gtacampus.R;

public class AlarmOptions extends Activity {
	Boolean alarmstatus=true,snooze=false,sh_snooze=true,mathsolver=false,sun=false,mon=false,tue=false,wed=false,thu=false,fri=false,sat=false;
	int snoozetimeout,hour,minute,day,month,year,itemid,alarmid;
	String AlarmTitle;
	LinearLayout enable,title,time,repeat,disnooze,snoozetout,shake,titlechanger,mathprblm;
	Button save,discard,delete;
	Intent i;
	SeekBar snoozetime;
	TextView alarmrpt,alarmrptxt,snoozesel,altitle,t,alarmtime;
	CheckBox en_alarm,dis_snooze,shake_mode,mathchck;
	EditText tit;
	final int CHANGE_TITLE=0,CHANGE_DATE=1,CHANGE_TIME=2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmoptions);
		Intent i = getIntent();
		alarmid=i.getIntExtra("alarmid", -1);
		alarmrpt=(TextView)findViewById(R.id.alarmsettings);
		alarmrptxt = (TextView)findViewById(R.id.alarm_repeat);
		repeat=(LinearLayout)findViewById(R.id.ll_repeat);
		hour=i.getIntExtra("hour", Calendar.HOUR_OF_DAY);
		minute=i.getIntExtra("minute", Calendar.MINUTE);
		itemid=i.getIntExtra("itemid", 0);
		
		if(itemid==1)
		{
			alarmrpt.setText("Alarm Date");
			day = i.getIntExtra("day", Calendar.DAY_OF_MONTH);
			month = i.getIntExtra("month", Calendar.MONTH);
			year = i.getIntExtra("year", Calendar.YEAR);
			alarmrptxt.setText(day + " / " + (month+1) + " / " + year);
			repeat.setOnClickListener(changedate);
			}
		else
		{
			repeat.setOnClickListener(rpt_handler);
			setxtinrpt(i);
		}
		alarmstatus=i.getBooleanExtra("alarmstat", true);
		en_alarm = (CheckBox)findViewById(R.id.enablealarm);
		en_alarm.setChecked(alarmstatus);
		enable = (LinearLayout)findViewById(R.id.ll_enablealarm);
		enable.setOnClickListener(alarmstatchng);
		title = (LinearLayout)findViewById(R.id.ll_alarmtitle);
		title.setOnClickListener(chngtitle);
		altitle = (TextView)findViewById(R.id.alarmtitle);
		AlarmTitle=i.getStringExtra("alarmtitle");
		if(AlarmTitle!=null)
			altitle.setText(AlarmTitle);
		time=(LinearLayout)findViewById(R.id.ll_alarmtime);
		alarmtime=(TextView)findViewById(R.id.alarmtime);
		alarmtime.setText(((hour%12==0)?12:hour%12) + " : " + minute + " " + ((hour/12 > 0)?"PM":"AM"));
		time.setOnClickListener(timechange);
		snoozesel=(TextView)findViewById(R.id.snoozeseltime);
		snoozesel.setText(i.getIntExtra("snoozetime", 5) + "mins");
		snoozetimeout=i.getIntExtra("snoozetime", 5);
		snoozetime=(SeekBar)findViewById(R.id.snoozetime);
		snoozetime.setProgress(snoozetimeout);
		snoozetime.setOnSeekBarChangeListener(snoozetimechanged);
		mathprblm=(LinearLayout) findViewById(R.id.ll_stopprblm);
		mathsolver=i.getBooleanExtra("mathsolver", false);
		mathchck = (CheckBox)findViewById(R.id.mathstop);
		mathchck.setChecked(mathsolver);
		mathprblm.setOnClickListener(en_solver);
		disnooze = (LinearLayout) findViewById(R.id.ll_disablesnooze);
		dis_snooze=(CheckBox)findViewById(R.id.disablesnooze);
		snooze=(snoozetimeout>0)?true:false;
		dis_snooze.setChecked(!snooze);
		disnooze.setOnClickListener(disabledsnooze);
		shake = (LinearLayout)findViewById(R.id.ll_shakesnooze);
		shake.setOnClickListener(snoozeshake);
		shake_mode=(CheckBox)findViewById(R.id.shakesnooze);
		sh_snooze=i.getBooleanExtra("shake_snooze", true);
		shake_mode.setChecked(sh_snooze);
		save = (Button)findViewById(R.id.alarm_save);
		save.setOnClickListener(saved);
		discard = (Button)findViewById(R.id.alarm_cancel);
		discard.setOnClickListener(cancelled);
		delete=(Button)findViewById(R.id.alarm_delete);
		delete.setOnClickListener(deleted);
	}
	
	protected View.OnClickListener saved = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent results = new Intent();
			results.putExtra("operation","save");
			results.putExtra("hour", hour);
			results.putExtra("minute", minute);
			results.putExtra("day", day);
			results.putExtra("month", month);
			results.putExtra("year", year);
			if(AlarmTitle==null)
			{
				if(itemid==0)
				AlarmTitle="Custom Alarm";
				else
				AlarmTitle="Custom Task";
			}
			results.putExtra("alarmtitle", AlarmTitle);
			results.putExtra("alarmstatus",alarmstatus);
			results.putExtra("snoozetime", snoozetimeout);
			results.putExtra("mathsolver", mathsolver);
			results.putExtra("shake_mode", sh_snooze);
			results.putExtra("sun", sun);
			results.putExtra("mon", mon);
			results.putExtra("tue", tue);
			results.putExtra("wed", wed);
			results.putExtra("thu", thu);
			results.putExtra("fri", fri);
			results.putExtra("sat", sat);
			if(itemid==0)
					{results.putExtra("type","alarm");}
			else {results.putExtra("type","task");}
			setResult(Activity.RESULT_OK, results);
			finish();
		}
	};
	
	final View.OnClickListener deleted = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent results = new Intent();
			results.putExtra("operation", "delete");
			setResult(Activity.RESULT_OK, results);
			finish();
		}
	};
	
	final View.OnClickListener cancelled = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
	};

	final View.OnClickListener alarmstatchng = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			en_alarm.setChecked(!en_alarm.isChecked());
			alarmstatus=en_alarm.isChecked();
		}
	};
	
	final View.OnClickListener en_solver = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mathchck.setChecked(!mathchck.isChecked());
			mathsolver = mathchck.isChecked();
		}
	};
	
	final View.OnClickListener snoozeshake = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			shake_mode.setChecked(!shake_mode.isChecked());
			sh_snooze = shake_mode.isChecked();
		}
	};
	
	final View.OnClickListener disabledsnooze = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dis_snooze.setChecked(!dis_snooze.isChecked());
			snooze=dis_snooze.isChecked();
			if(snooze)
				snoozetime.setProgress(0);
		}
	};
	final View.OnClickListener timechange = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(CHANGE_TIME);
		}
	};
	
	final View.OnClickListener rpt_handler = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			i = new Intent(AlarmOptions.this,AlarmRepeater.class);
			i.putExtra("Sun",sun);
			i.putExtra("Mon", mon);
			i.putExtra("Tue", tue);
			i.putExtra("Wed", wed);
			i.putExtra("Thu", thu);
			i.putExtra("Fri", fri);
			i.putExtra("Sat", sat);
			i.setAction("repeat");
			startActivityForResult(i, 1);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==Activity.RESULT_OK)
		{
			if(requestCode==1)
			setxtinrpt(data);
			if(requestCode==5)
			{
				AlarmTitle = data.getStringExtra("title");
				altitle.setText(AlarmTitle);
			}
		}
	};
	

	
	private void setxtinrpt(Intent data)
	{CharSequence rptsq= "<";
	
		if(data.getBooleanExtra("sun", true))
		{
			rptsq = rptsq+  "- Sun ";
			sun=true;}
		else sun=false;
		if(data.getBooleanExtra("mon", true)){
			rptsq = rptsq + "- Mon ";
			mon=true;}
		else mon=false;
		if(data.getBooleanExtra("tue", true)){
			rptsq = rptsq + "- Tue ";
			tue=true;}
		else tue=false;
		if(data.getBooleanExtra("wed", true)){
			rptsq = rptsq + "- Wed ";
			wed=true;}
		else wed=false;
		if(data.getBooleanExtra("thu", true)){
			rptsq = rptsq + "- Thu ";
			thu=true;}
		else thu=false;
		if(data.getBooleanExtra("fri", true)){
			rptsq = rptsq + "- Fri ";
			fri=true;}
		else fri=false;
		if(data.getBooleanExtra("sat", true)){
			rptsq = rptsq + "- Sat ";
			sat=true;}
		else sat=false;
		rptsq=rptsq+"->";
		alarmrptxt.setText(rptsq);
	}
	
	private SeekBar.OnSeekBarChangeListener snoozetimechanged = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			snooze=true;
			dis_snooze.setChecked(!snooze);
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
		  snoozesel.setText(progress + "mins");
		  snoozetimeout=progress;
		}
	};
	
	private View.OnClickListener changedate = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(CHANGE_DATE);
		}
	};
	
	TimePickerDialog.OnTimeSetListener changedtime = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int Minute) {
			// TODO Auto-generated method stub
			hour=hourOfDay;
			minute = Minute;
			alarmtime.setText(((hour%12==0)?12:hour%12) + " : " + minute + " " + ((hour/12 > 0)?"PM":"AM"));
		}
	};
	
	DatePickerDialog.OnDateSetListener changeddate = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int Year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			year=Year;
			month=monthOfYear;
			day=dayOfMonth;
			alarmrptxt.setText(day + " / " + (month+1) + " / " + year);
		}
	};
	
	private View.OnClickListener chngtitle = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		/*	titlechanger = new LinearLayout(AlarmOptions.this);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			titlechanger.setLayoutParams(params);
			t = new TextView(getBaseContext());
			t.setText("Enter the Title   ");
			tit= new EditText(AlarmOptions.this);
			tit.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
			tit.setKeyListener(TextKeyListener.getInstance());
			titlechanger.addView(t);
			titlechanger.addView(tit);
			showDialog(CHANGE_TITLE);*/
			Intent i = new Intent(getBaseContext(),AlarmRepeater.class);
			i.setAction("title");
			startActivityForResult(i, 5);
		}
	};
	
	protected final Dialog onCreateDialog(final int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id)
		{
		case CHANGE_TITLE : builder.setView(titlechanger)
		.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				AlarmTitle=tit.getText().toString();
				altitle.setText(AlarmTitle);
			}
		})
		.setCancelable(true);
		break;
		
		case CHANGE_DATE : return new DatePickerDialog(AlarmOptions.this, changeddate, year, month, day);
		
		case CHANGE_TIME : return new TimePickerDialog(AlarmOptions.this, changedtime, hour, minute, false);
		}
		
		return builder.create();
	}
}
