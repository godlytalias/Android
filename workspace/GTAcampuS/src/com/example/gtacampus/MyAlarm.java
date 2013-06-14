package com.example.gtacampus;



import java.util.Calendar;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.RingtonePreference;
import android.provider.AlarmClock;
import android.widget.Toast;


public class MyAlarm extends Service{
	private Ringtone alarmalert;
	PowerManager pm;
	AlarmManager am = null;
	PowerManager.WakeLock wl;
	PendingIntent setalarm;
	DataManipulator db;
	Notification alarmnotification;
	NotificationManager alarmnotifier;
	private int ALARM_NOTIFICATION =10;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		pm = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "keepAlive");
		wl.acquire();
		db = new DataManipulator(this);
		alarmnotifier = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
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
			setalarm();
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
		long nxt_time = getnextalarmtime();
		Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
		
			PendingIntent pendingalarms = PendingIntent.getBroadcast(this, 0, intent1, 0);
			
			if(nxt_time!=0)
			pushalarm(nxt_time, pendingalarms);
					
	}
	
	public void snoozealarm()
	{
		SharedPreferences alpref=getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		long snoozetime = System.currentTimeMillis() + ((alpref.getInt("alarmsnooze", 5))*60000);
		snoozealarm(snoozetime);
		}
	
	public void snoozealarm(long snoozetime)
	{
	Intent i = new Intent(this,MyAlarmBrdcst.class);
	setalarm = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
	am.cancel(setalarm);
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
		setalarm();
		stopSelf();
	}
	
	public void setalarm()
		{
			long nxt_time = getnextalarmtime();
			Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
			setalarm= PendingIntent.getBroadcast(this, 0, intent1,
					PendingIntent.FLAG_UPDATE_CURRENT);
		
			if(nxt_time!=0)
				pushalarm(nxt_time,setalarm);
			else
			{
				am = (AlarmManager) this.getSystemService(this
						.getApplicationContext().ALARM_SERVICE);
				am.cancel(setalarm);
				alarmnotifier.cancel(ALARM_NOTIFICATION);
				SharedPreferences.Editor alarmdetedit = getBaseContext().getSharedPreferences("GTAcampuS_alarmdet", MODE_PRIVATE).edit();
				alarmdetedit.putString("alarmdetails", "none");
				alarmdetedit.commit();
			}
			}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("deprecation")
	public void pushalarm(long time,PendingIntent setalarm){
		SharedPreferences al = getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		am = (AlarmManager) this.getSystemService(this
				.getApplicationContext().ALARM_SERVICE);
		am.cancel(setalarm);
		alarmnotifier.cancel(ALARM_NOTIFICATION);
		am.set(AlarmManager.RTC_WAKEUP,time,setalarm);
		Intent resultintent = new Intent(getBaseContext(),Alarmsetter.class);
		resultintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent result = PendingIntent.getActivity(this, 0, resultintent,0);
		String alarmtimedetails = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US)+ " , " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " +calendar.get(Calendar.DAY_OF_MONTH) + "  -  " + calendar.get(Calendar.HOUR) + " : " + calendar.get(Calendar.MINUTE) + " " + ((calendar.get(Calendar.AM_PM)==1)?"PM":"AM");
		alarmnotification = new Notification(R.drawable.alarm,"Next alert set for "+alarmtimedetails, System.currentTimeMillis());
		alarmnotification.setLatestEventInfo(this, "GTAcampuS", "Alert Name : " + al.getString("alarmtitle", "Custom Alert") + "   -   " + alarmtimedetails,result);
		alarmnotification.flags = Notification.FLAG_NO_CLEAR;
		alarmnotifier.notify(ALARM_NOTIFICATION, alarmnotification);
		SharedPreferences.Editor alarmdetedit = getBaseContext().getSharedPreferences("GTAcampuS_alarmdet", MODE_PRIVATE).edit();
		alarmdetedit.putString("alarmdetails", alarmtimedetails);
		alarmdetedit.commit();
	}
	
	public void launchalarm(Intent i)
	{
		alarmnotifier.cancel(ALARM_NOTIFICATION);
		AudioManager alarm = (AudioManager)getSystemService(AUDIO_SERVICE);
		alarm.setStreamVolume(AudioManager.STREAM_ALARM, alarm.getStreamMaxVolume(AudioManager.STREAM_ALARM)/4, AudioManager.FLAG_VIBRATE);
		playalarmtone();
		Thread alarmhandler = new Thread(alarmdialog);
		alarmhandler.start();
	}
	
	private Ringtone getalarmtone(){
		Ringtone r = null;
		
		try{
			//r=RingtoneManager.getRingtone(getBaseContext(), Uri.parse(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
			r=RingtoneManager.getRingtone(getBaseContext(), Uri.parse("content://com.example.gtacampus/res/raw/sundaychurch"));
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
	
	private Runnable volup = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AudioManager alarm = (AudioManager)getSystemService(AUDIO_SERVICE);
			int cur_vol,max_vol;
			max_vol=alarm.getStreamMaxVolume(AudioManager.STREAM_ALARM);
			cur_vol=max_vol/4;
			while(cur_vol<max_vol){
				cur_vol++;
				alarm.setStreamVolume(AudioManager.STREAM_ALARM, cur_vol, AudioManager.FLAG_VIBRATE);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}}
	};
	
	public void playalarmtone()
	{
		Thread alert = new Thread(alarmthread);
		alert.setDaemon(true);
		alert.start();
		 Thread volume = new Thread(volup);
		 volume.setDaemon(true);
		 volume.start();
	}
	
		
	public int getday(int day, int dayofweek, ContentValues week){
		while(true){
		if(dayofweek==Calendar.SUNDAY){
			if(week.getAsBoolean("sun"))
				break;
			else{
				day++;
				dayofweek++;
			}
		}
		
		if(dayofweek==Calendar.MONDAY){
			if(week.getAsBoolean("mon"))
				break;
			else{
				day++;
				dayofweek++;
			}
		}
		
		if(dayofweek==Calendar.TUESDAY){
			if(week.getAsBoolean("tue"))
				break;
			else{
				day++;
				dayofweek++;
			}
		}
		
		if(dayofweek==Calendar.WEDNESDAY){
			if(week.getAsBoolean("wed"))
				break;
			else{
				day++;
				dayofweek++;
			}
		}
		
		if(dayofweek==Calendar.THURSDAY){
			if(week.getAsBoolean("thu"))
				break;
			else{
				day++;
				dayofweek++;
			}
		}
		
		if(dayofweek==Calendar.FRIDAY){
			if(week.getAsBoolean("fri"))
				break;
			else{
				day++;
				dayofweek++;
			}
		}
		
		if(dayofweek==Calendar.SATURDAY){
			if(week.getAsBoolean("sat"))
				break;
			else{
				day++;
				dayofweek++;
			}
		}
	}
		return day;
		}
	
	
	public long alarmgetmillis(int hour,int minute, ContentValues week)
	{
		Calendar cal = Calendar.getInstance();
		long cur_time = cal.getTimeInMillis();
		int day,month,year,cur_day;
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		cur_day = cal.get(Calendar.DAY_OF_MONTH);
		day=getday(cur_day, cal.get(Calendar.DAY_OF_WEEK), week);		
		cal.set(year, month, day, hour, minute, 0);
		if(cal.getTimeInMillis()<cur_time)
			day=getday(cur_day+1, cal.get(Calendar.DAY_OF_WEEK), week);
		cal.set(year, month, day, hour, minute, 0);
		return cal.getTimeInMillis();
	}
	
	public long getnextalarmtime()
	{
		Calendar cal = Calendar.getInstance();
		long cur_time = cal.getTimeInMillis();
		SharedPreferences.Editor alarmprefs = getBaseContext().getSharedPreferences("GTAcampuS", MODE_PRIVATE).edit();
		int hour,minute;
		long nxttime,nxtalarmtime=0;
		db=new DataManipulator(this);
		Cursor en_alarms = db.fetchenabledalarms();
		if(en_alarms==null)
			return 0;
		else{
		en_alarms.moveToFirst();
		while(!en_alarms.isAfterLast()){
			hour = en_alarms.getInt(4);
			minute=en_alarms.getInt(5);
			if(en_alarms.getString(7).equals("task"))
			{
				cal.set(en_alarms.getInt(1), en_alarms.getInt(2), en_alarms.getInt(3),  hour,minute,0);
				nxttime = cal.getTimeInMillis();
			}
			else{
				ContentValues week = new ContentValues();
				week.put("sun",en_alarms.getInt(12)!=0);
				week.put("mon",en_alarms.getInt(13)!=0);
				week.put("tue",en_alarms.getInt(14)!=0);
				week.put("wed",en_alarms.getInt(15)!=0);
				week.put("thu",en_alarms.getInt(16)!=0);
				week.put("fri",en_alarms.getInt(17)!=0);
				week.put("sat",en_alarms.getInt(18)!=0);
				nxttime = alarmgetmillis(hour, minute, week);
			}
			if((nxttime<nxtalarmtime && nxttime>cur_time)||nxtalarmtime==0)
			{
				nxtalarmtime = nxttime;
				alarmprefs.putInt("alarmid", en_alarms.getInt(0));
				alarmprefs.putString("alarmtype", en_alarms.getString(7));
				alarmprefs.putString("alarmtitle", en_alarms.getString(6));
				alarmprefs.putInt("alarmsnooze", en_alarms.getInt(9));
				alarmprefs.putBoolean("alarmshake", en_alarms.getInt(10)!=0);
				alarmprefs.putBoolean("alarmmath", en_alarms.getInt(11)!=0);
				alarmprefs.commit();
			}
			en_alarms.moveToNext();
		}
		en_alarms.close();
		db.close();
		return nxtalarmtime;
		}
	}
}