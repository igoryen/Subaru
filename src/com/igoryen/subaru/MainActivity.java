package com.igoryen.subaru;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBAdapter db = new DBAdapter(this);
        Log.d("a1", "Main: a dbadapter created");
        
        /*/---add a contact ---
        db.open();
        //long id = db.insertContact("Wei-Meng Lee", "weimenglee@learn2develop.net");
        long id = db.insertRow("2004-03-29", "22.24", "32.750", "1198", "fill", "petrocan", "8.55");
        db.close();
        */
        
        /*
        //--get all contacts---
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
        //---update contact---
        db.open();
        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
            Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
        db.close();
        */
        
        /*
        //---delete a contact---
        db.open();
        if (db.deleteContact(1))
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();
        */
        
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            Log.d("a1", "Main: "+ destPath);
            File f = new File(destPath);
            if(f.exists()){
            	Log.d("a1", "Main: f.exists() = true");
            }
            if (!f.exists()) { 
            	Log.d("a1", "Main: f.exists() = false");
            	f.mkdirs();
                f.createNewFile();
            	
            	//---copy the db from the assets folder into 
            	// the databases folder---
                Log.d("a1", "Main: before CopyDB");
                CopyDB(getBaseContext().getAssets().open("subaru"), new FileOutputStream(destPath + "/subaru"));
                Log.d("a1", "Main: after CopyDB");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        //---get all contacts---
        db.open();
        Cursor c = db.getAllRows(); // fill "c" with all rows
        Log.d("a1", "Main: after 'Cursor c = db.getAllRows()'");
        if(!c.moveToFirst()){
        	Log.d("a1", "Main: c.moveToFirst() = false, not a single row is available");
        }
        if (c.moveToFirst()){
        	Log.d("a1", "Main: c.moveToFirst() = true");
            do {
                DisplayRow(c);
            } while (c.moveToNext());
        }
        db.close();
    }
    //==========================================================
    
    public void CopyDB(InputStream inputStream, 
    OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public void DisplayRow(Cursor c){
    	//String CNAME = "station";
    	//Log.d("a1", "Main: entered DisplayRow(); _id: " + c.getString(c.getColumnIndex("_id")));
    	//Log.d("a1", "Main: adate: " + c.getString(c.getColumnIndex("adate")));
    	Log.d("a1", "Main: cost: " + c.getString(c.getColumnIndex("cost")));
    	//Log.d("a1", "Main: liters: " + c.getString(c.getColumnIndex("liters")));
    	//Log.d("a1", "Main: odometer: " + c.getString(c.getColumnIndex("odometer")));
    	//Log.d("a1", "Main: fill: " + c.getString(c.getColumnIndex("fill")));
    	//Log.d("a1", "Main: station: " + c.getString(c.getColumnIndex("station")));
    	//Log.d("a1", "Main: carwash: " + c.getString(c.getColumnIndex("carwash")));

    	String msg = 	"id: " 		+	c.getString(0) + "\n" +
		                "Date: " 	+	c.getString(1) + "\n" +
		                "Cost: " 	+	c.getString(2) + "\n" +
		                "Liters: " 	+	c.getString(3) + "\n" +
		                "Odometer: " + 	c.getString(4) + "\n" +
		                "Fill: " 	+	c.getString(5) + "\n" +
		                "Station: " + 	c.getString(6) + "\n" +
		                "Carwash: " + 	c.getString(7);
    	Log.d("a1", "Main: in DisplayRow(), msg filled");

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    //==========================================================
}
