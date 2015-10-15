package com.app.andy.bulkapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the main screen of the app, it shows the date, calorie total and achieved
 * Created by Andy on 9/13/2015.
 */
public class MainFragment extends Fragment {

	private Button submit, showList;
	private EditText foodNameEdit, calorieCountEdit;
	private FoodItemDataSource dataSource;
	private static final String LOG_TAG = "Bulk Fragment";
	private ListShowListener showListener;

	public static interface ListShowListener {
		public void onListShow();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Log.e(LOG_TAG, "attached");
		try{
			showListener = (ListShowListener) context;
		}catch (ClassCastException e){
			throw new ClassCastException(context.toString() + " didnt implement!!!");
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		dataSource = new FoodItemDataSource(getActivity());
		try {
			dataSource.open();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		init(view);
		return view;
	}

	@Override
	public void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	public void onResume() {
		try {
			dataSource.open();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		super.onResume();
	}
	/**
	 * Initilize the view
	 *
	 * @param view
	 */
	private void init(View view) {
		TextView dateText = (TextView) view.findViewById(R.id.dateTextView);
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateText.setText(format.format(new Date()));
		foodNameEdit = (EditText) view.findViewById(R.id.foodEditText);
		calorieCountEdit = (EditText) view.findViewById(R.id.calEditText);
	}

	/**
	 * Return appropriate date format
	 * @return
	 */
	private String getDate() {
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return formatter.format(new Date()).toString();
	}

	/**
	 * The onClick listener
	 * @param v
	 */
	public void onClick(View v){
		switch(v.getId()){
			case R.id.submitButton:
				String foodName = foodNameEdit.getText().toString();
				String calorieCount = calorieCountEdit.getText().toString();
				if (foodName.equals("")) {
					Toast.makeText(getActivity().getBaseContext(), "You didn't enter a food name!", Toast.LENGTH_SHORT).show();
					return;
				} else if (calorieCount.equals("")) {
					Toast.makeText(getActivity().getBaseContext(), "You didn't enter a calorie count!", Toast.LENGTH_SHORT).show();
					return;
				}
				//do save action
				else {
					FoodItem item = new FoodItem(foodName, Double.parseDouble(calorieCount), getDate());
					long id = dataSource.insertFoodItem(item);
					if (id == -1) {
						Toast.makeText(getActivity().getBaseContext(), "There was an error inserting", Toast.LENGTH_SHORT).show();
						Log.v("saveAction:", foodName + " " + calorieCount);
					} else {
						Log.v(LOG_TAG, "Insert successful" + id);
					}
				}
				break;
			case R.id.showListButton:
				if (showListener != null) {
					showListener.onListShow();
				}
				break;
			case R.id.deleteAllButton:
				break;
			default:
				Log.e(LOG_TAG, "Invalid button");
				throw new IllegalArgumentException();
		}
	}
}
