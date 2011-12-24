package com.cyhex.smmMobile;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
//import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class SmmMobileResultsActivity extends TabActivity implements Runnable{
	
	private Thread updateRunner = null;
	private Boolean updateRunFlag = true;
	private TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	
    	setContentView(R.layout.results);
    	tabHost = getTabHost();  // The activity TabHost
    	
    	updateGui();
        tabHost.setCurrentTab(0);
        
        //start interval loader
        updateRunner = new Thread(SmmMobileResultsActivity.this);
        updateRunner.start();
        
        setTitle("SmmMobile - " + SmmConnectionClient.getSearchString() );
        
        
       
    }
    
    public void updateGui(){
        
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SmmMobileTab1Activity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("tab1").setIndicator("Sentiment").setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, SmmMobileTab2Activity.class);
        spec = tabHost.newTabSpec("tab2").setIndicator("Cumulative").setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, SmmMobileTab3Activity.class);
        spec = tabHost.newTabSpec("tab3").setIndicator("C.Chart").setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, SmmMobileTab4Activity.class);
        spec = tabHost.newTabSpec("tab4").setIndicator("Volume").setContent(intent);
        tabHost.addTab(spec);
       
    }
    
    
   
    
    @Override
    public void onPause(){
    	super.onPause();
    	updateRunFlag = false;
    }

	@Override
	public void run() {
		SystemClock.sleep(20000);
		// reload remote data
		while (updateRunFlag && SmmConnectionClient.canRealod()){
			Log.i("interval_loader","running update");
			SmmConnectionClient.load();
			handler.sendEmptyMessage(0);
			SystemClock.sleep(20000);
		}
	}
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	
        	LocalActivityManager manager = getLocalActivityManager();    
        	Activity curActivity = manager.getCurrentActivity();
        	
        	if(curActivity instanceof SmmMobileTab1Activity ){
        		((SmmMobileTab1Activity) curActivity).updateGui();
        	}
        	if(curActivity instanceof SmmMobileTab2Activity ){
        		((SmmMobileTab2Activity) curActivity).updateGui();
        	}
        	if(curActivity instanceof SmmMobileTab3Activity ){
        		((SmmMobileTab3Activity) curActivity).updateGui();
        	}
        	if(curActivity instanceof SmmMobileTab4Activity ){
        		((SmmMobileTab4Activity) curActivity).updateGui();
        	}
        	
        }
	};
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main_menu, menu);
	        return true;
	    }
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.m_about:
				Intent myIntent1 = new Intent(SmmMobileResultsActivity.this, SmmMobileAboutActivity.class);
	            startActivityForResult(myIntent1, 0);
				break;
			case R.id.m_contact:
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://smm.streamcrab.com/contact"));
				startActivity(browserIntent);
				break;
			}
			return true;
		}
	
}