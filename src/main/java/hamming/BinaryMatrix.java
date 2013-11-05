package hamming;


public class BinaryMatrix {

	private int[][] binaryMatrix;

	public BinaryMatrix(int numberOfRows, int numberOfColumns) {

		binaryMatrix = new int[numberOfRows][numberOfColumns];
	}

	public BinaryMatrix(int[][] binaryMatrix) {
		this.binaryMatrix = binaryMatrix;
	}

	public BinaryMatrix add(BinaryMatrix error) {

		// Add entries at the same positions.
		int[][] sum = new int[getNumberOfRows()][getNumberOfColumns()];
		for (int row = 0; row < getNumberOfRows(); row++) {
			for (int column = 0; column < getNumberOfColumns(); column++) {

				sum[row][column] = (get(row, column) + error.get(row, column)) % 2;
			}
		}

		return new BinaryMatrix(sum);
	}

	public BinaryMatrix multiplyWith(BinaryMatrix factor) {

		BinaryMatrix multiplication = new BinaryMatrix(getNumberOfRows(), factor.getNumberOfColumns());

		// Multiply the rows of the first matrix with the columns of the second.
		for (int row = 0; row < getNumberOfRows(); row++) {
			for (int column = 0; column < factor.getNumberOfColumns(); column++) {

				int multiplicationSum = 0;
				for (int i = 0; i < getNumberOfColumns(); i++) {
					multiplicationSum += binaryMatrix[row][i]*factor.binaryMatrix[i][column];
				}
				multiplication.set(row, column, multiplicationSum);
			}
		}

		return multiplication;
	}

	public void print() {

		System.out.println();
		for (int row = 0; row < getNumberOfRows(); row++) {
			for (int column = 0; column < getNumberOfColumns(); column++) {
				System.out.print(get(row, column) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void set(int row, int column, int value) {
		binaryMatrix[row][column] = value % 2;
	}
	
	public void flip(int row, int column) {
		if (get(row, column) == 1) {
			set(row, column, 0); 
		}
		else {
			set(row, column, 1); 
		}
	}

	public int get(int row, int column) {
		return binaryMatrix[row][column];
	}

	public int getNumberOfRows() {
		return binaryMatrix.length;
	}

	public int getNumberOfColumns() {
		return binaryMatrix[0].length;
	}

	public int rowToInt(int row) {

		int rowAsLong = 0;
		int powerOf2 = 1;
		for (int column = getNumberOfColumns() - 1; column >= 0; column--) {

			rowAsLong += get(row, column) * powerOf2;
			powerOf2 *= 2;
		}

		return rowAsLong;
	}

	@Override
	public boolean equals(Object object) {

		// It is equal to binary matrices of the same size with the same entries.
		if (object instanceof BinaryMatrix) {

			BinaryMatrix binaryMatrix = (BinaryMatrix) object;
			if (binaryMatrix.getNumberOfRows() == getNumberOfRows() &&
					binaryMatrix.getNumberOfColumns() == getNumberOfColumns()) {

				for (int row = 0; row < getNumberOfRows(); row++) {
					for (int column = 0; column < getNumberOfColumns(); column++) {
						if (get(row, column) != binaryMatrix.get(row, column)) {
							return false;
						}
					}
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public BinaryMatrix clone() {

		BinaryMatrix clone = new BinaryMatrix(getNumberOfRows(), getNumberOfColumns());
		for (int row = 0; row < getNumberOfRows(); row++) {
			for (int column = 0; column < getNumberOfColumns(); column++) {
				clone.set(row, column, get(row, column));
			}
		}
		
		return clone;
	}
}
