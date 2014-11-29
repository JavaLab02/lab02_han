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
				//Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
				
				//Continuously serve the client
				while(true)
				{
					//Receive data from the client
					double radius = inputFromClient.readDouble();
					
					double area = radius*radius*Math.PI;
							
					//Send data
					outputToClient.writeDouble(area);
					
					jta.append("radius received from client: " + radius + "\n");
					jta.append("Area found: " + area + "\n");
				}
			}
			catch(IOException ex)
			{
				System.err.println(ex);
			}
		}
		 
	}
	
	
	public static void main(String[] args) 
	{
	
	}

}
