 package com.sagar.iva;
 
 import java.io.BufferedReader;
 import java.io.BufferedWriter;
 import java.io.DataInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileWriter;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.util.HashSet;
 
 public class AIMLSet extends HashSet<String>
 {
   public String setName;
   int maxLength = 0;
   String host;
   String botid;
   boolean isExternal = false;
 
   public AIMLSet(String name)
   {
     this.setName = name;
     if (this.setName.equals(MagicStrings.natural_number_set_name)) this.maxLength = 1;
   }
 
   public boolean contains(String s)
   {
     if ((this.isExternal) && (MagicBooleans.enable_external_sets)) {
       String[] split = s.split(" ");
       if (split.length > this.maxLength) return false;
       String query = MagicStrings.set_member_string + this.setName.toUpperCase() + " " + s;
       String response = Sraix.sraix(null, query, "false", null, this.host, this.botid, null, "0");
       System.out.println("External " + this.setName + " contains " + s + "? " + response);
       if (response.equals("true")) return true;
       return false;
     }if (this.setName.equals(MagicStrings.natural_number_set_name)) {
       try {
         Integer.parseInt(s);
         return true;
       } catch (Exception ex) {
         return false;
       }
     }
     return super.contains(s);
   }
   public void writeAIMLSet() {
     System.out.println("Writing AIML Set " + this.setName);
     try
     {
       FileWriter fstream = new FileWriter(MagicStrings.sets_path + "/" + this.setName + ".txt");
       BufferedWriter out = new BufferedWriter(fstream);
       for (String p : this)
       {
         out.write(p.trim());
         out.newLine();
       }
 
       out.close();
     } catch (Exception e) {
       System.err.println("Error: " + e.getMessage());
     }
   }
 
   public int readAIMLSetFromInputStream(InputStream in, Bot bot) { BufferedReader br = new BufferedReader(new InputStreamReader(in));
 
     int cnt = 0;
     try
     {
       String strLine;
       while (((strLine = br.readLine()) != null) && (strLine.length() > 0)) {
         cnt++;
 
         if (strLine.startsWith("external")) {
           String[] splitLine = strLine.split(":");
           if (splitLine.length >= 4) {
             this.host = splitLine[1];
             this.botid = splitLine[2];
             this.maxLength = Integer.parseInt(splitLine[3]);
             this.isExternal = true;
             System.out.println("Created external set at " + this.host + " " + this.botid);
           }
         }
         else {
           strLine = strLine.toUpperCase().trim();
           String[] splitLine = strLine.split(" ");
           int length = splitLine.length;
           if (length > this.maxLength) this.maxLength = length;
 
           add(strLine.trim());
         }
       }
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
    }
     return cnt; }
 
   public void readAIMLSet(Bot bot)
   {
     System.out.println("Reading AIML Set " + MagicStrings.sets_path + "/" + this.setName + ".txt");
     try
     {
       File file = new File(MagicStrings.sets_path + "/" + this.setName + ".txt");
       if (file.exists()) {
         FileInputStream fstream = new FileInputStream(MagicStrings.sets_path + "/" + this.setName + ".txt");
 
         InputStream in = new DataInputStream(fstream);
         readAIMLSetFromInputStream(in, bot);
         in.close();
       } else {
         System.out.println(MagicStrings.sets_path + "/" + this.setName + ".txt not found");
       }
     } catch (Exception e) { System.err.println("Error: " + e.getMessage()); }
 
   }
 }
