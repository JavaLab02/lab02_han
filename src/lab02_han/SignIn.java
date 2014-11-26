package lab02_han;



//实现登录窗口
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SignIn extends JFrame
{
	public JTextField account = new JTextField(30);
	public JTextField password = new JTextField(30);
	public JButton jbt1 = new JButton("登录");
	public JButton jbt2 = new JButton("取消");
	public TitledBorder bd1 = new TitledBorder("账户");
	public TitledBorder bd2 = new TitledBorder("密码");
	
	public SignIn() 
	{
		
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p1.add(account);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p2.add(password);
		
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER,20,5));
		p3.add(jbt1);
		p3.add(jbt2);
		
		p1.setBorder(bd1);
		p2.setBorder(bd2);
		
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		add(p3, BorderLayout.SOUTH);
		
	}

}
