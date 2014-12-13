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
		 * ����UI����Ҫ�������ݵİ���������
		 */
		//search button listener
		ui.search.addActionListener(new SearchListener());
		
		//SignIn button Listener ��¼��ť
		ui.sign_in.addActionListener(new SignInListener());
		
		//SignUp button listener ע�ᰴť
		ui.sign_up.addActionListener(new SignUpListener());
		
		ConnectionToServer();
		Thread thread = new Thread(this);
		thread.start();
		
	}
	
	//�������������IO����
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
			System.err.println(ex+" δ�����Ϸ�����");
			
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
				//��ȡ�������ַ�
				head = fromServer.readChar();
				//��ȡʣ���ַ�
				while ( (ch = fromServer.readChar()) != '*')
				{
					recv += ch;
				}
				
				//���ʲ�ѯ
				if (head == '0')
				{
					handleSearchFeedback(recv);
				
				}
				//��¼
				else if (head=='1')
				{
					handleSignInFeedback(recv);
				}
				//ע��
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
	
	//�������Է������Ĳ�ѯ����
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
	
	//�����¼����
	//recv��ʽ:  "0/1&name1,name2,name3"
	private void handleSignInFeedback(String recv)
	{
		String[] temp = recv.split("&");
		System.out.println(recv);
		if (temp[0].equals("0"))//��¼ʧ��
		{
			
		}
		else if(temp[0].equals("1"))//�ɹ�
		{
			String[] names = temp[1].split(",");
			for (int i=0; i<names.length; i++)
			{
				this.onlineUserList.add(names[i]);
				System.out.println(names[i]);
			}
			ui.updateUserList(onlineUserList);
		}
	}
	
	//����ע�ᷴ��
	private void handleSignUpFeedback(String recv)
	{
		
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
	
	//handle SignIn action ��¼
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
						//�жϳ���
						else if(account.length()>=20 || password.length()>=20)
						{
							MyDialog md = new MyDialog(signin,"��ʾ",true,"�û��������벻�ܳ���20�ֽ�");
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
	
	
	//handle SignUp action ע��
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
						
						//�ж��Ƿ�Ϊ��
						if (account==null || account.replace(" ", "").equals("") || password==null || password.replace(" ", "").equals("") )
						{
							MyDialog md = new MyDialog(signup,"��ʾ",true,"�û��������벻��Ϊ��");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
						//�жϳ���
						else if(account.length()>=20 || password.length()>=20)
						{
							MyDialog md = new MyDialog(signup,"��ʾ",true,"�û��������벻�ܳ���20�ֽ�");
							md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
						
						else if (isValid(account)&&isValid(password))
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
	
		

	
	
	
	public static void main(String[] args)
	{
		new Client();
	}
	
}
