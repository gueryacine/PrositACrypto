package fr.exia.a4;

import java.util.ArrayList;
import java.util.List;

public class BitAsBooleanUtils {

	public static char binstr2char(String str)
    {
        char ret = 0;
        int v=0;
        int p=0;
        for(int i = str.length() - 1; i >= 0; i--)
        {
            if (str.charAt(i) == '1')
                v += Math.pow(2, p);
            p++;
        }
        ret = (char)v;
        return ret;
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
	
}
