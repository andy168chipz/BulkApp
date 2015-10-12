package com.app.andy.bulkapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andy on 10/11/2015.
 */
public class FoodItemArrayAdapter extends ArrayAdapter<FoodItem> {
	private final ArrayList<FoodItem> items;
	private Context mContext;

	public FoodItemArrayAdapter(Context context, ArrayList<FoodItem> objects) {
		super(context, R.layout.item_row_layout, objects);
		this.items = objects;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FoodItem item = getItem(position);

		//check if view is being reused, else init
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_row_layout, parent, false);
		}
		TextView foodNameTv = (TextView) convertView.findViewById(R.id.item_row_layout_nameItem);
		TextView calorieCountTv = (TextView) convertView.findViewById(R.id.item_row_layout_calorieItem);
		TextView dateTv = (TextView) convertView.findViewById(R.id.item_row_layout_dateItem);
		foodNameTv.setText(item.getName());
		calorieCountTv.setText(Double.toString(item.getCalorie()));
		dateTv.setText(item.getDate());
		return convertView;
	}
}


