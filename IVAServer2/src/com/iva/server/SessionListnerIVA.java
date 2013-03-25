package com.iva.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.sagar.iva.MyChat;

public class SessionListnerIVA implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent arg0) {
	
		System.out.print("Session Created");
  
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		
		
		HttpSession session = arg0.getSession();
		session.setMaxInactiveInterval(20);
		
		String uId = (String) session.getAttribute("userId");
		
		MyChat chatSession = (MyChat)session.getAttribute("chatSession");
		
		System.out.print(uId);
		String predicatesData="";
		for(String key:chatSession.predicates.keySet())
		{
			predicatesData+=key+":"+chatSession.predicates.get(key)+"\n";			
		}
		System.out.print(predicatesData);
		
		try {
	    Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://ec2-23-21-211-172.compute-1.amazonaws.com/ivadb", "sagartalla007", "123456");
		Statement s = con.createStatement();
		String sql = "UPDATE predicates"
				+ " SET predicatesData = '"+ predicatesData +"'"
				+ " WHERE userId='"+uId+"'";
		
		s.executeUpdate(sql);
		System.out.print("Session Destroyed1");
		} catch (Exception e) {
			e.printStackTrace();
		}
        	
		System.out.print("Session Destroyed2");
	}

}
