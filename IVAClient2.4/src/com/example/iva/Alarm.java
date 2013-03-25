package com.example.iva;

import java.sql.Date;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("Wakelock")
public class Alarm extends BroadcastReceiver 
{    
		
     
	@Override
     public void onReceive(Context context, Intent intent) 
     {   
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
         wl.acquire();

         Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show();
         

         wl.release();
     }

     @SuppressWarnings("deprecation")
	public void SetAlarm(Context context,Calendar c,String message)
     {
         
    	 AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         Intent i = new Intent(context, Alarm.class);
         PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        
         Date d =new Date(c.getTimeInMillis());         
         Log.i("before adding", d.toGMTString());
         
         
         c.add(Calendar.DATE, 1);
         c.add(Calendar.HOUR, -19);
         c.add(Calendar.MINUTE, 33);
         
         if(!message.equalsIgnoreCase("wake up"))
         {
        	 c.add(Calendar.DATE, 1);
             c.add(Calendar.HOUR, -19);
             c.add(Calendar.MINUTE, 33);        	 
         }
         d= new Date(c.getTimeInMillis());
         Log.i("after adding", d.toGMTString());
         
         
         
         am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                
         Toast.makeText(context, "alarm set at "+c.getTime().toGMTString(), Toast.LENGTH_LONG).show();
         
     }

     public void CancelAlarm(Context context)
     {
         Intent intent = new Intent(context, Alarm.class);
         PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(sender);
     }

	
}