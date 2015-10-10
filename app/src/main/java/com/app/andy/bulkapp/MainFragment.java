package com.app.andy.bulkapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		init(view);
		return view;
	}

	/**
	 * Initilize the view
	 * @param view
	 */
	public void init(View view){
		TextView dateText = (TextView) view.findViewById(R.id.dateTextView);
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateText.setText(format.format(new Date()));
		foodNameEdit = (EditText)view.findViewById(R.id.foodEditText);
		calorieCountEdit = (EditText)view.findViewById(R.id.calEditText);
		submit = (Button)view.findViewById(R.id.submitButton);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(foodNameEdit.getText().toString().equals("") ){
					Toast.makeText(getActivity().getBaseContext(), "You didn't enter a food name!", Toast.LENGTH_SHORT).show();
					return;
				}
				else if(calorieCountEdit.getText().toString().equals("")){
					Toast.makeText(getActivity().getBaseContext(), "You didn't enter a calorie count!", Toast.LENGTH_SHORT).show();
					return;
				}
				//do save action here
			}
		});
	}
}
