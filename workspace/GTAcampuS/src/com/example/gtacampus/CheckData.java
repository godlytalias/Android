package com.example.gtacampus;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CheckData extends ListActivity  {     
	static final int DIALOG_ID = 0;
	String message;
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
	      tv.setText("COURSE - SLOT");
		stg1=new String[names2.size()]; 

		int x=0;
		String stg;

		for (String[] course : names2) {
			stg = course[1]+"  -   "+course[2];

			stg1[x]=stg;
			x++;
		}


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
				this,android.R.layout.simple_list_item_1,   
				stg1);
        this.setListAdapter(adapter);
		

	}      

	public void onListItemClick(ListView parent, View v, int position, long id){
		
		
		String s=stg1[position];
		String[] s1=s.split("-");
		String coursename=s1[0];
		String slot=s1[1];
		message="       COURSE DETAILS\n\nCOURSE NAME           :   "+coursename+"\nSLOT                             :   "+slot;
		message=message+"\n          CLASS HOURS\nDAY             TIME\n\n";
	    if(slot.equals("   A"))
	    {
	    	message=message+"MON            8:00 - 9:00\nWED             9:00 - 10:00\nFRI              10:15 - 11:15\n\nTUE (A+)      1:00 - 2:00";
	    }
	    else if(slot.equals("   B"))
	    {
	    	message=message+"MON            11:15 - 12:15\nTUE             8:00 - 9:00\nTHU              9:00 - 10:00\n\nFRI (B+)      2:00 - 3:00";
	    }
	    else if(slot.equals("   C"))
	    {
	    	message=message+"TUE            11:15 - 12:15\nWED             8:00 - 9:00\nFRI              9:00 - 10:00\n\nTHU (C+)      2:00 - 3:00";
	    }
	    else if(slot.equals("   D"))
	    {
	    	message=message+"MON            10:15 - 11:15\nTUE             8:00 - 9:00\nTHU              9:00 - 10:00\n\nFRI (D+)      5:00 - 6:00";
	    }
	    else if(slot.equals("   E"))
	    {
	    	message=message+"TUE            10:15 - 11:15\nTHU             11:15 - 12:15\nFRI              8:00 - 9:00\n\nMON (E+)      2:00 - 3:00";
	    }
	    else if(slot.equals("   F"))
	    {
	    	message=message+"MON            9:00 - 10:00\nWED             10:15 - 11:15\nFRI              11:15-12:15\n\nTUE (F+)      2:00 - 3:00";
	    }
	    else if(slot.equals("   G"))
	    {
	    	message=message+"MON            1:00 - 2:00\nTUE             9:00 - 10:00\nTHU               10:15-11:15\n\nWED (G+)        2:00 - 3:00";
	    }
	    else if(slot.equals("   H"))
	    {
	    	message=message+"MON            5:00 - 6:00\nTUE             5:00 - 6:00\nWED              1:00 - 2:00\n\nFRI (H+)      1:00 - 2:00";
	    }
	    else if(slot.equals("   P"))
	    {
	    	message=message+"MON             2:00 - 5:00";
	    }
	    else if(slot.equals("   Q"))
	    {
	    	message=message+"TUE             2:00 - 5:00";
	    }
	    else if(slot.equals("   R"))
	    {
	    	message=message+"WED             2:00 - 5:00";
	    }
	    else if(slot.equals("   S"))
	    {
	    	message=message+"THU             2:00 - 5:00";
	    }
	    else if(slot.equals("   T"))
	    {
	    	message=message+"FRI             2:00 - 5:00";
	    }
	    else if(slot.equals("   U"))
	    {
	    	message=message+"THU             10:15 - 1:15";
	    }
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
					CheckData.this.finish();

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