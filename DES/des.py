#!/usr/bin/env python3

# Data Encryption Standard Simulator
# Author: Casey Lewis
# Encrypts ASCII plaintext strings using plain DES

# The data for these tables was found at https://en.wikipedia.org/wiki/DES_supplementary_material 

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

# Expansion D-box Table 
r_b_expansion = [32, 1 , 2 , 3 , 4 , 5 , 4 , 5, 
		 6 , 7 , 8 , 9 , 8 , 9 , 10, 11, 
  		 12, 13, 12, 13, 14, 15, 16, 17, 
 		 16, 17, 18, 19, 20, 21, 20, 21, 
		 22, 23, 24, 25, 24, 25, 26, 27, 
		 28, 29, 28, 29, 30, 31, 32, 1 ] 
# S-box Table 
s_box = [[[14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7], 
	  [0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8], 
          [4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0], 
       	  [15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13]], 
			
	  [[15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10], 
	   [3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5], 
  	   [0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15], 
	   [13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9]], 
	
	  [[10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8], 
	   [13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1], 
	   [13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7], 
	   [1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12]], 
		
	  [[7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15], 
	   [13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9], 
	   [10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4], 
	   [3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14]], 
		
	  [[2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9], 
	   [14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6], 
	   [4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14], 
	   [11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3]], 
		
	  [[12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11], 
	   [10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8], 
	   [9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6], 
	   [4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13]], 
		
	  [[4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1], 
	   [13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6], 
	   [1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2], 
	   [6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12]], 
		
	  [[13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7], 
	   [1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2], 
	   [7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8], 
	   [2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11]]] 
	
# Permutaion Table after the S-Boxing 
p = [16, 7, 20, 21, 
     29, 12, 28, 17,
     1, 15, 23, 26, 
     5, 18, 31, 10, 
     2, 8, 24, 14, 
     32, 27, 3, 9, 
     19, 13, 30, 6, 
     22, 11, 4, 25] 

# Final Permutaion (IP -1)  Table 
final_p =  [40, 8, 48, 16, 56, 24, 64, 32, 
	    39, 7, 47, 15, 55, 23, 63, 31, 
	    38, 6, 46, 14, 54, 22, 62, 30, 
	    37, 5, 45, 13, 53, 21, 61, 29, 
	    36, 4, 44, 12, 52, 20, 60, 28, 
	    35, 3, 43, 11, 51, 19, 59, 27, 
	    34, 2, 42, 10, 50, 18, 58, 26, 
	    33, 1, 41, 9, 49, 17, 57, 25] 

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
        permutated_string += binary_string[i_p[i]-1]

    return permutated_string

def pc_1(k):

    # convert the key to binary
    k = string_to_binary(k)

    pc_1_key = ""
    for i in range(56):
        pc_1_key += k[p_c_1[i]-1]

    return pc_1_key

# Generates the subkey based off of the current round
def sub_key_generator(l, r):
    
    sk = []
    curr = ""
    left_sub_key = ""
    right_sub_key = ""

    for i in range(16):
        # Perform the appropriate amount of left shifts for the current round
        for j in range(bit_shift[i]):
            left_first_bit = l[0]
            left_sub_key = l[1:]
            left_sub_key += left_first_bit
            right_first_bit = r[0]
            right_sub_key = r[1:]
            right_sub_key += right_first_bit
            l = left_sub_key
            r = right_sub_key
        # Combine the two key parts
        curr = l + r
        # Use the PC-2 Table to create the subkey
        sk.append(pc_2(curr))
        curr = ""
    
    return sk

def pc_2(sub):

    p_c_2_key = ""
    # Use the p_c_2 table to create the subkey
    for i in range(48):
        p_c_2_key += sub[p_c_2[i]-1]

    return p_c_2_key

# Uses the expansion table to turn the right plaintext block from 32 to 48 bits
def right_block_expansion(right_block):
    r_b = ""
    for i in range(48):
        r_b += right_block[r_b_expansion[i] - 1]

    return r_b

# Performs an XOR operation on two equal-length binary strings
def x_or(b, k):
    res = ""
    for i in range (len(b)):
        if b[i] == k[i]:
            res += "0"
        else:
            res += "1"

    return res

def permutate(perm):
    val = ""
    for i in range(32):
        val += perm[p[i] - 1]

    return val

# Uses the S-Box and P tables to generate the 32-bit output
def s_box_with_perm(block):
    s = ""
    for index in range(8):
        block_start = 6 * index
        i = int(block[block_start] + block[block_start + 5],2)
        j = int(block[block_start + 1] + block[block_start + 2] + block[block_start + 3] + block[block_start + 4],2)
        s_choice = bin(s_box[index][i][j]).replace("0b", "")
       # print(" ", index, " ", i, " ", j)
        while len(s_choice) < 4:
            s_choice = '0' + s_choice
        s += s_choice

    # Use the P table to permutate the result of the S-Boxing
    s = permutate(s)

    return s

def final_permute(fin): 
    final_result = ""
    for i in range(64):
        final_result += fin[final_p[i] - 1]
    return final_result


# Binary to hexadecimal conversion
def binary_to_hex(s):
    return hex(int(s,2)).replace("0x","")

# Runs through the DES process to encrypt the block
def encrypt(pt):

    # Convert the user's plaintext block to binary
    pt = string_to_binary(pt)

    # Run the Initial Permutation (IP) on the binary plaintext
    pt = initial_permutation(pt)
    
    # Split the permutated text block into two halves
    left_block = pt[:32]
    right_block = pt[32:]

    key = "sh0wC@s3"
    
    key = pc_1(key)

    left_key = key[:28]
    right_key = key[28:]

    # Generate the sub key
    sub_key = sub_key_generator(left_key, right_key)
    
    # Perform all 16 rounds of encryption
    for i in range(16):
        
        left_ph = right_block
        
        #Expand the right plaintext block to 48 bits
        right_block = right_block_expansion(right_block)
        
        # XOR the expanded RPT with the subkey
        right_block = x_or(right_block, sub_key[i])

        # Use the S-Box and permutation tables
        right_block = s_box_with_perm(right_block)
        
        # XOR the S-Box result with the original LPT block
        right_block = x_or(right_block, left_block)
        
        # Set the left block equal to the original right block
        left_block = left_ph
 
        if i == 15:
            left_block, right_block = right_block, left_block

    result = left_block + right_block

    result = final_permute(result)
    
    return result


def main():
    
    print("Welcome to the DES Simulator!")
    
    # Have the user input the plaintext block to be encrypted
    plaintext = input("Please enter an ASCII string to encrypt: \n")

    # Pad the block with the NULL character to fill the block (if necessary)
    while len(plaintext) % 8 != 0:
        plaintext += '\0'
    
    ciphertext_blocks = []

    # Encrypt each 8-bit block of plaintext
    for i in range (int(len(plaintext) / 8)):
        curr_plaintext = plaintext[i*8:(i*8)+8]
        ciphertext_blocks.append(encrypt(curr_plaintext))

    # Print the results
    print("Cipher Text: ")    
    for j in range (len(ciphertext_blocks)):
        print(binary_to_hex(ciphertext_blocks[j]))

main()
