package com.camp.counter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class analytics extends Activity {
	
	LinearLayout counter1,counter2,counter3;
	TextView total_read;
	int tot;
	float total;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analysis);
		Intent analytics = getIntent();
		total_read = new TextView(this);
		total_read = (TextView) findViewById(R.id.total);
		total = analytics.getIntExtra("total", 0);
		tot = analytics.getIntExtra("total", 0);
		total_read.setText(tot+"");
		init();
		Button restart = new Button(this);
		restart = (Button) findViewById(R.id.restart);
		restart.setOnClickListener(newcalc);
		Button exit = new Button(this);
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(exitpgm);
	}
	
	View.OnClickListener exitpgm = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			analytics.this.finish();
		}
	};
	
	View.OnClickListener newcalc = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent calc = new Intent(analytics.this,MainActivity.class);
			startActivity(calc);
			analytics.this.finish();
		}
	};
	
	private void init(){
		counter1 = new LinearLayout(this);
		counter1 = (LinearLayout) findViewById(R.id.list1);
		counter2 = new LinearLayout(this);
		counter2 = (LinearLayout) findViewById(R.id.list2);
		counter3 = new LinearLayout(this);
		counter3 = (LinearLayout) findViewById(R.id.list3);
		database db = new database(this);
		
		TextView qn = new TextView(analytics.this);
		qn = (TextView) findViewById(R.id.question);
		qn.setText(db.getquestion());
		
		Cursor options = db.getoption();
		Cursor counts = db.getvalues();
		options.moveToFirst();
		counts.moveToFirst();
						
		while(!options.isAfterLast()){
			TextView t1 = new TextView(analytics.this);
			t1.setText(options.getString(0));
			t1.setGravity(Gravity.CENTER);
			counter1.addView(t1);
			TextView t2 = new TextView(analytics.this);
			t2.setText(counts.getString(0));
			t2.setGravity(Gravity.CENTER);
			counter2.addView(t2);
			TextView t3 = new TextView(analytics.this);
			t3.setText(((Integer.parseInt(counts.getString(0))/total)*100)+" %");
			t3.setGravity(Gravity.CENTER);
			counter3.addView(t3);
			options.moveToNext();
			counts.moveToNext();
		}
		
		db.close();
		
	}

}
