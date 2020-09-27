package com.pearl.activities;


import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.booklist.BookList;
import com.pearl.books.Book;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadStatus;
import com.pearl.network.downloadmanager.utils.DownloadType;
import com.pearl.network.request.ResponseStatus;
import com.pearl.network.request.ServerRequests;
import com.pearl.parsers.json.BookListParser;

public class OnlineActivity extends FragmentActivity {
    private TextView bookTitile;
    private TextView bookDescription;
    private ImageView myshelf;
    private Button onlineMenuButton;
    private ImageView onlineCoverImage;
    private ImageView refresh, help;
    private ApplicationData appData;
    private VegaConfiguration vegaConfig;
    private boolean forceRefresh = false;
    private ServerRequests serverRequests;
    private final String tag = "OnlineActivty";
    private BookList booksList;
    private BookslistArrayAdapter bl_adptr;
    private int selectedBookPosition = 0;
    private Timer timer;
    private Context onlineContext;
    private boolean clickable;
    private Button emptyButton;
    public static Handler h;
    private boolean downloadInProgress;
    private ProgressBar progressBar;
    private int counter = 0;
    private TextView noCoverImage;
    private TextView partialDownload;
    private int toastCounter = 0, i =0;
    private boolean gridSeltedFlag=true;
    PowerManager pm;
    PowerManager.WakeLock wl;
    
    private GridView grid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	try {
	    Thread.sleep(1000);
	    setContentView(R.layout.online_fragment);
	} catch (final Exception e) {
	    Logger.error("ONLINE ACTIVITY", "STACKTRACE IS " + e);
	}
	appData = (ApplicationData) getApplication();
	serverRequests = new ServerRequests(appData);
	vegaConfig = new VegaConfiguration(this);
	onlineContext = this;
	final Bundle bundle = getIntent().getExtras();
	if (bundle != null) {
		gridSeltedFlag=bundle.getBoolean("gridSeltedFlag");
	}

	bookTitile = (TextView) findViewById(R.id.bookTitle);
	bookDescription = (TextView) findViewById(R.id.bookDescription);
	partialDownload = (TextView) findViewById(R.id.partial_download);

	emptyButton = (Button) findViewById(R.id.empty_download);

	myshelf = (ImageView) findViewById(R.id.backToshelfFromOnline);
	onlineMenuButton = (Button) findViewById(R.id.online_menu_button);

	onlineCoverImage = (ImageView) findViewById(R.id.onlineCoverImage);
	refresh = (ImageView) findViewById(R.id.refreshOnline);

	help = (ImageView) findViewById(R.id.online_help);
	progressBar = (ProgressBar) findViewById(R.id.refreshProgressBar);
	
	grid= (GridView)findViewById(R.id.books_grid);
	

	if (!appData.isLoginStatus()) {
	    final Intent login = new Intent(this, LoginActivity.class);
	    startActivity(login);
	    Logger.info(tag,
		    "LOGIN status if shelf..." + appData.isLoginStatus());
	    finish();
	}

