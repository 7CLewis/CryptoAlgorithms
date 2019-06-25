import java.util.Scanner;

/**
 *
 * Driver to test the Caesar Cipher.
 * 
 * @author cmlewis
 *
 */

public class CaesarCipherDriver {
    public static void main(String[] args) {
                
        boolean successful = false;
        boolean validString = false;
        boolean validKey = false;
        System.out.println("Welcome to CAESAR CIPHER!");
        System.out.println("Would you like to ENCODE or DECODE a message?\n");
        System.out.println("Type E for ENCODE, or D for DECODE.");
        Scanner scan = new Scanner(System.in);
        while(!successful) {
            if(scan.next().toLowerCase().charAt(0) == 'e')
            {
                successful = true;
                System.out.println("Please enter the message that will be encoded (Characters 'A-Z' and 'a-z' ONLY)");
                String msg = scan.next();
                while(validString) {
                    //Do the A-Z and a-z only check HERE
                    System.out.println("Please enter the message that will be encoded (Characters 'A-Z' and 'a-z' ONLY, and no spaces)");
                }
                System.out.println("Please enter the key for the encoding, or type '?' for help");
                String s = scan.next();
                while(!validKey) {
                    if(s.equals("?")) {
                        showHelpMenu();
                        System.out.println("Please enter the key for the encoding, or type '?' for help");
                    } else if(Integer.parseInt(s) != 0) { //OR however you test to see if a string can be converted to an int or not
                        validKey = true;
                        int key = Integer.parseInt(s);
                        CaesarCipher cc = new CaesarCipher(msg, key);
                        System.out.println("Your message has been succesfully encoded. Here is the encoded version: \n");
                        String encoded = cc.printEncodedMessage();
                        System.out.println("\nIf you would like to see your original message, type 'M'. Otherwise, type 'exit' to finish.");
                        String end = scan.next().toLowerCase();
                        while(!end.equals("exit")) {
                            if(end.equals("m")) {
                                cc.decodeMessage(encoded, key);
                                cc.printDecodedMessage();
                                System.out.println("\nRun the program again if you would like to encode another message. Goodbye.");
                                return;
                            } else {
                                System.out.println("\nIf you would like to see your original message, type 'M'. Otherwise, type 'exit' to finish");
                            }
                        }
                    }
                }
            } else if(scan.next().toLowerCase().charAt(0) == 'd') {
                successful = true;
                System.out.println("Please enter the message that you would like to decode (Characters 'A-Z' and 'a-z' ONLY)");
                String msg = scan.next();
                //Do the A-Z and a-z only check HERE
                CaesarCipher cc = new CaesarCipher(msg);
                String[] possibilities = cc.decodeMessage(msg);
                System.out.println("There 26 possible messages. Please look through and find the correct solution (The one that looks most like English): \n");
                int i = 1;
                for(String s: possibilities) {
                    System.out.println("#" + i + ": " + s);
                    i++;
                }
                System.out.println("If none of the solutions look like a readable message, then you can conclude that the Caesar Cipher was not (or at least, not the ONLY) algorithm used to encode the message");
            } else {
                System.err.println("ERROR: Invalid entry. Please type 'E' or 'D'");
            }
        }

    }

    private static void showHelpMenu() {
        System.out.println("Caesar Cipher Key: \n SOME EXPLANATION");                            
    }
}
