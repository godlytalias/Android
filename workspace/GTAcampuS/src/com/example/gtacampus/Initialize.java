package com.example.gtacampus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class Initialize extends Activity{
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.init_app);
		Spinner coursehour = (Spinner)findViewById(R.id.classhours);
		coursehour.setSelection(6);
		coursehour.setOnItemSelectedListener(hourno);
	}
	
    OnItemSelectedListener hourno = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
	/*		
			LinearLayout initialize = (LinearLayout) findViewById(R.id.initialize);
			int i;
			for(i=0;i<=arg2;i++){
				TextView godly = new TextView(Initialize.this);
				godly.setText("godly");
				initialize.addView(godly);
			}*/
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    	
	};
}