	h = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {

		case 0:
		    finish();
		    break;
		}
	    }
	};
	pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "QuestionsActivity");
	wl.acquire();

	final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	final KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
	lock.disableKeyguard();
	help.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDialog();
		}
	});
	grid.setOnItemClickListener(new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int postion,
				long arg3) {
			if (!downloadInProgress) {
				inflateDetailsView(postion);
			}else{
				Toast.makeText(getApplicationContext(),
					    "Please wait till book download completes ",
					    Toast.LENGTH_LONG).show();
			}
		}
		
	});
    }
    
    private void showDialog(){
    	i = 0;
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent("ereader_download_book.txt", this);
		if(list != null && list.size() > 0){
			tips.setText(list.get(0));
		}
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list != null && list.size() > 0){
					if(i < (list.size() - 1)){
						i = i +1;
						tips.setEnabled(true);
						tips.setText(list.get(i));						
					}
					else
						tips.setEnabled(false);
				}
			}
		});
		previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list != null && list.size() > 0){
					if(i > 0){
						i = i -1;
						tips.setEnabled(true);
						tips.setText(list.get(i));						
					}
					else
						tips.setEnabled(false);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	
    
    }

    @Override
    public void onAttachedToWindow() {

	final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	final KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
	lock.disableKeyguard();
	this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
	super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (downloadInProgress && keyCode == KeyEvent.KEYCODE_BACK) {
	    Toast.makeText(getApplicationContext(),
		    "Please wait till book download completes ",
		    Toast.LENGTH_LONG).show();
	    return true;
	} else
	    return super.onKeyDown(keyCode, event);
    }

    private void bindEvents() {
	myshelf.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (!downloadInProgress) {
		    final Intent intent = new Intent(v.getContext(),
			    ShelfActivity.class);
		    intent.putExtra("USERID", appData.getUserId());
		    intent.putExtra("gridSeltedFlag", gridSeltedFlag);
		    startActivity(intent);
		    // finish();
		} else
		    Toast.makeText(getApplicationContext(),
			    "Please wait till book download completes ",
			    Toast.LENGTH_LONG).show();

	    }
	});

	
	
	
	
	
	refresh.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (!downloadInProgress) {
		    emptyButton.setClickable(true);
		    new asyncTaskUpdateProgress().execute();
		    refresh.setVisibility(View.GONE);
		    if (clickable) {
			forceRefresh = true;
			refresh.setClickable(false);
			// lastRefresh.setText("Refreshing..");
		    } else {
			Toast.makeText(onlineContext,
				R.string.network_connection_unreachable,
				Toast.LENGTH_SHORT).show();
		    }

		    final File f = new File(appData.getAvailableBooksListXML());
		} else
		    Toast.makeText(getApplicationContext(),
			    "Please wait till book download completes ",
			    Toast.LENGTH_LONG).show();
	    }
	});

	onlineMenuButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub

		if (!downloadInProgress) {
		    final Intent actionBar = new Intent(v.getContext(),
			    com.pearl.activities.ActionBar.class);
		    startActivity(actionBar);
		} else
		    Toast.makeText(getApplicationContext(),
			    "Please wait till book download completes ",
			    Toast.LENGTH_LONG).show();
	    }
	});

    }

    /**
     * Called when a number gets clicked
     */
    /*
     * @Override public void onItemClick(AdapterView<?> parent, View view, int
     * position, long id) {
     * 
     * Fragment f = new TestFragment(position + 1); Logger.warn(tag,
     * "DOWNLOAD - item selected"); // Toast.makeText(getBaseContext(), //
     * "item at   "+position+"   is clicked" , Toast.LENGTH_SHORT).show();
     * selectedBookPosition = position; //Logger.warn(tag,
     * "DOWNLOAD - book downloading ???? " //+ downloadInProgress); if
     * (!downloadInProgress) { //Logger.warn(tag,
     * "DOWNLOAD - No book is being downloaded"); inflateDetailsView(position);
     * }else if(downloadInProgress){ Toast.makeText(this,
     * "Please wait till the book downloads", Toast.LENGTH_LONG).show(); }else
     * if(booksList == null){ //Logger.warn(tag,
     * "DOWNLOAD - book being downloded"); listView.setClickable(false);
     * Toast.makeText(this, "Please wait till the book downloads",
     * Toast.LENGTH_LONG).show(); } FragmentTransaction ft =
     * getSupportFragmentManager().beginTransaction(); ft.replace(R.id.the_frag,
     * f); ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
     * ft.addToBackStack(null); ft.commit(); }
     */

    /**
     * Downloads the list of available books for a grade and inflates the list
     * with the books
     */
    public void populateBooksList() {
	Logger.info("OnlineActivity", "Populating bookslist");

	// Logger.error("available books path",
	if (!ApplicationData.isFileExists(appData.getAvailableBooksListXML())
		|| forceRefresh) {
		ShowProgressBar.showProgressBar("Downloading books list.", downloadAvailableBooksList(), this);
	}

	// Logger.info("populate-books-list", "books list population started");

	try {
	    booksList = BookListParser.getBooksList(appData
		    .getAvailableBooksListXML());
	    Logger.warn(tag, "Books list after parsing is:" + booksList);
	} catch (final Exception e) {
	    Logger.error(tag, "json not downloaded" + e);
	    ShowProgressBar.showProgressBar("Downloading books list.", downloadAvailableBooksList(), this);
	}
	

	if (booksList != null && booksList.getBookList() != null
		&& booksList.getBookList().size() > 0) {
	    Logger.warn(tag, "book lsit size in poppulatebookslist"
		    + booksList.getBookList().size());
	    // showDetails(0);
	    grid.setAdapter(new GridViewAdapter(this.getApplicationContext()));
	    inflateDetailsView(selectedBookPosition);
	    
	    /*
	     * if(booksList.getBookList().size() != 0) inflateDetailsView(0);
	     */
	} else if (booksList == null) {
	    Toast.makeText(this, "You have no books assigned...",
		    Toast.LENGTH_SHORT).show();
	    Logger.info("populate-books-list", "No books are available");
	}

	// Logger.info("populate-books-list", "books list population ended");
    }

    /**
     * 
     * Inflates the list with the books available for a grade
     * 
     */
    private class BookslistArrayAdapter extends ArrayAdapter<Book> {
	private final List<Book> items;

	public BookslistArrayAdapter(Context context, int textViewResourceId,
		List<Book> items) {
	    super(context, textViewResourceId, items);
	    this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = convertView;
	    if (v == null) {
		final LayoutInflater vi = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.onlineactivity_bookslist_rowitem, null);
	    }
	   
	    if (position == selectedBookPosition) {
            v.setSelected(true);
            v.setPressed(true);
            v.setBackgroundColor(Color.parseColor("#93DBE6"));
        } else {
			v.setSelected(false);
			v.setPressed(false);
			v.setBackgroundColor(Color.TRANSPARENT);
		}

	    final Book book = items.get(position);

	    if (book != null) {
		final TextView tt = (TextView) v.findViewById(R.id.title);

		if (tt != null) {
		    String title = book.getMetaData().getTitle();
		    if (title.length() > 35) {
			int pos;
			pos = title.substring(35).indexOf(" ");
			title = title.substring(0, 35 + pos) + "....";
		    }
		    tt.setText(title);
		}
	    }
	    return v;
	}
    }

    private void startTimer() {
	timer = new Timer();
	timer.schedule(new UpdateTimerTask(), 0, 2000);
    }

    public class UpdateTimerTask extends TimerTask {

	@Override
	public void run() {
	    if (!AppStatus.getInstance(onlineContext).isOnline(onlineContext)) {
		// Logger.warn(tag, "no network");
		onNetworkUnAvailable();
	    } else {
		// Logger.warn(tag, "connected");
		onNetworkAvailable();
	    }
	}
    }

    /**
     * Fills the details portion with title,description and cover image of a
     * book depending on the book selected from the list
     * 
     * @param position
     */
    private void inflateDetailsView(int position) {
	//Logger.warn(tag, "position in inflateDetailsView()" + position);
	if (booksList == null || booksList.getBookList() != null && booksList.getBookList().size() == 0) {
	    onlineCoverImage.setVisibility(View.INVISIBLE);
	   // onlineCoverImage.setText("");
	    emptyButton.setVisibility(View.INVISIBLE);
	    refresh.setVisibility(View.INVISIBLE);
	    bookTitile.setText("No books are assigned to you");
	} else if (booksList != null && booksList.getBookList() != null
		&& booksList.getBookList().size() != 0) {
		emptyButton.setVisibility(View.VISIBLE);
		emptyButton.setClickable(true);
	    // Logger.warn("OnlineActivity", "--bookList is not null--");
	    Logger.warn(tag, "inflatedetailsview bookslists is" + booksList);
	    Logger.warn(tag, "inflatedetailsview bookslists size is"
		    + booksList.getBookList().size());
	    /*
	     * //Logger.warn("OnlineActivity->updateButtonState",
	     * "InflateView - book download status of " +
	     * booksList.getBookList().get(position).getMetaData() .getTitle() +
	     * "is " + booksList.getBookList().get(position)
	     * .getDownloadStatus().getStatus());
	     */


	    final Book book = booksList.getBookList().get(position);
	    final String title = book.getMetaData().getTitle();
	    // Logger.info(tag, "title is:" + title);
	    bookTitile.setText(title);
	    onlineCoverImage.setVisibility(View.VISIBLE);
	    bookDescription.setVisibility(View.VISIBLE);
	    String desc = book.getMetaData().getDescription();
	    if (desc.length() > 90) {
		int pos;
		pos = desc.substring(90).indexOf(" ");
		desc = desc.substring(0, pos + 90) + "....";
	    }
	    bookDescription.setText(desc);

	    final String coverpageUrl = book.getCoverpage().getWebPath().trim();
	    if (coverpageUrl != "") {
		setCoverpageImage(this, book);
	    }

	    Logger.warn(tag, "BUTTON - download status is:"
		    + book.getDownloadStatus().getStatus());

	    if (book.getDownloadStatus().getStatus()
		    .equals(ResponseStatus.SUCCESS)) {
		Logger.warn(tag,
			"BUTTON - !!!!!!! downloaded so make the view button visible");
		checkForBookInLocal(book);

	    } else if (book.getDownloadStatus().getStatus() == ResponseStatus.IN_PROGRESS
		    && appData.isNetworkConnectionAvailable()) {
		Logger.info(tag, "BUTTON - download in progress");
		new Thread(new Runnable() {
		    @Override
		    public void run() {
			while (book.getDownloadPercentage() <= 99
				&& book.getDownloadStatus().getStatus() == ResponseStatus.IN_PROGRESS) {
			    updateButtonState(emptyButton, book,
				    book.getDownloadPercentage());

			    // Logger.info(tag,
			    // "download in thread in progress("
			    // + book.getDownloadPercentage() + ")");
			}

			if (book.getDownloadPercentage() > 99) {
			    checkForBookInLocal(book);
			}

			// Logger.info(
			/*
			 * tag, "download in thread finished(" +
			 * book.getDownloadPercentage() + ")");
			 */
		    }
		}).run();

		// TODO FIX -- value not updating
		updateButtonState(emptyButton, book,
			book.getDownloadPercentage());

		// Logger.info("OnlineActivity", "download in progress");
	    } else {
		if (appData.isNetworkConnectionAvailable()) {
		    updateButtonState(emptyButton, book, -2);

		} else {
		    emptyButton.setEnabled(false);
		}
	    }

	    Logger.error(tag, "BUTTON - Download status of "
		    + book.getMetaData().getTitle() + " is:"
		    + book.getDownloadStatus().getStatus());

	    if (!book.getDownloadStatus().getStatus()
		    .equals(ResponseStatus.IN_PROGRESS)
		    && !book.getDownloadStatus().getStatus()
		    .equals(ResponseStatus.NOT_DONE)) {
		Logger.warn(tag,
			"BUTTON - book not downloaded so display downlod button");
		/*
		 * redownloadBook.setVisibility(View.GONE);
		 * partialDownload.setVisibility(View.GONE);
		 */
		checkForBookInLocal(book);
	    }
	   

	    emptyButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View paramView) {
		    if (emptyButton.getText().toString()
			    .equalsIgnoreCase("Failed! Retry?")||emptyButton.getText().toString()
			    .equalsIgnoreCase("Download")||emptyButton.getText().toString()
			    .equalsIgnoreCase("Redownload")) {
			downloadBook(book, emptyButton);
		    }
		    else if(emptyButton.getText().toString()
				    .equalsIgnoreCase("View")){

			    try {
			    Toast.makeText(getApplication().getApplicationContext(), "It will take few seconds to open the book..", Toast.LENGTH_LONG).show();
				vegaConfig.setValue(VegaConstants.HISTORY_BOOK_ID,
					book.getBookId());
			    } catch (final ColumnDoesNoteExistsException e) {
				e.printStackTrace();
			    }
			    final Intent intent = new Intent(OnlineActivity.this,
				    ReadbookActivity.class);
			    if (intent != null) {
				intent.putExtra("book_id", book.getBookId());

				Log.w("From Shelf Activity",
					"Starting Readbook Activity");
				startActivity(intent);
				finish();
			    }
			
		    }
		}
	    });
	    
	}
    }

    /**
     * Downloads cover image for a book
     * 
     * @param p_ctx
     * @param p_book
     */
    private void setCoverpageImage(Context p_ctx, Book p_book) {
	final Context ctx = p_ctx;
	final Book book = p_book;
	final String coverpageUrl = book.getCoverpage().getWebPath();

	final int pos = coverpageUrl.lastIndexOf(".");
	final String extension;

	// If there wasn't any '.' just return the string as is.
	if (pos == -1) {
	    extension = ".png";
	} else {
	    extension = coverpageUrl.substring(pos, coverpageUrl.length());
	}

	final String imgPath = appData.getBookFilesPath(book.getBookId())
		+ "coverpage" + extension;
	Logger.warn(tag, "image path is:" + imgPath);

	try {
	    /*
	     * FileInputStream fis = new FileInputStream(new File(
	     * appData.getBookFilesPath(book.getBookId()) + "coverpage" +
	     * extension));
	     * onlineCoverImage.setImageBitmap(BitmapFactory.decodeStream(fis));
	     */
	    final BitmapFactory.Options o = new BitmapFactory.Options();
	    o.inJustDecodeBounds = true;

	    FileInputStream fis = new FileInputStream(new File(
		    appData.getBookFilesPath(book.getBookId()) + "coverpage"
			    + extension));
	    Logger.warn(
		    tag,
		    "COVERIMAGE - decodestream: "
			    + BitmapFactory.decodeStream(fis, null, o));
	    fis.close();
	    final int IMAGE_MAX_SIZE = 110;
	    int scale = 1;
	    if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
		scale = (int) Math.pow(
			2,
			(int) Math.round(Math.log(IMAGE_MAX_SIZE
				/ (double) Math.max(o.outHeight, o.outWidth))
				/ Math.log(0.5)));
	    }
	    Logger.info(tag, "COVERIMAGE - IMAGE - width is:" + o.outWidth
		    + " height is:" + o.outHeight + " scale is:" + scale);

	    // Decode with inSampleSize
	    final BitmapFactory.Options o2 = new BitmapFactory.Options();
	    o2.inSampleSize = scale;
	    fis = new FileInputStream(new File(appData.getBookFilesPath(book
		    .getBookId()) + "coverpage" + extension));
	    onlineCoverImage.setImageBitmap(BitmapFactory.decodeStream(fis,
		    null, o2));
	    fis.close();

	    if (o.outHeight <= 0 && o.outWidth <= 0) {
		Logger.warn(tag,
			"COVERIMAGE - height and width is -1 so redownload the coverimage");
		if (counter < 5) {
		    downloadCoverImage(book, extension, ctx);
		} else {
		    noCoverImage.setVisibility(View.GONE);
		    onlineCoverImage
		    .setImageResource(R.drawable.book_coverpage);
		}
	    }
	} catch (final Exception e) {
	    onlineCoverImage.setImageResource(R.drawable.book_coverpage);
	    downloadCoverImage(book, extension, p_ctx);
	}
    }

    /**
     * Shows the download progress
     * 
     * @param download_button
     * @param book
     * @param downloadPercentage
     */
    public void updateButtonState(Button download_button, Book book,
	    int downloadPercentage) {

	// Logger.warn("OnlineActivity->updateButtonState",
	// "book download status of " + book.getMetaData().getTitle()
	// + "is " + book.getDownloadStatus().getStatus());

	if (book.getDownloadStatus().getStatus()
		.equals(ResponseStatus.NOT_DONE)
		&& !downloadInProgress) {
	    Logger.warn(tag, "BUTTON -  not downloaded");
	    checkForBookInLocal(book);
	    partialDownload.setVisibility(View.INVISIBLE);
	    download_button.setText("Download");
	    download_button.setClickable(true);
	} else {
	    download_button.setVisibility(View.VISIBLE);
	    // Logger.warn(tag,
	    // "DOWNLOAD - update button state download progress:"
	    // + downloadInProgress);
	    if (book.getDownloadStatus().getStatus()
		    .equals(ResponseStatus.IN_PROGRESS)
		    && downloadInProgress) {
		// Logger.warn(tag,
		// "DOWNLOAD - update button state - book is downloading");
		if (downloadPercentage == -2) {
			download_button.setText("Downloading...");
		    // download_button.setText("Downloading..");
		} else if (downloadPercentage < 0) {
		    Logger.warn(tag, "download progress is negetive");
		} else {
			download_button.setClickable(false);
		    download_button
		    .setText(downloadPercentage + "% Downloaded");
		}

		download_button.setEnabled(false);
	    } else if (book.getDownloadStatus().getStatus()
		    .equals(ResponseStatus.ABORT)) {
		download_button.setText("Cancelled! Restart?");
		download_button.setClickable(true);
		download_button.setEnabled(true);
	    } else if (book.getDownloadStatus().getStatus()
		    .equals(ResponseStatus.FAIL)) {
		Logger.info(tag, "BUTTON -  download failed");
		download_button.setText("Failed! Retry?");
		download_button.setClickable(true);

		download_button.setEnabled(true);
	    } else if (book.getDownloadStatus().getStatus()
		    .equals(DownloadStatus.DELETED)) {
		download_button.setText("Redownload");
		download_button.setClickable(true);
		download_button.setEnabled(true);
	    } else if (book.getDownloadStatus().getStatus()
		    .equals(ResponseStatus.SUCCESS)) {
	    	download_button.setText("View");
	    	download_button.setEnabled(true);
	    	download_button.setClickable(true);
	    	partialDownload.setVisibility(View.INVISIBLE);
	    }
	}
    }

    /**
     * Handles the intent
     * 
     * @return Intent
     */
    private Intent getReadBookIntent() {
	final Intent intent = new Intent();

	// Logger.warn("OnlineActivity", "book file name is "
	// + appData.getBook().getFilename());
	// TODO testing purpose hard coded below to allow always into IF
	if (appData.getBook().getFilename().endsWith(".pdf")) {
	    // File f = new File(appData.getAppTempPath()+"pdf.pdf");
	    final File f = new File(appData.getBookFilesPath(appData.getBookId())
		    + appData.getBook().getFilename());
	    intent.setDataAndType(Uri.fromFile(f), "application/pdf");
	    intent.setAction("android.intent.action.VIEW");
	} else if (appData.getBook().getFilename().endsWith(".epub")) { // defaults
	    // to
	    // EPUB
	    // format
	    intent.setClass(this, ReadbookActivity.class);
	} else if (appData.getBook().getFilename().endsWith(".html")) {
	} else {
	    return null;
	}

	return intent;
    }

    @Override
    public void onResume() {
	super.onResume();
	Logger.warn(tag, "resume");
	startTimer();
	bindEvents();
	if (ApplicationData.isFileExists(appData.getAvailableBooksListXML())) {
		Logger.info(tag, "in if");
		populateBooksList();
	} else {
		Logger.info(tag, "download");
		ShowProgressBar.showProgressBar("Downloading books list.", downloadAvailableBooksList(), this);
	}


	inflateDetailsView(selectedBookPosition);
	if (!appData.isLoginStatus()) {
	    final Intent login = new Intent(this, LoginActivity.class);
	    startActivity(login);
	    Logger.info(tag,
		    "LOGIN status if shelf..." + appData.isLoginStatus());
	    finish();
	}
	try {
	    vegaConfig.setValue(VegaConstants.HISTORY_ACTIVITY,
		    "OnlineActivity");
	} catch (final ColumnDoesNoteExistsException e) {
	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    // TODO Auto-generated method stub
		    Toast.makeText(getApplicationContext(),
			    R.string.COLUMN_DOES_NOT_EXIST_NOTICEBOARDACTIVITY,
			    Toast.LENGTH_SHORT).show();

		    // Logger.warn(tag, "");
		    final Intent intent = new Intent();
		    intent.setClass(OnlineActivity.this, OnlineActivity.class);
		    startActivity(intent);
		    finish();
		}
	    });
	}
    }

    private void downloadBook(final Book book, final Button button_download) {

	// Logger.warn(tag, "NW - book download clicked");

	if (clickable) {
	    // Logger.warn(tag,
	    // "NW - cant download as nw is unavailable");
	    final ServerRequests serverRequests = new ServerRequests(
		    getApplicationContext());
	    final DownloadType downloadType = new DownloadType();
	    String deviceId;
	    try {
		deviceId = vegaConfig.getValue(VegaConstants.HISTORY_DEVICEID);
	    } catch (final InvalidConfigAttributeException e) {
		Logger.error(tag, e);
		deviceId = null;
	    }
	    final DownloadStatus ds = new DownloadStatus();

	    String url = "";

	    // szaveri - To provide feedback to the user
	    emptyButton.setText("Download started");

	    url = serverRequests.getRequestURL(
		    com.pearl.network.request.ServerRequests.DOWNLOAD_BOOK,
		    appData.getUserId() + "", book.getBookId() + "", deviceId);
	    // Logger.warn(tag, "url for download book is:" + url);
	    downloadType.setType(DownloadType.BOOK);

	    // Logger.info(tag,
	    // "BookDownload: Download book URL is:" + url);

	    // Setup Download Object
	    final Download bookDownload = new Download(downloadType, url,
		    appData.getBookFilesPath(book.getBookId()),
		    book.getFilename());

	    // Call Download Manager to initiate the download
	    // and handle the flow

	    final DownloadManager dm = new DownloadManager(appData, bookDownload);
	    dm.startDownload(new DownloadCallback() {
		@Override
		public void onSuccess(String downloadedString) {
		    /**
		     * Download is success, so make the View button visible
		     */
		    downloadInProgress = false;
		    // Logger.warn("BookDownload: Onlineactivity",
		    // "***download success so make the view button visible");
		    Logger.info("BookDownload: Onlineactivity",
			    "BUTTON - book downloaded - success");
		    button_download.setText("View");
		    button_download.setClickable(true);
		    button_download.setEnabled(true);
		    ds.setStaus(ResponseStatus.SUCCESS);
		    book.setDownloadStatus(ds);
		    checkForBookInLocal(book);
		}

		@Override
		public void onProgressUpdate(int progressPercentage) {
		    downloadInProgress = true;
		    // Logger.warn(tag,
		    // "DOWNLOAD - book download in progress");
		    ds.setStaus(ResponseStatus.IN_PROGRESS);
		    book.setDownloadStatus(ds);
		    book.setDownloadPercentage(progressPercentage);
		    updateButtonState(button_download, book, progressPercentage);
		}

		@Override
		public void onFailure(String failureMessage) {
		    downloadInProgress = false;
		    ds.setStaus(ResponseStatus.FAIL);
		    book.setDownloadStatus(ds);
		    Toast.makeText(getApplicationContext(),
			    R.string.Unable_to_reach_pearl_server,
			    Toast.LENGTH_SHORT).show();
		    updateButtonState(button_download, book, -1);
		}
	    }, book.getBookId());
	} else {
	    // Logger.warn(tag,
	    // "NW - cant download as nw is unavailable");
	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    Toast.makeText(onlineContext,
			    R.string.network_connection_unreachable,
			    Toast.LENGTH_SHORT).show();
		}
	    });
	}

    }

    public void onNetworkAvailable() {
	Logger.info(tag, "BUTTON - Online In onNetworkAvailable()");
	// //Logger.warn(tag, "network available so enable the refresh button");
	clickable = true;

	/**
	 * Enable a Book Download button b Refresh button
	 */
	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		if (downloadInProgress) {
		    Logger.warn(tag, "BUTTON - Network - download in progress");
		}

		if (booksList != null && booksList.getBookList().size() > 0) {
		    final Book book = booksList.getBookList().get(
			    selectedBookPosition);
		    Logger.warn(tag, "BUTTON - Network - status is:"
			    + book.getDownloadStatus().getStatus());
		    if (book != null) {
			if (!book.getDownloadStatus().getStatus()
				.equals(ResponseStatus.SUCCESS)
				&& !book.getDownloadStatus().getStatus()
				.equals(ResponseStatus.IN_PROGRESS)) {
			    Logger.warn(tag, "BUTTON - if ");
			    if (book.getDownloadStatus().getStatus()
				    .equals(ResponseStatus.FAIL)) {
				Logger.warn(tag,
					"BUTTON - network downlaod failed");
				emptyButton.setVisibility(View.VISIBLE);
			    } else {
				Logger.error(
					tag,
					"BUTTON --------making the download button visible as the status is not fail do status is "
						+ book.getDownloadStatus()
						.getStatus());
				/*
				 * downloadBook.setVisibility(View.VISIBLE);
				 * downloadBook.setClickable(true);
				 * downloadBook.setEnabled(true);
				 */
				// viewBook.setVisibility(View.GONE);
				// emptyButton.setVisibility(View.GONE);
			    }
			} else if (book.getDownloadStatus().getStatus()
				.equals(ResponseStatus.IN_PROGRESS)) {
			}
		    }
		}
		refresh.setAlpha(225);
	    }
	});

	/*if (!ApplicationData.isFileExists(appData.getDownloadedBooksListXML())) {

	    *//**
	     * Forecefully refresh and download the JSON files related to books
	     *//*
	    forceRefresh = true;
	    lastRefresh.setText("Refreshing..");
	    runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    populateBooksList();
		}
	    });

	} else {
	     Logger.warn(tag, "file exists in local");
	}*/

    }

    public void onNetworkUnAvailable() {

	Logger.warn(tag, "In onNetworkUnAvailable()");
	// //Logger.warn(tag,
	// "network not available so disable the refresh button" +
	// selectedBookPosition);
	clickable = false;

	/**
	 * Disable a Book Download button b Refresh button
	 */
	runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		if (booksList != null && booksList.getBookList().size() > 0) {
		    final Book book = booksList.getBookList().get(
			    selectedBookPosition);
		    if (book != null) {
			Log.w(tag, "---- Network unavailble "
				+ book.getDownloadStatus().getStatus());
			if (!book.getDownloadStatus().getStatus()
				.equals(ResponseStatus.SUCCESS)) {
			    Logger.warn(tag,
				    "-- Network unavailable - download status is not success");
			    if (toastCounter < 5)
				Toast.makeText(
					onlineContext,
					R.string.unable_to_download_book_no_network_connection,
					Toast.LENGTH_SHORT).show();
			    if (book.getDownloadStatus().getStatus()
				    .equals(ResponseStatus.FAIL)) {
				Logger.warn(tag,
					"----BUTTON - network downlaod failed");
				emptyButton.setVisibility(View.VISIBLE);
			    } else {
				Logger.warn(tag,
					"-- Network not available and status is not done else");
				partialDownload.setVisibility(View.INVISIBLE);
			    }
			    toastCounter++;
			}
		    }
		}
		refresh.setAlpha(70);
	    }
	});
    }

    private void endTimer() {
	if (timer != null)
	    timer.cancel();
    }

    private int downloadAvailableBooksList() {

	Logger.error("In getBooksList if", "entered");
	final String url = serverRequests.getRequestURL(
		com.pearl.network.request.ServerRequests.AVAILABLEBOOKSLIST,
		appData.getUserId() + "");
	// Logger.warn(tag, "url for available books list is:" + url);
	// Logger.info("OnlineActivity", "available books URL is:" + url);
	final Download booklistDownload = new Download(url,
		appData.getBooksListsPath(),
		ApplicationData.AVAILABLEBOOKSLIST_FILENAME);
	final DownloadManager dm = new DownloadManager(appData, booklistDownload);

	dm.startDownload(new DownloadCallback() {
	    @Override
	    public void onFailure(String failureMessage) {
		Logger.error(tag, "Unable to reach pearl server");
		ShowProgressBar.progressBar.dismiss();
		Toast.makeText(getApplicationContext(),
			R.string.Unable_to_reach_pearl_server,
			Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		// Logger.info("Books list downloadnprogress", ""
		// + progressPercentage);
	    }

	    @Override
	    public void onSuccess(String downloadedString) {
		forceRefresh = false;
		// Re-Populate the books list
		populateBooksList();
		ShowProgressBar.progressBar.dismiss();
	    }
	});
	return 100;
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	endTimer();
    }

    public class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {

	int progress;

	@Override
	protected void onPostExecute(Void result) {
	    // TODO Auto-generated method stub
	    refresh.setClickable(true);
	    refresh.setVisibility(View.VISIBLE);
	    progressBar.setVisibility(View.GONE);
	    emptyButton.setClickable(true);
	    populateBooksList();
	    inflateDetailsView(0);
	}

	@Override
	protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    progress = 0;
	    progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    // TODO Auto-generated method stub
	    progressBar.setProgress(values[0]);

	}

	@Override
	protected Void doInBackground(Void... arg0) {
	    // TODO Auto-generated method stub
	    while (progress < 100) {
		progress++;
		publishProgress(progress);
		SystemClock.sleep(100);
	    }
	    return null;
	}
    }

    private void downloadCoverImage(final Book book, String extension,
	    final Context ctx) {

	onlineCoverImage.setImageResource(R.drawable.book_coverpage);
	Logger.warn(tag, "COVERIMAGE - cover image path is:"
		+ book.getCoverpage().getWebPath());
	// image missing.. so re-downloading..
	String url = "";
	Logger.warn(tag, "COVERIMAGE - user id is:" + appData.getUserId()
		+ " and book id is:" + book.getBookId());
	url = serverRequests.getRequestURL(ServerRequests.COVERIMAGE_PATH,
		appData.getUserId() + "", book.getBookId() + "");
	final Download coverimagedownload = new Download(url,
		appData.getBookFilesPath(book.getBookId()), "coverpage"
			+ extension);
	final DownloadManager dm = new DownloadManager(appData, coverimagedownload);
	dm.startDownload(new DownloadCallback() {
	    @Override
	    public void onSuccess(String downloadedString) {
		counter++;
		Logger.warn(tag, "COVERIMAGE - download count is:" + counter);
		Logger.warn(tag, "COVERIMAGE - response from server is:"
			+ downloadedString);
		Logger.info(tag,
			"COVERIMAGE - cover image downloaded successfully");
		setCoverpageImage(ctx, book);
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		Logger.warn(tag,
			"COVERIMAGE - cover page download in progress :"
				+ progressPercentage);
	    }

	    @Override
	    public void onFailure(String failureMessage) {
		Logger.info(tag, "COVERIMAGE - failed to download cover image");
		Toast.makeText(getApplicationContext(),
			R.string.Unable_to_reach_pearl_server,
			Toast.LENGTH_SHORT).show();
	    }
	});
    }

    @Override
    protected void onPause() {
	// TODO Auto-generated method stub
	endTimer();
	super.onPause();
    }

    private void checkForBookInLocal(Book book) {

	if (ApplicationData.isFileExists(appData.getCompleteBookFilePath(book))
		&& book.getDownloadStatus().getStatus()
		.equals(ResponseStatus.NOT_DONE)) {
		emptyButton.setText("Download");
		emptyButton.setClickable(true);
		partialDownload.setVisibility(View.INVISIBLE);
	} else if (ApplicationData.isFileExists(appData
		.getCompleteBookFilePath(book))) {
	    Logger.warn(tag, "BUTTON - CLICK - File exixts");
	    appData.setBook(book);
	    partialDownload.setVisibility(View.INVISIBLE);
	    if (!downloadInProgress){
	    	emptyButton.setText("View");
	    	emptyButton.setEnabled(true);
		    emptyButton.setClickable(true);
	    }
	} else if (!ApplicationData.isFileExists(appData
		.getCompleteBookFilePath(book))
		&& book.getDownloadStatus().getStatus()
		.equals(ResponseStatus.SUCCESS)) {
	    Logger.warn(tag, "BUTTON - CLICK - File doesnot exixts");
	    partialDownload.setVisibility(View.VISIBLE);
	    emptyButton.setText("Redownload");
	    emptyButton.setEnabled(true);
	    emptyButton.setClickable(true);
	} else {
	    Logger.warn(tag, "BUTTON - CLICK - ELSE");
	    emptyButton.setText("View");
	    emptyButton.setClickable(true);
	    emptyButton.setEnabled(true);
	    partialDownload.setVisibility(View.INVISIBLE);
	}

    }
    
   public class GridViewAdapter extends BaseAdapter {
    	private Context context;
    	
    	private class ViewHolder {
    		TextView imageTitle;
    		ImageView imageView;
    	}
    	
    	 public GridViewAdapter(Context c) {
 	        context = c;
 	    }

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row = convertView;
    		ViewHolder holder = null;
    		
    		final Book book=booksList.getBookList().get(position);

    		if (row == null) {
    			LayoutInflater inflater = (LayoutInflater) context
    			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			row = inflater.inflate(R.layout.grid_list_row,parent,false);
    			holder = new ViewHolder();
    			holder.imageTitle = (TextView) row.findViewById(R.id.grid_names);
    			holder.imageView = (ImageView) row.findViewById(R.id.images);
    			row.setTag(holder);
    		} else {
    			holder = (ViewHolder) row.getTag();
    		}

    		//ImageItem item = data.get(position);
    		holder.imageTitle.setText(book.getMetaData().getTitle());
    		
    		final String coverpageUrl = book.getCoverpage().getWebPath();
    		final int pos = coverpageUrl.lastIndexOf(".");
    		final String extension;

    		// If there wasn't any '.' just return the string as is.
    		if (pos == -1) {
    		    extension = ".png";
    		} else {
    		    extension = coverpageUrl.substring(pos, coverpageUrl.length());
    		}

    		final String imgPath = appData.getBookFilesPath(booksList.getBookList().get(position).getBookId())
    			+ "coverpage" + extension;
    		holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
    		holder.imageView.setMaxWidth(150);
			holder.imageView.setMaxHeight(200);
    		return row;
    	}

		@Override
		public int getCount() {
			return booksList.getBookList().size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

    	
    }

}