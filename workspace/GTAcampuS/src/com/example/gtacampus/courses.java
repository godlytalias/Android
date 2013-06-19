package com.example.gtacampus;


import java.util.ArrayList;
import java.util.List;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class courses extends Activity {
	
	List<String> slots=new ArrayList<String>();
	
	static final int DIALOG_ID = 0,NOT_FREE=1,EMPTY_FIELDS=2,DUP_VALUES=3;
	EditText course_name,course_code,course_teacher;
	LinearLayout c_timing;
	Spinner course_day, course_time;
	DataManipulator db;
	int class_count,timeflag=0;
	int[] day_id,hour_pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);
		course_name = (EditText) findViewById(R.id.course_name);
		course_teacher = (EditText) findViewById(R.id.course_teacher);
		course_code = (EditText) findViewById(R.id.course_code);
		c_timing = (LinearLayout)findViewById(R.id.ll_course_timing);
		course_day = (Spinner)findViewById(R.id.course_day);
		course_time=(Spinner)findViewById(R.id.course_time);
		db = new DataManipulator(this);
		Cursor hourtimings = db.gethourtimings();
		hourtimings.moveToFirst();
		for(int i=1;i<hourtimings.getColumnCount();i++)
		{
			slots.add(hourtimings.getString(i));
		}
		hourtimings.close();
		db.close();
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner, slots);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		course_time.setAdapter(adapter);
		class_count=0;
		day_id = new int[15];
		hour_pos = new int[15];
	}
	
	public void addtime(View v)
	{
		if(class_count<15){
		db = new DataManipulator(this);
		int day = course_day.getSelectedItemPosition();
		int hour_ps = course_time.getSelectedItemPosition()+1;
		Cursor slotstat = db.slotstat();
		slotstat.moveToPosition(day);
		if(slotstat.getString(hour_ps).equals("-"))
		{
		timeflag=1;
		TextView check = new TextView(this);
		check.setText("   " + course_day.getSelectedItem().toString() + "           " + course_time.getSelectedItem());
		c_timing.addView(check);
		day_id[class_count]=day+3;
		hour_pos[class_count] = hour_ps;
		class_count++;
		}
		else showDialog(NOT_FREE);
		slotstat.close();
		db.close();
	}}
		
	
	public void addata(View v)
	{
		long flag=0;
		String name,code,teacher;
		name = course_name.getText().toString();
		code = course_code.getText().toString();
		teacher = course_teacher.getText().toString();
		if(name.length()>0&&code.length()>0&&teacher.length()>0&&timeflag==1){
		db = new DataManipulator(this);
		flag=db.insert(name, code, teacher);
		if(flag!=-1){
			for(int i=0;i<class_count;i++)
			{
				ContentValues val = new ContentValues();
				val.put("HOUR"+hour_pos[i], course_name.getText().toString());
				db.addcourse(day_id[i],val);
			}
			class_count=0;
		showDialog(DIALOG_ID);}
		else
			showDialog(DUP_VALUES);

		db.close();
		}
		else
			showDialog(EMPTY_FIELDS);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
	
	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		switch(id) {
		case DIALOG_ID:
			builder.setMessage("Course added! Add another course ?")
			.setTitle("GTAcampuS")
			.setCancelable(false)
			.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent setalarm = new Intent(courses.this,MyAlarm.class);
					setalarm.setAction("setalarm");
					startService(setalarm);
					Intent newint=new Intent(courses.this,CampusActivity.class);
					startActivity(newint);
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

		case NOT_FREE : builder.setMessage("Selected hour is not free!")
		.setCancelable(false)
		.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();}});
		dialog = builder.create();
			break;
			
		case EMPTY_FIELDS : builder.setMessage("Some required fields are empty! ")
		.setTitle("Alert!")
		.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(EMPTY_FIELDS);
			}
		});
		dialog=builder.create();
		break;
		
		case DUP_VALUES : builder.setMessage("Course with same Name / Code already exists! ")
		.setTitle("Alert!")
		.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(DUP_VALUES);
			}
		});
		dialog=builder.create();
		break;
		default : break;
		}
		return dialog;
	}
}