package aris.kots.adminclientapplication;

import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ArisTabCustom extends Fragment {
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
	public void CreateListView(final ArrayList<String> MyList){
		String selectedDevice;
		synchronized (this) {
			selectedDevice = TabMenu.SelectedDevice;
		}
		
		final MyCustomListViewAdapter adapter = new MyCustomListViewAdapter( getActivity(),MyList,getActivity().getAssets(),selectedDevice);
		// final ListAdapter adapter = new
		// ListAdapter(this,items,getScreenHeight(),getAssets());
		listView.setAdapter(adapter);
		//////////////
		
		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ListView Clicked item value
				final String itemValue = adapter.getItem(position);
				// Show Alert
				Toast.makeText(getActivity(), "Device Selected: " + itemValue,
						Toast.LENGTH_LONG).show();
				synchronized (this) {
					TabMenu.SelectedDevice = itemValue;
				}
				
				final MyCustomListViewAdapter adapter = new MyCustomListViewAdapter( getActivity(),MyList,getActivity().getAssets(),itemValue);
				listView.setAdapter(adapter);
			///	adapter.ColorPosition = itemValue;
		///		adapter.notifyDataSetChanged();
			//updateView(position);
			
				TabMenu.changeTABbyAris(1);
//			       new Thread() {
//			            @Override
//			            public void run() {
//
//			    				final MyCustomListViewAdapter adapter = new MyCustomListViewAdapter( getActivity(),MyList,getActivity().getAssets(),itemValue);
//			    			
//			                try {
//
//			                    // code runs in a thread
//			                	getActivity().runOnUiThread(new Runnable() {
//			                        @Override
//			                        public void run() {
//			                        	TabMenu.changeTABbyAris(1);
//			                        	listView.setAdapter(adapter);
//			                        }
//			                    });
//			                } catch (final Exception ex) {
//			                }
//			            }
//			        }.start();
				
			}
		});
	}
	private void updateView(int index){
	    View v = listView.getChildAt(index - 
	        listView.getFirstVisiblePosition());
	    if(v == null)
	       return;

	    TextView someText = (TextView) v.findViewById(R.id.textView1);
	    someText.setTextColor(Color.BLUE);
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
