package lab02_han;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;
@SuppressWarnings("serial")
public class Send extends JPanel
{
	JTextField input = new JTextField(20);
	JButton send = new JButton("���͵��ʿ�");
	JButton zan = new JButton("��");
	JLabel label = new JLabel("����");
	JLabel zan_count = new JLabel("0");
	
	public Send()
	{
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		label.setForeground(Color.RED);
		zan_count.setForeground(Color.RED);
		add(new JLabel("�û�ID:"));
		add(input);
		add(send);
		add(zan);
		add(label);
		add(zan_count);
		add(label);
		label.setVisible(false);
		
	}
	public void updatezan(int n)
	{
		zan_count.setText(""+n);
	}
	

}