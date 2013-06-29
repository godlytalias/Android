package com.example.gtacampus;


import java.text.NumberFormat;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.ContentValues;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import android.widget.WrapperListAdapter;

public class gtaconvertor extends Activity {
	
	ContentValues values;
	Spinner category,sp_conv,sp_convr;
	ArrayAdapter<String> spinneradapter;
	EditText conv_value;
	TextView conv_selcat1,conv_selcat2,convr_value;
	String[] items;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convertor);
		values = new ContentValues();
		conv_selcat1=(TextView) findViewById(R.id.conv_selcat1);
		conv_selcat2 = (TextView) findViewById(R.id.conv_selcat2);
		convr_value = (TextView) findViewById(R.id.convr_value);
		conv_value = (EditText) findViewById(R.id.conv_value);
		conv_value.setText("0.0");
		conv_value.setOnClickListener(clearbox);
		category = (Spinner) findViewById(R.id.conv_cat);
		sp_conv = (Spinner) findViewById( R.id.sp_conv);
		sp_convr = (Spinner) findViewById(R.id.sp_convr);
		
		category.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case 0: //length
					items = new String[]{"Meter","KiloMeter","Mile","Yard","Foot","Centimeter","Inch","Nautical Mile","Millimetre","Light Year"};
					break;
				case 1: //mass
					items = new String[]{"Gram","Kilogram","Milligram","Stone", "Pound", "Ounce","Tray Ounce","Carat", "Mcg", "Short ton", "Long ton", "Metric Ton"};
					break;
				case 2: //speed
					items = new String[]{"Miles/hr", "Feet/sec", "meter/sec", "Km/hr", "Knot"};
					break;
				case 3: //area
					items = new String[]{"Sq.m","Sq.Km", "Sq.mile","Sq.yard","Sq.ft","Sq.inch","Sq.cm","Cent","Acre","Are","Hectare"};
					break;
				case 4: //volume
					items = new String[]{"Litre","Millilitre","Cubic.m","Cubic.ft","Cubic.inch","Cubic.cm","Cubic.yard","us gal","us pint","us cup","us quart","us oz","us tbsp","us tsp","imp. gal","imp. pint","imp. quart","imp. oz","imp. tbsp","imp. tsp"};
					break;
				case 5: //time
					items = new String[]{"Second","Nanosecond","Microsecond","Millisecond","Minute","Hour","Day","Week","Month","Year","Decade","Century"};
					break;
				case 6: //temperature
					items = new String[]{"Celsius","Fahrenheit","Kelvin"};
					break;
				case 7: //digital storage
					items = new String[]{"bit","byte","Kilobit","KiloByte","Megabit","MegaByte","Gigabit","GigaByte","Terabit","TeraByte","Petabit","PetaByte"};
					break;
				case 8: //fuel consumption
					items = new String[]{"Km/l","mpg(us)","mpg(imp)"};
					break;
				default: break;
				}
				spinneradapter = new ArrayAdapter<String>(gtaconvertor.this, R.layout.spinner, items);
				sp_conv.setAdapter(spinneradapter);
				sp_convr.setAdapter(spinneradapter);
				conv_selcat1.setText(items[0]);
				conv_selcat2.setText(items[0]);
				}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				category.setSelection(0);
			}
		}); 
		
	
	sp_conv.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			conv_selcat1.setText(items[arg2]);
			updateval();
					}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			conv_selcat1.setText(items[0]);
		}
	});
	
	sp_convr.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			conv_selcat2.setText(items[arg2]);
			updateval();
					}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

			conv_selcat2.setText(items[0]);
			sp_convr.setSelection(0);
		}
	});
	
	conv_value.setOnEditorActionListener(new OnEditorActionListener() {
		@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
			updateval();
		return false;
	}
});
	
	}
	
	View.OnClickListener clearbox = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			conv_value.setText("");
		}
	};
	
	private void updateval(){
		String text = new String();
		switch(category.getSelectedItemPosition()){
		case 0: text =""+ length(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 1: text =""+ mass(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 2: text =""+ speed(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 3: text =""+ area(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 4: text =""+ volume(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 5: text =""+ time(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 6: text =""+ temper(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 7: text =""+ storage(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		case 8: text =""+ fuel(Double.parseDouble(conv_value.getText().toString()), conv_selcat1.getText().toString(), conv_selcat2.getText().toString());
				break;
		default:break;}
		convr_value.setText(text);
	}
		
	
	private double length(double val,String giv,String ret){
		double res,temp;  //Normalised with meter
		values.put("Meter", 1.0); 
		values.put("KiloMeter", 0.001); 
		values.put("Mile", 0.00062137); 
		values.put("Yard", 1.0936133); 
		values.put("Foot", 3.2808399); 
		values.put("Centimeter", 100.00); 
		values.put("Inch", 39.370079); 
		values.put("Nautical Mile", 0.0005399568); 
		values.put("Millimetre", 1000);
		values.put("Light Year",(1/9460800000000000.0));
		temp=values.getAsDouble(ret)/values.getAsDouble(giv);
		res=temp*val;
		return res;
	}
	
	private double mass(double val,String giv,String ret){
		//normalised with gram
		double res,temp;
		values.put("Gram", 1.0);
		values.put("Milligram", 0.001);
		values.put("Kilogram", 1000.0);
		values.put("Carat", 0.2);
		values.put("Tray Ounce", 31.1035);
		values.put("Stone", 6350.29);
		values.put("Pound", 453.592);
		values.put("Ounce", 28.3495);
		values.put("Mcg",0.000001);
		values.put("Metric Ton",1000000.0);
		values.put("Long ton",1016050.0);
		values.put("Short ton", 97185.0);
		temp=values.getAsDouble(giv)/values.getAsDouble(ret);
		res=temp*val;
		return res;
	}
	
	private double speed(double val, String giv, String ret){
		//normalised with km/hr
		double res,temp;
		values.put("Km/hr", 1.0);
		values.put("Knot",1.852);
		values.put("meter/sec",0.27777778);
		values.put("Miles/hr",0.62137);
		values.put("Feet/sec",0.91134442);		
		
		temp=values.getAsDouble(ret)/values.getAsDouble(giv);
		res=temp*val;
		return res;
	}
	
	private double area(double val, String giv,String ret){
		//normalised with Sq.m
		double res,temp;
		values.put("Sq.m",1.0);
		values.put("Sq.Km",1000000.0);
		values.put("Sq.mile",2589990.0);
		values.put("Acre", 4046.86);
		values.put("Sq.cm", 0.000001);
		values.put("Cent", 40.4686);
		values.put("Are",100.0);
		values.put("Sq.yard",0.836127);
		values.put("Sq.ft",0.092903);
		values.put("Sq.inch",0.00064516);
		values.put("Hectare",10000.0);
		
		temp=values.getAsDouble(giv)/values.getAsDouble(ret);
		res=temp*val;
		return res;
	}
	
	private double volume(double val, String giv, String ret){
		//normalised with liter
		double res,temp;
		values.put("Litre",1.0);
		values.put("Millilitre",0.001);
		values.put("Cubic.m", 1000.0);
		values.put("Cubic.cm", 1000000000.0);
		values.put("Cubic.ft",28.3168);
		values.put("Cubic.yard", 764.55);
		values.put("Cubic.inch",0.0163871);
		values.put("us gal",3.785412);
		values.put("us pint",0.4731765);
		values.put("us cup",0.236588);
		values.put("us quart",0.946353);
		values.put("us oz",0.0295735);
		values.put("us tbsp",0.0147868);
		values.put("us tsp",0.00492892);
		values.put("imp. gal",4.54609);
		values.put("imp. pint",0.568261);
		values.put("imp. quart", 1.13652);
		values.put("imp. oz",0.0284131);
		values.put("imp. tbsp", 0.0177582);
		values.put("imp. tsp",0.00591939);
		
		temp=values.getAsDouble(giv)/values.getAsDouble(ret);
		res=temp*val;
		return res;
	}

	private double time(double val, String giv, String ret){
		//normalised with second
		double res,temp;
		values.put("Second",1.0);
		values.put("Nanosecond",0.000000001);
		values.put("Microsecond",0.000001);
		values.put("Millisecond",0.001);
		values.put("Minute",60.0);
		values.put("Hour",3600.0);
		values.put("Day",86400.0);
		values.put("Week",604800.0);
		values.put("Month", 18144000.0);
		values.put("Year", 220752000.0);
		values.put("Decade", 2207520000.0);
		values.put("Century", 22075200000.0);
		
		temp=values.getAsDouble(giv)/values.getAsDouble(ret);
		res=temp*val;
		return res;
	}
	
	private double temper(double val, String giv, String ret){
	//	"Celsius","Fahrenheit","Kelvin"
		if(ret.equals("Celsius")){
			if(giv.equals("Fahrenheit")){
				return (val-32.0)*(5.0/9.0);
			}
			else if(giv.equals("Kelvin")){
				return (val-273.15);
			}
		}
		if(ret.equals("Kelvin")){
			if(giv.equals("Fahrenheit")){
				return ((val-32.0)*(5.0/9.0))+273.15;
			}
			else if(giv.equals("Celsius")){
				return (val+273.15);
			}
		}
		
		if(ret.equals("Fahrenheit")){
			if(giv.equals("Celsius")){
				return ((9.0/5.0)*val)+32;
			}
			if(giv.equals("Kelvin")){
				return ((val-273.15)*(9.0/5.0))+32;
			}
		}
		
		return val;
	}
	private double fuel(double val,String giv, String ret){
		//normalised with mpg(us)
		double res, temp;// "Km/l","mpg(us)","mpg(imp)"
		values.put("mpg(us)", 1.0);
		values.put("mpg(imp)", 1.20095);
		values.put("Km/l",0.425144);
		
		temp=values.getAsDouble(ret)/values.getAsDouble(giv);
		res=temp*val;
		return res;
	}
	
	private double storage(double val, String giv, String ret){
	//normalised with MB
		double res, temp;
		values.put("MegaByte", 1.0);
		values.put("Megabit",0.125);
		values.put("GigaByte",1024.0);
		values.put("Gigabit",8192.0);
		values.put("Terabit",8388608.0);
		values.put("TeraByte",1048576.0);
		values.put("Petabit", 8589934592.0);
		values.put("PetaByte",1073741824.0);
		values.put("KiloByte",(1.0/1024.0));
		values.put("Kilobit",(0.125/1024.0));
		values.put("bit",(0.125/(1024.0*1024.0)));
		values.put("byte", (1.0/(1024.0*1024.0)));
		
		temp=values.getAsDouble(giv)/values.getAsDouble(ret);
		res=temp*val;
		return res;
	}
}
