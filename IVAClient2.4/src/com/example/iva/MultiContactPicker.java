package com.example.iva;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.R.bool;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MultiContactPicker extends  ListActivity implements OnClickListener  {

	// List variables
    public String[] Contacts = {};
    public int[] to = {};
    public ListView myListView;
    //boolean running  = false;

    
    
    Button save_button,done_button;

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_multi_contact_picker);
        
        
        // Initializing the buttons according to their ID
        save_button = (Button)findViewById(R.id.save_group_button);

        // Defines listeners for the buttons
        save_button.setOnClickListener(this);

        Cursor mCursor = getContacts();
        startManagingCursor(mCursor);

        ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, mCursor,
                                                      Contacts = new String[] {ContactsContract.Contacts.DISPLAY_NAME },
                                                      to = new int[] { android.R.id.text1 });
        setListAdapter(adapter);
        myListView = getListView();
        myListView.setItemsCanFocus(false);
        myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        done_button = (Button)findViewById(R.id.done_button);
        done_button.setOnClickListener(this);
        
        

    } 

    @SuppressWarnings("deprecation")
	private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Contacts._ID,
                                        ContactsContract.Contacts.DISPLAY_NAME};
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '"
                + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_multi_contact_picker, menu);
		return true;
	}

	
	
	public void onClick(View view) {
        
    	if(view.getId()==R.id.save_group_button)
    	{
    		
    		
    		long[] ids = myListView.getCheckedItemIds();
    		
    		    		
    		for(long id : ids)
            {            	
                Cursor contact = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id + "" }, null);
                contact.moveToFirst();
                String name = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String no = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                
               Log.i("CONTACT INFO",name+"  "+no);
               
                // Do whatever you want with the data
              UploadContact up = new UploadContact();
              try {
				up.execute(new String[]{name,no.replace("-", "")}).get();
              } catch (InterruptedException e) {
            	  e.printStackTrace();
              } catch (ExecutionException e) {
            	  e.printStackTrace();
              }
                        
            }
    	  
    	}
    	
    	if(view.getId()==R.id.done_button)
    	{      		
			finish();
    	}
    	
    	
    }

	private class UploadContact extends AsyncTask<String, Void, String>{

		
		private ProgressDialog pdia;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdia = new ProgressDialog(MultiContactPicker.this);
            pdia.setMessage("Loading...");
            pdia.show(); 
			 
                   
		}
		
		@Override
		protected String doInBackground(String... urls) {
			
			String param = null,param2 = null;
			
			try {
				param = URLEncoder.encode(urls[0]+" s number is "+urls[1] , "UTF-8");
				
				param2 = URLEncoder.encode("call "+urls[0], "UTF-8");
				
				Log.i("PARAM",param+" "+param2);
				
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String response = "",check=""; 
	        HttpGet httpGet;
			try {
					
				
				httpGet = new HttpGet("http://ivaserver.intelligent-voice-agent.cloudbees.net/MainBotChat?input="+param+"&userId="+GlobalObjects.userId);
			    HttpResponse execute = GlobalObjects.getThreadSafeClient().execute(httpGet);
	     			
				InputStream content = execute.getEntity().getContent();
	            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	            String s = "";
	            while ((s = buffer.readLine()) != null) {
	                            response += s;
	                       }
	            
	            Log.i("contact Load Message", response);
	            
	            /*httpGet = new HttpGet("http://ivaserver.intelligent-voice-agent.cloudbees.net/MainBotChat?input="+param2+"&userId="+GlobalObjects.userId);
			    execute = GlobalObjects.getThreadSafeClient().execute(httpGet);
	     			
				content = execute.getEntity().getContent();
	            buffer = new BufferedReader(new InputStreamReader(content));
	            s = "";
	            while ((s = buffer.readLine()) != null) {
	                            check += s;
	                       }
	            
	            Log.i("contact CHECK", check);*/
	            
				}			
			catch(Exception e) {
				e.printStackTrace();
				}
			finally {
			    
			}
	    
			return response;
		
	}
		@Override
		protected void onPostExecute(String result){
		   super.onPostExecute(result);
		       pdia.dismiss(); 
		   
		}
	
}
}