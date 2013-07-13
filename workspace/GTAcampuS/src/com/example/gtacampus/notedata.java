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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.project.gtacampus.R;


public class notedata extends ListActivity{
	static final int DELETE_NOTE=1,NOTE_OPTIONS=2;
	Integer selid;
	List<String> slots=new ArrayList<String>();
	String message,title;
	TextView selection;
	public int idToModify; 
	DataManipulator dm;
	Bundle state;

	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] stg1,stg2;
	Integer[] ids;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = savedInstanceState;
		setContentView(R.layout.notes);
		ListView notelist = (ListView) findViewById(android.R.id.list);
		notelist.setOnItemLongClickListener(deleter);
				  dm = new DataManipulator(this);
		      names2 = dm.selectAllnotes();
		      dm.close();
		      stg1=new String[names2.size()]; 
		      stg2=new String[names2.size()];
		      ids = new Integer[names2.size()];
				int x=0;

				for (String[] note : names2) {
					
					stg2[x]=note[2];
					stg1[x]=note[1];
					ids[x]=Integer.parseInt(note[0]);
					x++;
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
						this,android.R.layout.simple_list_item_1,   
						stg1);
		        this.setListAdapter(adapter);
	}
	
	public void add(View v)
	{
		Intent noteintent= new Intent(this,addnote.class);
		noteintent.setAction("add");
		addnote(noteintent);
	}
	
	private void addnote(Intent noteintent){
		startActivity(noteintent);
		this.finish();
	}
	
	AdapterView.OnItemLongClickListener deleter = new OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
				message = stg2[arg2];
				title = stg1[arg2];
				selid = ids[arg2];
				showDialog(NOTE_OPTIONS);
				return false;
		}
	};
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){
		message=stg2[position];
		title = stg1[position];
		selid = ids[position];
		Intent read = new Intent(notedata.this,Textviewer.class);
		read.putExtra("text", message);
		read.putExtra("title", title);
		startActivity(read);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
		switch(requestCode){
		case 0:
				showDialog(DELETE_NOTE);
				break;
		case 1:
				Intent note = new Intent(notedata.this,addnote.class);
				note.setAction("update");
				note.putExtra("id", selid);
				note.putExtra("title", title);
				note.putExtra("content", message);
				addnote(note);
				break;
		default: break;
		}}
	}
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id) {
		case NOTE_OPTIONS:
			builder.setTitle(title)
			.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startActivityForResult(new Intent(notedata.this, Password.class), 1);
					dismissDialog(NOTE_OPTIONS);
				}
			})
			.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startActivityForResult(new Intent(notedata.this, Password.class), 0);
					dismissDialog(NOTE_OPTIONS);
				}
			});
			break;
		case DELETE_NOTE:
			builder.setMessage("Are you sure to delete this note?! ")
			.setCancelable(false)
			.setPositiveButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					removeDialog(DELETE_NOTE);
				}})
			.setNegativeButton("YES",new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stubdm=new DataManipulator(notedata.this);
					dm=new DataManipulator(notedata.this);
					dm.deletenote(selid);
					dm.close();
					onCreate(state);
					removeDialog(DELETE_NOTE);
				}
			});
				
			break;

		default: break;

		}
		AlertDialog alert = builder.create(); 
		dialog = alert;
		return dialog;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
}