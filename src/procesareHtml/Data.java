package procesareHtml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import stemmer.Stemmer;
import clase.Metadata;

public class Data {
	private static File exceptions;
	private static HashSet<String> exception;
	private static File stop;
	private static HashSet<String> stopwords;
	public Data() throws IOException {
		exceptions = new File("exceptions.txt");
		exception = new HashSet<String>();
		stop = new File ("stopwords.txt");
		stopwords = new HashSet<String>();
		BufferedReader bufferReader = null;
		FileReader fileReader = null;
		String line;
		fileReader = new FileReader(exceptions);
		bufferReader = new BufferedReader(fileReader);
		while ((line = bufferReader.readLine()) != null) {
			exception.add(line);
		}

		fileReader = new FileReader(stop);
		bufferReader = new BufferedReader(fileReader);
		while ((line = bufferReader.readLine()) != null) {
			stopwords.add(line);
		}

		if (bufferReader != null)
			bufferReader.close();
		if (fileReader != null)
			fileReader.close();
		
	}
	
	public static String getTitle(Document doc ){
		if(!doc.title().isEmpty()) {
			return doc.title() + "\n";
		}
		else {
			return null;
		}
	}
	

	public static HashSet<String> getLinks(Document doc){
		HashSet<String> urls = new HashSet<String>();
		Elements href = doc.select("a");
		for (Element hre : href)
		{
			String URL = hre.attr("abs:href");
			if(URL.contains("#")){
				int index = URL.indexOf("#");
				if(URL.substring(0,index).length() != 0){
					urls.add(hre.text() + "-> " + URL.substring(0, index));
				}

			}
			else{
				urls.add(hre.text() + "-> " +URL);
			}
		}
		return urls;
	}
	public static String getText(Document doc){
		return doc.body().text();
	}
	public static boolean isException(String word){
		return exception.contains(word);
	}
	public static boolean isStopWord(String word){
		return stopwords.contains(word);
	}
	
	public static List<Metadata> getMetadatas(Document doc){
		List<Metadata> metadatas = new ArrayList<Metadata>();
		
		Elements metaTags = doc.select("meta");
		for (Element metatag : metaTags) 
		{
			if(metatag.hasAttr("name")) {
				String name = metatag.attr("name").toLowerCase();
				if( name.equals("keywords") || name.equals("description") || name.equals("robots")){
					metadatas.add( new Metadata(name,metatag.attr("content")));
				}
			}
		}
		return metadatas;
	}
	
	public static void getWords(String text, HashMap<String,Integer> hashMap){
		char c;
		String word = "";
		
		int len = text.length();
		
		for(int i = 0; i < len; i++){
			c = text.charAt(i);
			
			if((c<'a'|| c>'z') && ( c<'A' || c>'Z') && (c<'0' || c>'9')){
				if(word != ""){
					if(isException(word)){
						if(hashMap.containsKey(word)){
							word = word.toLowerCase();
							hashMap.put(word, hashMap.get(word) + 1);
						}
						else{
							word = word.toLowerCase();
							hashMap.put(word, 1);
						}
						word="";
					}
					else {
						word = word.toLowerCase();
						if(isStopWord(word)){
							word="";
						}
						else{
							Stemmer s = new Stemmer();
							char[] chs = word.toCharArray();
							s.add(chs, chs.length);
							s.stem();
							word = s.toString();
							if(hashMap.containsKey(word)){
								hashMap.put(word, hashMap.get(word) + 1);
							}
							else{
								hashMap.put(word, 1);
							}
							word="";
						}
					}
				}
			}
			else{
				word += c;
			}
		}
	}
}
