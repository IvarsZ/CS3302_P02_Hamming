package hamming;

import java.util.Map;

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

	public void testSyndromeToErrorMap() {

		// For r = 3, the mappings should be:
		Map<Long, BinaryMatrix> syndromeToError = rIs3Code.getSyndromeToErrorMap();

		// 000 to 0000000.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 0, 0, 0}}), syndromeToError.get(new BinaryMatrix(new int[][]{{0, 0, 0}}).rowToLong(0)));

		// 001 to 0000001.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 0, 0, 1}}), syndromeToError.get(new BinaryMatrix(new int[][]{{0, 0, 1}}).rowToLong(0)));

		// 010 to 0000010.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 0, 1, 0}}), syndromeToError.get(new BinaryMatrix(new int[][]{{0, 1, 0}}).rowToLong(0)));

		// 011 to 0000011.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 0, 1, 1}}), syndromeToError.get(new BinaryMatrix(new int[][]{{0, 1, 1}}).rowToLong(0)));

		// 100 to 0000100.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 1, 0, 0}}), syndromeToError.get(new BinaryMatrix(new int[][]{{1, 0, 0}}).rowToLong(0)));

		// 101 to 0000101.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 1, 0, 1}}), syndromeToError.get(new BinaryMatrix(new int[][]{{1, 0, 1}}).rowToLong(0)));

		// 110 to 0000110.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 1, 1, 0}}), syndromeToError.get(new BinaryMatrix(new int[][]{{1, 1, 0}}).rowToLong(0)));

		// 111 to 0001000.
		assertEquals(new BinaryMatrix(new int[][]{{0, 0, 0, 0, 1, 1, 1}}), syndromeToError.get(new BinaryMatrix(new int[][]{{1, 1, 1}}).rowToLong(0)));
	}
}
