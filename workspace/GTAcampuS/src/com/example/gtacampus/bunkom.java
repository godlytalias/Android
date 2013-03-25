package com.example.gtacampus;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class bunkom extends ListActivity{
	static final int DIALOG_ID = 0;
	DataManipulator dm;
	String[] stg2,stg3,bunkval;

	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bunklist);
		 dm = new DataManipulator(this);
	      names2 = dm.selectAll();
	      String[] stg1=new String[names2.size()]; 
	      stg3=new String[names2.size()]; 
	      bunkval=new String[names2.size()]; 
	      stg2=new String[names2.size()]; 

			int x=0;
			String stg;

			for (String[] course : names2) {
				stg = course[1]+"  -   "+course[3];
				stg2[x]=course[1];
				stg3[x]=course[0];
				bunkval[x]=course[3];
				stg1[x]=stg;
				x++;
			}


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
					this,android.R.layout.simple_list_item_1,   
					stg1);
	        this.setListAdapter(adapter);
	}
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){
		
		Integer num=new Integer(Integer.parseInt(bunkval[position]));
		num=num+1;
		dm.update(stg3[position], num.toString());
		Toast.makeText(this, stg2[position].toUpperCase()+" BUNKED", Toast.LENGTH_LONG).show();
		this.finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
	
}