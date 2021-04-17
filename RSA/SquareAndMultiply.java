import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Helper functions for some of the math used in RSA.
 *
 * @author Casey Lewis
 */
public class SquareAndMultiply {

    
	private BigInteger y;
	private final int MAX_ARRAY_LENGTH = 100;

	/**
	 * Fast exponentiation using the Square-and-Multiply shortcut
	 * @param x - the base integer
	 * @param H - the exponent 
	 * @param n - the modulus
	 * @return the exponentiated value
	 */
	public BigInteger squareAndMultiply(BigInteger x, BigInteger H, BigInteger n) {
		ArrayList<BigInteger> hBinary = new ArrayList<BigInteger>();
        hBinary = convertToBinary(H);
        y = x;
        for (int i = hBinary.size() - 2; i >= 0; i--) {
            y = y.modPow(BigInteger.TWO, n);
            if(hBinary.get(i).equals(BigInteger.ONE)) y = (y.multiply(x)).mod(n);
        }

		return y;
	}

	/**
	 * Converts a given number to binary
	 */
	public ArrayList<BigInteger> convertToBinary(BigInteger num) {

		BigInteger n;
		BigInteger a;

		ArrayList<BigInteger> binaryNumber = new ArrayList<BigInteger>();
		n = num;
		while (n.compareTo(BigInteger.ZERO) > 0) {
			a = n.mod(BigInteger.TWO);
			binaryNumber.add(a);
			n = n.divide(BigInteger.TWO);
		}

		return binaryNumber;
	}

    public static void main(String[] args) {
		/* Use these 2 tests of an input of your choosing to test the program */
		SquareAndMultiply h = new SquareAndMultiply();
		System.out.println("Square and Multiply: " + h.squareAndMultiply(BigInteger.valueOf(26), BigInteger.valueOf(7), BigInteger.valueOf(990))); // Correct answer: 556
	}    

}
