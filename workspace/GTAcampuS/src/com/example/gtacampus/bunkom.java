package com.example.gtacampus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.project.gtacampus.R;


public class bunkom extends ListActivity{
	static final int BUNK = 0;
	Notification notifydet;
	DataManipulator dm;
	String msg;
	String[] stg1,c_name;

	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bunklist);
		ListView bunkmeter = (ListView) findViewById(android.R.id.list);
		bunkmeter.setOnItemLongClickListener(bunkit);
		 initlist();
	}
	
	protected void initlist(){
		dm = new DataManipulator(this);
	      names2 = dm.selectAll();
	      stg1=new String[names2.size()];
			int x=0;
			String stg;

			for (String[] course : names2) {
				stg = course[0]+"		-   "+course[2];
				stg1[x]=stg;
				x++;			}


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
					this,android.R.layout.simple_list_item_1,   
					stg1);
	        this.setListAdapter(adapter);
	        dm.close();
	}
	
	AdapterView.OnItemLongClickListener bunkit = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			c_name = stg1[arg2].split("		-   ");
			showDialog(BUNK);			
			return false;
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 10:
			if(resultCode==Activity.RESULT_OK){
				dm = new DataManipulator(bunkom.this);
				dm.update(c_name[0]);
				dm.close();
				initlist();
			}
			break;
		default: break;
		}
	};
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){
		
		c_name = stg1[position].split("		-   ");
		dm = new DataManipulator(bunkom.this);
		Cursor dates = dm.getdates(c_name[0]);
		dates.moveToFirst();
		msg = "\n\tTotal Bunks\t: " + c_name[1]+"\n\n\t    DATES\n";
		Calendar cal = Calendar.getInstance();
		int count =1;
		while(!dates.isAfterLast()){
			cal.setTimeInMillis(dates.getLong(0));
			try{
			msg+="\n"+ count + ")  "+cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)+"  "+ cal.get(Calendar.DAY_OF_MONTH)+" ,   "+((cal.get(Calendar.HOUR)==0)?12:cal.get(Calendar.HOUR))+" : " + cal.get(Calendar.MINUTE) + " " + ((cal.get(Calendar.AM_PM)==0)?"AM":"PM");
			}
			catch(NoSuchMethodError e){
				msg+="\n"+ count + ")  "+cal.get(Calendar.DAY_OF_MONTH)+" / "+ cal.get(Calendar.MONTH)+" ,   "+((cal.get(Calendar.HOUR)==0)?12:cal.get(Calendar.HOUR))+" : " + cal.get(Calendar.MINUTE) + " " + ((cal.get(Calendar.AM_PM)==0)?"AM":"PM");
							}
			count++;
		dates.moveToNext();
		}
		dates.close();
		dm.close();
		Intent read = new Intent(bunkom.this,Textviewer.class);
		read.putExtra("text", msg);
		read.putExtra("title", c_name[0]);
		startActivity(read);
	}
	
	
	protected final Dialog onCreateDialog(final int id){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		switch(id){
		case BUNK : dialog.setMessage("Do you want to add a bunk to the course '" + c_name[0] + "' ?")
		.setTitle("Bunking..")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(bunkom.this,Password.class), 10);
				removeDialog(BUNK);
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				removeDialog(BUNK);
			}
		});
		break;
		
		default: break;
		}
		
		return dialog.create();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
}