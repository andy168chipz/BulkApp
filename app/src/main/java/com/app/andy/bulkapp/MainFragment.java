package com.app.andy.bulkapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
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

	private EditText foodNameEdit, calorieCountEdit, goalEditText;
	private FoodItemDataSource dataSource;
	private static final String LOG_TAG = "Bulk Fragment";
	private ListShowListener showListener;
	private TextView achievedText;
	private SharedPreferences sharedPreferences;
	private int goal;
	private final int SHARED_PREF_DEF_VALUE = -100;

	public interface ListShowListener {
		void onListShow();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Log.e(LOG_TAG, "attached");
		try {
			showListener = (ListShowListener) context;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " didnt implement!!!");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
		goal = sharedPreferences.getInt("calorie_goal", SHARED_PREF_DEF_VALUE);
		if (goal == SHARED_PREF_DEF_VALUE) {
			promptGoalDialog();
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
		dateText.setText(getDate());
		goalEditText = (EditText) view.findViewById(R.id.goalEditText);
		if (goal != SHARED_PREF_DEF_VALUE) {
			goalEditText.setText(Integer.toString(goal));
		}
		achievedText = (TextView) view.findViewById(R.id.achieveText);
		achievedText.setText("Achieved: " + getAchieved());
		foodNameEdit = (EditText) view.findViewById(R.id.foodEditText);
		calorieCountEdit = (EditText) view.findViewById(R.id.calEditText);
	}

	/**
	 * Return appropriate date format
	 *
	 * @return
	 */
	private String getDate() {
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return formatter.format(new Date()).toString();
	}

	private String getAchieved() {
		Cursor cursor = dataSource.queryToday(getDate());
		int sum = 0;
		while (cursor.moveToNext()) {
			sum += cursor.getInt(0);
			Log.v(LOG_TAG, cursor.getString(0));
		}
		return Integer.toString(sum);
	}

	/**
	 * The onClick listener
	 *
	 * @param v
	 */
	public void onClick(View v) {
		switch (v.getId()) {
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
					foodNameEdit.setText("");
					calorieCountEdit.setText("");
					achievedText.setText("Achieved: " + getAchieved());
				}
				break;
			case R.id.showListButton:
				if (showListener != null) {
					showListener.onListShow();
				}
				break;
			case R.id.deleteAllButton:
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Are you sure you want to delete all entries?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						long id = dataSource.deleteAllItem();
						if (id > 0) { Log.v("Delete action", Long.toString(id)); } else {
							Log.v("Delete action", "none");
						}
					}
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
				break;
			case R.id.goalSaveButton:
				promptGoalDialog();
				break;
			default:
				Log.e(LOG_TAG, "Invalid button");
				throw new IllegalArgumentException();
		}
	}

	private void promptGoalDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Enter your daily calorie goal");
		final EditText input = new EditText(getContext());
		InputFilter filter[] = new InputFilter[1];
		filter[0] = new InputFilter.LengthFilter(8);
		input.setFilters(filter);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);

		builder.setView(input);
		builder.setNeutralButton("Done", null);
		AlertDialog dialog = builder.create();
		dialog.show();

		Button button = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
		button.setOnClickListener(new View.OnClickListener() {

			private Dialog dialog;

			@Override
			public void onClick(View view) {
				String calorieAmount = input.getText().toString();
				if (calorieAmount.equals("")) {
					Toast.makeText(getActivity().getBaseContext(), "You need to enter a calorie count, You can change this later!", Toast.LENGTH_SHORT).show();
				} else {
					Log.v(LOG_TAG, calorieAmount);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putInt("calorie_goal", Integer.parseInt(calorieAmount));
					editor.apply();
					goalEditText.setText(calorieAmount);
					dialog.dismiss();
				}
			}

			public View.OnClickListener init(Dialog dialog) {
				this.dialog = dialog;
				return this;
			}
		}.init(dialog));
	}
}
