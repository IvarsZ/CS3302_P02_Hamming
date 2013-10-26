package hamming;

import junit.framework.TestCase;

public class HammingTest extends TestCase {

	public void testParityCheckMatrix() {

		// For r = 3, the matrix should be:
		int[][] expected = {{0, 1, 1},
							{1, 0, 1},
							{1, 1, 0},
							{1, 1, 1},
							{1, 0, 0},
							{0, 1, 0},
							{0, 0, 1}
						   };

		assertEquals(new BinaryMatrix(expected), new HammingCode(3).getParityCheckMatrix());		
	}
}
