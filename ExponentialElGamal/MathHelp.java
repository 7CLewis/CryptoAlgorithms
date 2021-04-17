import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Helper functions for some of the math used in ElGamal.
 * Also includes the Baby-Step Giant-Step attack to check if the
 * ElGamal implementation is vulnerable to that attack.
 */
public class MathHelp {
    
	private BigInteger y;
	private final int MAX_ARRAY_LENGTH = 100;

	public MathHelp() {}

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

	/**
	 * Implements the Baby-Step Giant-Step attack algorithm, which
	 * can be used to find the discrete log of an integer h
	 * @param p - the prime number that h is not divisible by
	 * @param g - the primitive root modulo p
	 * @param h - the integer we are trying to find the discrete log of
	 * @return the discrete log of h, or -1 if not found
	 */
	public BigInteger bsgs(BigInteger p, BigInteger g, BigInteger h) {
		BigInteger m = p.sqrt().add(BigInteger.ONE);
		BigInteger arrLength = m.intValue() > MAX_ARRAY_LENGTH ? BigInteger.valueOf(MAX_ARRAY_LENGTH) : m;
		BigInteger[] gValues = new BigInteger[arrLength.intValue()];
		gValues[0] = BigInteger.ONE;
		for(BigInteger i = BigInteger.ONE; i.compareTo(arrLength) < 0;)
		{
			gValues[i.intValue()] = squareAndMultiply(g, i, p);
			/* 
			 * If we discover the exponent within the first sqrt(m) tries,
			 * we are finished and can return that value. 
			 */
			if(gValues[i.intValue()].compareTo(h) == 0)
				return i;

			i = i.add(BigInteger.ONE);
		}

		BigInteger multiplier = squareAndMultiply(extendedEuclidian(p, g), m, p); //g^-m
		boolean done = false;
		BigInteger q = BigInteger.valueOf(-1);
		BigInteger gCollision = BigInteger.valueOf(-1);
		while(!done && q.compareTo(arrLength) < 0) {
			q = q.add(BigInteger.ONE);
			// Compute a value, then check all of the g values for a match
			BigInteger possibleCollision = (h.multiply(squareAndMultiply(multiplier, q, p))).mod(p);
			for(BigInteger j = BigInteger.ZERO; j.compareTo(arrLength) < 0;) {
				if(possibleCollision.equals(gValues[j.intValue()])) {
					gCollision = j;
					done = true;
					break;
				}
				j = j.add(BigInteger.ONE);
			}
		}

		if(!done) {
			System.out.println("Error: Could not find the discrete log in 100 tries. Cannot attempt more due to memory space. Exiting program.");
			System.exit(-1);
		}

		BigInteger discreteLog = q.multiply(m).add(gCollision).mod(p);

		return discreteLog;
	}

	/**
	 * Implements the extended Euclidian algorithm to find t
	 * @param a - first number
	 * @param b - second number
	 * @return t[i] - the modular inverse of b
	 */
	public BigInteger extendedEuclidian(BigInteger a, BigInteger b) {
		BigInteger[] r = new BigInteger[10];
		BigInteger[] q = new BigInteger[10];
		BigInteger[] s = new BigInteger[10];
		BigInteger[] t = new BigInteger[10];

		//Set initial values
		r[0] = a;
		r[1] = b;
		s[0] = BigInteger.ONE;
		s[1] = BigInteger.ZERO;
		t[0] = BigInteger.ZERO;
		t[1] = BigInteger.ONE;

		int i = 2;
		boolean done = false;
		while(i < 10 && !done)
		{
			r[i] = (r[i-2].mod(r[i-1])).mod(a);
			q[i-1] = ((r[i-2].subtract(r[i])).divide(r[i-1])).mod(a);
			s[i] = (s[i-2].subtract(q[i-1].multiply(s[i-1]))).mod(a);
			t[i] = (t[i-2].subtract(q[i-1].multiply(t[i-1]))).mod(a);
			if(t[i].compareTo(BigInteger.ZERO) < 0)
				t[i] = t[i].add(a);

			if(r[i].equals(BigInteger.ONE))
				break;
			i++;
		}
		return t[i];
	}

	
    public static void main(String[] args) {
		/* Use these 2 tests of an input of your choosing to test the program */
		MathHelp h = new MathHelp();
		System.out.println("Square and Multiply: " + h.squareAndMultiply(BigInteger.valueOf(26), BigInteger.valueOf(7), BigInteger.valueOf(990))); // Correct answer: 556
		System.out.println("Baby-Step Giant Step with p=31, g=3, h=6: " + h.bsgs(BigInteger.valueOf(31), BigInteger.valueOf(3), BigInteger.valueOf(6))); // Correct answer: 25
	}    
}