import java.util.*;

/**
 * Analyses a WordleAI by running experiments and computing statistics.
 * 
 * @author 23212326 AND 23687599
 */
public class WordleAIAnalyser
{

    // Do not modify the fields of this class.
    private WordleDictionary dictionary;
    private int wordLength;
    // You should read but NOT modify the WordleExperimentResult class
    private ArrayList<WordleExperimentResult> experimentResults;

    /**
     * Constructor takes a dictionary and word length to run experiments with.
     */
    public WordleAIAnalyser(WordleDictionary dictionary, int wordLength)
    {
        this.dictionary = dictionary;
        this.wordLength = wordLength;
        this.experimentResults = new ArrayList<>();
    }

    /**
     * !!! DO NOT MODIFY !!!
     * This method has been implemented for you.
     */
    public ArrayList<WordleExperimentResult> getExperimentResults()
    {
        return experimentResults;
    }

    /**
     * Runs an experiment on a word and stores the result in experimentResults.
     * An experiment is the WordleExperimentResult from using the WordleAI on a WordleGame
     * with word as the secret word.
     * 
     * No checks or guards are needed on the word parameter.
     * It is always assumed to be the right length and to come from the dictionary.
     */
    public void runExperiment(String word)
    {
        //Creat a new object experimentGame with parameter word,call WordleAI.findWord method get experimentGuess 
        //Creat a new object experiment,add the experiment result into experimentResults
        WordleGame experimentGame = new WordleGame(word);
        ArrayList<String> experimentGuess = WordleAI.findWord(dictionary, experimentGame); 
        WordleExperimentResult experiment = new WordleExperimentResult(word,experimentGuess);
        experimentResults.add(experiment);
    }

    /**
     * Runs and stores experiments for each word in the dictionary with the right length.
     * 
     * Should call runExperiment once for each word.
     */
    public void runExperimentsWithAllWords()
    {
        ArrayList<String> wordsWithLength = dictionary.getWordsWithLength(wordLength);
        for(String word : wordsWithLength){
            runExperiment(word);
        }
    }

    /**
     * Runs and stores experiments for each word in the dictionary with the right length.
     * 
     * Only uses words that are lexicographically between the start and finish.
     * A word is only used if it is the same as start or comes after AND it is the same as finish or comes before.
     * 
     * For example, if our words are "act", "bat", "bet", "cat"
     * Then runExperimentsWithWordsBetween("baa", "caa")
     * would only run experiments for "bat" and "bet"
     * 
     * Should call runExperiment once for each word.
     * 
     * HINT: Recall the String compareTo method.
     */
    public void runExperimentsWithWordsBetween(String start, String finish)
    {
        ArrayList<String> wordsWithLength = dictionary.getWordsWithLength(wordLength);
        Collections.sort(wordsWithLength);      
        for(String word : wordsWithLength){
            // Get the words between start and finish.
            if(word.compareTo(start) >= 0 && word.compareTo(finish) <= 0){
                runExperiment(word); 
            }
        }
    }
    

    /**
     * Returns a list of all experiment words that were not solved by WordleAI.
     * The returned list of words should be in lexicographic order.
     * 
     * There may be duplicates in experimentResults.
     * This method should NOT return any duplicated words!
     * 
     * HINT 1: Remeber that findWord returns null when it cannot solve the word.
     * HINT 2: See Collections.sort and the ArrayList contains method.
     */
    public ArrayList<String> getUnsolvedWords()
    {
        ArrayList<String> unsolvedWordsDistinct = new ArrayList<>();
        for(WordleExperimentResult resultList : experimentResults){
            //Unsolved word guessess are null.
            if (resultList.getGuesses() == null){
                if(!unsolvedWordsDistinct.contains(resultList.getWord())){
                    //Get unsolvedWord without duplication into unsolvedWordsDistinct
                    unsolvedWordsDistinct.add(resultList.getWord());
                }
            }
        }  
        //Make unsolvedWords in lexicographic order
        Collections.sort(unsolvedWordsDistinct);
        return unsolvedWordsDistinct;
    }

