package com.example.iva.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.iva.GlobalObjects;
import com.example.iva.LauncherProcessor;
import com.example.iva.R;
import com.example.iva.WidgetInit;
import com.example.iva.widget.MyResultReceiver.Receiver;


public class WidgetProvider extends AppWidgetProvider implements Receiver{

	
	public MyResultReceiver mReceiver;
	WidgetInit wgi;
	AppWidgetManager appWidgetManager;
	Context context;
	String request = "";
	LauncherProcessor l;
	int[] allWidgetIds;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		
		this.context = context; 
		this.appWidgetManager = appWidgetManager;
		ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
	    allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		GlobalObjects.widgetContext = context;
		GlobalObjects.appWidgetManager = appWidgetManager;		
		GlobalObjects.allWidgetIds = allWidgetIds;
		
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		    	
	    		
		Intent intent = new Intent(context,DialogActivity.class);
    	intent.putExtra("receiverTag", mReceiver);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	
		wgi = new WidgetInit();
		GlobalObjects.widgetInit = wgi;
		wgi.setIntent(R.id.imageButton1, pendingIntent);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		
		String responce = resultData.getString("responce"); 
		String request = resultData.getString("request");
		Log.i("DATA RECEIVED",responce);
		GlobalObjects.bot_responce = responce;
		GlobalObjects.user_request = request;	    
	    wgi.printData(R.id.textView2,request);
	    sendToProcess(responce);
				
	}
	
	public void sendToProcess(String responce)
	  {
		
		  l = new LauncherProcessor(false);
		  responce = responce.replaceAll("\\\"", "'");
		
		  if(responce.contains("<oob>"))
		  {
			  
			  l.process(responce);			  
		  }
			  
		  
		  else{
			 
			  
			  if(responce.contains("<a href"))
			  {
				  
				  
			  }else{
				   wgi.printData(R.id.textView1, responce);
			  }
		      
		  }
		  
		  
	  }
	
	 
	  
	
}
