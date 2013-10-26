package hamming;

import java.util.Map;

public class HammingCode {

	private int r;
	private int n;
	private int m;

	private BinaryMatrix parityCheckMatrix;
	private BinaryMatrix generatorMatrix;

	private Map<BinaryMatrix, BinaryMatrix> syndromeToError; 

	public HammingCode(int r) {

		this.r = r;
		n = (int) (Math.pow(2, r)) - 1;
		m = (int) (Math.pow(2, r)) - r - 1;

		generateParityCheckMatrix();
		generateGeneratorMatrix();
		generateSyndromeToErrorMap();	
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

			if (!isPowerOf2(i)) {
				
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

	private void generateSyndromeToErrorMap() {

	}

	// TODO util class?
	private int[] toBinary(int number, int base) {
		int[] ret = new int[base];
		for (int i = 0; i < base; i++) {
			
			if ((1 << i & number) != 0) {
				ret[base - 1 - i] = 1;
			}
		}
		return ret;
	}
	
	private boolean isPowerOf2(int number) {
		return (number & -number) == number;
	}

}
