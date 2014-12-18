package lab02_han;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class WordCard extends JFrame
{
	public JTextArea jta = new JTextArea();
	public JScrollPane jsp = new JScrollPane(jta);
	public TitledBorder bd = new TitledBorder("Word Card");
	public WordCard()
	{
		Font font1 = new Font("Default",Font.PLAIN,30);
		Font font2 = new Font("Default",Font.PLAIN,20);
	
		bd.setTitleFont(font1);
		jta.setFont(font2);
		jsp.setBorder(bd);
		jsp.setPreferredSize(new Dimension(480,270));
		jta.setLineWrap(true);
		jta.setEditable(false);
		this.add(jsp);
		
	}
	public static void main(String[] args) 
	{
		WordCard temp = new WordCard();
		temp.setTitle("µ¥´Ê¿¨");
		temp.setVisible(true);
		temp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		temp.pack();
		temp.setContent("hello", "ÄãºÃ");
		
	}
	public void setContent(String word, String definitions)
	{
		bd.setTitle(word);
		jta.setText(definitions);
	}

}

