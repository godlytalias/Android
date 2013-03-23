package com.example.gtacampus;


import java.util.ArrayList;
import java.util.List;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class courses extends Activity {
	
	List<String> slots=new ArrayList<String>();
	
	MultiAutoCompleteTextView mv;
	private DataManipulator dh;     
	static final int DIALOG_ID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.course);
		mv=(MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView1);
		mv.setHint("Enter the course");
		Spinner slotspin=(Spinner)findViewById(R.id.slots);
		slots.add("Select Slot");
		slots.add("A");
		slots.add("B");
		slots.add("C");
		slots.add("D");
		slots.add("E");
		slots.add("F");
		slots.add("G");
		slots.add("H");
		slots.add("P");
		slots.add("Q");
		slots.add("R");
		slots.add("S");
		slots.add("T");
		slots.add("U");
		
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner, slots);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		slotspin.setAdapter(adapter);
		
		
		
	}
		
	
	public void addata(View v)
	{
		String Text2,Text1;
		int flag=0;
		List<String[]> names2 =null ;
		DataManipulator dm = new DataManipulator(this);
	      names2 = dm.selectAll();
		mv=(MultiAutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextView1);
		Text1=mv.getText().toString();
		Spinner spin = (Spinner)findViewById(R.id.slots);
		Text2 = String.valueOf(spin.getSelectedItem());
		
		for (String[] slots : names2) {
			if(slots[2].equals(Text2))
			{
				flag=1;
				Toast.makeText(this, "Slot already assigned", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
		if((Text2.equals("Select Slot"))||(flag==1))
		{
			Toast.makeText(this, "Select a slot first", Toast.LENGTH_SHORT).show();
		}
		else
		{
		this.dh = new DataManipulator(this);
		this.dh.insert(Text1,Text2);
		showDialog(DIALOG_ID);
		Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		switch(id) {
		case DIALOG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Course added! Add another course ?")
			.setCancelable(false)
			.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					courses.this.finish();

              }
			})
			.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					Intent newint=new Intent(courses.this,courses.class);
					startActivity(newint);
					courses.this.finish();
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