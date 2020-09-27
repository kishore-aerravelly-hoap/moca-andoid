/**
 * 
 */
package com.pearl.books.readers.handlers.epub;

/**
 * The Class Epage.
 *
 * @author KiranYNG
 */
public class Epage {/*
 * private Resource resource; protected String
 * rawPageContent; private int pageNo;
 * 
 * public Epage(Resource resource) { this.resource =
 * resource; pageNo = 0;
 * 
 * rawPageContent = ""; }
 * 
 * public void setPageNo(int pageno) { this.pageNo = pageno;
 * }
 * 
 * public String getRawContent() { if (this.rawPageContent
 * == "") { String str = ""; try { InputStream is =
 * resource.getInputStream(); BufferedReader in = new
 * BufferedReader( new InputStreamReader(is), 2048);
 * 
 * // do something with stream while ((str = in.readLine())
 * != null) { // Log.d("Reading spine page - ", str);
 * 
 * rawPageContent += str; } } catch (IOException e) {
 * Log.e("In Spine reading", e.getMessage()); } }
 * 
 * return this.rawPageContent; }
 * 
 * public String getHighlightedRawContent(String
 * hightlightString) { return
 * getHighlightedRawContent(hightlightString, 1); }
 * 
 * public String getHighlightedRawContent(String
 * hightlightString, int nth_position) { String content =
 * getRawContent();
 * 
 * if ((hightlightString != null) &&
 * (hightlightString.trim() != "")) { if (nth_position == 0)
 * { nth_position = 1; }
 * 
 * // TODO need to use regex replace here to highlight nth
 * element or // highlight based on case sensitivity etc
 * content = content .replace( hightlightString,
 * "<span style='background-color:#fff2a8 !important;font-style:italic !important;'>"
 * + hightlightString + "</span>"); }
 * 
 * return content; }
 * 
 * public int getPageNo() { return this.pageNo; }
 * 
 * public String getId() { return resource.getId(); }
 * 
 * public String getTitle() { if (this.resource != null) {
 * return this.resource.getTitle(); }
 * 
 * return "No Title"; }
 * 
 * public String getLocationHref() { return
 * resource.getHref(); }
 */
}