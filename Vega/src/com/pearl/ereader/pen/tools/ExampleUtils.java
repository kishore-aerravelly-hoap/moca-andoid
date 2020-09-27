package com.pearl.ereader.pen.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class ExampleUtils.
 */
public class ExampleUtils {
	
	/** The tag. */
	private static String TAG="ExampleUtils class";

	/**
	 * Gets the file extension.
	 *
	 * @param f the f
	 * @return the file extension
	 */
	public static String getFileExtension(File f) 
	{
		int idx = f.getName().lastIndexOf(".");
		if (idx == -1)
			return "";
		else 
			return f.getName().substring(idx+1);
	}

	/**
	 * File name remove extension.
	 *
	 * @param fileName the file name
	 * @return the string
	 */
	public static String fileNameRemoveExtension(String fileName)
	{
		if(fileName == null)
			return null;

		int idx = fileName.lastIndexOf(".");

		if(idx == -1)
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
		StringBuilder strbuilder = new StringBuilder();

		int size = str.length();
		for(int i = 0; i < size; i++) {
			char curChar = str.charAt(i);
			if(curChar == '\\' || curChar == '/' || curChar == ':' || curChar == '*' || curChar == '?' || curChar == '"' 
				|| curChar == '<' || curChar == '>' || curChar == '|') {
				strbuilder.append('_');
			}else
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
	public static String getUniqueFilename(File folder, String filename, String ext) {
		if (folder == null || filename == null) return null;

		String curFileName;
		File curFile;

		if(filename.length() > 20){
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
	 * Gets the existing file name.
	 *
	 * @param folder the folder
	 * @param filename the filename
	 * @param ext the ext
	 * @return the existing file name
	 */
	public static String getExistingFileName(File folder,String filename,String ext){
		if(folder==null || filename==null) return null;
		String curFilename;
		File curfile;
		
		if(filename.length()>20){
			filename=filename.substring(0,19);
		}
		filename=stringCheck(filename);
		
		int i=1;
		do{
		curFilename = String.format("%s_%02d.%s", filename, i++, ext);
		curfile=new File(folder,curFilename);
			if(curfile.exists()){
					break;
				}
		}while(curfile.exists());
		return curFilename;
	}

	/**
	 * Read bytedata.
	 *
	 * @param aFilename the a filename
	 * @return the byte[]
	 */
	public static byte[] readBytedata (String aFilename) {
		byte[] imgBuffer = null;

		FileInputStream fileInputStream = null;
		try {
			File file = new File(aFilename);
			fileInputStream = new FileInputStream(file);
			int byteSize = (int)file.length();
			imgBuffer = new byte[byteSize];

			if ( fileInputStream.read(imgBuffer) == -1 ) {
				Log.e(TAG, "failed to read image");
			}
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();            
		} catch (IOException e2) {
			e2.printStackTrace();            
		} finally {
			if(fileInputStream != null) {
				try{

					fileInputStream.close();

				} catch (IOException e) {
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
	public static boolean writeBytedata (String aFilename, byte[] imgBuffer) {

		FileOutputStream fileOutputStream = null;
		boolean result = true;

		try {
			File file = new File(aFilename);
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(imgBuffer);

			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = false;
		} catch (IOException e2) {
			e2.printStackTrace();
			result = false;
		} finally {
			if(fileOutputStream != null) {
				try{

					fileOutputStream.close();

				} catch (IOException e) {
					e.printStackTrace(); 
					result = false;
				} 
			}        	
		}

		return result;
	}
	
	
	/**
	 * Check file.
	 *
	 * @param fileName the file name
	 * @param filepath the filepath
	 * @return true, if successful
	 */
	public static boolean checkFile(String fileName,File filepath){
		if(filepath==null || fileName==null) return false;
		File[] files=filepath.listFiles();
		for (File file2 : files) {
			if(file2!=null && file2.getName().startsWith(fileName)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Sets the clear image.
	 *
	 * @param filename the filename
	 * @param file the file
	 * @return true, if successful
	 */
	public static boolean setClearImage(String filename,File file){
		if(checkFile(filename,file)){
		  File[] listFiles=file.listFiles();
			  for (File file2 : listFiles) {
				if(file2.getName().startsWith(filename)){
					file2.delete();
					return true;
				}
			}
		}
		return false;
	}
}
