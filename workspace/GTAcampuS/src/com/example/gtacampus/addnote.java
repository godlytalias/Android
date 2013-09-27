/*
 * GTAcampuS v1 Copyright (c) 2013 Godly T.Alias
 * 
   This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package com.example.gtacampus;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.gtacampus.R;


public class addnote extends Activity{
	static final int DIALOG_ID = 0;
	String message;
	EditText t1,t2;
	Intent i;
	private DataManipulator dh;     
	
	
	List<String> notes=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnotes);
		t1=(EditText)findViewById(R.id.editText1);
		t2=(EditText)findViewById(R.id.editText2);
		i = getIntent();
		if(i.getAction().equals("update")){
			t1.setText(i.getStringExtra("title"));
			t2.setText(i.getStringExtra("content"));
		}
	}
	
	public void save(View v)
	{
		String Text1,Text2;
		ContentValues vals = new ContentValues();
		Text1=t1.getText().toString();
		Text2=t2.getText().toString().replaceAll("'", "\'");
		vals.put("title", Text1);
		vals.put("notes", Text2);
		if(!(Text1.equals("") || Text2.equals(""))){
			this.dh = new DataManipulator(this);
		if(i.getAction().equals("add"))
			this.dh.insertnote(Text1,Text2);
		else
			this.dh.updatenote(i.getIntExtra("id", -1),vals);
		
		dh.close();
		Intent backtonotes=new Intent(this,notedata.class);
		startActivity(backtonotes);
		this.finish();}
		else
			Toast.makeText(getBaseContext(), "Enter the title and content first", Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
}

