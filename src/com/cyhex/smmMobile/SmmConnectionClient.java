package com.cyhex.smmMobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SmmConnectionClient {
	static private String searchString = "";
	static private String serverURI = "http://smm.streamcrab.com/poolResults/?search=%s&from_id=%s&mobile=1";
	static private InputStream httpStream;
	static private String httpResult;
	static private JSONObject jArray;
	static private Boolean dataLoaded = false;
	static private int loadCount = 0;
	
	static private CookieStore cookieStore = new BasicCookieStore();
	static private HttpContext localContext = new BasicHttpContext();
	
	private SmmConnectionClient(){
		throw new AssertionError();
	}
	
	
	public static void resetData(){
		searchString = null;
		httpStream = null;
		httpResult = null;
		jArray = null;
		dataLoaded = false;
		loadCount = 0;
	}
	
	public static JSONObject getResultsObj(String part){
		try{
			return jArray.getJSONObject("pooling").getJSONObject(part);
		}catch  (Exception e){
			Log.e("SmmConnectionClient", "json parsing " + e.toString());
		}
		return null;
	}
	
	public static JSONArray getResults(String part){
		try{
			return jArray.getJSONObject("pooling").getJSONArray(part);
		}catch  (Exception e){
			Log.e("SmmConnectionClient", "json parsing " + e.toString());
		}
		return null;
	}
	
	public static void setSearchString(String search){
		searchString = search.toLowerCase().trim();
	}
	
	public static String getSearchString(){
		return searchString;
	}
	
	public static void load(){
		loadData();
	}
	
	private static String buildURL(){
		String URI;
		try {
			URI = String.format(serverURI, URLEncoder.encode(searchString,"utf-8"), getLastId() );
		} catch (UnsupportedEncodingException e) {
			URI = null;
		}
		Log.i("dataloader",URI);
		return URI;
		
	}
	
	private static void loadData(){
		
		try {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

			DefaultHttpClient client = new DefaultHttpClient(); 
			HttpGet getRequest = new HttpGet(buildURL());
			HttpResponse getResponse = client.execute(getRequest,localContext);
			HttpEntity getResponseEntity = getResponse.getEntity();
			httpStream = getResponseEntity.getContent();

		} catch (IOException e) {
			Log.w("SmmConnectionClient", "Error for URL " + serverURI, e);
		}
		
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpStream, "utf8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			httpStream.close();
			httpResult = sb.toString();
			
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		
		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(httpResult);
		} catch (JSONException e) {
			Log.e("httpResult", "Error parsing data " + e.toString());
		}
		
		dataLoaded = true;
		loadCount +=1 ;
	}
	
	public static String getLastId() {
		if(dataLoaded == false){
			return "0";
		}
		try {
			return jArray.getJSONObject("pooling").getString("last_id");
		} catch (Exception e) {
			return "0";
		}
	}
	
	public static Boolean canRealod(){
		return loadCount <= 49;
	}
}
