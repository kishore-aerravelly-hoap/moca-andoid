package com.pearl.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.pearl.logger.Logger;

import android.content.Context;
import android.content.res.AssetManager;

// TODO: Auto-generated Javadoc
/**
 * The Class HelpParser.
 */
public class HelpParser {

	/** The Constant tag. */
	private static final String tag = "help parser";

	/**
	 * Gets the help content.
	 *
	 * @param fileName the file name
	 * @param context the context
	 * @return the help content
	 */
	public static List<String> getHelpContent(String fileName, Context context) {
		AssetManager manager = context.getAssets();
		List<String> helpList = new ArrayList<String>();
		InputStream inputStream = null;
		try {
			inputStream = manager.open("help/" + fileName);
			if (inputStream != null) {
				int size = inputStream.available();
				byte[] buffer = new byte[size];
				inputStream.read(buffer);
				inputStream.close();
				String text = new String(buffer);
				StringTokenizer st = new StringTokenizer(text, "\n");
				while (st.hasMoreTokens()) {
					helpList.add(st.nextToken());
				}
			}
		} catch (IOException e) {
			Logger.error(tag, e);
		}
		return helpList;
	}
	
	/**
	 * Gets the help content.
	 *
	 * @param fileName the file name
	 * @param manager the manager
	 * @return the help content
	 */
	public static List<String> getHelpContent(String fileName, AssetManager manager) {
		List<String> helpList = new ArrayList<String>();
		InputStream inputStream = null;
		try {
			inputStream = manager.open("help/" + fileName);
			if (inputStream != null) {
				int size = inputStream.available();
				byte[] buffer = new byte[size];
				inputStream.read(buffer);
				inputStream.close();
				String text = new String(buffer);
				StringTokenizer st = new StringTokenizer(text, "\n");
				while (st.hasMoreTokens()) {
					helpList.add(st.nextToken());
				}
			}
		} catch (IOException e) {
			Logger.error(tag, e);
		}
		return helpList;
	}
}
