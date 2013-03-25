 package com.sagar.iva;
 
 import java.io.PrintStream;
 import java.util.HashSet;
 import java.util.Set;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import net.reduls.sanmoku.Morpheme;
 import net.reduls.sanmoku.Tagger;
 
 public class JapaneseTokenizer
 {
   static final Pattern tagPattern = Pattern.compile("(<.*>.*</.*>)|(<.*/>)");
   static Set<Character.UnicodeBlock> japaneseUnicodeBlocks = new HashSet() { } ;
 
   public static void test()
   {
     String mixed = "This is a Japanese newspaper headline: ãƒ©ãƒ‰ã‚¯ãƒªãƒ•ã€�ãƒžãƒ©ã‚½ãƒ³äº”è¼ªä»£è¡¨ã�«1ä¸‡må‡ºå ´ã�«ã‚‚å�«ã�¿";
 
     for (char c : mixed.toCharArray())
       if (japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(c)))
         System.out.println(c + " is a Japanese character");
       else
         System.out.println(c + " is not a Japanese character");
   }
 
   public static String buildFragment(String fragment)
   {
     String result = "";
     for (Morpheme e : Tagger.parse(fragment)) {
       result = result + e.surface + " ";
     }
 
     return result.trim();
   }
   public static String morphSentence(String sentence) {
     String result = "";
     Matcher matcher = tagPattern.matcher(sentence);
     while (matcher.find()) {
       int i = matcher.start();
       int j = matcher.end();
       String prefix;
    
       if (i > 0) prefix = sentence.substring(0, i - 1); else prefix = "";
       String tag = sentence.substring(i, j);
       result = result + " " + buildFragment(prefix) + " " + tag;
       if (j < sentence.length()) sentence = sentence.substring(j, sentence.length()); else sentence = "";
 
     }
 
     result = result + " " + buildFragment(sentence);
     while (result.contains("$ ")) result = result.replace("$ ", "$");
     while (result.contains("  ")) result = result.replace("  ", " ");
     return result.trim();
   }
 }

