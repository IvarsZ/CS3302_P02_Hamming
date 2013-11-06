package hamming;

public class HammingCode {

	private int r;
	private int codeWordLength; // n.
	private int sourceWordLength; // m.

	private BinaryMatrix parityCheckMatrix;
	private BinaryMatrix generatorMatrix;

	/**
	 * Constructor for a Hamming code (2^r - 1, 2^r - r - 1, 3).
	 * 
	 * Its source word length is m = 2^r - r - 1, and code word length is n = 2^r - 1.
	 * The minimal distance between codewords is 3.
	 */
	public HammingCode(int r) {

		this.r = r;
		codeWordLength = (int) (Math.pow(2, r)) - 1;
		sourceWordLength = (int) (Math.pow(2, r)) - r - 1;

		generateParityCheckMatrix();
		generateGeneratorMatrix();
	}
	
	/**
	 * Converts a number to its binary form.
	 * 
	 * @param number - the number to convert.
	 * @param base - the number of bits the binary form should have.
	 * 
	 * @return the binary form.
	 */
	public static int[] toBinary(long number, int base) {
		int[] ret = new int[base];
		for (int i = 0; i < base; i++) {

			if ((1 << i & number) != 0) {
				ret[base - 1 - i] = 1;
			}
		}
		return ret;
	}

	/**
	 * Encodes a source word.
	 * 
	 * @param sourceWord - a binary vector of length m.  
	 * 
	 * @return corresponding code word - binary vector of length n.
	 */
	public BinaryMatrix encode(BinaryMatrix sourceWord) {
		return sourceWord.multiplyWith(getGeneratorMatrix());
	}

	/**
	 * Decodes a word.
	 * 
	 * @param word - a binary vector of length n.  
	 * 
	 * @return corresponding source word - binary vector of length m.
	 */
	public BinaryMatrix decode(BinaryMatrix word) {

		// Guess the error and add it to get the sent code word,
		int errorBit = guessErrorBit(word);
		
		BinaryMatrix sentCodeWord = word.clone();
		if (errorBit >= 0) {
			sentCodeWord.flip(0, errorBit);
		}

		// then pick the first m non-parity bits to decode it.
		int[][] sentSourceWord = new int[1][sourceWordLength];
		for (int i = 0; i < sourceWordLength; i++) {
			sentSourceWord[0][i] = sentCodeWord.get(0, i);
		}

		return new BinaryMatrix(sentSourceWord);
	}

	
	// TODO refactor.
	/**
	 * @return a vector of the most likely error corresponding to the given syndrome.
	 */
	public int guessErrorBit(BinaryMatrix word) {

		// Use the syndrome to get the row of the parity check matrix to which it corresponds to.
		BinaryMatrix syndrome = word.multiplyWith(parityCheckMatrix);
		int errorIndex = syndrome.rowToInt(0);
		
		// If there was an error,
		if (errorIndex != 0) {

			// Determine which bit was flipped.
			double powerOf2Double = Math.log(errorIndex)/Math.log(2);
			int powerOf2 = (int) powerOf2Double;
			if (powerOf2 == powerOf2Double) {
				
				// Syndromes that are converted to powers of two correspond to the identity matrix at the bottom in descending order.
				return codeWordLength - powerOf2 - 1;
			}
			else {
				
				// Non-powers of 2 correspond to the vectors at the top, which are in ascending order with powers of two skipped.
				return errorIndex - powerOf2 - 2;
			}
		}
		
		return -1;
	}
	
	public double getInformationRate() {
		return ((double) getSourceWordLength())/getCodeWordLength();
	}

	/**
	 * Getter for the parity check matrix.
	 */
	public BinaryMatrix getParityCheckMatrix() {
		return parityCheckMatrix;
	}

	/**
	 * Getter for the generator matrix.
	 */
	public BinaryMatrix getGeneratorMatrix() {
		return generatorMatrix;
	}

	public int getCodeWordLength() {
		return codeWordLength;
	}

	public int getSourceWordLength() {
		return sourceWordLength;
	}

	private void generateParityCheckMatrix() {

		int[][] parityCheckArray = new int[codeWordLength][r]; 

		// Form the parity check matrix from all non-zero vectors of length n.
		int rowForNotPowersOf2 = 0;
		int rowForPowersOf2 = codeWordLength - 1;
		for (int i = 1; i <= codeWordLength; i++) {

			// If the log of 2 is an integer it is a power of 2.
			double powerOf2Double = Math.log(i)/Math.log(2);
			int powerOf2 = (int) powerOf2Double;
			if (powerOf2 == powerOf2Double) {

				// Powers of 2 generate vectors of weight one that form the identity matrix at the bottom in descending order.
				parityCheckArray[rowForPowersOf2] = toBinary(i, r);
				rowForPowersOf2--;
			}
			else {

				// Non-powers of 2 generate the vectors at the top, put them in ascending order for easier decoding.
				parityCheckArray[rowForNotPowersOf2] = toBinary(i, r);
				rowForNotPowersOf2++;
			}
		}

		parityCheckMatrix = new BinaryMatrix(parityCheckArray);
	}

	private void generateGeneratorMatrix() {

		int[][] generatorArray = new int[sourceWordLength][codeWordLength];

		// Create the identity matrix.
		for (int i = 0; i < sourceWordLength; i++) {
			generatorArray[i][i] = 1;
		}

		// Copy the non-identity matrix part matrix from the parity check matrix.
		for (int row = 0; row < sourceWordLength; row++) {
			for (int column = sourceWordLength; column < codeWordLength; column++) {
				generatorArray[row][column] = parityCheckMatrix.get(row, column - sourceWordLength);
			}
		}

		generatorMatrix = new BinaryMatrix(generatorArray);
	}
}
