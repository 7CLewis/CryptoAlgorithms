import java.math.BigInteger;
import java.util.Random;

/**
 * This program implements the Exponential ElGamal Encryption and Decryption protocol.
 * It simulates two user's - Alice and Bob - where Alice encrypts a message (the number '10'),
 * and Bob decrypts the message. 
 * 
 * @author Casey Lewis
 */
public class ExponentialElGamal {

    private BigInteger a,  // Alice's private key
                       b,  // Bob's private key
                       d,  // Decrypted message
                       gM, // Encrypted g^M
                       k,  // Masking key
                       m;  // Encrypted message
    public BigInteger  A,  // Alice's public key
                       B;  // Bob's public key
                       
    private char user; // The user - a or b
    private MathHelp h; // Helper algorithms, including Baby-Step Giant-Step and Square-and-Multiply
    private BigInteger message; //plaintext message
    public final BigInteger P = new BigInteger("94752897787332972576771231937626855546296419527501194271332736163556382139615039109709099391080884707528493653764375712455906284717243592413308442860363824495168912421406486322499870971006453780980425297566958412308494512257836293858702951870204882585218098409652818868389908241802420723664494085741452988343");
    public final BigInteger g = new BigInteger("2"); // The generator (primitive element of Z*p)
    private final int KEY_BIT_LENGTH = 1024; // Bit length of the keys
    private Random rand = new Random(); // Random number generator

    /**
     *  The Exponential ElGamal Encryption protocol
     * @param user - Alice or Bob (user1 or user2)
     */
    public ExponentialElGamal(char user) {
        this.user = user;
        h = new MathHelp();
    }

    /**
     * Calculates the private and public keys of the given user
     */
    private void calculateUserKeys() {
        if(this.user == 'a') {
            // Calculate Alice's private and public keys 
            a = BigInteger.probablePrime(KEY_BIT_LENGTH, rand).mod(P.subtract(BigInteger.valueOf(2)));
            A = h.squareAndMultiply(g, a, P);
        } else {
            // Calculate Bob's private and public keys
            b = BigInteger.probablePrime(KEY_BIT_LENGTH, rand).mod(P.subtract(BigInteger.valueOf(2)));
            B = h.squareAndMultiply(g, b, P);
        }
    }

    /**
     * Gets the requesting user's public key
     * @return A or B - the user's public key
     */
    private BigInteger getUserPublicKey() {
        if(this.user == 'a') {
            return A;
        } else {
            return B;
        }
    }

    /**
     * Find and return the user's private key.
     * This method only returns the requesting user's private key.
     * It will not allow another user to see a private key that does
     * not belong to them
     * @return a or b - the user's private key
     */
    private BigInteger getUserPrivateKey() {
        if(this.user == 'a') {
            return a;
        } else {
            return b;
        }
    }

    /**
     * Encrypt the message
     * @param M - The plaintext message to encrypt
     * @param pubKey - The user's public key
     */
    private void encryptMessage(BigInteger M, BigInteger pubKey) {
        // Calculate k - the masking key
        k = h.squareAndMultiply(pubKey, a, P);

        // // Calculate m - the encrypted message
        gM = h.squareAndMultiply(g, M, P);
        m = gM.multiply(k);
        m = m.mod(P);
    }

    /**
     * Return the encrypted message
     * @return m - the encrypted message
     */
    private BigInteger getEncryptedMessage() {
        return m;
    }

    /**
     * Decrypt the message
     * @param maskKey - The masking key that was used to encrypt
     * @param encryptedMessage - The encrypted message itself
     * @param pubKey - The other user's public key
     * @return d - The decrypted message
     */
    private BigInteger decryptMessage(BigInteger maskKey, BigInteger encryptedMessage, BigInteger pubKey) {
        // Decrypt the message
        d = P.subtract(BigInteger.ONE);
        d = d.subtract(b);
        d = h.squareAndMultiply(pubKey, d, P);
        d = d.multiply(encryptedMessage);
        d = d.mod(P);

        // d is now equal to g^m, which is the DLP. To attack this
        // and recover the original message m, we will use BSGS.
        d = h.bsgs(P, g, d);
        return d;
    }

    /**
     * Stores the plaintext message Alice will encrypt and send
     * @param m - plaintext message (BigIntegers only for this program's implementation)
     */
    private void storeMessage(BigInteger m) {
        this.message = m;
    }

    /**
     * Retrives the plaintext message Alice chose to encrypt and send.
     * Should only be used to verify that the calculation that Bob did was successful
     * @return message - the plaintext message
     */
    private BigInteger retrieveMessage() {
        return this.message;
    }

    /**
     * Returns the large, safe prime that the algorithm is based on
     * @return P - the large, safe prime
     */
    private BigInteger prime() {
        return P;
    }

    /**
     * Get the math helper for square-and-multiply and BSGS
     * @return h - the math helper
     */
    private MathHelp getMathHelper() {
        return h;
    }
 
    public static void main(String[] args) { 
        // Create a user Alice and a user Bob
        // Alice does not have access to Bob's private key, and vice-versa
        ExponentialElGamal alice = new ExponentialElGamal('a');
        ExponentialElGamal bob = new ExponentialElGamal('b');
        
        // Calculate each user's public and private keys
        alice.calculateUserKeys();
        bob.calculateUserKeys();

        // Generate and encrypt the message Alice would like to send
        alice.storeMessage(BigInteger.valueOf(42)); // Store the plaintext message for verification after Bob decrypts
        alice.encryptMessage(alice.retrieveMessage(), bob.getUserPublicKey());

        // Bob gets the ciphertext from Alice and uses his private key to decrypt it
        BigInteger encryptedMessage = alice.getEncryptedMessage();
        BigInteger alicePublicKey = alice.getUserPublicKey();
        BigInteger maskKey = bob.getMathHelper().squareAndMultiply(alicePublicKey, bob.getUserPrivateKey(), bob.prime());
        BigInteger plaintextMessage = bob.decryptMessage(maskKey, encryptedMessage, alicePublicKey);

        System.out.println("Bob has unencrypted the message and found it to be the value: " + plaintextMessage);

        // Check if the decrypted message matches the stored message
        String aliceAnswer = plaintextMessage.equals(alice.retrieveMessage()) ? "YES" : "NO";
        System.out.println("Was this the original plaintext message that Alice wanted to send? Alice says: " + aliceAnswer);
    }
}
