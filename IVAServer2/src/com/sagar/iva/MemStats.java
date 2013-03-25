 package com.sagar.iva;
 
 import java.io.PrintStream;
 
 public class MemStats
 {
   public static long prevHeapSize = 0L;
 
   public static void memStats() {
     long heapSize = Runtime.getRuntime().totalMemory();
 
     long heapMaxSize = Runtime.getRuntime().maxMemory();
 
     long heapFreeSize = Runtime.getRuntime().freeMemory();
     long diff = heapSize - prevHeapSize;
     prevHeapSize = heapSize;
     System.out.println("Heap " + heapSize + " MaxSize " + heapMaxSize + " Free " + heapFreeSize + " Diff " + diff);
   }
 }

