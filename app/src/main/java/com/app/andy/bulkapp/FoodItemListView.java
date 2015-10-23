package com.app.andy.bulkapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
	private FoodItemArrayAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.list_view, container, false);
		return contentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		dataSource = new FoodItemDataSource(mContext);
		try {
			dataSource.open();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<FoodItem> items = (ArrayList) dataSource.getAllItems();
		adapter = new FoodItemArrayAdapter(mContext, items);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		final FoodItem item = (FoodItem) getListAdapter().getItem(position);
		final Dialog editDialog = new Dialog(mContext);
		editDialog.setContentView(R.layout.edit_item_dialog);
		final EditText foodEditText = (EditText) editDialog.findViewById(R.id.dialogFoodText);
		foodEditText.setText(item.getName());
		final EditText calorieEditText = (EditText) editDialog.findViewById(R.id.dialogCalorieText);
		calorieEditText.setText(Double.toString(item.getCalorie()));
		editDialog.show();

		Button doneButton = (Button) editDialog.findViewById(R.id.dialogDoneButton);
		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (foodEditText.getText().toString().equals("")) {
					Toast.makeText(mContext, R.string.warning_enter_food, Toast.LENGTH_SHORT).show();
					return;
				} else if (calorieEditText.getText().toString().equals("")) {
					Toast.makeText(mContext, R.string.warning_enter_calorie, Toast.LENGTH_SHORT).show();
					return;
				} else {
					item.setCalorie(Double.parseDouble(calorieEditText.getText().toString()));
					item.setName(foodEditText.getText().toString());
					dataSource.updateRows(item);
					adapter.notifyDataSetChanged();
					editDialog.dismiss();
				}
			}
		});

	}

}
