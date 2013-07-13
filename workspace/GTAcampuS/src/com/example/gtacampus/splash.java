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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.project.gtacampus.R;

public class splash extends Activity{
	private Thread splashscreen;
	public Animation fade;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		fade = AnimationUtils.loadAnimation(this, R.anim.magfade);
		setContentView(R.layout.splashscr);
		findViewById(R.id.appimage).startAnimation(fade);
		splashscreen = new Thread() {
			@Override
			public void run()
			{
				try{
					synchronized(this){
						wait(3000);
					}
				}
				catch(Exception e){}
				finally{
				Intent startpt=new Intent(splash.this,CampusActivity.class);
				startActivity(startpt);
				finish();			}}};
			splashscreen.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}	
}