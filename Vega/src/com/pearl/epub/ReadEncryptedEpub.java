package com.pearl.epub;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pearl.security.PearlSecurity;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;

public class ReadEncryptedEpub {
	
	private String passPhrase;
	private Book eBook;
	private Map<String,String> decryptedHref2html;
	private Map<String,Integer> chapter2Pages;
		
	public ReadEncryptedEpub (String passPhrase,String epubLocation) throws IOException, GeneralSecurityException{
		this.passPhrase=passPhrase;
		this.eBook=(new EpubReader()).readEpub(new FileInputStream(new File(epubLocation)));
		System.out.println("******** Decrypting the epub *******");
		this.decryptHTMLResources();
		System.out.println("******** Finished *******");
		//System.out.println(this.decryptedHref2html);
		
	}
	
	public List<String> getTableOfContents(){
		List<String> tableOfContents = new ArrayList<String>();
		TableOfContents toc=eBook.getTableOfContents();
		for(TOCReference ref:toc.getTocReferences()){
			tableOfContents.add(ref.getTitle());
		}
		return tableOfContents;
	}
	
	private void decryptHTMLResources() throws IOException, GeneralSecurityException{
		this.decryptedHref2html= new TreeMap<String, String>();
		List<SpineReference> sRefs= eBook.getSpine().getSpineReferences();
		for(SpineReference sRef:sRefs){
			String href =sRef.getResource().getHref();
			System.out.println("Decrypting:"+href);
			decryptedHref2html.put(href, ReadEncryptedEpub.decrypt(eBook.getResources().getByHref(href).getData(),this.passPhrase));
		}
		this.populatePages2Chapter();
		
	}
	
	
	private void populatePages2Chapter(){
		chapter2Pages=new TreeMap<String, Integer>();		
		for(String href:this.decryptedHref2html.keySet()){
			int numberOfPages =getNumberOfOccurrencesOfATagInHtml(decryptedHref2html.get(href),".page");
			chapter2Pages.put(href,numberOfPages);
		}
	}
	
	private int getNumberOfOccurrencesOfATagInHtml(String html,String tag){
		if(decryptedHref2html==null)
			return 0;
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select(tag);
		return ele.size();
	}
	
	public int getTotalPages(){
		int ret=0;
		for(String chapter:chapter2Pages.keySet())
			ret+=chapter2Pages.get(chapter);
		return ret;
	}
	
	public String getPageHTMLByPageNo(int pageNo,String css){
		//String html="";
		int tempTotal=0,processedPageCount=0,absPageNum=0,currentPageCount=0;
		String chapterHTML="";
		//get the chapter html
		for(String href:chapter2Pages.keySet()){
			tempTotal+=chapter2Pages.get(href);
			if(pageNo<=tempTotal){
				chapterHTML=href;
				break;
			}
		}
		
		//get total number of pages till this chapter
				for(String ref:chapter2Pages.keySet())
				{	
					currentPageCount=chapter2Pages.get(ref);
					if(ref.equalsIgnoreCase(chapterHTML))
						break;
					processedPageCount+=currentPageCount;
				}
				
				absPageNum=pageNo-processedPageCount;
				System.out.println("Absolute Page Number:"+absPageNum);
				return this.getHTMLPagesInAChapter(this.decryptedHref2html.get(chapterHTML), css).get(absPageNum-1);
	}
	
	private List<String> getHTMLPagesInAChapter(String chapterHtml,String css){
		List<String> pages = new ArrayList<String>();
		Document doc = Jsoup.parse(chapterHtml);
		Elements ele=doc.select(".page");
		for(Element page:ele){
			Document tgt = Document.createShell("/home/kishore/Downloads/2books/page.html");
			tgt.head().append("<link rel=\"stylesheet\" href=\""+css+"\">");
			tgt.select("body").first().appendChild(page);
			//System.out.println(page.html());
			pages.add(tgt.html());
		}
		return pages;
	}
	
	
	private static String decrypt(byte[] content,String passPhrase) throws GeneralSecurityException {
		byte[] decryptedContent = null;
		PearlSecurity pearlSecurity = new PearlSecurity(passPhrase);
		try {
			decryptedContent = pearlSecurity.decrypt(content);
		} catch (final Exception e) {
				e.printStackTrace();
		}
		if (decryptedContent == null) {
			decryptedContent = new byte[0];
		}
		return new String(decryptedContent);
	}
	
	
	
	public static void main(String[] args) throws GeneralSecurityException {
		/*try {
			//ReadEncryptedEpub reader = new ReadEncryptedEpub("x@y!a~b@$sT4t*u&k$$t$vAC694BC4CDD544A76E9D5BB02333F0BF3$25tjk$#@", "/home/kishore/Downloads/2books/book_5/Alive_in_Gods_Spirit_5.epub");
			//System.out.println("Total Number of pages:"+reader.getTotalPages());
			//System.out.println("Page 10:\n"+reader.getPageHTMLByPageNo(10));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File Not found");
		}*/
	}
	
	

}
