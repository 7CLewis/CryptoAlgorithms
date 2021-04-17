import java.math.BigInteger;

/**
 * The program simulates the RSA encryption and decryption
 * protocol using valid 1024-bit keys. It simulates two users -
 * Alice and Bob. Alice chooses a message (in this case, the number
 * 10), and encrypts it securely using her private key. Bob then
 * takes the encrypted message and securely decrypts it using his
 * public key.
 * 
 * @author Casey Lewis
 */
public class RSA {

    // Our RSA values, as given in the textbook
    private final BigInteger P = new BigInteger("E0DFD2C2A288ACEBC705EFAB30E4447541A8C5A47A37185C5A9CB98389CE4DE19199AA3069B404FD98C801568CB9170EB712BF10B4955CE9C9DC8CE6855C6123", 16);
    private final BigInteger Q = new BigInteger("EBE0FCF21866FD9A9F0D72F7994875A8D92E67AEE4B515136B2A778A8048B149828AEA30BD0BA34B977982A3D42168F594CA99F3981DDABFAB2369F229640115", 16);
    private final BigInteger N = P.multiply(Q);
    private final BigInteger E = new BigInteger("40B028E1E4CCF07537643101FF72444A0BE1D7682F1EDB553E3AB4F6DD8293CA1945DB12D796AE9244D60565C2EB692A89B8881D58D278562ED60066DD8211E67315CF89857167206120405B08B54D10D4EC4ED4253C75FA74098FE3F7FB751FF5121353C554391E114C85B56A9725E9BD5685D6C9C7EED8EE442366353DC39", 16);
    private final BigInteger D = new BigInteger("C21A93EE751A8D4FBFD77285D79D6768C58EBF283743D2889A395F266C78F4A28E86F545960C2CE01EB8AD5246905163B28D0B8BAABB959CC03F4EC499186168AE9ED6D88058898907E61C7CCCC584D65D801CFE32DFC983707F87F5AA6AE4B9E77B9CE630E2C0DF05841B5E4984D059A35D7270D500514891F7B77B804BED81", 16);

    private BigInteger message; // Plaintext message
    private SquareAndMultiply sm; //Helper equations for the modular exponentiation

    /**
     * Instantiate the math helper to user the Square-and-Multiply algorithm
     */
    public RSA() {
        sm = new SquareAndMultiply();
    }

    /**
     * Store the plaintext message in order to verify that
     * Bob successfully decrypted the message
     * @param msg
     */
    private void storeMessage(BigInteger msg) {
        this.message = msg;
    }

    private BigInteger retrieveMessage() {
        return this.message;
    }

    /**
     * Encrypt the message using the user's private key
     * @return the encrypted message
     */
    private BigInteger encryptMessage() {
        BigInteger encryptedMessage = sm.squareAndMultiply(message, E, N);
        return encryptedMessage;
    }

    /**
     * Decrypt the message using the user's private key
     * @return the decrypted message
     */
    private BigInteger decryptMessage(BigInteger encryptedMessage) {
        BigInteger decryptedMessage = sm.squareAndMultiply(encryptedMessage, D, N);
        return decryptedMessage;
    }

    public static void main(String[] args) {
        RSA alice = new RSA();
        RSA bob = new RSA();

        alice.storeMessage(BigInteger.valueOf(77)); // The plaintext message Alice wants to send is the number 77
        BigInteger enc = alice.encryptMessage(); // Alice securely encrypts her plaintext message using her private key
        BigInteger dec = bob.decryptMessage(enc); // Bob securely decrypts the encrypted message using his private key

        System.out.println("Bob has decrypted the message and determined it to be the value: " + dec);

        // If the decrypted message Bob computes is indeed the message alice wanted to send, Alice replies with YES
        String answer = dec.equals(alice.retrieveMessage()) ? "YES" : "NO";
        System.out.println("Was this the original plaintext message that Alice wanted to send? Alice says: " + answer);
    }
}
