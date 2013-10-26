package hamming;

public class BinaryMatrix {
	
	int[][] binaryMatrix;
	
	public BinaryMatrix(int numberOfRows, int numberOfColumns) {
		
		binaryMatrix = new int[numberOfRows][numberOfColumns];
	}
	
	public BinaryMatrix(int[][] binaryMatrix) {
		this.binaryMatrix = binaryMatrix;
	}

	public BinaryMatrix multiplyWith(BinaryMatrix factor) {
		
		BinaryMatrix multiplication = new BinaryMatrix(getNumberOfRows(), factor.getNumberOfColumns());
		
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
	
	public int get(int row, int column) {
		return binaryMatrix[row][column];
	}
	
	public int getNumberOfRows() {
		return binaryMatrix.length;
	}
	
	public int getNumberOfColumns() {
		return binaryMatrix[0].length;
	}
	
	@Override
	public boolean equals(Object object) {
		
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

}
