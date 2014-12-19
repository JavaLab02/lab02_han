package lab02_han;



import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.*;





public class ChatRoomClient implements Runnable
{
	//Interface
	ChatFrame chatFrame;
	//IO stream
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	
	private Vector<String> onlineUserList;

	//用户名
	private String username;
	
	
	
	//construction method
	public ChatRoomClient(String name)
	{
		username = name;
		chatFrame = new ChatFrame();
		chatFrame.setTitle("Chat Room");
		chatFrame.pack();
		chatFrame.setLocationRelativeTo(null);
		//chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setVisible(true);
		onlineUserList = new Vector<String>();
		
		chatFrame.jbt.addActionListener(new sendListener());
		
		
		ConnectionToServer();
		Thread thread = new Thread(this);
		thread.start();
		String userinfo = "0"+username+"*";
		try 
		{
			toServer.writeChars(userinfo);
			toServer.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	private class sendListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				//get word 
				String word = chatFrame.jta2.getText();
				
				//输入为空
				if (word==null || word.replace(" ", "").equals(""))
				{
					MyDialog md = new MyDialog(chatFrame ,"提示",true,"输入不能为空");
					md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				else
				{	
					chatFrame.jta2.setText("");
					word = word.trim();
					String send = "2"+ word +"*";
					toServer.writeChars(send);
					toServer.flush();
				}

			}
			catch(IOException  ex)
			{
				System.err.println(ex);
			}
			
		}
	}
	
	
	//建立与服务器的IO连接
	private void ConnectionToServer()
	{
		try
		{
			//Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8001);
			
			//Create an input stream to receive data from server
			fromServer = new DataInputStream(socket.getInputStream());
			//Create an output stream to send data to server
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex)
		{
			System.err.println(ex+" 未连接上服务器");
			System.exit(1);
			
		}
	}
	
	public void run()
	{
		try
		{
			//Continuously receive message from server
			while(true)
			{

				//Receive Data from the Server
				char head;
				char ch;
				String recv = new String("");
				//读取数据首字符
				head = fromServer.readChar();
				//读取剩余字符
				while ( (ch = fromServer.readChar()) != '*')
				{
					recv += ch;
				}
				
				//更新用户列表
				if (head=='1')
				{
					updateUserList(recv);
				}
				//接收信息，更新聊天窗口
				if(head=='2')
				{
					String[] temp = recv.split("&");
					if (temp[0].indexOf(username)!=-1)
					{
						chatFrame.setDocs(temp[0], Color.RED, true, 18);
						chatFrame.setDocs(temp[1], Color.BLACK, false, 15);
					}
					else
					{
						chatFrame.setDocs(temp[0], Color.BLUE, true, 18);
						chatFrame.setDocs(temp[1], Color.BLACK, false, 15);
					}
					//chatFrame.jta1.append(recv);
					System.out.println(recv);
				}
				//处理下线信息
				if (head=='3')
				{
					handleLogOut(recv);
				}
			
			}
		}
		catch(IOException ex)
		{
			System.err.println(ex);
		}
	}
	
	void updateUserList(String recv)
	{
		String[] names = recv.split(",");
		this.onlineUserList.removeAllElements();
		
		for (int i=0; i<names.length; i++)
		{
			this.onlineUserList.add(names[i]);
			//System.out.println(names[i]);
		}
		
		chatFrame.updateUserList(onlineUserList);
		
	}
	
	void handleLogOut(String recv)
	{
		if (recv.equals("out"))
		{
			MyDialog md = new MyDialog(chatFrame ,"提示",true,"Log out!您的帐号在其他地点登录");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			chatFrame.dispose();
		}
	}
	
	public static void main(String[] args)
	{
		new ChatRoomClient("test2");
		
	}
	
}
