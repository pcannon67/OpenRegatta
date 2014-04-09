package com.openregatta.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PerformanceDataSource {
	// Database fields
	  private SQLiteDatabase database;
	  private PerformanceDataHelper dbPerfHelper;
	  
	  private String[] allColumns = { 
			  PerformanceDataHelper.COLUMN_AWA,
			  PerformanceDataHelper.COLUMN_AWS,
			  PerformanceDataHelper.COLUMN_FLAT,
			  PerformanceDataHelper.COLUMN_HEEL,
			  PerformanceDataHelper.COLUMN_ID,
			  PerformanceDataHelper.COLUMN_LEE,
			  PerformanceDataHelper.COLUMN_REEF,
			  PerformanceDataHelper.COLUMN_SAIL,
			  PerformanceDataHelper.COLUMN_TWA,
			  PerformanceDataHelper.COLUMN_TWS,
			  PerformanceDataHelper.COLUMN_V,
			  PerformanceDataHelper.COLUMN_VMG};

	  public PerformanceDataSource(Context context) {
		  dbPerfHelper = PerformanceDataHelper.getInstance(context);
	  }

	  public void open() throws SQLException {
	    database = dbPerfHelper.getWritableDatabase();
	  }

	  public void close() {
		  dbPerfHelper.close();
	  }

	  public List<PerformanceRow> getAllPerformances() {
	    List<PerformanceRow> comments = new ArrayList<PerformanceRow>();

	    Cursor cursor = database.query(PerformanceDataHelper.TABLE_NAME,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	PerformanceRow comment = cursorToPerformanceRow(cursor);
	      comments.add(comment);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return comments;
	  }
	  
	  public int getCount() {
		  	String countQuery = "SELECT  * FROM " + PerformanceDataHelper.TABLE_NAME;
		    Cursor cursor = database.rawQuery(countQuery, null);
		    int cnt = cursor.getCount();
		    cursor.close();
		    return cnt;
	  }

	  public int LoadFromCSVFile(Uri fileURI) throws Exception {
		  	int insertedRows = 0;
		    FileInputStream fstream = new FileInputStream(fileURI.getPath());
		    
		    Scanner br = new Scanner(new InputStreamReader(fstream));
		    
		    removeAll();
		    
		    while (br.hasNext()) {
		        String strLine = br.nextLine();
		        if(strLine.startsWith("#"))
		        	continue;
		        
		        String[] split = strLine.split(",");
		        if(split.length == 11){//valid line
		        	float tws = Float.parseFloat(split[0]);
		        	float twa = Float.parseFloat(split[1]);
		        	float v = Float.parseFloat(split[2]);
		        	float vmg = Float.parseFloat(split[3]);
		        	float heel = Float.parseFloat(split[4]);
		        	float reef = Float.parseFloat(split[5]);
		        	float flat = Float.parseFloat(split[6]);
		        	float aws = Float.parseFloat(split[7]);
		        	float awa = Float.parseFloat(split[8]);
		        	float lee = Float.parseFloat(split[9]);
		        	String sail = split[10];
		        	
		        	insertPerformanceRow(awa, aws, flat, heel, lee, reef, sail, twa, tws, v, vmg);
		        }
		        insertedRows++;
		    }
		    fstream.close();
		    return insertedRows;
	  }

	  private void removeAll()
	  {
	      database.delete(PerformanceDataHelper.TABLE_NAME, null, null);
	  }
	  
	  private long insertPerformanceRow(float awa, float aws, float flat, float heel, float lee, float reef, String sail, float twa, float tws, float v, float vmg) {
		    ContentValues values = new ContentValues();
		    values.put(PerformanceDataHelper.COLUMN_AWA,awa);
		    values.put(PerformanceDataHelper.COLUMN_AWS,aws);
		    values.put(PerformanceDataHelper.COLUMN_FLAT,flat);
		    values.put(PerformanceDataHelper.COLUMN_HEEL,heel);
		    values.put(PerformanceDataHelper.COLUMN_LEE,lee);
		    values.put(PerformanceDataHelper.COLUMN_REEF,reef);
		    values.put(PerformanceDataHelper.COLUMN_SAIL,sail);
			values.put(PerformanceDataHelper.COLUMN_TWA,twa);
		    values.put(PerformanceDataHelper.COLUMN_TWS,tws);
		    values.put(PerformanceDataHelper.COLUMN_V,v);
		    values.put(PerformanceDataHelper.COLUMN_VMG,vmg);
		    long insertId = database.insert(PerformanceDataHelper.TABLE_NAME, null,
		        values);
		    //Cursor cursor = database.query(PerformanceDataHelper.TABLE_NAME,
		    //    allColumns, PerformanceDataHelper.COLUMN_ID + " = " + insertId, null,
		    //    null, null, null);
		    //cursor.moveToFirst();
		    //PerformanceRow newPerfRow = cursorToPerformanceRow(cursor);
		    //cursor.close();
		    //return newPerfRow;
		    return insertId;
		  }
	  
	  private PerformanceRow cursorToPerformanceRow(Cursor cursor) {
	    PerformanceRow perfRow = new PerformanceRow();
	    perfRow.setAwa(cursor.getFloat(0));
	    perfRow.setAws(cursor.getFloat(1));
	    perfRow.setFlat(cursor.getFloat(2));
	    perfRow.setHeel(cursor.getFloat(3));
	    perfRow.setId(cursor.getLong(4));
	    perfRow.setLee(cursor.getFloat(5));
	    perfRow.setReef(cursor.getFloat(6));
	    perfRow.setSail(cursor.getString(7));
	    perfRow.setTwa(cursor.getFloat(8));
	    perfRow.setTws(cursor.getFloat(9));
	    perfRow.setV(cursor.getFloat(10));
	    perfRow.setVmg(cursor.getFloat(11));
	    return perfRow;
	  }

	
	  
}
