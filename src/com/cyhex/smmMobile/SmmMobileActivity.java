package com.cyhex.smmMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SmmMobileActivity extends Activity implements Runnable {

	private EditText search;
	
	private ProgressDialog progressDialog;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	if(isOnline()==false){
 			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
 			alertDialog.setMessage("You must have an active internet connection in order to use this app");
 			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
 			      public void onClick(DialogInterface dialog, int which) {
 			    	  finish();
 			    } });
 			alertDialog.show();
 			
 		}
    	
    	
    	search = (EditText) findViewById(R.id.editText1);
    	
    	
        ImageButton next = (ImageButton) findViewById(R.id.searchBtn);
        
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	progressDialog = ProgressDialog.show(SmmMobileActivity.this, "", "Loading remote data...");
            	Thread thread = new Thread(SmmMobileActivity.this);
                thread.start();
            }
        });
    }
    
    
	@Override
	public void run() {
		SmmConnectionClient.resetData();
		SmmConnectionClient.setSearchString(search.getText().toString());
		SmmConnectionClient.load();
		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        		progressDialog.dismiss();
        		
        		if(SmmConnectionClient.getLastId()=="0"){
         			AlertDialog alertDialog = new AlertDialog.Builder(SmmMobileActivity.this).create();
         			alertDialog.setMessage("Sorry no results found");
         			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
         			      public void onClick(DialogInterface dialog, int which) {
         			    } });
         			alertDialog.show();
         			
         		}else{
        		
	        		Intent myIntent = new Intent(SmmMobileActivity.this, SmmMobileResultsActivity.class);
	                startActivityForResult(myIntent, 0);
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
			Intent myIntent = new Intent(SmmMobileActivity.this, SmmMobileAboutActivity.class);
            startActivityForResult(myIntent, 0);
			break;
		case R.id.m_contact:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://smm.streamcrab.com/contact"));
			startActivity(browserIntent);
			break;
		}
		return true;
	}
	
	public boolean isOnline() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;

	}

	
	
}