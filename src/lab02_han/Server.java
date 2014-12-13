package lab02_han;



import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Server extends JFrame
{
	
	JTextArea jta = new JTextArea();
	static ArrayList<HandleAClient> clients;
	static ArrayList<HandleAClient> onlineClients;
	static Vector<String> onlineUser;
	
	
	public Server()
	{
		clients = new ArrayList<HandleAClient>();
		onlineClients = new ArrayList<HandleAClient>();
		onlineUser = new Vector<String>();
		//Place text area on the frame
		setLayout(new BorderLayout());
		jta.setLineWrap(true);
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
				
				//加入客户端链表
				clients.add(task);
				
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
		//A connection socket
		Socket socket; 
		//user's name
		String name;
		
		boolean isOnline;
		
		DataInputStream inputFromClient;
		DataOutputStream outputToClient;	
		
		// Construct a thread
		public HandleAClient(Socket socket)
		{
			this.socket = socket;
			name = "";
			isOnline = false;
		}
		//run a thread
		public void run()
		{
			try
			{
				//Create Data input and output streams
				inputFromClient = new DataInputStream(socket.getInputStream());
				outputToClient = new DataOutputStream(socket.getOutputStream());
				
				//Continuously serve the client
				while(true)
				{
					if (!socket.isConnected())
					{
						System.out.print("connected");
					}
					
					//Receive Data from the client
					char head;
					char ch;
					String recv = new String("");
					//读取数据首字符
					head = inputFromClient.readChar();
					//读取剩余字符
					while ( (ch = inputFromClient.readChar()) != '*')
					{
						recv += ch;
					}
					
					//单词查询
					if (head=='0')
					{
						/*
						//prepare data to send to the client
						String send = "0"+GetTranslation.getBaidu(recv)+"&"+GetTranslation.getYoudao(recv)+"&"+GetTranslation.getBing(recv);
						
						//Send Data
						outputToClient.writeChars(send+"*");
						*/
						
						String send = "11&Tom,Bob,Lancy*";
						Server.this.sendToAll(send);
						
						jta.append("Data received from client: " + recv + "\n");
						jta.append("Server sends: " + send + "\n");
					}
					//登录
					else if(head=='1')
					{
						//prepare data to send to the client
						String[] temp = recv.split("&");
						String account = temp[0];
						String password = temp[1];
						
						try 
						{
							if (DataBaseHandler.isAuthorized(account, password))
							{
								onlineClients.add(this);
								onlineUser.add(account);
								this.isOnline = true;
								this.name = account;
								String send = head + "1&";
								boolean first = true;
								for (String name:onlineUser)
								{
									if (first==true)
									{
										send += name;
										first = false;
									}
									else
									{
										send +=","+name;
									}
									
								}
								send += "*";
								Server.this.sendToAll(send);
								
								jta.append("Data received from client: " + recv + "\n");
								jta.append("Server sends: " + send + "\n");
								
							}
							else
							{
								
							}
						} 
						catch (ClassNotFoundException e) 
						{
							e.printStackTrace();
						}
						
						
					}
					//注册
					else if (head=='2')
					{
						//prepare data to send to the client
						String[] temp = recv.split("&");
						String account = temp[0];
						String password = temp[1];
						String send = "2"+"SignUp \n accont: "+account+"\n password: "+password;
							
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
	
	public void sendmsg(DataOutputStream toClient,String msg)
	{
		try 
		{
			toClient.writeChars(msg);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	public void sendToAll(String msg)
	{
		for (HandleAClient client: clients)
		{
			sendmsg(client.outputToClient,msg);
		}
	}
	
	public static void main(String args[])
	{
		new Server();
	}
	
}

