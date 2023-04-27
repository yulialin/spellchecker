/**
 Create words to solve a jumble problem of one sequence
 of characters.
 @author Yulia Yueer Lin
 */

import java.io.*;   // for IO
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
 

import java.util.Arrays;

public class SpellCheck {
    public static final int NOT_FOUND = -1;
    // default values for dictionary and document
    private static String dictFilename = "./";
    private static String docFilename = "dictionary.txt";
    private String [] dictionary; // Array of words in dictionary.

    // private static long CompCount;

/**
    *find the cloest words to the misspelled word that is found 
    * 
    * @param word the misspelled word to check 
    * @return a list of the cloest possible words (top 3), might return an empty life is none is found
    */ 

    //read the dictionary file for the spell checker
    private static List<String> readDictionary() throws IOException { //read the dictionary for the spell checker
        List<String> dictionary = new ArrayList<>();
        //open the dictionary file and read its contents into a list
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            //read each line of the file
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim()); //trimming the head and the tail whitespace of the words
                //then add it to the dictionary list 
            }
        }
        return dictionary;
    }

    //read input file and read its content for spell checker 
    private static List<String> readFile(String filename) throws IOException { //read input file for the spell checker
        List<String> words = new ArrayList<>();
        //open the input file and read its contents into a list of words
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            //read each line of the file, split line into individual words 
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split("\\s+"); //split input into words
                for (String w : lineWords) {
                    words.add(w.trim().toLowerCase().replaceAll("[^a-z]", "")); //remove punctuation from input and case sensitive 
                } //then, add each word to the list
            }
        }
        return words;
    }

    /*
      Load dictionary used for spell checking
      File must contain one word per line.

      *constructor for the SpellCheck class that takes a filename as its input
    */
    public SpellCheck(String filename) {
        //create an ArrayList to hold the words read from the file 
        ArrayList<String> words = new ArrayList<String>();
        FileReader filereader = null;

        try {    //try to create a FileReade with the proviced filename 
            filereader = new FileReader(filename);
        } catch (FileNotFoundException ex) {
            // if the file is not found, we print out an error message and turns
            System.err.println(ex);
            return;
        }
        //create a BufferedReader to read the words from the file 
        BufferedReader reader = new BufferedReader(filereader);
        String s;
        try {   //read each line of the file and add it to the ArrayList 
            while ( (s = reader.readLine()) != null ) {
                words.add( s );
            }
            // convert arraylist to an array
            this.dictionary = words.toArray(new String [1]); //and set it as the dictionary 
        } catch (IOException ex) {
            //if there is an error reading the file, print out a error message and return 
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
        // Binary search implementation
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
        if (n == 0) { // base case (if there is no character to permute, add the permutation to the arraylist)
            strings.add(prefix + s);
        }
        else {
            // TODO
            for (int i = 0; i < n; i++) {
                //recursive function to perm()
                perm(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, n), strings);

            }
        }
    }

    /**
     * Return ArrayList of all permutations of s
     * Take a string as input & return an ArrayList of strings
     */
    public static ArrayList<String> permutations(String s) {
        ArrayList<String> strings = new ArrayList<String>();
        perm("",s,strings); //call perm() with empty prefix and input string 
        return strings;
    }

    /**takes a word and an integer n as input and returns a list of n-grams of length n
     * genrates n grams of a given length from a given word
     * a string and an integer as input, returns a List of strings
     */
    public static List<String> generateNgrams(String word, int n) {
        List<String> ngrams = new ArrayList<String>();
        for(int i = 0; i < word.length()-n+1; i++) { //Iterate over the word, generating n-grams
            ngrams.add(word.substring(i, i + n)); //add the generated n-gram to the List
        }
        return ngrams;
    }

    /** 
     * checks the spellings of all the words in the file in comparison to a dictionary of vocabularies 
     * prints out if there misspelled words is detected, with suggestd spellings alternatives
     * @param filename the name of the file to check the spelling of
     * @throw IOException if there is error reading the file
     */

    public static void checkSpelling(String filename) throws IOException {
        //read through the dictionary file contents
        List<String> dictionary = readDictionary();
        List<String> words = readFile(filename);
        //check each word from input file in comparison to the dictionary 
        for (String word : words) {
            if (!dictionary.contains(word)) {
                //print out the words that are misspelled 
                System.out.println("Misspelled word: " + word);
                List<String> suggestions = suggestedSpelling(word, dictionary); //suggested corrections for mispelled words
               // System.out.println("Suggestions: " + suggestions);
            // Print top 3 suggestions for correction, or all suggestions if less than 3
            int maxSuggestions = Math.min(3, suggestions.size());
            for (int i = 0; i < maxSuggestions; i++) {
                System.out.println((i+1) + ". " + suggestions.get(i)); }
            }
        }
    }

    /** 
     * from the given word and dictionary of vocabularies, find and return a list of suggest corrections for the misspelled word
     * @param word the misspelled word to suggest corrections for
     * @param dictionary the list of vocabs to make comparsion 
     * @return the suggested correction spellings for mispelled words
     */
    public static List<String> suggestedSpelling(String word, List<String> dictionary) {
        //set up trigram and edit distance threholds 
        int n = 3; // use trigrams
        int threshold = 2; // maximum edit distance
        //create a map of trigrams to their counts in the dictionary
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

        // Generate n-grams(tri) for the misspelled word and compute edit distance to dictionary words
        Map<String, Integer> misspelledNgrams = new HashMap<>();
        for (int i = 0; i <= word.length() - n; i++) {
            String ngram = word.substring(i, i + n);
            int count = misspelledNgrams.getOrDefault(ngram, 0);
            misspelledNgrams.put(ngram, count + 1);
        }
        for (String dictWord : dictionary) {
            if (Math.abs(dictWord.length() - word.length()) <= n) {

                //generate trigrams for dictionary vocabs & edit distance 
                Map<String, Integer> dictNgrams = new HashMap<>();
                for (int i = 0; i <= dictWord.length() - n; i++) { //iterates 
                    String ngram = dictWord.substring(i, i + n);
                    int count = dictNgrams.getOrDefault(ngram, 0);
                    dictNgrams.put(ngram, count + 1);
                }
                int editDistance = distance(misspelledNgrams, dictNgrams);
                //add word to suggestions if its edit distance is within the threshold
                if (editDistance <= threshold) {
                    suggestions.add(dictWord);
                }
            }
        }

        return suggestions;
    }

    /**
     * computes the edit distance btwn 2 maps of trigrams, disance is the sum of the abosule differences
     * the counts of eah trigram in the 2 maps
     */ 
    public static int distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        //create a set that contains all the elements from both of the maps 
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(s1.keySet());
        allKeys.addAll(s2.keySet());
        //compute the edit distance between the 2 maps 
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
            //check the spelling of the words in the input.txt file 
            checkSpelling(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read the dictionary file and the jumbled letters from the command line arguments 
        int minlen;
        if (args.length < 2) {
            System.out.println("USAGE: java SpellCheck dictionaryFile jumbled_letter");
            return; //checks if the length is less than 2, if so print out an error message without trying to access empty array 
        }
        String dictionaryFile = args[0];
        String letters = args[1];
        //	ArrayList words = new ArrayList();

        //create a spellcheck object with the dictionary file 
        SpellCheck checker = new SpellCheck(dictionaryFile);
        //find the index and the word in the dictionary that match with the jumbled letters
        int indexOf = checker.indexOf(letters);
        if (indexOf == SpellCheck.NOT_FOUND)
            System.out.println("Not found: "  + letters);
        else
            System.out.println("Position of " +  letters + " in dictionary " +
                    indexOf + " " + checker.get(indexOf));

        //print all the permutation of the jumbled letters that are in the dictionary 
        for (String s : checker.permutations(letters)) {
            indexOf = checker.indexOf(s);
            if (indexOf != SpellCheck.NOT_FOUND)
                System.out.println(s);
            }
        } 
    }
