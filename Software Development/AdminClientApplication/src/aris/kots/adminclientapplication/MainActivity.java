package aris.kots.adminclientapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	DBAdapter db = new DBAdapter(this);
	String isAdmin = "no";
	String USERNAME = "";
	String PASSWORD = "";
    //For KSOAP
    private static final String SOAP_ACTION = "\"http://server/login\"" ;
    private static final String METHOD_NAME = "login";
    private static final String NAMESPACE = "http://server/";
    private static final String URL = "http://192.168.1.4:9999/CIService/CIService?WSDL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //fullscreen 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.activity_main);
     
        /*########################*/
        /* Creating The Data Base */
        /*########################*/  	 	
        try {        	
        	String destPath = "/data/data/" + getPackageName() + "/databases/MyDatabase";
        	
        	File f = new File(destPath);        	
        	if (!f.exists()) {        	
			    CopyDB( getBaseContext().getAssets().open("mydb"), 
					new FileOutputStream(destPath));
        	}
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
       // isAdmin = "no";
//        db.open();
//        db.insertTableUSERSDEVICES("Aris", "Nexus 5");
//        db.insertTableUSERSDEVICES("Aris", "Nexus 6");
//        db.insertTableUSERSDEVICES("Malakas", "Samsung Galaxy S3");
//        db.insertTableUSERSDEVICES("Aris", "iMac");
//        db.insertTableUSERSDEVICES("Aris", "iphone 6s");
//        db.insertTableUSERSDEVICES("Aris", "Samsung Galaxy S6");
//        db.insertTableUSERSDEVICES("Kalimeris", "nokia 3310");
//        db.insertTableUSERSDEVICES("Palamas", "iphone 2g");
//        db.insertTableUSERSDEVICES("Bosdelekidis", "LG G2");
//        db.insertTableUSERSDEVICES("Malakas", "Samsung Galaxy S5");
//        db.close();
//        
        db.open();
		final Cursor c = db.getLogInUser();
		
		if (c.moveToFirst()) {
			do {
				Toast.makeText(MainActivity.this,"welcome " + c.getString(0),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this,TabMenu.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    startActivity(intent);
			    finish();
			} while (c.moveToNext());
		}else{
//			Toast.makeText(MainActivity.this,"No user Saved",
//					Toast.LENGTH_SHORT).show();
		}
		db.close();
        ////////
       final EditText username = (EditText) findViewById(R.id.username);
       final EditText paswword = (EditText) findViewById(R.id.password);
       Button Login = (Button) findViewById(R.id.button_login);
       Button Register = (Button) findViewById(R.id.button_register);
       
        // set title and Fonts
		int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        TextView yourTextView = (TextView) findViewById(titleId);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/COMICBD.TTF");
		yourTextView.setTypeface(tf);
		yourTextView.setTextColor(Color.WHITE);
		username.setTypeface(tf);
		paswword.setTypeface(tf);
		Login.setTypeface(tf);
		Register.setTypeface(tf);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));
		
		//Button Login
		Login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					USERNAME = username.getText().toString();
					PASSWORD = paswword.getText().toString();
					if(isNetworkAvailable()){
						 new LongOperation().execute(USERNAME,PASSWORD);
					}else{
						Toast.makeText(MainActivity.this,"Error Try Again.",
								Toast.LENGTH_SHORT).show();
					}
					
					
			}
		});
		//Button Register
		Register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					//Reload Activity
					Intent intent = new Intent(MainActivity.this,Register.class);
					MainActivity.this.startActivityForResult(intent,0);  
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
			}
		});
        
    }

    private class LongOperation extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
        	
			SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
			request.addProperty("arg0", params[0]);
			request.addProperty("arg1", params[1]);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				
				final SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(MainActivity.this,response.toString(),
//								Toast.LENGTH_SHORT).show();
//					}
//				});
				int number = Integer.valueOf(response.toString());////////
				if(number != 0)
					return true;
				else
					return false;

			} catch (Exception e) {
				Log.e(":D", "Error Login", e);
			}
			return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
			if( result == true ){
				if(USERNAME.equals("admin"))
					isAdmin = "yes";
				db.open();
				db.deleteLogInUser();
				db.RememberLoginUser(USERNAME,PASSWORD,isAdmin);
				db.close();
				//Reload Activity
				Intent intent = new Intent(MainActivity.this,MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainActivity.this.startActivityForResult(intent,0);  
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}else{
				Toast.makeText(MainActivity.this,"Wrong Username or Password!",
						Toast.LENGTH_SHORT).show();
			}

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
    public void CopyDB(InputStream inputStream, OutputStream outputStream) 
    throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }
}
