package hamming;

import java.text.DecimalFormat;
import java.util.Scanner;


public class HammingApp 
{
	
	public static final DecimalFormat DF = new DecimalFormat("#.###");
	
    public static void main( String[] args )
    {
    	// Read r and create the Hamming code.
    	Scanner in = new Scanner(System.in);
    	System.out.print("Enter r of the hamming code to use: ");
    	int r = readInt(in, 2);
    	HammingCode hammingCode = new HammingCode(r);
    	
    	// Read the chance of a bit being 1 in a source word.
    	System.out.print("Enter the chance that a bit is 1 in a source word: ");
    	double chanceOf1 = readDouble(in, 0);
    	
    	// Read the chance of a bit being flipped when sending a word through the channel.
    	System.out.print("Enter the chance that a bit is flipped while sending it through the channel");
    	double errorChance = readDouble(in, 0);
    	
    	// Generate a word and do encoding, sending and decoding.
    	BinaryMatrix sourceWord = generateWord(hammingCode.getSourceWordLength(), chanceOf1);
    	System.out.print("Source word: ");
    	sourceWord.print();
    	
    	BinaryMatrix encodedWord = hammingCode.encode(sourceWord);
    	System.out.print("Encoded word: ");
    	encodedWord.print();
    	
    	BinaryMatrix receivedWord = sendWord(encodedWord, errorChance);
    	System.out.print("Received word: ");
    	receivedWord.print();
    	
    	BinaryMatrix decodedWord = hammingCode.decode(receivedWord);
    	System.out.print("Decoded word (with error correction): ");
    	decodedWord.print();
    	
    	if (decodedWord.equals(sourceWord)) {
    		System.out.println("Correctly decoded.");
    	}
    	else {
    		System.out.println("Uncorrected error.");
    	}
    	System.out.println();
    	
    	// If there is an error detected keep resending.
    	int numberOfResendings = 0;
    	while (hammingCode.guessErrorBit(receivedWord) >= 0) {
    		receivedWord = sendWord(encodedWord, errorChance);
    	}
    	
    	decodedWord = hammingCode.decode(receivedWord);
    	System.out.print("Decoded word (with resending on error detection): ");
    	decodedWord.print();
    	
    	
    	if (decodedWord.equals(sourceWord)) {
    		System.out.println("Decoded correctly with " + numberOfResendings + " resendings");
    	}
    	else {
    		System.out.println("Wrong decoding due to an undetected error.");
    	}
    }
    
	public static BinaryMatrix sendWord(BinaryMatrix sentWord, double errorChance) {

		BinaryMatrix receivedWord = sentWord.clone();
		for (int column = 0; column < sentWord.getNumberOfColumns(); column++) {
			double r = Math.random();
			if (r < errorChance) {
				receivedWord.flip(0, column);
			}
		}
		
		return receivedWord;
	}
    
    public static BinaryMatrix generateWord(int length, double chanceOf1) {
    	
    	// To get a word consistent with the chance of a bit being 1,
    	// flip bits of the all zeroes word by sending it through a channel with error probability chanceOf1.
    	BinaryMatrix word = new BinaryMatrix(1, length);
    	word = sendWord(word, chanceOf1);
    	
    	return word;
    }
    
	/**
	 * @return an integer read from the given scanner
	 * that is larger or equal to the given lower bound - min.
	 */
	public static int readInt(Scanner in, int min) {
		
		// Keep reading while smaller than min.
		int readInt = Integer.MIN_VALUE;
		while (readInt < min) {

			// Check if integer.
			if (in.hasNextInt()) {
				readInt = in.nextInt();
			}
			
			if (readInt < min) {
				
				// Print error message on unsuccessful try and clear the input buffer.
				System.out.println("Invalid input, enter an integer larger or equal to " + min + ".");
				in.nextLine();
			}
		}
		
		return readInt;
	}
	
	/**
	 * @return a double read from the given scanner
	 * that is larger or equal to the given lower bound - min.
	 */
	public static double readDouble(Scanner in, double min) {
		
		// Keep reading while smaller than min.
		double readDouble = Double.NEGATIVE_INFINITY;
		while (readDouble < min) {

			// Check if double.
			if (in.hasNextDouble()) {
				readDouble = in.nextDouble();
			}
			
			if (readDouble < min) {
				
				// Print error message on unsuccessful try and clear the input buffer.
				System.out.println("Invalid input, enter a decimal larger or equal to " + min + ".");
				in.nextLine();
			}
		}
		
		return readDouble;
	}
}
