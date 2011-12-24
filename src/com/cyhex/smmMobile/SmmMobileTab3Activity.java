package com.cyhex.smmMobile;


import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class SmmMobileTab3Activity extends Activity {
    
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
    		 drawChart();
 		} catch (JSONException e) {
 			Log.e("SmmMobileTab3Activity",e.toString());
 		}
    }
    
    
   
    public void drawChart() throws JSONException{

		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(18);
		renderer.setLegendTextSize(18);
		renderer.setChartTitleTextSize(18);
		renderer.setZoomEnabled(false);
		renderer.setPanEnabled(false);
		renderer.setChartTitle("Cumulative Chart");
		renderer.setInScroll(false);
		renderer.setScale((float) 1.0);
		renderer.setMargins(new int[] { 15, 15, 15, 0 });
		
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.GREEN);
		renderer.addSeriesRenderer(r);

		r = new XYSeriesRenderer();
		r.setColor(Color.RED);
		renderer.addSeriesRenderer(r);

		r = new XYSeriesRenderer();
		r.setColor(Color.GRAY);
		renderer.addSeriesRenderer(r);
		
		CategorySeries data = null;
		try {
			data = getChartDataset();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(data != null){
			setContentView(ChartFactory.getPieChartView(this, data, renderer));
		}
	}
    
    private CategorySeries getChartDataset() throws JSONException {
    	JSONObject timeline = SmmConnectionClient.getResultsObj("totals");
    	double tVolScoreTVal = timeline.getJSONObject("volume").getDouble("total");
    	double tVolScorePVal = timeline.getJSONObject("volume").getDouble("positive");
    	double tVolScoreNVal = timeline.getJSONObject("volume").getDouble("negative");
    	double tVolScoreNeVal = timeline.getJSONObject("volume").getDouble("neutral");
    	
    	CategorySeries series = new CategorySeries("Totals");
        series.add("Positive (" + Double.toString( (tVolScorePVal / tVolScoreTVal) *100.0) + "%)"  , tVolScorePVal);
        series.add("Negative (" + Double.toString( (tVolScoreNVal / tVolScoreTVal) *100.0) + "%)" , tVolScoreNVal);
        series.add("Neutral (" +  Double.toString( (tVolScoreNeVal / tVolScoreTVal)*100.0) + "%)" , tVolScoreNeVal);
    	
    	return series;
    	
    }
   
}