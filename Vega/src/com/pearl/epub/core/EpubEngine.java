package com.pearl.epub.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pearl.epub.BookInfo.BOOK_TYPE;
import com.pearl.epub.TableOfContent;
import com.pearl.epub.exception.EbookException;
import com.pearl.epub.util.DOMUtil;
import com.pearl.security.PearlSecurity;

// TODO: Auto-generated Javadoc
//import com.gizrak.ebook.PrefActivity;

/**
 * The Class EpubEngine.
 */
public class EpubEngine {

    /** The Constant TAG. */
    private static final String TAG = "EpubEngine";

    /** The m book type. */
    private BOOK_TYPE mBookType;
    
    /** The pearl security. */
    private static PearlSecurity pearlSecurity;
    
    /** The last page of chapter. */
    private static List<Integer> lastPageOfChapter = new ArrayList<Integer>();

    /**
     * Returns input stream for each type of book.
     *
     * @return the chapter input stream
     */
    public InputStream getChapterInputStream() {
	final InputStream istream = null;
	switch (mBookType) {
	case EPUB:
	    break;

	case TEXT:
	    break;

	case HTML:
	    break;

	default:
	    break;
	}
	return istream;
    }

    /**
     * Read file.
     *
     * @param file the file
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String readFile(String file) throws IOException {
	final BufferedReader reader = new BufferedReader(new FileReader(file));
	String line = null;
	final StringBuilder stringBuilder = new StringBuilder();

	while ((line = reader.readLine()) != null) {
	    stringBuilder.append(line);
	}
	return stringBuilder.toString();
    }

    /**
     * Read file.
     *
     * @param fileName the file name
     * @param salt the salt
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String readFile(String fileName, String salt)
    		throws IOException,Exception {
	// Log.w("tag", "Reading file content which has a salt = " + salt);
	pearlSecurity = new PearlSecurity(salt);
	final byte[] htmlContent = pearlSecurity.readFromFile(fileName);
	// Log.w("tag", "DRM - content is:" + new String(htmlContent));
	return decrypt(htmlContent, salt);
    }

    /**
     * Decrypt.
     *
     * @param content the content
     * @param salt the salt
     * @return the string
     * @throws Exception 
     */
    private static String decrypt(byte[] content, String salt) throws Exception {
	// Log.i("tag", "DRM - SALT is:" + salt);
	byte[] decryptedContent = null;
	try {
	    decryptedContent = pearlSecurity.decrypt(content);
	} catch (final Exception e) {
	    Log.e("tag", e.toString());
	}
	if (decryptedContent == null) {
	    decryptedContent = new byte[0];
	}
	// Log.i("tag", "DRM - decrypted content is  "+ new
	// String(decryptedContent));
	return new String(decryptedContent);
    }

