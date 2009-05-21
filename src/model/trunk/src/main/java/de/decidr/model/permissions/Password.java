package de.decidr.model.permissions;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Tool class that generates authentication keys, password hashes and password
 * salts.
 * 
 * @author Daniel Huss
 * 
 */
public class Password {

	/**
	 * Do NOT change the algorithm or password charset unless absolutely
	 * necessary. Changing the algorithm will render all users unable to log
	 * into the system until they reset their passwords.
	 */
	private static final String hashAlgorithm = "SHA-512";

	private static final String charset = "UTF-8";

	/**
	 * Character table used to create a hex string from a byte array.
	 */
	private static final byte[] hexCharTable = { (byte) '0', (byte) '1',
			(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
			(byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
			(byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f' };

	/**
	 * Character table used to create an authentication key from a byte array.
	 */
	private static final char[] alnumCharTable = { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z' };

	/**
	 * Returns a lowercase hex string representing the given raw byte array.
	 * 
	 * @param raw
	 *            raw bytes to convert
	 * @return lowercase hex representation of raw input.
	 * @throws UnsupportedEncodingException
	 *             if the system doesn't support the ASCII encoding (should
	 *             never happen)
	 */
	private static String getHexString(byte[] raw)
			throws UnsupportedEncodingException {
		byte[] hex = new byte[2 * raw.length];
		int index = 0;

		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = hexCharTable[v >>> 4];
			hex[index++] = hexCharTable[v & 0xF];
		}
		return new String(hex, "ASCII");
	}

	/**
	 * Returns an alphanumeric string representation of the given raw byte
	 * array.
	 * 
	 * @param raw
	 *            bytes that are mapped to alphanumeric characters.
	 * @return alphanumeric string
	 */
	private static String getAlnumString(byte[] raw) {
		StringBuffer buf = new StringBuffer(raw.length);

		for (byte b : raw) {
			int positive = b + Byte.MAX_VALUE + 1;
			buf.append(alnumCharTable[positive % alnumCharTable.length]);
		}

		return buf.toString();
	}

	/**
	 * Returns a hash string of the given plaintext password using the given
	 * salt, using the following algorithm:
	 * 
	 * hash := SHA512( SHA512( plaintext * salt ) )
	 * 
	 * @param plaintext
	 *            the plaintext password
	 * @param salt
	 *            the salt as generated by getRandomSalt in lowercase hex string
	 *            representation.
	 * @return the password hash in lowercase hex string representation
	 * @throws NoSuchAlgorithmException
	 *             iff the hashing algorithm SHA-512 is not available
	 * @throws UnsupportedEncodingException
	 *             iff the encodings UTF-8 or ASCII are not supported
	 */
	public static String getHash(String plaintext, String salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest digest = MessageDigest
				.getInstance(Password.hashAlgorithm);

		digest.reset();
		digest.update(salt.getBytes(Password.charset));

		byte[] result = digest.digest(plaintext.getBytes(Password.charset));

		/*
		 * result now contains the salted hash of the password. For additional
		 * security, it is hashed again (double hash).
		 */

		digest.reset();
		result = digest.digest(result);

		return getHexString(result);
	}

	/**
	 * Returns a pseudorandom authentication key consisting of up to 64
	 * lowercase alphanumerical characters.
	 * 
	 * @return The authentication key
	 * @throws NoSuchAlgorithmException
	 *             iff the hashing algorithm SHA-512 is not available
	 * @throws UnsupportedEncodingException
	 *             iff the encodings UTF-8 or ASCII are not supported
	 */
	public static String getRandomAuthKey()
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		UUID uuid = UUID.randomUUID();

		MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
		digest.reset();
		byte[] randomBytes = digest.digest(uuid.toString().getBytes(charset));

		/**
		 * randomBytes now contains 64 random bytes
		 */
		return getAlnumString(randomBytes);
	}

	/**
	 * Returns a pseudorandom salt consisting of up to 128 lowercase
	 * alphanumerical characters for use with password hashing.
	 * 
	 * @return Salt in lowercase hex string representation.
	 * @throws NoSuchAlgorithmException
	 *             iff the hashing algorithm SHA-512 is not available
	 * @throws UnsupportedEncodingException
	 *             iff the encodings UTF-8 or ASCII are not supported
	 */
	public static String getRandomSalt() throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		return getHash(UUID.randomUUID().toString(), Double.toString(Math
				.random()));
	}

}
