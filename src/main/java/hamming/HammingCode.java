package hamming;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HammingCode {

	private int r;
	private int n;
	private int m;

	private BinaryMatrix parityCheckMatrix;
	private BinaryMatrix generatorMatrix;

	private Map<Long, BinaryMatrix> syndromeToErrorMap; 

	public HammingCode(int r) {

		this.r = r;
		n = (int) (Math.pow(2, r)) - 1;
		m = (int) (Math.pow(2, r)) - r - 1;

		generateParityCheckMatrix();
		generateGeneratorMatrix();
		generateSyndromeToErrorMap();	
	}
	
	public BinaryMatrix encode(BinaryMatrix sourceWord) {
		return sourceWord.multiplyWith(getGeneratorMatrix());
	}
	
	public BinaryMatrix decode(BinaryMatrix codeWord) {
		
		BinaryMatrix syndrome = codeWord.multiplyWith(parityCheckMatrix);
		BinaryMatrix error = syndromeToErrorMap.get(syndrome.rowToLong(0));
		BinaryMatrix sentCodeWord = codeWord.add(error);
		
		int[][] sentSourceWord = new int[1][m];
		for (int i = 0; i < m; i++) {
			sentSourceWord[0][i] = sentCodeWord.get(0, i);
		}
		
		return new BinaryMatrix(sentSourceWord);
	}

	public BinaryMatrix getParityCheckMatrix() {
		return parityCheckMatrix;
	}

	public BinaryMatrix getGeneratorMatrix() {
		return generatorMatrix;
	}
	
	public Map<Long, BinaryMatrix> getSyndromeToErrorMap() {
		return syndromeToErrorMap;
	}
	
	//TODO delete
	public void printMap() {
		for ( Entry<Long, BinaryMatrix> entries : getSyndromeToErrorMap().entrySet()) {
			System.out.println(Long.toBinaryString(entries.getKey()));
			entries.getValue().print();
		}
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
		
		syndromeToErrorMap = new HashMap<Long, BinaryMatrix>();
		
		for (int i = 0; i <= n; i++) {
			BinaryMatrix error = new BinaryMatrix(new int[][]{toBinary((long) Math.pow(2, i), n)});
			BinaryMatrix syndrome = error.multiplyWith(parityCheckMatrix);
			syndromeToErrorMap.put(syndrome.rowToLong(0), error);
		}
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
	
	private boolean isPowerOf2(int number) {
		return (number & -number) == number;
	}

}
