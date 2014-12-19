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

	//�û���
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
				
				//����Ϊ��
				if (word==null || word.replace(" ", "").equals(""))
				{
					MyDialog md = new MyDialog(chatFrame ,"��ʾ",true,"���벻��Ϊ��");
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
	
	
	//�������������IO����
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
			System.err.println(ex+" δ�����Ϸ�����");
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
				//��ȡ�������ַ�
				head = fromServer.readChar();
				//��ȡʣ���ַ�
				while ( (ch = fromServer.readChar()) != '*')
				{
					recv += ch;
				}
				
				//�����û��б�
				if (head=='1')
				{
					updateUserList(recv);
				}
				//������Ϣ���������촰��
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
				//����������Ϣ
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
			MyDialog md = new MyDialog(chatFrame ,"��ʾ",true,"Log out!�����ʺ��������ص��¼");
			md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			chatFrame.dispose();
		}
	}
	
	public static void main(String[] args)
	{
		new ChatRoomClient("test2");
		
	}
	
}
