package com.example.gtacampus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
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
		this.finish();
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
			DataManipulator db = new DataManipulator(Initialize.this);
			db.coursetableinit(no);
			db.coursetimings(start, no);
			db.coursetimings(end, no);
			db.initdays(no);
			db.close();
			finish();
		}
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
		   nexthour.setOnClickListener(gotonexthour);
	}
};
    	
	};