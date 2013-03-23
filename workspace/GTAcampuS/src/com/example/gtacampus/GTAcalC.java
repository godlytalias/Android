package com.example.gtacampus;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GTAcalC extends Activity {
	
	Double numbers[] = new Double[400];
	Double result;
	Integer numptr=0,decflag=0,gcdflag=0,gcdres=1,resflag=0;
	Character opnptr[]=new Character[400];
	TextView textchange;
	

	
	Button numzero;
	Button numone;
	Button numtwo;
	Button numthree;
	Button numfour;
	Button numfive;
	Button numsix;
	Button numseven;
	Button numeight;
	Button numnine;
	Button plus;
	Button gives;
	Button clr;
	Button dec;
	Button sqroot;
	Button power;
	Button gcd;
	Button minus;
	Button product;
	Button divide;
	
	public void arraymanip(Double numbers[],Character chr[],int ptr,int limit)
	{
	while(ptr<limit-1)
		{
			numbers[ptr]=numbers[ptr+1];
			chr[ptr]=chr[ptr+1];
			ptr++;
		}
		numbers[ptr]=numbers[ptr+1];
		
	}
	
	public int gcdfinder(int a,int b)
	{
		int tempvar;
		if(b<a)
		{
			a=a+b;
			b=a-b;
			a=a-b;
		}
		while(a%b!=0)
		{
			tempvar=a%b;
			a=b;
			b=tempvar;
				}
	return b;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
				super.onCreate(savedInstanceState);
				
				setContentView(R.layout.activity_gtacal_c);
		        
		
		textchange= (TextView)findViewById(R.id.text);
		textchange.setHint("Developed By Godly T.Alias\n\n");
		numbers[0]=0.0;

		numzero = (Button)findViewById(R.id.num0);
		numone = (Button)findViewById(R.id.num1);
		numtwo = (Button)findViewById(R.id.num2);
		numthree = (Button)findViewById(R.id.num3);
		numfour = (Button)findViewById(R.id.num4);
		numfive = (Button)findViewById(R.id.num5);
		numsix = (Button)findViewById(R.id.num6);
		numseven = (Button)findViewById(R.id.num7);
		numeight = (Button)findViewById(R.id.num8);
		numnine = (Button)findViewById(R.id.num9);
		plus=(Button)findViewById(R.id.add);
		gives=(Button)findViewById(R.id.equal);
		clr=(Button)findViewById(R.id.clear);
		dec=(Button)findViewById(R.id.decimal);
		divide=(Button)findViewById(R.id.div);
		power=(Button)findViewById(R.id.exp);
		gcd=(Button)findViewById(R.id.gcd);
		minus=(Button)findViewById(R.id.sub);
		product=(Button)findViewById(R.id.mul);
		sqroot=(Button)findViewById(R.id.sqrt);
		
		this.numzero.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				textchange.append("0");
				if(decflag==0)
				{
					numbers[numptr]=(numbers[numptr]*10)+0;}
					else
					{
					numbers[numptr]=numbers[numptr]+(0.0/decflag);
					decflag*=10;
					}
			}
		});
		
this.gcd.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(gcdflag==0)
		{
			textchange.setText("GCD(");
			gcdflag=1;
			Toast.makeText(GTAcalC.this,"GCD : Enter the number\nPress GCD \nto seperate between numbers",Toast.LENGTH_LONG).show();
			numptr=0;
			numbers[0]=0.0;
			decflag=0;
		}
		else
		{
			textchange.append(" , ");
			numptr++;
			numbers[numptr]=0.0;
			decflag=0;
		}
	}
});
		
this.dec.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(gcdflag==0)
		{
		decflag=10;
		textchange.append(".");}
		else
		{
			textchange.append(" , ");
			numptr++;
			numbers[numptr]=0.0;
			decflag=0;
		}
	}
});
		
this.sqroot.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(gcdflag==0)
				{
			Toast.makeText(GTAcalC.this," SQUARE ROOT ",Toast.LENGTH_SHORT).show();
				textchange.append("√");
				decflag=0;
				opnptr[numptr]='s';
			    numptr++;
				numbers[numptr]=0.0;
				}
				else
				{
					textchange.append(" , ");
					numptr++;
					numbers[numptr]=0.0;
					decflag=0;
				}
			}
		});

