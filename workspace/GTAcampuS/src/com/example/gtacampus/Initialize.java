package com.example.gtacampus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

public class Initialize extends Activity{

	Spinner coursehour,classtimings1,classtimings2;
	LinearLayout coursetimingmain;
	TextView timeseltext;
	Button nexthour;
	int no,i,temp;
	String start[],end[];

	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.init_app);
		Toast.makeText(getBaseContext(), "WELCOME!..THANKS FOR CHOOSING GTAcampuS", Toast.LENGTH_LONG).show();
		coursehour = (Spinner)findViewById(R.id.classhours);
		coursehour.setSelection(6);
		coursetimingmain = (LinearLayout) findViewById(R.id.ll_coursetimingmain);
		Button next = (Button)findViewById(R.id.next);
		next.setOnClickListener(gonext);
		i=1;
	}
	
	public void skip(View v)
	{		
		start=new String[10];
		end = new String[10];
		no=9;
		start[1]="8:00 AM"; end[1] = "9:00 AM";
		start[2]="9:00 AM"; end[2] = "10:00 AM";
		start[3]="10:15 AM"; end[3] = "11:15 AM";
		start[4]="11:15 AM"; end[4] = "12:15 PM";
		start[5]="1:00 PM"; end[5] = "2:00 PM";
		start[6]="2:00 PM"; end[6] = "3:00 PM";
		start[7]="3:00 PM"; end[7] = "4:00 PM";
		start[8]="4:00 PM"; end[8] = "5:00 PM";
		start[9]="5:00 PM"; end[9] = "6:00 PM";
		initialize_db();
	}
	
	View.OnClickListener gotonexthour = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		start[i]=classtimings1.getSelectedItem().toString();
		end[i]=classtimings2.getSelectedItem().toString();
		temp=classtimings2.getSelectedItemPosition();
		i++;
		if(i<=no)
		{
			switch(i)
			{
			case 2:timeseltext.setText("2ND");
					break;
			case 3:timeseltext.setText("3RD");
					break;
			default:timeseltext.setText(i+"TH");
					break;
			}
			classtimings1.setSelection(temp);
			classtimings2.setSelection(temp+4);
		}
		else
		{
			initialize_db();
		}
		}
	};
	
	protected void initialize_db(){
		DataManipulator db = new DataManipulator(Initialize.this);
		db.coursetableinit(no);
		db.coursetimings(start, no);
		db.coursetimings(end, no);
		db.initdays(no);
		db.close();
		finish();
	}
	
	AdapterView.OnItemSelectedListener timeselected = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			classtimings2.setSelection(arg2+4);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
  View.OnClickListener gonext = new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		no = coursehour.getSelectedItemPosition()+1;
		start=new String[no+1];
		end=new String[no+1];
	    coursetimingmain.removeView(findViewById(R.id.textView1));
	    coursetimingmain.removeView(findViewById(R.id.ll_alarmtiming1));
	    coursetimingmain.removeView(findViewById(R.id.ll_alarmtiming2));
	    LinearLayout classtimings = (LinearLayout)findViewById(R.id.ll_classtimings);
	   classtimings.setVisibility(View.VISIBLE);
	   nexthour=(Button)findViewById(R.id.timingdone);
	   timeseltext=(TextView)findViewById(R.id.timeseltext);
	   timeseltext.setText("1ST");	   
		classtimings1 = (Spinner)findViewById(R.id.classtimings1);
		classtimings2 = (Spinner)findViewById(R.id.classtimings2);
		classtimings1.setOnItemSelectedListener(timeselected);
		   nexthour.setOnClickListener(gotonexthour);
	}};};