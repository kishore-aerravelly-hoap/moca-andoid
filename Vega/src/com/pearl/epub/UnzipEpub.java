package com.pearl.epub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import android.util.Log;

public class UnzipEpub {
	
	public boolean extractBookToTemp(String tempPath, String bookLocation) throws InterruptedException {
		File tempfolder = new File(tempPath);
		// Creating the temp directory if does not exists
		if (tempfolder.exists()&& !tempfolder.isFile()) {
			tempfolder.deleteOnExit();
		}
		if (tempfolder.exists()) {
			System.out.println("EPUBHANDLER - file exists");
			// Toast.makeText(context, R.string.EpubUnzip,
			// Toast.LENGTH_SHORT).show();
			return true;
		} else if (!tempfolder.exists()) {
			System.out.println(" EPUBHANDLER- file doesnot exists");
			String target= renameFileExtension(bookLocation,new String("zip"));

			// Toast.makeText(context, R.string.EpubUnzip,
			// Toast.LENGTH_SHORT).show();
			

			Log.w("", "book location is:" + bookLocation);
			
			Log.w("", "Zip book location is:" + target);
			
			File bookFile= new File(target);
			ZipFile zipFile = null;
			ZipInputStream zin = null;
			Log.w("",
					"Abs path is:---------------"
							+ tempfolder.getAbsoluteFile());
			boolean status = tempfolder.mkdirs();

			System.out.println("EPUBHANDLER -"
					+ ((status) ? ("Success :") : ("Fail:"))
					+ ": creation of directories as " + tempPath);
			try {
				//zipFile = new ZipFile(bookFile);
				zin = new ZipInputStream(new FileInputStream(target));
                String workingDir = tempfolder.getAbsolutePath()+"/";

                byte[] buffer = new byte[(int)bookFile.length()];
                int bytesRead;
                ZipEntry entry = null;
                while ((entry = zin.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        File dir = new File(workingDir, entry.getName());
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        Log.i("", "[DIR] "+entry.getName());
                    } else {
                    	File f = new File(tempPath
    							+ entry.getName().replace("\\", File.separator));
    					Log.v("EPubReader", "TempPath  " + tempPath
    							+ entry.getName().replace("\\", "/"));

    					// If directory, then create and continue further
    					if (f.isDirectory()) {
    						createDir(f);
    						continue;
    					}   					
    					//else if(entry.getName().endsWith(".html")){ continue; }
    					 

    					// this is a case of file, check if parent folder exits.
    					if (!f.getParentFile().exists()) {

    						// Now that parent folder is created, continue
    						// further
    						createDir(f.getParentFile());
    					}

                        FileOutputStream fos = new FileOutputStream(workingDir + entry.getName());
                        while ((bytesRead = zin.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        zin.closeEntry();
                        fos.close();
                        Log.i("", "[FILE] "+entry.getName());
                    }
                }
                zin.close();

               
			} catch (IOException e) {
				// TODO Auto-generated catch block
				renameFileExtension(target,new String("epub"));
				Log.e("", e.toString());
			}
			renameFileExtension(target,new String("epub"));
		}
		/*
		 * } catch (Exception e) {
		 * System.out.println("In EbookReader:Exception"); DeleteRecursive(new
		 * File(tempfolder+"science-4.epub"));
		 * 
		 * return false; }
		 */
		
		
		return true;
	}
	
	
	

	private static void createDir(File dir) {
		if (dir.exists()) {
			return;
		}
		System.out.println("Creating dir " + dir.getName());
		if (!dir.mkdirs()) {
			throw new RuntimeException("Can not create dir " + dir);
		}
	}

	private void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
	}
	
	  public String renameFileExtension
	  (String source, String newExtension)
	  {
	    String target;
	    String currentExtension = getFileExtension(source);

	    if (currentExtension.equals("")){
	      target = source + "." + newExtension;
	    }
	    else {
	      target = source.replaceFirst(Pattern.quote("." +
	          currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));

	    }
	    new File(source).renameTo(new File(target));
	    return target;
	  }

	  public static String getFileExtension(String f) {
	    String ext = "";
	    int i = f.lastIndexOf('.');
	    if (i > 0 &&  i < f.length() - 1) {
	      ext = f.substring(i + 1);
	    }
	    return ext;
	  }
}
