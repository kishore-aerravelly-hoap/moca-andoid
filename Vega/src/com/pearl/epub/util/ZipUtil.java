package com.pearl.epub.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class ZipUtil.
 */
public class ZipUtil {

    /** The Constant TAG. */
    private static final String TAG = "ArchiveUtils";

    /** The Constant COMPRESSION_LEVEL. */
    private static final int COMPRESSION_LEVEL = 8;
    
    /** The Constant BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 1024 * 2;

    /**
     * Zip archive file.
     *
     * @param sourcePath the source path
     * @param output the output
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static final void zip(String sourcePath, String output)
	    throws IOException {
	Log.i(TAG, "[METHOD] void zip(sourcePath:" + sourcePath + ", output:"
		+ output + ")");

	final File sourceFile = new File(sourcePath);

	// return if is not file or directory
	if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
	    throw new FileNotFoundException();
	}

	// set compression level (max: 9, default: 8)
	final ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
		new FileOutputStream(output)));
	zos.setLevel(COMPRESSION_LEVEL);

	// make each zip file
	zipEntry(sourceFile, sourcePath, zos);

	zos.finish();
	zos.close();
    }

    /**
     * Zip one entry.
     *
     * @param sourceFile the source file
     * @param sourcePath the source path
     * @param zos the zos
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static final void zipEntry(File sourceFile, String sourcePath,
	    ZipOutputStream zos) throws IOException {
	// if directory
	if (sourceFile.isDirectory()) {
	    // return if .metadata
	    if (sourceFile.getName().equalsIgnoreCase(".metadata")) {
		return;
	    }

	    // sourceFile ì?˜ í•˜ìœ„ íŒŒì?¼ ë¦¬ìŠ¤íŠ¸
	    final File[] fileArray = sourceFile.listFiles();
	    for (final File element : fileArray) {
		// recursive call
		zipEntry(element, sourcePath, zos);
	    }
	} else {
	    BufferedInputStream bis = null;
	    final String sFilePath = sourceFile.getPath();
	    final String zipEntryName = sFilePath.substring(sourcePath.length() + 1,
		    sFilePath.length());

	    bis = new BufferedInputStream(new FileInputStream(sourceFile));
	    final ZipEntry zentry = new ZipEntry(zipEntryName);
	    zentry.setTime(sourceFile.lastModified());
	    zos.putNextEntry(zentry);

	    final byte[] buffer = new byte[BUFFER_SIZE];
	    int cnt = 0;
	    while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
		zos.write(buffer, 0, cnt);
	    }
	    zos.closeEntry();

	    bis.close();
	}
    }

    /**
     * Unzip archive file all.
     *
     * @param zipFile the zip file
     * @param targetDir the target dir
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static final void unzipAll(File zipFile, File targetDir)
	    throws IOException {
	Log.i(TAG, "[METHOD] void unzipAll(zipFile:" + zipFile + ", targetDir:"
		+ targetDir + ")");

	final ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
	ZipEntry zentry = null;

	// if exists remove
	if (targetDir.exists()) {
	    FileUtils.deleteDirectory(targetDir);
	    targetDir.mkdirs();
	} else {
	    targetDir.mkdirs();
	}
	Log.d(TAG, "targetDir: " + targetDir);

	// unzip all entries
	while ((zentry = zis.getNextEntry()) != null) {
	    final String fileNameToUnzip = zentry.getName();
	    final File targetFile = new File(targetDir, fileNameToUnzip);

	    // if directory
	    if (zentry.isDirectory()) {
		new File(targetFile.getAbsolutePath()).mkdirs();
	    } else {
		// make parent dir
		new File(targetFile.getParent()).mkdirs();
		unzipEntry(zis, targetFile);
		Log.d(TAG, "Unzip file: " + targetFile);
	    }
	}

	zis.close();
    }

    /**
     * Unzip one entry.
     *
     * @param zis the zis
     * @param targetFile the target file
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static final File unzipEntry(ZipInputStream zis, File targetFile)
	    throws IOException {
	final FileOutputStream fos = new FileOutputStream(targetFile);

	final byte[] buffer = new byte[BUFFER_SIZE];
	int len = 0;
	while ((len = zis.read(buffer)) != -1) {
	    fos.write(buffer, 0, len);
	}

	return targetFile;
    }

}
