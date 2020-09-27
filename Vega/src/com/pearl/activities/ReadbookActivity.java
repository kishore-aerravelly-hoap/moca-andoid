/**
 * 
 */
package com.pearl.activities;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.books.Book;
import com.pearl.books.additionalinfo.multipledownload.MultipleDownloads;
import com.pearl.books.pages.BookMark;
import com.pearl.books.pages.Note;
import com.pearl.database.handlers.AnalyticsHandler;
import com.pearl.database.handlers.BookmarkHandler;
import com.pearl.database.handlers.NotesHandler;
import com.pearl.epub.BookInfo;
import com.pearl.epub.EpubReader;
import com.pearl.epub.TableOfContent;
import com.pearl.epub.TableOfContent.Chapter;
import com.pearl.epub.core.EpubEngine;
import com.pearl.epub.search.Search;
import com.pearl.epub.search.SearchResult;
import com.pearl.ereader.pen.tools.ExampleUtils;
import com.pearl.ereader.pen.tools.SPen_SDK_Utils;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.gesture.SimpleGestureFilter;
import com.pearl.gesture.SimpleGestureFilter.SimpleGestureListener;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.security.PearlSecurity;
import com.pearl.util.DateFormat;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.HistoryUpdateListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.samsung.spensdk.applistener.SCanvasModeChangedListener;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadbookActivity.

 */
