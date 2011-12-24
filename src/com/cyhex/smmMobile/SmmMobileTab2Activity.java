package com.cyhex.smmMobile;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SmmMobileTab2Activity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 setContentView(R.layout.tab2);
    	 updateGui();
    }
    
    @Override
	public void onResume(){
		super.onResume();
		updateGui();
	}
    
    public void updateGui(){
    	 try {
 			fillTotals();
 		} catch (JSONException e) {
 			Log.e("SmmMobileTab2Activity",e.toString());
 		}
    }
    
    
    public Boolean fillTotals() throws JSONException{
    	JSONObject timeline = SmmConnectionClient.getResultsObj("totals");
    	
    	if(timeline == null){
    		return false;
    	}
    	
    	int tVolScoreTVal = timeline.getJSONObject("volume").getInt("total");
    	int tVolScorePVal = timeline.getJSONObject("volume").getInt("positive");
    	int tVolScoreNVal = timeline.getJSONObject("volume").getInt("negative");
    	int tVolScoreNeVal = timeline.getJSONObject("volume").getInt("neutral");
    	
    	TextView tVolScoreT = (TextView) findViewById(R.id.tVolScoreT);
    	tVolScoreT.setText(Integer.toString(tVolScoreTVal));
    	
    	TextView tVolScoreP = (TextView) findViewById(R.id.tVolScoreP);
    	tVolScoreP.setText(Integer.toString(tVolScorePVal) + " (" + Integer.toString((tVolScorePVal * 100 / tVolScoreTVal)) + " %)");
    	
    	TextView tVolScoreN = (TextView) findViewById(R.id.tVolScoreN);
    	tVolScoreN.setText(Integer.toString(tVolScoreNVal) + " (" + Integer.toString((tVolScoreNVal * 100 / tVolScoreTVal)) + " %)");
    	
    	TextView tVolScoreNe = (TextView) findViewById(R.id.tVolScoreNe);
    	tVolScoreNe.setText(Integer.toString(tVolScoreNeVal) + " (" + Integer.toString((tVolScoreNeVal * 100 / tVolScoreTVal)) + " %)");
    	
    	TextView aVolScoreC = (TextView) findViewById(R.id.aVolScoreC);
    	aVolScoreC.setText(timeline.getJSONObject("sentiment").getString("total").substring(0,5));
    	TextView aVolScoreN = (TextView) findViewById(R.id.aVolScoreN);
    	aVolScoreN.setText(timeline.getJSONObject("sentiment").getString("negative").substring(0,6));
    	TextView aVolScoreP = (TextView) findViewById(R.id.aVolScoreP);
    	aVolScoreP.setText(timeline.getJSONObject("sentiment").getString("positive").substring(0,5));
		return true;
    	
    	
    }
    
   
}