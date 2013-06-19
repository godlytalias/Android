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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CheckData extends ListActivity  {     
	static final int DIALOG_ID = 0;
	String message,title;
	TextView selection;
	public int idToModify; 
	DataManipulator dm;
	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] stg1;
	TextView tv;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.check);
		  dm = new DataManipulator(this);
	      names2 = dm.selectAll();
	      tv=(TextView)findViewById(R.id.selection2);
	      tv.setText("COURSE - CODE");
		stg1=new String[names2.size()]; 

		int x=0;
		String stg;

		for (String[] course : names2) {
			stg = course[1]+"    -    "+course[2];
			stg1[x]=stg;
			x++;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
				this,android.R.layout.simple_list_item_1,   
				stg1);
        this.setListAdapter(adapter);
		
        dm.close();
	}      

	public void onListItemClick(ListView parent, View v, int position, long id){
		
		String[] course = stg1[position].split("    -    ");
		DataManipulator db = new DataManipulator(CheckData.this);
		Cursor c_det = db.coursedetails(course[0]);
		if(c_det.moveToFirst()){
			title    =c_det.getString(1);
			message  = "\nCOURSE CODE           :     " + c_det.getString(2) + "\n";
			message  += "\nTEACHER     		        :		" + c_det.getString(4) + "\n";
			message  += "\nBUNKS     		        :		" + c_det.getInt(3) + "\n";
		}
		message += "\n\n\t   CLASS TIMINGS\t\n";
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
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		switch(id) {
		case DIALOG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

		default:

		}
		return dialog;
	}


}