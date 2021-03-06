package aris.kots.adminclientapplication;

import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ArisTab extends Fragment {
	DBAdapter db;
	CheckBox mydevices;
	boolean isCHECKED = false;
	ListView listView;
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("curChoice", isCHECKED);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			isCHECKED = savedInstanceState.getBoolean("curChoice", false);
			mydevices.setChecked(isCHECKED);
		}
		final ArrayList<String> MyList = new ArrayList<String>();
		if (isCHECKED) {
			db.open();
			final Cursor c = db.getDevicesByUsername(TabMenu.USERNAME);
			if (c.moveToFirst()) {
				do {
					MyList.add(c.getString(0));
				} while (c.moveToNext());
			}
			db.close();
		} else {
			db.open();
			final Cursor c = db.getAllDevices();
			if (c.moveToFirst()) {
				do {
					MyList.add(c.getString(0));
				} while (c.moveToNext());
			}
			db.close();
		}
		CreateListView(MyList);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		final View aris = inflater.inflate(R.layout.first_tab, container, false);

		db = new DBAdapter(getActivity());
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/COMICBD.TTF");
		
		// ////////////
		// if we click My Devices
		// ////////////////
		
		
		listView = (ListView) aris.findViewById(R.id.listView);
		mydevices = (CheckBox) aris.findViewById(R.id.checkBox1);
		isCHECKED = mydevices.isChecked();
		mydevices.setTypeface(tf);
		mydevices.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				ArrayList<String> MyList = new ArrayList<String>();
				isCHECKED = mydevices.isChecked();
				// is it checked?
				if (((CheckBox) v).isChecked()) {
						db.open();
						final Cursor c = db.getDevicesByUsername(TabMenu.USERNAME);
						if (c.moveToFirst()) {
							do {
								MyList.add(c.getString(0));
							} while (c.moveToNext());
						}
						db.close();
				}else{
					db.open();
					final Cursor c = db.getAllDevices();
					if (c.moveToFirst()) {
						do {
							MyList.add(c.getString(0));
						} while (c.moveToNext());
					}
					db.close();
				}
				CreateListView(MyList);
			}
		});

		return aris;
	}
	public void CreateListView(ArrayList<String> MyList){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, MyList);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
		
		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ListView Clicked item value
				String itemValue = (String) listView
						.getItemAtPosition(position);
				// Show Alert
				Toast.makeText(getActivity(), "Device Selected: " + itemValue,
						Toast.LENGTH_LONG).show();
				synchronized (this) {
					TabMenu.SelectedDevice = itemValue;
				}
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
		        tv.setTextColor(Color.BLUE);
				TabMenu.changeTABbyAris(1);
			}
		});
	}
	
	@Override
	public void onPause() {
//		Toast.makeText(getActivity(), String.valueOf(isCHECKED),
//				Toast.LENGTH_LONG).show();
		super.onPause();
	}

	@Override
	public void onResume() {
//		Toast.makeText(getActivity(), "Resume!: " +  String.valueOf(isCHECKED) ,
//				Toast.LENGTH_LONG).show();
		super.onResume();
	}

}
