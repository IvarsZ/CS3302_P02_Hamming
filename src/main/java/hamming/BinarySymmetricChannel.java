package hamming;

public class BinarySymmetricChannel {

	public static BinaryMatrix sendWord(BinaryMatrix sentWord, double errorChance) {

		BinaryMatrix receivedWord = sentWord.clone();
		for (int column = 0; column < sentWord.getNumberOfColumns(); column++) {
			double r = Math.random();
			if (r < errorChance) {
				receivedWord.flip(0, column);
			}
		}
		
		return receivedWord;
	}
}
