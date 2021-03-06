package aris.kots.adminclientapplication;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.FragmentTransaction;
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
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
public class TabMenu extends FragmentActivity {
	//My Vars
	Timer timer;
	static ViewPager Tab;
	static String SelectedDevice = "Nothing";
	static String USERNAME = "";
	static String PASSWORD = "";
	static String isAdmin;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;
    DBAdapter db;
    //For KSOAP
    private static final String SOAP_ACTION_LOGOUT = "\"http://server/logout\"" ;
    private static final String METHOD_NAME_LOGOUT = "logout";
	private static final String SOAP_ACTION_RETRIEVE = "\"http://server/retrieveStatistics\"";
	private static final String METHOD_NAME_RETRIEVE = "retrieveStatistics";
    private static final String NAMESPACE = "http://server/";
    private static final String URL = "http://192.168.1.4:9999/CIService/CIService?WSDL";
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tab_menu);
        db = new DBAdapter(this);
        //get user info
        db.open();
		final Cursor c = db.getLogInUser();
		
		if (c.moveToFirst()) {
			do {
				synchronized (this) {
					USERNAME = c.getString(0);
					PASSWORD = c.getString(1);
					isAdmin = c.getString(2);
					
				}
			} while (c.moveToNext());
		}else{
			Toast.makeText(TabMenu.this,"No user Logged in.",Toast.LENGTH_SHORT).show();
			finish();
		}
		db.close();
        // set title and Fonts
        
		int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        TextView yourTextView = (TextView) findViewById(titleId);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/COMICBD.TTF");
		yourTextView.setTypeface(tf);
		yourTextView.setTextColor(Color.WHITE);
		
        ///
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager(),isAdmin);
        Tab = (ViewPager)findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                      actionBar = getActionBar();
                      actionBar.setSelectedNavigationItem(position);                    }
                });
        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));
        actionBar.setTitle(USERNAME);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
      @Override
      public void onTabReselected(android.app.ActionBar.Tab tab,
          FragmentTransaction ft) {
        // TODO Auto-generated method stub
      }
      @Override
       public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
              Tab.setCurrentItem(tab.getPosition());
          }
      @Override
      public void onTabUnselected(android.app.ActionBar.Tab tab,
          FragmentTransaction ft) {
        // TODO Auto-generated method stub
      }};
      
      if(isAdmin.equals("yes")){
      //Add New Tab
    	  actionBar.addTab(actionBar.newTab().setText("All Devices List").setTabListener(tabListener));
    	  actionBar.addTab(actionBar.newTab().setText("Interfaces").setTabListener(tabListener));
    	  actionBar.addTab(actionBar.newTab().setText("Insert Malicious Pattern/Ip").setTabListener(tabListener));
    	  actionBar.addTab(actionBar.newTab().setText("Delete User").setTabListener(tabListener));

      }else{
    	  actionBar.addTab(actionBar.newTab().setText("All Devices List").setTabListener(tabListener));
    	  actionBar.addTab(actionBar.newTab().setText("Interfaces").setTabListener(tabListener));
      }
      final Handler mHandler = new Handler();
//      if (android.os.Build.VERSION.SDK_INT > 9) 
//      {
//          StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//          StrictMode.setThreadPolicy(policy);
//      }
      final Handler handler = new Handler();
      timer = new Timer();
      TimerTask task = new TimerTask() {       
           @Override
           public void run() {
             handler.post(new Runnable() {
                public void run() {  
                   new LongOperation().execute();
                }
              });
            }
      };
      
      timer.schedule(task, 0, 5000); //it executes this every 1000ms

    }
    public static void changeTABbyAris(int tabNumber){
    	Tab.setCurrentItem(tabNumber);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab_menu, menu);
		return true;
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			db.open();
			db.deleteLogInUser();
			db.close();
			SelectedDevice = "Nothing";
			//LOGOUT SOAP
			Intent intent = new Intent(TabMenu.this,MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    startActivity(intent);
		//    Runtime.getRuntime().halt(0);//kill the periodic thread-loop
		    timer.cancel();
		    finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
		
        super.onDestroy();
        timer.cancel();
       // Runtime.getRuntime().halt(0);
      //  Thread.currentThread().interrupt();
    }
    private class LongOperation extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
        	
      	  if(isNetworkAvailable()){
      			
				SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME_RETRIEVE);
				request.addProperty("arg0", USERNAME);
				request.addProperty("arg1",PASSWORD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				final ArrayList<StatisticalReports> ListStatistics = new ArrayList<StatisticalReports>();
				try {


					envelope.dotNet = false;
					androidHttpTransport.call(SOAP_ACTION_RETRIEVE, envelope);
					final SoapObject response = (SoapObject) envelope.bodyIn;
					
//					runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(TabMenu.this, String.valueOf(response.getPropertyCount()),Toast.LENGTH_SHORT).show();
//					}
//					});
				//	SoapObject obj2 =(SoapObject) response.getProperty(0);

					String tempIP = "";
					String tempPatterns = "";
					for(int i=0; i<response.getPropertyCount(); i++)
					{
					//   SoapObject obj3 =(SoapObject) obj2.getProperty(0);
						SoapObject myObject = (SoapObject) response.getProperty(i);
						
					   String ipStatistics = myObject.getProperty(0).toString();
					   
					   String[] ipArray = ipStatistics.split("%%");
					   for(int j=0; j<ipArray.length; j++){
						   String[] ipInner = ipArray[j].split("#");
						   
						   if(ipInner.length == 5){
							   db.open();
							   db.insertTableIP(ipInner[0], ipInner[1], ipInner[2], ipInner[3], Integer.valueOf(ipInner[4]));
							   db.close();
						   }
						   
					   }
					   
					   String patternStatistics= myObject.getProperty(1).toString();
					   
					   String[] PatternArray = patternStatistics.split("%%");
					   for(int j=0; j<PatternArray.length; j++){
						   String[] ipInner = PatternArray[j].split("#");
						   
						   if(ipInner.length == 5){
							   db.open();
							   db.insertTablePATTERNS(ipInner[0], ipInner[1], ipInner[2], ipInner[3], Integer.valueOf(ipInner[4]));
							   db.close();
						   }
						   
					   }
					   tempIP += ipStatistics;
					   tempPatterns += patternStatistics;
					}
					final String rdy1 = tempIP;
					final String rdy2 = tempPatterns;
					
//					runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(TabMenu.this, "IP:\n" + rdy1 + " PATTERNS:\n" + rdy2,Toast.LENGTH_LONG).show();
//					}
//					});

					//Now Lets update our Database
					
				} catch (Exception e) {
					Log.e(":D", "I got an error", e);
					runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(TabMenu.this, "Excheption in AutoReload",Toast.LENGTH_SHORT).show();

					}
				});
				}  
      	  }
			return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
			if( result == true ){
//
//				Toast.makeText(TabMenu.this,"Hi!",
//						Toast.LENGTH_SHORT).show();
			}

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
class StatisticalReports {
	private String ips_statistics;
	private String pattern_statistics;

	public StatisticalReports(){}

	public StatisticalReports(String ips_statistics, String pattern_statistics) {
		this.ips_statistics = ips_statistics;
		this.pattern_statistics = pattern_statistics;
	}

	public String getIps_statistics() {
	return this.ips_statistics;
}

	public void setIps_statistics(String ips_statistics) {
		this.ips_statistics = ips_statistics;
	}

	public String getPattern_statistics() {
		return this.pattern_statistics;
	}

	public void setPattern_statistics(String pattern_statistics) {
		this.pattern_statistics = pattern_statistics;
	}
}