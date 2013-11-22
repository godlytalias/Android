package gta.radio.aviv;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
	
	Button play,stop,fb,twitter,mail;
	MediaPlayer avivplayer=null;
	SeekBar vol;
	TextView track;
	ImageView aviv;
	NetworkInfo internet;
	NotificationManager notifs;
	Notification notification;
	IcyStreamMeta streamMeta;
	MetadataTask2 metadataTask2;
	String title_artist;
	Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notifs = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                
        play=(Button) findViewById(R.id.play);
        play.setOnClickListener(avivstreaming);
        stop=(Button) findViewById(R.id.stop);
        stop.setOnClickListener(stopstreaming);
        vol=(SeekBar) findViewById(R.id.vol);
        vol.setOnSeekBarChangeListener(volchanged);
        vol.setProgress(10);
        track = (TextView) findViewById(R.id.playing_track);
        aviv = (ImageView) findViewById(R.id.avivradio);
        aviv.setOnClickListener(webpage);
        fb=(Button) findViewById(R.id.fb);
        fb.setOnClickListener(fbpage);
        twitter=(Button) findViewById(R.id.twitter);
        twitter.setOnClickListener(twitterpage);
        mail=(Button) findViewById(R.id.mail);
        mail.setOnClickListener(mailer);
       
       streamMeta = new IcyStreamMeta();
       metadataTask2=new MetadataTask2();

       checkinternet();
          
       
    }
    
    public void initialize_meta(){
    	 try {
    			streamMeta.setStreamUrl(new URL("http://s2.voscast.com:8452/"));
    		 metadataTask2.execute(new URL(("http://s2.voscast.com:8452/")));}
    	       catch (MalformedURLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}  
    }
    
    public void getMeta(){

        timer = new Timer();
        TimerTask timertask = new TimerTask() {
			
        	public void run() {

                URL url;
                //Message msg = handler.obtainMessage();
                try {
                     url = new URL("http://s2.voscast.com:8452");
                      IcyStreamMeta icy = new IcyStreamMeta(url);
               		streamMeta.refreshMeta();

                    title_artist=streamMeta.getStreamTitle();
                    track.setText(title_artist);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                               
                    }
            
		};
        timer.schedule(timertask, (long)0, (long)10000);

    } 
    
    public Boolean checkinternet()
    {
    	 ConnectivityManager conn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
         internet = conn.getActiveNetworkInfo();
         if(internet==null || !(internet.isConnected()))
         	{Toast.makeText(getBaseContext(), "Get an active Internet connection!", Toast.LENGTH_LONG).show();
         	return false;
         	}
         else
        	 return true;
    }
    
    SeekBar.OnSeekBarChangeListener volchanged = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			try{
				if(avivplayer==null)
				{
					avivplayer=new MediaPlayer();
				}
			avivplayer.setVolume(progress/(float)10.0, progress/(float)10.0);}
			catch(Exception e)
			{Log.d("AvivRadio","Mediaplayer not running");		}
		}
	};
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	try{
        	if(!avivplayer.isPlaying())
        		avivplayer.release();}catch(Exception e){}
    }
    
    public void stop()
    {
    	try{
    	avivplayer.stop();
    	timer.cancel();
    	notifs.cancel("RadioAVIV",0);}
    	catch(Exception e)
    	{Log.d("RadiAVIV","mediaplayer already stopped");}
    }
    
    View.OnClickListener webpage = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.avivradio.com"));
			startActivity(webintent);
		}
	};
    
	View.OnClickListener stopstreaming = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			stop();
			track.setText("Stopped");
		}
	};
	
    View.OnClickListener avivstreaming = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(checkinternet()){
				Intent avivintent = new Intent(getBaseContext(), MainActivity.class);
				PendingIntent radioaviv = PendingIntent.getActivity(getBaseContext(), 0, avivintent, 0);
				notification = new Notification(R.drawable.aviv, "RadioAVIV", System.currentTimeMillis());
				notification.setLatestEventInfo(getBaseContext(), "RadioAVIV", "You're now listening to RadioAVIV", radioaviv);
				
				initialize_meta();
			Uri myUri = Uri.parse("http://s2.voscast.com:8452");
			  try {
			   if (avivplayer==null) {
			    avivplayer = new MediaPlayer();
			    avivplayer.setDataSource(MainActivity.this, myUri); // Go to Initialized state
				   avivplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				   avivplayer.setOnPreparedListener(MainActivity.this);
				   avivplayer.setOnBufferingUpdateListener(MainActivity.this);

				   avivplayer.setOnErrorListener(MainActivity.this);
				   avivplayer.prepareAsync();
				   avivplayer.start();
					track.setText("Loading...");

					notifs.notify("RadioAVIV", 0, notification);
			   } 
			   else if(avivplayer.isPlaying())
				{
			    avivplayer.pause();
			    track.setText("Paused");
				}
				else{
				avivplayer.start();
				track.setText("Loading...");
				
				notifs.notify("RadioAVIV", 0, notification);
				
			     Log.d("AvivRadio", "LoadClip Done");}
			  } catch (Throwable t) {
			   Log.d("AvivRadio", t.toString());
			  }
		}}
	};
	
	@Override
	public void onPrepared(MediaPlayer avivplayer)
	{
		Log.d("AvivRadio","Prepared..Starting");
		avivplayer.start();
		track.setText("Playing...");
	}
    
    View.OnClickListener fbpage = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent fbintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/avivradio"));
			startActivity(fbintent);
		}
	};
	
	View.OnClickListener twitterpage = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent twintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/avivradio"));
			startActivity(twintent);
		}
	};
	
	View.OnClickListener mailer = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try{
			Intent mailintent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:info@avivradio.com"));
			startActivity(mailintent);
			}
			catch(Exception e)
			{
				Toast.makeText(getBaseContext(), "Action not supported. Mail us at info@avivradio.com", Toast.LENGTH_LONG).show();
			}
		}
	};


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onBufferingUpdate(MediaPlayer aviv, int percent) {
		// TODO Auto-generated method stub
		Log.d("AvivRadio","Buffering");
		if(percent<100)
		track.setText("Buffering...");
	}


	@Override
	public boolean onError(MediaPlayer aviv, int w, int e) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		  sb.append("Media Player Error: ");
		  switch (w) {
		  case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
		   sb.append("Not Valid for Progressive Playback");
		   break;
		  case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
		   sb.append("Server Died");
		   break;
		  case MediaPlayer.MEDIA_ERROR_UNKNOWN:
		   sb.append("Unknown");
		   break;
		  default:
		   sb.append(" Non standard (");
		   sb.append(w);
		   sb.append(")");
		  }
		  sb.append(" (" + w + ") ");
		  sb.append(e);
		  Log.e("AvivRadio", sb.toString());
		  return true;
	}


	@Override
	public void onCompletion(MediaPlayer aviv) {
		// TODO Auto-generated method stub
		stop();
		track.setText("Track over");
	}
	
	protected class MetadataTask2 extends AsyncTask<URL, Void, IcyStreamMeta> 
    {
        @Override
        protected IcyStreamMeta doInBackground(URL... urls) 
        {
            try 
            {
                streamMeta.refreshMeta();
                Log.e("Retrieving MetaData","Refreshed Metadata");
            } 
            catch (IOException e) 
            {
                Log.e(MetadataTask2.class.toString(), e.getMessage());
            }
            return streamMeta;
        }

        @Override
        protected void onPostExecute(IcyStreamMeta result) 
        {
            try 
            {
                title_artist=streamMeta.getStreamTitle();
                Log.e("Retrieved title_artist", title_artist);
                if(title_artist.length()>0)
                {
                    streamMeta.refreshMeta();
                }
            }
            catch (IOException e) 
            {
                Log.e(MetadataTask2.class.toString(), e.getMessage());
            }
        }
    }
    
}
