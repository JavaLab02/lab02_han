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
	//Number of clients
	int clientNo = 0;
	
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
				//加入客户端链表
				clients.add(task);
				
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
					
					//单词查询
					if (head=='0')
					{
						handleSearch(recv);
					}
					//登录
					else if(head=='1')
					{
						handleSignIn(recv);
					}
					//注册
					else if (head=='2')
					{
						handleSignUp(recv);
					}
					//注销log out
					else if(head=='3')
					{
						handleLogOut(recv);
					}
					
					//向某在线用户发送单词卡
					else if (head == '4')
					{
						
					}
					
					//处理客户端窗口关闭
					else if (head=='-')
					{
						
						
					}
						
				}
				
			}
			catch(IOException ex)
			{
				if (isOnline)
				{
					
					int index1 = onlineClients.indexOf(this);
					int index2 = onlineUser.indexOf(name);
					int index3 = clients.indexOf(this);
					//System.out.println("Online+ "+index1+" "+" "+index2);
					onlineClients.remove(index1);
					onlineUser.remove(index2);
					clients.remove(index3);
				}
				else
				{
					
					int index = clients.indexOf(this);
					//System.out.println("off line "+index);
					clients.remove(index);
				}
				
				jta.append("Client "+ this.no +" socket closed! \n");
				System.out.println(ex+"断开连接");
				
			}
		}
		
		
		
		 public void handleSearch(String recv) throws IOException
		 {
			 //prepare data to send to the client
			 //String send = "0"+GetTranslation.getBaidu(recv)+"&"+GetTranslation.getYoudao(recv)+"&"+GetTranslation.getBing(recv)+"*";
				
			 GetTranslation word = new GetTranslation(recv);
			 GetZan zan = new GetZan(recv);
			 zan.isInDB();

			 word.translate();
			 String send = "0"+word.getBaiduTranslation()+"&"+word.getYoudaTranslation()+"&"+word.getBingTranslation();
			 send +="&"+zan.getzanBaidu()+","+zan.getzanYoudao()+","+zan.getzanBing()+"*"; 
			
			 
			 //Send Data
			 outputToClient.writeChars(send);	
			 jta.append("Data received from client: " + recv + "\n");
			 jta.append("Server sends: " + send + "\n");
		 }
		 public void handleSignIn(String recv) throws IOException
		 {
			//prepare data to send to the client
			 String[] temp = recv.split("&");
			 String account = temp[0];
			 String password = temp[1];
				
			 try 
			 {
				 //登录成功
				 if (DataBaseHandler.isAuthorized(account, password))
				 {
					//删除重复登录
					 rmRelogin(account);					
					 onlineClients.add(this);
					 onlineUser.add(account);
					
					 this.isOnline = true;
					 this.name = account;
					 String send = "1" + "1&";
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
					 Server.this.sendToOnline(send);
					
					 jta.append("Data received from client: " + recv + "\n");
					 jta.append("Server sends: " + send + "\n");
					 
				 }
				 //登录失败
				 else
				 {
					 String send = "1" + "0*";
					 outputToClient.writeChars(send);
					 jta.append("Data received from client: " + recv + "\n");
					 jta.append("Server sends: " + send + "\n");
				 }
			 } 
			 catch (ClassNotFoundException e) 
			 {
				 e.printStackTrace();
			 }
				
				
		 }
		 public void handleSignUp(String recv) throws IOException
		 {
			//prepare data to send to the client
			String[] temp = recv.split("&");
			String account = temp[0];
			String password = temp[1];
				
			try 
			{
				if (DataBaseHandler.regUser(account, password)==1)
				{
					String send = "2" + "1*";
					outputToClient.writeChars(send);
					jta.append("Data received from client: " + recv + "\n");
					jta.append("Server sends: " + send + "\n");
				}
				else 
				{
					String send = "2" + "0*";
					outputToClient.writeChars(send);
					jta.append("Data received from client: " + recv + "\n");
					jta.append("Server sends: " + send + "\n");
				}
			}
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
		 }

		 public void handleLogOut(String recv)
		 {
			 int name_index = onlineUser.indexOf(recv);
			 int client_index = onlineClients.indexOf(this);
			 onlineUser.remove(name_index);
			 onlineClients.remove(client_index);
		
				
			 //更新在线用户
			 String send = "3"+"";
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
			 Server.this.sendToOnline(send);
				
			 jta.append("Data received from client: " + recv + "\n");
			 jta.append("Server sends: " + send + "\n");
		 }
		 
		 public void handleSendWordCard(String username, String word)
		 {
			 
			 
			 
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
			if (client.name.equalsIgnoreCase(username));
				sendmsg(client.outputToClient,msg);
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
				String send = "5out*";
				sendmsg(client.outputToClient,send);
				onlineClients.remove(onlineClients.indexOf(client));
				onlineUser.remove(onlineUser.indexOf(name));
				return true;
			}
			
		}
		return false;
	}
	public static void main(String args[])
	{
		new Server();
	}
	
}

