package com.pearl.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * �* @author rdamazio �* @see http://code.google.com/p/android-notifier/ �
 */

public class PearlSecurity {

    /** The max key length. */
    public static int MAX_KEY_LENGTH = DESedeKeySpec.DES_EDE_KEY_LEN;
    
    /** The encryption key type. */
    private static String ENCRYPTION_KEY_TYPE = "DESede";
    
    /** The encryption algorithm. */
    private static String ENCRYPTION_ALGORITHM = "DESede/CBC/PKCS5Padding";
    
    /** The key spec. */
    private final SecretKeySpec keySpec;
    
    /** The x. */
    static String X = "x@y!a~b@$s";
    
    /** The y. */
    static String Y = "t*u&k$$t$v";
    
    /** The z. */
    static String Z = "3$25tjk$#@";

    /**
     * Read from file.
     *
     * @param fileName the file name
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public byte[] readFromFile(String fileName) throws IOException {
	Logger.info("ps", "PARSING: Pearl Security - File to read is:"
		+ fileName);
	final File file = new File(fileName);
	final FileInputStream fileInputStream = new FileInputStream(file);
	final byte fileContent[] = new byte[(int) file.length()];
	fileInputStream.read(fileContent);
	return fileContent;
    }

    /**
     * Instantiates a new pearl security.
     *
     * @param passphrase the passphrase
     */
    public PearlSecurity(String passphrase) {
	byte[] key;
	try {
	    // get bytes representation of the password
	    key = passphrase.getBytes("UTF8");
	} catch (final UnsupportedEncodingException e) {
	    throw new IllegalArgumentException(e);
	}

	key = padKeyToLength(key, MAX_KEY_LENGTH);
	keySpec = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
    }

    // !!! - see post below
    /**
     * Pad key to length.
     *
     * @param key the key
     * @param len the len
     * @return the byte[]
     */
    private byte[] padKeyToLength(byte[] key, int len) {
	final byte[] newKey = new byte[len];
	System.arraycopy(key, 0, newKey, 0, Math.min(key.length, len));
	return newKey;
    }

    // standard stuff
    /**
     * Encrypt.
     *
     * @param unencrypted the unencrypted
     * @return the byte[]
     * @throws GeneralSecurityException the general security exception
     */
    public byte[] encrypt(byte[] unencrypted) throws GeneralSecurityException {
	return doCipher(unencrypted, Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypt.
     *
     * @param encrypted the encrypted
     * @return the byte[]
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException the bad padding exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws NoSuchProviderException the no such provider exception
     * @throws NoSuchPaddingException the no such padding exception
     * @throws InvalidKeyException the invalid key exception
     * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
     */
    public byte[] decrypt(byte[] encrypted) throws IllegalBlockSizeException,
    BadPaddingException, NoSuchAlgorithmException,
    NoSuchProviderException, NoSuchPaddingException,
    InvalidKeyException, InvalidAlgorithmParameterException {
	byte[] decryptedText = null;
	decryptedText = doCipher(encrypted, Cipher.DECRYPT_MODE);

	return decryptedText;
    }

    /**
     * Do cipher.
     *
     * @param original the original
     * @param mode the mode
     * @return the byte[]
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException the bad padding exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws NoSuchProviderException the no such provider exception
     * @throws NoSuchPaddingException the no such padding exception
     * @throws InvalidKeyException the invalid key exception
     * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
     */
    private byte[] doCipher(byte[] original, int mode)
	    throws IllegalBlockSizeException, BadPaddingException,
	    NoSuchAlgorithmException, NoSuchProviderException,
	    NoSuchPaddingException, InvalidKeyException,
	    InvalidAlgorithmParameterException {
	byte[] decryptedContent = null;
	final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM, "BC");
	// IV = 0 is yet another issue, we'll ignore it here
	final IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0,
		0, 0 });
	cipher.init(mode, keySpec, iv);

	try {
	    Logger.warn("ps", "doCipher() - encrpyted content is:" + original);
	    decryptedContent = cipher.doFinal(original);
	} catch (final Exception e) {
	    Logger.error("Pearl Security", "DRM - " + e);
	    decryptedContent = null;
	}
	return decryptedContent;
    }

    /**
     * Write to file.
     *
     * @param fileName the file name
     * @param data the data
     */
    private void writeToFile(String fileName, byte[] data) {
	try {
	    // Create file
	    final FileOutputStream fileOutputStream = new FileOutputStream(fileName);
	    fileOutputStream.write(data);

	    // Close the output stream
	    fileOutputStream.close();

	} catch (final Exception e) {// Catch exception if any
	    System.err.println("Error: " + e.getMessage());
	}
    }

    /**
     * Gets the salt.
     *
     * @param tabletId the tablet id
     * @param studentId the student id
     * @return the salt
     */
    public static String getSalt(String tabletId, String studentId) {
	return X + tabletId + Y + studentId + Z;
    }

    /**
     * Gets the salt for login.
     *
     * @param tabletId the tablet id
     * @return the salt for login
     */
    public static String getSaltForLogin(String tabletId) {
	return X + tabletId + Y + Z;
    }
}