 package com.sagar.iva;
 
 import java.io.BufferedReader;
 import java.io.DataInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.HashSet;
 
 public class Utilities
 {
   public static String tagTrim(String xmlExpression, String tagName)
   {
     String stag = "<" + tagName + ">";
     String etag = "</" + tagName + ">";
     if (xmlExpression.length() >= (stag + etag).length()) {
       xmlExpression = xmlExpression.substring(stag.length());
       xmlExpression = xmlExpression.substring(0, xmlExpression.length() - etag.length());
     }
     return xmlExpression;
   }
   public static HashSet<String> stringSet(String[] strings) {
     HashSet set = new HashSet();
     for (String s : strings) set.add(s);
     return set;
   }
   public static String getFileFromInputStream(InputStream in) {
     BufferedReader br = new BufferedReader(new InputStreamReader(in));
 
     String contents = "";
     try
     {
       String strLine;
       while ((strLine = br.readLine()) != null)
         if (strLine.length() == 0) contents = contents + "\n"; else
           contents = contents + strLine + "\n";
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
     return contents.trim();
   }
   public static String getFile(String filename) {
     String contents = "";
     try {
       File file = new File(filename);
       if (file.exists())
       {
         FileInputStream fstream = new FileInputStream(filename);
 
         InputStream in = new DataInputStream(fstream);
         contents = getFileFromInputStream(in);
         in.close();
       }
     } catch (Exception e) {
       System.err.println("Error: " + e.getMessage());
     }
 
     return contents;
   }
   public static String getCopyrightFromInputStream(InputStream in) {
     BufferedReader br = new BufferedReader(new InputStreamReader(in));
 
     String copyright = "";
     try
     {
       String strLine;
       while ((strLine = br.readLine()) != null)
         if (strLine.length() == 0) copyright = copyright + "\n"; else
           copyright = copyright + "<!-- " + strLine + " -->\n";
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
     return copyright;
   }
   public static String getCopyright(Bot bot, String AIMLFilename) {
     String copyright = "";
     Calendar cal = Calendar.getInstance();
     int year = cal.get(1);
     SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
     dateFormat.setCalendar(cal);
     String date = dateFormat.format(cal.getTime());
     try {
       copyright = getFile(MagicStrings.config_path + "/copyright.txt");
       String[] splitCopyright = copyright.split("\n");
       copyright = "";
       for (int i = 0; i < splitCopyright.length; i++) {
         copyright = copyright + "<!-- " + splitCopyright[i] + " -->\n";
       }
       copyright = copyright.replace("[url]", bot.properties.get("url"));
       copyright = copyright.replace("[date]", date);
       copyright = copyright.replace("[YYYY]", String.valueOf(cal.get(1)));
       copyright = copyright.replace("[version]", bot.properties.get("version"));
       copyright = copyright.replace("[botname]", bot.name.toUpperCase());
       copyright = copyright.replace("[filename]", AIMLFilename);
       copyright = copyright.replace("[botmaster]", bot.properties.get("botmaster"));
       copyright = copyright.replace("[organization]", bot.properties.get("organization"));
     } catch (Exception e) {
       System.err.println("Error: " + e.getMessage());
     }
 
     return copyright;
   }
 
   public static String getPannousAPIKey() {
     String apiKey = getFile(MagicStrings.config_path + "/pannous-apikey.txt");
     if (apiKey.equals("")) apiKey = MagicStrings.pannous_api_key;
     return apiKey;
   }
   public static String getPannousLogin() {
     String login = getFile(MagicStrings.config_path + "/pannous-login.txt");
     if (login.equals("")) login = MagicStrings.pannous_login;
     return login;
   }
 
   public static boolean isCharCJK(char c)
   {
     if ((Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT) || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) || (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS))
     {
       return true;
     }
     return false;
   }
 }

