package com.example.gtacampus;

import java.util.ArrayList;
import java.util.List;

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


public class notedata extends ListActivity{
	static final int DELETE_NOTE=1;
	List<String> slots=new ArrayList<String>();
	private DataManipulator dh;   
	String message,title;
	TextView selection;
	public int idToModify; 
	DataManipulator dm;
	Bundle state;

	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] stg1,stg2;

	
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

				int x=0;
				String stg,stga;

				for (String[] note : names2) {
					stg = note[1];
					stga=note[2];
					stg2[x]=stga;
					stg1[x]=stg;
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
		startActivity(noteintent);
		this.finish();
	}
	
	AdapterView.OnItemLongClickListener deleter = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
				message = stg2[arg2];
				title = stg1[arg2];
				showDialog(DELETE_NOTE);
				return false;
		}
	};
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){
		message=stg2[position];
		title = stg1[position];
		Intent read = new Intent(notedata.this,Textviewer.class);
		read.putExtra("text", message);
		read.putExtra("title", title);
		startActivity(read);
	}
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		switch(id) {
		case DELETE_NOTE:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Do you want to delete this note?! ")
			.setCancelable(false)
			.setPositiveButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dismissDialog(DELETE_NOTE);
				}})
			.setNegativeButton("YES",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dm=new DataManipulator(notedata.this);
					dm.deletenote(message);
					dm.close();
					onCreate(state);
				}
			});
				AlertDialog alert = builder.create(); 
			dialog = alert;
			break;

		default: break;

		}
		return dialog;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
}