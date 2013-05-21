package com.example.gtacampus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class alarmnotif extends Activity{
	
	private LinearLayout rootlayout;
	private KeyguardLock lock;
	private Button bt1,bt2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		rootlayout=(LinearLayout)findViewById(R.id.rootlayout);
		setContentView(R.layout.alarm);
		bt1=(Button)findViewById(R.id.albutton1);
		bt2=(Button)findViewById(R.id.albutton2);
		bt1.setOnClickListener(snoozealarm);
		bt2.setOnClickListener(stopalarm);
		KeyguardManager keyguard = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
		lock = keyguard.newKeyguardLock("GTAcampuS");
		
}
	
	
	
	@Override
	public void onConfigurationChanged(Configuration config)
	{
		setContentView(rootlayout);
		super.onConfigurationChanged(config);
	}
	
	@Override
	protected void onPause(){
		lock.reenableKeyguard();
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		lock.disableKeyguard();
		super.onResume();
	}
	
	private View.OnClickListener snoozealarm = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	private View.OnClickListener stopalarm = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
}