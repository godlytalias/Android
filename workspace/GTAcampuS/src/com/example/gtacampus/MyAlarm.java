package com.example.gtacampus;



import java.util.Calendar;

import android.app.AlarmManager;
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
		long nxt_time = getnextalarmtime();
		Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
		if(alarms!=null)
		alarms.moveToFirst();
		String toaster = new String();
			PendingIntent pendingalarms = PendingIntent.getBroadcast(this, 0, intent1, 0);
			am = (AlarmManager) this.getSystemService(this
					.getApplicationContext().ALARM_SERVICE);
			am.cancel(pendingalarms);
			if(nxt_time!=0)
			am.set(AlarmManager.RTC_WAKEUP,nxt_time,pendingalarms);
			//toaster = toaster + String.format("%d %d %d %d %d\n", alarms.getInt(1), alarms.getInt(2), alarms.getInt(3), alarms.getInt(4), alarms.getInt(5));
			alarms.moveToNext();
	//	Toast.makeText(this, toaster, Toast.LENGTH_LONG).show();
		alarms.close();
	}
	
	public void snoozealarm()
	{
		SharedPreferences alpref=getSharedPreferences("GTAcampuS", MODE_PRIVATE);
	long snoozetime = System.currentTimeMillis() + ((alpref.getInt("snooze", 5))*60000);
	Intent i = new Intent(this,MyAlarmBrdcst.class);
	setalarm = PendingIntent.getBroadcast(getBaseContext(), 0, i, 0);
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
		stopSelf();
	}
	
	public void setalarm(Intent i)
		{
			long nxt_time = getnextalarmtime();
			Intent intent1 = new Intent(this, MyAlarmBrdcst.class);
			setalarm= PendingIntent.getBroadcast(this, 0, intent1,
					PendingIntent.FLAG_UPDATE_CURRENT);
		
			am = (AlarmManager) this.getSystemService(this
					.getApplicationContext().ALARM_SERVICE);
			am.cancel(setalarm);
			if(nxt_time!=0)
				am.set(AlarmManager.RTC_WAKEUP,nxt_time,setalarm);
			else
				Toast.makeText(getBaseContext(), "no alarms", Toast.LENGTH_SHORT).show();
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
	
	public long alarmgetmillis(int hour,int minute, ContentValues week)
	{
		Calendar cal = Calendar.getInstance();
		int day,month,year,dayofweek;
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		day = cal.get(Calendar.DAY_OF_MONTH);
		dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		
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
		
		cal.set(year, month, day, hour, minute, 0);
		
		return cal.getTimeInMillis();
	}
	
	public long getnextalarmtime()
	{
		Calendar cal = Calendar.getInstance();
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
			if((nxttime<nxtalarmtime && nxttime>0)||nxtalarmtime==0)
			{
				nxtalarmtime = nxttime;
				alarmprefs.putInt("alarmid", en_alarms.getInt(0));
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