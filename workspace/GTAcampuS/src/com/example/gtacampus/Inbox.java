package com.example.gtacampus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread.State;
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

import com.project.gtacampus.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
	NetworkInfo mWifi,mob,lan;
	Integer pos;
	SharedPreferences servraddr;
	final int loading=0,reading=1,CONFIG=2,checking=3;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	servraddr= getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
	setContentView(R.layout.notes);
	msgs = new ArrayList<String>();
	button = (Button) findViewById(R.id.button1);
	button.setText("New Message");
	button.setOnClickListener(compose);
	ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	lan=connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
	mob = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	
	if(mWifi.isConnected()||mob.isConnected()||lan.isConnected()){
	if(checkfiles("gtacinbox.php")){
	Thread net = new Thread(networkthread);
	showDialog(loading);
	net.setDaemon(true);
	net.start();
	while(!(net.getState()==Thread.State.TERMINATED)){
	}
	dismissDialog(loading);
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
	 else{
		 showDialog(CONFIG);
	 }
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
	if(mWifi.isConnected()||mob.isConnected() || lan.isConnected()){
		if(!checkfiles("gtacinbox.php"))
		{
			showDialog(CONFIG);
		}
		else{
			showDialog(reading);
	Thread msg = new Thread(msgreadthread);
	msg.setDaemon(true);
	msg.start();
	while(!(msg.getState()==Thread.State.TERMINATED)){}
	dismissDialog(reading);
	Intent read = new Intent(Inbox.this,Textviewer.class);
	read.putExtra("text", result);
	read.putExtra("title", parts[1]);
	startActivity(read);
	}}
	else
		Toast.makeText(getBaseContext(), "Internet not available!", Toast.LENGTH_LONG).show();
}

private Runnable msgreadthread = new Runnable() {
	
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(servraddr.getString("server", null)+"gtacread.php");

		
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
				result = "Problems in connecting to server, check your connection";
				// TODO Auto-generated catch block
				} catch (IOException e) {
				result = "Problems in connecting to server, check your connection";
				// TODO Auto-generated catch block
				}
		
	}
};

	
private Runnable networkthread = new Runnable() {
	
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(servraddr.getString("server", null)+"gtacinbox.php");

		
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


public boolean checkfiles(final String filename){

result=new String();
Thread checkthread = new Thread(new Runnable() {
	
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(servraddr.getString("server", null)+"check.php");

		
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			SharedPreferences user = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
			nameValuePairs.add(new BasicNameValuePair("FILE", filename));
	
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
			}});

showDialog(checking);
checkthread.setDaemon(true);
checkthread.start();
while(checkthread.getState()!=State.TERMINATED){}
dismissDialog(checking);

if(result.equals("true"))
	return true;
else
	return false;
}


View.OnClickListener compose = new View.OnClickListener() {
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mWifi.isConnected() || mob.isConnected()|| lan.isConnected()) {
			if(checkfiles("gtacampus.php")){
		Intent i= new Intent(Inbox.this,Messages.class);
		startActivity(i);
		finish();}
			else
				Toast.makeText(getBaseContext(),"Web Service file not found!. Configure your server properly" , Toast.LENGTH_LONG).show();
				
				
		}
		else
			Toast.makeText(getBaseContext(), "Get an active Internet connection first", Toast.LENGTH_LONG).show();
	}
};

protected Dialog onCreateDialog(int id) {
	ProgressDialog dialog = new ProgressDialog(Inbox.this);
	dialog.setIndeterminate(true);
	dialog.setCancelable(true);
	switch(id){
	case reading:
	dialog.setMessage("Reading Message...");
	return dialog;
	
	case checking:
		dialog.setMessage("Checking services...");
		return dialog;
	
	case CONFIG:
		AlertDialog.Builder builder= new AlertDialog.Builder(Inbox.this);
		builder.setMessage("The web-service file is not found in the given location. Configure your server properly")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				removeDialog(2);
			}
		});
	
		return builder.create();
	case loading:
		dialog.setMessage("Loading Messages. This may take some time depending on your connection speed");
		return dialog;
	};

return null;
	
	}
}