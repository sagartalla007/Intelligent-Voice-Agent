package com.example.iva.widget;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.iva.GlobalObjects;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class MainService extends IntentService  {

	String response = "";	
	String request = "";
	public MainService() {
		super("MainService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		 
         
		ResultReceiver rec = intent.getParcelableExtra("receiverTag");
        request= intent.getStringExtra("request");
        Log.i("REQUEST DATA",request);

       
		
      final Thread t=   new Thread(new Runnable() {
			
			@Override
			public void run() {
		
				String requestString="";
				try {
					requestString = URLEncoder.encode(request , "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				
		        HttpGet httpGet;
		        try{
		        	
		        	httpGet = new HttpGet("http://ivaserver.intelligent-voice-agent.cloudbees.net/MainBotChat?input="+requestString+"&userId="+GlobalObjects.userId);
				    HttpResponse execute = new DefaultHttpClient().execute(httpGet);
		     			
					InputStream content = execute.getEntity().getContent();
		            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		            String s = "";
		            while ((s = buffer.readLine()) != null) {
		                            response += s;
		                       }	
		        	
		           	            
		        }catch (Exception e) {
		        	e.printStackTrace();
		        }
				
			}
		});
        t.start();
        try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
      	
        
        
        
        Log.i("SENDING BACK DATA",response);
        Bundle b= new Bundle();
        b.putString("responce",response);
        b.putString("request",request);
        rec.send(0, b);
         
       	
	}
	
	
}
