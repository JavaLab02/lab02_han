package lab02_han;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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
	
	private Vector<String> otherUserList;
	//�û���
	private String username;
	//������Ϣ���ݿ�
	DBWordInfo wordinfo;
	//��¼Ŀǰ��ѯ�ĵ���
	String wordToSearch;
	//�Ƿ�����
	boolean isOnline;
	
	//construction method
	public Client()
	{
		wordinfo = new DBWordInfo();
		ui = new UI();
		ui.setTitle("Online Dictionary");
		ui.pack();
		ui.setLocationRelativeTo(null);
		//ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
		onlineUserList = new Vector<String>();
		otherUserList = new Vector<String>();
		wordToSearch="";
		isOnline = false;
		
		/*
		 * ����UI����Ҫ�������ݵİ���������
		 */
		//search button listener
		ui.search.addActionListener(new SearchListener());
		
		//SignIn button Listener ��¼��ť
		ui.sign_in.addActionListener(new SignInListener());
		
		//SignUp button listener ע�ᰴť
		ui.sign_up.addActionListener(new SignUpListener());
		//log out ע����ť
		ui.log_out.addActionListener(new LogOutListener());
		
		//baidu������send�͵���
		ui.send1.send.addActionListener(new sendBaidu());
		ui.send1.zan.addActionListener(new zanBaidu());
		
		//youdao������send�͵���
		ui.send2.send.addActionListener(new sendYoudao());
		ui.send2.zan.addActionListener(new zanYoudao());

		//Bing������send�͵���
		ui.send3.send.addActionListener(new sendBing());
		ui.send3.zan.addActionListener(new zanBing());
		
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
					handleSignUpFeedback(recv);
				}
				//���������û��б�ͷ��͵��ʿ�������
				else if (head=='3')
				{
					handleUpdateUserList(recv);
					updateSendList();
				}
				
				//������յ��ʿ���Ϣ
				else if (head=='4')
				{
					handleReceiveWordCard(recv);
				}
				
				//����������
				else if(head == '5')
				{
					handleForceLogOut(recv);
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
		/*
		temp[0].trim();
		temp[1].trim();
		temp[2].trim();
		*/
		
		
		ui.text_area1.setText("");
		ui.text_area2.setText("");
		ui.text_area3.setText("");
		
		//Display to the text area
		ui.text_area1.append(temp[0]);
		ui.text_area2.append(temp[1]);
		ui.text_area3.append(temp[2]);
		
		
		
		//���޴���
		String[] zan = temp[3].split(",");
		if (zan.equals("0,0,0"))
			;
		else
		{
			//System.out.println(zan[0]+zan[1]+zan[2]);
			ui.sort_by_zan(zan);
		}
		
		
	}
	
	//�����¼����
	//recv��ʽ:  "0/1&name1,name2,name3"
	private void handleSignInFeedback(String recv)
	{
		String[] temp = recv.split("&");
		if (temp[0].equals("0"))//��¼ʧ��
		{
			MyDialog md = new MyDialog(ui,"��ʾ",true,"�û������������");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		else if(temp[0].equals("1"))//�ɹ�
		{
			isOnline = true;
			String[] names = temp[1].split(",");
			this.onlineUserList.removeAllElements();
			
			for (int i=0; i<names.length; i++)
			{
				this.onlineUserList.add(names[i]);
				//System.out.println(names[i]);
			}
			
			ui.updateUserList(onlineUserList);
			ui.online(username);
			updateSendList();
		}
	}
	
	//���·��͵��ʿ����û���������
	private void updateSendList()
	{
		otherUserList.removeAllElements();
		for (String str:onlineUserList)
		{
			if ( !str.equals(username))
				otherUserList.add(str);
	
		}
		
		ui.updateSendList(otherUserList);
	}
	
	//������������û��б�
	private void handleUpdateUserList(String recv)
	{
		String[] names = recv.split(",");
		this.onlineUserList.removeAllElements();
		
		for (int i=0; i<names.length; i++)
		{
			this.onlineUserList.add(names[i]);
			//System.out.println(names[i]);
		}
		
		ui.updateUserList(onlineUserList);
	}
	
	//����ע�ᷴ��
	private void handleSignUpFeedback(String recv)
	{
		System.out.println(recv);
		if (recv.equals("1"))
		{
			MyDialog md = new MyDialog(ui,"��ʾ",true,"ע��ɹ�");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		else
		{
			MyDialog md = new MyDialog(ui,"��ʾ",true,"���û����Ѵ���");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
	
	private void handleReceiveWordCard(String recv)
	{
		 
		
		String[] temp = recv.split("&");
		WordCard card = new WordCard();
		card.setContent(temp[0], temp[1]);
		ui.addWordCard(temp[0],card);
		MyDialog md = new MyDialog(ui,"��ʾ",true,"<html> <body> "+"���յ�һ�ŵ��ʿ� ("+temp[0]+ ") <br> "+"���ڡ��ҵĵ��ʿ����в鿴"+ " <body> </html> ");
		md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		/*
		final WordCard card = new WordCard();
		card.setTitle("���ʿ�");
		String[] temp = recv.split("&");
		card.setContent(temp[0], temp[1]);
		card.pack();
		card.setLocationRelativeTo(null);
		card.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		card.setVisible(true);
		*/
	}
	//������ǿ������
	private void handleForceLogOut(String recv)
	{
		if (recv.endsWith("out"))
		{
			isOnline = false;
			ui.logout();
			onlineUserList.removeAllElements();
			ui.updateUserList(onlineUserList);
			MyDialog md = new MyDialog(ui ,"��ʾ",true,"Log out!�����ʺ��������ص��¼");
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
					ui.send1.initial();
					ui.send2.initial();
					ui.send3.initial();
					word = word.trim();
					//�������ַ�Ϊ0����ʾ��ѯ
					toServer.writeChars("0");
					//ĩβ��*����ʾ������
					toServer.writeChars(word+"*");
					toServer.flush();
					wordToSearch = word;
					
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
							username = account;
							signin.dispose();
							
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
						String password2 = signup.confirmPassword.getText();
						
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
						else if (! password.equals(password2))
						{
							MyDialog md = new MyDialog(signup,"��ʾ",true,"���벻һ��");
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
							signup.dispose();
							
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
	
	//���߰�ť����
	private class LogOutListener implements ActionListener
	{	
		public void actionPerformed(ActionEvent e)
		{
			isOnline = false;
			String send = "3"+username+"*";
			ui.logout();
			onlineUserList.removeAllElements();
			
			updateSendList();
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
			String content = ui.text_area1.getText();
			//String username = ui.send1.input.getText().trim();
			String username = (String) ui.send1.input.getSelectedItem();
			
			if (isOnline)
			{
				if (username!=null )//&& username.length()>0)
				{
					if (content!=null && content.length()>0)
					{
						String send = "4"+ username+"&"+wordToSearch+"&"+ content +"*";
						try 
						{
							toServer.writeChars(send);
							toServer.flush();
						} 
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}
						
						
						MyDialog md = new MyDialog(ui,"��ʾ",true,"���ͳɹ�");
						md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
					else
					{
						MyDialog md = new MyDialog(ui,"��ʾ",true,"�������ݲ���Ϊ��");
						md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
				}
				else
				{
					MyDialog md = new MyDialog(ui,"��ʾ",true,"�û�������Ϊ��");
					md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
			}
			else
			{
				MyDialog md = new MyDialog(ui,"��ʾ",true,"���ȵ�¼");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

		}
	}
	
	
	private class sendYoudao implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String content = ui.text_area2.getText();
			String username = (String) ui.send2.input.getSelectedItem();
			
			if (isOnline)
			{
				if (username!=null )//&& username.length()>0)
				{
					if (content!=null && content.length()>0)
					{
						String send = "4"+ username+"&"+wordToSearch+"&"+ content +"*";
						try 
						{
							toServer.writeChars(send);
							toServer.flush();
						} 
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}
						
						
						MyDialog md = new MyDialog(ui,"��ʾ",true,"���ͳɹ�");
						md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
					else
					{
						MyDialog md = new MyDialog(ui,"��ʾ",true,"�������ݲ���Ϊ��");
						md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
				}
				else
				{
					MyDialog md = new MyDialog(ui,"��ʾ",true,"�û�������Ϊ��");
					md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
			}
			else
			{
				MyDialog md = new MyDialog(ui,"��ʾ",true,"���ȵ�¼");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

		}
	}
	private class sendBing implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String content = ui.text_area3.getText();
			String username = (String) ui.send3.input.getSelectedItem();
			if (isOnline)
			{
				if (username!=null )//&& username.length()>0)
				{
					if (content!=null && content.length()>0)
					{
						String send = "4"+ username+"&"+wordToSearch+"&"+ content +"*";
						try 
						{
							toServer.writeChars(send);
							toServer.flush();
						} 
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}
						
						
						MyDialog md = new MyDialog(ui,"��ʾ",true,"���ͳɹ�");
						md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
					else
					{
						MyDialog md = new MyDialog(ui,"��ʾ",true,"�������ݲ���Ϊ��");
						md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					}
				}
				else
				{
					MyDialog md = new MyDialog(ui,"��ʾ",true,"�û�������Ϊ��");
					md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
			}
			else
			{
				MyDialog md = new MyDialog(ui,"��ʾ",true,"���ȵ�¼");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

		}
	}
	private class zanBaidu implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String content = ui.text_area1.getText();
			if(content.length()>0)	
			{
				int i = Integer.parseInt(ui.send1.zan_count.getText());
				if (ui.send1.zan.getText().equals("��"))
				{
					ui.send1.zan.setText("ȡ����");
					i +=1;
					ui.send1.zan_count.setText(""+i);
					ui.send1.label.setVisible(true);
					wordinfo.Update(wordToSearch, 1, 0, 0);
						
				}
				else
				{
					ui.send1.zan.setText("��");
					i -= 1;
					ui.send1.zan_count.setText(""+i);
					ui.send1.label.setVisible(false);
					wordinfo.Update(wordToSearch, -1, 0, 0);
							
				}
			}
			else
			{
				MyDialog md = new MyDialog(ui,"��ʾ",true,"�޵�������");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
			
		}
	}
	
	
	
	private class zanYoudao implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String content = ui.text_area2.getText();
			if(content.length()>0)	
			{
				int i = Integer.parseInt(ui.send2.zan_count.getText());
				if (ui.send2.zan.getText().equals("��"))
				{
					ui.send2.zan.setText("ȡ����");
					i +=1;
					ui.send2.zan_count.setText(""+i);
					ui.send2.label.setVisible(true);
					wordinfo.Update(wordToSearch, 0, 1, 0);
						
				}
				else
				{
					i -= 1;
					ui.send2.zan_count.setText(""+i);
					ui.send2.zan.setText("��");
					ui.send2.label.setVisible(false);
					wordinfo.Update(wordToSearch, 0, -1, 0);
							
				}
			}
			else
			{
				MyDialog md = new MyDialog(ui,"��ʾ",true,"�޵�������");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		}
	}
	
	private class zanBing implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String content = ui.text_area3.getText();
			if(content.length()>0)	
			{
				int i = Integer.parseInt(ui.send3.zan_count.getText());
				if (ui.send3.zan.getText().equals("��"))
				{
					ui.send3.zan.setText("ȡ����");
					i += 1;
					ui.send3.zan_count.setText(""+i);
					ui.send3.label.setVisible(true);
					wordinfo.Update(wordToSearch, 0, 0, 1);
						
				}
				else
				{
					ui.send3.zan.setText("��");
					i -= 1;
					ui.send3.zan_count.setText(""+i);
					ui.send3.label.setVisible(false);
					wordinfo.Update(wordToSearch, 0, 0, -1);
							
				}
			}
			else
			{
				MyDialog md = new MyDialog(ui,"��ʾ",true,"�޵�������");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
			
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
