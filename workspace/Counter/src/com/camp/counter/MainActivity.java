package com.camp.counter;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.Menu;
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
		database db = new database(this);
		db.clear_table();
		db.close();
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
			i++;
			ContentValues answers = new ContentValues();
			database db = new database(MainActivity.this);
			TextView addedoption = new TextView(MainActivity.this);
			addedoption.setText(i+". "+option.getText().toString());
			answers.put("answers", option.getText().toString());
			answers.put("count", 0);
			db.setanswers(answers);
			option.setText("");
			options.addView(addedoption);
			db.close();}
		else
			Toast.makeText(getBaseContext(), "Blank field", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
