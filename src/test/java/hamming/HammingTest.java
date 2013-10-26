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
}
