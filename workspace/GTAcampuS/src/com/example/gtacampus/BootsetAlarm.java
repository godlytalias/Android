package com.example.gtacampus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootsetAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context,MyAlarm.class);
		i.setAction("bootsetalarm");
		context.startService(i);
	}

}
