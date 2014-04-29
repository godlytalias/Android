package com.camp.counter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class tallyclass extends Activity {
	
	LinearLayout counter;
	TextView reading;
	int count_reg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tallylayout);
		reading = new TextView(this);
		reading = (TextView) findViewById(R.id.reading);
		init();
		Button done = new Button(this);
		done = (Button) findViewById(R.id.donecount);
		done.setOnClickListener(donecount);
	}
	
	private void init(){
		counter = new LinearLayout(this);
		counter = (LinearLayout) findViewById(R.id.counter);
		reading.setText("0");
		count_reg=0;
		database db = new database(this);
		
		Cursor options = db.getoption();
		options.moveToFirst();
		
		LayoutParams text = new LayoutParams(LayoutParams.WRAP_CONTENT, 80, 4);
		LayoutParams buttons = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		
		while(!options.isAfterLast()){
			LinearLayout counterviews = new LinearLayout(this);
			counterviews.setOrientation(LinearLayout.HORIZONTAL);
			counterviews.setPadding(1, 12, 1, 12);
			
			TextView opt = new TextView(this);
			opt.setText(options.getString(0));
			opt.setLayoutParams(text);
			opt.setEms(10);
			opt.setGravity(Gravity.CENTER);
			
			Button subtract = new Button(tallyclass.this);
			subtract.setText("-");
			subtract.setTag(options.getString(0));
			subtract.setOnClickListener(sub);
			subtract.setLayoutParams(buttons);
			
			Button add = new Button(tallyclass.this);
			add.setTag(options.getString(0));
			add.setText("+");
			add.setOnClickListener(addn);
			add.setLayoutParams(buttons);
			
			counterviews.addView(subtract);
			counterviews.addView(opt);
			counterviews.addView(add);
			counter.addView(counterviews);
			options.moveToNext();
		}
		
		db.close();
		
	}
	
	View.OnClickListener donecount = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		if(count_reg>=1){
			Intent analytics = new Intent(tallyclass.this,analytics.class);
			analytics.putExtra("total", count_reg);
			startActivity(analytics);
			tallyclass.this.finish();
		}
		else
			Toast.makeText(getBaseContext(),"No entries",Toast.LENGTH_SHORT).show();
		}
	};
	
	View.OnClickListener sub = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			database db = new database(tallyclass.this);
			if(!(db.getvalue(v.getTag().toString()).equals("0")))
			{
			db.subtract(v.getTag().toString());
			db.close();
			count_reg-=1;
			reading.setText(count_reg+"");
			}
			else
				Toast.makeText(getBaseContext(), "Values can't be negative!", Toast.LENGTH_SHORT).show();
		}
	};

	View.OnClickListener addn = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			database db = new database(tallyclass.this);
			db.add(v.getTag().toString());
			db.close();
			count_reg+=1;
			reading.setText(count_reg+"");
		}
	};
	
}
