package com.example.gtacampus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.project.gtacampus.R;
import android.app.ListActivity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Inbox extends ListActivity {

	Button button;
	List<String> msgs;
	String[] listadapter;
	InputStream istream;
	StringBuilder sbuilder;
	String result;
	Handler myhandler;
	NetworkInfo mWifi,mob;
	Integer pos;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.notes);
	msgs = new ArrayList<String>();
	button = (Button) findViewById(R.id.button1);
	button.setText("New Message");
	button.setOnClickListener(compose);
	ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	mob = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	
	if(mWifi.isConnected()||mob.isConnected()){
	Thread net = new Thread(networkthread);
	net.setDaemon(true);
	net.start();
	while(!(net.getState()==Thread.State.TERMINATED)){}
	listadapter = new String[msgs.size()];
	int i=0;
	for(String temp: msgs){
		listadapter[i]=temp;
		i++;
	}
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(   
			Inbox.this,android.R.layout.simple_list_item_1,   
			listadapter);
    Inbox.this.setListAdapter(adapter);
	}
	else
		Toast.makeText(getBaseContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
}

@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);
	String[] parts = listadapter[position].split(" . ");
	pos=Integer.parseInt(parts[0]);
	if(mWifi.isConnected()||mob.isConnected()){
	Thread msg = new Thread(msgreadthread);
	msg.setDaemon(true);
	msg.start();
	while(!(msg.getState()==Thread.State.TERMINATED)){}
	Intent read = new Intent(Inbox.this,Textviewer.class);
	read.putExtra("text", result);
	read.putExtra("title", parts[1]);
	startActivity(read);
	}
	else
		Toast.makeText(getBaseContext(), "Internet not available!", Toast.LENGTH_LONG).show();
}

private Runnable msgreadthread = new Runnable() {
	
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://athena.nitc.ac.in/~godly_bcs10/GTAcampuS/gtacread.php");

		
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("id", pos.toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			result = httpclient.execute(httppost, responseHandler);				
		}
			catch (ClientProtocolException e) {
				result = e.toString();
				// TODO Auto-generated catch block
				} catch (IOException e) {
				result = e.toString();
				// TODO Auto-generated catch block
				}
		
	}
};

	
private Runnable networkthread = new Runnable() {
	
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://athena.nitc.ac.in/~godly_bcs10/GTAcampuS/gtacinbox.php");

		
		try {
			// Add your data

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			istream = entity.getContent();
		}
			catch (ClientProtocolException e) {e.printStackTrace();
				} catch (IOException e) {e.printStackTrace();
				}
		
		try{
	        BufferedReader reader = new BufferedReader(new   InputStreamReader(istream,"iso-8859-1"),8);
	        int i=0;
	        while(((result=reader.readLine()) != null) && i<100){
	        	msgs.add(result);
	        }
	        
	        istream.close();
	      			// TODO Auto-generated method stub
					
				
	    }catch(Exception e){ e.printStackTrace(); }
		
	}
};


View.OnClickListener compose = new View.OnClickListener() {
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mob = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mWifi.isAvailable() || mob.isAvailable()) {
		Intent i= new Intent(Inbox.this,Messages.class);
		startActivity(i);}
		else
			Toast.makeText(getBaseContext(), "Turn on your Wi-fi and get a Internet connection first", Toast.LENGTH_LONG).show();
	}
};

	
	}
