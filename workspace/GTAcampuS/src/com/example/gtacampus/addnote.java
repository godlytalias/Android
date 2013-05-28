package com.example.gtacampus;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class addnote extends Activity{
	static final int DIALOG_ID = 0;
	String message;
	
	private DataManipulator dh;     
	
	
	List<String> notes=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnotes);
	
	}
	
	public void save(View v)
	{
		String Text1,Text2;
		EditText t1,t2;
		t1=(EditText)findViewById(R.id.editText1);
		Text1=t1.getText().toString();
		t2=(EditText)findViewById(R.id.editText2);
		Text2=t2.getText().toString();
		this.dh = new DataManipulator(this);
		this.dh.insertnote(Text1,Text2);
		
		Intent backtonotes=new Intent(this,notedata.class);
		startActivity(backtonotes);
		this.finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
	
}

