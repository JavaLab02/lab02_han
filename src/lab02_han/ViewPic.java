package lab02_han;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;





public class ViewPic extends JFrame
{
	
	JButton jbt = new JButton("open");
	JLabel label;
	JFileChooser chooser;
	JTextField TextField;
	public ViewPic()
	{
		setTitle("查看我的单词卡");
		TextField=new JTextField(30);
		label = new JLabel();
		this.setLayout(new FlowLayout());
		add(jbt);
		add(TextField);
		add(label);
		chooser = new JFileChooser("./mywordcard");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
    		   "请选择jpg图片文件", "jpg");//文件名过滤器
		chooser.setFileFilter(filter);
		jbt.addActionListener(new ActionListener() 
		{
          
			public void actionPerformed(ActionEvent arg0) {
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					String path = chooser.getSelectedFile().getPath();
					TextField.setText(path);
					ImageIcon pic = new ImageIcon(path);
					label.setIcon(pic);
					ViewPic.this.setSize(pic.getIconWidth(), pic.getIconHeight()+100); 
				}
			}
		}); 
      
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
	 public static void main(String[] args)
	    {
	    	ViewPic frame = new ViewPic();
	    	frame.setLocationRelativeTo(null);
	        frame.pack();
	        frame.setVisible(true);
	    }
  
}