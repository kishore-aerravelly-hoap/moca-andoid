package com.pearl.activities;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.epub.ReadEncryptedEpub;
import com.pearl.security.PearlSecurity;


public class EpubReaderActivity extends BaseActivity{

	@Override
	public String getActivityType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onNetworkAvailable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkUnAvailable() {
		// TODO Auto-generated method stub
		
	}
	
	String bookFileName;
	int bookId;
	WebView webView;
	List<String> toc ;
	String salt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.epub_reader);
		vegaConfig = new VegaConfiguration(this);
		appData = (ApplicationData) getApplication();
		bookId  = getIntent().getExtras().getInt("book_id");
		bookFileName = getIntent().getExtras().getString("bookfilename");
		salt = PearlSecurity.getSalt(appData.getUserId(), deviceId);
		ReadEncryptedEpub readepub = null;
		try {
			readepub = new ReadEncryptedEpub(salt,appData.getBookFilesPath(bookId) + bookFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		toc=readepub.getTableOfContents();
		
		webView = (WebView) findViewById(R.id.epubWebView);
		Log.e("ReadBookActivity", toc.toString());
		
		
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVerticalScrollBarEnabled(true);
		//webView.loadData(readepub.getPageHTMLByPageNo(3,"theme1"), "text/html", "UTF8");
		webView.loadDataWithBaseURL("file:///android_asset/",readepub.getPageHTMLByPageNo(3,"epub_js/theme1.css"), "text/html", "UTF8",null);

	}

}
