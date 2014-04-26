package com.camp.counter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class analytics extends Activity {
	
	TableLayout tallytable;
	TextView total_read;
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
		total_read.setText((int)total+"");
		tallytable = new TableLayout(this);
		tallytable = (TableLayout) findViewById(R.id.tallytable);
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
		
		database db = new database(this);
		android.widget.TableRow.LayoutParams rowparams = new android.widget.TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
		android.widget.TableRow.LayoutParams params = new android.widget.TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1);
		
		TextView qn = new TextView(analytics.this);
		qn = (TextView) findViewById(R.id.question);
		qn.setText(db.getquestion());
		
		Cursor options = db.getoption();
		Cursor counts = db.getvalues();
		options.moveToFirst();
		counts.moveToFirst();
						
		while(!options.isAfterLast()){
			TableRow row = new TableRow(analytics.this);
			row.setLayoutParams(rowparams);
			row.setGravity(Gravity.CENTER);
			
			TextView t1 = new TextView(analytics.this);
			t1.setText(options.getString(0));
			t1.setGravity(Gravity.CENTER);
			t1.setEms(15);
			t1.setLayoutParams(params);

			TextView t2 = new TextView(analytics.this);
			t2.setText(counts.getString(0));
			t2.setGravity(Gravity.CENTER);
			t2.setLayoutParams(params);
			t2.setEms(15);

			TextView t3 = new TextView(analytics.this);
			t3.setText(((Integer.parseInt(counts.getString(0))/total)*100)+" %");
			t3.setGravity(Gravity.CENTER);
			t3.setLayoutParams(params);
			t3.setEms(15);

			row.addView(t1);
			row.addView(t2);
			row.addView(t3);
			tallytable.addView(row);
			options.moveToNext();
			counts.moveToNext();
		}
		
		db.close();
		
	}
	
}
