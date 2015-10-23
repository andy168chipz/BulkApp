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
		SQLiteDatabase db = BulkDbHelper.getIntance(mContext).getWritableDatabase();
		assertEquals(true, db.isOpen());
		db.close();
	}

	public void testInsertDb() {
		BulkDbHelper dbHelper = BulkDbHelper.getIntance(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues testValues = fakeValues(100);
		long testRowId = db.insert(BulkContract.FoodItemEntry.TABLE_NAME, null, testValues);

		//check at row is valid
		assertTrue(testRowId != -1);
		Log.d(TEST_TAG, "New row id: " + testRowId);

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

	public void testDeleteRow() {
		BulkDbHelper dbHelper = BulkDbHelper.getIntance(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long testRowId = db.insert(BulkContract.FoodItemEntry.TABLE_NAME, null, fakeValues(200));

		//check at row is valid
		assertTrue(testRowId != -1);
		Log.d(TEST_TAG, "New row id: " + testRowId);

		//delete all rows
		long deleteRodId = db.delete(BulkContract.FoodItemEntry.TABLE_NAME, "1", null);
		assertTrue(deleteRodId == 1);
		dbHelper.close();
	}

	public void testAlterRow() {
		BulkDbHelper dbHelper = BulkDbHelper.getIntance(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long testRowId = db.insert(BulkContract.FoodItemEntry.TABLE_NAME, null, fakeValues(200));

		//check at row is valid
		assertTrue(testRowId != -1);

		long alterId = db.update(BulkContract.FoodItemEntry.TABLE_NAME, fakeValues(600), "_id=" + testRowId, null);
		assertTrue(alterId == 1);
		Cursor cursor = db.query(BulkContract.FoodItemEntry.TABLE_NAME, null, "_id=" + testRowId, null, null, null, null);
		validateCursor(cursor,fakeValues(600));
	}

	public void testDeleteWhereRow() {
		BulkDbHelper dbHelper = BulkDbHelper.getIntance(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long testRowId = db.insert(BulkContract.FoodItemEntry.TABLE_NAME, null, fakeValues(200));
		long testRowId2 = db.insert(BulkContract.FoodItemEntry.TABLE_NAME, null, fakeValues(400));

		//check at row is valid
		assertTrue(testRowId != -1);
		//delete all rows
		long deleteRodId = db.delete(BulkContract.FoodItemEntry.TABLE_NAME, BulkContract.FoodItemEntry.COLUMN_CALORIE_COUNT + "=" + 400, null);
		assertTrue(deleteRodId == 1);
		long deleteRodId2 = db.delete(BulkContract.FoodItemEntry.TABLE_NAME, "1", null);
		assertTrue(deleteRodId2 == 1);
		dbHelper.close();

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	static ContentValues fakeValues(int n) {
		ContentValues testValues = new ContentValues();
		testValues.put(BulkContract.FoodItemEntry.COLUMN_FOOD_KEY, "beef noodle soup");
		testValues.put(BulkContract.FoodItemEntry.COLUMN_CALORIE_COUNT, n);
		testValues.put(BulkContract.FoodItemEntry.COLUMN_DATE, "2010-04-21 15:34:55");
		return testValues;
	}

	static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

		assertTrue(valueCursor.moveToFirst());
		Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
		for (Map.Entry<String, Object> entry : valueSet) {
			String columnName = entry.getKey();
			int idx = valueCursor.getColumnIndex(columnName);
			assertFalse(idx == -1);
			String expectedValue = entry.getValue().toString();
			assertEquals(expectedValue, valueCursor.getString(idx));
		} valueCursor.close();
	}
}
