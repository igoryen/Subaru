package com.igoryen.subaru;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.igoryen.subaru.MyOnItemSelectedListener;

public class MainActivity extends Activity {
	
	DatePicker datePicker;
	private Spinner spinner;
	private Button btnSet;
	static final int DATE_DIALOG_ID = 0;
	
	int yr, month, day;
	
	String station;
	String date;
	String[] selection;
	
	//========================================================================
    /** Called when the activity is first created. */
	//========================================================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        
        addItemsOnSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();


        
       
        

        //===================================================================
        // Check if data base already exists at the destination folder: 
        // data/data/com.igoryen.subaru/databases/subaru.db 
        //===================================================================
        File file = getBaseContext().getFileStreamPath("subaru.db");
        if(file.exists()){
        	Log.d("a1", "M: subaru.db exists");
        }
        // OR
        String destPath = "/data/data/" + getPackageName() + "/databases";
        File f = new File(destPath);
        Log.d("a1", "M: before db - f.exists() = "+ f.exists());
        
        
        //===================================================================
        // Create an instance of the DBAdapter class:
        //===================================================================
        DBAdapter db = new DBAdapter(this);
        Log.d("a1", "M: a db created");
        Log.d("a1", "M: after db - f.exists() = "+ f.exists()); 
        
        
        /*        
        //===================================================================
        // The insertRow() method returns the ID of the inserted row. 
        // If an error occurs during the operation, it returns -1.
        //===================================================================
        db.open();
        long id = db.insertRow("2004-03-29", "22.24", "32.750", "1198", "fill", "petrocan", "8.55");
        id = db.insertRow("2004-03-08", "37.45", "54.672", "430", "fill", "shell", "0.0");
        id = db.insertRow("2004-03-15", "28.72", "40.738", "694", "fill", "shell", "6.50");
        id = db.insertRow("2004-03-22", "26.06", "37.823", "949", "fill", "sunoco", "8.55");
        id = db.insertRow("2004-06-13", "32.23", "44.635", "4513", "fill", "pioneer", "0.0");
        db.close();

        */
        
        /*
        //==============================================================================
        //--get all contacts---
        // The getAllRows() method of the DBAdapter class retrieves all the contacts 
        // stored in the database. The result is returned as a Cursor object. 
        // To display all the contacts, you first need to call the moveToFirst() method of the Cursor object. 
        // If it succeeds (which means at least one row is available),
        // then you display the details of the contact using the DisplayRow() method. 
        // To move to the next row, call the moveToNext() method of the Cursor object.
        //==============================================================================
        db.open();
        Cursor c1 = db.getAllRows();
        if (c1.moveToFirst())
        {
            do {
                DisplayRow(c1);
            } while (c1.moveToNext());
        }
        db.close();
        */
        
        
        
        /*
        //===================================================================
        //---get a contact---
        db.open();
        Cursor c = db.getContact(2);
        if (c.moveToFirst())        
            DisplayContact(c);
        else
            Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close();     
        */
        
        
        
