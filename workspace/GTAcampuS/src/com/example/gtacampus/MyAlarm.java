package com.example.gtacampus;



import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;


public class MyAlarm extends Service{

	PowerManager pm;
	PowerManager.WakeLock wl;
	@Override
	public void onCreate() {
		super.onCreate();
		pm = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "keepAlive");
		wl.acquire();
	}
	
	@Override
	public void onStart(Intent i,int startId)
	{
		try{
			wl.release();
		}
		catch(Exception e){}
		Calendar mycal= Calendar.getInstance();
		 mycal.set(i.getIntExtra("year", 2013), i.getIntExtra("month", 0), i.getIntExtra("day", 0), i.getIntExtra("hour", 0), i.getIntExtra("minute", 0), 0);
		
		Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
	
		@SuppressWarnings("static-access")
		AlarmManager am = (AlarmManager) this.getSystemService(this
				.getApplicationContext().ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP,mycal.getTimeInMillis(),sender);
			}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*@Override
	public void onDestroy(){
		
	}*/

}