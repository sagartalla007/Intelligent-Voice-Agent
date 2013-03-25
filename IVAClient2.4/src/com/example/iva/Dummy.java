package com.example.iva;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class Dummy extends Activity {

	ProgressDialog myPd_ring;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy);
	
		
		myPd_ring=ProgressDialog.show(Dummy.this, "Please wait", "Loading please wait..", true);
        myPd_ring.setCancelable(true);
		  
        
					SampleTask t = new SampleTask();
					t.execute();

            	
		}

	
	private class SampleTask extends AsyncTask<Void, Void, String>{

		
		
				@Override
				  protected void onPreExecute(){ 
			           super.onPreExecute();
			                    
			        }
		
		@Override
		protected String doInBackground(Void... params) {
			
			
			HttpGet httpGet;
			String response="";
			try {
				httpGet = new HttpGet("http://google.co.in");
			    	
				HttpResponse execute = GlobalObjects.getThreadSafeClient().execute(httpGet);
	     			
				InputStream content = execute.getEntity().getContent();
	            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	            String s = "";
	            while ((s = buffer.readLine()) != null) {
	                            response += s;
	                       }
	            
	            execute.getEntity().consumeContent();
				
				}			
			catch(Exception e) {
				e.printStackTrace();
				}
			finally {
				
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			myPd_ring.dismiss();
			Log.i("SAMPLETASK", result);
		}
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_dummy, menu);
		return true;
	}

	
}
