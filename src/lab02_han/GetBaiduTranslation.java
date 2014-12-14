package lab02_han;

public class GetBaiduTranslation implements Runnable{
	private String word = "";
	private String content = "";
	public GetBaiduTranslation(String word){
		this.word = word;
	}
	
	public void setWord(String word){
		this.word = word;
	}
	
	public String getWord(){
		return word;
	}
	
	public String getBaiduTranslation(){
		return content;
	}
	public void run(){
		String url = "http://dict.baidu.com/s?wd=";
		TranslateOnline baidu = new TranslateOnline();
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
		//System.out.println(content);
	}
}
