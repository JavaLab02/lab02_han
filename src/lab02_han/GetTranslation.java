package lab02_han;

public class GetTranslation {
	
	public static String getBing(String word){
		String url = "http://cn.bing.com/dict/search?q=";
		TranslateOnline bing = new TranslateOnline();
		String content = "";
		content = bing.fetchBing(url+word);
		return content;
	}
	
	public static String getBaidu(String word){
		String url = "http://dict.baidu.com/s?wd=";
		TranslateOnline bing = new TranslateOnline();
		String content = "";
		content = bing.fetchBaidu(url+word);
		return content;
	}
	
	public static String getYoudao(String word){
		String url = "http://dict.youdao.com/search?q=";
		TranslateOnline bing = new TranslateOnline();
		String content = "";
		content = bing.fetchYoudao(url+word);
		return content;
	}
}
