package lab02_han;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ChatFrame extends JFrame
{
	JTextArea jta2 = new JTextArea();
	 JTextPane jtp = new JTextPane();  
	JButton jbt = new JButton("发送文字");
	JButton jbt2 = new JButton("发送图片");
	JLabel space = new JLabel("     "); 
	SelectPicFrame sel = new SelectPicFrame();
	Vector<String> user_list = new Vector<String>();
	JList<String> jlist = new JList<String>(user_list);
	
	JScrollPane jsp1 = new JScrollPane(jtp);
	JScrollPane jsp2 = new JScrollPane(jta2);
	JScrollPane jsp3 = new JScrollPane(jlist);
	
	ChatFrame()
	{
		this.setResizable(false);
		jtp.setEditable(false);
		TitledBorder bd1 = new TitledBorder("对话框");
		TitledBorder bd2 = new TitledBorder("输入框");
		TitledBorder bd3 = new TitledBorder("用户列表");
		
		jta2.setLineWrap(true);
		
		jsp1.setPreferredSize(new Dimension(500,440));
		jsp2.setPreferredSize(new Dimension(500,160));
		jsp3.setPreferredSize(new Dimension(200,605));
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout(5,5));
	    p1.add(jsp1,BorderLayout.NORTH);
	    p1.add(jsp2,BorderLayout.SOUTH);
	    jsp1.setBorder(bd1);
	    jsp2.setBorder(bd2);
	    jsp3.setBorder(bd3);
	    JPanel p2 = new JPanel();
	    p2.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
	    p2.add(sel);
	    p2.add(jbt2);
	    p2.add(space);
	    p2.add(jbt);
	    
	    JPanel p3 = new JPanel();
	    p3.setLayout(new BorderLayout(5,5));
	    p3.add(p1,BorderLayout.NORTH);
	    p3.add(p2,BorderLayout.SOUTH);
	    
	    JPanel p4 = new JPanel();
	    p4.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
	    p4.add(jsp3);
	    
	    add(p3, BorderLayout.WEST);
	    add(p4, BorderLayout.EAST);
	    this.setVisible(false);
	    /*
	    jlist.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					//int index = jlist.getSelectedIndex();
					String name = jlist.getSelectedValue(); 
					final ChatDialog frame = new ChatDialog();
					frame.setTitle(name);
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
					
				
				}
			}
		);
	    */
	}
	
	protected void processWindowEvent(WindowEvent e)
	{
		
		 if (e.getID() == WindowEvent.WINDOW_CLOSING) 
		 {  
			 this.setVisible(false); 
		 }
		 else 
		 {    
			 super.processWindowEvent(e);      
		 }  
		
	}
	public void updateUserList(Vector<String> list)
	{
		user_list.removeAllElements();
		jlist.setListData(list);
	}
	
	public void insert(String   str,   AttributeSet   attrSet)   
	{   
        Document  doc = jtp.getDocument();   
        try   
        {   
            doc.insertString(doc.getLength(),   str,   attrSet);   
        }   
        catch   (BadLocationException   e)   
        {   
            System.out.println("BadLocationException:   "   +   e);   
        }   
    }  
	
	public void setDocs(String str, Color col, boolean bold, int fontSize)   
	{   
        SimpleAttributeSet attrSet = new SimpleAttributeSet();   
        StyleConstants.setForeground(attrSet, col);   
        //颜色   
        if(bold==true)
        {   
            StyleConstants.setBold(attrSet, true);   
        }//字体类型   
        StyleConstants.setFontSize(attrSet, fontSize);   
        //字体大小   
        insert(str, attrSet);   
    }   
	public void setPic(String path)
	{
		 Icon image = new ImageIcon(path);
		 Document doc = jtp.getDocument();
		 jtp.setCaretPosition(doc.getLength());
		 jtp.insertIcon(image);
		 SimpleAttributeSet attrSet = new SimpleAttributeSet(); 
		 try {
			doc.insertString(doc.getLength(), "\n",attrSet);
		} catch (BadLocationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		 
	}
	
	
	public static void main(String[] args) 
	{
		ChatFrame chatFrame = new ChatFrame();
		chatFrame.setTitle("Chat Room");
		chatFrame.pack();
		chatFrame.setLocationRelativeTo(null);
		//chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setVisible(true);
	}

}
