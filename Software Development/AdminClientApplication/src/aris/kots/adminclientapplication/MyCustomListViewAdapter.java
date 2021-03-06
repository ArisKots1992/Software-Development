package aris.kots.adminclientapplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCustomListViewAdapter extends BaseAdapter {
	static class ViewHolder {
		public TextView text;
		public ImageView image;
	}

	LayoutInflater inflater;
	List<String> items;
	AssetManager am;
	String ColorPosition;
	public MyCustomListViewAdapter(Activity context, List<String> items, AssetManager asset,
			String ColorPostition) {
		super();
		am = asset;
		this.items = items;
		this.ColorPosition = ColorPostition;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub

		String item = items.get(position);

		View vi = convertView;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.listview_row, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) vi.findViewById(R.id.textView1);
			viewHolder.image = (ImageView) vi.findViewById(R.id.imageView1);
			vi.setTag(viewHolder);
		}else{
		}
		
		final ViewHolder holder = (ViewHolder) vi.getTag();
		
		
		
		if(ColorPosition.equals(item)){
			holder.text.setTextColor(Color.BLUE);
			holder.text.setText(item);
			holder.image.setBackgroundResource(R.drawable.unnamed);
		}else{
			holder.text.setText(item);
			holder.text.setTextColor(Color.BLACK);
			holder.image.setBackgroundResource(R.drawable.icon);
		}
		Typeface tf = Typeface.createFromAsset(am, "fonts/COMICBD.TTF");
		holder.text.setTypeface(tf);
	//	if(ColorPosition.equals(item))	
		//	holder.text.setText("SKATA");
		return vi;
	}

}