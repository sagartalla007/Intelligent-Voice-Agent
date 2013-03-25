package com.example.iva.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.widget.EditText;

public class DialogActivity extends Activity {

	String request = "";
	public ResultReceiver rec;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		rec =  getIntent().getParcelableExtra("receiverTag");
		
		final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
		.setMessage("TYPE PROMPT")
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            request = input.getText().toString();
	            Intent intent = new Intent(getBaseContext(), MainService.class);
	            intent.putExtra("receiverTag", rec);
	            intent.putExtra("request", request);
	            startService(intent);           
	            finish();
	                        
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	finish();
	        }
	    }).show();
		
		/*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    	intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
    	startActivityForResult(intent, 1234);*/
        
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1234 && resultCode == RESULT_OK) {
	    ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	    
	    Intent intent = new Intent(getBaseContext(), MainService.class);
        intent.putExtra("receiverTag", rec);
        intent.putExtra("request", matches.get(0));
        startService(intent);
	    
	    }
	       
	    }

}
