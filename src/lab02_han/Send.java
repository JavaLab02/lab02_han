package lab02_han;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;
@SuppressWarnings("serial")
public class Send extends JPanel
{
	JTextField input = new JTextField(20);
	JButton send = new JButton("发送单词卡");
	JButton zan = new JButton("赞");
	JLabel label = new JLabel("已赞");
	
	public Send()
	{
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		label.setForeground(Color.RED);
		add(new JLabel("用户ID:"));
		add(input);
		add(send);
		add(zan);
		add(label);
		label.setVisible(false);
		
	}
	

}