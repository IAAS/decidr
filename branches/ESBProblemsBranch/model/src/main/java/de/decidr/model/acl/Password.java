/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.model.acl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * Tool class that generates authentication keys, password hashes and password
 * salts.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public final class Password {

    /**
     * Do NOT change the algorithm or password charset unless absolutely
     * necessary. Changing the algorithm will render all users unable to log
     * into the system until they reset their passwords.
     */
    private static final String hashAlgorithm = "SHA-512";

    /**
     * For <strong>consistency</strong>, all password strings are converted to
     * this encoding before they are hashed.
     */
    private static final String charset = "UTF-8";

    /**
     * Character table used to create a hex string from a byte array.
     */
    private static final char[] hexCharTable = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Character table used to create a random password.
     */
    private static final char[] alnumCharTable = { '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z' };

    /**
     * Returns the used <code>{@link #hashAlgorithm}</code>.
     */
    public static final String getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * Returns a string representation of the given raw message digest.
     * <p>
     * This is a java port of the code posted by &quot;Andre D&quot; at &quot;<a
     * href="http://de2.php.net/manual/en/function.sha1.php">http://de2.php.net/
     * manual/en/function.sha1.php</a>&quot; on 09-Oct-2008 03:28.
     * 
     * @param bytes
     *            raw message digest
     * @param bitsPerCharacter
     *            bits per character, if this value is too large using the given
     *            character set, it will be adjusted to the maximum possible
     *            value for the character set.
     * @param chars
     *            character set to use. If <code>null</code> or an array that
     *            contains less than two characters is passed, all alphanumeric
     *            characters (upper and lowercase), the minus sign and the comma
     *            will be used.<br>
     *            This default character set allows for 6 bits per character.<br>
     *            <em><b><u>NOTE</u></b>: It is your responsibility to make sure
     *            that there are no duplicate characters in the provided array.</em>
     * @return the string representation of the given message digest
     */
    public static String getDigestNotation(byte[] bytes, int bitsPerCharacter,
            char[] chars) {

        if (chars == null || chars.length < 2) {
            chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-,"
                    .toCharArray();
        }

        if (bitsPerCharacter < 1) {
            // bitsPerCharacter must be at least 1
            bitsPerCharacter = 1;

        } else if (chars.length < Math.pow(2, bitsPerCharacter)) {
            // Character length of chars is too small for bitsPerCharacter
            // Set bitsPerCharacter to greatest value allowed by length of chars
            bitsPerCharacter = 1;
            int minCharLength = 2;

            while (chars.length >= (minCharLength *= 2)) {
                bitsPerCharacter++;
            }
        }

        int byteCount = bytes.length;

        StringBuffer result = new StringBuffer();

        int byteIdx = 0;
        byte currentByte = bytes[byteIdx];

        int bitsRead = 0;

        for (int i = 0; i < byteCount * Byte.SIZE / bitsPerCharacter; i++) {

            int oldBits;
            int oldBitCount;

            if (bitsRead + bitsPerCharacter > Byte.SIZE) {
                // Not enough bits remain in this currentByte for the current
                // character
                // Get remaining bits and get next byte
                oldBits = currentByte
                        - (currentByte >> Byte.SIZE - bitsRead << Byte.SIZE
                                - bitsRead);

                if (byteIdx == bytes.length - 1) {
                    // Last bits; match final character and exit loop
                    result.append(chars[oldBits]);
                    break;
                }

                oldBitCount = Byte.SIZE - bitsRead;

                byteIdx++;
                currentByte = bytes[byteIdx];
                bitsRead = 0;

            } else {
                oldBitCount = 0;
                oldBits = 0;
            }

            // Read only the needed bits from this byte
            int bits = currentByte >> Byte.SIZE
                    - (bitsRead + (bitsPerCharacter - oldBitCount));
            bits = bits
                    - (bits >> bitsPerCharacter - oldBitCount << bitsPerCharacter
                            - oldBitCount);
            bitsRead += bitsPerCharacter - oldBitCount;

            if (oldBitCount > 0) {
                // Bits come from seperate bytes, add oldBits to bits
                bits = (oldBits << bitsPerCharacter - oldBitCount) | bits;
            }

            result.append(chars[bits]);
        }

        return result.toString();
    }

    /**
     * Returns a string representation of the given raw message digest using all
     * alphanumeric characters (upper and lowercase), the minus sign and the
     * comma.
     * 
     * @param bytes
     *            raw message digest
     * @return string the string representation of the given message digest
     */
    public static String getDigestNotation(byte[] bytes) {
        return getDigestNotation(bytes, 6, null);
    }

    /**
     * Returns a hash string of the given plaintext password using the given
     * salt using the following algorithm:
     * <ul>
     * <li><code><em>ALG</em></code> represents the hashing algorithm specified
     * by <code>{@link Password#hashAlgorithm}.</code></li>
     * <li><code><em>ALG<sup>n</sup></em></code> means that the algorithm is
     * iterated n times.</li>
     * <li>data1 * data2 is an algorithm-dependent operation which causes the
     * digest to be updated with data1 before finalizing the digest using data2.
     * </li>
     * </ul>
     * <code>hash :=  <em>ALG<sup>5000</sup></em>(<em>ALG</em>( salt * plaintext ) )</code>
     * <br>
     * 
     * <p>
     * <em>Note that for consistency, the encoding of password and salt are converted to UTF-8 before
     * hashing.</em>
     * 
     * @param plaintext
     *            the plaintext password
     * @param salt
     *            the salt as generated by getRandomSalt in lowercase hex string
     *            representation.
     * @return the password hash in lowercase hex string representation
     * @throws RuntimeException
     *             <ul>
     *             <li> iff the hashing algorithm specified by <code>
     *             {@link Password#hashAlgorithm}</code> is not available</li>
     *             <li>iff the encodings UTF-8 or ASCII are not supported</li>
     *             </ul>
     */
    public static String getHash(String plaintext, String salt) {
        if (plaintext == null || salt == null) {
            throw new IllegalArgumentException(
                    "Password and salt must not be null.");
        }

        MessageDigest digest;
        byte[] result;

        try {
            digest = MessageDigest.getInstance(Password.hashAlgorithm);
            digest.reset();
            digest.update(salt.getBytes(Password.charset));

            result = digest.digest(plaintext.getBytes(Password.charset));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        /*-
         * 5000 iterations are performed to make this a more costly operation.
         *
         * (see RFC 2898: http://tools.ietf.org/html/rfc2898)
         */
        for (int iteration = 0; iteration < 5000; iteration++) {
            digest.reset();
            result = digest.digest(result);
        }

        // the hex char table can store 4 bits in a single character.
        return getDigestNotation(result, 4, hexCharTable);
    }

    /**
     * Returns a pseudorandom authentication key consisting of up to 64
     * lowercase alphanumerical characters.
     * 
     * @return The authentication key
     * @throws RuntimeException
     *             <ul>
     *             <li>iff the hashing algorithm specified by <code>
     *             {@link Password#hashAlgorithm}</code>
     *             is not available</li>
     *             <li>iff the encodings UTF-8 or ASCII are not supported</li>
     *             </ul>
     */
    public static String getRandomAuthKey() {
        UUID uuid = UUID.randomUUID();

        byte[] randomBytes;

        try {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
            digest.reset();

            randomBytes = digest.digest(uuid.toString().getBytes(charset));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        /*
         * randomBytes now contains 64 random bytes
         */
        return getDigestNotation(randomBytes, 8, alnumCharTable).substring(0,
                64);
    }

    /**
     * Returns a pseudorandom salt consisting of up to 128 lowercase
     * alphanumerical characters for use with password hashing.
     * 
     * @return Salt in lowercase hex string representation.
     * @throws RuntimeException
     *             <ul>
     *             <li>iff the hashing algorithm specified by <code>
     *             {@link Password#hashAlgorithm}</code>
     *             is not available</li>
     *             <li>iff the encodings UTF-8 or ASCII are not supported</li>
     *             </ul>
     */
    public static String getRandomSalt() {
        return getHash(UUID.randomUUID().toString(), Double.toString(Math
                .random()));
    }

    /**
     * Creates a random password string consisting of alphanumerical characters.<br>
     * Properties of the generated password:
     * <ul>
     * <li>Minimum length: 8 characters</li>
     * <li>Maximum length: 12 characters</li>
     * <li>Charset: [a-zA-Z0-9]</li>
     * </ul>
     * 
     * @return a randomly generated password
     */
    public static String generateRandomPassword() {
        int minPasswordLength = 8;
        Random rnd = new Random();
        int passwordLength = minPasswordLength + rnd.nextInt(5);

        StringBuffer buf = new StringBuffer(minPasswordLength);
        for (int i = 1; i <= passwordLength; i++) {
            char c = alnumCharTable[rnd.nextInt(alnumCharTable.length)];
            if (rnd.nextBoolean()) {
                c = Character.toUpperCase(c);
            }
            buf.append(c);
        }

        return buf.toString();
    }
}
