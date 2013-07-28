package com.example.gtacampus;

import java.io.IOException;
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
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Messages extends Activity {
	
	EditText editText1,editText2;
	TextView tv1, tv2;
	Button button1;
	String response;
	Handler myhandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnotes);
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
	
	private Runnable networkthread = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://athena.nitc.ac.in/~godly_bcs10/GTAcampuS/gtacampus.php");

			
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				SharedPreferences user = getSharedPreferences("GTAcampuSettings", MODE_PRIVATE);
				nameValuePairs.add(new BasicNameValuePair("sender", user.getString("Username", "GTAcampuS User")));
				nameValuePairs.add(new BasicNameValuePair("message", editText2.getText().toString()));

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
					showDialog(0);
				}
			});
		}
	};
	
	View.OnClickListener save = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Thread nethread = new Thread(networkthread);
			nethread.start();
			}
		};
		
		protected android.app.Dialog onCreateDialog(int id) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			switch(id){
			case 0: builder.setMessage(response)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					removeDialog(0);
					finish();
				}
			});
			}
			return builder.create();
		};

}
