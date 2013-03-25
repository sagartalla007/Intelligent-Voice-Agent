 package com.sagar.iva;
 
 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.net.InetAddress;
 import java.net.NetworkInterface;
 import java.net.SocketException;
 import java.net.URI;
 import java.net.URLEncoder;
 import java.util.Calendar;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.apache.http.HttpEntity;
 import org.apache.http.HttpResponse;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.methods.HttpGet;
 import org.apache.http.impl.client.DefaultHttpClient;
 import org.json.JSONArray;
 import org.json.JSONObject;
 
 public class Sraix
 {
   public static HashMap<String, String> custIdMap = new HashMap();
 
   private static String custid = "0";
 
   public static String localIPAddress()
   {
     try
     {
       for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
         NetworkInterface intf = (NetworkInterface)en.nextElement();
         for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
           InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
           if (!inetAddress.isLoopbackAddress()) {
             String ipAddress = inetAddress.getHostAddress().toString();
             int p = ipAddress.indexOf("%");
             if (p > 0) ipAddress = ipAddress.substring(0, p);
             if (MagicBooleans.trace_mode) System.out.println(new StringBuilder().append("--> localIPAddress = ").append(ipAddress).toString());
             return ipAddress;
           }
         }
       }
     }
     catch (SocketException ex)
     {
       
       ex.printStackTrace();
     }
     return "127.0.0.1";
   }
 
   public static String sraix(Chat chatSession, String input, String defaultResponse, String hint, String host, String botid, String apiKey, String limit)
   {
     String response;
    
     if ((host != null) && (botid != null))
       response = sraixPandorabots(input, chatSession, host, botid);
     else
       response = sraixPannous(input, hint, chatSession);
     if (response.equals("SRAIXFAILED")) {
       if ((chatSession != null) && (defaultResponse == null)) response = AIMLProcessor.respond("SRAIXFAILED", "nothing", "nothing", chatSession);
       else if (defaultResponse != null) response = defaultResponse;
     }
     return response;
   }
 
   public static String sraixPandorabots(String input, Chat chatSession, String host, String botid) {
     String defaultResponse = "SRAIXFAILED";
     HttpResponse httpResponse = pandorabotsRequest(input, host, botid);
     if (httpResponse == null) return defaultResponse;
     return pandorabotsResponse(httpResponse, chatSession, host, botid);
   }
   public static HttpResponse pandorabotsRequest(String input, String host, String botid) {
     String defaultResponse = "SRAIXFAILED";
     try {
       custid = "0";
       String key = new StringBuilder().append(host).append(":").append(botid).toString();
       if (custIdMap.containsKey(key)) custid = (String)custIdMap.get(key);
       String spec;
      
       if (custid.equals("0")) {
         spec = String.format("%s?botid=%s&input=%s", new Object[] { new StringBuilder().append("http://").append(host).append("/pandora/talk-xml").toString(), botid, URLEncoder.encode(input) });
       }
       else
       {
         spec = String.format("%s?botid=%s&custid=%s&input=%s", new Object[] { new StringBuilder().append("http://").append(host).append("/pandora/talk-xml").toString(), botid, custid, URLEncoder.encode(input) });
       }
 
       HttpClient client = new DefaultHttpClient();
       HttpGet request = new HttpGet();
       request.setURI(new URI(spec));
       return client.execute(request);
     }
     catch (Exception ex) {
       ex.printStackTrace();
     }return null;
   }
 
   public static String pandorabotsResponse(HttpResponse response, Chat chatSession, String host, String botid) {
     String defaultResponse = "SRIAXFAILED";
     String sraixResponse = "";
     try
     {
       BufferedReader inb = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
 
       StringBuilder sb = new StringBuilder("");
 
       String NL = System.getProperty("line.separator");
       String line;
       while ((line = inb.readLine()) != null) {
         sb.append(line).append(NL);
       }
       inb.close();
       sraixResponse = sb.toString();
       if (MagicBooleans.trace_mode) System.out.println(new StringBuilder().append("Page = ").append(sraixResponse).toString()); 
     }
     catch (Exception ex) { ex.printStackTrace(); }
 
     int n1 = sraixResponse.indexOf("<that>");
     int n2 = sraixResponse.indexOf("</that>");
     String botResponse = defaultResponse;
     if (n2 > n1)
       botResponse = sraixResponse.substring(n1 + "<that>".length(), n2);
     n1 = sraixResponse.indexOf("custid=");
     if (n1 > 0) {
       custid = sraixResponse.substring(n1 + "custid=\"".length(), sraixResponse.length());
       n2 = custid.indexOf("\"");
       if (n2 > 0) custid = custid.substring(0, n2); else
         custid = "0";
       String key = new StringBuilder().append(host).append(":").append(botid).toString();
 
       custIdMap.put(key, custid);
     }
     if (botResponse.endsWith(".")) botResponse = botResponse.substring(0, botResponse.length() - 1);
     return botResponse;
   }
 
   public static String sraixPannous(String input, String hint, Chat chatSession)
   {
     try {
       input = new StringBuilder().append(" ").append(input).append(" ").toString();
       input = input.replace(" point ", ".");
       input = input.replace(" rparen ", ")");
       input = input.replace(" lparen ", "(");
       input = input.replace(" slash ", "/");
       input = input.replace(" star ", "*");
       input = input.replace(" dash ", "-");
       input = input.trim();
       input = input.replace(" ", "+");
       Calendar cal = Calendar.getInstance();
       int offset = (cal.get(15) + cal.get(16)) / 60000;
 
       String locationString = "";
       if (Chat.locationKnown) {
         locationString = new StringBuilder().append("&location=").append(Chat.latitude).append(",").append(Chat.longitude).toString();
       }
 
       String url = new StringBuilder().append("https://weannie.pannous.com/api?input=").append(input).append("&locale=en_US&timeZone=").append(offset).append(locationString).append("&login=").append(MagicStrings.pannous_login).append("&ip=").append(localIPAddress()).append("&botid=0&key=").append(MagicStrings.pannous_api_key).append("&exclude=Dialogues,ChatBot&out=json").toString();
       if (MagicBooleans.trace_mode) System.out.println(new StringBuilder().append("Sraix url='").append(url).append("'").toString());
       HttpClient client = new DefaultHttpClient();
       HttpGet request = new HttpGet();
       request.setURI(new URI(url));
       HttpResponse response = client.execute(request);
       BufferedReader inb = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
 
       StringBuilder sb = new StringBuilder("");
 
       String NL = System.getProperty("line.separator");
       String line;
       while ((line = inb.readLine()) != null) {
         sb.append(line).append(NL);
       }
       inb.close();
       String page = sb.toString();
       if (MagicBooleans.trace_mode) System.out.println(new StringBuilder().append("Sraix: ").append(page).toString());
       String text = "";
       String imgRef = "";
       if ((page == null) || (page.length() == 0)) {
         text = "SRAIXFAILED";
       }
       else {
         JSONArray outputJson = new JSONObject(page).getJSONArray("output");
         if (outputJson.length() == 0) {
           text = "SRAIXFAILED";
         }
         else {
           JSONObject firstHandler = outputJson.getJSONObject(0);
           JSONObject actions = firstHandler.getJSONObject("actions");
           if (actions.has("reminder")) {
             Object obj = actions.get("reminder");
             if ((obj instanceof JSONObject)) {
               JSONObject sObj = (JSONObject)obj;
               String date = sObj.getString("date");
               date = date.substring(0, "2012-10-24T14:32".length());
 
               String duration = sObj.getString("duration");
 
               Pattern datePattern = Pattern.compile("(.*)-(.*)-(.*)T(.*):(.*)");
               Matcher m = datePattern.matcher(date);
               String year = ""; String month = ""; String day = ""; String hour = ""; String minute = "";
               if (m.matches()) {
                 year = m.group(1);
                 month = String.valueOf(Integer.parseInt(m.group(2)) - 1);
                 day = m.group(3);
 
                 hour = m.group(4);
                 minute = m.group(5);
                 text = new StringBuilder().append("<year>").append(year).append("</year>").append("<month>").append(month).append("</month>").append("<day>").append(day).append("</day>").append("<hour>").append(hour).append("</hour>").append("<minute>").append(minute).append("</minute>").append("<duration>").append(duration).append("</duration>").toString();
               }
               else
               {
                 text = MagicStrings.schedule_error;
               }
             }
           } else if (actions.has("say")) {
             Object obj = actions.get("say");
             if ((obj instanceof JSONObject)) {
               JSONObject sObj = (JSONObject)obj;
               text = sObj.getString("text");
               if (sObj.has("moreText")) {
                 JSONArray arr = sObj.getJSONArray("moreText");
                 for (int i = 0; i < arr.length(); i++)
                   text = new StringBuilder().append(text).append(" ").append(arr.getString(i)).toString();
               }
             }
             else {
               text = obj.toString();
             }
           }
           if ((actions.has("show")) && (!text.contains("Wolfram")) && (actions.getJSONObject("show").has("images")))
           {
             JSONArray arr = actions.getJSONObject("show").getJSONArray("images");
 
             int i = (int)(arr.length() * Math.random());
 
             imgRef = arr.getString(i);
             if (imgRef.startsWith("//")) imgRef = new StringBuilder().append("http:").append(imgRef).toString();
             imgRef = new StringBuilder().append("<a href=\"").append(imgRef).append("\"><img src=\"").append(imgRef).append("\"/></a>").toString();
           }
 
         }
 
         if ((hint != null) && (hint.equals("event")) && (!text.startsWith("<year>"))) return "SRAIXFAILED";
         if (text.equals("SRAIXFAILED")) return AIMLProcessor.respond("SRAIXFAILED", "nothing", "nothing", chatSession);
 
         text = text.replace("&#39;", "'");
         text = text.replace("&apos;", "'");
         text = text.replaceAll("\\[(.*)\\]", "");
 
         String[] sentences = text.split("\\. ");
 
         String clippedPage = sentences[0];
         for (int i = 1; i < sentences.length; i++) {
           if (clippedPage.length() < 500) clippedPage = new StringBuilder().append(clippedPage).append(". ").append(sentences[i]).toString();
 
         }
 
         return new StringBuilder().append(clippedPage).append(" ").append(imgRef).toString();
       }
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
       System.out.println(new StringBuilder().append("Sraix '").append(input).append("' failed").toString());
     }
     return "SRAIXFAILED";
   }
 }

