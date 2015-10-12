package spellchecker;

import java.io.*;
import java.util.*;


/**
 * 
 * @author bernard, inspired by Norvic
 * Checks misspelt words and write the right word to file.
 *
 */

public class SpellChecker {
	private static Hashtable<String, String> dictionary;
	private static BufferedWriter bw;
	
	public static void main(String[] args){
		dictionary = new Hashtable<String, String>();
	
		readDictionary(args[1]);
		getWords(args[0]);
		
		
	}
	
	// reads the dictionary into the program
	private static void readDictionary(String wordBank){
		BufferedReader br = null;
		try {
			 br = new BufferedReader(new FileReader(new File(wordBank)));
		    String line;
		    while ((line = br.readLine()) != null) {
				String word = line.toLowerCase();
				dictionary.put(word, word);
		    }
		    System.out.println(wordBank);
		} catch (IOException e) {
			
			e.printStackTrace();
		}	    
	}
	
	
	
	
	//finds a word close to the wrongly spelt word
	private static String correct(String word) {
        ArrayList<String> list = edits(word);
        HashMap<String, String> candidates = new HashMap<String, String>();
        
        for(String s : list) 
        	if(dictionary.containsKey(s)) candidates.put(dictionary.get(s),s);
        
        if(candidates.size() > 0) 
        	return candidates.get(Collections.max(candidates.keySet()));
        
        for(String s : list) 
        	for(String w : edits(s)) 
        		if(dictionary.containsKey(w)) candidates.put(dictionary.get(w),w);
        
        return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
    }

	
	
	private static void getWords(String document){
		BufferedReader br;
		try {
			outputText();
			 br = new BufferedReader(new FileReader(new File(document)));
		    String line;
		    while ((line = br.readLine()) != null) {
				String[] words = line.split(" ");
				for (String word : words) {
					boolean isCapitalized = !(word.equals(word.toLowerCase()));
					word = word.toLowerCase();
					if (dictionary.contains(word) || word.matches(".*[.,()]")) {
						bw.write((isCapitalized ? capitalize(word) : word)+" ");
					}else{
						String fin = correct(word);
						bw.write((isCapitalized ? capitalize(fin) : fin)+ " ");
					}
				}
				bw.newLine();
		    }

			bw.close();
			
			System.out.println(document);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String capitalize(final String line) {
	   return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	//write the corrected word to file
	private static void outputText(){
		File file = new File("document-corrected.txt");
		try {
			if (!file.exists())
				file.createNewFile();
			 bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private final static ArrayList<String> edits(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0; i < word.length(); ++i) 
        	result.add(word.substring(0, i) + word.substring(i+1));
        for(int i=0; i < word.length()-1; ++i) 
        	result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
        for(int i=0; i < word.length(); ++i) 
        	for(char c='a'; c <= 'z'; ++c) 
        		result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
        for(int i=0; i <= word.length(); ++i) 
        	for(char c='a'; c <= 'z'; ++c) 
        		result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
        return result;
    }
}