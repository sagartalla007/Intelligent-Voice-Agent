package com.iva.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sagar.iva.MagicStrings;
import com.sagar.iva.MyChat;

/**
 * Servlet implementation class MainBotChat
 */
public class MainBotChat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//protected Bot bot;
	protected MyChat chatSession;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainBotChat() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter(); 
		response.setContentType("text/html");
				
		
		String uid_req = request.getParameter("userId");
		
		if(uid_req != null)
		{			
		String predicatesData=null;
				
		
		if(request.getSession(false) ==  null)
		
			{			
				
			        
			
			        HttpSession session = request.getSession(true);
					session.setAttribute("userId", uid_req);
				    session.setMaxInactiveInterval(15*60);
				   
					MagicStrings.root_path=getServletContext().getRealPath("/var");
					BotProvider botprovider = new BotProvider(MagicStrings.root_path);
					chatSession = new MyChat(botprovider.getBot()); 
				   			
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://ec2-23-21-211-172.compute-1.amazonaws.com/ivadb", "sagartalla007", "123456");
					Statement s = con.createStatement();
					String sql = "SELECT * FROM predicates where userId='"+uid_req+"'";			
					ResultSet rs = s.executeQuery(sql);
					
					
					while(rs.next())
					{
						predicatesData = rs.getString(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(predicatesData!= null)
				{
					InputStream in  = new ByteArrayInputStream(predicatesData.getBytes());
					chatSession.predicates.getPredicateDefaultsFromInputStream(in);
								    
				}
				session.setAttribute("chatSession", chatSession);
		}
		
		
		
		
		    HttpSession session = request.getSession(true);
		    session.setMaxInactiveInterval(2*60*60);
		    String uid_session = (String) session.getAttribute("userId");
		    
		    if(uid_session.equals(uid_req)){
		    chatSession = (MyChat)session.getAttribute("chatSession"); 
			String bot_output=chatSession.multisentenceRespond(request.getParameter("input"));		
			out.print(bot_output);
		    }
		    else {
				out.print("401:wrong userID");
			}
		}	
		else {
			out.print("401:no userID");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