    /**
     * Preprocess.
     *
     * @param chapter the chapter
     * @param jspath the jspath
     * @param csspath the csspath
     * @param ctx the ctx
     * @param salt the salt
     * @return the string
     * @throws EbookException the ebook exception
     */
    public static String preprocess(String chapter, String jspath,
	    String csspath, Context ctx, String salt) throws EbookException,Exception {

	if (true) {
	    // Log.w(TAG, "PARSING: Opening chapter from location: " + chapter);
	    // Log.w(TAG, "PARSING: Salt is " + salt);
	    String htmlContent;
	    try {
		if (salt == null) {
		    htmlContent = readFile(chapter);
		    Log.w("tag", "PARSING: Salt is  null");
		} else {
		    Log.w("tag", "PARSING: Salt is " + salt);
		    htmlContent = readFile(chapter, salt);
		}
		// return inducejsCss(htmlContent);

	    } catch (final IOException e) {
		e.printStackTrace();
		htmlContent = "Unable to read file becoz" + e.toString();
	    }
	    // Log.w(TAG, "PARSING: chapter content: " + htmlContent);
	    return htmlContent;
	}
	/*
	 * 1. prepare dom
	 */
	// get dom
	Document doc = null;
	try {
	    doc = DOMUtil.getDom(chapter);
	} catch (final EbookException e) {
	    e.printStackTrace();
	    throw new EbookException(e.getMessage());
	}

	/*
	 * 2. handle dom tree
	 */
	final NodeList nodeList = doc.getElementsByTagName("*");
	for (int i = 0; i < nodeList.getLength(); i++) {
	    final Node node = nodeList.item(i);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		/*
		 * <head> Element
		 * 
		 * 1. append monocle library (CORE) 2. append monocle library
		 * (FLIPPERS) 3. append monocle library (CONTROLS) 4. set font
		 * face
		 */
		if ("head".equalsIgnoreCase(node.getNodeName())) {
		    final Element headElement = (Element) node;

		    // 1. append monocle library (CORE)
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/monocle.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/compat.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/reader.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/book.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/component.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/place.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/styles.js");

		    // 2. append monocle library (FLIPPERS)
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/flippers/slider.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/flippers/legacy.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/flippers/instant.js");

		    // 3. append monocle library (CONTROLS)
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/controls/spinner.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/controls/magnifier.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/controls/scrubber.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/controls/placesaver.js");
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "monocle/controls/contents.js");

		    // append monocle interface script
		    EpubEngine.addJavaScriptLink(doc, headElement,
			    "javascript/interface.js");

		    // 4. set font face
		    final SharedPreferences pref = PreferenceManager
			    .getDefaultSharedPreferences(ctx);
		    final Element cssElement = doc.createElement("style");
		    cssElement.setAttribute("type", "text/css");

		    headElement.appendChild(cssElement);
		} else if ("body".equalsIgnoreCase(node.getNodeName())) {
		    final Element bodyElement = (Element) node;

		    // 1. insert div for monocle
		    final Element divElement = doc.createElement("div");
		    divElement.setAttribute("id", "reader");
		    final NodeList bodyChildList = bodyElement.getChildNodes();
		    for (int j = 0; j < bodyChildList.getLength(); j++) {
			final Node bodyChild = bodyChildList.item(j);
			divElement.appendChild(bodyChild);
		    }
		    bodyElement.appendChild(divElement);

		    // 2. clear attributes
		    bodyElement.removeAttribute("xml:lang");

		    // 3. set font size
		    final SharedPreferences pref = PreferenceManager
			    .getDefaultSharedPreferences(ctx);
		}

		else if ("img".equalsIgnoreCase(node.getNodeName())) {
		    final Element imgElement = (Element) node;

		}
	    }
	}

	/*
	 * 3. DOM to string
	 */
	final StringWriter outText = new StringWriter();
	final StreamResult sr = new StreamResult(outText);

	final Properties oprops = new Properties();
	oprops.put(OutputKeys.METHOD, "html");
	// oprops.put("indent-amount", "4");

	final TransformerFactory tf = TransformerFactory.newInstance();
	Transformer trans = null;
	try {
	    trans = tf.newTransformer();
	    trans.setOutputProperties(oprops);
	    trans.transform(new DOMSource(doc), sr);
	} catch (final TransformerConfigurationException e) {
	    e.printStackTrace();
	} catch (final TransformerException e) {
	    e.printStackTrace();
	}

	return outText.toString();
    }

    /**
     * Add External Javascript Src.
     *
     * @param doc the doc
     * @param headElement the head element
     * @param path the path
     */
    private static void addJavaScriptLink(Document doc, Element headElement,
	    String path) {
	final Element scriptElement = doc.createElement("script");
	scriptElement.setAttribute("type", "text/javascript");
	scriptElement.setAttribute("src", "url('file:///android_asset/" + path
		+ "')");
	headElement.appendChild(scriptElement);
	headElement.appendChild(doc.createTextNode("\n"));
    }

    /**
     * Creates separated HTML files from TEXT file.
     *
     * @param textPath the text path
     * @return the string
     * @throws FileNotFoundException the file not found exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String makeHtml(String textPath)
	    throws FileNotFoundException, IOException {
	Log.i(TAG, "[METHOD] ArrayList<String> makeFileTextToHtml(textPath:"
		+ textPath + ")");

	// generate html files
	final StringBuffer sb = new StringBuffer();
	// sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
	// sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n");
	sb.append("<html>\n");
	sb.append("<head>\n");
	// sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
	sb.append("</head>\n");
	sb.append("<body>\n");
	sb.append("<p>");

	String lineStr = "";
	final BufferedReader br = new BufferedReader(new InputStreamReader(
		new FileInputStream(textPath), "euc-kr"));

	while ((lineStr = br.readLine()) != null) {
	    // replace reference values
	    lineStr = lineStr.replace("<", "&lt;");
	    lineStr = lineStr.replace(">", "&gt;");
	    lineStr = lineStr.replace("&", "&amp;");

	    // append p tags
	    if (lineStr.length() == 0 || "".equals(lineStr.trim())) {
		sb.append("</p>\n<p>");
	    }
	    sb.append(lineStr);
	}

	sb.append("</p>\n");
	sb.append("</body>\n");
	sb.append("</html>\n");

	return sb.toString();
    }

    /**
     * Creates separated HTML files from TEXT file.
     *
     * @param textPath the text path
     * @param baseUrl the base url
     * @return the array list
     * @throws FileNotFoundException the file not found exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static ArrayList<String> makeFileTextToHtml(String textPath,
	    String baseUrl) throws FileNotFoundException, IOException {
	Log.i(TAG, "[METHOD] ArrayList<String> makeFileTextToHtml(textPath:"
		+ textPath + ", baseUrl:" + baseUrl + ")");
	final ArrayList<String> mGeneratedHtmlFiles = new ArrayList<String>();

	// make baseUrl
	new File(baseUrl).mkdirs();

	// generate html files
	final StringBuffer sbHeader = new StringBuffer();
	sbHeader.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
	sbHeader.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n");
	sbHeader.append("<html>\n");
	sbHeader.append("<head>\n");
	sbHeader.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
	sbHeader.append("</head>\n");
	sbHeader.append("<body>\n");
	sbHeader.append("<p>");
	final String header = sbHeader.toString();

	final StringBuffer sbTail = new StringBuffer();
	sbTail.append("</p>\n");
	sbTail.append("</body>\n");
	sbTail.append("</html>\n");
	final String tail = sbTail.toString();

	int lineCount = 0;
	int fileCount = 0;
	String lineStr = "";
	final BufferedReader br = new BufferedReader(new InputStreamReader(
		new FileInputStream(textPath), "euc-kr"));
	final StringBuffer sbContent = new StringBuffer();

	while ((lineStr = br.readLine()) != null) {
	    // replace reference values
	    lineStr = lineStr.replace("&", "&amp;");
	    lineStr = lineStr.replace("<", "&lt;");
	    lineStr = lineStr.replace(">", "&gt;");

	    if (lineCount == 0) {
		sbContent.append(lineStr);
		lineCount++;
		continue;
	    }

	    // append p tags
	    if (lineStr.length() == 0 || "".equals(lineStr.trim())) {
		sbContent.append("</p>\n<p>");
	    }
	    sbContent.append(lineStr);

	    // create file
	    if (lineCount >= 300) {
		final String genFile = "generated_"
			+ String.format("%05d", ++fileCount) + ".html";
		final FileWriter fw = new FileWriter(
			new File(baseUrl + "/" + genFile));
		fw.write(header);
		fw.write(sbContent.toString());
		fw.write(tail);
		fw.close();

		final String url = baseUrl + "/" + genFile;
		mGeneratedHtmlFiles.add(genFile);
		Log.d(TAG, url + " was created.");

		lineCount = 0;
		sbContent.delete(0, sbContent.length());

		continue;
	    }

	    lineCount++;
	}

	// create file
	final String genFile = "generated_" + String.format("%05d", ++fileCount)
		+ ".html";
	final FileWriter fw = new FileWriter(new File(baseUrl + "/" + genFile));
	fw.write(header);
	fw.write(sbContent.toString());
	fw.write(tail);
	fw.close();

	final String url = baseUrl + "/" + genFile;
	mGeneratedHtmlFiles.add(genFile);
	Log.d(TAG, url + " was created.");

	lineCount = 0;
	sbContent.delete(0, sbContent.length());

	br.close();

	return mGeneratedHtmlFiles;
    }

    /**
     * Calculate page count.
     *
     * @param baseUrl the base url
     * @param ctx the ctx
     * @param toc the toc
     * @param salt the salt
     * @return the int
     */
    public static int calculatePageCount(String baseUrl, Context ctx,
	    TableOfContent toc, String salt) throws IllegalBlockSizeException,
	    BadPaddingException, NoSuchAlgorithmException,
	    NoSuchProviderException, NoSuchPaddingException,
	    InvalidKeyException, InvalidAlgorithmParameterException,Exception{
	lastPageOfChapter = new ArrayList<Integer>();
	int totalPages = 0;
	String chapterPath = null;
	Log.w("tag", "PAGINATION - chapter size is:"
		+ toc.getChapterList().size());
	for (int i = 0; i < toc.getChapterList().size(); i++) {
	    chapterPath = baseUrl + "/" + toc.getChapter(i).getUrl();
	    Log.w("Epub engine", "Samreen - chapter path is:" + chapterPath);
	    try {
		final String html = EpubEngine.preprocess(chapterPath, "", "", ctx,
			salt);
		// count gives the no of occurrences of the div tag
		// that gives the count of number pages in a chapter
		/*int count = 0;
		final int count1 = StringUtils.countMatches(html,
			"<div class=\"page\">");
		final int count2 = StringUtils.countMatches(html,
			"<div class=\'page\'>");
		count = count1 + count2;
		Log.w("Epub engine", "Samreen - no of occurences of div is:"
			+ count);
		// total count gives the count of over all page and the last
		// page of a chapter
		*/
		String pattern1 = "<div class=\"page\">";
		String pattern2 = "<div class='page'>";
		Pattern p1 = Pattern.compile(pattern1);
		Pattern p2 = Pattern.compile(pattern2);
		Matcher m1 = p1.matcher(html);
		Matcher m2 = p2.matcher(html);
		int count1=0,count2=0;
		
		while(m1.find())
			count1++;
		
		while(m2.find())
			count2++;
		
		
		int count = count1+count2;
		
		System.out.println(count);
		totalPages = count + totalPages;
		Log.w("Epub engine", "PAGINATION - total pages is:"
			+ totalPages);
		setChapterList(totalPages);
	    } catch (final EbookException e) {
		Log.e("tag", "PAGINATION - Exception:" + e.toString());
	    }
	}
	return totalPages;
    }

    /**
     * Sets the chapter list.
     *
     * @param totalPages the new chapter list
     */
    public static void setChapterList(int totalPages) {
	lastPageOfChapter.add(totalPages);
    }

    /**
     * Gets the chapter list.
     *
     * @return the chapter list
     */
    public static List<Integer> getChapterList() {
	return lastPageOfChapter;
    }

    /**
     * Gets the chapter for given page.
     *
     * @param pageNo the page no
     * @return the chapter for given page
     */
    public static int getChapterForGivenPage(int pageNo) {
	int chapter = 0;
	Log.i("tag", "PAGINATION - last page of chapter list is:"
		+ lastPageOfChapter);
	for (int i = 0; i < getChapterList().size(); i++) {
	    if (pageNo <= getChapterList().get(i)) {
		chapter = i + 1;
		return chapter;
	    }
	}
	return chapter;
    }

    /**
     * Gets the page num.
     *
     * @param chapterNo the chapter no
     * @param relativePageNum the relative page num
     * @return the page num
     */
    public static int getPageNum(int chapterNo, int relativePageNum) {
	int lastChapterPosition = 0;

	if (chapterNo != 1)
	    lastChapterPosition = lastPageOfChapter.get(chapterNo - 2);

	Log.i("tag", "PAGINATION - last chapter position is:"
		+ lastChapterPosition);
	return relativePageNum + lastChapterPosition; // get relative position
	// of the page with in
	// chapter
    }

    /**
     * gets the relative page number for a given chapter.
     *
     * @param pageNo the page no
     * @param chapterNo the chapter no
     * @return the relative position for page
     */
    public static int getRelativePositionForPage(int pageNo, int chapterNo) {
	int lastChapterPosition = 0;

	if (chapterNo != 1)
	    lastChapterPosition = lastPageOfChapter.get(chapterNo - 2);

	Log.i("tag", "PAGINATION - last chapter position is:"
		+ lastChapterPosition);
	return pageNo - lastChapterPosition; // get relative position of the
	// page with in chapter
    }

    /**
     * Gets the chapter max pages.
     *
     * @param chapterNo the chapter no
     * @return the chapter max pages
     */
    public static int getChapterMaxPages(int chapterNo) {
	return EpubEngine.lastPageOfChapter.get(chapterNo);
    }
}