        /*
        //===================================================================
        //---update contact---
        db.open();
        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();
        */
       
        
        /*
        //===================================================================
        // delete a contact by the supplied _id
        //===================================================================
        db.open();
        if (db.deleteContact(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();
        */
       
        
        
        
        //============================================================================================
        // copy the database file only if it does not exist in the destination folder at the destPath
        // If you don�t perform this check, every time the activity is created 
        // you will overwrite the database file with the one in the assets folder. 
        // This may not be desirable, as your application may make changes to the database file 
        // during runtime, and this will overwrite all the changes you have made so far.
        //============================================================================================
        try {
            //String destPath = "/data/data/" + getPackageName() + "/databases";
            Log.d("a1", "M: destPath: "+ destPath);
            //File f = new File(destPath);
            if(f.exists()){
            	Log.d("a1", "M: f.exists() = true; database file is at "+ destPath);
            }
            if (!f.exists()) { 
            	Log.d("a1", "M: f.exists() = false; no database file at "+ destPath);
            	f.mkdirs();
                f.createNewFile();
            	
            	//---copy the db from the assets folder into the databases folder---
                Log.d("a1", "M: before CopyDB");
                CopyDB(getBaseContext().getAssets().open("subaru"), new FileOutputStream(destPath + "/subaru"));
                //CopyDB(this.getAssets().open("subaru.db"), new FileOutputStream(destPath + "/subaru"));
                Log.d("a1", "M: after CopyDB");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        
        
        //==============================================================================
        // get all rows and display them through toasts---
        // call getAllRows and fill a Cursor with all the rows.
        // moveToFirst(): if there is at least one row available
        // pass all the cursor with all the rows to DisplayRow()
        //==============================================================================
        /*
        db.open();
        Cursor c = db.getAllRows(); // <=== !!! fill "c" with all rows
        Log.d("a1", "M: after 'Cursor c = db.getAllRows()'");
        if(!c.moveToFirst()){
        	Log.d("a1", "M: c.moveToFirst() = false, not a single row is available");
        }
        if (c.moveToFirst()){
        	Log.d("a1", "M: c.moveToFirst() = true");
            do {
                DisplayRow(c);
            } while (c.moveToNext());
        }
        db.close();
        */
        
        
       
        
        
        
        
        
        
        /* ===== a =======
        addToListView(db);
        /*
        
        Button TermButton = (Button) findViewById(R.id.allrows);
        final Intent iar = new Intent("com.igoryen.subaru.AllRowsActivity");
        TermButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
            	startActivity(iar);
            	Log.d("a1", "M: All Rows button called AllRowsActivity");
        	}
    	});
        
    }
    
    /*
     // ==== a =============================================
    @SuppressWarnings("deprecation")
	private void addToListView(DBAdapter db){
    	Cursor c2 = db.getAllRows();
    	startManagingCursor(c2);
    	String[] from = {
    			DBAdapter.KEY_ROWID, 
    			DBAdapter.KEY_ADATE, 
    			DBAdapter.KEY_COST, 
    			DBAdapter.KEY_LITERS, 
    			DBAdapter.KEY_ODOMETER, 
    			DBAdapter.KEY_FILL, 
    			DBAdapter.KEY_STATION, 
    			DBAdapter.KEY_CARWASH
    	};
    	int[] to = {R.id.textView1, R.id.textView2, R.id.textView3};
    	
    	SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
    			this,
    			R.layout.aaa,
    			c2,
    			from,
    			to);    
    	
    	ListView lv = (ListView) findViewById(android.R.id.list);
    	lv.setAdapter(cursorAdapter);
    
    //==== a========================== =====================
     */

        
    } // onCreate()
	
    
    //================================================================== 
    // populate the spinner dynamically with values
    //================================================================== 
    public void addItemsOnSpinner() {
   
      spinner = (Spinner) findViewById(R.id.spinner);
      String[] items = new String[]{"shell", "sunoco", "petrocan"};
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
    		  android.R.layout.simple_spinner_item, items);
      spinner.setAdapter(adapter);
    }
    
    
    
    //========================================================================
    // add a listener on the item selected in the spinner
    //========================================================================
    public void addListenerOnSpinnerItemSelection() {
    	    spinner = (Spinner) findViewById(R.id.spinner);
    	    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }
    
    
    
    

    //=====================================================================
    // add listener on the Spinner's Submit button.
    // The button's id is "btnSet". It is created in the activity_main.xml
    // When the Submit button is clicked, display a toast with the choice.
    //=====================================================================
    public void addListenerOnButton() {
    	spinner = (Spinner) findViewById(R.id.spinner);
    	btnSet = (Button) findViewById(R.id.btnSet);
    	btnSet.setOnClickListener(new OnClickListener() {
    	 
    	      @Override
    	      public void onClick(View v) {
    	    	selection[0] = String.valueOf(spinner.getSelectedItem());
    	    	selection[1] = getDate();
    	    	//DisplayToast(station);
    	      }
    	 });
    }
    
   

    //=====================================================================
    // CopyDB() method to copy the database file from one location to another.
    // Use the InputStream object to read from the source file
    // Use the OutputStream object to write it to the destination file .
    //=====================================================================
    public void CopyDB(
    		InputStream inputStream, 
    		OutputStream outputStream) throws IOException {
    	Log.d("a1", "M: CopyDB() entered");
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    	Log.d("a1", "M: CopyDB() exiting");
    }
   
