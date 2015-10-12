package com.app.andy.bulkapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.andy.bulkapp.Data.BulkContract;
import com.app.andy.bulkapp.Data.BulkDbHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 10/11/2015.
 * The DAO class
 */
public class FoodItemDataSource {

	private SQLiteDatabase database;
	private BulkDbHelper dbHelper;
	private String[] allColumns = {BulkContract.FoodItemEntry.COLUMN_FOOD_KEY, BulkContract.FoodItemEntry.COLUMN_CALORIE_COUNT, BulkContract.FoodItemEntry.COLUMN_DATE};

	/**
	 * ctor
	 *
	 * @param context
	 */
	public FoodItemDataSource(Context context) {
		dbHelper = new BulkDbHelper(context);
	}

	/**
	 * Open the db connection
	 *
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close the db connection
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Insert a FoodItem into the database
	 *
	 * @param item
	 * @return
	 */
	public long insertFoodItem(FoodItem item) {
		ContentValues values = new ContentValues();
		values.put(BulkContract.FoodItemEntry.COLUMN_FOOD_KEY, item.getName());
		values.put(BulkContract.FoodItemEntry.COLUMN_CALORIE_COUNT, item.getCalorie());
		values.put(BulkContract.FoodItemEntry.COLUMN_DATE, item.getDate());
		long insertId = database.insert(BulkContract.FoodItemEntry.TABLE_NAME, null, values);
		return insertId;
	}

	/**
	 * Get all the items in db
	 *
	 * @return
	 */
	public List<FoodItem> getAllItems() {
		List<FoodItem> items = new ArrayList<FoodItem>();
		Cursor cursor = database.query(BulkContract.FoodItemEntry.TABLE_NAME, allColumns, null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			FoodItem item = cursorToFoodItem(cursor); items.add(item);
			cursor.moveToNext();
		} cursor.close(); return items;
	}

	/**
	 * Convert a cursor object to a FoodItem object
	 *
	 * @param cursor
	 * @return
	 */
	private FoodItem cursorToFoodItem(Cursor cursor) {
		FoodItem foodItem = new FoodItem();
		foodItem.setId(cursor.getInt(0));
		foodItem.setName(cursor.getString(1));
		foodItem.setCalorie(cursor.getDouble(2));
		foodItem.setDate(cursor.getString(3)); return foodItem;
	}

}
