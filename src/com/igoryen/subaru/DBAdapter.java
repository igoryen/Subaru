package com.igoryen.subaru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	
    static final String KEY_ROWID = "_id";
    static final String KEY_ADATE = "adate";
    static final String KEY_COST = "cost";
    static final String KEY_LITERS = "liters";
    static final String KEY_ODOMETER = "odometer";
    static final String KEY_FILL = "fill";
    static final String KEY_STATION = "station";
    static final String KEY_CARWASH = "carwash";    
    
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "subaru";
    static final String DATABASE_TABLE = "gas";
    static final int 	DATABASE_VERSION = 1;

    static final String DATABASE_CREATE = "create table gas (" +
    		"_id integer primary key, " +
    		"adate DATE, " +
    		"cost REAL, " +
    		"liters REAL, " +
    		"odometer INT, " +
    		"fill TEXT, " +
    		"station TEXT, " +
    		"carwash REAL);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    
    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        Log.d("a1", "DBAdapter: a db created in the app");
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            try {
                db.execSQL(DATABASE_CREATE);
                Log.d("a1", "DBAdapter: SQL executed, db created");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        Log.d("a1", "DBAdapter: db opened");
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
        Log.d("a1", "DBAdapter: db closed");
    }

    //---insert a contact into the database---
    public long insertRow(
    	String adate, 
    	String cost, 
    	String liters, 
    	String odometer, 
    	String fill, 
    	String station, 
    	String carwash) {
    	
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_ADATE, 	adate);
        initialValues.put(KEY_COST, 	cost);
        initialValues.put(KEY_LITERS, 	liters);
        initialValues.put(KEY_ODOMETER, odometer);
        initialValues.put(KEY_FILL, 	fill);
        initialValues.put(KEY_STATION, 	station);
        initialValues.put(KEY_CARWASH, 	carwash); 
        
        Log.d("a1", "DBAdapter: bottom of insertRow()");
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteRow(long rowId){
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllRows(){
    	Log.d("a1", "DBAdapter: in getAllRows()");
    	String[] columns = new String[] {
    			KEY_ROWID, 
    			KEY_ADATE, 
    			KEY_COST, 
    			KEY_LITERS, 
    			KEY_ODOMETER, 
    			KEY_FILL, 
    			KEY_STATION, 
    			KEY_CARWASH};
    	Log.d("a1", "DBAdapter: in getAllRows(), columns: " + columns.length);
    	Cursor allRows = db.query(DATABASE_TABLE, columns, null, null, null, null, null);
    	Log.d("a1", "DBAdapter: in getAllRows(), rows: " + allRows.getCount()); // return number of rows in cursor
        return allRows;
    }

    //---retrieves a particular contact---
    public Cursor getRow(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_ADATE, 
        		KEY_COST,
        		KEY_LITERS, 
        		KEY_ODOMETER, 
        		KEY_FILL, 
        		KEY_STATION, 
        		KEY_CARWASH}, 
        		KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateRow(
    		long rowId, 
    		String adate, 
    		String cost, 
    		String name, 
    		String liters,
    		String odometer, 
    		String fill, 
    		String station, 
    		String carwash) {
        ContentValues args = new ContentValues();
        
        args.put(KEY_ADATE, 	adate);
        args.put(KEY_COST, 		cost);
        args.put(KEY_LITERS, 	liters);
        args.put(KEY_ODOMETER, 	odometer);
        args.put(KEY_FILL, 		fill);
        args.put(KEY_STATION, 	station);
        args.put(KEY_CARWASH, 	carwash); 
        
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
