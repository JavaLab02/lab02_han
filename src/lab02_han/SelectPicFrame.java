package lab02_han;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelectPicFrame extends JPanel
{
    JTextField jtf = new JTextField(30);
    JButton jbt=new JButton("ѡ��ͼƬ");
    
    public SelectPicFrame()
    {
    	 jbt.addActionListener(new ActionListener()
    	 {
             public void actionPerformed(ActionEvent e) 
             {
               
            	 JFileChooser chooser = new JFileChooser("./");
            	 FileNameExtensionFilter filter = new FileNameExtensionFilter("��ѡ��ͼƬ�ļ�", "png", "jpg");//�ļ���������
                 chooser.setFileFilter(filter);
                 int returnVal = chooser.showOpenDialog(jtf);
                 if(returnVal == JFileChooser.APPROVE_OPTION) {
                     jtf.setText(chooser.getSelectedFile().getAbsolutePath());
                 }
             }
         });
    	 this.setLayout(new FlowLayout());
    	 this.add(jbt);
    	 this.add(jtf);
    }
    /*
    public static void main(String args[]){
    	SelectPicFrame f = new SelectPicFrame();
    	f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
       
    }
   */
}
