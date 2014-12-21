package lab02_han;
//package lab02_han;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
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
		/*
		String word = "minus";
		GetTranslation trans = new GetTranslation(word);
		trans.translate();
		String note = trans.getBingTranslation();
		
		WordCardPic temp = new WordCardPic(word,note);
		*/
		WordCardPic temp = new WordCardPic("Test_baidu","test....");
		temp.setTitle("单词卡");
		temp.setVisible(true);
		temp.setSize(400, 440);
		temp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}

}

class CardComponent extends JPanel{
	private int width = 400;
	private int height = 400;
	private Image image;
	public CardComponent(Image image,String word)
	{
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
    String pic_name ;
    Image imageCreate;
	public WordCardCreate(String word,String definitions){
		pic_name = word;
		this.word = word.split("_")[0];
		this.definitions = definitions;
		
	}
	
	public Image createCard(){
		  try  
		    {  
		        Image background = ImageIO.read(new File("./mybackground/background"+(int)(Math.random()*3)+".jpg"));
		        image = new BufferedImage(background.getWidth(null),background.getHeight(null),BufferedImage.TYPE_INT_RGB);
		        Graphics2D g2d = (Graphics2D)image.getGraphics();
		        g2d.drawImage(background, 0, 0, background.getWidth(null), background.getHeight(null), null);
		        g2d.setColor(Color.black);
		        g2d.setFont(new Font("宋体",Font.BOLD,30));
		        //------------单词居中处理-------------------
		        int length = word.length();
		        int pos = 175 - length*12/2;
		        
		        g2d.drawString(word, pos, 70);
		        g2d.setFont(new Font("宋体",Font.PLAIN,15));
		        String[] definition = definitions.split("\n");
		        int ypos = 100;
		        for(int i = 0;i < definition.length;i++){
		        	if(definition[i].contains("；")){
		        		if(definition[i].length() < 21){
				        	g2d.drawString(definition[i], 100, ypos);
				        	ypos = ypos + 30;
			        	}else{
			        		String subStr1 = definition[i].substring(0, 19);
			        		String subStr2 = definition[i].substring(20);
			        		g2d.drawString(subStr1, 100, ypos);
				        	ypos = ypos + 30;
				        	g2d.drawString(subStr2, 100, ypos);
				        	ypos = ypos + 30;
			        	}
		        	}else{
			        	if(definition[i].length() < 29){
				        	g2d.drawString(definition[i], 100, ypos);
				        	ypos = ypos + 30;
			        	}else{
			        		String subStr1 = definition[i].substring(0, 28);
			        		String subStr2 = definition[i].substring(29);
			        		g2d.drawString(subStr1, 100, ypos);
				        	ypos = ypos + 30;
				        	g2d.drawString(subStr2, 100, ypos);
				        	ypos = ypos + 30;
			        	}
		        	}
		        }
//		        g2d.drawString(definitions, 5, 60);
		        g2d.drawImage(image, 0, 0, null);
		        
		       
		        
		        //释放对象  
		        g2d.dispose();
		        ImageIO.write(image, "jpg", new File("./mywordcard/local/"+pic_name+".jpg"));
		        return image;
		    }  
		    catch(Exception ex)  
		    {  
		        ex.printStackTrace();  
		    }
		  return null;
	}
	
}
