package lab02_han;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetTranslation {
	private String baiduTrans = "";
	private String youdaoTrans = "";
	private String bingTrans = "";
	private String word = "";
	public GetTranslation(String word){
		this.word = word;
	}
	
	public void translate(){
		GetBingTranslation bing = new GetBingTranslation(word);
		GetBaiduTranslation baidu = new GetBaiduTranslation(word);
		GetYoudaoTranslation youdao = new GetYoudaoTranslation(word);
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.execute(bing);
		executor.execute(baidu);
		executor.execute(youdao);
		executor.shutdown();
		while(!executor.isTerminated()){}
		bingTrans = bing.getBingTranslation();
		baiduTrans = baidu.getBaiduTranslation();
		youdaoTrans = youdao.getYoudaoTranslation();
	}
	
	public String getBingTranslation(){
		return bingTrans;
	}
	public String getBaiduTranslation(){
		return baiduTrans;
	}
	public String getYoudaTranslation(){
		return youdaoTrans;
	}
}
