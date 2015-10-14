package com.app.andy.bulkapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Andy on 10/11/2015.
 */
public class FoodItemListView extends ListFragment {

	private FoodItemDataSource dataSource;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.list_view, container, false);
		return contentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		dataSource = new FoodItemDataSource(mContext );
		try {
			dataSource.open();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<FoodItem> items = (ArrayList)dataSource.getAllItems();
		FoodItemArrayAdapter adapter = new FoodItemArrayAdapter(mContext , items);
		setListAdapter(adapter);
	}


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		FoodItem item = (FoodItem)getListAdapter().getItem(position);
		Toast.makeText(mContext , item.getName(), Toast.LENGTH_SHORT).show();
	}

}
