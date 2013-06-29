package com.example.gtacampus;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Textviewer extends Activity {
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.textviewer);
		this.setTitle(getIntent().getStringExtra("title"));
		TextView text = (TextView) findViewById(R.id.textviewer);
		text.setText(getIntent().getStringExtra("text"));
		Button back = (Button)findViewById(R.id.goback);
		back.setOnClickListener(goback);
	};

	View.OnClickListener goback = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Textviewer.this.finish();
		}
	};
}
