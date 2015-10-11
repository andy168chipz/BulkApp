package com.app.andy.bulkapp.Data;

import android.provider.BaseColumns;

/**
 * Created by Andy on 10/10/2015.
 * The contract with the sql lite db helper
 */
public class BulkContract {

	/**
	 * Inner class that represents the food items table
	 */
	public static final class FoodItemEntry implements BaseColumns {
		//name of table
		public static final String TABLE_NAME = "foods";

		//name of food item
		public static final String COLUMN_FOOD_KEY = "food_id";
		//calorie of food item
		public static final String COLUMN_CALORIE_COUNT = "calorie";
		//date of food item entry
		public static final String COLUMN_DATE = "date";
	}
}
