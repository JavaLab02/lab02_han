package lab02_han;
public class TestTranslate {
	public static void main(String[] args){
		String word = "Hello";
		//获取bing在线翻译
		//String bing = GetTranslation.getBing(word);
		
		//System.out.println("("+bing+")");
		//获取百度在线翻译
		String baidu = GetTranslation.getBaidu(word);
		System.out.println(baidu);
		//获取有道在线翻译
		String youdao = GetTranslation.getYoudao(word);
	}
}
