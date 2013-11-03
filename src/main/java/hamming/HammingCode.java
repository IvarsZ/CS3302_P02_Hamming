package hamming;

public class HammingCode {

	private int r;
	private int n;
	private int m;

	private BinaryMatrix parityCheckMatrix;
	private BinaryMatrix generatorMatrix;

	public HammingCode(int r) {

		this.r = r;
		n = (int) (Math.pow(2, r)) - 1;
		m = (int) (Math.pow(2, r)) - r - 1;

		generateParityCheckMatrix();
		generateGeneratorMatrix();
	}

	public BinaryMatrix encode(BinaryMatrix sourceWord) {
		return sourceWord.multiplyWith(getGeneratorMatrix());
	}

	public BinaryMatrix decode(BinaryMatrix codeWord) {

		BinaryMatrix error = guessError(codeWord.multiplyWith(parityCheckMatrix));

		BinaryMatrix sentCodeWord = codeWord.add(error);

		int[][] sentSourceWord = new int[1][m];
		for (int i = 0; i < m; i++) {
			sentSourceWord[0][i] = sentCodeWord.get(0, i);
		}

		return new BinaryMatrix(sentSourceWord);
	}

	public BinaryMatrix guessError(BinaryMatrix syndrome) {

		int errorIndex = syndrome.rowToInt(0);
		int[][] errorArray = new int[1][n];
		if (errorIndex != 0) {

			double powerOf2Double = Math.log(errorIndex)/Math.log(2);
			int powerOf2 = (int) powerOf2Double;

			if (powerOf2 == powerOf2Double) {
				errorArray[0][n - powerOf2 - 1] = 1;
			}
			else {
				errorArray[0][errorIndex - powerOf2 - 2] = 1;
			}
		}

		// BinaryMatrix error = syndromeToErrorMap.get(syndrome.rowToLong(0));
		BinaryMatrix error = new BinaryMatrix(errorArray);
		return error;
	}

	public BinaryMatrix getParityCheckMatrix() {
		return parityCheckMatrix;
	}

	public BinaryMatrix getGeneratorMatrix() {
		return generatorMatrix;
	}

	private void generateParityCheckMatrix() {

		int[][] parityCheckArray = new int[n][r]; 

		int rowForNotPowersOf2 = 0;
		int rowForPowersOf2 = n - 1;
		for (int i = 1; i <= n; i++) {

			double powerOf2Double = Math.log(i)/Math.log(2);
			int powerOf2 = (int) powerOf2Double;

			if (powerOf2 != powerOf2Double) {

				parityCheckArray[rowForNotPowersOf2] = toBinary(i, r);
				rowForNotPowersOf2++;
			}
			else {

				parityCheckArray[rowForPowersOf2] = toBinary(i, r);
				rowForPowersOf2--;
			}
		}

		parityCheckMatrix = new BinaryMatrix(parityCheckArray);
	}

	private void generateGeneratorMatrix() {

		int[][] generatorArray = new int[m][n];

		// Create the identity matrix.
		for (int i = 0; i < m; i++) {
			generatorArray[i][i] = 1;
		}

		// Copy the non-identity matrix part matrix from the parity check matrix.
		for (int row = 0; row < m; row++) {
			for (int column = m; column < n; column++) {
				generatorArray[row][column] = parityCheckMatrix.get(row, column - m);
			}
		}

		generatorMatrix = new BinaryMatrix(generatorArray);
	}

	// TODO util class?
	public int[] toBinary(long number, int base) {
		int[] ret = new int[base];
		for (int i = 0; i < base; i++) {

			if ((1 << i & number) != 0) {
				ret[base - 1 - i] = 1;
			}
		}
		return ret;
	}

}
