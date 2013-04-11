package com.example.iva;

import java.util.ArrayList;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import android.util.Log;

public class JsonIVA {
	   
	
  public String toJson(ArrayList<String> listData)
   {
	 String jsonText = JSONValue.toJSONString(listData);
	  //Log.i("JSON DATA", jsonText);
	   return jsonText;
   }
  
  @SuppressWarnings("unchecked")
public MyArrayList toList(String jsonString)
  {
	  JSONParser parser = new JSONParser();
		
	  MyArrayList l = null;
		  try{
			   ArrayList<String> al = (ArrayList<String>) parser.parse(jsonString);
			   l=new MyArrayList(al); 
		  }
		  catch(Exception pe){
			    System.out.println(pe);
			  }   
	     //Log.i("LIST DATA", l.toString());
		  return l;
  }
}
