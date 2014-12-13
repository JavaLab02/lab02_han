package lab02_han;




import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class UI extends JFrame
{
	
	 JButton sign_in = new JButton("��¼");
	 JButton sign_up = new JButton("ע��");
	
	 JTextField input = new JTextField(25);
	 JButton search = new JButton("��ѯ");
	
	 JCheckBox baidu;
	 JCheckBox youdao;
	 JCheckBox bing;
	 int check_state;
	
	 Vector<String> user_list = new Vector<String>();
	 JList<String> jlist = new JList<String>(user_list);
	
	 JTextArea text_area1 = new JTextArea();
	 JTextArea text_area2 = new JTextArea();
	 JTextArea text_area3 = new JTextArea();
	
	 JScrollPane jsp1 = new JScrollPane(text_area1);
	 JScrollPane jsp2 = new JScrollPane(text_area2);
	 JScrollPane jsp3 = new JScrollPane(text_area3);
	 //�û��б�
	 JScrollPane jsp4 = new JScrollPane(jlist);
	 JPanel p_result;

	
	 JPanel p_baidu = new JPanel();
	 JPanel p_youdao = new JPanel();
	 JPanel p_bing = new JPanel();
	
	
	public UI()
	{
		//�����Զ�����
		text_area1.setLineWrap(true);
		text_area2.setLineWrap(true);
		text_area3.setLineWrap(true);
				
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p1.add(new JLabel("Online Dictionary"));
		p1.add(sign_in);
		p1.add(sign_up);
		
		baidu = new JCheckBox("�ٶȴʵ�");
	    baidu.setSelected(true);
		youdao = new JCheckBox("�е��ʵ�");
	    youdao.setSelected(true);
		bing = new JCheckBox("��Ӧ�ʵ�");
	    bing.setSelected(true);
	    check_state = 7; //��ʼ��Ϊ7����������111����ʾ3��ȫ��ѡ��
	    
	    JPanel p_check = new JPanel();
	    p_check.setLayout(new GridLayout(1,0));
	    p_check.add(baidu);
	    p_check.add(youdao);
	    p_check.add(bing);
	    
		TitledBorder bd1 = new TitledBorder("�ٶȴʵ�");
		TitledBorder bd2 = new TitledBorder("�е��ʵ�");
		TitledBorder bd3 = new TitledBorder("��Ӧ�ʵ�");
		TitledBorder bd4 = new TitledBorder("���ʲ�ѯ");
		TitledBorder bd5 = new TitledBorder("�����û��б�");
		TitledBorder bd6 = new TitledBorder("��ѯ���");
		
		JPanel p_search = new JPanel();
		p_search.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p_search.add(new JLabel("Input"));
		p_search.add(input);
		p_search.add(search);
		p_search.add(p_check);
		p_search.setBorder(bd4);
		
		JPanel p_north = new JPanel();
		p_north.setLayout(new BorderLayout(5,5));
		p_north.add(p1,BorderLayout.NORTH);
		p_north.add(p_search,BorderLayout.CENTER);
		//p_north.add(p_check,BorderLayout.SOUTH);
		
		
		p_result = new JPanel();
		//p_result.setLayout(new GridLayout(3,1));
		p_result.setLayout(new BorderLayout(0,0));
		
		
		p_baidu.setLayout(new BorderLayout(5,5));
		p_youdao.setLayout(new BorderLayout(5,5));
		p_bing.setLayout(new BorderLayout(5,5));
		p_baidu.setBorder(bd1);
		p_youdao.setBorder(bd2);
		p_bing.setBorder(bd3);
		
	
		
		
		final Send send1 = new Send();
		final Send send2 = new Send();
		final Send send3 = new Send();
		
		p_baidu.add(jsp1, BorderLayout.NORTH);
		p_baidu.add(send1, BorderLayout.SOUTH);
		p_youdao.add(jsp2, BorderLayout.NORTH);
		p_youdao.add(send2, BorderLayout.SOUTH);
		p_bing.add(jsp3, BorderLayout.NORTH);
		p_bing.add(send3, BorderLayout.SOUTH);
		
		p_result.add(p_baidu, BorderLayout.NORTH);
		p_result.add(p_youdao, BorderLayout.CENTER);
		p_result.add(p_bing, BorderLayout.SOUTH);
		p_result.setBorder(bd6);
		
		jsp1.setPreferredSize(new Dimension(600,140));
		jsp2.setPreferredSize(new Dimension(600,140));
		jsp3.setPreferredSize(new Dimension(600,140));
		
		
		jsp4.setBorder(bd5);
		jsp4.setPreferredSize(new Dimension(200,450));
		
		
	
		JPanel p_south = new JPanel();
		p_south.setLayout(new BorderLayout(5,5));
		p_south.add(jsp4, BorderLayout.WEST);
		//p_south.add(p_check, BorderLayout.SOUTH);
		p_south.add(p_result, BorderLayout.CENTER);
		
		
		
		add(p_north,BorderLayout.NORTH);
		add(p_south,BorderLayout.SOUTH);
		
		
		
		baidu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				update_check(source);
			}
		});
		
		youdao.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				update_check(source);
			}
		});
	
		bing.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				update_check(source);
			}
		});
		
		send1.send.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MyDialog md = new MyDialog(UI.this,"��ʾ",true,"���ͳɹ�");
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		send1.zan.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (send1.zan.getText().equals("��"))
				{
					send1.zan.setText("ȡ����");
					send1.label.setVisible(true);
				
				}
				else
				{
					send1.zan.setText("��");
					send1.label.setVisible(false);
					
				}
			}
		});
		
		send2.send.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MyDialog md = new MyDialog(UI.this,"��ʾ",true,"���ͳɹ�");
				md.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
		send2.zan.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (send2.zan.getText().equals("��"))
				{
					send2.zan.setText("ȡ����");
					send2.label.setVisible(true);
				
				}
				else
				{
					send2.zan.setText("��");
					send2.label.setVisible(false);
					
				}
			}
		});
		
		send3.send.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MyDialog md = new MyDialog(UI.this,"��ʾ",true,"���ͳɹ�");
				md.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
		send3.zan.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (send3.zan.getText().equals("��"))
				{
					send3.zan.setText("ȡ����");
					send3.label.setVisible(true);
				
				}
				else
				{
					send3.zan.setText("��");
					send3.label.setVisible(false);
					
				}
			}
		});
		
		
	}
	
	private void update_check(Object s)
	{
		//Component[] components = p_result.getComponents();
		//int len  = components.length;
		//LayoutManager layout = p_result.getLayout();
		if (s==baidu)
		{
			
			if (baidu.isSelected()) //��Ϊѡ��
			{
				if (check_state==3)//011
				{
					check_state=7;//111
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,140));
					jsp2.setPreferredSize(new Dimension(600,140));
					jsp3.setPreferredSize(new Dimension(600,140));
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.add(p_bing, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==2) //010
				{
					check_state=6; //110
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,244));
					jsp2.setPreferredSize(new Dimension(600,244));
					
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.CENTER);
					
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==1) //001
				{
					check_state=5; //101
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,244));
					
					jsp3.setPreferredSize(new Dimension(600,244));
					p_result.add(p_baidu, BorderLayout.NORTH);
					
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if(check_state==0)//000
				{
					check_state=4; //100
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,560));
					p_result.add(p_baidu, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				
			}
			else //��Ϊδѡ��
			{
				
				if(check_state==7) // 111
				{
					check_state = 3; //011
					p_result.removeAll();
					jsp2.setPreferredSize(new Dimension(600,244));
					jsp3.setPreferredSize(new Dimension(600,244));
					p_result.add(p_youdao, BorderLayout.NORTH);
					p_result.add(p_bing, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==6)// 110
				{
					check_state = 2; // 010
					p_result.removeAll();
					jsp2.setPreferredSize(new Dimension(600,560));
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==5)// 101
				{
					check_state = 1; // 001
					p_result.removeAll();
					jsp3.setPreferredSize(new Dimension(600,560));
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==4)//100
				{
					check_state = 0; // 000
					p_result.removeAll();
					JLabel temp = new JLabel("δѡ���κδʵ�");
					p_result.add(temp, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				
				
			
				
			}
			
		}
		else if(s==youdao)
		{
			/*
			text_area2.setText("");
			if (youdao.isSelected())
			{
				text_area2.append("selected");
			}
			else
			{
				text_area2.append("unselected");
			}
			text_area2.paintImmediately(text_area2.getBounds());
			*/
			if (youdao.isSelected()) //��Ϊѡ��
			{
				/*
				*101->111
				*100->110
				*001->011
				*000->010
				*/
				if (check_state==5)//101
				{
					check_state=7;//111
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,140));
					jsp2.setPreferredSize(new Dimension(600,140));
					jsp3.setPreferredSize(new Dimension(600,140));
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.add(p_bing, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==4) //100
				{
					check_state=6; //110
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,244));
					jsp2.setPreferredSize(new Dimension(600,244));
					
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.CENTER);
					
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==1) //001
				{
					check_state=3; //011
					p_result.removeAll();
					jsp2.setPreferredSize(new Dimension(600,244));
					
					jsp3.setPreferredSize(new Dimension(600,244));
					p_result.add(p_youdao, BorderLayout.NORTH);
					
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==0)//000
				{
					check_state=2; //010
					p_result.removeAll();
					jsp2.setPreferredSize(new Dimension(600,560));
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				
			}
			else //��Ϊδѡ��
			{
				
				if(check_state==7) // 111
				{
					check_state = 5; //101
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,244));
					jsp3.setPreferredSize(new Dimension(600,244));
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_bing, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==6)// 110
				{
					check_state = 4; // 100
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,560));
					p_result.add(p_baidu, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==3)// 011
				{
					check_state = 1; // 001
					p_result.removeAll();
					jsp3.setPreferredSize(new Dimension(600,560));
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state == 2)//010
				{
					check_state = 0; // 000
					p_result.removeAll();
					JLabel temp = new JLabel("δѡ���κδʵ�");
					p_result.add(temp, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				
				
			
				
			}
		}
		else if(s==bing)
		{
			if (bing.isSelected()) //��Ϊѡ��
			{
				/*
				*110-111
				*100-101
				*010-011
				*000-001
				*/
				if (check_state==6)//110
				{
					check_state=7;//111
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,140));
					jsp2.setPreferredSize(new Dimension(600,140));
					jsp3.setPreferredSize(new Dimension(600,140));
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.add(p_bing, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==4) //100
				{
					check_state=5; //101
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,244));
					jsp3.setPreferredSize(new Dimension(600,244));
					
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_bing, BorderLayout.CENTER);
					
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==2) //010
				{
					check_state=3; //011
					p_result.removeAll();
					jsp2.setPreferredSize(new Dimension(600,244));
					jsp3.setPreferredSize(new Dimension(600,244));
					p_result.add(p_youdao, BorderLayout.NORTH);
					
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==0)//000
				{
					check_state=1; //001
					p_result.removeAll();
					jsp3.setPreferredSize(new Dimension(600,560));
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				
			}
			else //��Ϊδѡ��
			{
				/*
				 * 111->110
				 * 101->100
				 * 011->010
				 * 001->000
				 */
				if(check_state==7) // 111
				{
					check_state = 6; //110
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,244));
					jsp2.setPreferredSize(new Dimension(600,244));
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==5)// 101
				{
					check_state = 4; // 100
					p_result.removeAll();
					jsp1.setPreferredSize(new Dimension(600,560));
					p_result.add(p_baidu, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==3)// 011
				{
					check_state = 2; // 010
					p_result.removeAll();
					jsp2.setPreferredSize(new Dimension(600,560));
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				else if (check_state==1)//001
				{
					check_state = 0; // 000
					p_result.removeAll();
					JLabel temp = new JLabel("δѡ���κδʵ�");
					p_result.add(temp, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				
			}
		}
		
		
	}
	
	public void updateUserList(Vector<String> list)
	{
		user_list.removeAllElements();
		
		jlist.setListData(list);
		for (String str: list)
		{
			System.out.println(str);
		}
		
		
	}
	
	/*
	public static void main(String[] args) 
	{
		UI frame = new UI();
		frame.setTitle("Online Dictionary");
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	*/

}
