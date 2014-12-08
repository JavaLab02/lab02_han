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
				
				//����Ϊ��
				if (word==null || word.replace(" ", "").equals(""))
				{
					MyDialog md = new MyDialog(ui ,"��ʾ",true,"���벻��Ϊ��");
					md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				else if (isLetter(word))
				{
					/*
					 * send word to Server
					 */
					word = word.trim();
					//�������ַ�Ϊ0����ʾ��ѯ
					toServer.writeChars("0");
					//ĩβ��*����ʾ������
					toServer.writeChars(word+"*");
					toServer.flush();
					
				}
				else
				{
					//���벻ΪӢ�ĵ���
					MyDialog md = new MyDialog(ui ,"��ʾ",true,"������Ӣ�ĵ���");
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
	
	//����Ƿ�ֻ����ĸ���
	boolean isLetter(String str)
	{
		//��ȥ�ո�תСд
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
					//��ȡ�������ַ�
					head = inputFromServer.readChar();
					//��ȡʣ���ַ�
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
