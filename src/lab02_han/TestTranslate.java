package lab02_han;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTranslate {
	public static void main(String[] args){
		String w = "dictionary";
		GetTranslation word = new GetTranslation(w);
		word.translate();
		System.out.println("百度翻译:\n"+word.getBaiduTranslation()+"\n有道翻译：\n"+word.getYoudaTranslation()+"\n必应翻译：\n"+word.getBingTranslation());
		
	}
}
