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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class notedata extends ListActivity{
	static final int DIALOG_ID = 0;
	
	List<String> slots=new ArrayList<String>();
	private DataManip dh;   
	String message;
	TextView selection;
	public int idToModify; 
	DataManip dm;

	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] stg1,stg2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes);
		
				  dm = new DataManip(this);
		      names2 = dm.selectAll();
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
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){
		message=stg2[position];
		showDialog(DIALOG_ID);
	}
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		switch(id) {
		case DIALOG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(message)
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					notedata.this.finish();

              }
			});
						AlertDialog alert = builder.create(); 
			dialog = alert;
			break;

		default:

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