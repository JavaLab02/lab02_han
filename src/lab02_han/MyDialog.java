package lab02_han;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyDialog extends JDialog
{
	
	public MyDialog(JFrame f, String title , boolean bool, String contains)
	{
		super(f,title,bool);
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JLabel jlb = new JLabel(contains);
		jlb.setFont(new   Font("×ÖÌå",   1,   13)); 
		JButton ok = new JButton("È·¶¨");
		p1.add(jlb);
		p2.add(ok);
		
		this.add(p1, BorderLayout.NORTH);
		this.add(p2, BorderLayout.SOUTH);
		
		
		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MyDialog.this.dispose();
			}
		});
		
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);  
        this.setLocationRelativeTo(null);
        this.setSize(300, 150);  
        this.setVisible(true);
         
		
	}
	
	

}