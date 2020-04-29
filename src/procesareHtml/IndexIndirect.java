package procesareHtml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import clase.IndexDirectTemplate;
import clase.IndexIndirectTemplate;

public class IndexIndirect {
	private static List<IndexIndirectTemplate> reverse(String filename){
		List<IndexIndirectTemplate> list = new ArrayList<IndexIndirectTemplate>();
        Gson gson = new Gson();
        String json = Parser.citireFisier(filename);
        List<IndexDirectTemplate> listOfFiles = gson.fromJson(json, new TypeToken<List<IndexDirectTemplate>>() {}.getType());
        for (IndexDirectTemplate di : listOfFiles) {
			for (Map.Entry<String,Integer> word : di.getWords().entrySet()) {
				IndexIndirectTemplate wt = new IndexIndirectTemplate();
				wt.setWord(word.getKey());
				wt.add(di.getFileName(), word.getValue()); 
				list.add(wt);
			}
		}
        return list;
	}
	
	private static List<IndexIndirectTemplate> gather(List<IndexIndirectTemplate> list){
		List<IndexIndirectTemplate> finalList = new ArrayList<IndexIndirectTemplate>();
		finalList.add(list.get(0));
		int last = 0;
		for (IndexIndirectTemplate wordsTemplate : list) {
			if(finalList.get(last).getWord().equals(wordsTemplate.getWord())){
				finalList.get(last).getDocs().putAll(wordsTemplate.getDocs());
			}
			else{
				finalList.add(wordsTemplate);
				++last;
			}
		}
		return finalList;
	}

	private static void sort(List<IndexIndirectTemplate> list) {
        list.sort(Comparator.comparing(IndexIndirectTemplate::getWord));
    }
	
	public static void writeIndirectJsonIndexFile(List<IndexIndirectTemplate> list) {
		Gson gs = new GsonBuilder().setPrettyPrinting().create();
		String strJson = gs.toJson(list);
		Writer writer = null;
		try {
			writer = new FileWriter("indexIndirect.json");
			writer.write(strJson);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Integer> getDocsFromWord(String word, List<IndexIndirectTemplate> list)
	{
		HashMap<String, Integer> docs = new HashMap<String, Integer>();
		for (IndexIndirectTemplate wordsTemplate : list) {
			if(word==wordsTemplate.getWord())
			{
				docs = wordsTemplate.getDocs();
			}
			else
			{
				System.out.println("Nu exista asa ceva!");
				return null;
			}
		}
		return docs;
	}
	public static void indirectIndex(){
		List<IndexIndirectTemplate> list = IndexIndirect.reverse("indexDirect.json");

		sort(list);
		List<IndexIndirectTemplate> finalList = gather(list);
		writeIndirectJsonIndexFile(finalList);
		System.out.println("S-a creat fisierul pentru indexarea indirecta.");

	}

}
