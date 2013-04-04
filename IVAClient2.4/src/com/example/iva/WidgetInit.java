package com.example.iva;

import android.app.PendingIntent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetInit {

	RemoteViews remoteViews;
	
	public WidgetInit(){
		try{
		remoteViews = new RemoteViews(GlobalObjects.widgetContext.getPackageName(), R.layout.widget1);
		}catch (Exception e) {
			Log.i("WIDGETINIT", "NO widget Present");
		}
	}
	public void printData(int id,String data)
	{
	   Log.i("OOB WIDGET", data);
  	   remoteViews.setTextViewText(id, data);
  	   GlobalObjects.appWidgetManager.updateAppWidget(GlobalObjects.allWidgetIds, remoteViews);	
	}
	public void setIntent(int id, PendingIntent pi)
	{
		
		remoteViews.setOnClickPendingIntent(id, pi);
		GlobalObjects.appWidgetManager.updateAppWidget(GlobalObjects.allWidgetIds, remoteViews);
	}
	
}
