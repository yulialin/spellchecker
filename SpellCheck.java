/**
   Create words to solve a jumble problem of one sequence
   of characters.
   @author Yulia Yueer Lin 
 */

import java.io.*;   // for IO
import java.util.ArrayList;
import java.util.Arrays;

public class SpellCheck {
    public static final int NOT_FOUND = -1;
    // default values for dictionary and document
    private static String dictFilename = "./";
    private static String docFilename = "dictionary.txt";
    private String [] dictionary; // Array of words in dictionary.

   // private static long CompCount;

    /*
      Load dictionary used for spell checking
      File must contain one word per line.
    */
    public SpellCheck(String filename) {
		ArrayList<String> words = new ArrayList<String>();
		FileReader filereader = null;
	
		try {
	   	 filereader = new FileReader(filename);
	} catch (FileNotFoundException ex) {
	    System.err.println(ex);
	    return;
	}
	BufferedReader reader = new BufferedReader(filereader);
	String s;
	try {
	    while ( (s = reader.readLine()) != null ) {
			words.add( s );
	    }
	    // convert arraylist to an array
	    this.dictionary = words.toArray(new String [1]);
	} catch (IOException ex) {
	    System.err.println("Error reading " + filename);
	    return;
	}
    }

    /*
      Return ith element from dictionary.
    */
    public String get(int i) { return dictionary[i]; }

    /**
       @return index in list of words of spell checker.
    */
    public int indexOf(String key) {
        if (key == null)
	  	  throw new IllegalArgumentException("argument to indexOf() is null");
		//CompCount++;
		return indexOf(dictionary,key,0,dictionary.length-1);
    }

    /**
       @return index in array of key or NOT_FOUND if not found
       Uses String compareTo method.
    */
    private static int indexOf(String[] arr, String key, int lo, int hi) {
	// TODO
		int mid = lo + (hi - lo) / 2;
		if (lo > hi) return NOT_FOUND;

		else if (key.compareTo(arr[mid]) < 0) return indexOf(arr,key,lo,mid-1);
		else if (key.compareTo(arr[mid]) > 0) return indexOf(arr, key, mid+1, hi);
		else return mid;

    }

 /**
       Places all permutations of s into strings.
       
       @param prefix will be concatenated to the permutations of s to
       form new strings.
       @param strings will contain all strings of length
       (prefix.length + s.length).  They are added in the base case of
       perm.

    */
    private static void perm(String prefix,String s, ArrayList<String> strings) {
		int n = s.length();
		if (n == 0) { // base case
		    strings.add(prefix + s);
		}
		else {
	    // TODO
			for (int i = 0; i < n; i++) {
				perm(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, n), strings);

			}
		}
  	  }
    
    /**
       Return ArrayList of all permutations of s.
    */
    public static ArrayList<String> permutations(String s) {
		ArrayList<String> strings = new ArrayList<String>();
		perm("",s,strings);
		return strings;
    }

	/**takes a word and an integer n as input and returns a list of n-grams of length n
	*/
    public static List<String> generateNgrams(String word, int n) {
   	 List<String> ngrams = new ArrayList<String>();
   	 for(int i = 0; i < word.length()-n+1; i++) {
      	 	 ngrams.add(word.substring(i, i + n));
   			 }
 	   return ngrams;
    }
	
	
   public static void checkSpelling(String filename) throws IOException {
  	  List<String> dictionary = readDictionary();
   	  List<String> words = readFile(filename);

        for (String word : words) {
           if (!dictionary.contains(word)) {
             System.out.println("Misspelled word: " + word);
             List<String> suggestions = suggestCorrectSpelling(word, dictionary);
             System.out.println("Suggestions: " + suggestions);
           }
         }
        }

   public static List<String> suggestCorrectSpelling(String word, List<String> dictionary) {
   	 int n = 3; // use trigrams
   	 int threshold = 2; // maximum edit distance
    	Map<String, Integer> ngramCounts = new HashMap<>();
   	 List<String> suggestions = new ArrayList<>();

   	 // Compute n-gram counts for the dictionary
       for (String dictWord : dictionary) {
         for (int i = 0; i <= dictWord.length() - n; i++) {
            String ngram = dictWord.substring(i, i + n);
            int count = ngramCounts.getOrDefault(ngram, 0);
            ngramCounts.put(ngram, count + 1);
        }
    }

    // Generate n-grams for the misspelled word and compute edit distance to dictionary words
      Map<String, Integer> misspelledNgrams = new HashMap<>();
      for (int i = 0; i <= word.length() - n; i++) {
         String ngram = word.substring(i, i + n);
         int count = misspelledNgrams.getOrDefault(ngram, 0);
           misspelledNgrams.put(ngram, count + 1);
          }
      for (String dictWord : dictionary) {
        if (Math.abs(dictWord.length() - word.length()) <= n) {
            Map<String, Integer> dictNgrams = new HashMap<>();
            for (int i = 0; i <= dictWord.length() - n; i++) {
                String ngram = dictWord.substring(i, i + n);
                int count = dictNgrams.getOrDefault(ngram, 0);
                dictNgrams.put(ngram, count + 1);
            }
            int editDistance = computeEditDistance(misspelledNgrams, dictNgrams);
            	if (editDistance <= threshold) {
                suggestions.add(dictWord);
              }
           }
         }

        return suggestions;
       }

     public static int computeEditDistance(Map<String, Integer> s1, Map<String, Integer> s2) {
  	  Set<String> allKeys = new HashSet<>();
  	  allKeys.addAll(s1.keySet());
   	  allKeys.addAll(s2.keySet());
   	  int editDistance = 0;
    	for (String key : allKeys) {
       	   int count1 = s1.getOrDefault(key, 0);
           int count2 = s2.getOrDefault(key, 0);
         editDistance += Math.abs(count1 - count2);
            }
        return editDistance;
     }


    /**
       Looks up all permutations of a string in the dictionary.
       USAGE: java SpellCheck dictionaryFile jumbled_letters
     */
    public static void main(String[] args)
			throws FileNotFoundException, IOException {
	    try {
       		 String filename = "input.txt";
     		   checkSpelling(filename);
   		 } catch (IOException e) {
    		   e.printStackTrace();
    		  }
		int minlen;
		String dictionaryFile = args[0];
		String letters = args[1];
	//	ArrayList words = new ArrayList();

	SpellCheck checker = new SpellCheck(dictionaryFile);
	
	int indexOf = checker.indexOf(letters);
	if (indexOf == SpellCheck.NOT_FOUND)
	    System.out.println("Not found: "  + letters);
	else
	    System.out.println("Position of " +  letters + " in dictionary " +
			       indexOf + " " + checker.get(indexOf));

	for (String s : checker.permutations(letters)) {
	    indexOf = checker.indexOf(s);
	    if (indexOf != SpellCheck.NOT_FOUND)
			System.out.println(s);
	}

    }
}
