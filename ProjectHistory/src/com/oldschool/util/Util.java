package com.oldschool.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

	public static String encriptarPass(String username, String pass) throws Exception{
		//Implantar semilla
		String newPass = username + pass + username;
		//Encriptar MD5
		String newEncPass = Util.generateMD5(newPass);
		return newEncPass;
	}
	
	public static String generateMD5(String input) {
		try {
			 MessageDigest md = MessageDigest.getInstance("MD5");
			 byte[] messageDigest = md.digest(input.getBytes());
			 BigInteger number = new BigInteger(1, messageDigest);
			 String hashtext = number.toString(16);
			 
			 while (hashtext.length() < 32) {
				 hashtext = "0" + hashtext;
			 }
			 return hashtext;
		 }catch (NoSuchAlgorithmException e) {
			 throw new RuntimeException(e);
		 }
	 }
	
	public static boolean isEmpty(String cadena){
		if(cadena!=null && cadena.length()>0){
			return false;
		}else{
			return true;
		}
	}

}
