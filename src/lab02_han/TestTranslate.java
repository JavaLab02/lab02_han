package lab02_han;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTranslate {
	public static void main(String[] args){
		String w = "dictionary";
		GetTranslation word = new GetTranslation(w);
		word.translate();
		System.out.println("�ٶȷ���:\n"+word.getBaiduTranslation()+"\n�е����룺\n"+word.getYoudaTranslation()+"\n��Ӧ���룺\n"+word.getBingTranslation());
		
	}
}
