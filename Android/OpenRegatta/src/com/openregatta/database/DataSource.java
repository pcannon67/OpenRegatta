package com.openregatta.database;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

public class DataSource {
	// Database fields
	  private SQLiteDatabase database;
	  private DataHelper dbPerfHelper;
	  
	  private String[] allColumns = { 
			  DataHelper.COLUMN_AWA,
			  DataHelper.COLUMN_AWS,
			  DataHelper.COLUMN_FLAT,
			  DataHelper.COLUMN_HEEL,
			  DataHelper.COLUMN_ID,
			  DataHelper.COLUMN_LEE,
			  DataHelper.COLUMN_REEF,
			  DataHelper.COLUMN_SAIL,
			  DataHelper.COLUMN_TWA,
			  DataHelper.COLUMN_TWS,
			  DataHelper.COLUMN_V,
			  DataHelper.COLUMN_VMG,
			  DataHelper.COLUMN_ISBEST,
			  DataHelper.COLUMN_BOATID};
	  
	  public DataSource(Context context) {
		  dbPerfHelper = DataHelper.getInstance(context);
	  }

	  public void open() throws SQLException {
	    database = dbPerfHelper.getWritableDatabase();
	  }

	  public void close() {
		  dbPerfHelper.close();
	  }

	  public int getCount() {
		  	String countQuery = "SELECT  * FROM " + DataHelper.TABLE_NAME;
		    Cursor cursor = database.rawQuery(countQuery, null);
		    int cnt = cursor.getCount();
		    cursor.close();
		    return cnt;
	  }

	  public int insertPolarsFromTSVFile(Uri fileURI) throws Exception {
		  	int insertedRows = 0;
		    FileInputStream fstream = new FileInputStream(fileURI.getPath());
		    Scanner br = new Scanner(new InputStreamReader(fstream));
		    
		    removeAll();
		    
		    String sql = "INSERT INTO "+ DataHelper.TABLE_NAME 
					  +"("+ DataHelper.COLUMN_AWA 
					  +","+ DataHelper.COLUMN_AWS
					  +","+ DataHelper.COLUMN_FLAT
					  +","+ DataHelper.COLUMN_HEEL
					  +","+ DataHelper.COLUMN_LEE
					  +","+ DataHelper.COLUMN_REEF
					  +","+ DataHelper.COLUMN_SAIL
					  +","+ DataHelper.COLUMN_TWA
					  +","+ DataHelper.COLUMN_TWS
					  +","+ DataHelper.COLUMN_V
					  +","+ DataHelper.COLUMN_VMG
					  +","+ DataHelper.COLUMN_ISBEST
					  +","+ DataHelper.COLUMN_BOATID
					  +") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
			  
	          SQLiteStatement statement = database.compileStatement(sql);
	          database.beginTransaction();
		    
	          while (br.hasNext()) {
		        String strLine = br.nextLine();
		        if(strLine.startsWith("#"))
		        	continue;
		        
		        String[] split = strLine.split("\\t");
		        if(split.length == 12){//valid line
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
		        	short isBest = Short.parseShort(split[11]);
		        	
		        	statement.clearBindings();
                    statement.bindDouble(1, (double)awa);
                    statement.bindDouble(2, (double)aws);
                    statement.bindDouble(3, (double)flat);
                    statement.bindDouble(4, (double)heel);
                    statement.bindDouble(5, (double)lee);
                    statement.bindDouble(6, (double)reef);
                    statement.bindString(7, sail);
                    statement.bindDouble(8, (double)twa);
                    statement.bindDouble(9, (double)tws);
                    statement.bindDouble(10, (double)v);
                    statement.bindDouble(11, (double)vmg);
                    statement.bindLong(12, (long)isBest);
                    statement.bindLong(13, 367);
                    statement.execute();
		        }
		        insertedRows++;
		    }
	        database.setTransactionSuccessful();	
		    database.endTransaction(); 
		    fstream.close();
		    return insertedRows;
	  }

	  public List<PerfRow> getBestPerformancesForBoat(int boatId)
	  {
		  List<PerfRow> bestPerformances = new ArrayList<PerfRow>();
		  
		  Cursor cursor = database.query(
				  DataHelper.TABLE_NAME, /* table */
				  allColumns, /* columns */
				  DataHelper.COLUMN_ISBEST + " = 1 AND " + DataHelper.COLUMN_BOATID + " = " + String.valueOf(boatId), /* where */
				  null, /* where args */
				  null, /* groupBy */
				  null, /* having */
				  null); /* order by */
		  
		  if(cursor!=null){
			  cursor.moveToFirst();
			  while (!cursor.isAfterLast()) {
				  PerfRow perfRow = cursorToPerformanceRow(cursor);
				  bestPerformances.add(perfRow);
				  cursor.moveToNext();
			  }
			  
			  // make sure to close the cursor
			  cursor.close();
		  }
	    
		  return bestPerformances;
	  }
	  
	  private void removeAll()
	  {
	      database.delete(DataHelper.TABLE_NAME, null, null);
	  }
	  
	  private PerfRow cursorToPerformanceRow(Cursor cursor) {
	    PerfRow perfRow = new PerfRow();
	    perfRow.setAwa(cursor.getFloat(0));
	    perfRow.setAws(cursor.getFloat(1));
	    perfRow.setFlat(cursor.getFloat(2));
	    perfRow.setHeel(cursor.getFloat(3));
	    perfRow.setId(cursor.getInt(4));
	    perfRow.setLee(cursor.getFloat(5));
	    perfRow.setReef(cursor.getFloat(6));
	    perfRow.setSail(cursor.getString(7));
	    perfRow.setTwa(cursor.getFloat(8));
	    perfRow.setTws(cursor.getFloat(9));
	    perfRow.setV(cursor.getFloat(10));
	    perfRow.setVmg(cursor.getFloat(11));
	    perfRow.setBoatId(cursor.getInt(13));
	    perfRow.setBest(cursor.getString(12).equals("1"));
	    return perfRow;
	  }

	
	  
}
