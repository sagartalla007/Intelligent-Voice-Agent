package com.example.iva;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PrefsActivity extends PreferenceActivity{
	
static final int BOT_PIC = 1;
static final int YOUR_PIC = 2;
static final int ADD_CONTACT = 3;

static String userimg="images/photo.jpg";
static String botimg="images/photo.jpg";
static String clientname = "CLIENT";
static String theme = "theme1";

@SuppressWarnings("deprecation")
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.prefs);
   
   ListView v = getListView();
   
   Button b = new Button(this);
   b.setText("SAVE");
   b.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		
		returnToCaller();
		
	}
  });
     
   
   v.addFooterView(b);
   
   
   
    Preference prefereces1=findPreference("your_pic");
   prefereces1.setOnPreferenceClickListener (new Preference.OnPreferenceClickListener(){
       public boolean onPreferenceClick(Preference preference){
           Intent intent = new Intent();
           intent.setType("image/*");
           intent.setAction(Intent.ACTION_GET_CONTENT);
           
           startActivityForResult(Intent.createChooser(intent, "Select Picture"), YOUR_PIC);
           return true;
       }
   });
   
   
   final Preference prefereces2=findPreference("your_name");
   prefereces2.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
	
		String name = (String) newValue;
		Log.i("your name", name);
		clientname = name;
		return false;
	}
});
   
   Preference prefereces3=findPreference("bot_pic");
   prefereces3.setOnPreferenceClickListener (new Preference.OnPreferenceClickListener(){
       public boolean onPreferenceClick(Preference preference){
           Intent intent = new Intent();
           intent.setType("image/*");
           intent.setAction(Intent.ACTION_GET_CONTENT);
           
           startActivityForResult(Intent.createChooser(intent, "Select Picture"), BOT_PIC);
           return true;
       }
   });
   
   
   final Preference prefereces4=findPreference("themepref");
   prefereces4.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
	
		
		String selecttheme = (String)newValue;
		
		Log.i("THEME SELECTED", theme);
		
		theme = selecttheme;
		
		return false;
	}
});
   
   final Preference prefereces5=findPreference("choose_contacts");
   prefereces5.setOnPreferenceClickListener (new Preference.OnPreferenceClickListener(){
       public boolean onPreferenceClick(Preference preference){
           Intent intent = new Intent(getBaseContext(), MultiContactPicker.class);     
           startActivity(intent);
           return true;
       }
   });
}

protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
   
    
    if(resultCode == RESULT_OK){  
    	
    	switch(requestCode)
    	{
    	
    	case YOUR_PIC: 
    	
    	case BOT_PIC:
    	{
		 Uri selectedImage = imageReturnedIntent.getData();
		 String[] filePathColumn = {MediaStore.Images.Media.DATA};
		 String path="";
		 try{
		 Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		 cursor.moveToFirst();
		 
		 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		 String filePath = cursor.getString(columnIndex);
		 cursor.close();
		 
		 path = "file://"+filePath.substring(4);
		 }
		 catch (Exception e) {
               e.printStackTrace();
		 }
		 
		 Log.i("path to image", path);		 
		 
		 if(path.endsWith(".jpg")||path.endsWith(".png")||path.endsWith(".gif"))
		 	{ 
			 	if(requestCode == YOUR_PIC) 
			 		{
			 			userimg = path;
			 		    Log.i("userimg", userimg);
			 		}
			 	    
			 	  
			 	else if(requestCode == BOT_PIC)
			 		{
			 		 	botimg = path;
			 		 	Log.i("userimg", userimg);
			 		}
		 	}
		 else{
				
				Toast.makeText(this, "select an image with jpg or png or gif extension", Toast.LENGTH_LONG).show();
				
			}
		}
    	
    	break;
    	   	
    	}
    	
    }
    
} 

	public void returnToCaller()
		{
			Intent intent=new Intent();
			intent.putExtra("userpath", userimg);
			intent.putExtra("botpath", botimg);
			intent.putExtra("clientname", clientname);
			intent.putExtra("selecttheme", theme);
			setResult(RESULT_OK, intent);
			finish();

		}

   
}