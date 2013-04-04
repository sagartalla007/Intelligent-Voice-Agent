package com.example.iva;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class GetApps extends AsyncTask<String, Void, List<ApplicationInfo>>{

	    MainActivity main;
	    //Set<ApplicationInfo> installedApps = new HashSet<ApplicationInfo>();
	    PackageManager pm;
	    
	    String name;
    
	    public GetApps(MainActivity main,String name) {
		  this.main = main;
		  pm = main.getPackageManager();
		  this.name = name;
		}
	
		@Override
		protected List<ApplicationInfo> doInBackground(String... arg0) {
			
			 
			List<ApplicationInfo> apps = pm.getInstalledApplications(0);		
			/*Set<ApplicationInfo> installedApps = new HashSet<ApplicationInfo>();
			for(ApplicationInfo app : apps) {
			    //checks for flags; if flagged, check if updated system app
			    if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
			        installedApps.add(app);
			       
			    //it's a system app, not interested
			    } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
			        //Discard this one
			    //in this case, it should be a user-installed app
			    } else {
			        installedApps.add(app);
			       
			    }
			}*/
							
			return apps;
		}
		
		@Override
		protected void onPostExecute(List<ApplicationInfo> result) {
		super.onPostExecute(result);
		
		for(ApplicationInfo app : result)
		{
			Log.i("post exe", (String) pm.getApplicationLabel(app));
			
		}
		launchApp(result,name);
		
		}
		
		
        public void launchApp(List<ApplicationInfo> result,String name){
        	
        	//Log.i("launchApp ", String.valueOf(installedApps.size()));
        	boolean flag = false;
        	
        	for(ApplicationInfo app : result)
        	{
        		Log.i("outer for", app.packageName+"   "+pm.getApplicationLabel(app));
        		if(pm.getApplicationLabel(app).toString().equalsIgnoreCase(name) || pm.getApplicationLabel(app).toString().startsWith(name) || pm.getApplicationLabel(app).toString().endsWith(name))
        		{
        			flag = true;
        			try{
        				Log.i("LAUNCHING PKG NAME",app.packageName);
        				Intent i = main.getPackageManager().getLaunchIntentForPackage(app.packageName);
        				main.startActivity(i);
        			}catch(ActivityNotFoundException e){
        				
        				main.wv.loadUrl("javascript:addTextS(\""+ "problem in launching application" + name +"\")");
        		        //main.Listitems.add("server:"+"problem in launching application" + name);
        			}
        			
        				
        			break;
        		      			
        		}
        	       		
        		
        	}
        	 if(!flag)
    			{
        		 	main.wv.loadUrl("javascript:addTextS(\""+ "could not find application let me search the google play" + name +"\")");
        		 	//main.Listitems.add("server:"+"could not find application let me search the google play" + name);
        		 	
        		 	Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://search?q=" + name));
        		 	main.startActivity(goToMarket);        		
    			}   		        
    		
        } 		
}
