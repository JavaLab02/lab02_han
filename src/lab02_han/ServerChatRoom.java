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
				
					
					
					//聊天室聊天信息
					else if (head=='2')
					{
						InetAddress inetAddress = socket.getInetAddress();
						String content = "2"+ name +" ("+inetAddress.getHostAddress()+")" + new Date()+'\n'+"&"+recv+'\n'+"*";
						sendToOnline(content);
					}
					
					//下线
					else if (head=='3')
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
						isOnline = false;
						jta.append("Client "+ this.no +" socket closed! \n");
						
					}
					//receive picture
					else if (head=='4')
					{
						String[] temp = recv.split("&");
						try 
						{
							handlePic(temp[0],temp[1]);
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
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
					if (index>=0)
					{
						allClients.remove(index);
					}
					
				}
				
				jta.append("Client "+ this.no +" socket closed! \n");
				System.out.println(ex+"断开连接");
			}
		}
		
		byte[] image2Bytes(String imgSrc) throws Exception
		{
			FileInputStream fin = new FileInputStream(new File(imgSrc));
			byte[] bytes  = new byte[fin.available()];
			fin.read(bytes);
			fin.close();
			return bytes;
		}
		void buff2Image(byte[] b,String tagSrc) throws Exception
		{
			FileOutputStream fout = new FileOutputStream(tagSrc);
			//将字节写入文件
			fout.write(b);
			fout.close();
		}
		
		
		void handlePic(String recv, String picname) throws Exception
		{
			//图片数据长度
			int len=Integer.parseInt(recv);
			//图片数据
			byte[] b = new byte[len];
			//接收图片数据
			inputFromClient.readFully(b);
			sendPicToAllClient(b, picname);
			
			
		}
		void sendPicToAllClient(byte[] b, String picname) throws Exception
		{
	        int len = b.length;
	        
	        for (HandleAClient client: onlineClients)
	        {
	        	 try 
	 	        {	
	        		InetAddress inetAddress = socket.getInetAddress();
	        		String user = this.name+" ("+inetAddress.getHostAddress()+")" + new Date()+"\n";
	 	        	client.outputToClient.writeChars("4");
	 	        	client.outputToClient.writeChars(len+"&"+picname+"&"+user+"*");
	 	        	client.outputToClient.write(b);
	 	        	client.outputToClient.flush();
	 			} 
	 	        catch (IOException e) 
	 	        {
	 				e.printStackTrace();
	 			}
	        }
		}
		
			
		public void sendUserListMsg()
		{
			 String send = "0";
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