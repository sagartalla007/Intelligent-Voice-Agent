package com.example.iva;

import java.util.ArrayList;

import android.util.Log;

public class MyArrayList extends ArrayList<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyArrayList() {
		
	}
	
	MyArrayList(ArrayList<String> l)
	{
		super (l);
	}
	
	@Override
	public boolean add(String object) {
				 
		 boolean ret = true;
		 
		 if (this.size() < 25) {
	          ret = super.add(object);
	      }else{
	    	  this.remove(0);
	    	ret = this.add(object);
	      }
	     
		 String data = this.toString();
		 Log.i("CURRENTLISTDATA", data);
		 		 
		 return ret;
	}
}
