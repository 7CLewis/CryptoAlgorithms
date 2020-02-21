import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
* Create an RSA encoding of a packet
* 
* @author 7CLewis
*/
public class RSA {

    private long p, q, n, d, phi, e;
    private short k;
    private int[] M;
    private long[] C;

    public RSA(String message)
    {
        //Convert message to number packets
        M = convertMessageToPackets(message);

        //Generate p and q
        //p = generateRandomPrime(0);
        //q = generateRandomPrime(p);

        p=7;
        q=3;

        //Compute n    
        n = p*q;

        //Compute phi
        phi = (p-1) * (q-1);

        //Compute e
        e = findValidE(phi);

        //Encrypt Message Packets with public key 'e'
        C = encryptPackets(M, e, n);
        
        // Compute d - the private key used for decryption
        k = 2;
        d = (1 + (k*phi))/e; 

    }

    private long generateRandomPrime(long i) {
        long prime;
        boolean done = false;
        while(!done){
            prime = ThreadLocalRandom.current().nextLong(100) + 1000;
            if(isPrime(prime) && prime != i){
                done = true;
                return prime;
            } else {
                if(prime % 2 == 0)
                    prime += 1;
                else 
                    prime += 2;
            }
        }

        return 0;
    }

    private long findValidE(long num) {
        e = 2;
        while(e < num) {
            if(coPrime(e,phi)==1) {
                return e;
            } else {
                e++;
            }
        }
        return 0;
    }

    private long coPrime(long num1, long num2) {
            // Everything divides 0  
            if (num1 == 0 || num2 == 0) 
                return 0; 
              
            // base case 
            if (num1 == num2) 
                return num1; 
              
            // num1 is greater than num1
            if (num1 > num2) 
                return coPrime(num1-num2, num2); 
                      
            //num2 is greater than num1
            return coPrime(num1, num2-num1);
    }

    private int[] convertMessageToPackets(String msg) {
        //Convert each char/group of chars to ints
        int[] packets = new int[msg.length()];
        for(int i = 0; i < msg.length(); i++){
            packets[i] = (int)msg.charAt(i);
        }
        return packets;
    }


    /**
     * Check if a number is prime
     * @return true if the given number is prime, false otherwise.
     */
    private boolean isPrime(long num) {
        if ( num > 2 && num%2 == 0 ) {
            return false;
        }
        long end = (long)Math.sqrt(num) + 1;
        for(int i = 3; i < end; i+=2){
            if(num % i == 0){
                return false;
            }
        }
        return true; 
    }

    private long[] encryptPackets(int[] packetArray, long exp, long num) {

        long[] encryptedArray = new long[packetArray.length];
        for(int i = 0; i < packetArray.length; i++){
            encryptedArray[i] = ((long)Math.pow(packetArray[i], exp)) % num;
        }
        return encryptedArray;

    }

    public void printEncryptedPackets() {
        for(int i = 0; i < C.length; i++){
            System.out.println("Packet #" + (i+1) + ": " + C[i] + "\n");
        }
    }

}
