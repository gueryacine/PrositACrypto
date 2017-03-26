package fr.exia.a4;

import java.util.ArrayList;
import java.util.List;

import fr.exia.a4.utils.ICipher;
import fr.exia.a4.utils.Utils;

public class SDES_BitAsBoolean implements ICipher<String, String> {

	private boolean[][][] s_box1 = new boolean[4][4][2];
	private boolean[][][] s_box2 = new boolean[4][4][2];

	public SDES_BitAsBoolean() {
		initSandbox();
	}

	private void initSandbox() {
		boolean b0[] = new boolean[2];
		b0[0] = false;
		b0[1] = false;

		boolean b1[] = new boolean[2];
		b0[0] = false;
		b0[1] = true;

		boolean b2[] = new boolean[2];
		b0[0] = true;
		b0[1] = false;

		boolean b3[] = new boolean[2];
		b0[0] = true;
		b0[1] = true;

		s_box1[0][0] = b1;
		s_box1[0][1] = b0;
		s_box1[0][2] = b3;
		s_box1[0][3] = b2;

		s_box1[1][0] = b3;
		s_box1[1][1] = b2;
		s_box1[1][2] = b1;
		s_box1[1][3] = b0;

		s_box1[2][0] = b0;
		s_box1[2][1] = b2;
		s_box1[2][2] = b1;
		s_box1[2][3] = b3;

		s_box1[3][0] = b3;
		s_box1[3][1] = b1;
		s_box1[3][2] = b3;
		s_box1[3][3] = b2;
		// ---------------------
		s_box2[0][0] = b0;
		s_box2[0][1] = b1;
		s_box2[0][2] = b2;
		s_box2[0][3] = b3;

		s_box2[1][0] = b2;
		s_box2[1][1] = b0;
		s_box2[1][2] = b1;
		s_box2[1][3] = b3;

		s_box2[2][0] = b3;
		s_box2[2][1] = b0;
		s_box2[2][2] = b1;
		s_box2[2][3] = b0;

		s_box2[3][0] = b2;
		s_box2[3][1] = b1;
		s_box2[3][2] = b0;
		s_box2[3][3] = b3;
		// ---------------------
	}

	@Override
	public String encryption(String data, String key) {

		// On convertir la cha�ne de donn�es en tableau de boolean (des bits
		// pour simplifier)
		boolean[] bits_block = Utils.byte2bits(data);

		// On convertit la cha�ne de caract�re de la cl� en tableau de boolean
		// (des bits pour simplifier)
		boolean[] masterKey = Utils.char2bin(key);
		if (masterKey.length != 10) {
			throw new IllegalArgumentException("Key must have a length of 10 chars");
		}
		
		//ciphertext = IP-1( fK2 ( SW (fK1 (IP (plaintext)))))
		
		

		return null;
	}
	
	private char encryption(boolean[] bits_block, boolean[] masterKey) {
        List<boolean[]> keys = generateKeys(masterKey);
        //ciphertext = IP-1(FK(Switch(FK(IP(plaintext)))))
        return 0;
	}

	public static List<boolean[]> generateKeys(boolean[] masterKey) {
		// Tableau qui va contenir K1 et K2
		List<boolean[]> keys = new ArrayList<>(2);

		// On applique P10 et on s�pare en deux tableaux de 5 bits
		List<boolean[]> temp = Utils.splitBlock(P10(masterKey));

		// On applique P8 sur
		keys.add(P8(Utils.circularLeftShift(temp.get(0), 1), Utils.circularLeftShift(temp.get(1), 1)));
		keys.add(P8(Utils.circularLeftShift(temp.get(0), 3), Utils.circularLeftShift(temp.get(1), 3)));

		return keys;
	}

	public static boolean[] IP(boolean[] plainText) {
		// Indice dans key : 1 2 3 4 5 6 7 8
		// Permutation : 2 6 3 1 4 8 5 7
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
		// Permutation : 4 1 3 5 7 2 8 6
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
	 * La fonction de permutation est d�finie telle que : P10(k1, k2, k3, k4,
	 * k5, k6, k7, k8, k9, k10) = (k3, k5, k2, k7, k4, k10, k1, k9, k8, k6)
	 * 
	 * @param key
	 * @return
	 */
	public static boolean[] P10(boolean[] key) {
		// Indice dans key : 1 2 3 4 5 6 7 8 9 10
		// Permutation : 3 5 2 7 4 10 1 9 8 6
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

	public static boolean[] P8(boolean[] part1, boolean[] part2) {
		// Indice dans key : 1 2 3 4 5 6 7 8
		// Permutation : 5 2 6 3 7 4 9 8
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
	 * E/P (n1, n2, n3, n4) = (n4, n1, n2, n3, n2, n3, n4, n1)
	 * 
	 * @param input
	 * @return
	 */
	public static boolean[] EP(boolean[] input) {
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

	/**
	 * TODO Doc
	 * 
	 * @param part1
	 * @param part2
	 * @return
	 */
	public static boolean[] P4(boolean[] part1, boolean[] part2) {
		// Indice dans key : 1 2 3 4
		// Permutation : 0,1 1,1 1,0 0,0
		boolean[] permutatedArray = new boolean[4];
		permutatedArray[0] = part1[1];// 0,1
		permutatedArray[1] = part2[1];// 1,1
		permutatedArray[2] = part2[0];// 1,0
		permutatedArray[3] = part1[0];// 0,0
		return permutatedArray;
	}

	/**
	 * 
	 * @param right
	 * @param sk
	 *            sous-cl�
	 * @return
	 */
	public boolean[] F(boolean[] right, boolean[] sk) {
		// On applique ep sur right
		boolean[] a = EP(right);

		// On effectue un OU exclusif entre le r�sultat obtenu et la sous-cl� sk
		boolean[] b = Utils.xor(a, sk);

		// On divise
		List<boolean[]> temp = Utils.splitBlock(b);

		// On effectue les op�rations des sand-boxes sur chaque moiti� obtenue
		boolean[] sb1 = getSandBoxes(temp.get(0), s_box1);
		boolean[] sb2 = getSandBoxes(temp.get(0), s_box2);

		// On applique P4 et on renvoie le r�sultat
		return P4(sb1, sb2);
	}

	public static boolean[] getSandBoxes(boolean[] block, boolean[][][] sandBox) {
		return sandBox[Utils.binstr2char(Utils.bin2str(block[0]) + Utils.bin2str(block[3]))]
				[Utils.binstr2char(Utils.bin2str(block[1]) + Utils.bin2str(block[2]))];
	}

	@Override
	public String decryption(String data, String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
