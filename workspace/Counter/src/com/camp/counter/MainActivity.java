package com.camp.counter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	LinearLayout options;
	EditText option,question;
	int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		i=0;
		database db = new database(this);
		db.clear_table();
		db.close();
		options = new LinearLayout(this);
		options = (LinearLayout)findViewById(R.id.options);
		option = new EditText(this);
		Button addopt = new Button(this);
		question = new EditText(this);
		question = (EditText) findViewById(R.id.question);
		option=(EditText)findViewById(R.id.opt);
		Button done = new Button(this);
		done = (Button) findViewById(R.id.donebutton);
		done.setOnClickListener(doneadd);
		addopt = (Button) findViewById(R.id.addopt);
		addopt.setOnClickListener(addoption);
	}
	
	View.OnClickListener doneadd = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		if(i>=2 && !(question.getEditableText().toString().equals(""))){
			database db = new database(MainActivity.this);
			db.setquestion(question.getEditableText().toString());
			db.close();
			Intent tally = new Intent(MainActivity.this,tallyclass.class);
			startActivity(tally);	
			MainActivity.this.finish();}
		else if(i<2){
			Toast.makeText(MainActivity.this, "Enter atleast two options", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(getBaseContext(), "Enter a question", Toast.LENGTH_SHORT).show();
		}
	};
	
    View.OnClickListener addoption = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		if(!option.getText().toString().equals("")){
			database db = new database(MainActivity.this);
			ContentValues answers = new ContentValues();
			answers.put("answers", option.getText().toString());
			answers.put("count", 0);
			if(db.setanswers(answers)>=0)
			{
				i++;
				TextView addedoption = new TextView(MainActivity.this);
				addedoption.setText(i+". "+option.getText().toString());
				option.setText("");
				options.addView(addedoption);}
			else
				Toast.makeText(getBaseContext(), "Option already entered", Toast.LENGTH_SHORT).show();
			db.close();
		}
		else
			Toast.makeText(getBaseContext(), "Blank fields are not allowed", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, Menu.FIRST+1, 0, "Exit");
		menu.add(0,Menu.FIRST+2,1,"About Application");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case Menu.FIRST+1 : this.finish();
							break;
		case Menu.FIRST+2 : showDialog(0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected final	Dialog onCreateDialog(final int id){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		TextView txt = new TextView(MainActivity.this);
		txt.setText("Counter Application\nCopyright © 2014 Davis Abraham\n\nThis program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, version 3 of the License.");
		txt.setGravity(Gravity.CENTER);
		
		switch(id){
		case 0:
		builder.setView(txt)
		.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(0);
			}
		});
		break;
		};
		return builder.create();
	}

}
