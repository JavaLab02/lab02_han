package lab02_han;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TranslateOnline {
	private CloseableHttpClient client;
	private BasicCookieStore cookieStore = null;// 存储cookie用
	public TranslateOnline(){
		//PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();//多线程
	    //cm.setMaxTotal(5);
		cookieStore = new BasicCookieStore();
		client = HttpClients.custom().setDefaultCookieStore(cookieStore)
				.build();
	}
	public BasicCookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(BasicCookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	/**
	 * 从respose 中获取响应文本信息
	 * 
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public String getContentGBK(CloseableHttpResponse response) throws IOException {
		HttpEntity entity=null;
		//获取响应状态码和响应内容
		if(response.getStatusLine().getStatusCode()>=200&&response.getStatusLine().getStatusCode()<300)
		  entity = response.getEntity();
		String content = null;
		try {
			if(entity!=null){
				content = EntityUtils.toString(entity, "GBK");
				EntityUtils.consume(entity);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(entity!=null){
				entity.getContent().close();
			}
			response.close();
			entity=null;//防止内存泄露
			response=null;
		}
		return content;
	}

	/**
	 * 从respose 中获取响应文本信息
	 * 
	 * @param response
	 * @return
	 */
	public synchronized String  getContentUTF(CloseableHttpResponse response) {
		HttpEntity entity=null;
		int code=response.getStatusLine().getStatusCode();
		if(code>=200&&code<300)
		  entity = response.getEntity();
		String content = null;
		try {
			if(entity!=null){
				content = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(entity!=null){
					entity.getContent().close();
				}
				response.close();
				entity=null;//防止内存泄露
				response=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}
	
	public CloseableHttpResponse getRequest(CloseableHttpClient client, String url)throws ClientProtocolException, IOException {
	
		//Http request URIS包含协议名、主机名、主机端口（可选）、资源路径、query（可选）和片段信息（可选）
		RequestConfig requestConfig = RequestConfig.custom()
		        .setSocketTimeout(600000)
		        .setConnectTimeout(600000)
		        .setConnectionRequestTimeout(600000)
		        .build();
		
		HttpGet get = new HttpGet(url.trim());
		get.setConfig(requestConfig);
		get.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36");
		
		CloseableHttpResponse response = client.execute(get);
		return response;
	}
	
	public String fetchBing(String url){
		String content = "";
		String noteOfWord = "";
		String sentenceEnglish = "";
		String sentenceChinese = "";
		try{
			content = getContentGBK(getRequest(client, url.trim()));
			Document doc = Jsoup.parse(content);
			//单词释义部分
			if(doc!=null){
				Element element = null;
				if(doc.getElementsByAttributeValue("class", "qdef") != null)
					element = doc.getElementsByAttributeValue("class", "qdef").first();
					//单词的词性
				if(element != null){
					Elements classOfWord = element.getElementsByTag("li");;
					if(classOfWord != null){
						Iterator iter = classOfWord.iterator();
						while(iter.hasNext()){
							noteOfWord = noteOfWord +((Element)iter.next()).text() + "\n";
						}
					}
					//以上是单词释义部分
					//单词的例句部分
					Element exampleElement = null;
					if(doc.getElementsByAttributeValue("class", "de_seg")!=null)
						exampleElement = doc.getElementsByAttributeValue("class", "de_seg").first();
					Element sentenceElement = null;
					if(exampleElement!=null){
						if(exampleElement.getElementsByAttributeValue("class","li_ex")!=null)
							sentenceElement = exampleElement.getElementsByAttributeValue("class","li_ex").first();
					}
					if(sentenceElement!=null){
						if(sentenceElement.getElementsByAttributeValue("class", "val_ex")!=null)
							sentenceEnglish = sentenceElement.getElementsByAttributeValue("class", "val_ex").first().text();
						if(sentenceElement.getElementsByAttributeValue("class", "bil_ex")!=null)
							sentenceChinese = sentenceElement.getElementsByAttributeValue("class", "bil_ex").first().text();
					}
					if(sentenceEnglish.length() > 0||sentenceChinese.length() > 0)
						noteOfWord = noteOfWord + "\n"+ "权威英汉双解例句\n"+sentenceEnglish + "\n" + sentenceChinese;
					System.out.println(noteOfWord);
				}else{
					noteOfWord = "词典中没有与您搜索的关键词匹配的内容";
					System.out.println(noteOfWord);
				}
				
			}else{
				noteOfWord = "词典中没有与您搜索的关键词匹配的内容";
				//System.out.println(noteOfWord);
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return noteOfWord;
	}
	
	public String fetchBaidu(String url){
		String content = "";
		String noteOfWord = "";
		String sentenceEnglish = "";
		String sentenceChinese = "";
		try{
			content = getContentGBK(getRequest(client, url.trim()));
			Document doc = Jsoup.parse(content);
			//单词部分
			if(doc != null){
				Element element = doc.getElementsByAttributeValue("class", "tab en-simple-means dict-en-simplemeans-english").first();
					//单词的词性
				if(element != null){
					Elements classOfWord = element.getElementsByTag("p");
					if(classOfWord != null){
						Iterator iter = classOfWord.iterator();
						while(iter.hasNext()){
							noteOfWord = noteOfWord +((Element)iter.next()).text() + "\n";
						}
					}
					//以上是单词部分
					//例句部分
					Element exampleElement = null;
					if(doc.getElementsByAttributeValue("class", "tab en-collins dict-en-collins-english") != null)
						exampleElement = doc.getElementsByAttributeValue("class", "tab en-collins dict-en-collins-english").first();
					Element sentenceElement = null;
					if(exampleElement != null){
						if(exampleElement.getElementsByTag("li") != null)
							sentenceElement = exampleElement.getElementsByTag("li").first();
					}
					if(sentenceElement != null){
						if(sentenceElement.getElementsByTag("li") != null){
							if(sentenceElement.getElementsByTag("li").first().getElementsByTag("p") != null){
								sentenceEnglish = sentenceElement.getElementsByTag("li").first().getElementsByTag("p").first().text();
								sentenceChinese = sentenceElement.getElementsByTag("li").first().getElementsByTag("p").get(1).text();
							}
						}
					}
					if(sentenceEnglish.length() > 0||sentenceChinese.length() > 0)
						noteOfWord = noteOfWord + "\n"+ "柯林斯高阶英汉词典/例句\n"+sentenceEnglish + "\n" + sentenceChinese;
					System.out.println(noteOfWord);
				}
				else{
					noteOfWord = "词典中没有与您搜索的关键词匹配的内容";
					System.out.println(noteOfWord);
				}
			}
			else{
				noteOfWord = "词典中没有与您搜索的关键词匹配的内容";
				//System.out.println(noteOfWord);
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return noteOfWord;
	}
	
	public String fetchYoudao(String url){
		String content = "";
		String noteOfWord = "";
		try{
			content = getContentGBK(getRequest(client, url.trim()));
			Document doc = Jsoup.parse(content);
			if(doc != null){
				//单词部分
				Element element = null;
				if(doc.getElementsByAttributeValue("class", "trans-container") != null)
					element = doc.getElementsByAttributeValue("class", "trans-container").first();
				if(element!=null){
					//单词的词性
					Elements classOfWord = element.getElementsByTag("li");
					if(classOfWord != null){
						Iterator iter = classOfWord.iterator();
						while(iter.hasNext()){
							noteOfWord = noteOfWord +((Element)iter.next()).text()+"\n";
						}
					}
					Element additional = null;
					if(element.getElementsByAttributeValue("class", "additional") != null){
						additional = element.getElementsByAttributeValue("class", "additional").first();
						if(additional!=null)
							noteOfWord = noteOfWord + additional.text();
					}
//					/System.out.println(noteOfWord);
				}else{
					noteOfWord = "无释义";
				}
				//例句部分
				Element additionalSentenceEnglish = null;
				Element additionalSentenceChinese = null;
				if(doc.getElementsByAttributeValue("class", "additional")!=null){
					if(doc.getElementsByAttributeValue("class", "additional").size() > 3){
						additionalSentenceEnglish = doc.getElementsByAttributeValue("class", "additional").get(1);
						additionalSentenceChinese = doc.getElementsByAttributeValue("class", "additional").get(2);
						noteOfWord = "\n" + noteOfWord + "\n" +additionalSentenceEnglish.text() + "\n" +additionalSentenceChinese.text();
					}
				}
				System.out.println(noteOfWord);
			}else{
				noteOfWord = "词典中没有与您搜索的关键词匹配的内容";
				//System.out.println(noteOfWord);
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return noteOfWord;
	}

}
