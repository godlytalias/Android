package com.example.gtacampus;

import java.sql.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class CampusActivity extends Activity {
	
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
	

}
