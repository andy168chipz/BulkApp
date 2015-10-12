package com.app.andy.bulkapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

	private Button submit;
	private EditText foodNameEdit, calorieCountEdit;
	private FoodItemDataSource dataSource;

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
		submit = (Button) view.findViewById(R.id.submitButton);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String foodName = foodNameEdit.getText().toString();
				String calorieCount = calorieCountEdit.getText().toString();
				if (foodName.equals("")) {
					Toast.makeText(getActivity().getBaseContext(), "You didn't enter a food name!", Toast.LENGTH_SHORT).show();
					return;
				} else {
					if (calorieCount.equals("")) {
						Toast.makeText(getActivity().getBaseContext(), "You didn't enter a calorie count!", Toast.LENGTH_SHORT).show();
						return;
					}
					//do save action here
					else {
						Log.v("saveAction:", foodName + " " + calorieCount);
						Log.v("saveaction test", getDate());
						FoodItem item = new FoodItem(foodName, Double.parseDouble(calorieCount), getDate());
					}
				}
			}
		});
	}

	private String getDate(){
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return	formatter.format(new Date()).toString();
	}





}
