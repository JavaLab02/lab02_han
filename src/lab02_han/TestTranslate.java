package lab02_han;
public class TestTranslate {
	public static void main(String[] args){
		String word = "Hello";
		//��ȡbing���߷���
		//String bing = GetTranslation.getBing(word);
		
		//System.out.println("("+bing+")");
		//��ȡ�ٶ����߷���
		String baidu = GetTranslation.getBaidu(word);
		System.out.println(baidu);
		//��ȡ�е����߷���
		String youdao = GetTranslation.getYoudao(word);
	}
}
