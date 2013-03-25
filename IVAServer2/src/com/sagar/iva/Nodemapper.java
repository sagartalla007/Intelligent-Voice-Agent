 package com.sagar.iva;
 
 import java.util.ArrayList;
 import java.util.HashMap;
 
 public class Nodemapper
 {
   public Category category = null;
   public int height = MagicNumbers.max_graph_height;
   public StarBindings starBindings = null;
   public HashMap<String, Nodemapper> map = null;
   public String key = null;
   public Nodemapper value = null;
   public boolean shortCut = false;
   public ArrayList<String> sets;
 }

