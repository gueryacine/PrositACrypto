package fr.exia.a4;

import java.util.ArrayList;
import java.util.List;

public class SDES_BitAsBoolean implements ICipher<String, String> {

	@Override
	public String encryption(String data, String key) {
		
		// On convertir la cha�ne de donn�es en tableau de boolean (des bits pour simplifier)
		boolean[] bits_block = byte2bits(data);
		
		// On convertit la cha�ne de caract�re de la cl� en tableau de boolean (des bits pour simplifier)
		boolean[] masterKey = char2bin(key);
		if (masterKey.length != 10) {
			throw new IllegalArgumentException("Key must have a length of 10 chars");
		}
		
		return null;
	}

	private boolean[] byte2bits(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	private static boolean[] char2bin(String key) {
		boolean[] output = new boolean[key.length()];
        for (int i = 0; i < key.length(); i++)
        {
        	output[i] = char2bin(key.charAt(i));
        }
        return output;
	}

	public static boolean char2bin(char bit)
    {
        if (bit == '0') return false;
        else if (bit == '1') return true;
        else throw new RuntimeException("Key must be in binary string format [0,1]");
    }
    
    public static boolean[] circularLeftShift(boolean[] key, int bits)
    {
        boolean[] shifted = new boolean[key.length];
        for (int index = 0, i = bits; index < key.length; i++)
        {
            shifted[index++] = key[i % key.length]; 
        }
        return shifted;
    }
    
    public static List<boolean[]> generateKeys(boolean[] masterKey)
    {
    	// Tableau qui va contenir K1 et K2
        List<boolean[]> keys = new ArrayList<>(2);
        
        // On applique P10 et on s�pare en deux tableaux de 5 bits
        List<boolean[]> temp = splitBlock(P10(masterKey));
        
        // On applique P8 sur 
        keys.add(P8(circularLeftShift(temp.get(0), 1), circularLeftShift(temp.get(1), 1)));
        keys.add(P8(circularLeftShift(temp.get(0), 3), circularLeftShift(temp.get(1), 3)));
        
        return keys;
    }
    
	public static List<boolean[]> splitBlock(boolean[] block) {
		
		// On pr�pare deux tableaux
		List<boolean[]> splited = new ArrayList<>(2);
        splited.add(new boolean[block.length / 2]);
        splited.add(new boolean[block.length / 2]);
        
        // On split en deux
        for(int i = 0, middle = block.length / 2; i < block.length; i++)
        {
        	// TODO V�rifier le bon fonctionnement du ceil
            splited.get(i >= middle ? 0 : 1)[i] = block[i];
        }
        return splited;
	}
	
	public static boolean[] IP(boolean[] plainText) {
		// Indice dans key : 1 2 3 4 5 6 7 8
        // Permutation     : 2 6 3 1 4 8 5 7
        boolean[] permutatedArray = new boolean[8];
        permutatedArray[0] = plainText[1];
        permutatedArray[1] = plainText[5];
        permutatedArray[2] = plainText[2];
        permutatedArray[3] = plainText[0];
        permutatedArray[4] = plainText[3];
        permutatedArray[5] = plainText[7];
        permutatedArray[6] = plainText[4];
        permutatedArray[7] = plainText[6];
        return permutatedArray;
	}
	
	public static boolean[] reverseIP(boolean[] permutedText) {
		// Indice dans key : 1 2 3 4 5 6 7 8
        // Permutation     : 4 1 3 5 7 2 8 6
        boolean[] permutatedArray = new boolean[8];
        permutatedArray[0] = permutedText[3];
        permutatedArray[1] = permutedText[0];
        permutatedArray[2] = permutedText[2];
        permutatedArray[3] = permutedText[0];
        permutatedArray[4] = permutedText[3];
        permutatedArray[5] = permutedText[7];
        permutatedArray[6] = permutedText[4];
        permutatedArray[7] = permutedText[6];
        return permutatedArray;
	}
	
	/**
	 * La fonction de permutation est d�finie telle que :
	 * 	P10(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10) = (k3, k5, k2, k7, k4, k10, k1, k9, k8, k6)
	 * 
	 * @param key
	 * @return
	 */
    public static boolean[] P10(boolean[] key)
    {
    	// Indice dans key : 1 2 3 4 5 6  7 8 9 10
        // Permutation     : 3 5 2 7 4 10 1 9 8 6
        boolean[] permutatedArray = new boolean[10];
        permutatedArray[0] = key[2];
        permutatedArray[1] = key[4];
        permutatedArray[2] = key[1];
        permutatedArray[3] = key[6];
        permutatedArray[4] = key[3];
        permutatedArray[5] = key[9];
        permutatedArray[6] = key[0];
        permutatedArray[7] = key[8];
        permutatedArray[8] = key[7];
        permutatedArray[9] = key[5];
        return permutatedArray;
    }
    
    public static boolean[] P8(boolean[] part1, boolean[] part2)
    {
    	// Indice dans key : 1 2 3 4 5 6 7 8
        // Permutation     : 5 2 6 3 7 4 9 8
        boolean[] permutatedArray = new boolean[8];
        permutatedArray[0] = part2[0];// 5
        permutatedArray[1] = part1[2];// 2
        permutatedArray[2] = part2[1];// 6
        permutatedArray[3] = part1[3];// 3
        permutatedArray[4] = part2[2];// 7
        permutatedArray[5] = part1[4];// 4
        permutatedArray[6] = part2[4];// 9
        permutatedArray[7] = part2[3];// 8
        return permutatedArray;
    }
    
    /**
     *  E/P (n1, n2, n3, n4) = (n4, n1, n2, n3, n2, n3, n4, n1)
     * 
     * @param input
     * @return
     */
    public static boolean[] EP(boolean[] input)
    {
        boolean[] permutatedArray = new boolean[8];
        permutatedArray[0] = input[3];
        permutatedArray[1] = input[0];
        permutatedArray[2] = input[1];
        permutatedArray[3] = input[2];
        permutatedArray[4] = input[1];
        permutatedArray[5] = input[2];
        permutatedArray[6] = input[3];
        permutatedArray[7] = input[0];
        return permutatedArray;
    }
    
    public static boolean[] xor(boolean[] a, boolean[] b)
    {
    	// On fabrique un tableau de la taille maximale de deux tableaux a ou b
    	// Initialement, toutes les valeurs valent FALSE
        boolean[] result = new boolean[Math.max(a.length, b.length)];
        
        // On parcours les deux tableaux jusqu'� la fin du plus petit
        for (int i = 0, min = Math.min(a.length, b.length); i < min; i++) 
        {
        	result[i] = ((a[i] && b[i]) || (!a[i] && !b[i])) ? false : true;
        }
        return result;
    }
    
    /**
     * Renvoie un tableau de booleans (assimilables � des bits) correspondant
     * aux valeurs des bits de l'octet pass� en param�tre.
     * 
     * @param block
     * @return
     */
    public static boolean[] byte2bits(char block)
    {
    	boolean result[] = new boolean[8];
        int c = block;
        // On parcours les 8 bits en partant des bits de poids forts (la gauche)
        for (int p = 7, i = 0; p >= 0; p--, i++) {
        	// Si le bit actuel est � 1
            if (c - Math.pow(2, p) >= 0)
            {
                result[i] = true;
                // On d�calle au bit pr�c�dent (toujours en partant de la gauche)
                c -= Math.pow(2, p);
            }   
            else result[i] = false;
        }
        return result;
    }
    
    /**
     * Renvoie une cha�ne de caract�re compos�e de 0 et de 1 correspondant aux
     * valeurs des bits de l'octet pass� en param�tre.
     * 
     * @param block
     * @return
     */
    public static String char2binstr(char block)
    {
        String ret = "";
        int c = block;
        // On parcours les 8 bits en partant des bits de poids forts (la gauche)
        for (int p = 7; p >= 0; p--) {
        	// Si le bit actuel est � 1
            if (c - Math.pow(2, p) >= 0)
            {
                ret += "1";
                // On d�calle au bit pr�c�dent (toujours en partant de la gauche)
                c -= Math.pow(2, p);
            }   
            else ret += "0";
        }
        return ret;
    }

	@Override
	public String decryption(String data, String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
