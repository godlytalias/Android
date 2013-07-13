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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.project.gtacampus.R;

public class AlarmRepeater extends Activity {
	
	private Button done,cancel,settitle;
	private EditText entertitle;
	private LinearLayout all,week,sun,mon,tue,wed,thu,fri,sat;
	private CheckBox c_all,c_week,c_sun,c_mon,c_tue,c_wed,c_thu,c_fri,c_sat;
	final int REPEAT=0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		
		
		if(i.getAction().equals("repeat")){
		setContentView(R.layout.alarm_repeat);
		setTitle("Repeat Alarm");
		
		done=(Button)findViewById(R.id.donebtn);
		done.setOnClickListener(done_handler);
		cancel=(Button)findViewById(R.id.cancelbtn);
		cancel.setOnClickListener(cancel_handler);
		all=(LinearLayout)findViewById(R.id.repeat_all);
		all.setOnClickListener(sel_all);
		week=(LinearLayout)findViewById(R.id.repeat_week);
		week.setOnClickListener(sel_week);
		sun=(LinearLayout)findViewById(R.id.repeat_sunday);
		sun.setOnClickListener(sel_sun);
		mon=(LinearLayout)findViewById(R.id.repeat_monday);
		mon.setOnClickListener(sel_mon);
		tue=(LinearLayout)findViewById(R.id.repeat_tuesday);
		tue.setOnClickListener(sel_tue);
		wed=(LinearLayout)findViewById(R.id.repeat_wedday);
		wed.setOnClickListener(sel_wed);
		thu=(LinearLayout)findViewById(R.id.repeat_thursday);
		thu.setOnClickListener(sel_thu);
		fri=(LinearLayout)findViewById(R.id.repeat_friday);
		fri.setOnClickListener(sel_fri);
		sat=(LinearLayout)findViewById(R.id.repeat_saturday);
		sat.setOnClickListener(sel_sat); 
		
		
		c_sun=(CheckBox)findViewById(R.id.sun_check);
		c_sun.setChecked(i.getBooleanExtra("Sun", false));
		c_mon=(CheckBox)findViewById(R.id.mon_check);
		c_mon.setChecked(i.getBooleanExtra("Mon", false));
		c_tue=(CheckBox)findViewById(R.id.tue_check);
		c_tue.setChecked(i.getBooleanExtra("Tue", false));
		c_wed=(CheckBox)findViewById(R.id.wed_check);
		c_wed.setChecked(i.getBooleanExtra("Wed", false));
		c_thu=(CheckBox)findViewById(R.id.thu_check);
		c_thu.setChecked(i.getBooleanExtra("Thu", false));
		c_fri=(CheckBox)findViewById(R.id.fri_check);
		c_fri.setChecked(i.getBooleanExtra("Fri", false));
		c_sat=(CheckBox)findViewById(R.id.sat_check);
		c_sat.setChecked(i.getBooleanExtra("Sat", false));
		c_all=(CheckBox)findViewById(R.id.all_check);
		c_all.setChecked(c_sun.isChecked() && c_mon.isChecked() && c_tue.isChecked() && c_wed.isChecked() && c_thu.isChecked() && c_fri.isChecked() && c_sat.isChecked());
		c_week=(CheckBox)findViewById(R.id.week_check);
		c_week.setChecked((!c_sun.isChecked()) && c_mon.isChecked() && c_tue.isChecked() && c_wed.isChecked() && c_thu.isChecked() && c_fri.isChecked() && !c_sat.isChecked());
		

		
	}
		else if(i.getAction().equals("title"))
		{
			setContentView(R.layout.addtitle);
			entertitle = (EditText) findViewById(R.id.getalarmtitle);
			entertitle.setInputType(InputType.TYPE_CLASS_TEXT);
			settitle = (Button) findViewById(R.id.settitle);
			settitle.setOnClickListener(changingtitle);
		}
	};
	
	final View.OnClickListener changingtitle = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent data = new Intent();
			data.putExtra("title", entertitle.getText().toString());
			setResult(Activity.RESULT_OK, data);
			finish();
		}
	};

	final View.OnClickListener done_handler = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent repeat = new Intent();
			repeat.putExtra("sun", c_sun.isChecked());
			repeat.putExtra("mon", c_mon.isChecked());
			repeat.putExtra("tue", c_tue.isChecked());
			repeat.putExtra("wed", c_wed.isChecked());
			repeat.putExtra("thu", c_thu.isChecked());
			repeat.putExtra("fri", c_fri.isChecked());
			repeat.putExtra("sat", c_sat.isChecked());
			if(c_sun.isChecked() || c_mon.isChecked() || c_tue.isChecked() || c_wed.isChecked() || c_thu.isChecked() || c_fri.isChecked() || c_sat.isChecked())
			{setResult(Activity.RESULT_OK, repeat);
			finish();}
			else
				showDialog(REPEAT);
		}
		
		
	};
	
	final View.OnClickListener cancel_handler = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
	};
	
	final View.OnClickListener sel_all = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_all.setChecked(!c_all.isChecked());
			c_sun.setChecked(c_all.isChecked());
			c_mon.setChecked(c_all.isChecked());
			c_tue.setChecked(c_all.isChecked());
			c_wed.setChecked(c_all.isChecked());
			c_thu.setChecked(c_all.isChecked());
			c_fri.setChecked(c_all.isChecked());
			c_sat.setChecked(c_all.isChecked());
		}
	};
	
	final View.OnClickListener sel_week = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_week.setChecked(!c_week.isChecked());
			c_all.setChecked(false);
			c_sun.setChecked(false);
			c_sat.setChecked(false);
			c_mon.setChecked(c_week.isChecked());
			c_tue.setChecked(c_week.isChecked());
			c_wed.setChecked(c_week.isChecked());
			c_thu.setChecked(c_week.isChecked());
			c_fri.setChecked(c_week.isChecked());
		}
	};
	
	final View.OnClickListener sel_sun = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_sun.setChecked(!c_sun.isChecked());
			c_week.setChecked(false);
			c_all.setChecked(false);
		}
	};
	
final View.OnClickListener sel_mon = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_mon.setChecked(!c_mon.isChecked());
			c_week.setChecked(false);
			c_all.setChecked(false);
		}
	};
	
	
final View.OnClickListener sel_tue = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_tue.setChecked(!c_tue.isChecked());
			c_week.setChecked(false);
			c_all.setChecked(false);
		}
	};
	
final View.OnClickListener sel_wed = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_wed.setChecked(!c_wed.isChecked());
			c_week.setChecked(false);
			c_all.setChecked(false);
		}
	};
	
final View.OnClickListener sel_thu = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_thu.setChecked(!c_thu.isChecked());
			c_week.setChecked(false);
			c_all.setChecked(false);
		}
	};
	
	
final View.OnClickListener sel_fri = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_fri.setChecked(!c_fri.isChecked());
			c_week.setChecked(false);
			c_all.setChecked(false);
		}
	};
	
final View.OnClickListener sel_sat = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			c_sat.setChecked(!c_sat.isChecked());
			c_week.setChecked(false);
			c_all.setChecked(false);
		}
	};
	
	protected final Dialog onCreateDialog(final int id){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		switch(id)
		{
		case REPEAT :
			alert.setMessage("Atleast one day should be\nselected for an alarm")
			.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				   dialog.dismiss();
				}
			} )
			.setCancelable(false);
		}
		return alert.create();
	}
}
