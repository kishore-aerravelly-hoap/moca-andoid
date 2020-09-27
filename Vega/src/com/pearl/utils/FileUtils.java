package com.pearl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUtils.
 */
public class FileUtils {

    /**
     * Gets the file extension.
     *
     * @param f the f
     * @return the file extension
     */
    public static String getFileExtension(File f) {
	final int idx = f.getName().indexOf(".");
	if (idx == -1)
	    return "";
	else
	    return f.getName().substring(idx + 1);
    }

    /**
     * File name remove extension.
     *
     * @param fileName the file name
     * @return the string
     */
    public static String fileNameRemoveExtension(String fileName) {
	if (fileName == null)
	    return null;

	final int idx = fileName.indexOf(".");

	if (idx == -1)
	    return fileName;

	else
	    return fileName.substring(0, idx);
    }

    /**
     * String check.
     *
     * @param str the str
     * @return the string
     */
    public static String stringCheck(String str) {
	final StringBuilder strbuilder = new StringBuilder();

	final int size = str.length();
	for (int i = 0; i < size; i++) {
	    final char curChar = str.charAt(i);
	    if (curChar == '\\' || curChar == '/' || curChar == ':'
		    || curChar == '*' || curChar == '?' || curChar == '"'
		    || curChar == '<' || curChar == '>' || curChar == '|') {
		strbuilder.append('_');
	    } else
		strbuilder.append(curChar);
	}
	return strbuilder.toString();
    }

    /**
     * Gets the unique filename.
     *
     * @param folder the folder
     * @param filename the filename
     * @param ext the ext
     * @return the unique filename
     */
    public static String getUniqueFilename(File folder, String filename,
	    String ext) {
	if (folder == null || filename == null)
	    return null;

	String curFileName;
	File curFile;

	if (filename.length() > 20) {
	    filename = filename.substring(0, 19);
	}

	filename = stringCheck(filename);

	int i = 1;
	do {
	    curFileName = String.format("%s_%02d.%s", filename, i++, ext);
	    curFile = new File(folder, curFileName);
	} while (curFile.exists());
	return curFileName;
    }

    /**
     * Read bytedata.
     *
     * @param aFilename the a filename
     * @return the byte[]
     */
    public static byte[] readBytedata(String aFilename) {
	byte[] imgBuffer = null;

	FileInputStream fileInputStream = null;
	try {
	    final File file = new File(aFilename);
	    fileInputStream = new FileInputStream(file);
	    final int byteSize = (int) file.length();
	    imgBuffer = new byte[byteSize];

	    if (fileInputStream.read(imgBuffer) == -1) {
		Log.e("FileUtils", "failed to read image");
	    }
	    fileInputStream.close();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	} catch (final IOException e2) {
	    e2.printStackTrace();
	} finally {
	    if (fileInputStream != null) {
		try {

		    fileInputStream.close();

		} catch (final IOException e) {
		    e.printStackTrace();
		}
	    }
	}

	return imgBuffer;
    }

    /**
     * Write bytedata.
     *
     * @param aFilename the a filename
     * @param imgBuffer the img buffer
     * @return true, if successful
     */
    public static boolean writeBytedata(String aFilename, byte[] imgBuffer) {

	FileOutputStream fileOutputStream = null;
	boolean result = true;

	try {
	    final File file = new File(aFilename);
	    fileOutputStream = new FileOutputStream(file);
	    fileOutputStream.write(imgBuffer);

	    fileOutputStream.close();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    result = false;
	} catch (final IOException e2) {
	    e2.printStackTrace();
	    result = false;
	} finally {
	    if (fileOutputStream != null) {
		try {

		    fileOutputStream.close();

		} catch (final IOException e) {
		    e.printStackTrace();
		    result = false;
		}
	    }
	}

	return result;
    }
}
