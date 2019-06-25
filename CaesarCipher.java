import java.lang.StringBuilder;

/**
*
* Encode or decode a message based off driver class
*
* @author cmlewis
*/

public class CaesarCipher {

    private int key;
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

        if(k == 0) {
            System.out.println("ERROR: Invalid key. Message will not be encoded properly.");
        } else {
            key = k;
            encodeMessage(message);                        
        }
    }

    private void encodeMessage(String code) {

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < code.length(); i++) {
            if(Character.isUpperCase(code.charAt(i))) {
                int c = ((((int)code.charAt(i) - UPPERCASE_ASCII_START) + key) % NUM_LETTERS_IN_ALPHABET) + UPPERCASE_ASCII_START;
                builder.append((char)c);                             
            } else if(Character.isLowerCase(code.charAt(i))){
                int c = ((((int)code.charAt(i) - LOWERCASE_ASCII_START) + key) % NUM_LETTERS_IN_ALPHABET) + LOWERCASE_ASCII_START;
                builder.append((char)c);                                                                                             
            } else {
                System.err.println("ERROR: Caesar cipher only works with characters A-Z and a-z.");
                return;
            }
        }
        encodedMessage = builder.toString();
    }

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

    public String[] decodeMessage(String message) {

        //Do stuff
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


    public String printDecodedMessage() {
        System.out.println(decodedMessage);
        return decodedMessage;
    }

    public String printEncodedMessage() {
        System.out.println(encodedMessage);
        return encodedMessage;
    }

}
