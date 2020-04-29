package procesareHtml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import clase.IndexDirectTemplate;

public class IndexDirect {

	private static IndexDirectTemplate getTemplateFile(String path, Writer writer) throws IOException{
		IndexDirectTemplate indexDirect = new IndexDirectTemplate();
		Writer inWriter = null;
		File file = new File(path);
		String filename = file.getName();

		String[] aux = filename.split("\\.");

		String absolutePath = file.getAbsolutePath();

		String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
		filename = aux[0];
		if(aux[1].equals("html")){
			indexDirect.setFileName(absolutePath);
			inWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath+"/"+filename+".txt"), "utf-8"));

			Parser.parsareInformatii(inWriter, file);

			File f = new File(filePath+"/"+filename+".txt");
			HashMap<String, Integer> word = Parser.parseText(f);
			indexDirect.setWords(word);
		
			writer.write(file.getName() + ":indexDirect.json\n");
			inWriter.close();
		}
		return indexDirect;
	}
	
	private static List<IndexDirectTemplate> getListOfFileTemplates(List<String> paths, Writer writer) throws IOException{
		List<IndexDirectTemplate> listOfFileTemplates = new ArrayList<IndexDirectTemplate>();
		for (String path : paths) {
			IndexDirectTemplate indexDirect = IndexDirect.getTemplateFile(path, writer);
			if(indexDirect.getFileName() != null && indexDirect.getWords() != null){
				listOfFileTemplates.add(indexDirect);
			}
		}
		return listOfFileTemplates;
	}

	public static void writeDirectJsonIndexFile(List<IndexDirectTemplate> list) throws IOException {
		Gson gs = new GsonBuilder().setPrettyPrinting().create();
		String strJson = gs.toJson(list);
		Writer writer = null;
		writer = new FileWriter("indexDirect.json");
		writer.write(strJson);
		writer.close();
	}
	
	public static void directIndex(String dirName) throws IOException {
		List<String> paths = Parser.getFiles(new File(dirName));
		Writer writer = null;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fisierMapare.txt"), "utf-8"));

		List<IndexDirectTemplate> listOfFileTemplates = getListOfFileTemplates(paths, writer);
		
		writeDirectJsonIndexFile(listOfFileTemplates);
		
		System.out.println("S-a creat fisierul pentru indexarea directa.");
		
		writer.close();
	}

}
