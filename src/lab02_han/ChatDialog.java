package lab02_han;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
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

public class ChatDialog extends JFrame
{
	JTextArea jta2 = new JTextArea();
	JTextPane jtp = new JTextPane();  
	JButton jbt = new JButton("发送");
	JScrollPane jsp1 = new JScrollPane(jtp);
	JScrollPane jsp2 = new JScrollPane(jta2);
	
	public ChatDialog()
	{
		this.setResizable(false);
		jtp.setEditable(false);
		TitledBorder bd1 = new TitledBorder("对话框");
		TitledBorder bd2 = new TitledBorder("输入框");
		jsp1.setPreferredSize(new Dimension(500,300));
		jsp2.setPreferredSize(new Dimension(500,120));
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout(5,5));
	    p1.add(jsp1,BorderLayout.NORTH);
	    p1.add(jsp2,BorderLayout.SOUTH);
	    jsp1.setBorder(bd1);
	    jsp2.setBorder(bd2);
	    
	    JPanel p2 = new JPanel();
	    p2.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
	    p2.add(jbt);
	    add(p1,BorderLayout.NORTH);
	    add(p2,BorderLayout.SOUTH);
	}
	public   void   insert(String   str,   AttributeSet   attrSet)   
	{   
        Document   doc   =   jtp.getDocument();   
        try   
        {   
            doc.insertString(doc.getLength(),   str,   attrSet);   
        }   
        catch   (BadLocationException   e)   
        {   
            System.out.println("BadLocationException:   "   +   e);   
        }   
    }  
	
	public   void   setDocs(String str, Color col, boolean bold, int fontSize)   
	{   
        SimpleAttributeSet   attrSet   =   new   SimpleAttributeSet();   
        StyleConstants.setForeground(attrSet,   col);   
        //颜色   
        if(bold==true)
        {   
            StyleConstants.setBold(attrSet,   true);   
        }//字体类型   
        StyleConstants.setFontSize(attrSet,   fontSize);   
        //字体大小   
        insert(str,   attrSet);   
    }   
	protected void processWindowEvent(WindowEvent e)
	{
		
		 if (e.getID() == WindowEvent.WINDOW_CLOSING) 
		 {  
			 this.dispose();
		 }
		 else 
		 {    
			 super.processWindowEvent(e);      
		 }  
		
	}
	
	public static void main(String[] args) 
	{
		ChatDialog frame = new ChatDialog();
		frame.setTitle("Chat Room");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
