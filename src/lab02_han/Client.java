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
	//IO stream
	private DataInputStream fromServer;
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
			/*
			if (!socket.isConnected())
			{
				System.out.println("未连接上服务器");
			}
			*/
			//Create an input stream to receive data from server
			fromServer = new DataInputStream(socket.getInputStream());
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
				word = word.trim();
				
				//send word to Server
				if (word!=null && !(word.replace(" ", "").equals("")))
				{	
					//数据首字符为0，表示查询
					toServer.writeChars("0");
					//末尾加*，表示结束符
					toServer.writeChars(word+"*");
					toServer.flush();
					
					//get data from Server
					char ch;
					String feedback = new String("");
					while ( (ch = fromServer.readChar()) != '*')
					{
						feedback += ch;
					}
					
					
					//String feedback = new String((String) fromServer.readData());
					
					//System.out.println(feedback);
					//Display to the text area
					ui.text_area1.append(feedback);
					ui.text_area2.append(feedback);
					ui.text_area3.append(feedback);
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
	
	public static void main(String[] args)
	{
		new Client();
	}
	
}
