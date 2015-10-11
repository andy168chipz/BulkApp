package com.app.andy.bulkapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andy on 10/10/2015.
 * The sql lite helper for bulk app
 */
public class BulkDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "bulk.db";

	public BulkDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String SQL_CREATE_FOODS_TABLE = "CREATE TABLE " + BulkContract.FoodItemEntry.TABLE_NAME + " ( " +
				//ID
				BulkContract.FoodItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

				//the column data associated with this food item id
				BulkContract.FoodItemEntry.COLUMN_FOOD_KEY + " TEXT NOT NULL, " +
				BulkContract.FoodItemEntry.COLUMN_CALORIE_COUNT + " REAL NOT NULL, " +
				BulkContract.FoodItemEntry.COLUMN_DATE + " TEXT NOT NULL); ";

		//create the table
		db.execSQL(SQL_CREATE_FOODS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//will update when needed
//		if(newVersion > oldVersion){
//			db.execSQL("ALTER TABLE "+BulkContract.FoodItemEntry.TABLE_NAME+" ADD COLUMN ");
//		}
		switch(oldVersion){
			default:
				return;
		}

	}

}

