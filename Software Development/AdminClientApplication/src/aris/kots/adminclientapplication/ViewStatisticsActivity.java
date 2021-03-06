package aris.kots.adminclientapplication;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class ViewStatisticsActivity extends Activity {
	ActionBar actionBar;
	String Device_id;
	String Interface_Name;
    DBAdapter db = new DBAdapter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_view_statistics);
		
		Device_id = getIntent().getExtras().getString("Device_Name");
		Interface_Name = getIntent().getExtras().getString("Interface_Name");
		
		actionBar = getActionBar();////
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));///
        // set title and Fonts
		int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        TextView yourTextView = (TextView) findViewById(titleId);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/COMICBD.TTF");
		yourTextView.setTypeface(tf);
		yourTextView.setTextColor(Color.WHITE);
		actionBar.setTitle(Interface_Name + " : " + Device_id);
		
  		final EditText Patterns = (EditText) findViewById(R.id.ViewPatternsStatistics);
  		final EditText Ips = (EditText) findViewById(R.id.ViewIpStatistics);

  		Patterns.setTypeface(tf);
  		Ips.setTypeface(tf);
  		String IpList = "Interface IP,Malicious IP,Frequency\n\n";
  		String PatternsList = "Interface IP,Malicious Pattern,Frequency\n\n";

		db.open();
		final Cursor c = db.getIPStatistics(Device_id, Interface_Name);
		if (c.moveToFirst()) {
			do {
				String temp = "";
				temp = c.getString(3) + " , " + c.getString(4) + " , " + c.getInt(5);
				IpList += temp + "\n";
			} while (c.moveToNext());
		}
		final Cursor c2 = db.getPatternStatistics(Device_id, Interface_Name);
		if (c2.moveToFirst()) {
			do {
				String temp = "";
				temp = c2.getString(3) + " , " + c2.getString(4) + " , " + c2.getInt(5);
				PatternsList += temp + "\n";
			} while (c2.moveToNext());
		}
		db.close();
		
		
  		Patterns.setText(PatternsList);
  		Ips.setText(IpList);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.view_statistics, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
