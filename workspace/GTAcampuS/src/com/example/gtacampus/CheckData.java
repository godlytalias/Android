package com.example.gtacampus;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CheckData extends ListActivity  {     
	static final int DIALOG_ID = 0,DELETE_COURSE=1;
	String message,title;
	TextView selection;
	public int idToModify; 
	DataManipulator dm;
	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] stg1;
	TextView tv;
	ListView listview;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.check);
		listview = (ListView) findViewById(android.R.id.list);
		listview.setOnItemLongClickListener(deletecourse);
		  dm = new DataManipulator(this);
	      names2 = dm.selectAll();
	      tv=(TextView)findViewById(R.id.selection2);
	      tv.setText("COURSES");
		stg1=new String[names2.size()]; 

		int x=0;

		for (String[] course : names2) {
			stg1[x] = course[0];
			x++;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
				this,android.R.layout.simple_list_item_1,   
				stg1);
        this.setListAdapter(adapter);
		
        dm.close();
	}      

	public void onListItemClick(ListView parent, View v, int position, long id){
		
		DataManipulator db = new DataManipulator(CheckData.this);
		Cursor c_det = db.coursedetails(stg1[position]);
		if(c_det.moveToFirst()){
			title    =c_det.getString(1);
			message  = "\nCOURSE CODE           :		" + c_det.getString(2) + "\n";
			message  += "\nTEACHER     		        :		" + c_det.getString(0) + "\n";
			message  += "\nBUNKS     		        :		" + c_det.getInt(3) + "\n";
		}
		message += "\n\n\t\tCLASS TIMINGS\t\n";
		message += db.courseslots(c_det.getString(1));
		c_det.close();
		db.close();
		
		showDialog(DIALOG_ID);
		
			}

	public void coursefn(View v)
	{
		Intent courseintent=new Intent (CheckData.this,courses.class);
		startActivity(courseintent);
	}
	
	AdapterView.OnItemLongClickListener deletecourse = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			message = stg1[arg2];
			showDialog(DELETE_COURSE);
			return false;
		}
	};
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id) {
		case DIALOG_ID:
			builder.setMessage(message)
			.setTitle(title)
			.setCancelable(false)
			.setIcon(R.drawable.course)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dismissDialog(DIALOG_ID);

              }
			});
						AlertDialog alert = builder.create(); 
			dialog = alert;
			break;
		case DELETE_COURSE:
			builder.setMessage("Do you really want to delete course "+message)
			.setPositiveButton("NO", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dismissDialog(DELETE_COURSE);
				}
			})
			.setNegativeButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					DataManipulator db = new DataManipulator(CheckData.this);
					db.deletecourse(message);
					db.close();
					Intent setalarm = new Intent(CheckData.this,MyAlarm.class);
					setalarm.setAction("setalarm");
					startService(setalarm);
					dismissDialog(DELETE_COURSE);
					Intent refresh = new Intent(CheckData.this,CheckData.class);
					startActivity(refresh);
				}
			});
			dialog=builder.create();			
			break;
			
		default:break;

		}
		return dialog;
	}


}