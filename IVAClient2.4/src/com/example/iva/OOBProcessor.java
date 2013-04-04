package com.example.iva;

import java.util.Calendar;

import com.example.iva.widget.WidgetProvider;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews.RemoteView;
import android.widget.RemoteViews;
import android.widget.Toast;



public class OOBProcessor extends MainActivity {
	private MainActivity main;
    private String print_data;
    WebViewInit wvi = GlobalObjects.webViewInit;
    WidgetInit wgi =  GlobalObjects.widgetInit;
    
	public OOBProcessor()
	  {
	    main =  GlobalObjects.mainActivity;
	  }
	
	 public void batteryLevel(String oobData, boolean isActivity)
	  {
	     
		 final Boolean isActivity2 = isActivity;
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                
                if (rawlevel >= 0 && scale > 0) {
                   int battery_level= (rawlevel * 100) / scale;
                   
                   Toast t = Toast.makeText(GlobalObjects.mainActivity, battery_level + "%", 100);
                   t.show();
                                                        
                   print_data = GlobalObjects.bot_responce;
                   print_data=print_data.replace("<oob><battery/></oob>", String.valueOf(battery_level));
                   
                   
                   
                   if(isActivity2){
                	   wvi.printDataAsServer(print_data);
                   }                   
                   else{
                	   wgi.printData(R.id.textView1,print_data);
                   }
                   
                }
                
            }
        };
        
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        GlobalObjects.mainActivity.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
       
    }
	 
	 public void searchWeb(String query,boolean isActivity)
	 {		 
		 print_data = GlobalObjects.bot_responce;
         print_data=print_data.replace("<oob><search>"+query+"</search></oob>", "");
         
         if(isActivity){
        	 wvi.printDataAsServer(print_data);
         }else{
        	 wgi.printData(R.id.textView1,print_data);
         }
		 Uri uri = Uri.parse("http://www.google.com/search?q="+query);
		 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		 GlobalObjects.mainActivity.startActivity(intent);
		
	 }
	 
	 public void gotoUrl(String url,boolean isActivity)
	 {  
		 Log.i("OOBURL", url);
		 		 		 
		 print_data = GlobalObjects.bot_responce;
         print_data=print_data.replace("<oob><url>"+url+"</url></oob>", "");
         
         if(isActivity){
        	 wvi.printDataAsServer(print_data);
             
         }else{
        	 
        	 wgi.printData(R.id.textView1,print_data);
        	 
         }
         
		 Uri uri = Uri.parse(url);
		 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		 GlobalObjects.mainActivity.startActivity(intent);
	 }
	 public void openDialer(String number, boolean isActivity)
	 {
		 String c_name = GlobalObjects.user_request.substring(5);
		 String c_no = "";
		 
		 print_data = "now dailing "+c_name; 
		 
		 Log.i("CONTACT NAME", GlobalObjects.user_request.substring(5)+ "   " + GlobalObjects.user_request.indexOf(" ", 0) + "   " + GlobalObjects.user_request.indexOf(" ", 5));
		 
		 
		    Cursor phones = GlobalObjects.mainActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.Contacts.DISPLAY_NAME + " like '"
		            + c_name + "%'",null, null);
			while (phones.moveToNext())
			{
			  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			  Log.i(name, phoneNumber);
			  c_no = phoneNumber;
			}
			phones.close();		 
		 
			if(c_no.equals(""))
			{				
				print_data = "No contact found with that name";
			}else{
				
				 Uri uri = Uri.parse("tel:"+c_no);
				 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				 main.startActivity(intent);		
				
			}
			
			if(isActivity){
				 wvi.printDataAsServer(print_data);
	             
	         }else{
	        	 
	        	 wgi.printData(R.id.textView1,print_data);
	        	 
	         }
			
		 
	 }
	 public void openSMS(String recipient,String message,boolean isActivity)
	 {
		 int start = GlobalObjects.user_request.indexOf(" ", 0);
		 int end = GlobalObjects.user_request.indexOf(" ", 5);
		 String c_name = GlobalObjects.user_request.substring(start+1,end);
		 String c_no = "";
		 	 
		 print_data = "now sending SMS to "+c_name;
		 		 		 
		  
		 
		 Log.i("CONTACT NAME", c_name);
         
		 Cursor phones = GlobalObjects.mainActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.Contacts.DISPLAY_NAME + " like '"
		            + c_name + "%'",null, null);
			while (phones.moveToNext())
			{
			  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			  Log.i(name, phoneNumber);
			  c_no = phoneNumber;
			}
					
			phones.close();		
		 
			if(c_no.equals(""))
			{
				
				print_data = "No contact found with that name";
			}else{
				
				Uri sms_uri = Uri.parse("smsto:"+c_no); 
		         Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri); 
		         sms_intent.putExtra("sms_body", message); 
		         GlobalObjects.mainActivity.startActivity(sms_intent);
			}
			
			if(isActivity){
				     wvi.printDataAsServer(print_data);
				 }else{
					 wgi.printData(R.id.textView1,print_data);	 
		         }			
			
		 	
         
         
	 }
	 public void setAlarm(String message,String year,String month,String day,String hour,String minute,String duration,boolean isActivity)
	 {
		// Log.i("alarm data", message + year + month + day + hour + minute + duration);
		 int index = GlobalObjects.bot_responce.indexOf("<oob>");
         print_data = GlobalObjects.bot_responce.substring(0,index);
         if(isActivity){
        	 wvi.printDataAsServer(print_data);
         }else{
        	 wgi.printData(R.id.textView1,print_data);
         }
		         Calendar c = Calendar.getInstance();
		         c.setTimeInMillis(java.lang.System.currentTimeMillis());
    	         c.clear();
    	         c.set(Calendar.YEAR, Integer.valueOf(year));
    	         c.set(Calendar.MONTH, Integer.valueOf(month));
    	         c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
    	         c.set(Calendar.HOUR, Integer.valueOf(hour));
    	         c.set(Calendar.MINUTE, Integer.valueOf(minute));
    	             	             	         
    	         Alarm a = new Alarm();    	 		
    	 		 a.SetAlarm(main,c,message);       
    	 		 
    	 		 
	 }
	 
	 public void gotoMAP(String address,boolean isActivity)
	 {
		 print_data = GlobalObjects.bot_responce;
		 print_data = print_data.replace("<oob><map>"+address+"</map></oob>", "");

		 if(isActivity){		
			 wvi.printDataAsServer(print_data);
		 }else{
			 wgi.printData(R.id.textView1,print_data);
         }
		 
		 address = address.replace(" ","+");
		 Intent geoIntent = new Intent (android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
		 main.startActivity(geoIntent);
		 Log.i("GEO DATA", "after launch");
	 }
	 
	 public void showDirections(String from,String to,boolean isActivity)
	 {
		 print_data = GlobalObjects.bot_responce;
		 print_data = print_data.replace("<oob><directions><from>"+from+"</from><to>"+to+"</to></directions></oob>", "");

		 print_data = print_data.replaceAll("\\\"", "'");
		 if(isActivity){
			 wvi.printDataAsServer(print_data);
		 
		 }else{
			 
			 wgi.printData(R.id.textView1,print_data);
		 }
		
         String url;
         if(from == null)
          url= "http://maps.google.com/maps?daddr="+to;
         else  url= "http://maps.google.com/maps?saddr="+from+"&daddr="+to;
         Intent drn = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
         GlobalObjects.mainActivity.startActivity(drn);
		 
	 }
	 
	 public void gpsToggle(String onOff)
	 {
		 if(onOff.equalsIgnoreCase("on"))
		 {
			 Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
			 intent.putExtra("enabled", true);
			 main.sendBroadcast(intent);	
			 
			 Toast t = Toast.makeText(main, "switching on gps", Toast.LENGTH_LONG);
			 t.show();
		 }	
		 
		 else {
			 Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			 intent.putExtra("enabled", false);
			 main.sendBroadcast(intent);
			 
			 Toast t = Toast.makeText(main, "switching off gps", Toast.LENGTH_LONG);
			 t.show();
		 }
	 }
	 
	 public void wifiToggle(String onOff)
	 {
		 if(onOff.equalsIgnoreCase("on"))
		 {
			 final WifiManager wifi = (WifiManager) main.getSystemService(Context.WIFI_SERVICE);
		        wifi.setWifiEnabled(true);
			 
			 Toast t = Toast.makeText(main, "switching on wifi", Toast.LENGTH_LONG);
			 t.show();
		 }	
		 
		 else {
			 final WifiManager wifi = (WifiManager) main.getSystemService(Context.WIFI_SERVICE);
		        wifi.setWifiEnabled(false);
			 
			 Toast t = Toast.makeText(main, "switching off wifi", Toast.LENGTH_LONG);
			 t.show();
		 }
				 
	 }
	 
	 public void launchCamera()
	 {
		 Toast t = Toast.makeText(main, "Launching the camera", Toast.LENGTH_LONG);
		 t.show();
		 
		 Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
	     main.startActivity(intent);
	 }
	 
	 public void bluetoothToggle(String onOff)
	 {
		 BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter(); 
		 if(onOff.equalsIgnoreCase("on"))
		 {		
			 Toast t = Toast.makeText(main, "switching on bluetooth", Toast.LENGTH_LONG);
			 t.show();
			 adapter.enable();
			 			 
		 }else{
			 Toast t = Toast.makeText(main, "switching off bluetooth", Toast.LENGTH_LONG);
			 t.show();
			 adapter.disable();
			 			 
		 }
		 
	 }
	 
	 public void launchUserApp(String name,boolean isActivity){
		 
		 GetApps ga = new GetApps(main,name);
		// ga.launchApp(name); 
		 ga.execute(new String[]{});
		
		 print_data = GlobalObjects.bot_responce;
		 print_data = print_data.replace("<oob><launch>"+ name +"</launch></oob>", "");

		 print_data = print_data.replaceAll("\\\"", "'");
		 if(isActivity){
			 wvi.printDataAsServer(print_data);
		 }
		 else{
			 
			 wgi.printData(R.id.textView1,print_data);
		 }
			
	 }
	
	 

}
