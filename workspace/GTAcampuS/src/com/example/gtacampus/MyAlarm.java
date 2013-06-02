package com.example.gtacampus;



import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.RingtonePreference;
import android.provider.AlarmClock;
import android.widget.Toast;


public class MyAlarm extends Service{
	private Ringtone alarmalert;
	Calendar mycal;
	PowerManager pm;
	AlarmManager am = null;
	PowerManager.WakeLock wl;
	PendingIntent setalarm;
	DataManipulator db;
	private final Handler alarmhandler = new Handler();
	
	@Override
	public void onCreate() {
		super.onCreate();
		pm = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "keepAlive");
		wl.acquire();
		db = new DataManipulator(this);
	}
	
	@Override
	public void onStart(Intent i,int startId)
	{
		super.onStart(i,startId);
		action(i);
		try{
			wl.release();
		}
		catch(Exception e){}
		
			}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onDestroy(){
		try{
			wl.release();
		}
		catch(Exception e)
		{}
		db.close();
		super.onDestroy();
	}

	public void action(Intent i)
	{
		String actiontodo= i.getAction();
		if(actiontodo.equals("setalarm"))
			setalarm(i);
		if(actiontodo.equals("launchalarm"))
			launchalarm(i);
		if(actiontodo.equals("snooze"))
			snoozealarm();
		if(actiontodo.equals("stop"))
			stopalarm();
		if(actiontodo.equals("bootsetalarm"))
			bootsetalarm();
	}
	
	
	public void bootsetalarm()
	{
		Cursor alarms = db.fetchalarms();
		Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
		if(alarms!=null)
		alarms.moveToFirst();
		String toaster = new String();
		while(!alarms.isAfterLast())
		{
			PendingIntent pendingalarms = PendingIntent.getBroadcast(this, 0, intent1, 0);
			mycal=Calendar.getInstance();
			mycal.set(alarms.getInt(1), alarms.getInt(2), alarms.getInt(3), alarms.getInt(4), alarms.getInt(5));
			am = (AlarmManager) this.getSystemService(this
					.getApplicationContext().ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP,mycal.getTimeInMillis(),pendingalarms);
			//toaster = toaster + String.format("%d %d %d %d %d\n", alarms.getInt(1), alarms.getInt(2), alarms.getInt(3), alarms.getInt(4), alarms.getInt(5));
			alarms.moveToNext();
		}
	//	Toast.makeText(this, toaster, Toast.LENGTH_LONG).show();
		alarms.close();
	}
	
	public void snoozealarm()
	{
	long snoozetime = System.currentTimeMillis() + 300000;
	Intent i = new Intent(this,MyAlarmBrdcst.class);
	setalarm = PendingIntent.getBroadcast(getBaseContext(), 0, i, 0);
	am.set(AlarmManager.RTC_WAKEUP, snoozetime, setalarm);
	//	am.setRepeating(AlarmManager.RTC_WAKEUP, snoozetime, 300000, setalarm);
	try{alarmalert.stop();
	alarmalert=null;}
	catch(Exception e){	}
	}
	
	
	public void stopalarm()
	{
		if(alarmalert!=null)
		try{alarmalert.stop();
		alarmalert=null;}
		catch(Exception e){}
		stopSelf();
	}
	
	public void setalarm(Intent i)
		{
			mycal= Calendar.getInstance();
			 mycal.set(i.getIntExtra("year", 2013), i.getIntExtra("month", 0), i.getIntExtra("day", 0), i.getIntExtra("hour", 0), i.getIntExtra("minute", 0), 0);
			
			Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
			setalarm= PendingIntent.getBroadcast(this, 0, intent1,
					PendingIntent.FLAG_UPDATE_CURRENT);
		
			am = (AlarmManager) this.getSystemService(this
					.getApplicationContext().ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP,mycal.getTimeInMillis(),setalarm);
			db.alarmsave(i.getIntExtra("year", 2013), i.getIntExtra("month", 0), i.getIntExtra("day", 0),i.getIntExtra("hour",0), i.getIntExtra("minute", 0), i.getStringExtra("title"));
		}
	
	public void launchalarm(Intent i)
	{
		AudioManager alarm = (AudioManager)getSystemService(AUDIO_SERVICE);
		alarm.setStreamVolume(AudioManager.STREAM_ALARM, alarm.getStreamMaxVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_VIBRATE);
		playalarmtone();
		alarmhandler.post(alarmdialog);
	}
	
	private Ringtone getalarmtone(){
		Ringtone r = null;
		try{
			r=RingtoneManager.getRingtone(getBaseContext(), Uri.parse(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
			r.setStreamType(AudioManager.STREAM_ALARM);
		}
		catch(Exception e){
			try{
				r=RingtoneManager.getRingtone(getBaseContext(), RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(),RingtoneManager.TYPE_ALL));
				r.setStreamType(AudioManager.STREAM_ALARM); }
			catch(Exception ex){}
		}
		return r;
	}
	
	private final Runnable alarmdialog = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent notif = new Intent(getBaseContext(),alarmnotif.class);
			notif.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND);
			PendingIntent alarmnotif = PendingIntent.getActivity(getBaseContext(), 0, notif, 0);
			try {
				alarmnotif.send();
			} catch (PendingIntent.CanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					}
	};
	private Runnable alarmthread = new Runnable(){
		
		@Override
		public void run(){
			try{
				if (alarmalert==null)
					alarmalert=getalarmtone();
				if(!(alarmalert.isPlaying()))
					alarmalert.play();
			
			}
			catch(Exception e){};
		}
	};
	
	public void playalarmtone()
	{
		Thread alert = new Thread(alarmthread);
		alert.setDaemon(true);
		alert.start();
	}
	
	public void wakesnooze()
	{
		
	}
}