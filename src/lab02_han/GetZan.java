package lab02_han;



public class GetZan {
	private int zanBaidu = 0;
	private int zanYoudao = 0;
	private int zanBing = 0;
	
	private String word = "";
	public GetZan(String word){
		this.word = word;
	}
	
	public boolean isInDB(){
		DBWordInfo db = new DBWordInfo();
		String zanInfo = db.Search(word);
		String[] group = zanInfo.split("&");
		if(group.length > 2){
			zanBaidu = Integer.parseInt(group[0]);
			zanYoudao = Integer.parseInt(group[1]);
			zanBing = Integer.parseInt(group[2]);
			if(zanBaidu == 0 && zanYoudao == 0 && zanBing == 0)
				return false;
			else
				return true;
		}
		else
			return false;
	}
	
	public int getzanBaidu(){
		return zanBaidu;
	}
	
	public int getzanYoudao(){
		return zanYoudao;
	}
	
	public int getzanBing(){
		return zanBing;
	}
}
