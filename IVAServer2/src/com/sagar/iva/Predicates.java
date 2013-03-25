 package com.sagar.iva;
 
 import java.io.BufferedReader;
 import java.io.DataInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.util.HashMap;
 
 public class Predicates extends HashMap<String, String>
 {
   public String put(String key, String value)
   {
     if (MagicBooleans.trace_mode) System.out.println("Setting predicate " + key + " to " + value);
     return (String)super.put(key, value);
   }
   public String get(String key) {
     String result = (String)super.get(key);
     if (result == null) return MagicStrings.unknown_predicate_value;
     return result;
   }
   public void getPredicateDefaultsFromInputStream(InputStream in) {
     BufferedReader br = new BufferedReader(new InputStreamReader(in));
     try
     {
       String strLine;
       while ((strLine = br.readLine()) != null)
         if (strLine.contains(":")) {
           String property = strLine.substring(0, strLine.indexOf(":"));
           String value = strLine.substring(strLine.indexOf(":") + 1);
           put(property, value);
         }
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
   }
 
   public void getPredicateDefaults(String filename)
   {
     try {
       File file = new File(filename);
       if (file.exists()) {
         FileInputStream fstream = new FileInputStream(filename);
 
         InputStream in = new DataInputStream(fstream);
         getPredicateDefaultsFromInputStream(in);
         in.close();
       }
     } catch (Exception e) {
       System.err.println("Error: " + e.getMessage());
     }
   }
 }

