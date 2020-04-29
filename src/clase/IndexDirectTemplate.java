package clase;

import java.util.HashMap;

public class IndexDirectTemplate {
	String numeFisier;

	HashMap<String, Integer> cuvinte;
	public IndexDirectTemplate() {
		numeFisier = null;
		cuvinte = null;
	}

	public IndexDirectTemplate(String numeFisier, HashMap<String, Integer> cuvinte) {
		this.numeFisier = numeFisier;
		this.cuvinte = cuvinte;
	}

	public String getFileName() {
		return numeFisier;
	}

	public void setFileName(String numeFisier) {
		this.numeFisier = numeFisier;
	}

	public HashMap<String, Integer> getWords() {
		return cuvinte;
	}

	public void setWords(HashMap<String, Integer> words) {
		this.cuvinte = words;
	}
}
