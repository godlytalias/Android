package com.example.gtacampus;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;
import com.project.gtacampus.R;

public class CheckData extends ListActivity  {     
	static final int DELETE_COURSE=1;
	String message,title;
	TextView selection;
	Bundle state;
	public int idToModify; 
	DataManipulator dm;
	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] stg1;
	TextView tv;
	ListView listview;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		state = savedInstanceState;
		setContentView(R.layout.check);
		listview = (ListView) findViewById(android.R.id.list);
		listview.setOnItemLongClickListener(deletecourse);
		initlist();
	}      

	
	public void initlist(){
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
			message  = "\nCOURSE CODE\t\t:\t" + c_det.getString(2) + "\n";
			message  += "\nTEACHER\t\t\t\t:\t" + c_det.getString(0) + "\n";
			message  += "\nBUNKS\t\t\t\t\t:\t" + c_det.getInt(3) + "\n";
		}
		message += "\n\n\t\tCLASS TIMINGS\t\n";
		message += db.courseslots(c_det.getString(1));
		c_det.close();
		db.close();
		Intent read = new Intent(CheckData.this,Textviewer.class);
		read.putExtra("text", message);
		read.putExtra("title", title);
		startActivity(read);
		
			}

	public void coursefn(View v)
	{
		startActivityForResult(new Intent(CheckData.this,Password.class), 0);
	}
	
	AdapterView.OnItemLongClickListener deletecourse = new OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			message = stg1[arg2];
			showDialog(DELETE_COURSE);
			return false;
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 0:
			if(resultCode==Activity.RESULT_OK)
			{
				Intent courseintent=new Intent (CheckData.this,courses.class);
				startActivity(courseintent);
			}
			break;
			
		case 1:
			if(resultCode==Activity.RESULT_OK)
			{
				DataManipulator db = new DataManipulator(CheckData.this);
				db.deletecourse(message);
				db.close();
				Intent setalarm = new Intent(CheckData.this,MyAlarm.class);
				setalarm.setAction("setalarm");
				startService(setalarm);
				onCreate(state);
			}
			break;
		default: break;
		}
	};
	
	protected void onResume() {
		super.onResume();
		initlist();
	};
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id) {
		
		case DELETE_COURSE:
			builder.setMessage("Do you really want to delete course '"+message+"' ?")
			.setPositiveButton("NO", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					removeDialog(DELETE_COURSE);
				}
			})
			.setNegativeButton("YES", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					removeDialog(DELETE_COURSE);
					startActivityForResult(new Intent(CheckData.this,Password.class), 1);
				}
			});
			dialog=builder.create();			
			break;
			
		default:break;

		}
		return dialog;
	}


}