package hamming;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;

public class HammingApp 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
    
    public static boolean tryErrorCorrection(HammingCode code, double errorChance, double chanceOf1) {
    	
    	// To get a source word consistent with the chance of a bit being 1,
    	// flip bits of the all zeroes word by sending it through a channel with error probability chanceOf1.
    	BinaryMatrix sourceWord = new BinaryMatrix(1, code.getSourceWordLength());
    	sourceWord = BinarySymmetricChannel.sendWord(sourceWord, chanceOf1);
    	
    	// Now encode and send it through a channel.
    	BinaryMatrix codeWord = code.encode(sourceWord);
    	BinaryMatrix receivedWord = BinarySymmetricChannel.sendWord(codeWord, errorChance);
    	
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
    	
    	// To get a source word consistent with the chance of a bit being 1,
    	// flip bits of the all zeroes word by sending it through a channel with error probability chanceOf1.
    	BinaryMatrix sourceWord = new BinaryMatrix(1, code.getSourceWordLength());
    	sourceWord = BinarySymmetricChannel.sendWord(sourceWord, chanceOf1);
    	
    	// Encode.
    	BinaryMatrix codeWord = code.encode(sourceWord);

    	// If there is an error detected keep resending.
    	int numberOfSendings = 0;
    	int error;
    	BinaryMatrix receivedWord;
    	do {
    		
    		receivedWord = BinarySymmetricChannel.sendWord(codeWord, errorChance);
    		error = code.guessErrorBit(codeWord);
    		numberOfSendings++;
    	} while (error >= 0);
    	
    	BinaryMatrix decodedWord = code.decode(receivedWord);
    	if (sourceWord.equals(sourceWord)) {
    		return numberOfSendings;
    	}
    	else {
    		return -1;
    	}
    }
}
