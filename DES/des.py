#!/usr/bin/env python3

key = "ABCDEFGH"

# Initial Permutation Table (Not to be confused with iptables)
i_p = [58, 50, 42, 34, 26, 18, 10, 2,
       60, 52, 44, 36, 28, 20, 12, 4,
       62, 54, 46, 38, 30, 22, 14, 6,
       64, 56, 48, 40, 32, 24, 16, 8,
       57, 49, 41, 33, 25, 17, 9, 1,
       59, 51, 43, 35, 27, 19, 11, 3,
       61, 53, 45, 37, 29, 21, 13, 5,
       63, 55, 47, 39, 31, 23, 15, 7]

# Permuted Choice 2 (PC-2) Table 
p_c_2 = [14, 17, 11, 24, 1, 5, 
         3, 28, 15, 6, 21, 10, 
         23, 19, 12, 4, 26, 8, 
         16, 7, 27, 20, 13, 2, 
         41, 52, 31, 37, 47, 55, 
         30, 40, 51, 45, 33, 48, 
         44, 49, 39, 56, 34, 53, 
         46, 42, 50, 36, 29, 32 ]

# Permuted Choice 1 (PC-1) Table 
p_c_1 = [57, 49, 41, 33, 25, 17, 9, 
	1, 58, 50, 42, 34, 26, 18, 
	10, 2, 59, 51, 43, 35, 27, 
	19, 11, 3, 60, 52, 44, 36, 
	63, 55, 47, 39, 31, 23, 15, 
	7, 62, 54, 46, 38, 30, 22, 
	14, 6, 61, 53, 45, 37, 29, 
	21, 13, 5, 28, 20, 12, 4] 

# For rounds 1-16, the number of left shifts to perform 
bit_shift = [1, 1, 2, 2, 
	     2, 2, 2, 2, 
	     1, 2, 2, 2, 
	     2, 2, 2, 1 ] 

# Converts the given input string to a binary string, keeping the length at 64 bits
def string_to_binary(ascii_string):
     binary_string = bin(int.from_bytes(ascii_string.encode(), 'big')).replace("0b", "")
     while(len(binary_string) < 64):
         binary_string = '0' + binary_string

     return binary_string

# Runs the Initial Permutation (IP) on the binary string
def initial_permutation(binary_string):
    permutated_string = ""
    for i in range(64):
        permutated_string += binary_string[ip_table[i]-1]

    return permutated_string

def pc_1(k):

    # convert the key to binary
    k = string_to_binary(k)

    pc_1_key = ""
    for i in range(56):
        pc_1_key += k[p_c_1[i]]

    return pc_1_key

# Generates the subkey based off of the current round
def sub_key_generator(r_num, l, r):
    
    sk = ""
    left_sub_key = ""
    right_sub_key = ""

    # Perform the appropriate amount of left shifts for the current round
    for i in range(1, bit_shift[r_num]):
        left_first_bit = l[0]
        left_sub_key = l << 1
        left_sub_key += left_first_bit
        right_first_bit = r[0]
        right_sub_key = r << 1
        right_sub_key += right_first_bit

    # Combine the two key parts
    sk = left_sub_key + right_sub_key
    
    # Use the PC-2 Table to create the subkey
    sk = pc_2(sk)
    
    return sk

def pc_2(sub):

    p_c_2_key = ""
    # Use the p_c_2 table to create the subkey
    for i in range(48):
        p_c_2_key += sub[p_c_2[i]]

    return p_c_2_key

# Performs 1 round of encryption
def encryption_round(round_num, lk, rk, lb, rb):
    sub_key = sub_key_generator(round_num, lk, rk)


# Runs through the DES process to encrypt the block
def encrypt(pt):

    # Convert the user's plaintext block to binary
    pt = string_to_binary(pt)
    print(pt)

    # Run the Initial Permutation (IP) on the binary plaintext
    pt = initial_permutation(pt)

    # Split the permutated text block into two halves
    left_block = pt[:32]
    right_block = pt[32:]

    key = pc_1(key)

    left_key = key[:28]
    right_key = key[28:]

    # Perform all 16 rounds of encryption
    for i in range(16):
        encyption_round(i, left_key, right_key, left_block, right_block)

def main():
    
    print("Welcome to the DES Simulator!")
    
    # Have the user input the plaintext block to be encrypted
    plaintext = input("Please enter an ASCII string to encrypt: \n")

    while (len(plaintext) != 8):
        plaintext = input("Error: please enter an ASCII string of length 8\n")
    
    encrypt(plaintext);

main()
