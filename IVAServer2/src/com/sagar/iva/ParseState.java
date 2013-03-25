 package com.sagar.iva;
 
 public class ParseState
 {
   public Nodemapper leaf;
   public String input;
   public String that;
   public String topic;
   public Chat chatSession;
   public int depth;
   public Predicates vars;
 
   public ParseState(int depth, Chat chatSession, String input, String that, String topic, Nodemapper leaf)
   {
     this.chatSession = chatSession;
     this.input = input;
     this.that = that;
     this.topic = topic;
     this.leaf = leaf;
     this.depth = depth;
     this.vars = new Predicates();
   }
 }

