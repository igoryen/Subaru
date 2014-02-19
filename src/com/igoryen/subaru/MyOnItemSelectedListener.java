package com.igoryen.subaru;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class MyOnItemSelectedListener implements OnItemSelectedListener {

	public String station;
	@Override
	public void onItemSelected(AdapterView parent, View view, int pos, long id) {
		
		station = "Selected Station: " + parent.getItemAtPosition(pos).toString();
		Toast.makeText(parent.getContext(), station, Toast.LENGTH_SHORT).show();
	}

	public String getStation(){
		return station;
	}
	
	@Override
	public void onNothingSelected(AdapterView parent) {

	}
		
}