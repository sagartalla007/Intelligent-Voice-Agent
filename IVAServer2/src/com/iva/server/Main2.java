package com.iva.server;

import java.util.Scanner;

import com.sagar.iva.Bot;
import com.sagar.iva.Chat;
import com.sagar.iva.MagicStrings;

public class Main2 {

	public static void main(String args[])
	{
		
	     MagicStrings.root_path = "/home/sagar/workspace/workspace_web/IVAServer2/WebContent/var";
	     
	     Bot bot = new Bot("super", MagicStrings.root_path, "chat");
	     
	     bot.brain.nodeStats();
	     
	     Chat chatsession =  new Chat(bot);
	     
	     Scanner src = new Scanner(System.in);
	     while(true){
	    	 
	    	 System.out.println(chatsession.multisentenceRespond(src.nextLine()));
	    	 
	     }
		
	}
	
}
