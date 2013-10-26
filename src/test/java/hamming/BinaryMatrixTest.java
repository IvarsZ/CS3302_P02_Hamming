package hamming;

import junit.framework.TestCase;

public class BinaryMatrixTest extends TestCase {
	
	public void testMultiplication() {
		
		BinaryMatrix factorA = new BinaryMatrix(3, 2);
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 2; column++) {
				factorA.set(row, column, (row + column) % 2);
			}
		}
		
		BinaryMatrix factorB = new BinaryMatrix(2, 4);
		for (int row = 0; row < 2; row++) {
			for (int column = 0; column < 4; column++) {
				factorB.set(row, column, column % 2);
			}
		}
		
		BinaryMatrix multiplication = factorA.multiplyWith(factorB);
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 4; column++) {
				assertEquals(column % 2, multiplication.get(row, column));
			}
		}
	}

}
