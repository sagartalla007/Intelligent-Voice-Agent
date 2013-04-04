package com.example.iva;

import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;

import android.app.Activity;
import android.util.Log;

public class LauncherProcessor {
	public enum actionEn {dial,search,sms,url,alarm,battery,map,directions,gps,wifi,camera,bluetooth,launch}
	private MainActivity main;
	public boolean isActivity;
	
	public LauncherProcessor(boolean isActivity) {
		this.main = GlobalObjects.mainActivity;
				this.isActivity = isActivity;
	}
	
	public void process(String oobData)
	{
		
		
		try {
			
			Pattern OOB = Pattern.compile("<oob>(.+?)</oob>");
			 final Matcher matcher = OOB.matcher(oobData);
			 if(matcher.find())
			 {
				 Log.i("OOB", matcher.group(1));
			 }

			String str = matcher.group(1);
		
			str = str.replace('&', ';');
			
			Element action =  DocumentBuilderFactory
				    .newInstance()
				    .newDocumentBuilder()
				    .parse(new ByteArrayInputStream(str.getBytes()))
				    .getDocumentElement();
			
			String actionStr=action.getNodeName();
			actionEn launchAc = actionEn.valueOf(actionStr);
			
			
			OOBProcessor oobp = new OOBProcessor();
			
			switch(launchAc)
			{
			
			 case battery:
			 {
				 oobp.batteryLevel(oobData, isActivity);
			  	
			 } 
			 break;
			 
			 case search :
			 {
				oobp.searchWeb(action.getTextContent(),isActivity);
			 }
			 break;
			 
			 case url :
			 {
				 oobp.gotoUrl(action.getTextContent(),isActivity);				 
			 }
			 break;
			 
			 case dial:
			 {
				oobp.openDialer(action.getTextContent(),isActivity); 
			 }	 
			 break;
			 
			 case alarm :
			 {
				 				
				 
				 String message = action.getElementsByTagName("message").item(0).getTextContent();
				 String year = action.getElementsByTagName("year").item(0).getTextContent();
				 String month = action.getElementsByTagName("month").item(0).getTextContent();
				 String day = action.getElementsByTagName("day").item(0).getTextContent();
				 String hour = action.getElementsByTagName("hour").item(0).getTextContent();
				 String minute = action.getElementsByTagName("minute").item(0).getTextContent();
				 String duration = action.getElementsByTagName("duration").item(0).getTextContent();
				 
				 oobp.setAlarm(message, year, month, day, hour, minute, duration,isActivity);
				 
				   
			 }
			 break;
			 
			 case sms :
			 {
				 String recipient = action.getElementsByTagName("recipient").item(0).getTextContent();
				 String message = action.getElementsByTagName("message").item(0).getTextContent();
				 oobp.openSMS(recipient,message,isActivity); 
			 }
			 break;
			 
			 case map:
			 {
				 oobp.gotoMAP(action.getTextContent(),isActivity);				 
			 }
			 break;
			 
			 case directions:
			 {
				 String from = null;
				if(oobData.contains("<from>")) 
					from = action.getElementsByTagName("from").item(0).getTextContent();
				 String to = action.getElementsByTagName("to").item(0).getTextContent();
				 oobp.showDirections(from,to,isActivity); 
				 
			 }
			 break;
			 
			 case gps:
			 {
				 oobp.gpsToggle(action.getTextContent());	
			 }
			 break;
			 
			 case wifi:
			{
				oobp.wifiToggle(action.getTextContent());				
			}
			break;
			
			 case camera:
			 {
				 //Log.i("camera Launcher", "function call");
				 oobp.launchCamera();
			 }
			 break;
			 
			 case bluetooth:
			 {
				 //Log.i("bluetooth toggle", action.getTextContent());
				 oobp.bluetoothToggle(action.getTextContent());
				 
			 }break;
			 case launch:
			 {
				 
				 oobp.launchUserApp(action.getTextContent(),isActivity);
			 }
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
	}
}
