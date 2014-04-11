package com.openregatta.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {
	private static DataHelper mInstance = null;
	
	private static final String DATABASE_NAME = "openregatta.db";
	private static final int DATABASE_VERSION = 2;
	
	public static final String TABLE_NAME = "prediction";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_BOATID = "_boatid";
	public static final String COLUMN_TWS = "_tws";
	public static final String COLUMN_TWA = "_twa";
	public static final String COLUMN_V = "_v";
	public static final String COLUMN_VMG = "_vmg";
	public static final String COLUMN_HEEL = "_heel";
	public static final String COLUMN_REEF = "_reef";
	public static final String COLUMN_FLAT = "_flat";
	public static final String COLUMN_AWS = "_aws";
	public static final String COLUMN_AWA = "_awa";
	public static final String COLUMN_LEE = "_lee";
	public static final String COLUMN_SAIL = "_sail";
	public static final String COLUMN_ISBEST = "_isbest";
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_TWS + " FLOAT, "
			+ COLUMN_TWA + " FLOAT, "
			+ COLUMN_V + " FLOAT, "
			+ COLUMN_VMG + " FLOAT, "
			+ COLUMN_HEEL + " FLOAT, "
			+ COLUMN_REEF + " FLOAT, "
			+ COLUMN_FLAT + " FLOAT, "
			+ COLUMN_AWS + " FLOAT, "
			+ COLUMN_AWA + " FLOAT, "
			+ COLUMN_LEE + " FLOAT, "
			+ COLUMN_SAIL + " FLOAT, "
			+ COLUMN_BOATID + " INTEGER, "
			+ COLUMN_ISBEST + " BOOLEAN);";
	 
	public DataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static DataHelper getInstance(Context ctx) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (mInstance == null) {
	      mInstance = new DataHelper(ctx.getApplicationContext());
	    }
	    return mInstance;
	  }

	@Override
	public void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  Log.w(DataHelper.class.getName(),
	      "Upgrading database from version " + oldVersion + " to "
	          + newVersion + ", which will destroy all old data");
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	  onCreate(db);
	}
}
