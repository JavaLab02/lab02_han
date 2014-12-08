package lab02_han;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class Client 
{
	//Interface
	private UI ui;
	//output stream
	private DataOutputStream toServer;
	
	//construction method
	public Client()
	{
		ui = new UI();
		ui.setTitle("Online Dictionary");
		ui.pack();
		ui.setLocationRelativeTo(null);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
		
		try
		{
			//Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8000);
			
			Receive receive = new Receive(socket);
			
			Thread receiveThread = new Thread(receive);
			receiveThread.start();
			
			
			//Create an input stream to receive data from server
			//fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex)
		{
			System.err.println(ex+" 未连接上服务器");
			
		}
		
		/*
		 * 设置UI中需要传递数据的按键监听类
		 */
		//search button listener
		ui.search.addActionListener(new SearchListener());
		
		//SignIn button Listener 登录按钮
		ui.sign_in.addActionListener(new SignInListener());
		
		//SignUp button listener 注册按钮
		ui.sign_up.addActionListener(new SignUpListener());
		
	}
	
	
	
	//handle search button action
	private class SearchListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				
				
				//get word 
				String word = ui.input.getText();
				
				//输入为空
				if (word==null || word.replace(" ", "").equals(""))
				{
					MyDialog md = new MyDialog(ui ,"提示",true,"输入不能为空");
					md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				else if (isLetter(word))
				{
					/*
					 * send word to Server
					 */
					word = word.trim();
					//数据首字符为0，表示查询
					toServer.writeChars("0");
					//末尾加*，表示结束符
					toServer.writeChars(word+"*");
					toServer.flush();
					
				}
				else
				{
					//输入不为英文单词
					MyDialog md = new MyDialog(ui ,"提示",true,"请输入英文单词");
					md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					
				}		
			}
			catch(IOException  ex)
			{
				System.err.println(ex);
			}
			
		}
	}
	
	//handle SignIn action
	private class SignInListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			final SignIn signin = new SignIn();
			signin.setTitle("登录");
			signin.pack();
			signin.setLocationRelativeTo(null);
			signin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			signin.setVisible(true);
			signin.jbt1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{	
						//获取用户名个密码
						String account = signin.account.getText();
						String password = signin.password.getText();
						
						//判断是否为空
						if (account==null || account.replace(" ", "").equals("") || password==null || password.replace(" ", "").equals("") )
						{
							MyDialog md = new MyDialog(signin,"提示",true,"用户名和密码不能为空");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
							
						
						else if (isValid(account)&&isValid(password))
						{
							//数据受字符为1，表示登录
							toServer.writeChars("1");
							//&作为连接符
							toServer.writeChars(account+"&");
							//*作为结束符
							toServer.writeChars(password+"*");
							toServer.flush();
							
						}
						else
						{
							MyDialog md = new MyDialog(signin,"提示",true,"用户名和密码只能由字母或数字组成");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
					}
					catch(IOException  ex)
					{
						System.err.println(ex);
					}
				}
			});
			signin.jbt2.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					signin.dispose();
				}
			});
		}
	}
	
	
	//handle SignUp action
	private class SignUpListener implements ActionListener
	{	
		public void actionPerformed(ActionEvent e)
		{
			final SignUp signup = new SignUp();
			signup.setTitle("注册");
			signup.pack();
			signup.setLocationRelativeTo(null);
			signup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			signup.setVisible(true);
			signup.jbt1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{	
						//获取用户名个密码
						String account = signup.account.getText();
						String password = signup.password.getText();
						
						if (isValid(account)&&isValid(password))
						{
							//数据受字符为2，表示注册
							toServer.writeChars("2");
							//&作为连接符
							toServer.writeChars(account+"&");
							//*作为结束符
							toServer.writeChars(password+"*");
							toServer.flush();
							
						}
						else
						{
							MyDialog md = new MyDialog(signup,"提示",true,"用户名和密码只能由字母或数字组成");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
						
					}
					catch(IOException  ex)
					{
						System.err.println(ex);
					}
				}
			});
			signup.jbt2.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					signup.dispose();
				}
			});
		}
	}
	
	/*	
	 * 检查字符串是否合法
	 * 用于创建用户时的用户名/密码检查
	 * 规定用户名和密码都只能由英文字母或数字组成
	 */
	boolean isValid(String str)
	{
		/*
		if (str==null || str.replace(" ", "").equals(""))
			return false;
		*/
		str = str.toLowerCase();
		for (int i=0; i<str.length(); i++)
		{
			if ( (str.charAt(i)>='a'&&str.charAt(i)<='z') || (str.charAt(i)>='0'&&str.charAt(i)<='9'))
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	//检查是否只由字母组成
	boolean isLetter(String str)
	{
		//消去空格，转小写
		str = str.replace(" ", "").toLowerCase();
		for (int i=0; i<str.length(); i++)
		{
			if (str.charAt(i)>='a'&&str.charAt(i)<='z')
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	//Inner class
	//Define the thread class for receiving message from Server
	//and handle the message
	class Receive implements Runnable
	{
		private Socket socket; //A connection socket
				
				
		// Construct a thread
		public Receive(Socket socket)
		{
			this.socket = socket;
		}
		
		public void test(String str)
		{
			
		}
		
		//run a thread
		public void run()
		{
			try
			{
				//Create Data input 
				DataInputStream inputFromServer = new DataInputStream(socket.getInputStream());
				
				
				//Continuously serve the client
				while(true)
				{

					//Receive Data from the Server
					char head;
					char ch;
					String recv = new String("");
					//读取数据首字符
					head = inputFromServer.readChar();
					//读取剩余字符
					while ( (ch = inputFromServer.readChar()) != '*')
					{
						recv += ch;
					}
					
					//System.out.println(recv);
					
					if (head == '0')
					{

						String[] temp = recv.split("&");
						temp[0].trim();
						temp[1].trim();
						temp[2].trim();
						
						//Display to the text area
						ui.text_area1.append(temp[0]);
						ui.text_area2.append(temp[1]);
						ui.text_area3.append(temp[2]);
					}
					else if (head=='1')
					{
						
					}
					else if (head=='2')
					{
						
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
	
	
	
	public static void main(String[] args)
	{
		new Client();
	}
	
}
