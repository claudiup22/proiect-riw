package procesareHtml;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import clase.Metadata;

public class Parser {
	
	public static void parsareInformatii(Writer writer,File input) throws IOException{
		Document doc = Jsoup.parse(input, "UTF-8");
		if(Data.getTitle(doc)!=null){
			String title = Data.getTitle(doc);
			writer.write(title);
		}
		
		List<Metadata> metadatas = Data.getMetadatas(doc);
		
		for (Metadata metadata : metadatas) {
			if(metadata.getName()!="robots"){
				writer.write(metadata.getContent());
			}
		}
		String text = Data.getText(doc);
		writer.write(text);
	}
	
	public static void parseLinks(Writer writer, File input) throws IOException{
		Document doc;
		
		doc = Jsoup.parse(input, null);
		HashSet<String> urls = Data.getLinks(doc);
        for (String element : urls) {
			writer.write(element+"\n");
		}
	}
	
	public static String citireFisier(String filename){
		String document = "";
		BufferedReader buffer = null;
		FileReader file = null;
		try {
			String  line ;
			file = new FileReader(new File(filename));
			buffer = new BufferedReader(file);
			while ((line = buffer.readLine()) != null) {
				document += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null)
					buffer.close();
				if (file != null)
					file.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return document;
	}
	
	public static HashMap<String, Integer> parseText(File file){
		HashMap <String,Integer> words = new HashMap<String,Integer>();
		String document = citireFisier(file.getAbsolutePath());
		Data.getWords(document, words);
		return words;
	}
	
	public static List<String> getFiles(File director) {
		 	
		List<String> paths = new ArrayList<String>();
		Stack<File> stack = new Stack<File>();
		stack.push(director);
		
		while(!stack.isEmpty()) {
			
			File child = stack.pop();
			if (child.isDirectory()) {
				for(File f : child.listFiles()) stack.push(f);
			} else if (child.isFile()) {
				paths.add(child.getPath());
			}
		}
		return paths;
	}
}
