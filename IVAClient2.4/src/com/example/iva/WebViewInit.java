package com.example.iva;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewInit {

	WebView wv;
	String ClientName;
	SharedPreferences settings_restore;
	@SuppressLint("SetJavaScriptEnabled")
	public void loadWebView(){
	    
		GlobalObjects.webView = (WebView) GlobalObjects.mainActivity.findViewById(R.id.webView01);
	    wv = GlobalObjects.webView; 
			       
	    wv.setBackgroundColor(0);
	    wv.setBackgroundResource(color.transparent);
	    wv.setWebViewClient(new WebViewClient());
	    wv.getSettings().setJavaScriptEnabled(true);
	    settings_restore = GlobalObjects.mainActivity.getSharedPreferences("SETTINGS_DATA", 0);
	    String theme = settings_restore.getString("selecttheme", "theme1");
	    if(theme.equalsIgnoreCase("theme1"))
	    	wv.loadUrl("file:///android_asset/www/index.html");
	    else if(theme.equalsIgnoreCase("theme2"))
	    	wv.loadUrl("file:///android_asset/www/index2.html");
	    else if (theme.equalsIgnoreCase("theme3")) 
	    	wv.loadUrl("file:///android_asset/www/index3.html");
	    
	    wv.setWebViewClient(new WebViewClient() {

	    	   public void onPageFinished(WebView view, String url) {
	    	    
	    		   GlobalObjects.mainActivity.Listitems = new MyArrayList();
	    	   //SharedPreferences settings_restore = GlobalObjects.mainActivity.getSharedPreferences("SETTINGS_DATA", 0);
	   		    
	   		   String userPath = settings_restore.getString("userpath", "images/photo.jpg");
	   		   String botPath = settings_restore.getString("botpath", "images/photo.jpg");
	   		   String clientName = settings_restore.getString("clientname", "clientName");
	   		   ClientName = clientName; 
	   		   Log.i("SETING_DATA", userPath+"  "+botPath+"  "+clientName);
	   		      		   
	   		    wv.loadUrl("javascript:setUserImage(\""+ userPath +"\")");
	   		    wv.loadUrl("javascript:setBotImage(\""+ botPath +"\")");
	   		    wv.loadUrl("javascript:setUserName(\""+ clientName +"\")");
	    		   
	   		   	updateName();
	   		    
	   		     GlobalObjects.mainActivity.restoreList(null);
	    	    }
	    	});
				
		
	}
	
	public void printDataAsServer(String data){
		
		// Log.i("hello DATA", data + " " + wv.toString());
		 wv.loadUrl("javascript:addTextS(\""+ data +"\")");
  	     GlobalObjects.mainActivity.Listitems.add("server:"+data);
		
	}
	
	public void printDataAsClient(String data){
		
		 wv.loadUrl("javascript:addTextC(\""+ data +"\")");
 	     GlobalObjects.mainActivity.Listitems.add("client:"+data);
		
	}
	
	public void setUserpath(String path){
		wv.loadUrl("javascript:setUserImage(\""+ path +"\")");	     	
	}
	
	public void setBotimage(String path){		
		 wv.loadUrl("javascript:setBotImage(\""+ path +"\")");
	}
	public void setUserName(String name){
		wv.loadUrl("javascript:setUserName(\""+ name +"\")");
	}
	public void updateName() {
		
		class UpdateName extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... arg0) {
				
				try {
								
				   HttpGet	httpGet = new HttpGet("http://ivaserver.intelligent-voice-agent.cloudbees.net/MainBotChat?input=I am " + ClientName);
				   HttpResponse execute = GlobalObjects.getThreadSafeClient().execute(httpGet);
		     			             
					}			
				catch(Exception e) {
					e.printStackTrace();
					}
				finally {
				    
				}
		
				
				return null;
			}
 		
	}

		UpdateName upname = new UpdateName();
		upname.execute(new String[] {""});
}
}