public class ReadbookActivity extends BaseActivity implements
SimpleGestureListener,OnClickListener,GestureActionHandler {
	public enum Category { theme1,theme2, theme3;}
	
	public enum FontCategory { level1,level2,level3,level4;}
	/** The app data. */
	private ApplicationData appData;
	 
 	/** The detector. */
 	private SimpleGestureFilter detector;
	
	/** The is landscape. */
	private boolean isLandscape = false;// true;
	
	/** The current page num. */
	private int currentPageNum = 1;// this is wrt book
	
	/** The book id. */
	private int bookId;
	
	/** The max page no. */
	private int maxPageNo;
	
	/** The toast display time. */
	private int toastDisplayTime;
	
	/** The exam id. */
	private String examId;
	
	/** The book. */
	private Book book;
	
	/** The open book exam. */
	private boolean openBookExam;
	
	/** The date. */
	private String date;
	
	/** The subject. */
	private String subject;
	
	/** The bookmark id. */
	private int bookmarkId;
	
	/** The font size. */
	private float fontSize;
	private FontCategory fontCategory = FontCategory.level1;
	
	/** The bgcolor. */
	private String bgcolor;
	
	/** The video file name. */
	private String videoFileName;
	// private int fontLevel = 0;
	/** The font level. */
	private int fontLevel = 4;
	
	/** The foregrnd color. */
	private String foregrndColor;
	
	/** The Constant ADD_NOTES. */
	private static final String ADD_NOTES = "ADDNOTES";
	
	/** The Constant ADD_BOOKMARK. */
	private static final String ADD_BOOKMARK = "ADDBOOKMARK";
	
	/** The Constant BOOKMARKSlIST. */
	private static final String BOOKMARKSlIST = "BOOKMARKS_NOTES_lIST";
	
	/** The Constant SAVED_CANVAS_IMAGE_ID. */
	private static final int SAVED_CANVAS_IMAGE_ID = 1;

	private static final String BIGGER_SIZE = null;
	
	/** The salt. */
	private String salt;
	
	/** The bookmarks data. */
	private List<BookMark> bookmarksData = new ArrayList<BookMark>();
	
	/** The notes data. */
	private List<Note> notesData = new ArrayList<Note>();
	
	/** The notes handler. */
	private NotesHandler notesHandler;
	
	/** The notes adapter. */
	private NotesAdapter notesAdapter;
	
	/** The search results adapter. */
	private SearchResultsAdapter searchResultsAdapter;
	
	/** The epub reader. */
	private EpubReader epubReader;
	
	/** The book content. */
	private RelativeLayout bookContent;
	
	/** The Back light value. */
	private float BackLightValue;

	/** The book marks notes empty. */
	private TextView bookMarksNotesEmpty;

	// START - epub
	/** The book info. */
	private BookInfo bookInfo;
	
	/** The toc. */
	private TableOfContent toc;
	
	/** The chapter no. */
	private int chapterNo = 1;
	
	/** The m cur chapter. */
	private int mCurChapter;
	
	/** The chapter path. */
	private String chapterPath;
	
	/** The page content. */
	private String pageContent;
	
	/** The base url. */
	private String baseUrl;
	
	/** The theme. */
	private String theme;
	private Category themeCategory = Category.theme1;
	/** The relative page no. */
	private int relativePageNo;
	
	/** The h. */
	public static Handler h;
	
	/** The begin background tag. */
	private String BEGIN_BACKGROUND_TAG = "<span style='background-color: yellow;''>";
	
	/** The end background tag. */
	private String END_BACKGROUND_TAG = "</span>";
	// END - epub

	/** The list_bookmark. */
	public ListView list_bookmark;
	
	/** The list_layout. */
	public RelativeLayout list_layout;
	
	/** The list_note. */
	public ListView list_note;

	/** The left bookmark image. */
	ImageView leftBookmarkImage;
	
	/** The help. */
	ImageView tableOfContents, help;

	/** The toc adapter. */
	private TableOfContentsAdapter tocAdapter;

	/** The note book. */
	ImageView noteBook;
	
	/** The downloadimage. */
	ImageView downloadimage;
	
	/** The brightness. */
	ImageView brightness;
	//ImageView savedCanvasImage;
	/** The back to shelf. */
	ImageView backToShelf;
	
	/** The font. */
	ImageView font;
	
	/** The theme icon. */
	ImageView themeIcon;
	
	/** The bookmark. */
	ImageView bookmark;
	
	/** The notes. */
	ImageView notes;

	/** The back to exam. */
	ImageView backToExam;
	
	/** The canvas container. */
	FrameLayout canvasContainer;

	/** The web view scroll. */
	ScrollView webViewScroll;

	/** The book relative layout. */
	LinearLayout bookRelativeLayout;

	/** The go to page. */
	EditText goToPage;
	
	/** The toc list. */
	private ListView tocList;

	/** The brightness select. */
	SeekBar brightnessSelect;

	/** The buttons layout. */
	RelativeLayout buttonsLayout;

	/** The left page content. */
	WebView leftPageContent;
	
	/** The swipe page. */
	WebView swipePage;
	
	/** The search view. */
	private SearchView searchView;
	
	/** The search button. */
	private ImageView searchButton;

	/** The google search button. */
	ImageView googleSearchButton;
	
	/** The dictionary button. */
	ImageView dictionaryButton;
	
	/** The menu button. */
	Button menuButton;

	/** The view image. */
	ImageButton viewImage;
	
	/** The view video. */
	ImageButton viewVideo;

	/** The search block. */
	private LinearLayout searchBlock;
	
	/** The page search results. */
	private ListView pageSearchResults;

	/** The header layout. */
	RelativeLayout headerLayout;
	
	/** The footer layout. */
	LinearLayout footerLayout;
	
	/** The thumbnail. */
	SeekBar thumbnail;

	/** The book_content. */
	private FrameLayout book_content;
	
	/** The bookmark handler. */
	private BookmarkHandler bookmarkHandler;
	
	/** The vega config. */
	private VegaConfiguration vegaConfig;
	
	/** The analytics handler. */
	private AnalyticsHandler analyticsHandler;
	
	/** The multiple downloads. */
	MultipleDownloads multipleDownloads;

	/** The page anotes list. */
	List<Note> pageAnotesList;
	
	/** The page bnotes list. */
	List<Note> pageBnotesList;

	/** The search term. */
	String searchTerm;
	
	/** The search term on change temp. */
	String searchTermOnChangeTemp;

	/** The frame layout. */
	private FrameLayout frameLayout;
	/*
	 * private CanvasView mCanvasView; private SettingView mSettingView;
	 */
	/** The mark btn. */
	private ImageView markBtn;
	
	/** The read btn. */
	private ImageView readBtn;
	
	/** The i. */
	private int i = 0;
	
	/** The mark controls layout. */
	private LinearLayout markControlsLayout;
	
	/** The m pen btn. */
	private ImageView mPenBtn;
	
	/** The m eraser btn. */
	private ImageView mEraserBtn;
	
	/** The m undo btn. */
	private ImageView mUndoBtn;
	
	/** The m redo btn. */
	private ImageView mRedoBtn;
	
	/** The mpen_setting. */
	private ImageView mpen_setting;
	
	/** The pen container. */
	private RelativeLayout penContainer;
	
	/** The m s canvas view. */
	private SCanvasView mSCanvasView;
	
	/** The canvas image load. */
	private ImageView canvasImageLoad;
	
	/** The pen mode. */
	private boolean penMode;
	
	/** The display image. */
	private boolean displayImage=true;
	
	/** The m folder. */
	private File mFolder = null;
	
	/** The default app imagedata directory. */
	private String DEFAULT_APP_IMAGEDATA_DIRECTORY="BOOK_IMAGES";
	
	/** The saved file extension. */
	private String SAVED_FILE_EXTENSION="png";
	
	/** The image loaded per page. */
	private boolean imageLoadedPerPage;
	
	/** The cleared. */
	private boolean cleared;
	
	/** The pen set. */
	private boolean penSet;
	

	/** Pen functionality constants *. */
	
	private final String APPLICATION_ID_NAME="Read Book Activity";
	
	/** The application id version major. */
	private final int APPLICATION_ID_VERSION_MAJOR=2;
	
	/** The application id version minor. */
	private final int APPLICATION_ID_VERSION_MINOR=2;
	
	/** The application id version patchname. */
	private final String APPLICATION_ID_VERSION_PATCHNAME="Debug";
	
	/** The image max size. */
	final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
	
	/** The canvas bytedata length. */
	private static int CANVAS_BYTEDATA_LENGTH=0; 
	
	/** The init. */
	private boolean init=false;

	/** The search results. */
	ArrayList<SearchResult> searchResults;
	
	/** The load progress dialog. */
	ProgressDialog loadProgressDialog;

	/** The activity context. */
	private Context activityContext;
	
	/** The handler. */
	Handler handler;
	
	/**
	 * Checks if is cleared.
	 *
	 * @return the cleared
	 */
	public boolean isCleared() {
		return cleared;
	}

	/**
	 * Sets the cleared.
	 *
	 * @param cleared the cleared to set
	 */
	public void setCleared(boolean cleared) {
		this.cleared = cleared;
	}
	
	/**
	 * Checks if is pen set.
	 *
	 * @return the penSet
	 */
	public boolean isPenSet() {
		return penSet;
	}

	/**
	 * Sets the pen set.
	 *
	 * @param penSet the penSet to set
	 */
	public void setPenSet(boolean penSet) {
		this.penSet = penSet;
	}
	
	/**
	 * Checks if is image loaded per page.
	 *
	 * @return the imageLoadedPerPage
	 */
	public boolean isImageLoadedPerPage() {
		return imageLoadedPerPage;
	}

	/**
	 * Sets the image loaded per page.
	 *
	 * @param imageLoadedPerPage the imageLoadedPerPage to set
	 */
	public void setImageLoadedPerPage(boolean imageLoadedPerPage) {
		this.imageLoadedPerPage = imageLoadedPerPage;
	}
	
		@Override
	public void onCreate(Bundle savedInstanceState) {

		//try {
			super.onCreate(savedInstanceState);
			// Logger.warn(tag, "----------in read onCreate()----------");
			setContentView(R.layout.readbook);
			activityContext = this;
			vegaConfig = new VegaConfiguration(this);
            detector = new SimpleGestureFilter(this,this);
			appData = (ApplicationData) getApplication();
			bookmarkHandler = new BookmarkHandler(this);
			analyticsHandler = new AnalyticsHandler(this, appData);
		if (appData != null) {
			currentPageNum = 1;

			Logger.warn(tag, "Book Id is:" + bookId);
			Logger.warn(tag, "pageNum - Page Number is in onCreate:"
					+ currentPageNum);

			leftBookmarkImage = (ImageView) findViewById(R.id.bookmark_image);

			bookMarksNotesEmpty = (TextView) findViewById(R.id.notesEmpty);

			backToShelf = (ImageView) findViewById(R.id.backToShelf);
			tableOfContents = (ImageView) findViewById(R.id.toc);

			list_bookmark = (ListView) findViewById(R.id.list_bookmark);
			list_layout = (RelativeLayout) findViewById(R.id.hide_bookmark);
			list_note = (ListView) findViewById(R.id.list_notebook);

			webViewScroll = (ScrollView) findViewById(R.id.web_view_scroll);
			webViewScroll.setHorizontalScrollBarEnabled(false);
			bookContent = (RelativeLayout) findViewById(R.id.bookContent_new);

			thumbnail = (SeekBar) findViewById(R.id.thumbnails);
			leftPageContent = (WebView) findViewById(R.id.leftPageContent_new);

			buttonsLayout = (RelativeLayout) findViewById(R.id.buttonsLayout);

			font = (ImageView) findViewById(R.id.font);
			themeIcon = (ImageView) findViewById(R.id.theme);
			bookmark = (ImageView) findViewById(R.id.imageView3);
			notes = (ImageView) findViewById(R.id.imageView5);

			handler = new Handler();
			// rightPageContent = (WebView)
			// findViewById(R.id.rightPageContent);

			// swipePage = (WebView) findViewById(R.id.leftPageContent);

			tocList = (ListView) findViewById(R.id.tocList);

			searchButton = (ImageView) findViewById(R.id.searchButton);

			// noteBook = (ImageView) findViewById(R.id.noteBookEReader);
			downloadimage = (ImageView) findViewById(R.id.downloadimage);

			goToPage = (EditText) findViewById(R.id.gotoPage);

			brightness = (ImageView) findViewById(R.id.brightness);

			backToExam = (ImageView) findViewById(R.id.backToExam_book);

			canvasContainer = (FrameLayout) findViewById(R.id.canvas_container_new);

			viewImage = (ImageButton) findViewById(R.id.view_images);
			help = (ImageView) findViewById(R.id.read_book_help);
			// viewVideo = (ImageButton) findViewById(R.id.view_videos);

			// savedCanvasImage = (ImageView)
			// findViewById(R.id.savedCanvasImage_new);

			// headerLayout = (RelativeLayout)
			// findViewById(R.id.headerLayout);
			footerLayout = (LinearLayout) findViewById(R.id.footerLayout);

			this.theme = "theme1.css";

			// headerLayout.setVisibility(View.GONE);
			footerLayout.setVisibility(View.GONE);

			// canvas
			canvasContainer = (FrameLayout) findViewById(R.id.canvas_container_new);
			book_content = (FrameLayout) findViewById(R.id.bookLayout_new);
			frameLayout = (FrameLayout) findViewById(R.id.bookLayout_new);
			mPenBtn = (ImageView) findViewById(R.id.pen_imageView);
			// penContainer=(RelativeLayout)findViewById(R.id.canvas_viewfor_pen_function);
			mSCanvasView = (SCanvasView) findViewById(R.id.canvasViewforereader);// new
																					// SCanvasView(this);
			canvasImageLoad = (ImageView) findViewById(R.id.load_saved_canvas_image);

			/*
			 * mSCanvasView.setSCanvasInitializeListener(new
			 * SCanvasInitializeListener() {
			 * 
			 * @Override public void onInitialized() {
			 * if(!mSCanvasView.setAppID(APPLICATION_ID_NAME,
			 * APPLICATION_ID_VERSION_MAJOR, APPLICATION_ID_VERSION_MINOR,
			 * APPLICATION_ID_VERSION_PATCHNAME))
			 * Toast.makeText(getApplicationContext(), "Fail to set Id.",
			 * Toast.LENGTH_SHORT).show();
			 * 
			 * if(!mSCanvasView.setTitle("Read Book Pen Functionality"))
			 * Toast.makeText
			 * (getApplicationContext(),"Fail to set Title.",Toast.
			 * LENGTH_SHORT).show(); } });
			 * 
			 * mSCanvasView.setHistoryUpdateListener(new HistoryUpdateListener()
			 * {
			 * 
			 * @Override public void onHistoryChanged(boolean undo, boolean
			 * redo) {
			 * 
			 * } });
			 */

			help.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDialog();
				}
			});

			HashMap<String, Integer> settingResourceMapInt = SPen_SDK_Utils
					.getSettingLayoutLocaleResourceMap(true, true, true, true);
			HashMap<String, String> settingResourceMapString = SPen_SDK_Utils
					.getSettingLayoutStringResourceMap(true, true, true, true);
			// mSCanvasView.createSettingView(frameLayout,
			// settingResourceMapInt, settingResourceMapString);
			/*
			 * mSCanvasView.setSCanvasModeChangedListener(new
			 * SCanvasModeChangedListener() {
			 * 
			 * @Override public void onModeChanged(int mode) {
			 * updateModeState(); } });
			 */
			mFolder = new File(Environment.getExternalStorageDirectory(),
					DEFAULT_APP_IMAGEDATA_DIRECTORY);
			mPenBtn.setSelected(true);

			// ActionBar
			menuButton = (Button) findViewById(R.id.readbook_menu_button);
			menuButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent actionbar = new Intent(getApplicationContext(),
							actionbar_readbook.class);
					startActivity(actionbar);
				}
			});
			displayImage = false;
			searchView = (SearchView) findViewById(R.id.pageSearch);
			searchView.setSubmitButtonEnabled(true);
			searchTerm = "";
			searchTermOnChangeTemp = "";
			searchResults = new ArrayList<SearchResult>();
			searchBlock = (LinearLayout) findViewById(R.id.searchBlock);
			searchBlock.setVisibility(View.INVISIBLE);

			googleSearchButton = (ImageView) findViewById(R.id.googleSearchButton);
			dictionaryButton = (ImageView) findViewById(R.id.dictionaryButton);

			googleSearchButton.setVisibility(View.INVISIBLE);
			dictionaryButton.setVisibility(View.INVISIBLE);

			// viewImage.setVisibility(View.GONE);
			pageSearchResults = (ListView) findViewById(R.id.pageSearchResults);
			pageSearchResults.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					currentPageNum = searchResults.get(position).getPageNo();
					if (penMode
							&& CANVAS_BYTEDATA_LENGTH != 0
							&& mSCanvasView.getSCanvasBitmapData().length > CANVAS_BYTEDATA_LENGTH) {
						saveFileofCanvas();
					}
					penMode = false;
					setPenSet(false);
					setImageLoadedPerPage(true);
					mSCanvasView.clearAll(true);
					goToPage(currentPageNum);
					webViewScroll.bringToFront();
					searchBlock.setVisibility(View.INVISIBLE);
				}
			});
			bindEvents();

			/*
			 * loadProgressDialog = ProgressDialog.show(activityContext, "",
			 * "Loading.. Please wait", true); loadProgressDialog.hide();
			 */

			date = appData.getDate("MMM dd, yyyy");
			try {
				currentPageNum = Integer.parseInt(vegaConfig
						.getValue(VegaConstants.HISTORY_PAGE_NUMBER));
			} catch (InvalidConfigAttributeException e) {
				currentPageNum = 1;
				Logger.error(tag, e);
			}
			Logger.info(tag, "pageNum - page number from db is:"
					+ currentPageNum);

			// Logger.warn(tag, "Date is:" + date);
			subject = "Maths";
			// In case we are coming from list view
			Bundle bundle = this.getIntent().getExtras();
			// Logger.warn(tag, "Bundle is:" + bundle.size());
			if (bundle != null) {
				if (0 != bundle.getInt(BookmarkHandler.PAGE_NO)) {
					currentPageNum = bundle.getInt(BookmarkHandler.PAGE_NO);
				}
				Logger.warn(tag, "exam id is:" + examId);
				// subject = bundle.getString("Subject");
				Logger.warn(tag,
						"Book id from bundle is:" + bundle.getInt("book_id"));
				if (bundle.getInt("book_id") != 0) {
					bookId = bundle.getInt("book_id");
					Logger.warn(tag, "book id from bundle is:" + bookId);
				}
				examId = bundle.getString(VegaConstants.EXAM_ID);
				openBookExam = bundle.getBoolean("open_book");
				Logger.warn(tag, "pageNum - page No from bundle is:"
						+ currentPageNum);
			}

			if (openBookExam) {
				backToExam.setVisibility(View.VISIBLE);
				menuButton.setVisibility(View.GONE);
				canvasImageLoad.setVisibility(View.INVISIBLE);
				mPenBtn.setVisibility(View.INVISIBLE);
			}

			backToExam.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int questionNo;
					try {
						questionNo = Integer.parseInt(vegaConfig
								.getValue(VegaConstants.HISTORY_QUESTION_NUM));
					} catch (final Exception e) {
						Logger.error(tag, e);
						questionNo = 0;
					}
					Logger.warn(tag, "Read book - question n while setting is:"
							+ questionNo);
					if (null != appData.getCurrentExam()) {
						Logger.warn(tag, "Activity name in app data is:"
								+ appData.getActivityName());
						if (appData.getActivityName().equalsIgnoreCase(
								"QuestionsListActivity")) {
							final Intent intent = new Intent(v.getContext(),
									QuestionsListActivity.class);
							intent.putExtra(VegaConstants.EXAM_ID, examId);
							intent.putExtra(VegaConstants.QUESTION_NUM,
									questionNo);
							startActivity(intent);
							finish();
						} else if (appData.getActivityName().equalsIgnoreCase(
								"QuestionsActivity")) {
							final Intent intent = new Intent(v.getContext(),
									QuestionsActivity.class);
							intent.putExtra(VegaConstants.EXAM_ID, examId);
							intent.putExtra(VegaConstants.QUESTION_NUM,
									questionNo);
							startActivity(intent);
							finish();
						} else {
							final Intent intent = new Intent(v.getContext(),
									NoticeBoardActivity.class);
							startActivity(intent);
						}
					} else {
						// get activity name from data base
						Logger.warn(tag, "app data is null");
					}
				}
			});

			Logger.warn(tag, "book id before setting book is:" + bookId);
			book = appData.getBook(bookId);

			try {
				theme = vegaConfig.getValue(VegaConstants.THEME);
				String[] s1 = new String[0];
				if (theme.contains(".")) {
					s1 = theme.split("\\.");
				}
				themeCategory = Category.valueOf(s1[0]);
				String fontSize = vegaConfig.getValue(VegaConstants.TEXT_SIZE);
				fontLevel = (fontSize != null && !fontSize.isEmpty()) ? (Integer
						.parseInt(fontSize) >= fontLevel) ? Integer
						.parseInt(fontSize) : fontLevel : fontLevel;
				if (fontLevel == 4) {
					fontCategory = FontCategory.valueOf("level1");
				} else if (fontLevel == 5) {
					fontCategory = FontCategory.valueOf("level2");
				} else if (fontLevel == 6) {
					fontCategory = FontCategory.valueOf("level3");
				} else if (fontLevel == 7) {
					fontCategory = FontCategory.valueOf("level4");
				}
			} catch (InvalidConfigAttributeException e2) {
				Logger.error(tag,
						"Exception while getting values from db in oncreate: "
								+ e2);
			}

			leftPageContent.getSettings().setJavaScriptEnabled(true);
			leftPageContent.setVerticalScrollBarEnabled(true);
			leftPageContent.setWebChromeClient(webCC);
			leftPageContent.addJavascriptInterface(new JavascriptBridge(),
					"eReader");

			Logger.warn(tag, "book id before setting the value is:" + bookId);
			try {
				vegaConfig.setValue(VegaConstants.HISTORY_BOOK_ID, bookId);
			} catch (ColumnDoesNoteExistsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/*
			 * Logger.warn( tag, "PARSING - temp path is:" +
			 * appData.getUserBookReaderTempPath(bookId) + " and location is:" +
			 * appData.getBookFilesPath(bookId));
			 */

			if (!new File(appData.getUserBookReaderTempPath(bookId)).exists())
				Toast.makeText(this, R.string.book_does_not_exists_in_local,
						toastDisplayTime).show();
			// Logger.warn(tag, "EPUB EXTRACTION - STARTED");
			if (book != null) {
				appData.setBook(book);
				if (appData != null && appData.getBook() != null
						&& appData.getBookId() != 0)
					setColoumn1(appData.getBookId() + "");

				try {
					epubReader = new EpubReader(
							appData.getUserBookReaderTempPath(bookId),
							appData.getCompleteBookFilePath(book));
				} catch (Exception e) {
					Logger.error(tag, e);
				}
				try {
					// Logger.warn(tag, "EPUB EXTRACTION - END");
					bookInfo = epubReader.parseBookInfo();
					toc = epubReader.parseTableOfContent();
					/*
					 * Logger.warn(tag, "PARSINg - Book info is:" +
					 * bookInfo.getPath());
					 */

				} catch (Exception e) {
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					loadingDialog.cancel();
					boolean parsingError = true;
					Intent intent = new Intent();
					intent.putExtra(VegaConstants.BOOK_ERROR, parsingError);
					intent.putExtra(VegaConstants.BOOK_ID, bookId);
					intent.setClass(getApplicationContext(),
							ShelfActivity.class);
					startActivity(intent);
					finish();
					try {
						throw new Exception("Unable to parse bookInfo / TOC "
								+ e.toString());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (toc.getChapterList().size() < 1) {
					Toast.makeText(getApplicationContext(),
							R.string.book_no_chapter, toastDisplayTime).show();
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					try {
						throw new Exception("TOC contains no chapters");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				salt = PearlSecurity.getSalt(appData.getUserId(), deviceId);

				try {
					maxPageNo = EpubEngine.calculatePageCount(
							epubReader.getBaseUrl(), getApplicationContext(),
							toc, salt);
					if (maxPageNo == 0) {
						Toast.makeText(
								getApplicationContext(),
								"Unexpected error occured while decrypting please contact admin to rectify the problem",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Logger.error(tag, e);
				}
				thumbnail.setMax(maxPageNo); // set the max page number as
												// the size of the page
												// number

				toggleHeaderFooterVisibility();
				goToPage(currentPageNum);
			} else {
				// Logger.warn(tag, "Book is null");
				loadingDialog.cancel();
				Toast.makeText(this, "Please go back to shelf to open book",
						toastDisplayTime).show();
			}
		} else {

			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
		}
		
		mFolder=new File(mFolder.getPath()+File.separator+bookId);
		if(!mFolder.exists()){
			mFolder.mkdirs();
		}
		// Logger.warn(tag, "--- word in search view is:" + searchView.getQuery());

	}
	
	
		
		/** The web cc. */
	WebChromeClient webCC = new WebChromeClient() {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			// Required functionality here
			new AlertDialog.Builder(activityContext)
					.setTitle("Alert")
					.setMessage(message)
					.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm();
								}
							}).setCancelable(false).create().show();

			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage cm) {
			/*
			 * Log.d(cm.sourceId(), "Line " + cm.lineNumber() + ":-" +
			 * cm.message());
			 */
			return true;
		}
	};
	
	/**
	 * Show dialog.
	 */
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
		final List<String> list = HelpParser.getHelpContent("ereader_book view.txt", this);
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

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (!appData.isLoginStatus()) {
			Intent login = new Intent(this, LoginActivity.class);
			startActivity(login);
			Logger.info(tag, "LOGIN status if shelf..." + appData.isLoginStatus());
			finish();
		}
		displayBookmarkImage();
		getNotesListFromDB();
		getBookmarksListFromDB();
	}

	/**
	 * Search for the text the user wishes to find through the entire book.
	 *
	 * @param searchTerm the search term
	 */
	public void doSearch(final String searchTerm) {
		/*ArrayList<SearchResult> searchResults = Search.doSearch(searchTerm,
				epubReader.getBaseUrl(), toc, salt);
		showSearchResults(searchResults);
		*/
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setMessage("Please wait searching..");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();
 		searchResults = new ArrayList<SearchResult>();
		Search search = new Search(searchTerm, epubReader.getBaseUrl(), toc, salt, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				searchResults = Search.doSearch(searchTerm,
						epubReader.getBaseUrl(), toc, salt);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						showSearchResults(searchResults);
						dialog.dismiss();						
					}
				});
			}
			
			@Override
			public void onProgressUpdate(int progressPercentage) {
				
			}
			
			@Override
			public void onFailure(String failureMessage) {
				
			}
		});

		
		/* * Logger.warn("ReadebookActivity->doSearch", "Search term:" +
		 * searchTerm + "| SearchResults count:" + searchResults.size());*/
		 
		
	}

	/**
	 * SEARCH HELPERS.
	 *
	 * @param resultsList the results list
	 */
	private void showSearchResults(ArrayList<SearchResult> resultsList) {
		searchResults = resultsList;

		/*loadProgressDialog = ProgressDialog.show(activityContext, "",
				"Searching.. Please wait", true);*/
		if (searchResults.size() == 0) {
			searchBlock.setVisibility(View.VISIBLE);

			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Search Results for '" + searchTerm + "'")
					.setMessage("No results found matching your search String")
					.setPositiveButton("OK", null).show();
		}

		searchResultsAdapter = new SearchResultsAdapter(activityContext,
				android.R.layout.simple_selectable_list_item, searchResults);

		searchResultsAdapter.notifyDataSetChanged();

		pageSearchResults.setAdapter(searchResultsAdapter);

		//loadProgressDialog.hide();
	}

	/**
	 * The Class SearchResultsAdapter.
	 */
	private class SearchResultsAdapter extends ArrayAdapter<SearchResult> {
		
		/** The items. */
		private List<SearchResult> items;

		/**
		 * Instantiates a new search results adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param searchResultsList the search results list
		 */
		public SearchResultsAdapter(Context context, int textViewResourceId,
				List<SearchResult> searchResultsList) {
			super(context, textViewResourceId, searchResultsList);
			this.items = searchResultsList;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.searchresults_list_row, null);
			}
			SearchResult result = items.get(position);

			if (result != null) {
				TextView tv_title = (TextView) v
						.findViewById(R.id.search_result_title);

				String desc = "Page " + (result.getPageNo());

				if (tv_title != null) {
					tv_title.setText(desc);
				}

				TextView tv_description = (TextView) v
						.findViewById(R.id.search_result_description);

				desc = "<i>"
						+ result.getMatchingString().replace(searchTerm,
								"</i><b>" + searchTerm + "</b><i>") + "</i>";

				if (tv_description != null) {
					tv_description.setText(Html.fromHtml(desc));
				}
			}

			return v;
		}
	}

	/**
	 * Sets the content based on different parameters.
	 *
	 * @param chapNo the chap no
	 * @throws Exception the exception
	 */

	/**
	 * Sets the content for the specified page number
	 * 
	 */
	
	private void openChapter(int chapNo) throws Exception {
		// Log.i(tag, "PARSING: [METHOD] void openChapter(chapNo:" + chapNo +
		// ")");
		// loadCanvas();
		Chapter chapter = null;

		if (list_layout.getVisibility() == View.VISIBLE)
			list_layout.setVisibility(View.GONE);
		if (tocList.getVisibility() == View.VISIBLE)
			tocList.setVisibility(View.GONE);

		try {
			chapter = toc.getChapter(chapNo - 1);
			chapterPath = epubReader.getBaseUrl() + "/" + chapter.getUrl();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(tag, "PARSING: Having problem at " + e.toString());
		}

		// Log.d(tag, "PARSING: About to open chapter");

		// Logger.warn(tag, "PARSING: salt from open chapter " + salt);
		String html = EpubEngine.preprocess(chapterPath, "", "", this, salt);
		// System.out.println(html);

		if (searchTerm != null && searchTerm.length() > 0) {
		//String searchTermTemp=html.substring((html.toLowerCase()).indexOf((searchTerm.toLowerCase())),(html.toLowerCase()).indexOf((searchTerm.toLowerCase()))+searchTerm.length());
			html = html.replaceAll("(?i)"+searchTerm, BEGIN_BACKGROUND_TAG + searchTerm
					+ END_BACKGROUND_TAG);
		}
		this.pageContent = html;

		// load data on webView
		String baseUrl = "file://" + epubReader.getBaseUrl();
		// Log.d(tag, "PARSING: baseUrl: " + baseUrl);
		this.baseUrl = baseUrl;
		// Log.w(tag, "total pages are:" + toc.getTotalSize());
		leftPageContent.loadDataWithBaseURL(baseUrl,
				induceEpubHandlerJSFiles(induceCssFiles(html, theme)),
				"text/html", "UTF-8", null);
		Thread.sleep(500);
		int defaultFontLevel = 4;
		leftPageContent.loadUrl("javascript:setInitialFont(" + defaultFontLevel
				+ "," + fontLevel + ")");
		loadingDialog.hide();
		/*
		 * Log.w("tag", "PROCESSING: HTML CONTENT - " +
		 * induceEpubHandlerJSFiles(induceCssFiles(pageContent, theme)));
		 */
		if (ApplicationData.isFileExists(appData.getBookCanvasFileName(bookId,
				currentPageNum))) {
			// Logger.warn(tag, "file exists");
			//savedCanvasImage.setVisibility(View.VISIBLE);
			FileInputStream fis = new FileInputStream(new File(
					appData.getBookCanvasFileName(bookId, currentPageNum)));
			//savedCanvasImage.setImageBitmap(BitmapFactory.decodeStream(fis));
			//savedCanvasImage.bringToFront();
			/*
			 * Logger.warn(tag,
			 * "all deatils of a book.... but not showing canvas image..pagno" +
			 * currentPageNum + "book id" + bookId + " Input file stream");
			 */
		} else {
			// Logger.warn(tag, "file doesnot exists");
			//savedCanvasImage.setVisibility(View.GONE);
		}

		// set current chapter
		mCurChapter = chapNo;
		// Thread.sleep(OPEN_CHAPTER_FONT_DELAY);
		//Thread.sleep(500);
		// Logger.warn(tag, "FONT -  Open Chapter Setting font to " +
		// fontLevel);
		/*
		 * int defaultFontLevel = 1; int newFont = 4;
		 * leftPageContent.loadUrl("javascript:setInitialFont("
		 * +defaultFontLevel +"," +fontLevel+ ")");
		 */
	}

	/*
	 * private void bringSavedCanvasImageToFront() { try { Logger.warn(tag,
	 * "CANVAS: bringSavedCanvasImageToFront for page number " +
	 * currentPageNum); Logger.warn( tag, "CANVAS: File path = " +
	 * ApplicationData.isFileExists(appData .getBookCanvasFileName(bookId,
	 * currentPageNum)));
	 * 
	 * if (ApplicationData.isFileExists(appData.getBookCanvasFileName( bookId,
	 * currentPageNum))) { Logger.warn(tag, "CANVAS: file exists");
	 * savedCanvasImage.setVisibility(View.VISIBLE); FileInputStream fis = new
	 * FileInputStream(new File( appData.getBookCanvasFileName(bookId,
	 * currentPageNum))); savedCanvasImage
	 * .setImageBitmap(BitmapFactory.decodeStream(fis));
	 * savedCanvasImage.bringToFront();
	 * 
	 * Logger.warn(tag,
	 * "CANVAS: all deatils of a book.... but not showing canvas image..pagno" +
	 * currentPageNum + "book id" + bookId + " Input file stream");
	 * 
	 * } else { Logger.warn(tag, "CANVAS: file doesnot exists");
	 * savedCanvasImage.setVisibility(View.GONE);
	 * leftPageContent.bringToFront(); } } catch (FileNotFoundException e) {
	 * Logger.warn(tag, "CANVAS: Exception file doesnot exists");
	 * savedCanvasImage.setVisibility(View.GONE); } }
	 */

	// TODO XXX
	/**
	 * This method induces javascript method and method call that maximizes the
	 * first image in the page
	 * 
	 * just a quick fix to fix the image stretching issue.
	 *
	 * @param htmlOriginalContent the html original content
	 * @return the string
	 */

	private String induceEpubHandlerJSFiles(String htmlOriginalContent) {
		String htmlManipulatedContent = "";

		String jsScript = "<script language=\"javascript\" type=\"text/javascript\" src=\"file:///android_asset/epub_js/jquery-1.3.2.min.js\"></script>";
		jsScript += "<script language=\"javascript\" type=\"text/javascript\" src=\"file:///android_asset/epub_js/epub_processor.js\"></script>";
		jsScript += "<script language=\"javascript\" type=\"text/javascript\" src=\"file:///android_asset/epub_js/text_selection_processor.js\"></script>";

		htmlManipulatedContent = htmlOriginalContent.replace("</head", jsScript
				+ "</head");
		return htmlManipulatedContent;
	}

	/**
	 * Induce css files.
	 *
	 * @param htmlOriginalContent the html original content
	 * @param theme the theme
	 * @return the string
	 */
	private String induceCssFiles(String htmlOriginalContent, String theme) {
		String htmlManipulatedContent = "";
		// szaveri - read CSS from local system
		String styles = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/epub_js/"
				+ theme + "\" />";
		// String styles =
		// "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://121.241.53.70/ipadtest/pressmart/pdf/"+theme+"\" />";

		htmlManipulatedContent = htmlOriginalContent.replace("</head", styles
				+ "</head");

		return htmlManipulatedContent;
	}

		/**
	 * Go to page.
	 *
	 * @param pageNo the page no
	 */
	private void goToPage(int pageNo) {
		/**
		 * Check for boundary conditions for the page.
		 */
	    setImageLoadedPerPage(true);
		displayImage=true;
		if (pageNo < 1)
			pageNo = 1;
		if (pageNo > maxPageNo)
			pageNo = maxPageNo;

		// Logger.warn(tag, "GOTO - page number is:" + pageNo);
		chapterNo = EpubEngine.getChapterForGivenPage(pageNo);
		// Log.w("tag", "PROCESSING: chapter num is:" + chapterNo);

		relativePageNo = EpubEngine.getRelativePositionForPage(pageNo,
				chapterNo);

		try {
			openChapter(chapterNo);
			/*int defaultFontLevel = 4;
			leftPageContent.loadUrl("javascript:setInitialFont("
					+ defaultFontLevel + "," + fontLevel + ")");*/
			if (ApplicationData.isFileExists(appData.getBookCanvasFileName(
					bookId, currentPageNum))) {
				// Logger.warn(tag, "file exists");
				// loadCanvas();
				//savedCanvasImage.setVisibility(View.VISIBLE);
				FileInputStream fis = new FileInputStream(new File(
						appData.getBookCanvasFileName(bookId, currentPageNum)));
				//savedCanvasImage.setImageBitmap(BitmapFactory.decodeStream(fis));
				//savedCanvasImage.bringToFront();
				/*
				 * Logger.warn(tag,
				 * "all deatils of a book.... but not showing canvas image..pagno"
				 * + currentPageNum + "book id" + bookId +
				 * " Input file stream");
				 */
				// bringSavedCanvasImageToFront();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Log.w("tag", "PROCESSING: Unable to open chapter " + chapterNo);
		}
		leftPageContent.loadUrl("javascript:goToPage(" + relativePageNo + ")");
		// Log.w("tag", "PROCESSING: jumpt to releative Page " +
		// relativePageNo);
	}

	/**
	 * Sets the next page when swiped from Right to left.
	 */
	@Override
	public void moveToNextPage() {
/*
		try {
			// Logger.info(tag, "Gesture - move to next Page");
			leftPageContent.loadUrl("javascript:n()");
			Thread.sleep(MOVE_TO_NEXT_PAGE_DELAY);
			// bringSavedCanvasImageToFront();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	/**
	 * Move to next resource.
	 */
	public void moveToNextResource() {

			if (toc.getChapterList().size() == mCurChapter) {
				return; // we are in last chapter and on last page. Do nothing
			} else {
				mCurChapter = mCurChapter + 1;
			}
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					try {
					openChapter(mCurChapter);
					leftPageContent.loadUrl("javascript:n()");
					}catch (Exception e) {
						Log.e("tag", "Eception while opening next chapter:" + e.toString());
					}
				}
			});
			
		 
	}

	/**
	 * Sets the seekbar.
	 *
	 * @param relativePageNo the new seekbar
	 */
	public void setSeekbar(int relativePageNo) {
		try {
			webViewScroll.scrollTo(0, 0);
			int seekBarPosition = 1;
			if (mCurChapter == 1) {
				if (relativePageNo < 1)
					relativePageNo = 1;
				seekBarPosition = relativePageNo;
			} else {
				int lastChapterPage = EpubEngine
						.getChapterMaxPages(mCurChapter - 2);
				seekBarPosition = lastChapterPage + relativePageNo;
			}
			/*
			 * Log.w("tag", "PROCESSING: SEEKBAR setting to " + seekBarPosition
			 * + " total size " + thumbnail.getMax() + " relative position " +
			 * relativePageNo);
			 */
			currentPageNum = seekBarPosition;
			// Logger.warn(tag, "Page num in set seek bar is:" +
			// currentPageNum);
			vegaConfig.setValue(VegaConstants.HISTORY_PAGE_NUMBER,currentPageNum);
			/*
			 * Logger.warn(tag, "SET SEEKBAR  - current page number " +
			 * currentPageNum);
			 */
			if(!penMode){
			 webViewScroll.bringToFront();
			}
			thumbnail.setProgress(seekBarPosition);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					displayBookmarkImage();
					// loadCanvasImage();
				}
			});
		} catch (Exception e) {
			Logger.error("tag", e);
		}
	}

	/**
	 * Sets the previous page when swiped from Left to Right.
	 */
	@Override
	public void moveToPreviousPage() {
/*
		try {
			// Logger.info(tag, "Gesture - move to previous Page");
			leftPageContent.loadUrl("javascript:p()");
			Thread.sleep(MOVE_TO_PREVIOUS_PAGE_DELAY);
			// bringSavedCanvasImageToFront();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	/**
	 * Move to previous resource.
	 */
	synchronized public void moveToPreviousResource() {

		try {
			if (mCurChapter == 1) {
				return; // if we are on first chapter and on first page, don't
				// do anything
			} else {
				openChapter(mCurChapter - 1);
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						
						leftPageContent.loadUrl("javascript:l()");
						
					}
				},1300);
				
				// bringSavedCanvasImageToFront();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	/**
	 * Displays the bookmark image.
	 */
	private void displayBookmarkImage() {
		// Logger.warn(tag, "BOOKMARK - displayBookmarkImage");
		Logger.warn(tag, "BOOKMARK - page number in DBI is:" + currentPageNum);
		boolean status = bookmarkHandler.getBookmarkStatus(bookId,
				currentPageNum);
		Logger.warn(tag, "BOOKMARK - status is:" + status);
		if (status) {
			// Logger.warn(tag, "BOOKMARK - status if");
			leftBookmarkImage.setVisibility(View.VISIBLE);
			leftBookmarkImage.bringToFront();
		} else {
			// Logger.warn(tag, "BOOKMARK - status else");
			leftBookmarkImage.setVisibility(View.GONE);
		}
	}

	/* (non-Javadoc)
	 * @see com.pearl.book.guesturehandlers.GestureActionHandler#onJustTouch()
	 */
	@Override
	public void onJustTouch() {
		//toggleTableOfcontentsListVisibility();
	}

	/**
	 * Sets the visibilty of the header and footer fragment in the layout.
	 */
	@Override
	public void toggleHeaderFooterVisibility() {
		int visibilityStatus;
		if (footerLayout.getVisibility() == View.VISIBLE) {
			// View is visible, so make it invisible
			visibilityStatus = View.INVISIBLE;
		} else {
			visibilityStatus = View.VISIBLE;
		}
		// headerLayout.setVisibility(visibilityStatus);
		footerLayout.setVisibility(visibilityStatus);

		// additionally -- hiding the search block on clicking the book content
		// searchBlock.setVisibility(View.INVISIBLE);
	}

	/* (non-Javadoc)
	 * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleBookmarksNotesListVisibility()
	 */
	@Override
	public void toggleBookmarksNotesListVisibility() {
		if (list_layout.getVisibility() == View.VISIBLE)
			list_layout.setVisibility(View.GONE);
	}

	/* (non-Javadoc)
	 * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleTableOfcontentsListVisibility()
	 */
	@Override
	public void toggleTableOfcontentsListVisibility() {
		if (tocList.getVisibility() == View.VISIBLE)
			tocList.setVisibility(View.GONE);
		else{
			tocList.setVisibility(View.VISIBLE);
			tocList.bringToFront();
			}
	}
	
	/**
	 * Toggle search view visibility.
	 */
	public void toggleSearchViewVisibility(){
		if(searchView.getVisibility()==View.VISIBLE)
			searchView.setVisibility(View.GONE);
	}

	/**
	 * Change seek bar position.
	 *
	 * @param pageNo the page no
	 */
	public void changeSeekBarPosition(int pageNo) {

		try {
			// Logger.warn(tag, "max page no is:" + maxPageNo);
			// Logger.warn(tag, "page no in change seek bar position is:" +
			// pageNo);
			int changeToPostion;
			if (isLandscape) {
				changeToPostion = (((pageNo + 2) * 100) / maxPageNo);
				changeToPostion = changeToPostion + 1;
			} else {
				changeToPostion = (((pageNo) * 100) / maxPageNo);
			}

			/*
			 * Logger.warn(tag, "--ChangeSeekBarPosition maxPageNo is--:" +
			 * maxPageNo);
			 */
			/*
			 * Logger.warn(tag,
			 * "-- ChangeSeekBarPosition setting the position to--:" +
			 * changeToPostion);
			 */

			thumbnail.setProgress(changeToPostion);
		} catch (Exception e) {
			Toast.makeText(this, "You dont have pages in this book",
					toastDisplayTime).show();
		}
	}

	/**
	 * On pop up book mark image clicked.
	 *
	 * @param button the button
	 */
	public void OnPopUpBookMarkImageClicked(View button) {
		if(tocList.getVisibility()==View.VISIBLE){
			tocList.setVisibility(View.GONE);
		}
		
		if (searchBlock.getVisibility() == View.VISIBLE) {
			searchBlock.setVisibility(View.GONE);
		}

		
		PopupMenu popup = new PopupMenu(this, button);
		popup.getMenuInflater().inflate(R.menu.bookmark_popup, popup.getMenu());
		toggleBookmarksNotesListVisibility();
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getTitle().equals("ADD BOOKMARK")) {
					if (bookmarkHandler.toggleStatus(bookId, currentPageNum)) {
						Toast.makeText(activityContext,
								R.string.bookmark_status, toastDisplayTime)
								.show();
					} else {
						Intent intent = getIntents(ADD_BOOKMARK);
						intent.putExtra("PageNo", currentPageNum);
						intent.putExtra("bookId", bookId);
						intent.putExtra("Date", date);
						intent.putExtra("Row Id", 0);
						startActivity(intent);
						overridePendingTransition(
								R.anim.grow_from_bottomright_to_topleft, 0);
					}
				} else if (item.getTitle().equals("VIEW ALL BOOKMARKS")) {
					// Logger.warn(tag, "BOOKMARK - LIST");
					if(tocList.getVisibility()==View.VISIBLE){
						tocList.setVisibility(View.GONE);
					}
					if (searchBlock.getVisibility() == View.VISIBLE) {
						searchBlock.setVisibility(View.GONE);
					}
					getBookmarksListFromDB();
					list_layout.bringToFront();
					list_layout.setVisibility(View.VISIBLE);
					list_layout
							.setBackgroundResource(R.drawable.ereader_bookmark_menubg);
					list_bookmark.setVisibility(View.VISIBLE);
					list_bookmark.bringToFront();
					list_note.setVisibility(View.GONE);
					if (bookmarksData.size() == 0) {
						bookMarksNotesEmpty.setVisibility(View.VISIBLE);
						bookMarksNotesEmpty.setText(R.string.bookmarkAlert);
					} else {
						bookMarksNotesEmpty.setVisibility(View.INVISIBLE);
					}
				}
				return true;
			}
		});

		popup.show();
	}

	/**
	 * On pop font selection.
	 *
	 * @param button the button
	 */
	public void onPopFontSelection(View button) {
		// fontLevel=1;
		if(tocList.getVisibility()==View.VISIBLE){
			tocList.setVisibility(View.GONE);
		}
		
		if (searchBlock.getVisibility() == View.VISIBLE) {
			searchBlock.setVisibility(View.GONE);
		}
		PopupMenu menu = new PopupMenu(this, button);
		menu.getMenuInflater().inflate(R.menu.font_menu, menu.getMenu());
		MenuItem level1 = menu.getMenu().findItem(R.id.font_level1);
        MenuItem level2 = menu.getMenu().findItem(R.id.font_level2);
        MenuItem level3 = menu.getMenu().findItem(R.id.font_level3);
        MenuItem level4 = menu.getMenu().findItem(R.id.font_level4);
		
		switch (fontCategory)
        {
            case level1: 
            	level1.setChecked(true); 
            	break;
            case level2: 
            	level2.setChecked(true);
            	break;
            case level3: 
            	level3.setChecked(true);
            	break;
            case level4:
            	level4.setChecked(true);
            	break;
            default:
			break;
        }
		menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int newFontLevel = 4;
				if (item.getTitle().equals("Level 1")) {
					newFontLevel = 4;
					fontCategory =  FontCategory.valueOf("level1");
				} else if (item.getTitle().equals("Level 2")) {
					newFontLevel = 5;
					fontCategory =  FontCategory.valueOf("level2");
				} else if (item.getTitle().equals("Level 3")) {
					newFontLevel = 6;
					fontCategory =  FontCategory.valueOf("level3");
				} else if (item.getTitle().equals("Level 4")) {
					newFontLevel = 7;
					fontCategory =  FontCategory.valueOf("level4");
				}
				/*
				 * Logger.info(tag, "FONT - font level when font is changed:" +
				 * fontLevel);
				 */
				// Logger.info(tag, "FONT - new font level is:" +
				// newFontLevel);
				// int deltaFontLevel = newFontLevel - fontLevel;
				// Logger.warn(tag,
				// "FONT - delta font level is:"+deltaFontLevel);
				leftPageContent.loadUrl("javascript:setInitialFont("
						+ fontLevel + "," + newFontLevel + ")");
				if(penMode && CANVAS_BYTEDATA_LENGTH != 0 && mSCanvasView.getSCanvasBitmapData().length>CANVAS_BYTEDATA_LENGTH){
					saveFileofCanvas();
				}
				penMode=false;
				setImageLoadedPerPage(true);
				setPenSet(false);
				mSCanvasView.clearAll(true);
				goToPage(currentPageNum);
				webViewScroll.bringToFront();
				return true;
			}
		});
		menu.show();
	}

	/**
	 * On pop theme selection.
	 *
	 * @param button the button
	 */
	public void onPopThemeSelection(View button) {
		if(tocList.getVisibility()==View.VISIBLE){
			tocList.setVisibility(View.GONE);
		}
		if (searchBlock.getVisibility() == View.VISIBLE) {
			searchBlock.setVisibility(View.GONE);
		}
		PopupMenu menu = new PopupMenu(this, button);
		menu.getMenuInflater().inflate(R.menu.theme_menu, menu.getMenu());
		MenuItem theme1 = menu.getMenu().findItem(R.id.theme1);
        MenuItem theme2 = menu.getMenu().findItem(R.id.theme2);
        MenuItem theme3 = menu.getMenu().findItem(R.id.theme3);
       
		switch (themeCategory)
        {
            case theme1: 
            	theme1.setChecked(true); 
            	break;
            case theme2: 
            	theme2.setChecked(true);
            	break;
            case theme3: 
            	theme3.setChecked(true);
            	break;
            default:
			break;
        }
       
		menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
                  /* item.setChecked(true);
				if (item.getTitle().equals("Theme1")) {
					theme = "theme1.css";
				} else if (item.getTitle().equals("Theme2")) {
					theme = "theme2.css";
				} else if (item.getTitle().equals("Theme3")) {
					theme = "theme3.css";
                   }*/
		        switch (item.getItemId())
		        {
		            case R.id.theme2:
		            	themeCategory = Category.theme2;
		            	theme = Category.theme2.toString().toLowerCase()+".css";
		            	//item.setCheckable(true);
		            	break;
		            case R.id.theme1:  
		            	themeCategory = Category.theme1;
		            	theme = Category.theme1.toString().toLowerCase()+".css"; 
		            	//item.setCheckable(true);
		            	break;
		            case R.id.theme3: 
		            	themeCategory = Category.theme3;
		            	theme = Category.theme3.toString().toLowerCase()+".css";
		            	//item.setCheckable(true);
		            	break;
		            
				}
				try {
					/*leftPageContent.loadDataWithBaseURL(		baseUrl,
							induceEpubHandlerJSFiles(induceCssFiles(
									pageContent, theme)), "text/html", "UTF-8",
							null);
					Thread.sleep(100);*/
					vegaConfig.setValue(VegaConstants.THEME,theme);
					if(penMode && CANVAS_BYTEDATA_LENGTH != 0 && mSCanvasView.getSCanvasBitmapData().length>CANVAS_BYTEDATA_LENGTH){
						saveFileofCanvas();
					}
					penMode=false;
					setImageLoadedPerPage(true);
					setPenSet(false);
					mSCanvasView.clearAll(true);
					goToPage(currentPageNum);
					webViewScroll.bringToFront();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return true;
			}
		});
		menu.show();
	}

	/**
	 * On popaddnoteimage.
	 *
	 * @param button the button
	 */
	public void OnPopaddnoteimage(View button) {
		if(tocList.getVisibility()==View.VISIBLE){
			tocList.setVisibility(View.GONE);
		}
		if (searchBlock.getVisibility() == View.VISIBLE) {
			searchBlock.setVisibility(View.GONE);
		}
		PopupMenu popup = new PopupMenu(this, button);
		popup.getMenuInflater().inflate(R.menu.pop_up_note, popup.getMenu());
		toggleBookmarksNotesListVisibility();

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				/*
				 * Toast.makeText(ReadbookActivity.this,
				 * "Clicked popup menu item " + item.getTitle(),
				 * Toast.LENGTH_SHORT).show();
				 */
				if (item.getTitle().equals("VIEW ALL NOTES")) {
					if(tocList.getVisibility()==View.VISIBLE){
						tocList.setVisibility(View.GONE);
					}
					if (searchBlock.getVisibility() == View.VISIBLE) {
						searchBlock.setVisibility(View.GONE);
					}
					getNotesListFromDB();
					list_layout.bringToFront();
					list_layout.setVisibility(View.VISIBLE);
					list_layout
							.setBackgroundResource(R.drawable.ereader_bookmark_menubg);
					list_bookmark.setVisibility(View.GONE);
					list_note.bringToFront();
					list_note.setVisibility(View.VISIBLE);
					if (notesData.size() == 0) {
						bookMarksNotesEmpty.setVisibility(View.VISIBLE);
						bookMarksNotesEmpty.setText(R.string.notesAlert);
					} else {
						bookMarksNotesEmpty.setVisibility(View.INVISIBLE);
					}

				} else if (item.getTitle().equals("NEW")) {
					Intent intent = getIntents(ADD_NOTES);
					/*
					 * Logger.warn(tag, "CREATE NOTES - current page number " +
					 * currentPageNum);
					 */
					intent.putExtra("PageNo", currentPageNum);
					intent.putExtra("bookId", bookId);
					intent.putExtra("Date", date);
					intent.putExtra("Row Id", 0);
					overridePendingTransition(
							R.anim.grow_from_bottomright_to_topleft, 0);
					startActivity(intent);
				}
				return true;
			}
		});
		popup.show();
	}

	/**
	 * On pen functionality.
	 *
	 * @param view the view
	 */
	public void onPenFunctionality(View view){
		PopupMenu menu = new PopupMenu(this, view);
		menu.getMenuInflater().inflate(R.menu.pen_menu,menu.getMenu());
		setCleared(false);
		menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				penMode=true;
				mSCanvasView.bringToFront();
				if(!init && CANVAS_BYTEDATA_LENGTH==0 ){
					CANVAS_BYTEDATA_LENGTH=(mSCanvasView.getSCanvasBitmapData()!=null)?mSCanvasView.getSCanvasBitmapData().length:CANVAS_BYTEDATA_LENGTH;
					init=(CANVAS_BYTEDATA_LENGTH==0)?false:true;
				}
				if(item.getTitle().toString().equalsIgnoreCase("Pen")){
					if(mSCanvasView.getCanvasMode()== SCanvasConstants.SCANVAS_MODE_INPUT_PEN && isPenSet() )
					{
						mSCanvasView.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
						mSCanvasView.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
						setPenSet(false);
					}else{
						mSCanvasView.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
						updateModeState();
						setPenSet(true);
					}
					if(isImageLoadedPerPage()){
						loadCanvasImage();
						setImageLoadedPerPage(false);
					}
					item.setEnabled(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
				}else if(item.getTitle().toString().equalsIgnoreCase("Eraser")){
					if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
						mSCanvasView.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);;
						mSCanvasView.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
					}else{
						mSCanvasView.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
						updateModeState();
					}
					item.setEnabled(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
				}else if(item.getTitle().toString().equalsIgnoreCase("Undo")){
					if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
					}else if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					}
					mSCanvasView.undo();
					item.setEnabled(mSCanvasView.isUndoable());
				}else if(item.getTitle().toString().equalsIgnoreCase("Redo")){
					
					if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
					}else if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					}
					
					mSCanvasView.redo();
					item.setEnabled(mSCanvasView.isRedoable());
				}else if(item.getTitle().toString().equalsIgnoreCase("Save")){
					if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
					}else if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					}
				 
					// Logger.info(tag, "==========="+CANVAS_BYTEDATA_LENGTH);
						if(!isCleared() && CANVAS_BYTEDATA_LENGTH != 0 && mSCanvasView.getSCanvasBitmapData().length>CANVAS_BYTEDATA_LENGTH){
							saveFileofCanvas();
						}	
					}/*else if(item.getTitle().toString().equalsIgnoreCase("load")){
					if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
					}else if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					}
					displayImage=true;
					mSCanvasView.clearScreen();
					if(displayImage){
						loadCanvasImage();
						Logger.info(tag, "loaded images");
					}
				}*/else if(item.getTitle().toString().equalsIgnoreCase("Clear")){
					if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
					}else if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					}
					setCleared(true);
					mSCanvasView.clearAll(true);
					ExampleUtils.setClearImage("image"+currentPageNum, mFolder);
					//mSCanvasView.clearSCanvasView();
					//mSCanvasView.clearScreen();
				 // mSCanvasView.setClearImageBitmap(null);
				}else if(item.getTitle().toString().equalsIgnoreCase("Close")){
					if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
					}else if(mSCanvasView.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
						mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					}
					if(!isCleared() && CANVAS_BYTEDATA_LENGTH != 0 && mSCanvasView.getSCanvasBitmapData().length>CANVAS_BYTEDATA_LENGTH){
						saveFileofCanvas();
					}
					mSCanvasView.setCanvasMode(mSCanvasView.getCanvasMode());
					penMode=false;
					displayImage=false;
					mSCanvasView.clearAll(true);
					setCleared(false);
					setImageLoadedPerPage(true);
					setPenSet(false);
					/*mSCanvasView.clearScreen();
					mSCanvasView.setClearImageBitmap(null);*/
					webViewScroll.bringToFront();
				}
				return true;
			}
		});
		menu.show();
	}
	
	/**
	 * Gets all the bookmarks from the database that have been done by a user
	 * for a particular book.
	 *
	 * @return the bookmarks list from db
	 * @throws NullPointerException the null pointer exception
	 */
	private void getBookmarksListFromDB() throws NullPointerException {
		try {
			bookmarkHandler = new BookmarkHandler(this);
			// Logger.info(tag, "BOOKMARK - get data from db");
			bookmarksData = bookmarkHandler.fetch(bookId);
			// Logger.info(tag, "BOOKMARK - list size is:" +
			// bookmarksData.size());
			BookmarksAdapter bookmarksAdapter = new BookmarksAdapter(this,
					android.R.layout.simple_selectable_list_item, bookmarksData);
			list_bookmark.setAdapter(bookmarksAdapter);
			bookmarksAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			throw new NullPointerException(e.toString());
		}

		list_bookmark.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				// Logger.warn(tag, "" + bookmarksData.get(position));
				BookMark bookmark = bookmarksData.get(position);
				int pageNum = bookmark.getPageNum();
				// Log.w("In BookMarkList", "--pageNo is--:" + pageNum);
				goToPage(pageNum);
			}

		});
	}

	/**
	 * The Class BookmarksAdapter.
	 */
	private class BookmarksAdapter extends ArrayAdapter<BookMark> {
		
		/** The items. */
		private List<BookMark> items;

		/**
		 * Instantiates a new bookmarks adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param bookmarks the bookmarks
		 */
		public BookmarksAdapter(Context context, int textViewResourceId,
				List<BookMark> bookmarks) {
			super(context, textViewResourceId, bookmarks);
			// Logger.info(tag, "BOOKMARK - constructor");
			this.items = bookmarks;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			// Logger.info(tag, "BOOKMARK - in getview");
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.bookmarks_list_row, null);
			}
			TextView bookmarkView = (TextView) v
					.findViewById(R.id.bookmarkDescId);
			TextView bookmarkPageNum = (TextView) v
					.findViewById(R.id.bookmarkPageNo);
			TextView bookmarkDate = (TextView) v
					.findViewById(R.id.bookmarkDate);
			ImageView bookmarkIcon = (ImageView) v
					.findViewById(R.id.bookmarkListIcon);
			TextView bookmarksAlert = (TextView) v
					.findViewById(R.id.bookmarksAlert);
			final BookMark bookmark = items.get(position);
			bookmarkId = (int) bookmark.getId();
			if (bookmarksData.size() == 0) {
				bookmarksAlert.setVisibility(View.VISIBLE);
			}
			/*
			 * Logger.warn(tag, "BOOKMARK - bookmark id is:" + bookmarkId);
			 * Logger.warn(tag, "BOOKMARK - bookmark desc is:" +
			 * bookmark.getDescription()); Logger.warn(tag,
			 * "BOOKMARK - bookmark page no is:" + bookmark.getPageNum() + "");
			 * Logger.warn(tag, "BOOKMARK - bookmark date is:" +
			 * bookmark.getDate());
			 */
			setDescriptionForBookmarksAndNotes(bookmark.getDescription(),
					bookmarkView);
			bookmarkPageNum.setText(bookmark.getPageNum() + "");
			bookmarkDate.setText("Last modified "
					+ DateFormat.getFormattedTime(bookmark.getDate(),
							userTimeFormat, BOOKMARKSlIST));
			bookmarkIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(v.getContext(), BookmarksActivity.class);
					/*
					 * Logger.warn(tag,
					 * "BOOKMARK - bookmark id when bookmark icon clicked is:" +
					 * (int) bookmark.getId());
					 */
					intent.putExtra("Row Id", (int) bookmark.getId());
					intent.putExtra("BookmarkDesc", bookmark.getDescription());
					intent.putExtra("Date", appData.getDate());
					intent.putExtra("bookId", bookId);
					intent.putExtra("PageNo", bookmark.getPageNum());
					/*
					 * Logger.warn(tag, "page no is:" + bookmark.getPageNum());
					 * Logger.warn(tag, "Bookmark Desc is:" +
					 * bookmark.getDescription());
					 */
					startActivity(intent);
				}
			});
			return v;
		}

		
		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getCount()
		 */
		@Override
		public int getCount() {
			Logger.info(tag, "BOOKMARK - size is:" + items.size());
			return items.size();
		}
	}
	
	/**
	 * Sets the description for bookmarks and notes.
	 *
	 * @param desc the desc
	 * @param view the view
	 */
	private void setDescriptionForBookmarksAndNotes(String desc, TextView view) {
		String boldString = "";
		if (desc.length() > 3) {
			boldString = "<b>" + desc.substring(0, 2) + "</b>";
		} else
			boldString = "<b>" + desc + "</b>";
		if (desc.length() > 20 && desc.length() != 3) {
			/*
			 * Logger.warn( tag, "BOOKMARK - IF desc is:" + boldString + " " +
			 * desc.substring(2, 20) + "......");
			 */
			view.setText(Html.fromHtml(boldString) + desc.substring(2, 20)
					+ "......");
		} else if (desc.length() <= 3) {
			/* Logger.warn(tag, "BOOKMARK - ELSE desc is:" + boldString); */
			view.setText(Html.fromHtml(boldString));
		} else {
			view.setText(Html.fromHtml(boldString)
					+ desc.substring(2, desc.length()));
		}
	}

	/**
	 * Gets the notes from the database the user has created for a particular
	 * book.
	 *
	 * @return the notes list from db
	 * @throws NullPointerException the null pointer exception
	 */
	private void getNotesListFromDB() throws NullPointerException {
		try {
			notesHandler = new NotesHandler(this);
			notesData = notesHandler.fetch(bookId);
			/*
			 * if(notesData.size() == 0) Toast.makeText(this,
			 * "You dont have any notes please click on create notes to create one."
			 * , toastDisplayTime).show();
			 */
			/* Logger.warn(tag, "list size for notes is:" + notesData.size()); */
			notesAdapter = new NotesAdapter(this,
					android.R.layout.simple_selectable_list_item, notesData);
			notesAdapter.notifyDataSetChanged();
			list_note.setAdapter(notesAdapter);
		} catch (Exception e) {
			Logger.error("BookmarknotesList", e);

			throw new NullPointerException(e.toString());
		}

		list_note.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				// Log.e("Cursor Position", "" + position);
				Note notes = notesData.get(position);
				int pageNum = notes.getPageNum();
				// Log.w("In BookMarkList", "--pageNo is--:" + pageNum);
				goToPage(pageNum);
			}
		});
	}

	/**
	 * The Class NotesAdapter.
	 */
	private class NotesAdapter extends ArrayAdapter<Note> {
		
		/** The items. */
		private List<Note> items;

		/**
		 * Instantiates a new notes adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param notes the notes
		 */
		public NotesAdapter(Context context, int textViewResourceId,
				List<Note> notes) {
			super(context, textViewResourceId, notes);
			this.items = notes;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.notes_list_row, null);
			}
			TextView notesView = (TextView) v.findViewById(R.id.notesDescId);
			TextView notesPageNum = (TextView) v
					.findViewById(R.id.notesPageNoId);
			TextView notesDate = (TextView) v.findViewById(R.id.notesDate);
			ImageView notesIcon = (ImageView) v
					.findViewById(R.id.notesListIcon);
			TextView notesAlert = (TextView) v.findViewById(R.id.notesAlert);
			final Note notes = items.get(position);
			notes.getId();
			if (notesData.size() == 0) {
				notesAlert.setVisibility(View.VISIBLE);
			}
			setDescriptionForBookmarksAndNotes(notes.getDescription(),
					notesView);
			notesPageNum.setText(notes.getPageNum() + "");
			notesDate.setText("Last modified "
					+ DateFormat.getFormattedTime(notes.getDate(),
							userTimeFormat, BOOKMARKSlIST));
			notesIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(v.getContext(), NotesActivity.class);
					/*
					 * Logger.warn(tag, "notes id when notes icon clicked is:" +
					 * notes.getId());
					 */
					intent.putExtra("Row Id", notes.getId());
					intent.putExtra("NotesDesc", notes.getDescription());
					intent.putExtra("Date", appData.getDate());
					intent.putExtra("bookId", bookId);
					intent.putExtra("PageNo", notes.getPageNum());
					/* Logger.warn(tag, "Desc is:" + notes.getDescription()); */
					startActivity(intent);
				}
			});
			return v;
		}
	}

	/**
	 * The Class TableOfContentsAdapter.
	 */
	private class TableOfContentsAdapter extends
			ArrayAdapter<TableOfContent.Chapter> {
		
		/** The items. */
		private ArrayList<Chapter> items;

		/**
		 * Instantiates a new table of contents adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param chapter the chapter
		 */
		public TableOfContentsAdapter(Context context, int textViewResourceId,
				ArrayList<Chapter> chapter) {
			super(context, textViewResourceId, chapter);
			Logger.info(tag, "TOC - constructor");
			this.items = chapter;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			Logger.info(tag, "TOC - view is" + v);
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.table_of_contents_list_row, null);
			}
			TextView chapterName = (TextView) v
					.findViewById(R.id.toc_chap_name);
			TextView pageNum = (TextView) v.findViewById(R.id.toc_page_num);
			TextView chapterNum = (TextView) v.findViewById(R.id.toc_chap_num);
			final Chapter chapter = items.get(position);
			String chapterTitle = chapter.getTitle();
			/*
			 * Logger.warn(tag, "TOC - get view chapter title is:" +
			 * chapterTitle);
			 */
			if (chapterTitle.length() > 30) {
				int pos;
				pos = chapterTitle.substring(30).indexOf(" ");
				chapterTitle = chapterTitle.substring(0, pos + 30) + "...";
			}
			chapterName.setText(chapterTitle);
			chapterNum.setText((position + 1) + ".");
			if (position == 0)
				pageNum.setText("1");
			else
				pageNum.setText((EpubEngine.getChapterMaxPages(position - 1) + 1)
						+ "");
			return v;
		}
	}

	/**
	 * Inflate table of contents.
	 */
	public void inflateTableOfContents() {
		if (toc.getChapterList() != null) {
			/*
			 * Logger.warn(tag, "TOC - toc list while inflating is:" +
			 * toc.getChapterList().size());
			 */

			tocAdapter = new TableOfContentsAdapter(this,
					android.R.layout.simple_selectable_list_item,
					toc.getChapterList());
			tocAdapter.notifyDataSetChanged();
			tocList.setAdapter(tocAdapter);
			tocList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					try {
						if(penMode && CANVAS_BYTEDATA_LENGTH != 0 && mSCanvasView.getSCanvasBitmapData().length>CANVAS_BYTEDATA_LENGTH){
							saveFileofCanvas();
						}
						penMode=false;
						setImageLoadedPerPage(true);
						mSCanvasView.clearAll(true);
						setPenSet(false);
						openChapter(position + 1);
						leftPageContent.loadUrl("javascript:n()");
						webViewScroll.bringToFront();
						
					} catch (Exception e) {
						Toast.makeText(activityContext,
								R.string.book_parsing_error, Toast.LENGTH_LONG)
								.show();

						Logger.error(tag,
								"PROCESSING: Exception while opening chap through toc: "
										+ e.toString());
					}
				}
			});
		} else
			Logger.warn(tag, "TOC CHAPTER LIST IS ZERO");
	}

	/**
	 * Bind events.
	 */
	private void bindEvents() {

		// ActionBar
		menuButton = (Button) findViewById(R.id.readbook_menu_button);
		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent actionbar = new Intent(v.getContext(),
						actionbar_readbook.class);
				startActivity(actionbar);
			}
		});

		backToShelf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShelfActivity.class);

				intent.putExtra("USERID", appData.getUserId());
				intent.putExtra("READBOOK_TO_SHELF", "READBOOK_TO_SHELF");
				if (openBookExam)
					intent.putExtra(VegaConstants.EXAM_ID, examId);
				else
					Logger.warn(tag,
							"OPEN BOOK - not passing exam id as bundle as this is not open book");
				startActivity(intent);
				finish();

			}
		});

		goToPage.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (searchBlock.getVisibility() == View.VISIBLE) {
					searchBlock.setVisibility(View.GONE);
				}
				setCleared(false);
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					/*
					 * Log.w("tag", "PROCESSING: PAGINATION - go to:" +
					 * goToPage.getText().toString());
					 */
					if (!"".equalsIgnoreCase(goToPage.getText().toString())) {

						int pageNo = Integer.parseInt(goToPage.getText()
								.toString());
						if (pageNo <= maxPageNo){
							Logger.info(tag, "=========="+CANVAS_BYTEDATA_LENGTH);
							if(!isCleared() && CANVAS_BYTEDATA_LENGTH!=0 && mSCanvasView.getSCanvasBitmapData().length >CANVAS_BYTEDATA_LENGTH ){
								saveFileofCanvas();
							}
							penMode=false;
							setImageLoadedPerPage(true);
							setPenSet(false);
							mSCanvasView.clearAll(true);
							goToPage(pageNo);
							webViewScroll.bringToFront();
						}else

							Toast.makeText(
									v.getContext(),
									"Invalid page Num- Please enter the number between 1 and "
											+ maxPageNo, toastDisplayTime)
									.show();
					} else {
						Toast.makeText(v.getContext(),
								"field cannot be empty,type in a number to jump to a page",
								Toast.LENGTH_SHORT).show();
					}
					return true;
				}
				return false;
			}
		});

		tableOfContents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (searchBlock.getVisibility() == View.VISIBLE) {
					searchBlock.setVisibility(View.GONE);
					
				}
				if(list_bookmark.getVisibility()==View.VISIBLE){
					list_bookmark.setVisibility(View.GONE);
				}
				if (list_layout.getVisibility() == View.VISIBLE) {
					list_bookmark.setVisibility(View.GONE);
					list_layout.setVisibility(View.GONE);
					list_note.setVisibility(View.GONE);
				}
				toggleTableOfcontentsListVisibility();
				inflateTableOfContents();
			}
		});

		thumbnail.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int position = seekBar.getProgress();
				// Log.w("tag", "PROCESSING: Going to page " + position);
				mSCanvasView.clearAll(true);
				goToPage(position);
				webViewScroll.bringToFront();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});

		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(tocList.getVisibility()==View.VISIBLE){
					tocList.setVisibility(View.GONE);
				}
				if (searchBlock.getVisibility() == View.VISIBLE) {
					searchBlock.setVisibility(View.GONE);
				} else {
					searchBlock.setVisibility(View.VISIBLE);
				}
				if(list_layout.getVisibility()==View.VISIBLE){
					list_bookmark.setVisibility(View.GONE);
					list_layout.setVisibility(View.GONE);
					list_note.setVisibility(View.GONE);
				}
				searchBlock.setHorizontalFadingEdgeEnabled(true);
				searchBlock.setVerticalFadingEdgeEnabled(true);

				if (searchTermOnChangeTemp.trim() == "") {
					searchView.requestFocus();
					// searchBlockParams.height = 50;
				} else {
					// searchBlockParams.height = searchBlockDefaultHeight;
				}

				// searchBlock.setLayoutParams(searchBlockParams);
			}
		});

		leftBookmarkImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(tocList.getVisibility()==View.VISIBLE){
					tocList.setVisibility(View.GONE);
				}
				if (searchBlock.getVisibility() == View.VISIBLE) {
					searchBlock.setVisibility(View.GONE);
				}
				bookmarkHandler.delete(bookId, currentPageNum);
				Toast.makeText(v.getContext(), R.string.bookmarkDeleteStatus,
						toastDisplayTime).show();
				displayBookmarkImage();
			}
		});

		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String queryString) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				// TODO handle search onQueryTextChange event

				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
				}
				
				  Logger.warn(tag, "SearchTerm in textlistener is:" +
				  searchTerm +" query string is:"+queryString);
				 
				/* Logger.warn(tag, "query string is:" + queryString); */
				  if(searchTerm != null){
					  if (!searchTerm.equals(queryString)) {
							// Logger.warn(tag, "textlistener -IF");
							searchTerm = queryString;
							Logger.warn(tag, "--- word in search view1 is:"
									+ searchView.getQuery());
							doSearch(searchTerm);
						} else {
							// Logger.warn(tag, "textListener -ELSE");
							showSearchResults(searchResults);
						}					  
				  }
				if (leftPageContent.isFocusable()) {
					leftPageContent.requestFocus();
				}

				return false;
			} 

			@Override
			public boolean onQueryTextChange(String queryString) {
				// showSearchResults(searchTerm);
				// Log.e("onQueryTextChange", "SearchTerm is " + queryString);

				searchTermOnChangeTemp = queryString;

				if (queryString.length() < 1) {
					googleSearchButton.setVisibility(View.INVISIBLE);
					dictionaryButton.setVisibility(View.INVISIBLE);
					pageSearchResults.setAdapter(null);
				} else {
					if (openBookExam) {
						googleSearchButton.setVisibility(View.GONE);
						dictionaryButton.setVisibility(View.GONE);
					} else {
						googleSearchButton.setVisibility(View.VISIBLE);
						dictionaryButton.setVisibility(View.VISIBLE);
					}
				}

				return false;
			}
		});

		googleSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.warn(tag,
						"--- word in search view2 is:" + searchView.getQuery());
				String url = "http://www.google.co.in/search?q="
						+ URLEncoder.encode(searchView.getQuery().toString());
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));

				startActivity(i);
				// finish();
			}
		});

		dictionaryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingDialog.show();

				String url = "http://api-pub.dictionary.com/v001?vid=lw8mk3hzte4nw6mtvtsnp0rnh9ivu55f1d2lhkkbyp&q="
						+ URLEncoder.encode(searchTermOnChangeTemp)
						+ "&type=define&site=dictionary";
				Download dictionaryMeaningDownload = new Download(url, appData
						.getDictionaryMeaningFilesPath(),
						ApplicationData.DICTIONAY_FILENAME);

				DownloadManager dictionaryMeaningDownloader = new DownloadManager(
						appData, dictionaryMeaningDownload);

				dictionaryMeaningDownloader
						.startDownload(new DownloadCallback() {
							@Override
							public void onSuccess(String downloadedString) {
								
								runOnUiThread(new Thread() {
									
									@Override
									public void run() {
										loadingDialog.hide();

										Intent intent = new Intent();
										intent.setClass(activityContext,
												DictionaryDialogActivity.class);
										intent.putExtra("SearchString",
												searchTerm);
										startActivity(intent);
		
									}
								});
							}

							@Override
							public void onProgressUpdate(int progressPercentage) {
							}

							@Override
							public void onFailure(String failureMessage) {
								loadingDialog.hide();
								Toast.makeText(activityContext,
										R.string.Unable_to_reach_pearl_server,
										Toast.LENGTH_LONG).show();
							}
						});
			}
		});

		brightness.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(tocList.getVisibility()==View.VISIBLE){
					tocList.setVisibility(View.GONE);
				}
				if (searchBlock.getVisibility() == View.VISIBLE) {
					searchBlock.setVisibility(View.GONE);
				}
				Dialog dialog = new Dialog(activityContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setCancelable(true);
				dialog.setCanceledOnTouchOutside(true);
				dialog.setContentView(R.layout.brightness_selection);
				WindowManager.LayoutParams WMLP = dialog.getWindow()
						.getAttributes();
				WMLP.width = 450;
				WMLP.height = 300;
				WMLP.x = -70; // x position
				WMLP.y = -250; // y position
				dialog.getWindow().setAttributes(WMLP);
				brightnessSelect = (SeekBar) dialog
						.findViewById(R.id.brightnessSelection);
				brightnessSelect
						.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

							@Override
							public void onProgressChanged(SeekBar arg0,
									int arg1, boolean arg2) {
								BackLightValue = (float) arg1 / 100;
								if (BackLightValue <= 0.5)
									BackLightValue = 0.1f;
								// BackLightSetting.setText(String.valueOf(BackLightValue));
								/*
								 * Logger.warn(tag, "backLight value is:" +
								 * BackLightValue);
								 */
								WindowManager.LayoutParams layoutParams = getWindow()
										.getAttributes();
								layoutParams.screenBrightness = BackLightValue;
								getWindow().setAttributes(layoutParams);
							}

							@Override
							public void onStartTrackingTouch(SeekBar arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onStopTrackingTouch(SeekBar arg0) {
								// TODO Auto-generated method stub

							}
						});
				/*
				 * Logger.warn(tag, "backLight value when dialog is created is:"
				 * + BackLightValue);
				 */
				brightnessSelect.setProgress((int) (BackLightValue * 100));
				dialog.show();
			}
		});

		viewImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activityContext,ImageGalleryActivity.class).putExtra(VegaConstants.PAGE_NUM, currentPageNum);
				startActivity(intent);
			}
		});

		/*
		 * viewVideo.setOnClickListener(new OnClickListener() { public VideoView
		 * videoView = null;
		 * 
		 * @Override public void onClick(View v) { // set up dialog Dialog
		 * dialog = new Dialog(activityContext);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.videodialog);
		 * dialog.setCancelable(true); dialog.setCanceledOnTouchOutside(true);
		 * 
		 * this.videoView = (VideoView) dialog .findViewById(R.id.videoView);
		 * 
		 * videoView.bringToFront();
		 * //videoView.setVideoURI(Uri.parse(ServerRequests.VIDEO_URL));
		 * videoView.setVideoPath(appData.getAdditionalInfoVideoPath()+
		 * videoFileName);
		 * 
		 * videoView.setMediaController(new MediaController( activityContext));
		 * 
		 * videoView.requestFocus(); videoView.start();
		 * 
		 * // now that the dialog is set up, it's time to show it dialog.show();
		 * 
		 * } });
		 */


	}

	/**
	 * Gets the intents.
	 *
	 * @param type the type
	 * @return the intents
	 */
	private Intent getIntents(String type) {
		Intent intent = new Intent();
		// Logger.warn(tag, "type in getIntents is:" + type);
		if (type.equalsIgnoreCase("ADDNOTES")) {
			// Logger.warn(tag, "Notes-------");
			intent.setClass(this, NotesActivity.class);
		} else if (type.equalsIgnoreCase("ADDBOOKMARK")) {
			// Logger.warn(tag, "Bookmark-------");
			intent.setClass(this, BookmarksActivity.class);
		}
		return intent;
	}

	/*
	 * public void saveCanvas() { byte[] buffer = mCanvasView.getData(); //
	 * Samreen - changed the code to handle when book id is null if (appData !=
	 * null && appData.getBook() != null && appData.getBookId() != 0) { File
	 * folder = new File(appData.getBookCanvasFilesPath(appData .getBookId()));
	 * 
	 * if (buffer == null) return; if (!(folder.exists())) folder.mkdirs();
	 * 
	 * String savePath = appData.getBookCanvasFileName( appData.getBookId(),
	 * this.currentPageNum); Log.d(tag, "Save Path = " + savePath);
	 * FileUtils.writeBytedata(savePath, buffer);
	 * 
	 * } else { Log.e("tag", "Unable to get book id, moving to shelf"); Intent
	 * intent = new Intent(); intent.setClass(this, LoginActivity.class);
	 * startActivity(intent); } }
	 */

	/*
	 * public boolean loadCanvas() { String loadPath =
	 * appData.getBookCanvasFileName(appData.getBookId(), this.currentPageNum);
	 * if (!ApplicationData.isFileExists(loadPath)) { Log.d(tag, "Load Path = "
	 * + loadPath);
	 * 
	 * byte[] buffer = FileUtils.readBytedata(loadPath);
	 * 
	 * if (buffer == null) { mCanvasView.clearAll();
	 * 
	 * return false; } Logger.warn(tag, "buffer value is:" + new
	 * String(buffer)); mCanvasView.setData(buffer); return true; } else {
	 * Logger.warn(tag, "file doesnt exists:"); return false; } }
	 */
	/*
	 * private void sendRequestToServer(final String requestUrl, final
	 * DownloadCallback dc) { Logger.warn(tag, "additional info url is:" +
	 * requestUrl); try { new Thread(new Runnable() {
	 * 
	 * @Override public void run() { try { URL u = new URL(requestUrl);
	 * BufferedReader br = new BufferedReader( new
	 * InputStreamReader(u.openStream()), 2048); String str; String
	 * responseString = ""; while ((str = br.readLine()) != null) {
	 * responseString = str; } br.close(); Logger.warn(tag,
	 * "response string is:" + responseString); ObjectMapper mapper = new
	 * ObjectMapper(); BaseResponse1 response = mapper.readValue(
	 * responseString, BaseResponse1.class); String data; if
	 * (response.getData().toString() != null && response.getData().toString()
	 * != "") { data = response.getData().toString(); dc.onSuccess(data); } else
	 * { Logger.warn(tag, "data from json is null"); } } catch
	 * (MalformedURLException e) { Logger.error(tag, e); } catch (IOException e)
	 * { Logger.error(tag, e); }
	 * 
	 * } }).start();
	 * 
	 * } catch (Exception e) { Logger.error(tag, e); } }
	 */

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		// saveCanvas();
		// mCanvasView.destroyDrawingCache();
		Logger.warn(tag, "BOOK- ........readbook ondestroy().......");
		bookmarkHandler.close();
	handler.removeCallbacksAndMessages(null);
		
		/*
		 * if (notesHandler != null) notesHandler.close();
		 */
	//System.runFinalizersOnExit(true);
		// bookReader.closeBook();

	}

	/*
	 * public static class HeaderFragment extends Fragment {
	 * 
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle SavedInstanceState) { return
	 * inflater.inflate(R.layout.readbook_header, container, true); }
	 * 
	 * @Override public void onActivityCreated(Bundle savedInstanceState) {
	 * super.onActivityCreated(savedInstanceState); } }
	 */

	/**
	 * The Class PageFragment.
	 */
	public static class PageFragment extends Fragment {
		
		/* (non-Javadoc)
		 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle SavedInstanceState) {
			return inflater.inflate(R.layout.readbook_pagecontent, container,
					false);
		}

		/* (non-Javadoc)
		 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
		 */
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

		}
	}

	/**
	 * The Class FooterFragment.
	 */
	public static class FooterFragment extends Fragment {
		
		/* (non-Javadoc)
		 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle SavedInstanceState) {
			return inflater.inflate(R.layout.readbook_footer, container, false);
		}

		/* (non-Javadoc)
		 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
		 */
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}
	}

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
	 */
	@Override
	public void onNetworkAvailable() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
	 */
	@Override
	public void onNetworkUnAvailable() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Toast.makeText(getBaseContext(), "dcydgau", Toast.LENGTH_SHORT).show();
	}

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#getActivityType()
	 */
	@Override
	public String getActivityType() {
		return "book_reading";
	}

	 /**
 	 * The Class JavascriptBridge.
 	 */
 	final class  JavascriptBridge {

		 /**
 		 * Previous resource.
 		 */
 		synchronized public void  previousResource() {
			moveToPreviousResource();
		}

		 /**
 		 * Next resource.
 		 */
 		synchronized	public void nextResource() {
			moveToNextResource();
		}

		/**
		 * Sets the current page.
		 *
		 * @param relativePageNo the new current page
		 */
		public void setCurrentPage(final int relativePageNo) {
			/*
			 * Logger.warn(tag, "Page num in set current page is:" +
			 * relativePageNo);
			 */
			setSeekbar(relativePageNo);
		}

		/**
		 * Sets the font level.
		 *
		 * @param jsFontLevel the new font level
		 */
		public void setFontLevel(final int jsFontLevel) {
			try {
				fontLevel = jsFontLevel;
				vegaConfig.setValue(VegaConstants.TEXT_SIZE, fontLevel);
				/*
				 * Logger.warn("tag", "FONT - Font is now set to level " +
				 * fontLevel);
				 */
			} catch (ColumnDoesNoteExistsException e) {
				Logger.warn(tag, e.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (openBookExam && keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	/**
	 * Readbook timer.
	 *
	 * @param HH the hh
	 * @param MM the mm
	 * @param SS the ss
	 */
	public void readbookTimer(int HH, int MM, int SS) {
		if ((HH == 00 && MM == 04 && SS == 00) && openBookExam) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					AlertDialog.Builder _examAlert = new AlertDialog.Builder(
							activityContext);
					_examAlert.setTitle("Hurry up..");
					//_examAlert.setIcon(R.drawable.exam_warning);
					_examAlert
							.setMessage("Four minutes left for exam to complete");
					_examAlert.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					AlertDialog dialog = _examAlert.create();
					dialog.show();
				}
			});

		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onAttachedToWindow()
	 */
	@Override
	public void onAttachedToWindow() {
		if (examId != null)
			openBookExam = true;
		if (openBookExam) {
			KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardLock lock = keyguardManager
					.newKeyguardLock(KEYGUARD_SERVICE);
			lock.disableKeyguard();
			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
			super.onAttachedToWindow();
		} else {
			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
			super.onAttachedToWindow();
		}

	}
  
    /* (non-Javadoc)
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override 
    public boolean dispatchTouchEvent(MotionEvent me){
    		this.detector.onTouchEvent(me);
    		try{
    			return super.dispatchTouchEvent(me);
    		}catch(Exception e){
    			return false;
    		}
    }

/* (non-Javadoc)
 * @see com.pearl.gesture.SimpleGestureFilter.SimpleGestureListener#onSwipe(int)
 */
@Override
 public void onSwipe(int direction) {
	if(!penMode){
		//mSCanvasView.setClearImageBitmap(null);
		//mSCanvasView.clearAll(false);
		//mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
	switch (direction) {
	   case SimpleGestureFilter.SWIPE_RIGHT : 
			leftPageContent.loadUrl("javascript:p()");
			Log.i(tag, "RIGHT");
			break;
	   case SimpleGestureFilter.SWIPE_LEFT :  
			leftPageContent.loadUrl("javascript:n()");
			Log.i(tag, "left");
			break;
	   case SimpleGestureFilter.SWIPE_DOWN :
                                                 break;
	   case SimpleGestureFilter.SWIPE_UP :  
                                                 break;
	}
	displayImage=false;
  }
  
 }
 
 /* (non-Javadoc)
  * @see com.pearl.gesture.SimpleGestureFilter.SimpleGestureListener#onDoubleTap()
  */
 @Override
 public void onDoubleTap() {
	// leftPageContent.reload();
	 try {
	     if(!penMode){
		    mSCanvasView.clearAll(false);
		 }
		 goToPage(currentPageNum);
		//searchTerm=null;
	} catch (Exception e) {
		e.printStackTrace();
	}
	//	webViewScroll.refreshDrawableState();
 }

/* (non-Javadoc)
 * @see com.pearl.book.guesturehandlers.GestureActionHandler#scroll()
 */
@Override
public void scroll() {
	// TODO Auto-generated method stub
	
}

/*@Override
public void onShowPress() {
	Log.i(tag, "I AM PRESSED");	
//goToPage(currentPageNum);
}*/

/*@Override
public void onSingleTapUp() {
	// TODO Auto-generated method stub
	
	Log.i(tag, "I AM TAPPED.....");
}*/

/**
 * Update mode state.
 */
private void updateModeState(){
	int mode=mSCanvasView.getCanvasMode();
	mPenBtn.setEnabled(true);
}

/**
 * Save canvas image.
 *
 * @param bSaveOnlyForegroundImage the b save only foreground image
 * @return true, if successful
 */
private boolean saveCanvasImage(boolean bSaveOnlyForegroundImage) {
	boolean flag=false;
	Bitmap bmCanvas = mSCanvasView.getCanvasBitmap(bSaveOnlyForegroundImage);
	if (!(mFolder.exists()))
		if(!mFolder.mkdirs())
			return flag;
	String savePath = mFolder.getPath() + '/'+ExampleUtils.getExistingFileName(mFolder, "image"+currentPageNum, SAVED_FILE_EXTENSION);//ExampleUtils.getUniqueFilename(mFolder, "image"+currentPageNum, SAVED_FILE_EXTENSION);
		flag=saveBitmapPNG(savePath, bmCanvas);

	return flag;
	
}

/**
 * Save bitmap png.
 *
 * @param strFileName the str file name
 * @param bitmap the bitmap
 * @return true, if successful
 */
private boolean saveBitmapPNG(String strFileName, Bitmap bitmap){		
	if(strFileName==null || bitmap==null)
		return false;

	boolean bSuccess1 = false;	
	boolean bSuccess2;
	boolean bSuccess3;
	File saveFile = new File(strFileName);			

	if(saveFile.exists()) {
		if(!saveFile.delete())
			return false;
	}

	try {
		bSuccess1 = saveFile.createNewFile();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	OutputStream out = null;
	try {
		out = new FileOutputStream(saveFile);
		bSuccess2 = bitmap.compress(CompressFormat.PNG, 50, out);			
	} catch (Exception e) {
		e.printStackTrace();			
		bSuccess2 = false;
	}
	try {
		if(out!=null)
		{
			out.flush();
			out.close();
			bSuccess3 = true;
		}
		else
			bSuccess3 = false;

	} catch (IOException e) {
		e.printStackTrace();
		bSuccess3 = false;
	}finally
	{
		if(out != null)
		{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}				
		}			
	}		

	return (bSuccess1 && bSuccess2 && bSuccess3);
}

/**
 * Save fileof canvas.
 */
private void saveFileofCanvas(){
	saveCanvasImage(false);
	return;
}

/**
 * Load canvas image.
 *
 * @param view the view
 */
	public void loadCanvasImage(View view) {
		displayImage = true;
		mSCanvasView.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN,
				false);
		if (!init && CANVAS_BYTEDATA_LENGTH == 0) {
			CANVAS_BYTEDATA_LENGTH = (mSCanvasView.getSCanvasBitmapData() != null) ? mSCanvasView
					.getSCanvasBitmapData().length : CANVAS_BYTEDATA_LENGTH;
			init = (CANVAS_BYTEDATA_LENGTH == 0) ? false : true;

		}
		if (!loadCanvasImage()) {
			Toast.makeText(this.activityContext, "No data to Display",
					Toast.LENGTH_SHORT).show();
		}
	}


/**
 * Load canvas image.
 *
 * @return true, if successful
 * 

 */

public boolean loadCanvasImage(){
	// penContainer.bringChildToFront(mSCanvasView);
	mSCanvasView.bringToFront();
	mSCanvasView.clearScreen();
	mSCanvasView.setClearImageBitmap(null);
	boolean flag=false;
	             
	
	if(mFolder.exists()){
		if(mFolder.isDirectory()){
			File[] files=mFolder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().startsWith("image"+currentPageNum+"_");
				}
			});
			if(files!=null&& files.length>0){
				final File[] tempFiles=files;
				final int length=tempFiles.length;  
				class SlideShow{
					RefreshHandler handler=new RefreshHandler();
					private int i=0;
					/* (non-Javadoc)
					 * @see android.app.Activity#onCreate(android.os.Bundle)
					 */
					public void updateUi(){
						handler.sleep(1000);
						if(i<length){
							if(tempFiles[i].getName()!=null){
							    loadCanvasImage(tempFiles[i].getName(), true);
							    

								if (mSCanvasView.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_PEN) {
									mSCanvasView.showSettingView(
											SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
								}
								mSCanvasView.setCanvasMode(mSCanvasView.getCanvasMode());
								penMode = false;
								setImageLoadedPerPage(true);
								setPenSet(false);
								/*
								 * mSCanvasView.clearScreen();
								 * mSCanvasView.setClearImageBitmap(null);
								 */
								webViewScroll.bringToFront();
							
							}
							i++;
						}
					}
					
					class RefreshHandler extends Handler
					{
						 @Override
					        public void handleMessage(Message msg) {
					            // TODO Auto-generated method stub
							 
							 if(i<length)
					            SlideShow.this.updateUi();
					        }
					     public void sleep(long delayMillis){
					            this.removeMessages(0);
					            sendMessageDelayed(obtainMessage(0), delayMillis);
					      }
					}
					
				}
				new SlideShow().updateUi();
				flag=true;
				
			}
		}
	}
	return flag;
}

/**
 * Load canvas image.
 *
 * @param fileName the file name
 * @param loadAsForegroundImage the load as foreground image
 * @return true, if successful
 */
private boolean loadCanvasImage(String fileName, boolean loadAsForegroundImage) {
	String loadPath = mFolder.getPath()+'/'+ fileName;
	clearCanvasScreen();
	mSCanvasView.bringToFront();
	if(loadAsForegroundImage){
		Bitmap bmForeground = BitmapFactory.decodeFile(loadPath);
		/*if(bmForeground == null)
			return false;
		int width=bmForeground.getWidth();
		int height=bmForeground.getHeight();
		int nWidth = (int) Math.sqrt(IMAGE_MAX_SIZE / (((double)width ) / height));
		int nHeight = mSCanvasView.getHeight();//mSCanvasView.getHeight();
		bmForeground = Bitmap.createScaledBitmap(bmForeground, nWidth, nHeight, true);*/
		return mSCanvasView.setClearImageBitmap(bmForeground);
	}
	else {
		return mSCanvasView.setBGImagePath(loadPath);
	}
}

/**
 * Clear canvas screen.
 */
private void clearCanvasScreen(){
	mSCanvasView.clearScreen();
//	penMode=false;
//	webViewScroll.bringToFront();
}

}
