package com.example.iva;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class KeyCheck {
	
	private static final String PREFS_NAME = "LoginKey";
	private Activity main;
	private String userId = null;
	
	
	public KeyCheck(Activity main) {
			
		this.main = main;
				 
	}
	
	
	private class DownloadKeyTask extends AsyncTask<String, Void, String> {

		ProgressDialog myPd_ring;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			myPd_ring=ProgressDialog.show(main, "Please wait", "Loading please wait..", true);
	        myPd_ring.setCancelable(true);
		}
		
		
		@Override
		protected String doInBackground(String... urls) {
			HttpGet httpGet;
			String s="";
			try {
				httpGet = new HttpGet("http://ivaserver.intelligent-voice-agent.cloudbees.net/Login");
			    HttpResponse execute = GlobalObjects.client.execute(httpGet);
	            InputStream content = execute.getEntity().getContent();
	            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	            s = buffer.readLine();
	            
			} catch (Exception e) {
	                         e.printStackTrace();
	                         }
			
			return s;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.i("POST EXE", result);
			if(result.length()<=36)
		    {
				
				storeKey(result);
				
		    }
			myPd_ring.dismiss();
			
		}
	}
	
	private void storeKey(String key) {
	
		  Log.i("storeKey",key);
		  userId = key;	
		  SharedPreferences settings = main.getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("userKey", key);				
	      editor.commit();
	      GlobalObjects.userId = key;
	}
	
	public void fetchKey() {
	
        
	    	DownloadKeyTask d = new DownloadKeyTask();
	        d.execute(new String[] { null });
	    				    			
					
	    	 
	     }
	
	public String getKey()
	{
		SharedPreferences restore= main.getSharedPreferences(PREFS_NAME, 0);
		userId = restore.getString("userKey", null);
		if(userId == null)
		{
			fetchKey();			
		}
		else Log.i("GETKEY",userId);
		GlobalObjects.userId = userId;
		return userId;
	}
}