    /**
     * Returns an array with length 26.
     * The entry at index [0] is the number of 'a' characters guessed over all experiments by WordleAI.
     * The entry at index [1] is the number of 'b' characters.
     * ...
     * The entry at index [25] is the number of 'z' characters.
     * 
     * For example, if the WordleAI guessed the words "cat"+"hat" in one experiment and "log"+"cat" in another:
     * The return array would be {3, 0, 2, ... }
     * Which means 3 'a' characters, 0 'b' characters, 2 'c' characters, and so on...
     * 
     * HINT: Unsolved words have no guesses and should be skipped.
     */
    public int[] getGuessLetterFrequency()
    {
        // Analyse the String word from solved word list
        // Add to each count in frequencies the number of occurrences of each letter in word, and update letters 
        int[] frequency = new int[26];
        for(WordleExperimentResult resultList: experimentResults){
            if (resultList.getGuesses() != null){
                for(String word : resultList.getGuesses()){    
                    for (int k = 0; k < word.length(); k++){
                        char ch = word.charAt(k);
                        if (Character.isLetter(ch)){
                            frequency[Character.toLowerCase(ch) - 'a'] += 1;    
                        }
                    }
                }
            }
        }
        return frequency;
    }

    /**
     * Returns an array of length 7.
     * 
     * The entry at index [0] is the number of times the WordleAI guessed a word correctly after 1 guess.
     * The entry at index [1] is the number of times exactly 2 guesses were needed.
     * and so on.
     * The entry at index [6] is the number of times the WordleAI did not correctly guess the word.
     */
    public int[] getNumGuessesFrequency()
    {
        int[] index = new int[7];
        for(WordleExperimentResult resultList : experimentResults){
            //if WordleAI did not correctly guess the word.
            if(resultList.getGuesses() == null){
                index[6] += 1;
            }
            else{
                //if WordleAI correctly guess the word after 1,2,3,4,5 guesses.
                index[resultList.getGuesses().size()-1] += 1;
            }
        }
        return index;
    }

    /**
     * Makes a string containing a histogram picture of getNumGuessesFrequency().
     * 
     * A possible histogram might look like this:
     *     ..*....
     *     ..*..*.
     *     .**.**.
     *     .*****.
     *     ******.
     *
     * The stars form bars in a histogram, and the dots represent empty space.
     * This would correspond to a frequency table of {1, 3, 5, 2, 3, 4, 0}
     * Recall that the newline '\n' character can be used to encode a line break in a string.
     * Note that return string should end with a newline '\n' character.
     * 
     * Because the numbers can be large, we use a bucketSize.
     * The height of a bar in the chart is 0 if the corresponding number is in the inclusive range from 0 to bucketSize-1
     * The height is 1 if the number is in the inclusive range from bucketSize to bucketSize*2-1
     * The height is 2 if the number is in the inclusive range from bucketSize*2 to bucketSize*3-1
     * ...and so on.
     * 
     * The height of the histogram should be the same as the height of the tallest bar.
     * 
     */
    public String makeHistogram(int bucketSize)
    {
        //Get frequency to draw a graph. Create an Arraylist to store the row of the graph.
        int[] array = getNumGuessesFrequency();
        ArrayList<String> row = new ArrayList<String>();
        //Store the final graph to the string makeHistogram.
        String makeHistogram = "";
        //Give highest a default value from the array divided by bucket size.
        int highest = array[0]/bucketSize;
        for(int i = 1; i < 7;i++){
            //Check if next value divided by bucket size is higher and set higher to that
            if (highest < array[i]/bucketSize){
                highest = array[i]/bucketSize;
            }
        }
        //Loops as many times as the highest, which will be the height and number of lines.
        //List is constructed from the bottom so index(0) is the lowest line.
        for(int i = 0; i < highest;i++){
            String temp = "";
            for(int j = 0; j < 7;j++){
                if(array[j]/bucketSize >= (i+1)){
                    //If the value is high enough it adds a star in the currect line of histogram to represent height.
                    temp += "*";
                }
                else{
                    temp += ".";//else add a dot
                }
            }
            row.add(temp);//Appends temp to the list to store it more permanently
        }
        for(int i = highest-1; i >= 0; i--){
            //Go through backwards to construct string from top down, adds each lin to string.
            makeHistogram += row.get(i) + "\n";//
        }
        return makeHistogram;
    }

    /**
     * Prints the string made by makeHistogram(bucketSize)
     * 
     * The following code:
     * WordleAIAnalyser analyser = new WordleAIAnalyser(new WordleDictionary(), 5);
     * analyser.runExperimentsWithAllWords();
     * analyser.printHistogram(50);
     * 
     * 
     * Should print this to the terminal:
     *   ...*...
     *   ...*...
     *   ...*...
     *   ..**...
     *   ..**...
     *   ..***..
     *   ..***..
     *   ..***..
     *   ..***..
     *   .*****.
     */
    public void printHistogram(int bucketSize)
    {
        // No need to modify this method!
        // It has been provided for you.
        System.out.println(makeHistogram(bucketSize));
    }
}
