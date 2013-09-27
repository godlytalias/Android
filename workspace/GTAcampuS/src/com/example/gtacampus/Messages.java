package com.example.gtacampus;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Messages extends Activity {
	
	EditText editText1,editText2;
	TextView tv1, tv2;
	Button button1;
	String response,result;
	Handler myhandler;
	SharedPreferences servraddr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnotes);
		servraddr = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
		myhandler = new Handler();
		editText1 = (EditText) findViewById(R.id.editText1);
		editText2 = (EditText) findViewById(R.id.editText2);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv1.setVisibility(View.GONE);
		editText1.setVisibility(View.GONE);
		tv2.setText("Message");
		editText2.setHint("Message");
		button1 = (Button) findViewById(R.id.button1);
		button1.setText("Send");
		button1.setOnClickListener(save);
		
	}
	
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

checkthread.setDaemon(true);
checkthread.start();
showDialog(3);
while(checkthread.getState()!=State.TERMINATED){}
dismissDialog(3);

if(result.equals("true"))
	return true;
else
	return false;
}

	
	private Runnable networkthread = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(servraddr.getString("server", null)+"gtacampus.php");

			
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				SharedPreferences user = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
				nameValuePairs.add(new BasicNameValuePair("sender", user.getString("Username", "GTAcampuS User").replace("'", "''")));
				nameValuePairs.add(new BasicNameValuePair("message", editText2.getText().toString().replace("'", "''")));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = httpclient.execute(httppost, responseHandler);				
			}
				catch (ClientProtocolException e) {
					response = e.toString();
					// TODO Auto-generated catch block
					} catch (IOException e) {
					response = e.toString();
					// TODO Auto-generated catch block
					}
			myhandler.post(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					dismissDialog(1);
					showDialog(0);
				}
			});
		}
	};
	
	View.OnClickListener save = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!checkfiles("gtacampus.php")){
				showDialog(2);
			}
			else{
			showDialog(1);
			Thread nethread = new Thread(networkthread);
			nethread.start();
			}}
		};
		
		protected android.app.Dialog onCreateDialog(int id) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			ProgressDialog dialog = new ProgressDialog(Messages.this);
			switch(id){
			case 0: builder.setMessage(response)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					removeDialog(0);
					startActivity(new Intent(Messages.this,Inbox.class));
					finish();
				}
			});
			break;
			case 1:
				dialog.setMessage("Sending Message...");
				return dialog;
				
			case 3:
				dialog.setMessage("Checking services...");
				return dialog;

			case 2:
				builder.setMessage("The web-service file is not found in the given location. Configure your server properly")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						removeDialog(2);
					}
				});
			
			}
		
			
			
			return builder.create();
		};

}
