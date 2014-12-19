package lab02_han;




import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;



@SuppressWarnings("serial")
public class UI extends JFrame
{
	
	 JButton sign_in = new JButton("登录");
	 JButton sign_up = new JButton("注册");
	 JButton log_out = new JButton("log out");
	 
	 JTextField input = new JTextField(25);
	 JButton search = new JButton("查询");
	
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
	 //用户列表
	 JScrollPane jsp4 = new JScrollPane(jlist);
	 JPanel p_result;

	
	 JPanel p_baidu = new JPanel();
	 JPanel p_youdao = new JPanel();
	 JPanel p_bing = new JPanel();
	 JPanel p1;
	 
	 Send send1 ;
	 Send send2;
	 Send send3 ;
	 
	 //存储赞的次数
	 int zan_baidu;
	 int zan_youdao;
	 int zan_bing;
	 
	 //单词卡列表相关变量
	 JButton jbtcardlist = new JButton("我的单词卡");
	 Vector <String> words = new Vector<String>();
	 //Vector <WordCard> cards = new Vector<WordCard>();
	 Vector <WordCardPic> cards = new Vector<>();
	 
	 JButton jbtchat = new JButton("聊天室");
	
	public UI()
	{
		this.setResizable(false);
		//设置自动换行
		text_area1.setLineWrap(true);
		text_area2.setLineWrap(true);
		text_area3.setLineWrap(true);
		//设置不可编辑
		text_area1.setEditable(false);	
		text_area2.setEditable(false);	
		text_area3.setEditable(false);	
		
		//单词卡列表
		
		p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p1.add(new JLabel("Online Dictionary"));
		p1.add(sign_in);
		p1.add(sign_up);
		
		baidu = new JCheckBox("百度词典");
	    baidu.setSelected(true);
		youdao = new JCheckBox("有道词典");
	    youdao.setSelected(true);
		bing = new JCheckBox("必应词典");
	    bing.setSelected(true);
	    check_state = 7; //初始化为7，即二进制111，表示3个全被选中
	    
	    JPanel p_check = new JPanel();
	    p_check.setLayout(new GridLayout(1,0));
	    p_check.add(baidu);
	    p_check.add(youdao);
	    p_check.add(bing);
	    
		TitledBorder bd1 = new TitledBorder("百度词典");
		TitledBorder bd2 = new TitledBorder("有道词典");
		TitledBorder bd3 = new TitledBorder("必应词典");
		TitledBorder bd4 = new TitledBorder("单词查询");
		TitledBorder bd5 = new TitledBorder("在线用户列表");
		TitledBorder bd6 = new TitledBorder("查询结果");
		
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
		
	
		
		
		send1 = new Send();
		send2 = new Send();
		send3 = new Send();
		
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
		
	
		
		jbtcardlist.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				final WordCardList temp = new WordCardList();
				temp.setTitle("我的单词卡");
				temp.setVisible(true);
				temp.setContent(words, cards);
				temp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				temp.pack();
			}
		});
		
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
		
	
		
	}
	
	public void online(String name)
	{
		JLabel jlabel = new JLabel(name);
		p1.removeAll();
		p1.add(new JLabel("Welcome !    "));
		p1.add(jlabel);
		p1.add(log_out);
		p1.add(jbtcardlist);
		p1.add(jbtchat);
		p1.validate();
		p1.repaint();
		
	}
	public void logout()
	{
		p1.removeAll();
		p1.add(new JLabel("Online Dictionary"));
		p1.add(sign_in);
		p1.add(sign_up);
		p1.validate();
		p1.repaint();
	}
	
	private void update_check(Object s)
	{
		//Component[] components = p_result.getComponents();
		//int len  = components.length;
		//LayoutManager layout = p_result.getLayout();
		if (s==baidu)
		{
			
			if (baidu.isSelected()) //变为选中
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
			else //变为未选中
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
					JLabel temp = new JLabel("未选中任何词典");
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
			if (youdao.isSelected()) //变为选中
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
			else //变为未选中
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
					JLabel temp = new JLabel("未选中任何词典");
					p_result.add(temp, BorderLayout.CENTER);
					p_result.validate();
					p_result.repaint();
				}
				
				
			
				
			}
		}
		else if(s==bing)
		{
			if (bing.isSelected()) //变为选中
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
			else //变为未选中
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
					JLabel temp = new JLabel("未选中任何词典");
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
	}
	
	
	

	//根据赞的次数排序
	public void  sort_by_zan(String[] str)
	{
		zan_baidu = Integer.parseInt(str[0]);
		zan_youdao = Integer.parseInt(str[1]);
		zan_bing = Integer.parseInt(str[2]);
		send1.updatezan(zan_baidu);
		send2.updatezan(zan_youdao);
		send3.updatezan(zan_bing);
		//state==111 
		
		switch (check_state)
		{
		//111
		case 7:
			if (zan_baidu>=zan_youdao&&zan_baidu>=zan_bing)
			{
				if (zan_youdao>=zan_bing)
				{
					p_result.removeAll();
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.add(p_bing, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else //调换有道和bing
				{
					p_result.removeAll();
					p_result.add(p_baidu, BorderLayout.NORTH);
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.add(p_youdao, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
			}
			else if (zan_youdao>=zan_baidu&&zan_youdao>=zan_bing)
			{
				if (zan_baidu>=zan_bing)
				{
					p_result.removeAll();
					p_result.add(p_youdao, BorderLayout.NORTH);
					p_result.add(p_baidu, BorderLayout.CENTER);
					p_result.add(p_bing, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else
				{
					p_result.removeAll();
					p_result.add(p_youdao, BorderLayout.NORTH);
					p_result.add(p_bing, BorderLayout.CENTER);
					p_result.add(p_baidu, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
			}
			else if (zan_bing>=zan_baidu&&zan_bing>=zan_youdao)
			{
				if (zan_baidu>=zan_youdao)
				{
					p_result.removeAll();
					p_result.add(p_bing, BorderLayout.NORTH);
					p_result.add(p_baidu, BorderLayout.CENTER);
					p_result.add(p_youdao, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
				else
				{
					p_result.removeAll();
					p_result.add(p_bing, BorderLayout.NORTH);
					p_result.add(p_youdao, BorderLayout.CENTER);
					p_result.add(p_baidu, BorderLayout.SOUTH);
					p_result.validate();
					p_result.repaint();
				}
			}
			break;
		//110	
		case 6:
			if (zan_baidu>=zan_youdao)
			{
				p_result.removeAll();
				p_result.add(p_baidu, BorderLayout.NORTH);
				p_result.add(p_youdao, BorderLayout.CENTER);
				p_result.validate();
				p_result.repaint();
			}
			else
			{
				p_result.removeAll();
				p_result.add(p_youdao, BorderLayout.NORTH);
				p_result.add(p_baidu, BorderLayout.CENTER);
				p_result.validate();
				p_result.repaint();
			}
			break;
		//101	
		case 5:
			if (zan_baidu>zan_bing)
			{
				p_result.removeAll();
				p_result.add(p_baidu, BorderLayout.NORTH);
				p_result.add(p_bing, BorderLayout.CENTER);
				p_result.validate();
				p_result.repaint();
			}
			else
			{
				p_result.removeAll();
				p_result.add(p_bing, BorderLayout.NORTH);
				p_result.add(p_baidu, BorderLayout.CENTER);
				p_result.validate();
				p_result.repaint();
			}
			break;
		//100	
		case 4: //do nothing
			break;
		//011	
		case 3:
			if (zan_youdao>=zan_bing)
			{
				p_result.removeAll();
				p_result.add(p_youdao, BorderLayout.NORTH);
				p_result.add(p_bing, BorderLayout.CENTER);
				p_result.validate();
				p_result.repaint();
			}
			else
			{
				p_result.removeAll();
				p_result.add(p_bing, BorderLayout.NORTH);
				p_result.add(p_youdao, BorderLayout.CENTER);
				p_result.validate();
				p_result.repaint();
			}
			break;
		//010	
		case 2: //do nothing
			break;
		//001	
		case 1: //do nothing
			break;
		//000	
		case 0: //do nothing
			break;
		default:;
			
		}
		
		
		
	}

	void updateSendList(Vector<String> vec)
	{
		send1.updateList(vec);
		send2.updateList(vec);
		send3.updateList(vec);
	}
	
	
	public void addWordCard(String word, WordCardPic card)
	{
		words.add(word);
		cards.add(card);
		
	}
	
	protected void processWindowEvent(WindowEvent e)
	{
		
		 if (e.getID() == WindowEvent.WINDOW_CLOSING) 
		 {  
			 if (JOptionPane.showConfirmDialog(this, "确实要关闭？", "确认", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
			 {
				 System.exit(0);  
			 } 
			 else 
			 {  
	            //do nothing
			 }  
	        } 
		 else 
		 {    
			 super.processWindowEvent(e);      
		 }  
		
	}
	

}