    //========================================================================
    // use a toast to display all the rows fetched from the db
    // check all the column values (optionally) 
    //========================================================================
    public void DisplayRow(Cursor c){
    	/*
    	Log.d("a1", "M: entered DisplayRow(); _id: " + c.getString(c.getColumnIndex("_id")));
    	Log.d("a1", "M: adate: " + c.getString(c.getColumnIndex("adate")));
    	Log.d("a1", "M: cost: " + c.getString(c.getColumnIndex("cost")));
    	Log.d("a1", "M: liters: " + c.getString(c.getColumnIndex("liters")));
    	Log.d("a1", "M: odometer: " + c.getString(c.getColumnIndex("odometer")));
    	Log.d("a1", "M: fill: " + c.getString(c.getColumnIndex("fill")));
    	Log.d("a1", "M: station: " + c.getString(c.getColumnIndex("station")));
    	Log.d("a1", "M: carwash: " + c.getString(c.getColumnIndex("carwash")));
    	*/
	    String msg = "id: "      +   c.getString(0) + "\n" +
	                 "Date: "    +   c.getString(1) + "\n" +
	                 "Cost: "    +   c.getString(2) + "\n" +
	                 "Liters: "  +   c.getString(3) + "\n" +
	                 "Odometer: " +  c.getString(4) + "\n" +
	                 "Fill: "    +   c.getString(5) + "\n" +
	                 "Station: " +   c.getString(6) + "\n" +
	                 "Carwash: " +   c.getString(7);
	    Log.d("a1", "M: in DisplayRow(), toast-msg filled");

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    
    
    //========================================================================
    // "parta" = "Pass All Rows To Activity"
    // get all rows and send them to AllRowsActivity
    // call getAllRows and fill a Cursor with all the rows.
    // moveToFirst(): if there is at least one row available
    // pass all the cursor with all the rows to DisplayRow()
    //========================================================================
    public void parta(View view){
    	ArrayList<String> bag = new ArrayList<String>();
        DBAdapter db = new DBAdapter(this);
        
        db.open();
        Cursor c = db.getAllRows(); // <=== !!! fill "c" with all rows
        Log.d("a1", "M: after 'Cursor c = db.getAllRows()'");
        if(!c.moveToFirst()){
        	Log.d("a1", "M: c.moveToFirst() = false, not a single row is available");
        }
        if (c.moveToFirst()){
        	Log.d("a1", "M: c.moveToFirst() = true");
            do {
                bag.add("id: "      +   c.getString(0) + "\n" +
                        "Date: "    +   c.getString(1) + "\n" +
                        "Cost: "    +   c.getString(2) + "\n" +
                        "Liters: "  +   c.getString(3) + "\n" +
                        "Odometer: " +  c.getString(4) + "\n" +
                        "Fill: "    +   c.getString(5) + "\n" +
                        "Station: " +   c.getString(6) + "\n" +
                        "Carwash: " +   c.getString(7));
            } while (c.moveToNext());
        }
        db.close();
    	Intent i = new Intent("com.igoryen.subaru.AllRowsActivity");
    	i.putExtra("allrowsarray", bag);    	
    	startActivity(i);
    }
   
    
    
    //========================================================================
    // pasta = Pass all 
    //---get all rows and send them to AllRowsActivity---
    // call getAllRows and fill a Cursor with all the rows.
    // moveToFirst(): if there is at least one row available
    // pass all the cursor with all the rows to DisplayRow()
    //========================================================================
    public void pasta(View view){
    	ArrayList<String> bag = new ArrayList<String>();
        DBAdapter db = new DBAdapter(this);

        db.open();
        Cursor c = db.getAllRows(); // <=== !!! fill "c" with all rows
        Log.d("a1", "M: after 'Cursor c = db.getAllRows()'");
        if(!c.moveToFirst()){
        	Log.d("a1", "M: c.moveToFirst() = false, not a single row is available");
        }
        if (c.moveToFirst()){
        	Log.d("a1", "M: c.moveToFirst() = true");
        	//ArrayList<String> bag = new ArrayList<String>();
            do {
                bag.add("Station: " +   c.getString(6) + "\n" +
                        "Date: "    +   c.getString(1) + "\n" +
                        "Cost: "    +   c.getString(2) + "\n" +
                        "Liters: "  +   c.getString(3) + "\n" +
                        "Odometer: " +  c.getString(4) + "\n" +
                        "Fill: "    +   c.getString(5) + "\n" +
                        "Carwash: " +   c.getString(7));
            } while (c.moveToNext());
        }
        db.close();
    	Intent i = new Intent("com.igoryen.subaru.StationActivity");
    	i.putExtra("allstations", bag);    	
    	startActivity(i);
    }
   
    
    
    //===========================================================================
    // set the date picked in the DatePicker
    // "setDate()" is from activity_main/sbmBtn
    //===========================================================================
    public String setDate(View view) { 
    	date = (datePicker.getYear() + 1) + "-" 
    	        + (datePicker.getMonth() + 1) + "-" 
    	    	+ datePicker.getDayOfMonth();
    	DisplayToast(date);
    	return date;
    }
 
    
    //===========================================================================
    // set the date picked in the DatePicker
    // "setDate()" is from activity_main/sbmBtn
    //===========================================================================
    public String getDate() { 
    	return date;
    }
  
    
    //===========================================================================
    // set the Station selected in the spinner
    //===========================================================================
    public String setStation() {
    	String station;
    	MyOnItemSelectedListener moisl = new MyOnItemSelectedListener();
    	station = moisl.getStation();
    	return station;
    }
  
    
    /*
    //===========================================================================
    //===========================================================================

    private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(
				DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			yr = year;
			month = monthOfYear;
			day = dayOfMonth;
			
			Toast.makeText(getBaseContext(),
					"You have selected : " + (month + 1) +
					"/" + day + "/" + year,
					Toast.LENGTH_SHORT).show();
		}
	};
	*/
    
    
    
    //===========================================================================
    // DisplayToast() - the DisplayToast code, factored out
    //===========================================================================
    private void DisplayToast(String msg){
    	Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    	Log.d("a1", "M: Toast fired");
    }

    

    
} // activity
