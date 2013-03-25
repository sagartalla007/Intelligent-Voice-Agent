 package com.sagar.iva;
 
 import java.io.BufferedReader;
 import java.io.DataInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.util.HashMap;
 
 public class AIMLMap extends HashMap<String, String>
 {
   public String mapName;
   String host;
   String botid;
   boolean isExternal = false;
 
   public AIMLMap(String name)
   {
     this.mapName = name;
   }
 
   public String get(String key)
   {
     if (this.mapName.equals(MagicStrings.map_successor)) {
       try {
         int number = Integer.parseInt(key);
         return String.valueOf(number + 1);
       } catch (Exception ex) {
    	   return MagicStrings.unknown_map_value;
       }
     }
     if (this.mapName.equals(MagicStrings.map_predecessor))
    	 try {
    		 int number = Integer.parseInt(key);
    		 return String.valueOf(number - 1);
    	 } catch (Exception ex) {
    		 return MagicStrings.unknown_map_value;
    	 }
     String value;
     
     if ((this.isExternal) && (MagicBooleans.enable_external_sets)) {
    	 String[] split = key.split(" ");
    	 String query = this.mapName.toUpperCase() + " " + key;
    	 String response = Sraix.sraix(null, query, MagicStrings.unknown_map_value, null, this.host, this.botid, null, "0");
    	 System.out.println("External " + this.mapName + "(" + key + ")=" + response);
    	 value = response;
     } else {
    	 value = (String)super.get(key);
     }if (value == null) value = MagicStrings.unknown_map_value;
     System.out.println("AIMLMap get " + key + "=" + value);
     return value;
   }
   
   public String put(String key, String value)
   {
	   return (String)super.put(key, value);
   }
   
   public int readAIMLMapFromInputStream(InputStream in, Bot bot)
   {
	   int cnt = 0;
	   BufferedReader br = new BufferedReader(new InputStreamReader(in));
	   try
	   {
		   String strLine;
		   while (((strLine = br.readLine()) != null) && (strLine.length() > 0)) {
			   String[] splitLine = strLine.split(":");
			   
			   if (splitLine.length >= 2) {
				   cnt++;
				   if (strLine.startsWith(MagicStrings.remote_map_key)) {
					   if (splitLine.length >= 3) {
						   this.host = splitLine[1];
						   this.botid = splitLine[2];
						   this.isExternal = true;
						   System.out.println("Created external map at " + this.host + " " + this.botid);
					   }
				   }
				   else {
					   String key = splitLine[0].toUpperCase();
					   String value = splitLine[1];
					   
					   put(key, value);
				   }
			   }
		   }	
	   } catch (Exception ex) {
		   ex.printStackTrace();
	   }
	   return cnt;
   }
   
   public void readAIMLMap(Bot bot)
   {
	   System.out.println("Reading AIML Map " + MagicStrings.maps_path + "/" + this.mapName + ".txt");
	   try
	   {
		   File file = new File(MagicStrings.maps_path + "/" + this.mapName + ".txt");
		   if (file.exists()) {
			   FileInputStream fstream = new FileInputStream(MagicStrings.maps_path + "/" + this.mapName + ".txt");
			   
			   InputStream in = new DataInputStream(fstream);
			   readAIMLMapFromInputStream(in, bot);
			   
			   in.close();
		   } else {
			   System.out.println(MagicStrings.maps_path + "/" + this.mapName + ".txt not found");
		   }
	   } catch (Exception e) { System.err.println("Error: " + e.getMessage()); }
	   
   }
 }
 
