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
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
 public class PreProcessor
 {
   public int normalCount = 0;
   public int personCount = 0;
   public int person2Count = 0;
   public int genderCount = 0;
   public String[] normalSubs = new String[MagicNumbers.max_substitutions];
   public Pattern[] normalPatterns = new Pattern[MagicNumbers.max_substitutions];
   public String[] personSubs = new String[MagicNumbers.max_substitutions];
   public Pattern[] personPatterns = new Pattern[MagicNumbers.max_substitutions];
   public String[] person2Subs = new String[MagicNumbers.max_substitutions];
   public Pattern[] person2Patterns = new Pattern[MagicNumbers.max_substitutions];
   public String[] genderSubs = new String[MagicNumbers.max_substitutions];
   public Pattern[] genderPatterns = new Pattern[MagicNumbers.max_substitutions];
 
   public PreProcessor(Bot bot) { this.normalCount = readSubstitutions(MagicStrings.config_path + "/normal.txt", this.normalPatterns, this.normalSubs);
     this.personCount = readSubstitutions(MagicStrings.config_path + "/person.txt", this.personPatterns, this.personSubs);
     this.person2Count = readSubstitutions(MagicStrings.config_path + "/person2.txt", this.person2Patterns, this.person2Subs);
     this.genderCount = readSubstitutions(MagicStrings.config_path + "/gender.txt", this.genderPatterns, this.genderSubs);
     System.out.println("Preprocessor: " + this.normalCount + " norms " + this.personCount + " persons " + this.person2Count + " person2 "); }
 
   public String normalize(String request)
   {
     return substitute(request, this.normalPatterns, this.normalSubs, this.normalCount);
   }
   public String person(String input) {
     return substitute(input, this.personPatterns, this.personSubs, this.personCount);
   }
 
   public String person2(String input) {
     return substitute(input, this.person2Patterns, this.person2Subs, this.person2Count);
   }
 
   public String gender(String input) {
     return substitute(input, this.genderPatterns, this.genderSubs, this.genderCount);
   }
 
   String substitute(String request, Pattern[] patterns, String[] subs, int count)
   {
     String result = " " + request + " ";
     try {
       for (int i = 0; i < count; i++)
       {
         String replacement = subs[i];
         Pattern p = patterns[i];
         Matcher m = p.matcher(result);
 
         if (m.find())
         {
           result = m.replaceAll(replacement);
         }
       }
       while (result.contains("  ")) result = result.replace("  ", " ");
       result = result.trim();
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
     return result.trim();
   }
 
   public int readSubstitutionsFromInputStream(InputStream in, Pattern[] patterns, String[] subs) {
     BufferedReader br = new BufferedReader(new InputStreamReader(in));
 
     int subCount = 0;
     try
     {
       String strLine;
       while ((strLine = br.readLine()) != null)
       {
         strLine = strLine.trim();
         Pattern pattern = Pattern.compile("\"(.*?)\",\"(.*?)\"", 32);
         Matcher matcher = pattern.matcher(strLine);
         if ((matcher.find()) && (subCount < MagicNumbers.max_substitutions)) {
           subs[subCount] = matcher.group(2);
           patterns[subCount] = Pattern.compile(Pattern.quote(matcher.group(1)), 2);
           subCount++;
         }
       }
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
     return subCount;
   }
   int readSubstitutions(String filename, Pattern[] patterns, String[] subs) {
     int subCount = 0;
     try
     {
       File file = new File(filename);
       if (file.exists()) {
         FileInputStream fstream = new FileInputStream(filename);
 
         InputStream in = new DataInputStream(fstream);
         subCount = readSubstitutionsFromInputStream(in, patterns, subs);
 
         in.close();
       }
     } catch (Exception e) {
       System.err.println("Error: " + e.getMessage());
     }
     return subCount;
   }
   public String[] sentenceSplit(String line) {
     line = line.replace("。", ".");
     line = line.replace("？", "?");
     line = line.replace("！", "!");
 
     String[] result = line.split("[\\.!\\?]");
     for (int i = 0; i < result.length; i++) result[i] = result[i].trim();
     return result;
   }
   public void normalizeFile(String infile, String outfile) {
     try {
       BufferedWriter bw = null;
       FileInputStream fstream = new FileInputStream(infile);
       DataInputStream in = new DataInputStream(fstream);
       BufferedReader br = new BufferedReader(new InputStreamReader(in));
       bw = new BufferedWriter(new FileWriter(outfile));
       String strLine;
       while ((strLine = br.readLine()) != null) {
         strLine = normalize(strLine).toUpperCase();
         bw.write(strLine);
         bw.newLine();
       }
       bw.close();
       br.close();
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }
   }
 }

