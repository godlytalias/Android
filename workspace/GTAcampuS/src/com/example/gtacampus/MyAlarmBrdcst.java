/*
 * GTAcampuS v1 Copyright (c) 2013 Godly T.Alias
 * 
   This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

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
		 Intent i = new Intent(context,MyAlarm.class);
		if(intent.getAction().equals("setbacksounds"))
		{
			i.setAction("setbacksounds");
			context.startService(i);
		}
		else{
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "GTAcampuS");
		wl.acquire(12000);
		 i.setAction("launchalarm");
		 context.startService(i);}
	}
}


