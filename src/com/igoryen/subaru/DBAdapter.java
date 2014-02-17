package com.igoryen.subaru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/*
 * As good practice for dealing with databases this is to create a helper class 
 * to encapsulate all the complexities of accessing the data 
 * so that it is transparent to the calling code.
 * This helper class is called DBAdapter.
 * DBAdapter creates, opens, closes, and uses a SQLite database.
 */

public class DBAdapter {
	
	//===================================================================================
	/* You first defined several constants to contain the various fields for the table 
	 * that in your database you are going to create*/
	//-----------------------------------------------------------------------------------
	
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

    //SQL statement for creating the contacts table within the subaru database.
    static final String DATABASE_CREATE = "create table gas (" +
    		"_id integer primary key, " +
    		"adate DATE, " +
    		"cost REAL, " +
    		"liters REAL, " +
    		"odometer INT, " +
    		"fill TEXT, " +
    		"station TEXT, " +
    		"carwash REAL);";
    //--------------------------------------------------------------------------
    //==========================================================================
    
    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    
    
    
    //===============================================================================
    // To create a database in your application using the DBAdapter class, 
    // you create an instance of the DBAdapter class: (1)
    //-------------------------------------------------------------------------------
    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        Log.d("a1", "D: (1) a dbadapter created");
    }
    //-------------------------------------------------------------------------------
    //===============================================================================

    
    
    
    //=====================================================================================================
    // Within the DBAdapter class, you also added a private class that extended the SQLiteOpenHelper class,
    // which is a helper class in Android to manage database creation and version management.
    //-----------------------------------------------------------------------------------------------------
    
    private static class DatabaseHelper extends SQLiteOpenHelper{
    	
    	//-----------------------------------------------------------------------------------
    	// The constructor of the DBAdapter class will then (2) create 
    	// an instance of the DatabaseHelper class to create a new database:
    	//-----------------------------------------------------------------------------------
        DatabaseHelper(Context context){
            super(context, 
            		DATABASE_NAME, 
            		null, 
            		DATABASE_VERSION);
            Log.d("a1", "D: (2) the DBAdapter's constructor created a DatabaseHelper to create a new db");
        }
        
        

        //--------------------------------------------------------------------------------------
        // The onCreate() method creates a new database if the required database is not present. 
        //--------------------------------------------------------------------------------------
        @Override
        public void onCreate(SQLiteDatabase db){
            try {
                db.execSQL(DATABASE_CREATE);
                Log.d("a1", "D: DataBaseHelper.OnCreate(). SQL executed, db created");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //------------------------------------------------------------------------------------
        // The onUpgrade() method is called when the database needs to be upgraded. 
        // This is achieved by checking the value defined in the DATABASE_VERSION constant. 
        // Here you simply drop the table and create it again.
        // -----------------------------------------------------------------------------------
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " 
                + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    //-------------------------------------------------------------------------------
    //================================================================================
    

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        Log.d("a1", "D: open() -> db opened");
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
        Log.d("a1", "D: close() -> db closed");
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
    	
        Log.d("a1", "D: insertRow() entered");

        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_ADATE,    adate);
        initialValues.put(KEY_COST,     cost);
        initialValues.put(KEY_LITERS,   liters);
        initialValues.put(KEY_ODOMETER, odometer);
        initialValues.put(KEY_FILL,     fill);
        initialValues.put(KEY_STATION,  station);
        initialValues.put(KEY_CARWASH,  carwash); 
        
        Log.d("a1", "D: insertRow() exiting");
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteRow(long rowId){
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---------------------------------------------------------------------------------------------
    //---retrieves all the contacts---
    // Notice that Android uses the Cursor class as a return value for queries. 
    // Think of the Cursor as a pointer to the result set from a database query. 
    // Using Cursor enables Android to more efficiently manage rows and columns as needed.
    // Return result as a Cursor object.
    //---------------------------------------------------------------------------------------------
    public Cursor getAllRows(){
    	Log.d("a1", "D: getAllRows() entered	");
    	String[] columns = new String[] {
    			KEY_ROWID, 
    			KEY_ADATE, 
    			KEY_COST, 
    			KEY_LITERS, 
    			KEY_ODOMETER, 
    			KEY_FILL, 
    			KEY_STATION, 
    			KEY_CARWASH};
    	Log.d("a1", "D: in getAllRows(), columns: " + columns.length);
    	String restrict = null; // Get all entries
        String orderby = null;
    	Cursor allRows = db.query(DATABASE_TABLE, columns, restrict, null, null, null, orderby); // ???
    	Log.d("a1", "D: in getAllRows(), rows: " + allRows.getCount()); // return number of rows in cursor
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
    //---------------------------------------------------------------------------
    // You use a ContentValues object to store name/value pairs. 
    // Its put() method enables you to insert keys with values of different data types.
    //---------------------------------------------------------------------------
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
        
        args.put(KEY_ADATE,     adate);
        args.put(KEY_COST,      cost);
        args.put(KEY_LITERS,    liters);
        args.put(KEY_ODOMETER,  odometer);
        args.put(KEY_FILL,      fill);
        args.put(KEY_STATION,   station);
        args.put(KEY_CARWASH,   carwash); 
        
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
