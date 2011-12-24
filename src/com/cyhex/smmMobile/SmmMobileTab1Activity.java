package com.cyhex.smmMobile;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class SmmMobileTab1Activity extends Activity {

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
 		} catch (JSONException e) {
 			Log.e("SmmMobileTab1Activity",e.toString());
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
		renderer.setYTitle("Combined score");
		renderer.setXTitle("Seconds Ago");
		renderer.setChartTitle("Realtime Sentiment Analysis");
		renderer.setMargins(new int[] { 25, 35, 25, 0 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setPointStyle(PointStyle.TRIANGLE);
		r.setColor(Color.GREEN);
		r.setLineWidth(0.0001f);
		renderer.addSeriesRenderer(r);
		
		r = new XYSeriesRenderer();
		r.setPointStyle(PointStyle.CIRCLE);
		r.setColor(Color.RED);
		r.setLineWidth(0.0001f);
		renderer.addSeriesRenderer(r);
		
		r = new XYSeriesRenderer();
		r.setColor(Color.YELLOW);
		renderer.addSeriesRenderer(r);
		
		renderer.setAxesColor(Color.DKGRAY);
		renderer.setLabelsColor(Color.LTGRAY);
        
		return renderer;
		
    }
    
    private XYMultipleSeriesDataset getChartDataset() throws JSONException {
    	JSONArray timeline = SmmConnectionClient.getResults("timeline");
    	JSONArray sma = SmmConnectionClient.getResults("sma");
    	
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries seriesP = new XYSeries("Positive Tweets");
        XYSeries seriesN = new XYSeries("Negative Tweets");
        XYSeries seriesSMA = new XYSeries("Moving Average");
        
        long now = System.currentTimeMillis()/1000;
        if(timeline == null){
        	return null;
        }
        
        for (int k = 0; k < timeline.length(); k++) {
        	double val =  timeline.getJSONArray(k).getDouble(1);
        	Integer stamp =  timeline.getJSONArray(k).getInt(0);
        	if(val < 0){
        		seriesN.add( stamp - now, val);
        	}else if (val > 0){
        		seriesP.add(stamp - now, val);
        	}
        }
        
        for (int k = 0; k < sma.length(); k++) {
        	double val =  sma.getJSONArray(k).getDouble(1);
        	Integer stamp = sma.getJSONArray(k).getInt(0);
        	seriesSMA.add(stamp - now, val);
        }
        
        dataset.addSeries(seriesP);
        dataset.addSeries(seriesN);
        dataset.addSeries(seriesSMA);
        
        return dataset;
      }
}