this.power.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(gcdflag==0)
		{
		Toast.makeText(GTAcalC.this," RAISED TO ",Toast.LENGTH_SHORT).show();
		textchange.append("^");
		decflag=0;
		opnptr[numptr]='^';
	    numptr++;
		numbers[numptr]=0.0;
		}
		else
		{
			textchange.append(" , ");
			numptr++;
			numbers[numptr]=0.0;
			decflag=0;
		}
	}
});

this.minus.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(gcdflag==0)
		{
		
		Toast.makeText(GTAcalC.this," - ", Toast.LENGTH_SHORT).show();
		textchange.append("-");
		opnptr[numptr]='-';
		numptr++;
		decflag=0;
		numbers[numptr]=0.0;
		}
		else
		{
			textchange.append(" , ");
			numptr++;
			numbers[numptr]=0.0;
			decflag=0;
		}
		
	}
});

this.product.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(gcdflag==0)
		{
		Toast.makeText(GTAcalC.this," * ", Toast.LENGTH_SHORT).show();
		textchange.append("*");
		opnptr[numptr]='*';
		numptr++;
		decflag=0;
		numbers[numptr]=0.0;
		}
		else
		{
			textchange.append(" , ");
			numptr++;
			numbers[numptr]=0.0;
			decflag=0;
		}
		
	}
});

this.divide.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(gcdflag==0)
		{
		Toast.makeText(GTAcalC.this," / ", Toast.LENGTH_SHORT).show();
		textchange.append("/");
		opnptr[numptr]='/';
		numptr++;
		decflag=0;
		numbers[numptr]=0.0;
		}
		else
		{
			textchange.append(" , ");
			numptr++;
			numbers[numptr]=0.0;
			decflag=0;
		}
		
	}
});
		
this.clr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if((numptr==0)&&(decflag==0)&&(gcdflag==0)&&(numbers[numptr]==0.0)&&(resflag==0))
				{
					System.exit(CONTEXT_INCLUDE_CODE);
				}
			Toast.makeText(GTAcalC.this," CLEARED ",Toast.LENGTH_SHORT).show();
				textchange.setText("");
				textchange.setHint("Developed By Godly T.Alias");
				numptr=0;
				decflag=0;
				gcdflag=0;
				resflag=0;
				numbers[numptr]=0.0;
			}
		});
		
this.numone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				textchange.append("1");
				if(decflag==0)
				numbers[numptr]=(numbers[numptr]*10)+1;
				else
				{
				numbers[numptr]=numbers[numptr]+(1.0/decflag);
				decflag*=10;
				}
			}
		});


this.numtwo.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		textchange.append("2");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+2;
			else
			{
			numbers[numptr]=numbers[numptr]+(2.0/decflag);
			decflag*=10;
			}
	}
});


this.numthree.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		textchange.append("3");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+3;
			else
			{
			numbers[numptr]=numbers[numptr]+(3.0/decflag);
			decflag*=10;
			}
	}
});

this.numfour.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		textchange.append("4");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+4;
			else
			{
			numbers[numptr]=numbers[numptr]+(4.0/decflag);
			decflag*=10;
			}
	}
});

this.numfive.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		textchange.append("5");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+5;
			else
			{
			numbers[numptr]=numbers[numptr]+(5.0/decflag);
			decflag*=10;
			}
	}
});

this.numsix.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		textchange.append("6");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+6;
			else
			{
			numbers[numptr]=numbers[numptr]+(6.0/decflag);
			decflag*=10;
			}
	}
});

this.numseven.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		textchange.append("7");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+7;
			else
			{
			numbers[numptr]=numbers[numptr]+(7.0/decflag);
			decflag*=10;
			}
	}
});

this.numeight.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		textchange.append("8");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+8;
			else
			{
			numbers[numptr]=numbers[numptr]+(8.0/decflag);
			decflag*=10;
			}
	}
});

this.numnine.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		textchange.append("9");
		if(decflag==0)
			numbers[numptr]=(numbers[numptr]*10)+9;
			else
			{
			numbers[numptr]=numbers[numptr]+(9.0/decflag);
			decflag*=10;
			}
	}
});

