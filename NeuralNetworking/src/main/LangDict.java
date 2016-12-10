package main;


import java.util.List;
import java.util.ArrayList;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class LangDict {

	static String dir = "/home/henry/workspace/NeuralNetworking/src/main/Dictionaries/";

	public int count;
	public String language;
	public List<String> words;

	public void loadFile(){
		Scanner input = null;
		try {
			input = new Scanner(new File(dir+language));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (input.hasNext()){
			boolean valid = true;
			//Read Word
			String word = input.nextLine();

			//Can't be too long
			if (word.length() > 16) continue;
			
			//Check for all valid characters
			for (int i=0;i<word.length();i++){
				int c = (int)word.charAt(i);
				if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) continue;
				valid = false;
			}
			if (valid) words.add(word);
		}

		count = words.size();
	}//loadFile method

	public String getWord(int index){
		return words.get(index);
	}

	public LangDict(){
		count = 0;
		language = "Not Specified";
		words = new ArrayList<String>();
	}
	
	public LangDict(String lang){
		this.count = 0;
		this.language = lang;
		words = new ArrayList<String>();

		loadFile();
	}

	public String toString(){
		String str = "";
		for (String s: words) str += s+"\n";
		return str;
	}
}
