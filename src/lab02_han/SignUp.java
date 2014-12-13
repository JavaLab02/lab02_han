package lab02_han;


//实现注册窗口
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SignUp extends JFrame
{
	public JTextField account = new JTextField(30);
	public JTextField password = new JTextField(30);
	public JTextField confirmPassword = new JTextField(30);
	public JButton jbt1 = new JButton("注册");
	public JButton jbt2 = new JButton("取消");
	public TitledBorder bd1 = new TitledBorder("账户");
	public TitledBorder bd2 = new TitledBorder("密码");
	public TitledBorder bd3 = new TitledBorder("确认密码");
	
	public SignUp() 
	{
		
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p1.add(account);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p2.add(password);
		
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p3.add(confirmPassword);
		
		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.CENTER,20,5));
		p4.add(jbt1);
		p4.add(jbt2);
		
		p1.setBorder(bd1);
		p2.setBorder(bd2);
		p3.setBorder(bd3);
		
		this.setLayout(new GridLayout(4,1));
		add(p1);
		add(p2);
		add(p3);
		add(p4);
		
	}

}