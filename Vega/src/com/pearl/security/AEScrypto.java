package com.pearl.security;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.util.ByteArrayBuffer;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * TODO Currently supports only UTF-8 data.
 */
public class AEScrypto {
    
    /**
     * Encrypt.
     *
     * @param message the message
     * @param password the password
     * @return the byte[]
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws NoSuchPaddingException the no such padding exception
     * @throws InvalidKeyException the invalid key exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException the bad padding exception
     */
    public static byte[] encrypt(byte[] message, String password)
	    throws UnsupportedEncodingException, NoSuchAlgorithmException,
	    NoSuchPaddingException, InvalidKeyException,
	    IllegalBlockSizeException, BadPaddingException {
	final SecretKeySpec skeySpec = getSecretKeySpec(password);

	// Initiating the Cipher
	final Cipher cipher = Cipher.getInstance("AES");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

	// encrypting the message
	final byte[] encrypted = cipher.doFinal(message);

	return encrypted;
    }

    /**
     * Decrypt.
     *
     * @param encrypted the encrypted
     * @param password the password
     * @return the byte[]
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws NoSuchPaddingException the no such padding exception
     * @throws InvalidKeyException the invalid key exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException the bad padding exception
     */
    public static byte[] decrypt(byte[] encrypted, String password)
	    throws UnsupportedEncodingException, NoSuchAlgorithmException,
	    NoSuchPaddingException, InvalidKeyException,
	    IllegalBlockSizeException, BadPaddingException {
	final SecretKeySpec skeySpec = getSecretKeySpec(password);

	// Initiating the Cipher
	final Cipher cipher = Cipher.getInstance("AES");
	cipher.init(Cipher.DECRYPT_MODE, skeySpec);

	return cipher.doFinal(encrypted);
    }

    /**
     * Encrypt.
     *
     * @param filePath the file path
     * @param password the password
     * @return the byte[]
     */
    public static byte[] encrypt(String filePath, String password) {
	try {
	    return encrypt(readFileAsByteArray(filePath), password);
	} catch (final Exception e) {
	    Logger.error("AES CRYPTO", e);
	}

	return null;
    }

    /**
     * Decrypt.
     *
     * @param filePath the file path
     * @param password the password
     * @return the byte[]
     */
    public static byte[] decrypt(String filePath, String password) {
	try {
	    return decrypt(readFileAsByteArray(filePath), password);
	} catch (final Exception e) {
	    Logger.error("AES CRYPTO", e);
	}

	return null;
    }

    /**
     * Read decrypted file as stream.
     *
     * @param filePath the file path
     * @param password the password
     * @return the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InvalidKeyException the invalid key exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws NoSuchPaddingException the no such padding exception
     * @throws IllegalBlockSizeException the illegal block size exception
     * @throws BadPaddingException the bad padding exception
     */
    public static InputStream readDecryptedFileAsStream(String filePath,
	    String password) throws IOException, InvalidKeyException,
	    NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException {
	byte[] data = readFileAsByteArray(filePath);

	data = decrypt(data, password);

	return new ByteArrayInputStream(data);
    }

    /**
     * Read file as byte array.
     *
     * @param filePath the file path
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static byte[] readFileAsByteArray(String filePath)
	    throws IOException {
	FileInputStream input;
	final ByteArrayBuffer baf = new ByteArrayBuffer(150);

	input = new FileInputStream(filePath);

	int count = 0;
	final byte data[] = new byte[1024];
	while ((count = input.read(data)) != -1) {
	    baf.append(data, 0, count);
	}
	input.close();

	return baf.toByteArray();
    }

    /**
     * Read file as stream.
     *
     * @param filePath the file path
     * @return the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static InputStream readFileAsStream(String filePath)
	    throws IOException {
	final byte[] data = readFileAsByteArray(filePath);

	return new ByteArrayInputStream(data);
    }

    /**
     * Write file.
     *
     * @param data the data
     * @param folder the folder
     * @param filename the filename
     * @return true, if successful
     */
    private static boolean writeFile(byte[] data, String folder, String filename) {
	try {
	    if (!isFileExistsInLocal(folder)) {
		final File fileFolder = new File(folder);

		fileFolder.mkdirs();

		fileFolder.setWritable(true);
		fileFolder.setReadable(true);
	    }

	    if (folder == null) {
		folder = "";
	    }

	    final File newFile = new File(folder + filename);

	    final FileOutputStream fos = new FileOutputStream(newFile);
	    fos.write(data);
	    fos.close();

	    newFile.setWritable(true);
	    newFile.setReadable(true);
	} catch (final Exception e) {
	    Logger.warn("save:", e.toString());

	    return false;
	}

	return true;
    }

    /**
     * HELPER FUNCTIONS.
     *
     * @param password the password
     * @return the secret key spec
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    private static SecretKeySpec getSecretKeySpec(String password)
	    throws UnsupportedEncodingException, NoSuchAlgorithmException {
	byte[] key = password.getBytes("UTF-8");
	final MessageDigest sha = MessageDigest.getInstance("SHA-1");
	key = sha.digest(key);
	key = Arrays.copyOf(key, 16); // use only first 128 bit

	// Preparing the SecretKeySpec of AES type to get and initialize the
	// cipher of AES type
	return new SecretKeySpec(key, "AES");
    }

    /**
     * Checks if is file exists in local.
     *
     * @param filename the filename
     * @return true, if is file exists in local
     */
    protected static boolean isFileExistsInLocal(String filename) {
	final File file = new File(filename);
	if (file.exists()) {
	    return true;
	}

	return false;
    }
}
