package com.example.iva;

import java.util.ArrayList;

public class MyArrayList extends ArrayList<String> {
	
	public MyArrayList() {
		
	}
	
	MyArrayList(ArrayList<String> l)
	{
		super (l);
	}
	
	@Override
	public boolean add(String object) {
		
		 if (this.size() < 25) {
	          return super.add(object);
	      }else{
	    	  this.remove(0);
	    	return this.add(object);
	      }
	      
	}
}
