package com.pearl.epub.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUtil.
 */
public class FileUtil {

    /**
     * Resizes ByteArray Image.
     *
     * @param byteArrayImg the byte array img
     * @param width the width
     * @param height the height
     * @return the byte[]
     */
    public static byte[] resizingByteArrayImage(byte[] byteArrayImg, int width,
	    int height) {
	if (byteArrayImg == null)
	    return null;

	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inPurgeable = true;
	final Bitmap oriBitmap = BitmapFactory.decodeByteArray(byteArrayImg, 0,
		byteArrayImg.length, options);
	if (oriBitmap == null)
	    return null;

	final Bitmap resizedBitmap = makeThumbImg(oriBitmap, width, height);
	final byte[] retBytes = convertBitmapToByteArray(resizedBitmap);
	;

	if (resizedBitmap != null)
	    resizedBitmap.recycle();

	return retBytes;
    }

    /**
     * Makes Thumbnail Image.
     *
     * @param ori the ori
     * @param dstWidth the dst width
     * @param dstHeight the dst height
     * @return the bitmap
     */
    public static Bitmap makeThumbImg(Bitmap ori, int dstWidth, int dstHeight) {
	final int srcWidth = ori.getWidth();
	final int srcHeight = ori.getHeight();
	final float scaleWidth = (float) dstWidth / srcWidth;
	final float scaleHeight = (float) dstHeight / srcHeight;

	if (scaleWidth >= 1 || scaleHeight >= 1)
	    return ori;

	final Matrix matrix = new Matrix();
	matrix.postScale(scaleWidth, scaleHeight);

	final Bitmap resizedBitmap = Bitmap.createBitmap(ori, 0, 0, srcWidth,
		srcHeight, matrix, true);

	return resizedBitmap;
    }

    /**
     * Converts Bitmap to ByteArray.
     *
     * @param bitmap the bitmap
     * @return the byte[]
     */
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
	final ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// ignored for PNG
	final boolean result = bitmap.compress(CompressFormat.PNG, 0, bos);
	byte[] retBytes = null;

	if (result)
	    retBytes = bos.toByteArray();

	if (bos != null) {
	    try {
		bos.close();
	    } catch (final IOException e) {
		e.printStackTrace();
	    }
	}

	return retBytes;
    }

}
