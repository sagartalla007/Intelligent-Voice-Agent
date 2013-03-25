 package com.sagar.iva;
 
 import java.io.PrintStream;
 import java.util.ArrayList;
 
 public class Path extends ArrayList<String>
 {
   public String word;
   public Path next;
   public int length;
 
   private Path()
   {
     this.next = null;
     this.word = null;
     this.length = 0;
   }
 
   public static Path sentenceToPath(String sentence) {
     sentence = sentence.trim();
     return arrayToPath(sentence.split(" "));
   }
   public static String pathToSentence(Path path) {
     if (path == null) return "";
     return path.word + " " + pathToSentence(path.next);
   }
   private static Path arrayToPath(String[] array) {
     return arrayToPath(array, 0);
   }
   private static Path arrayToPath(String[] array, int index) {
     if (index >= array.length) return null;
 
     Path newPath = new Path();
     newPath.word = array[index];
     newPath.next = arrayToPath(array, index + 1);
     if (newPath.next == null) newPath.length = 1; else
       newPath.length = (newPath.next.length + 1);
     return newPath;
   }
 
   public void print() {
     String result = "";
     for (Path p = this; p != null; p = p.next) {
       result = result + p.word + ",";
     }
     if (result.endsWith(",")) result = result.substring(0, result.length() - 1);
     System.out.println(result);
   }
 }
