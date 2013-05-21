package com.example.gtacampus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.widget.Toast;

public class MyAlarmBrdcst extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context,Intent intent)
	{
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "GTAcampuS");
		wl.acquire(120000);
		Toast.makeText(context, "Alarm Started", Toast.LENGTH_SHORT).show();
		MediaPlayer mediaplayer= MediaPlayer.create(context, R.raw.sundaychurch);
		 mediaplayer.start();
		 Intent i = new Intent(context,MyAlarm.class);
		 i.setAction("launchalarm");
		 context.startService(i);
	}
}


