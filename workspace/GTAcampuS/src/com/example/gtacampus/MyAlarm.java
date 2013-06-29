package com.example.gtacampus;



import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;

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
import android.media.MediaPlayer;
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
	MediaPlayer ringer;
	AlarmManager am = null;
	PowerManager.WakeLock wl;
	PendingIntent setalarm;
	DataManipulator db;
	Notification alarmnotification;
	NotificationManager alarmnotifier;
	long endtime;
	private int ALARM_NOTIFICATION =10;
	private static int flag =0,alarmflag=1;
	private static long tenmins=10*60*1000;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		pm = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "keepAlive");
		wl.acquire();
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
		endtime=0;
		long nxt_time = getnextalarmtime();
		Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
		PendingIntent pendingalarms = PendingIntent.getBroadcast(this, 0, intent1, 0);
			if(nxt_time!=0)
			pushalarm(nxt_time, pendingalarms);
		stopSelf();
						}
	
	public void snoozealarm()
	{
		SharedPreferences alpref=getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		long snoozetime = System.currentTimeMillis() + (alpref.getInt("alarmsnooze", 5)*60000);
		snoozealarm(snoozetime);
		}
	
	public void snoozealarm(long snoozetime)
	{
	Intent i = new Intent(this,MyAlarmBrdcst.class);
	setalarm = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
	am = (AlarmManager) this.getSystemService(this
			.getApplicationContext().ALARM_SERVICE);
	am.cancel(setalarm);
	am.set(AlarmManager.RTC_WAKEUP, snoozetime, setalarm);
	//	am.setRepeating(AlarmManager.RTC_WAKEUP, snoozetime, 300000, setalarm);
	if(alarmalert!=null)
	try{alarmflag=1;
	alarmalert.stop();
	alarmalert=null;}
	catch(Exception e){	}
	}
	
	
	public void stopalarm()
	{
		if(alarmalert!=null)
		try{alarmflag=1;
		alarmalert.stop();
		alarmalert=null;}
		catch(Exception e){}
		setalarm();
		stopSelf();
	}
	
	public void setalarm()
		{
		SharedPreferences alarmdets = getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		if(alarmdets.contains("endtime"))
			endtime = alarmdets.getLong("endtime", 0);
		else
			endtime =0;
		SharedPreferences.Editor alarmdetedit = getBaseContext().getSharedPreferences("GTAcampuS", MODE_PRIVATE).edit();
		alarmdetedit.clear();
		alarmdetedit.commit();
			long nxt_time = getnextalarmtime();
			Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
			setalarm= PendingIntent.getBroadcast(this, 0, intent1,
					PendingIntent.FLAG_UPDATE_CURRENT);
		
			if(nxt_time>System.currentTimeMillis())
				pushalarm(nxt_time,setalarm);
			else
			{
				am = (AlarmManager) this.getSystemService(this
						.getApplicationContext().ALARM_SERVICE);
				am.cancel(setalarm);
				alarmnotifier.cancel(ALARM_NOTIFICATION);
				alarmdetedit = getBaseContext().getSharedPreferences("GTAcampuS_alarmdet", MODE_PRIVATE).edit();
				alarmdetedit.putString("alarmdetails", "none");
				alarmdetedit.commit();
			}
			
		stopSelf();
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
		Intent resultintent;
		if(al.getString("alarmtype", "alarm").equals("course"))
			resultintent = new Intent(getBaseContext(),Slot.class);
		else
		resultintent = new Intent(getBaseContext(),Alarmsetter.class);
		resultintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent result = PendingIntent.getActivity(this, 0, resultintent,0);
		String alarmtimedetails = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US)+ " , " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " +calendar.get(Calendar.DAY_OF_MONTH) + "  -  " + ((calendar.get(Calendar.HOUR)==0)?12:calendar.get(Calendar.HOUR)) + " : " + calendar.get(Calendar.MINUTE) + " " + ((calendar.get(Calendar.AM_PM)==1)?"PM":"AM");
		alarmnotification = new Notification(R.drawable.alarm,"Next alert on "+alarmtimedetails, System.currentTimeMillis());
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
		alarmflag=0;
		playalarmtone();
		Thread alarmhandler = new Thread(alarmdialog);
		alarmhandler.setDaemon(true);
		alarmhandler.start();
	}
	
	private Ringtone getalarmtone(){
		Ringtone r = null;
		
		try{
			r=RingtoneManager.getRingtone(getBaseContext(), Uri.parse("android.resource://com.example.gtacampus/"+R.raw.myringtone));
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
				while(alarmflag==0)
				if(!(alarmalert.isPlaying()))
				{
					alarmalert.setStreamType(AudioManager.STREAM_ALARM);
					alarmalert.play();
					Thread.sleep(16000);}
			/*	ringer = MediaPlayer.create(MyAlarm.this, R.raw.myringtone);
				ringer.start();		
				ringer.setLooping(true);*/
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
		//	max_vol=alarm.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			cur_vol=max_vol/3;
			while(cur_vol<max_vol){
				cur_vol++;
				alarm.setStreamVolume(AudioManager.STREAM_ALARM, cur_vol, AudioManager.FLAG_VIBRATE);
			//	alarm.setStreamVolume(AudioManager.STREAM_MUSIC, cur_vol, AudioManager.FLAG_VIBRATE);
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
		long nxttime=0,nxtalarmtime=0,extratime=0,temp=0;
		Calendar cal = Calendar.getInstance();
		final long cur_time = cal.getTimeInMillis();
		SharedPreferences.Editor alarmprefs = getBaseContext().getSharedPreferences("GTAcampuS", MODE_PRIVATE).edit();
		int hour,minute;
		boolean alertflag=false;
		db=new DataManipulator(this);
		Cursor en_alarms = db.fetchenabledalarms();
		if(en_alarms!=null)
			{
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
			if((nxttime<nxtalarmtime ||nxtalarmtime==0)&& nxttime>cur_time)
			{
				nxtalarmtime = nxttime;
				alarmprefs.putInt("alarmid", en_alarms.getInt(0));
				alarmprefs.putString("alarmtype", en_alarms.getString(7));
				alarmprefs.putString("alarmtitle", en_alarms.getString(6));
				alarmprefs.putInt("alarmsnooze", en_alarms.getInt(9));
				alarmprefs.putBoolean("alarmshake", en_alarms.getInt(10)!=0);
				alarmprefs.putBoolean("alarmmath", en_alarms.getInt(11)!=0);
				alarmprefs.commit();
				temp=nxtalarmtime;
				alertflag=true;
			}
			en_alarms.moveToNext();
		}
		}
		en_alarms.close();
		Cursor slotstat = db.slotstat();
		int count=0,day;
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		switch(cal.get(Calendar.DAY_OF_WEEK)){
		case Calendar.MONDAY:
			slotstat.moveToPosition(0);
			break;
		case Calendar.TUESDAY:
			slotstat.moveToPosition(1);
			break;
		case Calendar.WEDNESDAY:
			slotstat.moveToPosition(2);
			break;
		case Calendar.THURSDAY:
			slotstat.moveToPosition(3);
			break;
		case Calendar.FRIDAY:
			slotstat.moveToPosition(4);
			break;
		case Calendar.SATURDAY:
			count+=2;
			slotstat.moveToFirst();
			break;
		case Calendar.SUNDAY:
			count++;
			slotstat.moveToFirst();
			break;
		default: slotstat.moveToFirst();
			break;
		}
		ContentValues times = new ContentValues();	
		
		do{
			
		for(int i=1;i<slotstat.getColumnCount();i++)
		{
			if(!(slotstat.getString(i).equals("-")))
			{
				times = db.get_time(i);
				cal = Calendar.getInstance();
				cal.set(Calendar.HOUR, (times.getAsInteger("hour"))%12);
				cal.set(Calendar.MINUTE, times.getAsInteger("minute"));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.AM_PM, times.getAsInteger("am_pm"));
			//	cal.set(Calendar.DAY_OF_MONTH, day);
			//	cal.add(Calendar.DAY_OF_MONTH, count);
				extratime = count*24*60*60000;
				nxttime = cal.getTimeInMillis() + extratime;
				if(nxttime>(cur_time+tenmins) &&(nxttime<nxtalarmtime ||nxtalarmtime==0)&& ((nxttime>endtime)||endtime==0))
				{
					alarmprefs.putLong("coursetime", nxttime);
					nxtalarmtime = nxttime-tenmins;
					alarmprefs.putInt("alarmid", 10000);
					alarmprefs.putString("alarmtype", "course");
					alarmprefs.putString("alarmtitle", slotstat.getString(i));
					alarmprefs.putInt("alarmsnooze", 3);
					alarmprefs.putBoolean("alarmshake", true);
					alarmprefs.putBoolean("alarmmath", true);
					alarmprefs.putLong("endtime", (times.getAsLong("endtime")+extratime));
					alarmprefs.commit();
				}
				if(nxttime/1000==endtime/1000)
				{
					endtime=times.getAsLong("endtime")+extratime;
					nxtalarmtime=0;
				}
			}
		}
		slotstat.moveToNext();
		count++;
		if(slotstat.isAfterLast()){
			slotstat.moveToFirst();
			count+=2;}
		}while((nxtalarmtime==0)&&(count<=7));
		slotstat.close();
		db.close();
		if(temp>(cur_time+tenmins) && alertflag && (temp<endtime))
		{
			alarmnotification = new Notification(R.drawable.gtacampus, "Skipping Alerts", temp);
			alarmnotification.setLatestEventInfo(this, "GTAcampuS","You will miss some alerts set during the class hours" ,null);
			alarmnotifier.notify(ALARM_NOTIFICATION, alarmnotification);
		}
		return nxtalarmtime;
		}
	}