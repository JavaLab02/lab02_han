package lab02_han;

public class GetBingTranslation implements Runnable{
	private String word = "";
	private String content = "";
	public GetBingTranslation(String word){
		this.word = word;
	}
	
	public void setWord(String word){
		this.word = word;
	}
	
	public String getBingTranslation(){
		//System.out.println(content);
		return content;
	}
	public void run(){
		String url = "http://cn.bing.com/dict/search?q=";
		TranslateOnline bing = new TranslateOnline();
		String[] wordgroup = word.split(" ");
		String newWord = "";
		if(wordgroup.length > 1){
			newWord = newWord+wordgroup[0].trim();
			for(int i = 1;i < wordgroup.length; i++)
				newWord = newWord+"+"+wordgroup[i].trim();
			this.content = bing.fetchBing(url+newWord.trim());
		}else{
			this.content = bing.fetchBing(url+word.trim());
		}
		//System.out.println(content);
	}
}
