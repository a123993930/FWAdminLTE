package com.guy.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateUtils {

	protected static final Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

	/**
	 * 验证企业代码是否正确
	 * 
	 * @param code 企业组织机构代码
	 * @return
	 */
	public static final boolean isValidEntpCode(String code) {
		int[] ws = { 3, 7, 9, 10, 5, 8, 4, 2 };
		String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String regex = "^([0-9A-Z]){8}-[0-9|X]$";

		if (!code.matches(regex)) {
			return false;
		}
		int sum = 0;
		for (int i = 0; i < 8; i++) {
			sum += str.indexOf(String.valueOf(code.charAt(i))) * ws[i];
		}
		logger.info("sum is {}", sum);
		logger.info("sum % 11 is {}", sum % 11);

		int c9 = 11 - (sum % 11);

		String sc9 = String.valueOf(c9);
		if (11 == c9) {
			sc9 = "0";
		} else if (10 == c9) {
			sc9 = "X";
		}
		logger.info("sc9 is {}", sc9);
		return sc9.equals(String.valueOf(code.charAt(9)));
	}
	
	public static void main(String[] args) {
		boolean  bool=ValidateUtils.isValidEntpCode("40170530-0");
		if(bool){
			System.out.println("true");
		}else{
			System.out.println("false");
		}
	}
}
