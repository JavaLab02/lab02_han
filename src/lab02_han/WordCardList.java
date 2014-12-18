package lab02_han;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WordCardList extends JFrame
{
	Vector <String> words = new Vector<String>();
	Vector <WordCardPic> cards = new Vector<WordCardPic>();
	//Vector <WordCard> cards = new Vector<WordCard>();
	JList<String> list = new JList<String>(words);
	JScrollPane jsp = new JScrollPane(list);
	public WordCardList()
	{
		TitledBorder bd = new TitledBorder("单词卡列表");
		jsp.setBorder(bd);
		add(jsp);
		jsp.setPreferredSize(new Dimension(300,600));
		list.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					int index = list.getSelectedIndex();
					
					final WordCardPic card = cards.elementAt(index);
					card.setTitle("单词卡");
					card.pack();
					card.setLocationRelativeTo(null);
					card.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					card.setVisible(true);
					card.setSize(400, 400);
				
				}
			}
		);
	}
	/*
	public void setContent(Vector<String> vec1, Vector<WordCard> vec2)
	{
		words.removeAllElements();
		cards.removeAllElements();
		words.addAll(vec1);
		cards.addAll(vec2);
		list.setListData(words);
	}
	*/
	public void setContent(Vector<String> vec1, Vector<WordCardPic> vec2)
	{
		words.removeAllElements();
		cards.removeAllElements();
		words.addAll(vec1);
		cards.addAll(vec2);
		list.setListData(words);
	}
		
	public static void main(String[] args) 
	{
		WordCardList temp = new WordCardList();
		temp.setTitle("我的单词卡");
		temp.setVisible(true);
		temp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		temp.pack();
		//Vector <String> vec = new Vector<String>();
		//vec.add("111");
		//vec.add("222");
		//vec.add("333");
		//temp.setContent(vec);
		WordCard card = new WordCard();
		card.setContent("test", "测试");
		
	
		
		
	}
	
}
