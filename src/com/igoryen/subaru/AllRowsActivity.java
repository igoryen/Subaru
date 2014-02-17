package com.igoryen.subaru;

//import android.app.ActionBar;
//import android.app.Activity;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class AllRowsActivity extends ListActivity{ // change to "ListActivity" to use with ListView
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allrowsactivity);
		
		//Toast.makeText(this, getIntent().getStringExtra("AllRows"), Toast.LENGTH_LONG).show();
		
		ListView lstView = getListView();
		lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);        
	    lstView.setTextFilterEnabled(true);

	    
	    Intent i = getIntent();  
	    ArrayList<String> allrowsarray = i.getStringArrayListExtra("allrowsarray");
	    

	    setListAdapter(new ArrayAdapter<String>(this,
	       android.R.layout.simple_list_item_1, allrowsarray));
	        
			//Log.d("Log", "Third: Third activity is loaded");
	}
	
	private void DisplayToast(String msg){
    	Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
