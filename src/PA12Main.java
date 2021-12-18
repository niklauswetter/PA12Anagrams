/**
 * Niklaus Wetter
 * CSC210
 *
 * This class finds all the possible anagrams of a given word.
 * Arguments are taken through the command line:
 * A text file dictionary, a word to scramble, and a limit on how many words a solution may contain
 * This class implements a recursive backtracking method to accomplish this process
 */
import java.io.File;
import java.util.*;

public class PA12Main
{
    //Main method
    public static void main(String[] args) throws Exception
    {
        //Rejects incorrect arguments
        if(args.length!=3 || Integer.valueOf(args[2])<0)
            throw new Exception("Incorrect arguments");

        //Fields
        File file = new File(args[0]);
        String word = args[1].trim().toLowerCase();
        int wordLimit = Integer.valueOf(args[2]);
        Scanner scanner = new Scanner(file);
        List<String> dictionary = new ArrayList<>();

        //Load dictionary
        while(scanner.hasNextLine())
            dictionary.add(scanner.nextLine().trim().toLowerCase());

        //Get possible words
        List<String> wordList = getWords(dictionary,word);

        //Print output
        System.out.println("Phrase to scramble: "+word+"\n");
        System.out.println("All words found in "+word+":\n"+wordList);
        System.out.println("\nAnagrams for "+word+":");
        getAnagrams(wordList, new LetterInventory(word),wordLimit,word);
    }

    /**
     * This method accepts a dictionary and a word, and returns a list of all words that can be created using the
     * letters from the given word
     * @param dictionary a list of Strings containing all the words to check for
     * @param word a word to use the letters from to create other words
     * @return ArrayList of Strings, all words that can be created using the letters from the word parameter
     */
    public static List<String> getWords(List<String> dictionary, String word)
    {
        List<String> words = new ArrayList<>();
        LetterInventory wordLi = new LetterInventory(word);
        for(String s:dictionary)
        {
            LetterInventory test = new LetterInventory(s);
            if(wordLi.contains(test))
                words.add(s);
        }
        Collections.sort(words);
        return words;
    }

    /**
     * Shell method for recursive backtracking method, I pretty much only did this to make it look nicer
     * @param words List of words possible to create from the letters in the word parameter
     * @param wordLi LetterInventory object from the word parameter
     * @param wordLimit The maximum number of words allowed in any given solution
     * @param word A String to scramble
     */
    public static void getAnagrams(List<String> words, LetterInventory wordLi,
                                   int wordLimit,String word)
    {
        getAnagramsHelper(new ArrayList<>(),words, wordLi,wordLimit,word);
    }

    /**
     * Internal recursive backtracking method that actually finds the solutions
     * @param solution ArrayList used to store possible solutions
     * @param words List of words possible to create from the letters in the word parameter
     * @param wordLi LetterInventory object from the word parameter
     * @param wordLimit The maximum number of words allowed in any given solution
     * @param word A String to scramble
     */
    public static void getAnagramsHelper(ArrayList<String> solution, List<String> words, LetterInventory wordLi,
                                         int wordLimit, String word)
    {
        //Check if there is at least one possible word with the letters you have left
        boolean deadEnd = true;
        String temp;
        for(int i=0;i<words.size();i++)
        {
            temp = words.get(i);
            if(solution.contains(temp))
                continue;
            if(wordLi.contains(temp))
            {
                deadEnd=false;
                break; //Exit as soon as solution is determined to be possible
            }
        }
        //Word limit check
        if(wordLimit>0)
        {
            if(solution.size()==wordLimit && !wordLi.isEmpty())
                return;
        }
        /**
         * We consider the base case to be when we have run out of possible words
         * If a correct solution was found it will have been added to the list, so we exit
         */
        if(deadEnd)
        {
            return;
        }
        else
        {
            String test;
            for(int i=0;i<words.size();i++)
            {
                test=words.get(i);
                if(solution.contains(test)||test.equals(word))
                    continue;
                if(wordLi.contains(test))
                {
                    solution.add(test); //Make a decision
                    wordLi.subtract(test);
                    if(wordLi.isEmpty())
                    {
                        System.out.println(solution);
                    }
                    getAnagramsHelper(solution,words,wordLi,wordLimit,word); //Explore
                    solution.remove(test); //Un-make decision
                    wordLi.add(test);
                }
            }
        }
    }
}
