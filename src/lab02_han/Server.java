package lab02_han;



import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Server extends JFrame
{
	
	private JTextArea jta = new JTextArea();
	
	public Server()
	{
		//Place text area on the frame
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		setTitle("Server");
		setSize(500,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try
		{
			//create a server socket
			ServerSocket serversocket = new ServerSocket(8000);
			jta.append("Server start at "+new Date() + '\n');
			
			//Number a client
			int clientNo = 1;
			while(true)
			{
				//listen for a new connection request
				Socket socket = serversocket.accept();
				
				//Display the client number
				jta.append("Starting thread for client "+clientNo+" at "+ new Date()+ "\n");
				
				//Find the client's host name and IP address
				InetAddress inetAddress = socket.getInetAddress();
				jta.append("Client "+ clientNo + "'s host name is "+inetAddress.getHostName()+"\n");
				jta.append("Client "+ clientNo + "'s IP Address is "+inetAddress.getHostAddress()+"\n");
				
				//create a new thread for the connection
				HandleAClient task = new HandleAClient(socket);
				
				//start a new thread
				new Thread(task).start();
				
				//Increment clientNo
				clientNo ++;
				
			}
		}
		catch(IOException ex)
		{
			System.err.println(ex);
		}
	}
	
	//Inner class
	//Define the thread class for handling new connection
	class HandleAClient implements Runnable
	{
		private Socket socket; //A connection socket
				
				
		// Construct a thread
		public HandleAClient(Socket socket)
		{
			this.socket = socket;
		}
		//run a thread
		public void run()
		{
			try
			{
				//Create Data input and output streams
			
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
				
				//Continuously serve the client
				while(true)
				{

					//Receive Data from the client
					char head;
					char ch;
					String recv = new String("");
					//¶ÁÈ¡Êý¾ÝÊ××Ö·û
					head = inputFromClient.readChar();
					//¶ÁÈ¡Ê£Óà×Ö·û
					while ( (ch = inputFromClient.readChar()) != '*')
					{
						recv += ch;
					}
					
					//µ¥´Ê²éÑ¯
					if (head=='0')
					{
						//prepare data to send to the client
						String send = "got"+recv;
							
						//Send Data
						outputToClient.writeChars(send+"*");
						
						
						jta.append("Data received from client: " + recv + "\n");
						jta.append("Server sends: " + send + "\n");
					}
					//µÇÂ¼
					else if(head=='1')
					{
						//prepare data to send to the client
						String send = "got"+recv;
							
						//Send Data
						outputToClient.writeChars(send+"*");

						jta.append("Data received from client: " + recv + "\n");
						jta.append("Server sends: " + send + "\n");
					}
					//×¢²á
					else if (head=='2')
					{
						//prepare data to send to the client
						String send = "got"+recv;
							
						//Send Data
						outputToClient.writeChars(send+"*");

						jta.append("Data received from client: " + recv + "\n");
						jta.append("Server sends: " + send + "\n");
					}
					
					else
					{
						
					}
						
					
					
					
					
				}
			}
			catch(IOException ex)
			{
				System.err.println(ex);
			}
		}
		 
	}
	
	public static void main(String args[])
	{
		new Server();
	}
	
}

