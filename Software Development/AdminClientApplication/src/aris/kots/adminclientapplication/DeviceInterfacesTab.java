package aris.kots.adminclientapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceInterfacesTab extends Fragment {
	DBAdapter db;
	List<String> items;
	ListView listView;
	Bundle extras;
	String SelectedDevice = "";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View second = inflater.inflate(R.layout.second_tab, container, false);
		((TextView) second.findViewById(R.id.textView)).setText(" .... ");
		db = new DBAdapter(getActivity());
		
		final ArrayList<String> MyList = new ArrayList<String>();
		listView = (ListView) second.findViewById(R.id.listViewInterfaces);
		
		final Button addBtn = (Button) second.findViewById(R.id.button1);
		addBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				synchronized (this) {
					SelectedDevice = TabMenu.SelectedDevice;
				}
				((TextView) second.findViewById(R.id.textView)).setText("Selected Device: "+SelectedDevice);
				db.open();
				final Cursor c = db.getAllInterfacesByUser(SelectedDevice);
				if (c.moveToFirst()) {
					do {
						MyList.add(c.getString(0));
					} while (c.moveToNext());
				}
				db.close();
				
				//Activity activity = getActivity();
				//Toast.makeText(activity, SelectedDevice,Toast.LENGTH_SHORT).show();
				CreateListView(MyList);

			}
		});
//		items = new ArrayList<String>();
//		items.add("Nexus 5");
//		items.add("iphone 6");
//		items.add("galaxy s3");
//		items.add("galaxy s4");
//		items.add("HTC one plus");
//		items.add("imac");
//		items.add("Macbook Pro");
//		items.add("AlienWare");
//		items.add("Nexus 4");
//		final MyCustomListViewAdapter adapter = new MyCustomListViewAdapter( getActivity(),items,getActivity().getAssets(), "iphone 6");
//		// final ListAdapter adapter = new
//		// ListAdapter(this,items,getScreenHeight(),getAssets());
//		ListView lv = (ListView) second.findViewById(R.id.listViewInterfaces);
//		lv.setAdapter(adapter);
		
		
		return second;
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
				String itemValue = (String) listView.getItemAtPosition(position);
				// Show Alert
				Toast.makeText(getActivity(), "Interface " + itemValue,Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(getActivity(),ViewStatisticsActivity.class);
				extras = new Bundle();
				extras.putString("Interface_Name",itemValue);
				extras.putString("Device_Name",SelectedDevice);
				intent.putExtras(extras);
				startActivityForResult(intent, 0);
			}
		});
	}
	
}
