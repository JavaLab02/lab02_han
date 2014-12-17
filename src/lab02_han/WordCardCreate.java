package lab02_han;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class WordCardCreate {
	BufferedImage image;
	
	int no = 1;
	
	int width = 128;  
    int height = 64; 
    
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
		        // ����BufferedImage����  
		        Font font=new Font("����",Font.PLAIN,16);  
		        image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
		        // ��ȡGraphics2D  
		        Graphics2D g2d = image.createGraphics();  
		        // ��ͼ  
		        g2d.setBackground(new Color(255,255,255));  
		        g2d.setPaint(new Color(2,2,2));  
		        g2d.clearRect(0, 0, width, height);    
		        g2d.drawString(word, 0, 10);
		        g2d.drawString(definitions, 0, 30);
		        g2d.setFont(font);   
		        //�ͷŶ���  
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
