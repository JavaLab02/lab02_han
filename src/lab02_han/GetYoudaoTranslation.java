package lab02_han;

public class GetYoudaoTranslation implements Runnable{
	private String word = "";
	private String content = "";
	public GetYoudaoTranslation(String word){
		this.word = word;
	}
	
	public void setWord(String word){
		this.word = word;
	}
	
	public String getWord(){
		return word;
	}
	
	public String getYoudaoTranslation(){
		return content;
	}
	public void run(){
		String url = "http://dict.youdao.com/search?q=";
		TranslateOnline youdao = new TranslateOnline();
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
		//System.out.println(content);
	}

}
