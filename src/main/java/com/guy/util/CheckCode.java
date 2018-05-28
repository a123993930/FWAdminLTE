package com.guy.util;

import java.util.HashMap;
import java.util.Map;

public class CheckCode {

	public static String getCheckCode(String sCode) {
		sCode = sCode.toUpperCase();
		int[] wi = { 3, 7, 9, 10, 5, 8, 4, 2 };
		int sum = 0;
		String[] strKey = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
				"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };
		int[] values = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
				32, 33, 34, 35 };
		String strCheck = "";

		Map<String,Integer> hashData = null;
		hashData = new HashMap<String,Integer> ();

		for (int j = 0; j < strKey.length; j++) {
			hashData.put(strKey[j], Integer.valueOf(values[j]));
		}

		for (int i = 0; i < 8; i++) {
			strCheck = sCode.substring(i, i + 1);
			sum += wi[i] * hashData.get(strCheck).intValue();
		}
		sum = 11 - sum % 11;
		if (sum == 10)
			strCheck = sCode + "X";
		else if (sum == 11)
			strCheck = sCode + Integer.toString(0);
		else {
			strCheck = sCode + Integer.toString(sum);
		}
		return strCheck;
	}

	public static boolean isCheckCode(String aStrCode) {
		String strOrigin = "";
		if ((aStrCode == null) || (aStrCode.length() < 9)) {
			return false;
		}
		if (aStrCode.length() >= 9)
			strOrigin = aStrCode.substring(0, 8).toUpperCase();
		else {
			return false;
		}

		boolean hasSymble = !aStrCode.matches("^[\\da-zA-Z]*$");
		if (hasSymble) {
			return false;
		}

		strOrigin = getCheckCode(strOrigin);
		return strOrigin.equals(aStrCode);
	}

	public static String getIDCheckCode(String sCode) {
		int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
		int sum = 0;
		String[] ai = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		String strCheck = "";

		for (int i = 0; i < 17; i++) {
			strCheck = sCode.substring(i, i + 1);
			sum += wi[i] * Integer.parseInt(strCheck);
		}
		sum %= 11;

		strCheck = sCode + ai[sum];

		return strCheck;
	}

	public static boolean isIDCheckCode(String aStrCode) {
		String strOrigin = "";
		strOrigin = aStrCode.substring(0, 17);
		strOrigin = getIDCheckCode(strOrigin);
		return strOrigin.equals(aStrCode);
	}
	
	public static void main(String[] args) {
		String code=CheckCode.getCheckCode("MA07K001");//34763726 40170530
		System.out.println(code);
	}

}