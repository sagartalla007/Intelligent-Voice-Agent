package com.iva.server;
import java.io.BufferedReader;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
import java.util.Scanner;

import com.sagar.iva.*;

public class Main
{
public static void main(String[] args)
{
	MagicStrings.root_path = "/home/sagar/workspace/workspace_web/IVAServer2/WebContent/var";
	System.out.println("Working Directory = " + MagicStrings.root_path);
	mainFunction(args);
//	MagicStrings.root_path = System.getProperty("user.dir");
	/*MagicStrings.root_path = "/home/sagar/workspace/workspace_web/IVAServer2/WebContent/var";
	Bot bot = new Bot("super", MagicStrings.root_path, "chat");
	bot.brain.nodeStats();
	MyChat chatsession = new MyChat(bot);
	Scanner src = new Scanner(System.in); 
	String s=null;
	while((s=src.nextLine()) != null)
	{	        	 
		System.out.print(chatsession.multisentenceRespond(s));	  
	}
	  */        	
	        	          	
	}
	public static void mainFunction(String[] args) {
		String botName = "super";
		String action = "chat";
		System.out.println(MagicStrings.programNameVersion);
		
		for (String s : args) {
			System.out.println(s);
			String[] splitArg = s.split("=");
			if (splitArg.length >= 2) {
				String option = splitArg[0];
				String value = splitArg[1];
				if (option.equals("bot")) botName = value;
				if (option.equals("action")) action = value;
				if ((option.equals("trace")) && (value.equals("true"))) MagicBooleans.trace_mode = true; else
					MagicBooleans.trace_mode = false;
			}
		}
		System.out.println("trace mode = " + MagicBooleans.trace_mode);
		Graphmaster.enableShortCuts = true;
		Bot bot = new Bot(botName, MagicStrings.root_path, action);
		if (bot.brain.getCategories().size() < 100) bot.brain.printgraph();
		if (action.equals("chat")) testChat(bot, MagicBooleans.trace_mode);
		else if (action.equals("test")) testSuite(bot, MagicStrings.root_path + "/data/find.txt");
		else if (action.equals("ab")) testAB(bot);
		else if ((action.equals("aiml2csv")) || (action.equals("csv2aiml"))) convert(bot, action);
		else if (action.equals("abwq")) AB.abwq(bot); 
	}
	
	public static void convert(Bot bot, String action) { if (action.equals("aiml2csv")) bot.writeAIMLIFFiles();
	else if (action.equals("csv2aiml")) bot.writeAIMLFiles();  } 
	public static void testAB(Bot bot)
	{
		MagicBooleans.trace_mode = true;
		AB.ab(bot);
		AB.terminalInteraction(bot);
	}
	
	public static void testShortCuts()
	{
	}
	
	public static void testChat(Bot bot, boolean traceMode)
	{
		Chat chatSession = new Chat(bot);
		
		bot.brain.nodeStats();
		MagicBooleans.trace_mode = traceMode;
		BufferedReader lineOfText = new BufferedReader(new InputStreamReader(System.in));
		String textLine = "";
		while (true) {
			System.out.print("Human: ");
			try {
				textLine = lineOfText.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if ((textLine == null) || (textLine.length() < 1)) textLine = MagicStrings.null_input;
			if (textLine.equals("q")) { System.exit(0);
			} else if (textLine.equals("wq")) {
				bot.writeQuit();
				System.exit(0);
			}
			else if (textLine.equals("ab")) { testAB(bot);
			} else {
				String request = textLine;
				if (MagicBooleans.trace_mode) System.out.println("STATE=" + request + ":THAT=" + ((History)chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
				String response = chatSession.multisentenceRespond(request);
				System.out.println("Robot: " + response);
			}
		}
	}
	
	public static void testBotChat()
	{
		Bot bot = new Bot("alice");
		System.out.println(bot.brain.upgradeCnt + " brain upgrades");
		bot.brain.nodeStats();
		
		Chat chatSession = new Chat(bot);
		String request = "Hello.  How are you?  What is your name?  Tell me about yourself.";
		String response = chatSession.multisentenceRespond(request);
		System.out.println("Human: " + request);
		System.out.println("Robot: " + response);
	}
	public static void testSuite(Bot bot, String filename) {
		try {
			AB.passed.readAIMLSet(bot);
			AB.testSet.readAIMLSet(bot);
			System.out.println("Passed " + AB.passed.size() + " samples.");
			BufferedReader lineOfText = new BufferedReader(new InputStreamReader(System.in));
			String textLine = "";
			Chat chatSession = new Chat(bot);
			FileInputStream fstream = new FileInputStream(filename);
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			int count = 0;
			HashSet samples = new HashSet();
			String strLine;
			while ((strLine = br.readLine()) != null) {
				samples.add(strLine);
			}
			ArrayList<String> sampleArray = new ArrayList<String>(samples);
			Collections.sort(sampleArray);
			for (String request : sampleArray) {
				if (request.startsWith("Human: ")) request = request.substring("Human: ".length(), request.length());
				Category c = new Category(0, bot.preProcessor.normalize(request), "*", "*", MagicStrings.blank_template, MagicStrings.null_aiml_file);
				if (AB.passed.contains(request)) { System.out.println("--> Already passed " + request);
				} else if ((!bot.deletedGraph.existsCategory(c)) && (!AB.passed.contains(request))) {
					String response = chatSession.multisentenceRespond(request);
					System.out.println(count + ". Human: " + request);
					System.out.println(count + ". Robot: " + response);
					try {
						textLine = lineOfText.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					AB.terminalInteractionStep(bot, request, textLine, c);
					count++;
				}
			}
			
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
	
	