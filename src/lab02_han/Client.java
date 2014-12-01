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
				System.out.println("δ�����Ϸ�����");
			}
			*/
			//Create an input stream to receive data from server
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex)
		{
			System.err.println(ex+" δ�����Ϸ�����");
			
		}
		
		/*
		 * ����UI����Ҫ�������ݵİ���������
		 */
		//search button listener
		ui.search.addActionListener(new SearchListener());
		
		//SignIn button Listener ��¼��ť
		ui.sign_in.addActionListener(new SignInListener());
		
		//SignUp button listener ע�ᰴť
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
					//�������ַ�Ϊ0����ʾ��ѯ
					toServer.writeChars("0");
					//ĩβ��*����ʾ������
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
			signin.setTitle("��¼");
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
						//��ȡ�û���������
						String account = signin.account.getText();
						String password = signin.password.getText();
						
						//�ж��Ƿ�Ϊ��
						if (account==null || account.replace(" ", "").equals("") || password==null || password.replace(" ", "").equals("") )
						{
							MyDialog md = new MyDialog(signin,"��ʾ",true,"�û��������벻��Ϊ��");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
							
						
						else if (isValid(account)&&isValid(password))
						{
							//�������ַ�Ϊ1����ʾ��¼
							toServer.writeChars("1");
							//&��Ϊ���ӷ�
							toServer.writeChars(account+"&");
							//*��Ϊ������
							toServer.writeChars(password+"*");
							toServer.flush();
							
						}
						else
						{
							MyDialog md = new MyDialog(signin,"��ʾ",true,"�û���������ֻ������ĸ���������");
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
			signup.setTitle("ע��");
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
						//��ȡ�û���������
						String account = signup.account.getText();
						String password = signup.password.getText();
						
						if (isValid(account)&&isValid(password))
						{
							//�������ַ�Ϊ2����ʾע��
							toServer.writeChars("2");
							//&��Ϊ���ӷ�
							toServer.writeChars(account+"&");
							//*��Ϊ������
							toServer.writeChars(password+"*");
							toServer.flush();
							
						}
						else
						{
							MyDialog md = new MyDialog(signup,"��ʾ",true,"�û���������ֻ������ĸ���������");
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
	 * ����ַ����Ƿ�Ϸ�
	 * ���ڴ����û�ʱ���û���/������
	 * �涨�û��������붼ֻ����Ӣ����ĸ���������
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
