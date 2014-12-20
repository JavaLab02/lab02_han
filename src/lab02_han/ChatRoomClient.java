package lab02_han;



import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;





public class ChatRoomClient implements Runnable
{
	//Interface
	ChatFrame chatFrame;
	//IO stream
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	
	private Vector<String> onlineUserList;
	private Vector<ChatDialog> chatdialoglist;

	//�û���
	private String username;
	
	int piccount = 0;
	
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
		chatFrame.jbt2.addActionListener(new sendPicListener());

		
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
	private class sendPicListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			String path = chatFrame.sel.jtf.getText();
			String picname = username+"sendpic"+String.valueOf(piccount++);
			if (path!=null && path.length()>0)
			{
				try 
				{
					sendPic(path,picname);
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
			else
			{
				MyDialog md = new MyDialog(chatFrame,"��ʾ",true,"����ѡ���ļ�");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
				if (head=='0')
				{
					updateUserList(recv);
				}
				
				//������������Ϣ���������촰��
				else if(head=='2')
				{
					String[] temp = recv.split("&");
					if (temp[0].indexOf(username)!=-1)
					{
						chatFrame.setDocs(temp[0], Color.GREEN, true, 15);
						chatFrame.setDocs(temp[1], Color.BLACK, false, 18);
					}
					else
					{
						chatFrame.setDocs(temp[0], Color.BLUE, true, 15);
						chatFrame.setDocs(temp[1], Color.BLACK, false, 18);
					}
					
				}
				//����������Ϣ
				else if (head=='3')
				{
					handleLogOut(recv);
				}
				
				//receive picture
				//4len&picname&username*pic bytes
				else if (head=='4')
				{
					String[] temp=recv.split("&");
					
					if (temp[2].indexOf(username)!=-1)
					{
						chatFrame.setDocs(temp[2], Color.GREEN, true, 15);
					}
					else
					{
						chatFrame.setDocs(temp[2], Color.BLUE, true, 15);
					}
					
					try 
					{
						receivePic(temp[0],temp[1]);
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
	void updateDialog(String recv)
	{
		String[] names = recv.split(",");
		for (int i=0; i<names.length; i++)
		{
			this.onlineUserList.add(names[i]);
			//System.out.println(names[i]);
		}
	}
	
	void logout()
	{
		try 
		{
			toServer.writeChars("3*");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
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
	
	
	static byte[] image2Bytes(String imgSrc) throws Exception
	{
		FileInputStream fin = new FileInputStream(new File(imgSrc));
		//�������,������Ͳ�����̫��,���̫���Ҫ������취������һ�δ���̶�����byte[]
		byte[] bytes  = new byte[fin.available()];
		//���ļ�����д���ֽ����飬�ṩ���Ե�case
		fin.read(bytes);
		fin.close();
		return bytes;
	}
	void buff2Image(byte[] b,String tagSrc) throws Exception
	{
		FileOutputStream fout = new FileOutputStream(tagSrc);
		//���ֽ�д���ļ�
		fout.write(b);
		fout.close();
	}
	
	void sendPic(String path,String picname) throws Exception
	{
        File fi = new File(path);
        byte[] b = image2Bytes(path);
        int len = b.length;
        try 
        {	
        	toServer.writeChars("4");
			toServer.writeChars(len+"&"+picname+"*");
			toServer.write(b);
			toServer.flush();
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
       
       
	}
	void receivePic(String length,String picname) throws Exception
	{
		int len=Integer.parseInt(length);
		byte[] b = new byte[len];
		fromServer.readFully(b);
		String path = "./mypicReceived/"+picname+"_recv.jpg";
		buff2Image(b,path);
		chatFrame.setPic(path);
	}
	    
   
	
	public static void main(String[] args)
	{
		new ChatRoomClient("test2");
		
	}
	
}
