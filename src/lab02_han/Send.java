package lab02_han;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.*;
@SuppressWarnings("serial")
public class Send extends JPanel
{
	//JTextField input = new JTextField(20);
	Vector<String> list;
	JComboBox<String> input;
	JButton send = new JButton("发送单词卡");
	JButton zan = new JButton("赞");
	JLabel label = new JLabel("已赞");
	JLabel zan_count = new JLabel("0");
	
	public Send()
	{
		list=new Vector<String>();
		list.add("*All user");
		input = new JComboBox<String>(list);
		input.setPreferredSize(new Dimension(200,20));
		this.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		label.setForeground(Color.RED);
		zan_count.setForeground(Color.RED);
		add(new JLabel("用户名:"));
		//add(input);
		add(input);
		add(send);
		add(zan);
		add(label);
		add(zan_count);
		add(label);
		label.setVisible(false);
		zan_count.setVisible(false);
		
	}
	public void updatezan(int n)
	{
		zan_count.setVisible(true);
		zan_count.setText(""+n);
	}
	public void initial()
	{
		zan.setText("赞");
		zan_count.setVisible(false);
		label.setVisible(false);
	}
	public void updateList(Vector<String> vec)
	{
		list.removeAllElements();
		for (String s: vec)
		{
			list.add(s);
		}
		list.add("*All user");
		input.validate();
		
		
	}
	

}