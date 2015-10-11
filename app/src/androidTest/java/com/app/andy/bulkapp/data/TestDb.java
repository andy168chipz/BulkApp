package com.app.andy.bulkapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.app.andy.bulkapp.Data.BulkContract;
import com.app.andy.bulkapp.Data.BulkDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 10/10/2015.
 * Test for db
 */
public class TestDb extends AndroidTestCase {

	private static final String TEST_TAG = "DB TEST";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCreateDb() throws Throwable {
		mContext.deleteDatabase(BulkDbHelper.DATABASE_NAME);
		SQLiteDatabase db = new BulkDbHelper(this.mContext).getWritableDatabase();
		assertEquals(true, db.isOpen());
		db.close();
	}

	public void testInsertDb() {
		BulkDbHelper dbHelper = new BulkDbHelper(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues testValues = fakeValues();
		long testRowId = db.insert(BulkContract.FoodItemEntry.TABLE_NAME, null, testValues);

		//check at row is valid
		assertTrue(testRowId != -1); Log.d(TEST_TAG, "New row id: " + testRowId);

		//Cursor to read
		Cursor cursor = db.query(BulkContract.FoodItemEntry.TABLE_NAME, null, // all columns
				null, // Columns for the "where" clause
				null, // Values for the "where" clause
				null, // columns to group by
				null, // columns to filter by row groups
				null // sort order
				//copied from Udacity guide
		);
		validateCursor(cursor, testValues);
		dbHelper.close();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	static ContentValues fakeValues() {
		ContentValues testValues = new ContentValues();
		testValues.put(BulkContract.FoodItemEntry.COLUMN_FOOD_KEY, "beef noodle soup");
		testValues.put(BulkContract.FoodItemEntry.COLUMN_CALORIE_COUNT, "500");
		testValues.put(BulkContract.FoodItemEntry.COLUMN_DATE, "2010-04-21 15:34:55");
		return testValues;
	}

	static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

		assertTrue(valueCursor.moveToFirst());

		Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
		for (Map.Entry<String, Object> entry : valueSet) {
			String columnName = entry.getKey(); int idx = valueCursor.getColumnIndex(columnName);
			assertFalse(idx == -1); String expectedValue = entry.getValue().toString();
			assertEquals(expectedValue, valueCursor.getString(idx));
		} valueCursor.close();
	}
}
