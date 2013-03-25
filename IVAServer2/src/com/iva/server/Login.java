package com.iva.server;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.sagar.iva.MagicStrings;
import com.sagar.iva.MyChat;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//public static Bot bot;
	protected MyChat chatSession;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MagicStrings.root_path = getServletContext().getRealPath("/var");
		/*Bot bot = new Bot("super", MagicStrings.root_path, "chat");
		bot.brain.nodeStats();*/
		
		BotProvider botprovider = new BotProvider(MagicStrings.root_path);
		
		HttpSession httpsession = request.getSession(true);
		PrintWriter p = response.getWriter();
			
		String uId = request.getParameter("userId");
		
		if(uId == null)
		{					
			uId = UUIDGenerator();
			httpsession.setAttribute("userId", uId);
						 		
			//chatSession = new MyChat(botprovider.getBot()); 
			
			try{
				
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://ec2-23-21-211-172.compute-1.amazonaws.com/ivadb", "sagartalla007", "123456");
				Statement s = con.createStatement();
				String sql = "INSERT INTO predicates(userId) values('"+ uId +"')";
				
				s.executeUpdate(sql);
							
				p.write(uId);
				
				System.out.print("INSERT SUCCESS");
				
				chatSession = new MyChat(botprovider.getBot());
				httpsession.setAttribute("chatSession", chatSession);
				
			}catch(CommunicationsException e)
			{
				p.write(e.toString());
			}			
			catch(Exception e){
				e.printStackTrace();
				
			}
			
		}
		
		//httpsession.setAttribute("chatSession", chatSession);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private String UUIDGenerator()
	{
		 UUID idOne = UUID.randomUUID();
		 return idOne.toString();
		    		
	}

}
