package lab02_han;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class WordCardPic extends JFrame
{
	public JTextArea jta = new JTextArea();
	public JScrollPane jsp = new JScrollPane(jta);
	public TitledBorder bd = new TitledBorder("Word Card");
	public CardComponent card;
	public WordCardCreate wordCard;
	public Image image;
	public JPanel p = new JPanel();
	public WordCardPic(String word, String definitions)
	{
		this.setResizable(false);
		wordCard = new WordCardCreate(word,definitions);
		image =  wordCard.createCard();
		card = new CardComponent(image,word);
		add(card);
		
		
	}
	public static void main(String[] args) 
	{
		WordCardPic temp = new WordCardPic("hello","你好");
		temp.setTitle("单词卡");
		temp.setVisible(true);
		temp.setSize(400, 400);
		temp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}

}

class CardComponent extends JPanel{
	private int width = 400;
	private int height = 400;
	private Image image;
//	public TitledBorder bd = new TitledBorder("Word Card");
	public TitledBorder bd;
	public CardComponent(Image image,String word)
	{
		bd = new TitledBorder(word);
		Font font1 = new Font("Default",Font.PLAIN,30);
		bd.setTitleFont(font1);
		setBorder(bd);
		this.image = image;
	}
	protected void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(image, 0, 0, width,height, this);
	}
	

}


class WordCardCreate {
	BufferedImage image;
	
	int no = 1;
	
	int width = 400;  
    int height = 400; 
    
    String word;
    String definitions;
    
    Image imageCreate;
	public WordCardCreate(String word,String definitions){
		this.word = word;
		this.definitions = definitions;
	}
	
	public Image createCard(){
		  try  
		    {  
		        // 创建BufferedImage对象  
		        Font font=new Font("Default",Font.PLAIN,20);  
		        image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
		        // 获取Graphics2D  
		        Graphics2D g2d = image.createGraphics();  
		        // 画图  
		        g2d.setFont(font);
		        g2d.setBackground(new Color(255,255,255));  
		        g2d.setPaint(new Color(0,0,0));  
		        g2d.clearRect(0, 0, width, height);    
//		        g2d.drawString(word, 0, 60);
		        g2d.drawString(definitions, 5, 60);
		        g2d.setFont(font);   
		        //释放对象  
		        g2d.dispose();  
		        return image;
		    }  
		    catch(Exception ex)  
		    {  
		        ex.printStackTrace();  
		    }
		  return null;
	}
}
