import java.util.*;

/**
 * This class contains static methods that play Wordle using a simple artificial intelligence.
 *
 * @author 23212326 AND 23687599
 */
public class WordleAI
{
    private WordleAI()
    {
        // Constructor is private because all methods are static
        // and instances of WordleAI should not be created
    }

    /**
     * Returns true if guess contains the letter c and false otherwise.
     */
    public static boolean guessContains(String guess, char c)
    {
        for(int i = 0; i < guess.length(); i++){
            if(c == guess.charAt(i)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if newGuess is consistent with a previousGuess and its result
     * and false otherwise.
     * 
     * The parameter previousGuess is a previous Wordle guess made by the AI.
     * The parameter result is the result of the guessWord method in WordleGame.
     * The parameter newGuess is a potential new word to guess.
     * 
     * A newGuess is consistent with the previousGuess and result if they can be
     * explained by newGuess being the secret word. That is, newGuess should not
     * contradict results from previous guesses.
     * 
     * For example, suppose we have previousGuess="dxaxx" and the result="*_.__",
     * then newGuess="dairy" or newGuess="dzzza" would return true, but
     * newGuess="testa" or newGuess="dxiry" would be false.
     * This is because only newGuess="dairy" or newGuess="dzzza" could have been the secret word
     * for previousGuess="dxaxx" to get result="*_.__"
     * 
     * HINT: Can you use a new WordleGame(...) somehow?
     */
    public static boolean isConsistent(String previousGuess, String result, String newGuess)
    {
        //Create a new newGame object assume newGuess as secrect word,get description for previousGuess.
        WordleGame newGame = new WordleGame(newGuess);
        String desc = newGame.guessWord(previousGuess);
        //Campare desc with result
        return (result.equals(desc));
    }

    /**
     * Returns true if result contains only '*' characters
     * and false if it contains a non-'*' character.
     */
    public static boolean isAllStars(String result)
    {
        for(int i = 0; i < result.length(); i++){
            if(result.charAt(i) != '*'){
                return false;
            }
        }
        return true;
    }

    /**
     * This method runs the AI algorithm.
     * Given a dictionary and a game, makes a series of calls to game.guessWord(word)
     * to find the secret word in game.
     * Returns an ArrayList containing the words in the order they were guessed.
     * If the secret word could not be found, returns null.
     * 
     * The AI algorithm is very specific!
     * It uses a simple strategy similar to one you may have used when playing Wordle.
     * 
     * The AI starts by guessing the the lexicographicaly smallest word.
     * 
     * Then, for every guess after that, the AI guesses a word that does not contradict any previous
     * results it has seen. That is, it makes guesses that are consistent (see isConsistent)
     * with all guesses made so far.
     * 
     * If there are multiple possible guesses that are consistent, then the AI
     * will pick the lexicographically smallest option.
     * 
     * WordleGame has been modified to only allow 6 guesses. After this, it will return an
     * empty string "".
     * 
     * If the game ends because the AI has run out of guesses, then findWord returns null.
     * Otherwise, findWord returns once it has made a correct guess. This can
     * be checked using the isAllStars method above. In this case, a list of the AI's
     * guesses in the order they were made is returned.
     * 
     * HINT 1: You will almost certainly need to read the unit test and use the debugger for this method.
     * HINT 2: See Collections.sort in the Java class libraries for lexicographical ordering.
     */
    public static ArrayList<String> findWord(WordleDictionary dictionary, WordleGame game)
    {
        //Get word from dictionary with certain length, sort it
        ArrayList<String> wordsWithLength = dictionary.getWordsWithLength(game.getWordLength());
        ArrayList<String> allGuesses = new ArrayList<>();
        Collections.sort(wordsWithLength);
        String result = "";
        ArrayList<String> prevResult = new ArrayList<>(); 
        for(String newGuess : wordsWithLength){
            // First run，when no previous guess.
            if(allGuesses.isEmpty()){
                result = game.guessWord(newGuess);
                //If first run is correct, then add it to allGuesses and finish.
                if(isAllStars(result)){
                    allGuesses.add(newGuess);
                    return allGuesses;
                }
                //If fisrt run is not correct, then add it to allGuesses.
                prevResult.add(result);
                allGuesses.add(newGuess);
            } 
            else{ 
                // More runs.
                boolean alwaysConsistent = true;
                for (int i=0; i < allGuesses.size(); i++)
                {   
                    //In which condition the boolean is false.
                    if (!isConsistent(allGuesses.get(i), prevResult.get(i), newGuess)){
                        alwaysConsistent = false;
                    }
                }
                //When boolean is true.
                if (alwaysConsistent){
                    result = game.guessWord(newGuess);
                    //After 6 guesses, it will return an empty result and return null.
                    if(result == ""){
                        return null;
                    }
                    //Get secret word before 6 guesses.
                    if(isAllStars(result)){
                        allGuesses.add(newGuess);
                        return allGuesses;
                    }
                    //Add newGuess result into a list prevResult
                    prevResult.add(result);
                    //Add newGuess into allGuesses.
                    allGuesses.add(newGuess);
                }
            }
        }
        return null;
    }
}