package lab02_han;
public class TestTranslate {
	public static void main(String[] args){
		String word = "nanjing university";
		//获取bing在线翻译
		String bing = GetTranslation.getBing(word);
		System.out.println("Bing:\n"+bing+"\n");
		//获取百度在线翻译
		String baidu = GetTranslation.getBaidu(word);
		System.out.println("Baidu:\n"+baidu+"\n");
		
		//获取有道在线翻译
		String youdao = GetTranslation.getYoudao(word);
		System.out.println("Youdao:\n"+youdao+"\n");
	}
}
