package lab02_han;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;





@SuppressWarnings("serial")
public class ServerChatRoom extends JFrame
{
	
	JTextArea jta = new JTextArea();
	
	static ArrayList<HandleAClient> onlineClients;
	static ArrayList<HandleAClient> allClients;
	static Vector<String> onlineUser;
	//Number of clients
	int clientNo = 0;
	
	public ServerChatRoom()
	{
		
		onlineClients = new ArrayList<HandleAClient>();
		allClients = new ArrayList<HandleAClient>();
		onlineUser = new Vector<String>();
		//Place text area on the frame
		setLayout(new BorderLayout());
		jta.setLineWrap(true);
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		setTitle("Chat Room Server");
		setSize(500,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try
		{
			//create a server socket
			ServerSocket serversocket = new ServerSocket(8001);
			jta.append("Chat Room Server start at "+new Date() + '\n');
			
			while(true)
			{
				//Increment clientNo
				clientNo ++;
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
				task.no = clientNo;
				allClients.add(task);
				
				//start a new thread
				new Thread(task).start();
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
		//number of the client
		int no;
		
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
					
					//登录初始化
					if (head =='0')
					{
						name = recv;
						isOnline = true;
						jta.append("User "+ recv +" log in! \n");
						//删除重复登录
						rmRelogin(name);					
						onlineClients.add(this);
						onlineUser.add(name);
						sendUserListMsg();
						
					}
					
					//聊天信息
					else if (head=='2')
					{
						InetAddress inetAddress = socket.getInetAddress();
						String content = "2"+ name +"("+inetAddress.getHostAddress()+")" + new Date()+'\n'+"&"+recv+'\n'+"*";
						
						sendToOnline(content);
						
					}
					
					
						
				}
				
			}
			catch(IOException ex)
			{
				
				if (isOnline)
				{
					
					int index1 = onlineClients.indexOf(this);
					int index2 = onlineUser.indexOf(name);
					int index3 = allClients.indexOf(this);
					//System.out.println("Online+ "+index1+" "+" "+index2);
					onlineClients.remove(index1);
					onlineUser.remove(index2);
					allClients.remove(index3);
					
					//更新在线用户列表
					sendUserListMsg();
				}
				else
				{
					
					int index = allClients.indexOf(this);
					//System.out.println("off line "+index);
					allClients.remove(index);
				}
				
				jta.append("Client "+ this.no +" socket closed! \n");
				System.out.println(ex+"断开连接");
			}
		}
			
		public void sendUserListMsg()
		{
			 String send = "1";
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
			 this.sendToOnline(send);
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
		public void sendToOnline(String msg)
		{
			for (HandleAClient client: onlineClients)
			{
				sendmsg(client.outputToClient,msg);
			}
		}
		
		public void sendToUser(String username, String msg)
		{
			for (HandleAClient client: onlineClients)
			{
				if (client.name.equalsIgnoreCase(username))
				{
					sendmsg(client.outputToClient,msg);
				}
			}
		}
		
		
		//删除重复登录，被顶下线
		public boolean rmRelogin(String name)
		{
			name = name.trim();
			for (HandleAClient client: onlineClients)
			{
				if (client.name.equalsIgnoreCase(name)	)
				{
					client.isOnline = false;
					String send = "3out*";
					sendmsg(client.outputToClient,send);
					onlineClients.remove(onlineClients.indexOf(client));
					onlineUser.remove(onlineUser.indexOf(name));
					return true;
				}
				
			}
			return false;
		}
		
		
	}
	
	
	
	
	
	public static void main(String args[])
	{
		new ServerChatRoom();
	}
	
}