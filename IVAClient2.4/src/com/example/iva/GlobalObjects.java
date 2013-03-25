package com.example.iva;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.Context;

public class GlobalObjects extends Application {

	public static DefaultHttpClient client = new DefaultHttpClient();
	public static String userId = "-1";
	public static MainActivity mainActivity;
	public static Context widgetContext;
	public static String bot_responce;
	public static String user_request;
	public static AppWidgetManager appWidgetManager;
	public static int[] allWidgetIds;
	
	/*public static DefaultHttpClient getThreadSafeClient() {
		        DefaultHttpClient client2 = new DefaultHttpClient();
		        ClientConnectionManager mgr = client.getConnectionManager();
		        HttpParams params = client.getParams();
		    client2 = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
		                mgr.getSchemeRegistry()), params);
		   
		        return client2;
		    }
*/	

	public static DefaultHttpClient getThreadSafeClient()
	{
		DefaultHttpClient client2 = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client2.getParams();
		client2 = new DefaultHttpClient(new ThreadSafeClientConnManager(params,mgr.getSchemeRegistry()), params);
      
		return client2;
			
	}
	
}
