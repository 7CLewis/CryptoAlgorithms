import java.util.Scanner;

/**
 * 
 * 
 * 
 */
public class RSADriver {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the RSA algorithm creator/decoder. Please enter a message that you would like to encode.");
        String s = scan.next();
        System.out.println("Generating code... Please wait.");
        RSA rsa = new RSA(s);
        rsa.printEncryptedPackets();

    }

}
