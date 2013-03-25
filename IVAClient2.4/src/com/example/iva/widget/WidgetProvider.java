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
import android.widget.RemoteViews;

import com.example.iva.GlobalObjects;
import com.example.iva.LauncherProcessor;
import com.example.iva.R;
import com.example.iva.widget.MyResultReceiver.Receiver;


public class WidgetProvider extends AppWidgetProvider implements Receiver{

	
	public MyResultReceiver mReceiver;
	RemoteViews remoteViews;
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
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget1);
	    	
	    	// Register an onClickListener
	    	/*Intent intent = new Intent(context,MainService.class);
	    	intent.putExtra("request", request);
	    	intent.putExtra("receiverTag", mReceiver);
	    	PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    	remoteViews.setOnClickPendingIntent(R.id.imageButton1, pendingIntent);
	    	appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);*/
		
		Intent intent = new Intent(context,DialogActivity.class);
    	intent.putExtra("receiverTag", mReceiver);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	remoteViews.setOnClickPendingIntent(R.id.imageButton1, pendingIntent);
    	appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		
		String responce = resultData.getString("responce"); 
		String request = resultData.getString("request");
		Log.i("DATA RECEIVED",responce);
		GlobalObjects.bot_responce = responce;
		/*ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
	    allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);*/
	    
	    
	    
	    for(int widgetId:allWidgetIds){
	    	   		    	  	
	    	remoteViews.setTextViewText(R.id.textView2, request);
	    	appWidgetManager.updateAppWidget(widgetId, remoteViews);
	    	
	    }
	    
	    sendToProcess(responce);
		
		
	}
	
	public void sendToProcess(String responce)
	  {
		
		  l = new LauncherProcessor(GlobalObjects.mainActivity,false);
		  responce = responce.replaceAll("\\\"", "'");
		  
		  //Log.i("Responce",responce);
		  
		  if(responce.contains("<oob>"))
		  {
			  
			  l.process(responce);			  
		  }
			  
		  
		  else{
			 /* for(int widgetId:allWidgetIds)				    
			  {
				  remoteViews.setTextViewText(R.id.textView1, responce);
				  appWidgetManager.updateAppWidget(widgetId, remoteViews);
			  }
	     	  
	     	  if(responce.contains("<a href"))
	     	  {
	     		  String speak = responce.substring(0, responce.indexOf("<a href"));
	     		// tts.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
	     	  }else {
	     		   
	     		// tts.speak(responce, TextToSpeech.QUEUE_FLUSH, null);
	     	  }*/
			  
			  if(responce.contains("<a href"))
			  {
				  
				  
			  }else{
				  for(int widgetId:allWidgetIds)				    
				  {
					  remoteViews.setTextViewText(R.id.textView1, responce);
					  appWidgetManager.updateAppWidget(widgetId, remoteViews);
				  }			  
			  }
		      
		  }
		  
		  
	  }
	
	 
	  
	
}
