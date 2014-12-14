package lab02_han;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.*;

public class Client implements Runnable
{
	//Interface
	private UI ui;
	//IO stream
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	
	private Vector<String> onlineUserList;
	
	private String username;
	
	//construction method
	public Client()
	{
		ui = new UI();
		ui.setTitle("Online Dictionary");
		ui.pack();
		ui.setLocationRelativeTo(null);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
		onlineUserList = new Vector<String>();
		
		
		/*
		 * 设置UI中需要传递数据的按键监听类
		 */
		//search button listener
		ui.search.addActionListener(new SearchListener());
		
		//SignIn button Listener 登录按钮
		ui.sign_in.addActionListener(new SignInListener());
		
		//SignUp button listener 注册按钮
		ui.sign_up.addActionListener(new SignUpListener());
		//log out 注销按钮
		ui.log_out.addActionListener(new LogOutListener());
		
		//baidu翻译框的send和点赞
		ui.send1.send.addActionListener(new sendBaidu());
		ui.send1.zan.addActionListener(new zanBaidu());
		
		//youdao翻译框的send和点赞
		ui.send2.send.addActionListener(new sendYoudao());
		ui.send2.zan.addActionListener(new zanYoudao());

		//Bing翻译框的send和点赞
		ui.send3.send.addActionListener(new sendBing());
		ui.send3.zan.addActionListener(new zanBing());
		
		ConnectionToServer();
		Thread thread = new Thread(this);
		thread.start();
		
	}
	
	//建立与服务器的IO连接
	private void ConnectionToServer()
	{
		try
		{
			//Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8000);
			
			//Create an input stream to receive data from server
			fromServer = new DataInputStream(socket.getInputStream());
			//Create an output stream to send data to server
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex)
		{
			System.err.println(ex+" 未连接上服务器");
			
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
				
				//单词查询
				if (head == '0')
				{
					handleSearchFeedback(recv);
				
				}
				//登录
				else if (head=='1')
				{
					handleSignInFeedback(recv);
				}
				//注册
				else if (head=='2')
				{
					handleSignUpFeedback(recv);
				}
				//更新在线用户列表
				else if (head=='3')
				{
					handleUpdateUserList(recv);
				}
				
			}
		}
		catch(IOException ex)
		{
			System.err.println(ex);
		}
	}
	
	//处理来自服务器的查询反馈
	private void handleSearchFeedback(String recv)
	{
		String[] temp = recv.split("&");
		temp[0].trim();
		temp[1].trim();
		temp[2].trim();
		ui.text_area1.setText("");
		ui.text_area2.setText("");
		ui.text_area3.setText("");
		
		//Display to the text area
		ui.text_area1.append(temp[0]);
		ui.text_area2.append(temp[1]);
		ui.text_area3.append(temp[2]);
	}
	
	//处理登录反馈
	//recv格式:  "0/1&name1,name2,name3"
	private void handleSignInFeedback(String recv)
	{
		String[] temp = recv.split("&");
		System.out.println(recv);
		if (temp[0].equals("0"))//登录失败
		{
			MyDialog md = new MyDialog(ui,"提示",true,"用户名或密码错误");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		else if(temp[0].equals("1"))//成功
		{
			String[] names = temp[1].split(",");
			this.onlineUserList.removeAllElements();
			for (int i=0; i<names.length; i++)
			{
				this.onlineUserList.add(names[i]);
				System.out.println(names[i]);
			}
			ui.updateUserList(onlineUserList);
			//ui.online(names[names.length-1]);
			ui.online(username);
		}
	}
	
	private void handleUpdateUserList(String recv)
	{
		String[] names = recv.split(",");
		this.onlineUserList.removeAllElements();
		for (int i=0; i<names.length; i++)
		{
			this.onlineUserList.add(names[i]);
			System.out.println(names[i]);
		}
		ui.updateUserList(onlineUserList);
	}
	
	//处理注册反馈
	private void handleSignUpFeedback(String recv)
	{
		System.out.println(recv);
		if (recv.equals("1"))
		{
			MyDialog md = new MyDialog(ui,"提示",true,"注册成功");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		else
		{
			MyDialog md = new MyDialog(ui,"提示",true,"该用户名已存在");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
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
	
	//handle SignIn action 登录
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
						//判断长度
						else if(account.length()>=20 || password.length()>=20)
						{
							MyDialog md = new MyDialog(signin,"提示",true,"用户名和密码不能超过20字节");
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
							username = account;
							signin.dispose();
							
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
	
	
	//handle SignUp action 注册
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
						
						//判断是否为空
						if (account==null || account.replace(" ", "").equals("") || password==null || password.replace(" ", "").equals("") )
						{
							MyDialog md = new MyDialog(signup,"提示",true,"用户名和密码不能为空");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
						//判断长度
						else if(account.length()>=20 || password.length()>=20)
						{
							MyDialog md = new MyDialog(signup,"提示",true,"用户名和密码不能超过20字节");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
						
						else if (isValid(account)&&isValid(password))
						{
							//数据受字符为2，表示注册
							toServer.writeChars("2");
							//&作为连接符
							toServer.writeChars(account+"&");
							//*作为结束符
							toServer.writeChars(password+"*");
							toServer.flush();
							signup.dispose();
							
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
	
	private class LogOutListener implements ActionListener
	{	
		public void actionPerformed(ActionEvent e)
		{
			String send = "3"+username+"*";
			ui.logout();
			onlineUserList.removeAllElements();
			ui.updateUserList(onlineUserList);
			
			try 
			{
				toServer.writeChars(send);
				toServer.flush();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			
		}
	}
	
	private class sendBaidu implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			MyDialog md = new MyDialog(ui,"提示",true,"发送成功");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		}
	}
	private class sendYoudao implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			MyDialog md = new MyDialog(ui,"提示",true,"发送成功");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		}
	}
	private class sendBing implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			MyDialog md = new MyDialog(ui,"提示",true,"发送成功");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		}
	}
	private class zanBaidu implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int i = Integer.parseInt(ui.send1.zan_count.getText());
			if (ui.send1.zan.getText().equals("赞"))
			{
				ui.send1.zan.setText("取消赞");
				i +=1;
				ui.send1.zan_count.setText(""+i);
				ui.send1.label.setVisible(true);
					
			}
			else
			{
				ui.send1.zan.setText("赞");
				i -= 1;
				ui.send1.zan_count.setText(""+i);
				ui.send1.label.setVisible(false);
						
			}
		}
	}
	
	private class zanYoudao implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int i = Integer.parseInt(ui.send2.zan_count.getText());
			if (ui.send2.zan.getText().equals("赞"))
			{
				ui.send2.zan.setText("取消赞");
				i +=1;
				ui.send2.zan_count.setText(""+i);
				ui.send2.label.setVisible(true);
					
			}
			else
			{
				i -= 1;
				ui.send2.zan_count.setText(""+i);
				ui.send2.zan.setText("赞");
				ui.send2.label.setVisible(false);
						
			}
		}
	}
	
	private class zanBing implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int i = Integer.parseInt(ui.send3.zan_count.getText());
			if (ui.send3.zan.getText().equals("赞"))
			{
				ui.send3.zan.setText("取消赞");
				i += 1;
				ui.send3.zan_count.setText(""+i);
				ui.send3.label.setVisible(true);
					
			}
			else
			{
				ui.send3.zan.setText("赞");
				i -= 1;
				ui.send3.zan_count.setText(""+i);
				ui.send3.label.setVisible(false);
						
			}
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
	
		

	
	
	
	public static void main(String[] args)
	{
		new Client();
	}
	
}
