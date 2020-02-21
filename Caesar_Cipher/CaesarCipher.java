import java.lang.StringBuilder;

/**
*
* Encode or decode a message using the Caesar Cipher
*
* @author 7CLewis
*/

public class CaesarCipher {

    private int key; //The encryption key
    private final int NUM_LETTERS_IN_ALPHABET = 26;
    private final int UPPERCASE_ASCII_START = 65;
    private final int LOWERCASE_ASCII_START = 97;
    private String encodedMessage;
    private String decodedMessage;
    private String[] decodedMessagePossibilities = new String[NUM_LETTERS_IN_ALPHABET]; 

    public CaesarCipher(String message) {
        encodedMessage = message;                    
    }

    public CaesarCipher(String message, int k) {

        if(k % NUM_LETTERS_IN_ALPHABET == 0) {
            System.out.println("ERROR: Invalid key. Message will not be encoded properly."); //Shifting the characters by a factor of 26 will result in no change from the original message
        } else {
            key = k;
            encodeMessage(message);                        
        }
    }

    /**
     * Encode a message using the given string and the key
     * @param String code - the message to encode 
     */
    private void encodeMessage(String code) {

        StringBuilder builder = new StringBuilder();
        //Loop through the given string, one character at a time
        for(int i = 0; i < code.length(); i++) {
        	/* If the character is uppercase, shift it's ASCII value forward by 'key' % 26
        	 * The '% 26' part keeps it within the ASCII bounds [A,Z]
        	 */
            if(Character.isUpperCase(code.charAt(i))) {
                int c = ((((int)code.charAt(i) - UPPERCASE_ASCII_START) + key) % NUM_LETTERS_IN_ALPHABET) + UPPERCASE_ASCII_START;
                builder.append((char)c);
            } else if(Character.isLowerCase(code.charAt(i))){ // Do the same thing, but for lowercase ASCII values
                int c = ((((int)code.charAt(i) - LOWERCASE_ASCII_START) + key) % NUM_LETTERS_IN_ALPHABET) + LOWERCASE_ASCII_START;
                builder.append((char)c);                                                                                             
            } else {
            	// This version of the program only works with [A-Za-z]
                System.err.println("ERROR: Caesar cipher only works with characters A-Z and a-z.");
                return;
            }
        }
        encodedMessage = builder.toString();
    }

    /**
     * Decode a message using the given key
     * NOTE: Must know the encryption key to use this method
     * If the encryption key is not known, use the other decodeMessage method
     * @param String message - the encrypted message
     * @param int key - the encryption key
     */
    public void decodeMessage(String message, int key)
    {
        if(key == 0){
            decodeMessage(message);
        } else {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < message.length(); i++) {
                if(Character.isUpperCase(message.charAt(i))){
                    int c = ((((int)message.charAt(i) - UPPERCASE_ASCII_START) - key) % NUM_LETTERS_IN_ALPHABET);
                    c = c < 0 ? (c + NUM_LETTERS_IN_ALPHABET + UPPERCASE_ASCII_START) : c + UPPERCASE_ASCII_START;
                    builder.append((char) c);
                } else if(Character.isLowerCase(message.charAt(i))) {
                    int c = ((((int)message.charAt(i) - LOWERCASE_ASCII_START) - key) % NUM_LETTERS_IN_ALPHABET);
                    c = c < 0 ? (c + NUM_LETTERS_IN_ALPHABET + LOWERCASE_ASCII_START) : c + LOWERCASE_ASCII_START;
                    builder.append((char) c);
                }                                                
            }
            decodedMessage = builder.toString();
        }                                                                              
    }

    /**
     * Prints out every possible Caesar Cipher combination
     * The user will need to evaluate the 26 strings to determine
     * which one most closely resembles a possible message
     * @param String message - the encrypted message
     */
    public String[] decodeMessage(String message) {

    	// Creates 26 different strings, each representing a possible plaintext message
        for(int i = 1; i <= NUM_LETTERS_IN_ALPHABET; i++) {
            StringBuilder builder = new StringBuilder();
            key = i;
            for(int j = 0; j < message.length(); j++) {
                if(Character.isUpperCase(message.charAt(j))){
                    int c = ((((int)message.charAt(j) - UPPERCASE_ASCII_START) - key) % NUM_LETTERS_IN_ALPHABET);
                    c = c < 0 ? (c + NUM_LETTERS_IN_ALPHABET + UPPERCASE_ASCII_START) : c + UPPERCASE_ASCII_START;
                    builder.append((char) c);
                } else if(Character.isLowerCase(message.charAt(j))) {
                    int c = ((((int)message.charAt(j) - LOWERCASE_ASCII_START) - key) % NUM_LETTERS_IN_ALPHABET);
                    c = c < 0 ? (c + NUM_LETTERS_IN_ALPHABET + LOWERCASE_ASCII_START) : c + LOWERCASE_ASCII_START;
                    builder.append((char) c);
                }
            }
            decodedMessagePossibilities[i-1] = builder.toString();
        }
        return decodedMessagePossibilities;
    }


    /**
     * Prints the decoded message to stdout
     * @return the decoded message String
     */
    public String printDecodedMessage() {
        System.out.println(decodedMessage);
        return decodedMessage;
    }

    /**
     * Prints the encoded message to stdout
     * @return the decoded message String
     */
    public String printEncodedMessage() {
        System.out.println(encodedMessage);
        return encodedMessage;
    }

}

