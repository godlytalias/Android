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
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import com.project.gtacampus.R;

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
	SharedPreferences alpref,settings;
	static boolean launchflag;
	long endtime;
	private int ALARM_NOTIFICATION =10;
	private static int alarmflag=1;
	private static long mins;
	
	@Override
	public void onCreate() {
		super.onCreate();
		launchflag=false;
		pm = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "keepAlive");
		wl.acquire();
		settings = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
		alarmnotifier = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
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
		super.onDestroy();
	}

	public void action(Intent i)
	{
		String actiontodo= i.getAction();
		if(actiontodo.equals("setalarm"))
			setalarm();
		else if(actiontodo.equals("launchalarm"))
			launchalarm(i);
		else if(actiontodo.equals("stop"))
			stopalarm();
		else if(actiontodo.equals("snooze"))
			snoozealarm();
		else if(actiontodo.equals("setbacksounds"))
			setsounds();
		else if(actiontodo.equals("bootsetalarm"))
			bootsetalarm();
	}
	
	
	public void bootsetalarm()
	{	
		Intent intent1 = new Intent(MyAlarm.this, MyAlarmBrdcst.class);
	intent1.setAction("set_alarm");
	PendingIntent pendingalarms = PendingIntent.getBroadcast(this, 0, intent1, 0);
	am = (AlarmManager)getSystemService(ALARM_SERVICE);
	am.cancel(pendingalarms);
		long nxt_time = getnextalarmtime();
		alarmnotifier.cancel("GTAcampuS", 88); //deleting any snooze notifications set
			if(nxt_time>0)
			pushalarm(nxt_time, pendingalarms);
		if(!launchflag)
		stopSelf();	
			}
	
	public void snoozealarm()
	{
		alpref=getSharedPreferences("GTAcampuS", MODE_PRIVATE);
		int mins=0;
		if(alpref.getString("alarmtype", "task").equals("course")){
			mins=settings.getInt("snoozetime", 3);
		}
		else
			mins=alpref.getInt("alarmsnooze", 5);
		long snoozetime = System.currentTimeMillis() + mins*60000;
		snoozealarm(snoozetime);
		stopSelf();
		}
	
	public void snoozealarm(long snoozetime)
	{
	launchflag=false;
	Intent i = new Intent(MyAlarm.this,MyAlarmBrdcst.class);
	i.setAction("set_alarm");
	setalarm = PendingIntent.getBroadcast(this, 0, i, 0);
	am = (AlarmManager)getSystemService(ALARM_SERVICE);
	am.cancel(setalarm);
	am.set(AlarmManager.RTC_WAKEUP, snoozetime, setalarm);
	//	am.setRepeating(AlarmManager.RTC_WAKEUP, snoozetime, 300000, setalarm);
	if(alarmalert!=null){
	try{alarmflag=1;
	alarmalert.stop();
	alarmalert=null;}
	catch(Exception e){
		alarmflag=1;
		alarmalert.stop();
	}}}
	

	public void stopalarm()
	{
		launchflag=false;
		if(alarmalert!=null)
		try{alarmflag=1;
		alarmalert.stop();
		alarmalert=null;}
		catch(Exception e){
			alarmflag=1;
			alarmalert.stop();
		}
		setalarm();
	}
	
	public void setalarm()
		{
		SharedPreferences.Editor alarmdetedit;
			long nxt_time = getnextalarmtime();
			Intent intent1 = new Intent(MyAlarm.this, MyAlarmBrdcst.class);
			intent1.setAction("set_alarm");
			setalarm= PendingIntent.getBroadcast(this, 0, intent1, 0);
		
			if(nxt_time>System.currentTimeMillis())
				pushalarm(nxt_time,setalarm);
			else
			{
				am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
				am.cancel(setalarm);
				alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
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
		String alarmtimedetails;
		am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		am.cancel(setalarm);
		alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
		am.set(AlarmManager.RTC_WAKEUP,time,setalarm);
		Intent resultintent;
		if(al.getString("alarmtype", "alarm").equals("course"))
			resultintent = new Intent(getBaseContext(),Slot.class);
		else
		resultintent = new Intent(getBaseContext(),Alarmsetter.class);
		resultintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent result = PendingIntent.getActivity(this, 0, resultintent,0);
		try{
		alarmtimedetails = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US)+ " , " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " +calendar.get(Calendar.DAY_OF_MONTH) + "  -  " + ((calendar.get(Calendar.HOUR)==0)?12:calendar.get(Calendar.HOUR)) + " : " + calendar.get(Calendar.MINUTE) + " " + ((calendar.get(Calendar.AM_PM)==1)?"PM":"AM");
		}
		catch(NoSuchMethodError e){
			alarmtimedetails = calendar.get(Calendar.DAY_OF_MONTH) + " / " +calendar.get(Calendar.MONTH) + "  -  " + ((calendar.get(Calendar.HOUR)==0)?12:calendar.get(Calendar.HOUR)) + " : " + calendar.get(Calendar.MINUTE) + " " + ((calendar.get(Calendar.AM_PM)==1)?"PM":"AM");
		}
		if(settings.getBoolean("notifications", true)){
		alarmnotification = new Notification(R.drawable.alarm,"Next alert on "+alarmtimedetails, System.currentTimeMillis());
		alarmnotification.setLatestEventInfo(this, "GTAcampuS", "Alert Name : " + al.getString("alarmtitle", "Custom Alert") + "   -   " + alarmtimedetails,result);
		alarmnotification.flags = Notification.FLAG_NO_CLEAR;
		alarmnotifier.notify("GTAcampuS",ALARM_NOTIFICATION, alarmnotification);}
		SharedPreferences.Editor alarmdetedit = getBaseContext().getSharedPreferences("GTAcampuS_alarmdet", MODE_PRIVATE).edit();
		alarmdetedit.putString("alarmdetails", alarmtimedetails);
		alarmdetedit.commit();
	}
	
	public void launchalarm(Intent i)
	{
		alarmnotifier.cancel("GTAcampuS",ALARM_NOTIFICATION);
		launchflag=true;
		AudioManager alarm = (AudioManager)getSystemService(AUDIO_SERVICE);
		alarm.setStreamVolume(AudioManager.STREAM_ALARM, alarm.getStreamVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_VIBRATE);
		alarmflag=0;
		Thread alarmhandler = new Thread(alarmdialog);
		alarmhandler.setDaemon(true);
		alarmhandler.start();
		playalarmtone();}
		

	
	private Ringtone getalarmtone(){
		Ringtone r = null;
		
		try{
			r=RingtoneManager.getRingtone(getBaseContext(), Uri.parse("android.resource://com.project.gtacampus/"+R.raw.myringtone));
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
	
	public void setsounds(){
		AudioManager alarm = (AudioManager)this.getSystemService(AUDIO_SERVICE);
		alarm.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		alarm.setStreamVolume(AudioManager.STREAM_ALARM, alarm.getStreamMaxVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_VIBRATE);
		alarm.setStreamVolume(AudioManager.STREAM_NOTIFICATION,alarm.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),0);
		alarm.setStreamVolume(AudioManager.STREAM_RING,alarm.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
		alarm.setStreamVolume(AudioManager.STREAM_DTMF, alarm.getStreamMaxVolume(AudioManager.STREAM_DTMF), 0);
		alarm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		alarmnotifier.cancel("GTAcampuS", 3);
		SharedPreferences.Editor settingedit = settings.edit();
		settingedit.putBoolean("coursealerts", true);
		settingedit.commit();
		setalarm();
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
			if(alarm.getStreamVolume(AudioManager.STREAM_ALARM)!=1)
			{
			cur_vol=2;
			do{
				alarm.setStreamVolume(AudioManager.STREAM_ALARM, cur_vol, AudioManager.FLAG_VIBRATE);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cur_vol = alarm.getStreamVolume(AudioManager.STREAM_ALARM)+1;				
		}while(cur_vol<=max_vol && cur_vol>2);
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
	
		
	public int getdayadd(ContentValues week,Boolean todayflag){
		Calendar cal = Calendar.getInstance();
		int addcount=0;
		if(!todayflag)
		{
			addcount=1;
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		while(true){
		if(dayofweek==Calendar.SUNDAY){
			if(week.getAsBoolean("sun"))
				break;
			else{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dayofweek=cal.get(Calendar.DAY_OF_WEEK);
				addcount+=1;
			}
		}
		
		else if(dayofweek==Calendar.MONDAY){
			if(week.getAsBoolean("mon"))
				break;
			else{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dayofweek=cal.get(Calendar.DAY_OF_WEEK);
				addcount+=1;
			}
		}
		
		else if(dayofweek==Calendar.TUESDAY){
			if(week.getAsBoolean("tue"))
				break;
			else{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dayofweek=cal.get(Calendar.DAY_OF_WEEK);
				addcount+=1;
			}
		}
		
		else if(dayofweek==Calendar.WEDNESDAY){
			if(week.getAsBoolean("wed"))
				break;
			else{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dayofweek=cal.get(Calendar.DAY_OF_WEEK);
				addcount+=1;
			}
		}
		
		else if(dayofweek==Calendar.THURSDAY){
			if(week.getAsBoolean("thu"))
				break;
			else{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dayofweek=cal.get(Calendar.DAY_OF_WEEK);
				addcount+=1;
			}
		}
		
		else if(dayofweek==Calendar.FRIDAY){
			if(week.getAsBoolean("fri"))
				break;
			else{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dayofweek=cal.get(Calendar.DAY_OF_WEEK);
				addcount+=1;
			}
		}
		
		else if(dayofweek==Calendar.SATURDAY){
			if(week.getAsBoolean("sat"))
				break;
			else{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dayofweek=cal.get(Calendar.DAY_OF_WEEK);
				addcount+=1;
			}
		}
	}
		return addcount;
		}
	
	
	public long alarmgetmillis(int hour,int minute, ContentValues week)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE,minute);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DAY_OF_YEAR, getdayadd(week,true));
		if(cal.getTimeInMillis()<System.currentTimeMillis())
			cal.add(Calendar.DAY_OF_YEAR, getdayadd(week,false));
		return cal.getTimeInMillis(); 
	}
	
	public long getnextalarmtime()
	{
		mins=settings.getInt("alerttime", 10)*60000;
		long nxttime=0,nxtalarmtime=0,extratime=0;
		endtime=0;
		Calendar cal = Calendar.getInstance();
		final long cur_time = cal.getTimeInMillis();
		SharedPreferences.Editor alarmprefs = getBaseContext().getSharedPreferences("GTAcampuS", MODE_PRIVATE).edit();
		int hour,minute;
		boolean cnullflag=true;
		
		try{
		db=new DataManipulator(this);
		
		
		if(settings.getBoolean("coursealerts", true)){
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
				cnullflag=false;
				times = db.get_time(i);
				cal = Calendar.getInstance();
				cal.set(Calendar.HOUR, (times.getAsInteger("hour"))%12);
				cal.set(Calendar.MINUTE, times.getAsInteger("minute"));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.AM_PM, times.getAsInteger("am_pm"));
				cal.set(Calendar.DAY_OF_MONTH, day);
			//	cal.add(Calendar.DAY_OF_MONTH, count);
				extratime = count*24*60*60000;
				nxttime = cal.getTimeInMillis() + extratime;
			if(nxttime>(cur_time+mins) &&(nxttime<nxtalarmtime ||(nxtalarmtime==0)))
				{
					nxtalarmtime = nxttime-mins;
					alarmprefs.putInt("alarmid", 10000);
					alarmprefs.putLong("coursetime", nxttime);
					alarmprefs.putString("alarmtype", "course");
					alarmprefs.putString("alarmtitle", slotstat.getString(i));
					alarmprefs.putBoolean("alarmshake", true);
					alarmprefs.putBoolean("alarmmath", true);
					endtime = times.getAsLong("endtime")+extratime;
					alarmprefs.putLong("endtime", endtime);
					alarmprefs.commit();
				}
				if(Math.abs((nxttime/1000.0)-(endtime/1000.0))<=(settings.getInt("interval", 5))*60)
				{
					endtime=times.getAsLong("endtime")+extratime;
					alarmprefs.putLong("endtime", endtime);
					alarmprefs.commit();
				}
			}
		}
		slotstat.moveToNext();
		count++;
		if(slotstat.isAfterLast()){
			slotstat.moveToFirst();
			count+=2;}
		}while((nxtalarmtime<System.currentTimeMillis())&&((count<=7)||!cnullflag));
		slotstat.close();}
		
		
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
				alarmprefs.putLong("coursetime", 0);
				alarmprefs.putInt("alarmsnooze", en_alarms.getInt(9));
				alarmprefs.putBoolean("alarmshake", en_alarms.getInt(10)!=0);
				alarmprefs.putBoolean("alarmmath", en_alarms.getInt(11)!=0);
				alarmprefs.commit();
			}
		en_alarms.moveToNext();	
		}}
		en_alarms.close();
		

		}
		catch(RuntimeException e){
			e.printStackTrace();
			nxtalarmtime=0;
		}
		db.close();
		return nxtalarmtime;
		}
	}