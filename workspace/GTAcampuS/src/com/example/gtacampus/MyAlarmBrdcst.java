package com.example.gtacampus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;


public class MyAlarmBrdcst extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context,Intent intent)
	{
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "GTAcampuS");
		wl.acquire(120000);
	 Intent i = new Intent(context,MyAlarm.class);
		 i.setAction("launchalarm");
		 context.startService(i);
	}
	
	
}


