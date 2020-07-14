package com.peachkite.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.peachkite.exception.CryptoException;

public class CryptoUtils {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final String ALGO = "AES";
	private static final String SECRET_KEY = "993m83js8yutgf@#";
	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static String encrypt(String input) throws CryptoException{
		try{
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(input.getBytes());
			Base64.getEncoder().encodeToString(encVal);
			return Base64.getEncoder().encodeToString(encVal);
		}catch(Exception e){
			throw new CryptoException(e);
		}
	}

	public static String decrypt(String encryptedData) throws CryptoException {
		try{
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue);
			return decryptedValue;
		}catch(Exception e){
			throw new CryptoException(e);
		}
	}
	
	public static String calculateBase64EncodedHMACSHA1Signature(String data, String key) {
		String result = null;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			result = Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*public static void main(String args[])throws Exception{
		System.out.println(decrypt("bk5t3rgm7BAMNXWNQD0v0Q=="));
	}*/

	public static String generateMD5Hash(String input){
		MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(encodeBytesToHexString(digest.digest(input.getBytes())));
	}
	
	public static String generateMD5Hash(byte[] input){
		MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(encodeBytesToHexString(digest.digest(input)));
	}
	
	public static char[] encodeBytesToHexString(byte[] bytes) {
		final int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];

		int j = 0;
		for (int i = 0; i < nBytes; i++) {
			// Char for top 4 bits
			result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
			// Bottom 4
			result[j++] = HEX[(0x0F & bytes[i])];
		}

		return result;
	}

	public static byte[] decodeHexToBytes(CharSequence s) {
		int nChars = s.length();

		if (nChars % 2 != 0) {
			throw new IllegalArgumentException(
					"Hex-encoded string must have an even number of characters");
		}

		byte[] result = new byte[nChars / 2];

		for (int i = 0; i < nChars; i += 2) {
			int msb = Character.digit(s.charAt(i), 16);
			int lsb = Character.digit(s.charAt(i + 1), 16);

			if (msb < 0 || lsb < 0) {
				throw new IllegalArgumentException("Non-hex character in input: " + s);
			}
			result[i / 2] = (byte) ((msb << 4) | lsb);
		}
		return result;
	}
	
	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGO);
		return key;
	}
}

