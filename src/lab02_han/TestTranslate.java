package lab02_han;
public class TestTranslate {
	public static void main(String[] args){
		String word = "nanjing university";
		//��ȡbing���߷���
		String bing = GetTranslation.getBing(word);
		System.out.println("Bing:\n"+bing+"\n");
		//��ȡ�ٶ����߷���
		String baidu = GetTranslation.getBaidu(word);
		System.out.println("Baidu:\n"+baidu+"\n");
		
		//��ȡ�е����߷���
		String youdao = GetTranslation.getYoudao(word);
		System.out.println("Youdao:\n"+youdao+"\n");
	}
}
