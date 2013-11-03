package hamming;

import junit.framework.TestCase;

public class HammingTest extends TestCase {

	private HammingCode rIs3Code;

	public HammingTest() {
		rIs3Code = new HammingCode(3);
	}

	public void testParityCheckMatrix() {

		// For r = 3, the parity check matrix should be:
		int[][] expected = {{0, 1, 1},
				{1, 0, 1},
				{1, 1, 0},
				{1, 1, 1},
				{1, 0, 0},
				{0, 1, 0},
				{0, 0, 1}
		};

		assertEquals(new BinaryMatrix(expected), rIs3Code.getParityCheckMatrix());		
	}

	public void testGeneratorMatrix() {

		// For r = 3, the generator matrix should be:
		int[][] expected = {{1, 0, 0, 0, 0, 1, 1},
				{0, 1, 0, 0, 1, 0, 1},
				{0, 0, 1, 0, 1, 1, 0},
				{0, 0, 0, 1, 1, 1, 1}
		};

		assertEquals(new BinaryMatrix(expected), rIs3Code.getGeneratorMatrix());
	}

	// TODO remap.
	public void testSyndromeToErrorMap() {

		// For r = 3, the mappings should be:

		// 000 to 0000000.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 0, 0, 0}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{0, 0, 0}})));

		// 001 to 0000001.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 0, 0, 1}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{0, 0, 1}})));

		// 010 to 0000010.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 0, 1, 0}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{0, 1, 0}})));

		// 011 to 0000011.
		assertEquals(new BinaryMatrix(new int[][]{{1, 0, 0, 0, 0, 0, 0}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{0, 1, 1}})));

		// 100 to 0000100.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 1, 0, 0}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{1, 0, 0}})));

		// 101 to 0000101.
		assertEquals(new BinaryMatrix(new int[][]{{0, 1, 0, 0, 0, 0, 0}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{1, 0, 1}})));

		// 110 to 0000110.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 1, 0, 0, 0, 0}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{1, 1, 0}})));

		// 111 to 0001000.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 1, 0, 0, 0}}), rIs3Code.guessError(new BinaryMatrix(new int[][]{{1, 1, 1}})));
	}

	public void testEncodeDecode() {

		// All source words should be the same when encoded and decoded.
		for (int i = 0; i < 16; i++) {
			BinaryMatrix sourceWord = new BinaryMatrix(new int[][]{rIs3Code.toBinary(i, 4)});
			assertEquals(sourceWord, rIs3Code.decode(rIs3Code.encode(sourceWord)));
		}
	}
	
	public void testEncodeDecodeWithErrors() {

		// All source words should be the same when encoded and decoded.
		for (int i = 0; i < 16; i++) {
			BinaryMatrix sourceWord = new BinaryMatrix(new int[][]{rIs3Code.toBinary(i, 4)});
			BinaryMatrix encodedWord = rIs3Code.encode(sourceWord);
			
			// Try all possible weight 1 errors.
			for (int j = 0; j < 7; j++) {
				BinaryMatrix error = new BinaryMatrix(new int[][]{rIs3Code.toBinary((int) Math.pow(2, j), 7)});
				BinaryMatrix encodedWordWithError = encodedWord.add(error);
				
				assertEquals(sourceWord, rIs3Code.decode(encodedWordWithError));
			}
		}
	}
	
	public void testEncodeDecodeWithErrors2() {

		HammingCode rIs4Code = new HammingCode(4);
		
		// All source words should be the same when encoded and decoded.
		for (int i = 0; i < 2048; i++) {
			BinaryMatrix sourceWord = new BinaryMatrix(new int[][]{rIs4Code.toBinary(i, 11)});
			BinaryMatrix encodedWord = rIs4Code.encode(sourceWord);
			
			// Try all possible weight 1 errors.
			for (int j = 0; j < 15; j++) {
				BinaryMatrix error = new BinaryMatrix(new int[][]{rIs4Code.toBinary((int) Math.pow(2, j), 15)});
				BinaryMatrix encodedWordWithError = encodedWord.add(error);
				
				assertEquals(sourceWord, rIs4Code.decode(encodedWordWithError));
			}
		}
	}
	
	public void testEncodeDecodeWithErrorsLarge() {
		
		HammingCode rIs10Code = new HammingCode(10);
		// r is 10, n is 1023, m is 1013.
		
		BinaryMatrix sourceWord = new BinaryMatrix(new int[][]{rIs10Code.toBinary(890123812, 1013)});
		BinaryMatrix encodedWord = rIs10Code.encode(sourceWord);
		
		int[][] errorArray = new int[1][1023];
		errorArray[0][839] = 1;
		BinaryMatrix error = new BinaryMatrix(errorArray);
		BinaryMatrix encodedWordWithError = encodedWord.add(error);
		
		assertEquals(sourceWord, rIs10Code.decode(encodedWordWithError));
	}
}
