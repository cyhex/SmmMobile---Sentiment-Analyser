package com.cyhex.smmMobile;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class SmmMobileTab4Activity extends Activity {
private GraphicalView chart;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 updateGui();
    }
    
	@Override
	public void onResume(){
		super.onResume();
		updateGui();
	}
	
	public void updateGui(){
		XYMultipleSeriesDataset data = null;
 		try {
 			data = getChartDataset();
 		} catch (Exception e) {
 			Log.e("SmmMobileTab4Activity",e.toString());
 			
 		}
 		
 		if(data != null){
 			XYMultipleSeriesRenderer renderer = getRenderer();
 	 		chart = ChartFactory.getLineChartView(this, data, renderer);
 	 		setContentView(chart);
 		}
 		
 		
	}
    
    private XYMultipleSeriesRenderer getRenderer(){
    	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(18);
		renderer.setChartTitleTextSize(18);
		renderer.setLabelsTextSize(18);
		renderer.setLegendTextSize(18);
		renderer.setZoomEnabled(true);
		renderer.setShowGrid(true);
		renderer.setShowLabels(true);
		renderer.setYTitle("Volume (Tweets)");
		renderer.setXTitle("Seconds Ago");
		renderer.setChartTitle("Cumulative Volume Data ");
		renderer.setMargins(new int[] { 25, 35, 25, 0 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.GRAY);
		r.setFillBelowLine(true);
		r.setFillBelowLineColor(Color.GRAY);
		renderer.addSeriesRenderer(r);
		
		r = new XYSeriesRenderer();
		r.setColor(Color.GREEN);
		renderer.addSeriesRenderer(r);
		
		r = new XYSeriesRenderer();
		r.setColor(Color.RED);
		renderer.addSeriesRenderer(r);
		
		r = new XYSeriesRenderer();
		r.setColor(Color.rgb(0, 66, 99));
		renderer.addSeriesRenderer(r);
		
		
		
		renderer.setAxesColor(Color.DKGRAY);
		renderer.setLabelsColor(Color.LTGRAY);
        
		return renderer;
		
    }
    
    private XYMultipleSeriesDataset getChartDataset() throws JSONException {
    	JSONObject volumeData = SmmConnectionClient.getResultsObj("volume");
    	
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries seriesP = new XYSeries("Positive Volume");
        XYSeries seriesN = new XYSeries("Negative Volume");
        XYSeries seriesNe = new XYSeries("Netural Volume");
        XYSeries seriesC = new XYSeries("Cumulative Volume");
        
        JSONArray volumeDataP = volumeData.getJSONArray("positive");
        JSONArray volumeDataN = volumeData.getJSONArray("negative");
        JSONArray volumeDataNe = volumeData.getJSONArray("neutral");
        JSONArray volumeDataC = volumeData.getJSONArray("total");
        
        
        seriesP = creatSeries(volumeDataP,seriesP);
        seriesN = creatSeries(volumeDataN,seriesN);
        seriesNe = creatSeries(volumeDataNe,seriesNe);
        seriesC = creatSeries(volumeDataC,seriesC);
        
        dataset.addSeries(seriesC);
        dataset.addSeries(seriesP);
        dataset.addSeries(seriesN);
        dataset.addSeries(seriesNe);
       
        
        return dataset;
      }
    
    private XYSeries creatSeries(JSONArray volCollection, XYSeries series) throws JSONException{
    	long now = System.currentTimeMillis()/1000;
    	for (int k = 0; k < volCollection.length(); k++) {
    		Integer val =  volCollection.getJSONArray(k).getInt(1);
         	Integer stamp = volCollection.getJSONArray(k).getInt(0);
         	series.add(stamp - now, val);
         }
    	return series;
    }
    
}