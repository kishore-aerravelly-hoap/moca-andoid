package com.pearl.epub;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.pearl.epub.BookInfo.BOOK_TYPE;
import com.pearl.epub.BookInfo.Manifest;
import com.pearl.epub.exception.EbookException;
import com.pearl.epub.parser.BookInfoSAXParser;
import com.pearl.epub.parser.TocSAXParser;
import com.pearl.exceptions.EPUBPathNotFoundException;
import com.pearl.logger.Logger;

public class EpubReader {

	/**
	 * @param args
	 */

	String targetLocation;
	String bookLocation;
	// user_Ryo8x81j67501509, user_Ryde4p1p77890916
	String bookTpye = "EPUB";
	String mContentPath;
	String mBaseUrl;
	String mTocPath;
	private static final String TAG = "PearlEpubReader";

	public EpubReader(String targetLocation, String bookLocation) throws InterruptedException {
		this.bookLocation = bookLocation;
		this.targetLocation = targetLocation;
		getEpubFromLocal();
		getContentPath();
		try {
			getBaseURL();
		} catch (EPUBPathNotFoundException e) {
			Logger.warn("Epub reader", "Path not found " + e.toString());
			try {
				throw new Exception("unable to get path of container"
						+ e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		getTocPath();
	}

	// 1.unzip
	public void getEpubFromLocal() throws InterruptedException {
		UnzipEpub unZip = new UnzipEpub();
		unZip.extractBookToTemp(targetLocation, bookLocation);

	}

	// 2. get content path
	public void getContentPath() {

		try {
			String containerPath = targetLocation + "META-INF/container.xml";
			Log.w(TAG, "PARSING:----------Container path is:" + containerPath);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new FileReader(new File(containerPath)));

			int eventType = xpp.getEventType();
			Log.w(TAG, "PARSING: event type is:" + eventType);
			while (eventType != XmlPullParser.END_DOCUMENT) {
				System.out.println("in while");
				if (eventType == XmlPullParser.START_TAG) {
					String tagName = xpp.getName();
					Log.w(TAG, "PARSING: tag name is:" + tagName);
					if ("rootfile".equalsIgnoreCase(tagName)) {
						mContentPath = targetLocation + "/"
								+ xpp.getAttributeValue(null, "full-path");
						Log.w(TAG, "PARSING: mContentPath: " + mContentPath);
						break;
					}
				}
				eventType = xpp.next();
			}
			Log.w(TAG, "PARSING: Content path is:" + mContentPath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	// 3. get base url
	public void getBaseURL() throws EPUBPathNotFoundException {
		mBaseUrl = mContentPath.substring(0, mContentPath.lastIndexOf("/") + 1);
		Log.w(TAG, "PARSING: mBaseUrl: " + mBaseUrl);
	}

	// 4.get toc path
	public void getTocPath() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			// mContentPath = "D:/EpubReader/epub/OEBPS/content.opf";
			Log.w(TAG, "PARSING: Content path is " + mContentPath);
			Document doc = builder.parse(new File(mContentPath));
			Element elmtNcx = doc.getElementById("ncx");
			mTocPath = mBaseUrl + elmtNcx.getAttributeNode("href").getValue();
			Log.w(TAG, "PARSING: mTocPath: " + mTocPath);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses content.opf
	 * 
	 * @return
	 * @throws EbookException
	 */
	public BookInfo parseBookInfo() throws Exception {
		Log.w(TAG, "PARSING: [METHOD] BookInfo parseBookInfo()");
		BookInfo bookInfo = new BookInfo();

		BookInfoSAXParser bookInfoParser = new BookInfoSAXParser();
		try {
			bookInfo = bookInfoParser.getBookInfo(mContentPath);
			bookInfo.setBookType(BOOK_TYPE.EPUB);
			bookInfo.setPath(targetLocation);
			Log.w("tag", "max page num is:" + bookInfo.getSpineList().size());

			// cover image
			String imgHtml = "";
			String idref = bookInfo.getSpineItem(0);
			ArrayList<Manifest> manifestList = bookInfo.getManifestList();

			for (int i = 0; i < manifestList.size(); i++) {
				Manifest manifest = manifestList.get(i);

				if (idref.equals(manifest.id)) {
					imgHtml = mBaseUrl + "/" + manifest.href;
					Logger.error("tag", "imgHtml: " + imgHtml);
					break;
				}
			}

		} catch (ParserConfigurationException e) {
			Log.e("tag", "Unable to parse Book Info " + e);
			e.printStackTrace();
			throw e;
		} catch (SAXException e) {
			Log.e("tag", "Unable to parse Book Info " + e);
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			Log.e("tag", "Unable to read file " + e);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			Log.e("tag", "Unable to parse file " + e);
			e.printStackTrace();
			throw e;
		}

		return bookInfo;
	}

	/**
	 * Parses toc.ncx
	 * 
	 * @return
	 * @throws EbookException
	 */
	public TableOfContent parseTableOfContent() throws Exception {
		Logger.warn("", "[METHOD] void parseTableOfContent()");
		TableOfContent toc = new TableOfContent();

		TocSAXParser parser = new TocSAXParser();
		try {
			toc = parser.getTableOfContents(mTocPath);
		} catch (ParserConfigurationException e) {
			Log.e("tag", "Unable to parse TOC " + e);
			e.printStackTrace();
			throw e;
		} catch (SAXException e) {
			Log.e("tag", "Unable to parse TOC " + e);
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			Log.e("tag", "Unable to read TOC " + e);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			Log.e("tag", "Unable to parse TOC file " + e);
			e.printStackTrace();
			throw e;
		}

		return toc;
	}

	/**
	 * Gets Base URL
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return mBaseUrl;
	}

}