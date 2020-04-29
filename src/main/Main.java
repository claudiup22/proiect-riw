package main;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import procesareHtml.Data;
import procesareHtml.IndexDirect;
import procesareHtml.IndexIndirect;
import search.CautareBooleana;

public class Main {

	public static void main(String[] args) throws IOException{
		@SuppressWarnings("unused")
		Data d = new Data();

		Scanner in = new Scanner(System.in);
		System.out.println("Apasa: \n 1 - indexare directa\n 2 - indexare indirecta\n 3 - cautare booleana");
		int num = in.nextInt();
		switch(num)
		{
			case 1: 
				IndexDirect.directIndex("date");
				break;
			case 2:
				IndexIndirect.indirectIndex();
				break;
			case 3:
				String query = CautareBooleana.readQueryFromUser();
			    HashSet<String> rez = CautareBooleana.booleanSearch(query);
			    System.out.println((rez.size()==0)?"Nu sunt documente":rez + "\ncount:" + rez.size());
				break;
			default:
				System.out.println("Te rog introdu alta optiune");
				break; 
		}
		in.close();
	}
}