this.gives.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		int temp;
		Double store=0.0;
		resflag=1;
		// TODO Auto-generated method stub
	try{	
		temp=numptr-1;
		
		if(gcdflag==0)
		{
		do
		{
			switch(opnptr[temp])
			{
				case 's':
				numbers[temp+1]=Math.sqrt(numbers[temp+1]);
				numbers[temp]=numbers[temp+1];
				arraymanip(numbers,opnptr,temp,numptr-1);
				numptr--;
				break;
				
				case '^':
					numbers[temp+1]=Math.pow(numbers[temp], numbers[temp+1]);
					arraymanip(numbers,opnptr,temp,numptr);
					numptr--;
					break;
			default: break;
			}
			temp--;
		}while(temp>=0);
		temp=0;
		do
		{
			switch(opnptr[temp])
			{
			case '*': 
				if(numbers[temp+1]==0)
				{
					try{
					if(	opnptr[temp+1]=='-')
				{
				numbers[temp+2]=0.0-numbers[temp+2];
				arraymanip(numbers,opnptr,temp+1,numptr);
				numptr--;
				}
					}
				catch(Exception e)
				{
					//do nothing
				}}
			
				store=numbers[temp]*numbers[temp+1];
						numbers[temp+1]=store;
						arraymanip(numbers, opnptr, temp, numptr);
						numptr--;
			
						break;
			case '/': 
				
				if(numbers[temp+1]==0)
					try{
					if(	opnptr[temp+1]=='-')
				{
				numbers[temp+2]=0.0-numbers[temp+2];
				arraymanip(numbers,opnptr,temp+1,numptr);
				numptr--;
				}
					}
				catch(Exception e)
				{
					Toast.makeText(GTAcalC.this, "MATH ERROR", Toast.LENGTH_SHORT);
					textchange.setText("Division by 0 not allowed!");
				}
				
				store=numbers[temp]/numbers[temp+1];
				numbers[temp+1]=store;
				arraymanip(numbers,opnptr, temp,numptr);
				numptr--;
				
				
			
			break;
			default: temp++;
					break;
			}
			
			
		}while(temp<numptr);
		temp=0;
		result=numbers[0];
		if(numptr>0)
		{
	
		while(temp<numptr)
		{
			switch(opnptr[temp])
			{
			case '+': result+=numbers[temp+1];
						break;
			case '-': result-=numbers[temp+1];
						break;
			default:break;
						}
			temp++;
		}
		}
		
	
	Toast.makeText(GTAcalC.this,Double.toString(result),Toast.LENGTH_SHORT).show();
	textchange.setText("");
		textchange.setHint(Double.toString(result));
		}
		else
		{
			gcdres=(int)gcdfinder((int)Math.round(numbers[0]),(int)Math.round(numbers[1]));
			result=numbers[0]*numbers[1];
			result/=gcdres;
			temp=2;
			while(temp<=numptr)
			{
				gcdres=(Integer)gcdfinder((int)gcdres,(int)Math.round(numbers[temp]));
				result*=numbers[temp];
				result/=gcdres;
				temp++;
			}
			
			Toast.makeText(GTAcalC.this,Integer.toString(gcdres),Toast.LENGTH_SHORT).show();
			textchange.setText("");
				textchange.setHint("GCD = " + (Integer.toString(gcdres)) + "   LCM = " + (Double.toString(result)));
		}
	numptr=0;
	numbers[0]=0.0;
	decflag=0;
	gcdflag=0;
	}
	catch(Exception e)
	{
		Toast.makeText(GTAcalC.this,"SYNTAX ERROR",Toast.LENGTH_LONG).show();
		textchange.setText("SYNTAX ERROR");
	}
	}
});
this.plus.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(gcdflag==0)
		{
	Toast.makeText(GTAcalC.this," + ",Toast.LENGTH_LONG).show();
		textchange.append("+");
		opnptr[numptr]='+';
		numptr++;
		numbers[numptr]=0.0;
		decflag=0;
		}
		else
		{
			textchange.append(" , ");
			numptr++;
			numbers[numptr]=0.0;
			decflag=0;
		}
	}
});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
	
		
	}
