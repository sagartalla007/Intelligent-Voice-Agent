package com.iva.server;
import com.sagar.iva.*;



public class BotProvider {

	String Bot_path;
	static Bot bot;
	
	public BotProvider()
	{
			
	}
	
	public BotProvider(String path)
	{
	   if(bot == null)
	   {
		   bot = new Bot("super", path, "chat");
		   bot.brain.nodeStats();
	   }
	}
	
	public Bot getBot()
	{
		
		return bot;
	}
	
}
