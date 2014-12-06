package lab02_han;

public class GetTranslation {
	
	public static String getBing(String word){
		String url = "http://cn.bing.com/dict/search?q=";
		TranslateOnline bing = new TranslateOnline();
		String content = "";
		String[] wordgroup = word.split(" ");
		String newWord = "";
		if(wordgroup.length > 1){
			newWord = newWord+wordgroup[0].trim();
			for(int i = 1;i < wordgroup.length; i++)
				newWord = newWord+"+"+wordgroup[i].trim();
			content = bing.fetchBing(url+newWord.trim());
		}else{
			content = bing.fetchBing(url+word.trim());
		}
		return content;
	}
	
	public static String getBaidu(String word){
		String url = "http://dict.baidu.com/s?wd=";
		TranslateOnline baidu = new TranslateOnline();
		String content = "";
		String[] wordgroup = word.split(" ");
		String newWord = "";
		if(wordgroup.length > 1){
			newWord = newWord+wordgroup[0].trim();
			for(int i = 1;i < wordgroup.length; i++)
				newWord = newWord+"+"+wordgroup[i].trim();
			//System.out.println(newWord);
			content = baidu.fetchBaidu(url+newWord.trim());
		}else{
			content = baidu.fetchBaidu(url+word.trim());
		}
		return content;
	}
	
	public static String getYoudao(String word){
		String url = "http://dict.youdao.com/search?q=";
		TranslateOnline youdao = new TranslateOnline();
		String content = "";
		String[] wordgroup = word.split(" ");
		String newWord = "";
		if(wordgroup.length > 1){
			newWord = newWord+wordgroup[0].trim();
			for(int i = 1;i < wordgroup.length; i++)
				newWord = newWord+"+"+wordgroup[i].trim();
			content = youdao.fetchYoudao(url+newWord.trim());
		}else{
			content = youdao.fetchYoudao(url+word.trim());
		}
		return content;
	}
}
