package hamming;

public class Experiment {

    public static boolean tryErrorCorrection(HammingCode code, double errorChance, double chanceOf1) {
    	
    	BinaryMatrix sourceWord = generateWord(code.getSourceWordLength(), chanceOf1);
    	
    	// Now encode and send it through a channel.
    	BinaryMatrix codeWord = code.encode(sourceWord);
    	BinaryMatrix receivedWord = sendWord(codeWord, errorChance);
    	
    	// Decode and compare.
    	BinaryMatrix decodedWord = code.decode(receivedWord);
    	if (decodedWord.equals(sourceWord)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public static int tryErrorDetection(HammingCode code, double errorChance, double chanceOf1) {
    	
    	BinaryMatrix sourceWord = generateWord(code.getSourceWordLength(), chanceOf1);
    	
    	// Encode.
    	BinaryMatrix codeWord = code.encode(sourceWord);

    	// If there is an error detected keep resending.
    	int numberOfSendings = 0;
    	int error;
    	BinaryMatrix receivedWord;
    	do {
    		
    		receivedWord = sendWord(codeWord, errorChance);
    		error = code.guessErrorBit(codeWord);
    		numberOfSendings++;
    	} while (error >= 0);
    	
    	BinaryMatrix decodedWord = code.decode(receivedWord);
    	if (decodedWord.equals(sourceWord)) {
    		return numberOfSendings;
    	}
    	else {
    		return -1;
    	}
    }
}
