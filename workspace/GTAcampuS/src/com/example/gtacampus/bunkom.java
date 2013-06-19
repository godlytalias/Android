package com.example.gtacampus;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class bunkom extends ListActivity{
	static final int DIALOG_ID = 0;
	Notification notifydet;
	DataManipulator dm;
	String msg;
	String[] stg2,stg3,bunkval;
	private int SIMPLE_NOTIFICATION_ID=0;
	private NotificationManager mNotify;

	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	/*	mNotify=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	 notifydet = new Notification(R.drawable.campus,msg , System.currentTimeMillis());*/
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
				stg = course[0]+"  -   "+course[2];
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
	/*	
		Integer num=new Integer(Integer.parseInt(bunkval[position]));
		num=num+1;
		dm.update(stg3[position], num.toString());
		msg= stg2[position].toUpperCase()+" BUNKED";
		Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
		Context con = bunkom.this;
		CharSequence title="Alert!!";
		CharSequence content=msg;
		Intent resultIntent = new Intent(con, bunkom.class);
		PendingIntent resultPendingIntent =PendingIntent.getActivity(con, 0, resultIntent, 0);
	notifydet = new Notification (R.drawable.nitc,"ALERT!!",System.currentTimeMillis());	
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(con.NOTIFICATION_SERVICE);
		notifydet.setLatestEventInfo(con, title, content, resultPendingIntent);
		mNotificationManager.notify(SIMPLE_NOTIFICATION_ID,notifydet);
		this.finish();*/
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
	
}