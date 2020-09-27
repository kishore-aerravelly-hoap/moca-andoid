/**
 * 
 */
package com.pearl.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import android.R.color;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.booklist.BookList;
import com.pearl.books.Book;
import com.pearl.examination.Exam;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadType;
import com.pearl.network.request.ServerRequests;
import com.pearl.parsers.json.BookListParser;
import com.pearl.parsers.json.ExamParser;
import com.pearl.shelves.drawable.FastBitmapDrawable;
import com.pearl.shelves.view.ShelvesView;

// TODO: Auto-generated Javadoc
/**
 * The Class ShelfActivity.
 */
public class ShelfActivity extends BaseActivity {
	// ArrayList<Book> booksList;
	/** The books list. */
	BookList booksList;

	/** The app data. */
	ApplicationData appData;

	// GridView grid_main;
	/** The search string. */
	String searchString;

	/** The grid adapter. */
	ImageAdapter gridAdapter;

	/** The back to exam. */
	ImageView backToExam;

	/** The prog. */
	ProgressBar prog;

	/** The shelf button. */
	private Button shelfButton;

	/** The exam id. */
	private String examId;

	/** The search text. */
	private EditText searchText;

	/** The no results. */
	private TextView noResults;

	/** The refresh shelf. */
	private ImageView refreshShelf;

	/** The help. */
	private ImageView sync, help;

	/** The online. */
	private ImageView online;

	/** The shelf context. */
	protected Context shelfContext;

	/** The m grid. */
	private ShelvesView mGrid;

	/** The m grid position. */
	private View mGridPosition;

	/** The m popup. */
	private PopupWindow mPopup;

	/** The on click. */
	private boolean onClick;

	/** The i. */
	private int i;

	/** The exam. */
	private Exam exam;

	/** The open book exam. */
	private boolean openBookExam;

	/** The network available. */
	private boolean networkAvailable;

	/** The setalpha. */
	private boolean setalpha;

	/** The m default cover. */
	private FastBitmapDrawable mDefaultCover;

	/** The vega config. */
	private VegaConfiguration vegaConfig;

	/** The m scroll handler. */
	private final Handler mScrollHandler = new ScrollHandler();

	/** The Constant WINDOW_SHOW_DELAY. */
	private static final int WINDOW_SHOW_DELAY = 600;

	/** The m scroll state. */
	private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;

	/** The Constant MESSAGE_UPDATE_BOOK_COVERS. */
	private static final int MESSAGE_UPDATE_BOOK_COVERS = 1;

	/** The Constant COVER_TRANSITION_DURATION. */
	private static final int COVER_TRANSITION_DURATION = 175;

	/** The Constant DELAY_SHOW_BOOK_COVERS. */
	private static final int DELAY_SHOW_BOOK_COVERS = 550;

	/** The m finger up. */
	private boolean mFingerUp = true;

	/** The m pending covers update. */
	private boolean mPendingCoversUpdate;

	/** The Constant WINDOW_DISMISS_DELAY. */
	private static final int WINDOW_DISMISS_DELAY = 600;

	/** The server requests. */
	private ServerRequests serverRequests;

	/** The missing book ids. */
	private final ArrayList<Integer> missingBookIds = new ArrayList<Integer>();

	/** The delete flag. */
	private final boolean deleteFlag = false;
	// private ImageView shelfDelete;
	/** The shelf list view. */
	private ListView shelfListView;

	/** The counter. */
	private int counter = 0;

	/** The grid view. */
	private ImageView selectedGridDisplay, unSelectedGridDisplay;
	/** The list_display. */
	private ImageView selectedListDisplay, unSelectListDisplay;

	/** The grid layout. */
	private RelativeLayout gridLayout;

	/** The books adapter. */
	private BooksAdapter booksAdapter;

	/** The search_layout. */
	private LinearLayout search_layout;

	/** The progress bar. */
	private ProgressBar progressBar;

	/** The user id. */
	private String userId;

	/** The book opening error. */
	private boolean bookOpeningError = false;

	/** The book id to delete. */
	private int bookIdToDelete; // this book id represents the book id that we
	// get from readbook when there is problem with
	// parsing
	/** The selected book position. */
	private int selectedBookPosition;

	/** The h. */
	public static Handler h;

	/** The force refresh. */
	private boolean forceRefresh;
	

	/** The Constant DIALOG_REALLY_EXIT_ID. */
	private static final int DIALOG_REALLY_EXIT_ID = 0;

	/** The pm. */
	PowerManager pm;

	/** The wl. */
	PowerManager.WakeLock wl;
	private boolean gridSelectedFlag = true;
	

	/**
	 * Called when the activity is created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_shelves);
		Logger.info(tag, "SHELF - oncreate");
		getWindow().setBackgroundDrawable(null);
		vegaConfig = new VegaConfiguration(this);
		serverRequests = new ServerRequests(getApplicationContext());
		gridAdapter = new ImageAdapter(this);
		appData = (ApplicationData) getApplication();
		searchText = (EditText) findViewById(R.id.shelfSearch);
		noResults = (TextView) findViewById(R.id.noResults);
		online = (ImageView) findViewById(R.id.vega_store);
		shelfButton = (Button) findViewById(R.id.shelf_menu_button);
		// shelfDelete = (ImageView) findViewById(R.id.shelf_delete);
		shelfListView = (ListView) findViewById(R.id.shelf_list_view);
		gridLayout = (RelativeLayout) findViewById(R.id.grid_layout);
		// gridView = (ImageView) findViewById(R.id.shelf_display);

		// list_display = (ImageView) findViewById(R.id.list_display);
		selectedGridDisplay = (ImageView) findViewById(R.id.selected_grid_display);
		unSelectedGridDisplay = (ImageView) findViewById(R.id.unselected_grid_display);
		unSelectListDisplay = (ImageView) findViewById(R.id.unselected_list_display);
		selectedListDisplay = (ImageView) findViewById(R.id.selected_list_display);

		search_layout = (LinearLayout) findViewById(R.id.search_layout);
		// TODO uncomment the below lines when open book exam is implemented
		backToExam = (ImageView) findViewById(R.id.backToExamFromShelf);
		help = (ImageView) findViewById(R.id.shelf_help);
		// Logger.warn(tag, "user id is:" + appData.getUserId());
		// TODO uncomment the below lines when refresh and sync are enabled
		refreshShelf = (ImageView) findViewById(R.id.shelfRefresh);
		progressBar = (ProgressBar) findViewById(R.id.shelfRefreshProgress);

		// sync = (ImageView) findViewById(R.id.syncShelf);
		Logger.info(tag,
				"LOGIN status if shelf..in OnCreate." + appData.isLoginStatus());
		if (!appData.isLoginStatus()) {
			final Intent login = new Intent(this, LoginActivity.class);
			startActivity(login);
			Logger.info(tag,
					"LOGIN status if shelf..." + appData.isLoginStatus());
			finish();
		}

		Logger.info(tag, "LOGIN status if shelf..in normal flow in Oncreate"
				+ appData.isLoginStatus());
		final Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			Logger.info(tag, "UserId from other previous Intent is " + userId);
			bookOpeningError = bundle.getBoolean(VegaConstants.BOOK_ERROR);
			Logger.warn(tag, "SHELF - parsing error value is:"
					+ bookOpeningError);
			bookIdToDelete = bundle.getInt(VegaConstants.BOOK_ID);
			examId = bundle.getString(VegaConstants.EXAM_ID);
		}
		try {
			selectedBookPosition = Integer.parseInt(vegaConfig
					.getValue(VegaConstants.BOOK_SELECTED_POSITION_SHELF));
		} catch (final InvalidConfigAttributeException e) {
			Logger.error(tag, e);
		}

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setupViews();
		searchText.setVisibility(View.GONE);
		/**
		 * Disabling search text for list view as per requirement.
		 */
		/*
		 * searchText.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence searchstring, int
		 * arg1, int arg2, int arg3) { //Logger.warn(tag,
		 * "SHELF SEARCH - text changed"); //Logger.warn(tag,
		 * "SHELF SEARCH - onTextChanged - search string is:" + searchstring);
		 * populateBookList(searchstring.toString());
		 * 
		 * if(booksList!=null && booksList.getBookList()!=null) booksAdapter =
		 * new BooksAdapter(activityContext,
		 * android.R.layout.simple_selectable_list_item, booksList
		 * .getBookList()); shelfListView.setAdapter(booksAdapter); }
		 * 
		 * @Override public void beforeTextChanged(CharSequence arg0, int arg1,
		 * int arg2, int arg3) { //Logger.warn(tag,
		 * "SHELF SEARCH - before text changed"); //Logger.warn(tag,
		 * "SHELF SEARCH - beforeTextChanged - search string is:" + arg0); }
		 * 
		 * @Override public void afterTextChanged(Editable arg0) {
		 * //Logger.warn(tag, "SHELF SEARCH - after text changed");
		 * //Logger.warn(tag,
		 * /*"SHELF SEARCH - afterTextChanged - search string is:" + arg0); }
		 * });
		 */
	
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
				Logger.warn(tag, "SHELF - question n while setting is:"
						+ questionNo);
				if (null != appData.getCurrentExam()) {
					Logger.warn(
							tag,
							"Activity name in app data is:"
									+ appData.getActivityName());
					if (appData.getActivityName().equalsIgnoreCase(
							"QuestionsListActivity")) {
						final Intent intent = new Intent(v.getContext(),
								QuestionsListActivity.class);
						intent.putExtra(VegaConstants.EXAM_ID, examId);
						intent.putExtra(VegaConstants.QUESTION_NUM, questionNo);
						startActivity(intent);
						finish();
					} else if (appData.getActivityName().equalsIgnoreCase(
							"QuestionsActivity")) {
						final Intent intent = new Intent(v.getContext(),
								QuestionsActivity.class);
						intent.putExtra(VegaConstants.EXAM_ID, examId);
						intent.putExtra(VegaConstants.QUESTION_NUM, questionNo);
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

		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
			}
		});

		bindEvents();
		// populateBookList();
		searchText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
			}
		});
		shelfContext = this;

		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "ShelfActivity");
		wl.acquire();

	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * ((getIntent() != null && getIntent().getExtras() != null) &&
	 * ((getIntent().getExtras().getString("LOGIN") != null && getIntent()
	 * .getExtras().getString("LOGIN").equals("LOGIN")) ||
	 * (getIntent().getExtras().get("ACTIONBAR") != null && getIntent()
	 * .getExtras().getString("ACTIONBAR") .equalsIgnoreCase("ACTIONBAR")) ||
	 * (getIntent() .getExtras().get("READBOOK_TO_SHELF") != null && getIntent()
	 * .getExtras().getString("READBOOK_TO_SHELF")
	 * .equalsIgnoreCase("READBOOK_TO_SHELF"))) || (keyCode ==
	 * KeyEvent.KEYCODE_BACK)) { return true; }
	 * 
	 * return super.onKeyDown(keyCode, event); }
	 */

	/**
	 * Setup views.
	 */
	private void setupViews() {
		mGrid = (ShelvesView) findViewById(R.id.grid_shelves);

		if (null != examId) {
			backToExam.setVisibility(View.VISIBLE);
			online.setVisibility(View.GONE);
			refreshShelf.setVisibility(View.GONE);
			shelfButton.setVisibility(View.INVISIBLE);
			shelfButton.setClickable(false);
			
		}

		 populateBookList();
		try{
			if(vegaConfig.getValue(VegaConstants.SHELF_LIST_SELECTED_HISTORY).equals("0")){
				shelfListView.setVisibility(View.VISIBLE);
				selectedGridDisplay.setVisibility(View.INVISIBLE);
				unSelectedGridDisplay.setVisibility(View.VISIBLE);
				//unSelectListDisplay.setVisibility(View.INVISIBLE);
				selectedListDisplay.setVisibility(View.VISIBLE);
				mGrid.setVisibility(View.INVISIBLE);
				// shelfDelete.setVisibility(View.GONE);
				search_layout.setVisibility(View.INVISIBLE);
				booksAdapter = new BooksAdapter(getApplicationContext(),
						android.R.layout.simple_selectable_list_item, booksList
								.getBookList());
				booksAdapter.notifyDataSetChanged();
				shelfListView.setAdapter(booksAdapter);
			}else{
				mGrid.setVisibility(View.VISIBLE);
				final ShelvesView grid = mGrid;
				grid.setTextFilterEnabled(true);
				grid.setOnScrollListener(new ShelvesScrollManager());
				grid.setOnTouchListener(new FingerTracker());
				grid.setOnItemSelectedListener(new SelectionTracker());
				grid.setOnItemClickListener(new BookViewer()); // Open book
				gridAdapter = new ImageAdapter(this);
			}
		}catch(InvalidConfigAttributeException e){
			Logger.error(tag, e);
		}
	}

	/**
	 * Show dialog.
	 */
	private void showDialog() {
		i = 0;
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog
				.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent(
				"ereader_shelf.txt", this);
		if (list != null && list.size() > 0) {
			tips.setText(list.get(0));
		}
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list != null && list.size() > 0) {
					if (i < (list.size() - 1)) {
						i = i + 1;
						tips.setEnabled(true);
						tips.setText(list.get(i));
					} else
						tips.setEnabled(false);
				}
			}
		});
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list != null && list.size() > 0) {
					if (i > 0) {
						i = i - 1;
						tips.setEnabled(true);
						tips.setText(list.get(i));
					} else
						tips.setEnabled(false);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pearl.activities.BaseActivity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();

		setupViews();
		Logger.warn(tag, "SHELF - onresume");
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
		// making sure that populateBooksList method executes only once.. and
		// only during resume and not fresh start
		// Filling the books list with downloaded books list
		// populateBookList();
		Logger.info(tag,
				"LOGIN status if shelf..in OnResume." + appData.isLoginStatus());
		if (!appData.isLoginStatus()) {
			final Intent login = new Intent(this, LoginActivity.class);
			startActivity(login);
			Logger.info(tag,
					"LOGIN status if shelf..." + appData.isLoginStatus());
			finish();
		}
		Logger.info(tag, "LOGIN status if shelf..OnResume normal flow."
				+ appData.isLoginStatus());
		bindEvents();
		populateBookList();
		gridAdapter = new ImageAdapter(this);
		mGrid.setAdapter(gridAdapter);
		if (bookOpeningError) {
			checkForBookOpeningError();
		} else {
			Logger.info(tag, "SHELF - ther is no problem with parsing");
		}

	}

	/**
	 * Bind events.
	 */
	public void bindEvents() {

		shelfButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent actionbar = new Intent(getApplicationContext(),
						ActionBar.class);
				actionbar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				actionbar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(actionbar);

			}
		});

		online.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent intent = new Intent();
				intent.setClass(getApplicationContext(), OnlineActivity.class);
				startActivity(intent);
				// finish();
			}
		});

		/*
		 * shelfDelete.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (!onClick) { deleteFlag =
		 * false; shelfDelete.setAlpha(70); Toast.makeText(activityContext,
		 * R.string.network_connection_unreachable, toastDisplayTime).show(); }
		 * else { deleteFlag = true; shelfDelete.setAlpha(70); } } });
		 */

		selectedGridDisplay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGrid.setVisibility(View.VISIBLE);
				unSelectedGridDisplay.setVisibility(View.INVISIBLE);
				selectedListDisplay.setVisibility(View.INVISIBLE);
				unSelectListDisplay.setVisibility(View.VISIBLE);
				// unSelectedGridDisplay.setVisibility(View.VISIBLE);
				// selectedGridDisplay.setVisibility(View.INVISIBLE);
				try{
					vegaConfig.setValue(VegaConstants.SHELF_LIST_SELECTED_HISTORY, "0");
				}catch(ColumnDoesNoteExistsException e){
					Logger.error(tag, e);
				}

				// shelfDelete.setVisibility(View.VISIBLE);
				shelfListView.setVisibility(View.GONE);
				search_layout.setVisibility(View.GONE);
			}
		});
		
		selectedListDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unSelectedGridDisplay.setVisibility(View.VISIBLE);
				//unSelectListDisplay.setVisibility(View.VISIBLE);
				selectedGridDisplay.setVisibility(View.INVISIBLE);
				shelfListView.setVisibility(View.VISIBLE);
				mGrid.setVisibility(View.INVISIBLE);
				// shelfDelete.setVisibility(View.GONE);
				search_layout.setVisibility(View.INVISIBLE);
				booksAdapter = new BooksAdapter(v.getContext(),
						android.R.layout.simple_selectable_list_item, booksList
								.getBookList());
				booksAdapter.notifyDataSetChanged();
				shelfListView.setAdapter(booksAdapter);
			}
		});

		unSelectListDisplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedGridDisplay.setVisibility(View.INVISIBLE);
				selectedListDisplay.setVisibility(View.VISIBLE);
				unSelectedGridDisplay.setVisibility(View.VISIBLE);
				shelfListView.setVisibility(View.VISIBLE);
				unSelectListDisplay.setVisibility(View.INVISIBLE);
				mGrid.setVisibility(View.INVISIBLE);
				// shelfDelete.setVisibility(View.GONE);
				search_layout.setVisibility(View.INVISIBLE);
				booksAdapter = new BooksAdapter(v.getContext(),
						android.R.layout.simple_selectable_list_item, booksList
								.getBookList());
				try{
					vegaConfig.setValue(VegaConstants.SHELF_LIST_SELECTED_HISTORY, "0");					
				}catch(ColumnDoesNoteExistsException e){
					Logger.error(tag, e);
				}
				booksAdapter.notifyDataSetChanged();
				shelfListView.setAdapter(booksAdapter);
			}
		});
		
		unSelectedGridDisplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mGrid.setVisibility(View.VISIBLE);
				unSelectedGridDisplay.setVisibility(View.INVISIBLE);
				selectedListDisplay.setVisibility(View.INVISIBLE);
				unSelectListDisplay.setVisibility(View.VISIBLE);
				selectedGridDisplay.setVisibility(View.VISIBLE);
				// unSelectedGridDisplay.setVisibility(View.VISIBLE);
				// selectedGridDisplay.setVisibility(View.INVISIBLE);

				// shelfDelete.setVisibility(View.VISIBLE);
				shelfListView.setVisibility(View.GONE);
				search_layout.setVisibility(View.GONE);
				mGrid.setOnItemClickListener(new BookViewer());
			}
		});
		
		shelfListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				try {
					vegaConfig.setValue(
							VegaConstants.BOOK_SELECTED_POSITION_SHELF,
							position);
				} catch (final ColumnDoesNoteExistsException e) {
					Logger.error(tag, e);
				}
				openBook(position);
			}
		});

		refreshShelf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new asyncTaskUpdateProgress().execute();
				refreshShelf.setVisibility(View.INVISIBLE);
				refreshShelf.setClickable(false);
				if (onClick) {
					refreshShelf.setClickable(false);
					forceRefresh = true;
					populateBookList();
				} else {
					Toast.makeText(activityContext,
							R.string.network_connection_unreachable,
							toastDisplayTime).show();
				}
			}
		});
		// TODO uncomment below code after implementing sync functionality
		/*
		 * sync.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { new
		 * AlertDialog.Builder(v.getContext()).setIcon(
		 * android.R.drawable.ic_dialog_alert).setTitle(
		 * R.string.confirm_redownload_books_title).setMessage(
		 * R.string.confirm_redownload_msg).setPositiveButton( R.string.yes, new
		 * DialogInterface.OnClickListener() { int missingBookPosition;
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) {
		 * Toast.makeText(activityContext, R.string.downloadMissingBooks,
		 * toastDisplayTime).show();
		 * 
		 * DownloadType downloadType = new DownloadType();
		 * downloadType.setType(DownloadType.BOOK);
		 * 
		 * for (int i = 0; i < missingBookIds.size(); i++) { final Book book =
		 * booksList.getBookList() .get(missingBookIds.get(i));
		 * 
		 * missingBookPosition = i; String url = "";
		 * if(book.getFilename().toLowerCase().endsWith(".html")){ url =
		 * "http://172.16.202.131:8081/pearl/rest/encryptedContent/"
		 * +deviceId+"/"+appData.getUserId(); } else{ url =
		 * serverRequests.getRequestURL( ServerRequests.DOWNLOAD_BOOK, "" +
		 * appData.getUserId(), "" + book.getBookId()); }
		 * 
		 * Download download = new Download( downloadType, url, appData
		 * .getBookFilesPath(book .getBookId()), book .getFilename());
		 * DownloadManager bookDownload = new DownloadManager( appData,
		 * download);
		 * 
		 * ProgressBar prog = (ProgressBar) mGrid .getChildAt( missingBookIds
		 * .get(missingBookPosition)) .findViewById(R.id.bookloading);
		 * 
		 * prog.setVisibility(View.VISIBLE);
		 * 
		 * bookDownload.startDownload( new DownloadCallback() { int position =
		 * missingBookPosition; ProgressBar prog;
		 * 
		 * @Override public void onSuccess( String downloadedString) { prog =
		 * (ProgressBar) mGrid .getChildAt( missingBookIds .get(position))
		 * .findViewById( R.id.bookloading); prog
		 * .setVisibility(View.INVISIBLE); }
		 * 
		 * @Override public void onProgressUpdate( int progressPercentage) {
		 * prog = (ProgressBar) mGrid .getChildAt( missingBookIds
		 * .get(position)) .findViewById( R.id.bookloading); prog
		 * .setVisibility(View.VISIBLE); }
		 * 
		 * @Override public void onFailure( String failureMessage) { Logger
		 * .error( "Shelf-Missing Book Download", "Failed to download book - " +
		 * book .getMetaData() .getTitle()); } }, book.getBookId()); }
		 * 
		 * } }).setNegativeButton(R.string.no, new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * Toast.makeText(activityContext, R.string.removeNondownloadedFiles,
		 * toastDisplayTime).show(); //Logger.warn(tag,
		 * "Missing book - book list size " + booksList.getBookList().size());
		 * //Logger.warn(tag, "Missing book - missingBookId size " +
		 * missingBookIds.size()); int positionCounter = 0; while
		 * (missingBookIds.size() > 0){ //always consider the first element
		 * only. int removePosition = missingBookIds.get(0); //Logger.warn(tag,
		 * "Missing book - booklist position to be deleted is:"+removePosition);
		 * //because once a item is removed, index positions are reshuffled
		 * //making it i+1 because i starts from 0 removePosition =
		 * removePosition - (positionCounter); positionCounter++;
		 * //Logger.warn(tag,
		 * "Missing book - booklist position to be deleted is (relative):"
		 * +removePosition); booksList.getBookList().remove(removePosition);
		 * //clear elements from MissingBookId. This is because of race
		 * condition, popuplateBook is called from onCreate and onResume
		 * missingBookIds.remove(0); } //Now clear up the missingBookIds array
		 * 
		 * gridAdapter.notifyDataSetChanged(); } }).show(); } });
		 */
	}

	/**
	 * The Class ImageAdapter.
	 */
	public class ImageAdapter extends BaseAdapter {

		/** The m context. */
		Context mContext;

		/** The Constant ACTIVITY_CREATE. */
		public static final int ACTIVITY_CREATE = 10;

		/**
		 * Instantiates a new image adapter.
		 * 
		 * @param c
		 *            the c
		 */
		public ImageAdapter(Context c) {
			mContext = c;
			counter++;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			if (booksList == null) {
				return 0;
			}
			return booksList.getBookList().size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = null;

			Logger.warn(tag, "before convert view: " + counter);
			// Logger.warn(tag, "View - Convert view is:" + convertView);

			final Book book = booksList.getBookList().get(position);
			Logger.warn(tag, "bookid of" + book.getMetaData().getTitle()
					+ " is:" + book.getBookId());
			final LayoutInflater li = getLayoutInflater();
			v = li.inflate(R.layout.grid_list_row, parent, false);

			// if(ApplicationData.isFileExists(appData.getBookFilesPath(book.getBookId()))){
			if (convertView == null) {

				final ImageView iv = (ImageView) v
						.findViewById(R.id.images);
				final TextView tv= (TextView)v.findViewById(R.id.grid_names);
				tv.setText(book.getMetaData().getTitle());

				Log.e("Book " + book.getBookId() + " Download status",
						book.getDownloadStatus() + "::download percentage is "
								+ book.getDownloadPercentage());

				final String coverpageUrl = book.getCoverpage().getWebPath()
						.trim();
				setCoverImage(coverpageUrl, iv, book.getBookId());

				if (!book.getFilename().toLowerCase().endsWith(".epub")
						&& !book.getFilename().toLowerCase().endsWith(".pdf")
						&& !book.getFilename().toLowerCase().endsWith(".html")) {
					iv.setAlpha(50);
				}
				boolean setAlpha_Exam;
				Logger.warn(tag, "OPEN BOOK - exam id is:" + examId);
				if (null != examId && !examId.equalsIgnoreCase("")) {
					// TODO set the alpha of the delete to restrict book
					// deletion while in exam
					setAlpha_Exam = true;
					openBookExam = true;
					getExamDetails();
					Logger.warn(tag, "OPEN BOOK - booksList size in view is"
							+ exam.getOpenBooks().size());
					for (int j = 0; j < exam.getOpenBooks().size(); j++) {
						Logger.info(tag, "OPEN BOOK - book from appdata is:"
								+ exam.getOpenBooks().get(j));
						Logger.warn(tag, "OPEN BOOK - book id in view is:"
								+ book.getBookId());
						if (book.getBookId() == Integer.parseInt(exam
								.getOpenBooks().get(j).replace("[\"", "")
								.replaceAll("\"]", "").replace("\"", ""))) {
							setAlpha_Exam = false;
							break;
						}
					}
					Logger.warn(tag,
							"OPEN BOOK - alpha of book before setting is:"
									+ setAlpha_Exam);
					if (setAlpha_Exam) {
						iv.setAlpha(70);
					}
				}
				// if (null != examId)
				iv.setMaxWidth(150);
				iv.setMaxHeight(500);

			} else {
				v = convertView;
			}
			/*
			 * }else{ //Logger.warn(tag,
			 * "SHELFVIEW -  file doesnot exists in local"); }
			 */
			return v;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int index) {

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
	}

	/**
	 * Sets the cover image.
	 * 
	 * @param coverpageUrl
	 *            the coverpage url
	 * @param iv
	 *            the iv
	 * @param bookId
	 *            the book id
	 */
	public void setCoverImage(String coverpageUrl, ImageView iv, int bookId) {
		// Logger.warn(tag, "cover page url is:" + coverpageUrl);
		if (coverpageUrl != "") {
			try {
				final int extpos = coverpageUrl.lastIndexOf(".");
				String extention;

				// If there wasn't any '.' just return the string as is.
				if (extpos == -1) {
					extention = ".png";
				} else {
					extention = coverpageUrl.substring(extpos,
							coverpageUrl.length());
				}

				final BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				FileInputStream fis = new FileInputStream(new File(
						appData.getBookFilesPath(bookId) + "coverpage"
								+ extention));
				BitmapFactory.decodeStream(fis, null, o);
				fis.close();
				final int IMAGE_MAX_SIZE = 110;
				int scale = 1;
				if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
					scale = (int) Math.pow(
							2,
							(int) Math.round(Math.log(IMAGE_MAX_SIZE
									/ (double) Math
											.max(o.outHeight, o.outWidth))
									/ Math.log(0.5)));
				}
				// Logger.warn(tag, "IMAGE - width is:" + o.outWidth
				/* + " height is:" + o.outHeight + " scale is:" + scale); */

				// Decode with inSampleSize
				final BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				fis = new FileInputStream(new File(
						appData.getBookFilesPath(bookId) + "coverpage"
								+ extention));
				iv.setImageBitmap(BitmapFactory.decodeStream(fis, null, o2));
				if (o.outHeight <= 0 && o.outWidth <= 0) {
					Logger.warn(tag,
							"COVERIMAGE - height and width is -1 so redownload the coverimage");
					iv.setImageResource(R.drawable.book_coverpage);
				}
				fis.close();
			} catch (final Exception e) {
				iv.setImageResource(R.drawable.book_coverpage);

				final StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				final String stacktrace = sw.toString();

				Log.e("setting coverpage", stacktrace);
			}
		} else {
			iv.setImageResource(R.drawable.book_coverpage);
		}
	}

	/**
	 * Deletes the book from the list of downloaded books.
	 * 
	 * @param bookId
	 *            the book id
	 */
	private void deleteBook(int bookId) {
		Logger.warn(tag, "--- book id for deleting" + bookId);
		final Book book = appData.getBook(bookId);
		Logger.warn(tag, "book for deleting is:" + book);
		if (book != null) {
			if (book.getMetaData() != null) {
				final String bookTitle = book.getMetaData().getTitle();
				Toast.makeText(
						this,
						getString(R.string.deleteBook) + " '" + bookTitle + "'",
						toastDisplayTime).show();
			} else
				Toast.makeText(this, getString(R.string.deleteBook),
						toastDisplayTime).show();
			// TODO delete related files from local file system
			ApplicationData.deleteFolderOrFileRecursively(new File(appData
					.getBookFilesPath(bookId)));// BOOKS_DIR
			Logger.warn(tag, "Book id of the book being deleted is:" + bookId);
			ApplicationData.deleteFolderOrFileRecursively(new File(appData
					.getUserBookReaderTempPath(bookId)));
			final Context ctx = this;

			// Book deletion request
			String url = serverRequests.getRequestURL(
					ServerRequests.DELETE_BOOK, "" + appData.getUserId(), ""
							+ bookId);
			Logger.warn(tag, "url for downloaded booklist" + url);
			/*
			 * url =
			 * serverRequests.getRequestURL(ServerRequests.DOWNLOADED_BOOKSLIST,
			 * "" + appData.getUserId());
			 */
			final DownloadType deleteDownloadType = new DownloadType();
			deleteDownloadType.setType(DownloadType.BOOK_LIST);

			final Download deleteDownload = new Download(deleteDownloadType,
					url, appData.getBooksListsPath(),
					ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME);
			final DownloadManager deleteBook = new DownloadManager(appData,
					deleteDownload);
			deleteBook.startDownload(new DownloadCallback() {
				@Override
				public void onSuccess(String downloadedString) {
					bookOpeningError = false;
				}

				@Override
				public void onProgressUpdate(int progressPercentage) {
					// Logger.warn(tag, "book deletion in progress");
				}

				@Override
				public void onFailure(String failureMessage) {
					// Logger.warn(tag,
					// "book deletion failed could be junk data");

				}
			});

			/*
			 * DeleteBookRequest deleteRequest = new DeleteBookRequest(url);
			 * deleteRequest.request();
			 */
			// Book list re-download request
			url = serverRequests.getRequestURL(
					ServerRequests.DOWNLOADED_BOOKSLIST,
					"" + appData.getUserId());
			final DownloadType downloadType = new DownloadType();
			downloadType.setType(DownloadType.BOOK_LIST);

			final Download download = new Download(downloadType, url,
					appData.getBooksListsPath(),
					ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME);
			final DownloadManager booklistDownloader = new DownloadManager(
					appData, download);
			booklistDownloader.startDownload(new DownloadCallback() {
				@Override
				public void onSuccess(String downloadedString) {
					// Re-Populate the books list
					// Logger.warn(tag, "book deleted successfully");
					populateBookList();

					gridAdapter = new ImageAdapter(ctx);

					mGrid.setAdapter(gridAdapter);
					gridAdapter.notifyDataSetChanged();
				}

				@Override
				public void onProgressUpdate(int progressPercentage) {
					// Logger.warn(tag, "book deletion in progress");

				}

				@Override
				public void onFailure(String failureMessage) {
					// Logger.warn(tag,
					// "book deletion failed could be junk data");

				}
			});

			// Download Available book list

			url = serverRequests
					.getRequestURL(ServerRequests.AVAILABLEBOOKSLIST, ""
							+ appData.getUserId());
			final DownloadType availableDownloadType = new DownloadType();
			downloadType.setType(DownloadType.BOOK_LIST);

			final Download availableBookDownload = new Download(
					availableDownloadType, url, appData.getBooksListsPath(),
					ApplicationData.AVAILABLEBOOKSLIST_FILENAME);
			final DownloadManager availableBookListDownloader = new DownloadManager(
					appData, availableBookDownload);
			availableBookListDownloader.startDownload(new DownloadCallback() {
				@Override
				public void onSuccess(String downloadedString) {
					Logger.warn(tag,
							"successfully downloaded available books list after delete");
				}

				@Override
				public void onProgressUpdate(int progressPercentage) {

				}

				@Override
				public void onFailure(String failureMessage) {

				}
			});
		} else {
			Logger.warn(tag,
					"DELETE - can not delete book as the book id is null");
		}

	}

	/**
	 * Populate book list.
	 * 
	 * @param searchText
	 *            the search text
	 */
	public void populateBookList(String searchText) {

		if (ApplicationData.isFileExists(appData.getDownloadedBooksListXML())) {
			try {
				booksList = BookListParser.getBookList(searchText,
						appData.getDownloadedBooksListXML());
				noResults.setVisibility(View.INVISIBLE);
			} catch (final JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (booksList.getBookList().size() == 0) {

				noResults.bringToFront();
				noResults.setVisibility(View.VISIBLE);

			}
			final List<Integer> missingBooks = new ArrayList<Integer>();
			for (int i = 0; i < booksList.getBookList().size(); i++) {
				final Book book = booksList.getBookList().get(i);

				Log.e("Required Books", "Filename(" + book.getBookId() + ")"
						+ book.getFilename());
				if (!ApplicationData.isFileExists(appData.getBookFilesPath(book
						.getBookId()) + book.getFilename())) {
					Logger.warn(tag, "Missing book position " + i);
					missingBooks.add(book.getBookId());

					// removing missing books from listing.. need to delete from
					// the list in db
					// missingBookIds.add(i);
				}
			}
			for (int i = 0; i < missingBooks.size(); i++) {
				booksList.removeBook(missingBooks.get(i));
			}
		} else {
			Toast.makeText(this, R.string.shelf_search_empty, toastDisplayTime)
					.show();
		}
	}

	/**
	 * Populates the bookList with the book that has been downloaded.
	 */
	private synchronized void populateBookList() {

		// Logger.info("ShelfActivity", "Populating bookslist");
		Logger.info("ShelfActivity", "Populating bookslist from the path : "
				+ appData.getDownloadedBooksListXML());
		if (!ApplicationData.isFileExists(appData.getDownloadedBooksListXML())
				|| forceRefresh) {
			Logger.warn(tag, "file doesnot exists so download");
			downloadBooksList();
		}

		Logger.warn(tag, "file exists so parse");
		// Logger.info("ShelfActivity->Populatebookslist",
		// "In if - reading xml and updating missing books");
		final String filePath = appData.getBooksListsPath()
				+ ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME;
		try {
			booksList = BookListParser.getBooksList(filePath);
		} catch (final Exception e) {
			Logger.error(tag, "unable to parse json:" + e);
			downloadBooksList();
		}
		// Logger.warn(tag,
		// "bookslist size is:"+booksList.getBookList().size());
		if (booksList.getBookList().size() == 0) {
			Toast.makeText(getApplicationContext(), R.string.empty_shelf_alert,
					Toast.LENGTH_LONG).show();
			/*
			 * new
			 * AlertDialog.Builder(this).setTitle("Shelf").setMessage(R.string
			 * .empty_shelf_alert) .show();
			 */
		}
		Logger.warn(tag, "bookslist size is:" + booksList.getBookList().size());
		final List missingBooks = new ArrayList();
		for (int i = 0; i < booksList.getBookList().size(); i++) {
			final Book book = booksList.getBookList().get(i);

			Log.e("Required Books",
					"Filename(" + book.getBookId() + ")" + book.getFilename());
			if (!ApplicationData.isFileExists(appData.getBookFilesPath(book
					.getBookId()) + book.getFilename())) {
				// Logger.warn(tag, "Missing book position " + i);
				missingBooks.add(book.getBookId());

				// removing missing books from listing.. need to delete from the
				// list in db
				// missingBookIds.add(i);
			}
		}
		for (int i = 0; i < missingBooks.size(); i++) {
			booksList.removeBook((Integer) missingBooks.get(i));
		}

		Logger.warn(tag, "Missing book size are:" + missingBookIds.size());
	}

	/*
	 * if (missingBookIds.size() > 0) { sync.setVisibility(View.VISIBLE); }
	 */

	/**
	 * The Class ShelvesScrollManager.
	 */
	public class ShelvesScrollManager implements AbsListView.OnScrollListener {

		/** The m previous prefix. */
		private String mPreviousPrefix;

		/** The m popup will show. */
		private boolean mPopupWillShow;

		/** The m show popup. */
		private final Runnable mShowPopup = new Runnable() {
			@Override
			public void run() {
				showPopup();
			}
		};

		/** The m dismiss popup. */
		private final Runnable mDismissPopup = new Runnable() {
			@Override
			public void run() {
				mScrollHandler.removeCallbacks(mShowPopup);
				mPopupWillShow = false;
				dismissPopup();
			}
		};

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AbsListView.OnScrollListener#onScrollStateChanged(
		 * android.widget.AbsListView, int)
		 */
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (mScrollState == SCROLL_STATE_FLING
					&& scrollState != SCROLL_STATE_FLING) {
				final Handler handler = mScrollHandler;
				final Message message = handler.obtainMessage(
						MESSAGE_UPDATE_BOOK_COVERS, ShelfActivity.this);
				handler.removeMessages(MESSAGE_UPDATE_BOOK_COVERS);
				handler.sendMessageDelayed(message, mFingerUp ? 0
						: DELAY_SHOW_BOOK_COVERS);
				mPendingCoversUpdate = true;
			} else if (scrollState == SCROLL_STATE_FLING) {
				mPendingCoversUpdate = false;
				mScrollHandler.removeMessages(MESSAGE_UPDATE_BOOK_COVERS);
			}

			if (scrollState == SCROLL_STATE_IDLE) {
				mScrollHandler.removeCallbacks(mShowPopup);

				view.getChildCount();
			}
			mScrollState = scrollState;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AbsListView.OnScrollListener#onScroll(android.widget
		 * .AbsListView, int, int, int)
		 */
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			if (mScrollState != SCROLL_STATE_FLING)
				return;

			final int count = view.getChildCount();
			if (count == 0)
				return;

			final Handler scrollHandler = mScrollHandler;
			final Runnable dismissPopup = mDismissPopup;
			scrollHandler.removeCallbacks(dismissPopup);
			scrollHandler.postDelayed(dismissPopup, WINDOW_DISMISS_DELAY);
		}

	}

	/**
	 * The Class ScrollHandler.
	 */
	private static class ScrollHandler extends Handler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_UPDATE_BOOK_COVERS:
				((ShelfActivity) msg.obj).updateBookCovers();
				break;
			}
		}
	}

	/**
	 * Show popup.
	 */
	private void showPopup() {
		if (mPopup == null) {
			final PopupWindow p = new PopupWindow(this);
			p.setFocusable(false);
			p.setContentView(mGridPosition);
			p.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
			p.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
			p.setBackgroundDrawable(null);

			// p.setAnimationStyle(R.style.PopupAnimation);

			mPopup = p;
		}

		if (mGrid.getWindowVisibility() == View.VISIBLE) {
			mPopup.showAtLocation(mGrid, Gravity.CENTER, 0, 0);
		}
	}

	/**
	 * Dismiss popup.
	 */
	private void dismissPopup() {
		if (mPopup != null) {
			mPopup.dismiss();
		}
	}

	/**
	 * Update book covers.
	 */
	private void updateBookCovers() {
		mPendingCoversUpdate = false;
		// Logger.info("ShelfActivity", "UpdateBookCovers");
		final ShelvesView grid = mGrid;
		final int count = grid.getChildCount();

		for (int i = 0; i < count; i++) {
			grid.getChildAt(i);
		}

		grid.invalidate();
	}

	/**
	 * The Class FingerTracker.
	 */
	private class FingerTracker implements View.OnTouchListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
		 * android.view.MotionEvent)
		 */
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// Log.e("ShelfActivtiy", "in finger tracker");
			final int action = event.getAction();
			mFingerUp = action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL;
			if (mFingerUp
					&& mScrollState != OnScrollListener.SCROLL_STATE_FLING) {
				// Log.e("ShelvesActivtiy", "in finger tracker IF");
				postUpdateBookCovers();
			}
			return false;
		}
	}

	/**
	 * Post update book covers.
	 */
	private void postUpdateBookCovers() {
		// Log.i("ShelvesActivity", "in postUpdateBookCovers");
		final Handler handler = mScrollHandler;
		final Message message = handler.obtainMessage(
				MESSAGE_UPDATE_BOOK_COVERS, ShelfActivity.this);
		// Log.i("ShelvesActivity", "in postUpdate messsage is:" + message);
		handler.removeMessages(MESSAGE_UPDATE_BOOK_COVERS);
		mPendingCoversUpdate = true;
		handler.sendMessage(message);
	}

	/**
	 * The Class SelectionTracker.
	 */
	private class SelectionTracker implements
			AdapterView.OnItemSelectedListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
		 * android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			// Logger.error("ShelfActivity", "SelectionTracker");
			if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) {
				Log.e("ShelfActivity", "In if");
				mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
				postUpdateBookCovers();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
		 * (android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}

	/**
	 * The Class BookViewer.
	 */
	private class BookViewer implements AdapterView.OnItemClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemClick(AdapterView<?> view, View arg1,
				final int position, long id) {

			// szaveri - disabling book opening activity
			openBook(position);
		}
	}

	/**
	 * Open book.
	 * 
	 * @param position
	 *            the position
	 */
	private void openBook(final int position) {
		final Book book = booksList.getBookList().get(position);
		Logger.warn(tag, "CLICK - delete flag " + deleteFlag);
		boolean onClick_exam = false;
		if (null != examId && !examId.equalsIgnoreCase("")) {
			getExamDetails();
			onClick_exam = false;
			for (int i = 0; i < exam.getOpenBooks().size(); i++) {
				Logger.info(tag,
						"OPEN BOOK - Click event is:" + book.getBookId()
								+ " == " + exam.getOpenBooks().get(i));
				if (book.getBookId() == Integer.parseInt(exam.getOpenBooks()
						.get(i).replace("[\"", "").replace("\"]", ""))) {
					onClick_exam = true;
					break;
				}
			}
			Logger.warn(tag, "OPEN BOOK - on click before opening is:"
					+ onClick_exam);
		}
		// if (!deleteFlag || onClick_exam ) {
		if (onClick_exam || !openBookExam) {

			/**
			 * Notifying the user if he is trying to open a book that is not in
			 * local
			 */
			if (ApplicationData.isFileExists(appData
					.getCompleteBookFilePath(book))) {
				// Logger.warn(tag, "CLICK - file Exists ");
				Toast.makeText(this, "It will take few seconds to open the book..", Toast.LENGTH_LONG).show();
				Logger.warn(tag, "It will take few seconds to open the book..");
				Handler hanlder = new Handler();
				final Intent intent = new Intent();
				hanlder.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						intent.setClass(getApplicationContext(), EpubReaderActivity.class);
						appData.setBook(book);
						intent.putExtra("book_id", book.getBookId());
						intent.putExtra("bookfilename", book.getFilename());
						intent.putExtra(VegaConstants.EXAM_ID, examId);
						intent.putExtra("open_book", openBookExam);
						intent.putExtra(VegaConstants.EXAM_ID, examId);
						Logger.error(tag, "Book name is:" + book.getFilename());
						Logger.error(tag, "Book id is:" + book.getBookId());

						if (book.getFilename().toLowerCase().endsWith(".pdf")) {
							new File(appData.getBookFilesPath(book.getBookId())
									+ book.getFilename());

						} else if (book.getFilename().toLowerCase().endsWith(".epub")) {
							book.getMetaData().getBookFormat().getFormat();

							// Log.w("From Shelf Activity",
							// "Starting Readbook Activity");
							intent.putExtra("SHELF_To_READBOOK", "SHELF_To_READBOOK");
							startActivity(intent);
							// finish();
						} else if (book.getFilename().toLowerCase().endsWith(".html")) {
							new File(appData.getBookFilesPath(book.getBookId())
									+ book.getFilename());

						} else {
							new AlertDialog.Builder(shelfContext)
									.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle(R.string.XMlBookTypeTitle)
									.setMessage(R.string.versionSupport)
									.setPositiveButton("OK", null).show();
						}
					}
				}, 20);
			} else {
				Logger.warn(tag, "CLICK - File doesnot exixts");
				new AlertDialog.Builder(shelfContext)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage(R.string.book_not_downloaded)
						.setPositiveButton("OK", null).show();
			}
		} else {
			if (!onClick_exam) {
				gridLayout.setBackgroundColor(color.transparent);
				new AlertDialog.Builder(shelfContext)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage(
								"Oops!! You cannot open this book as this book is not allowed to be opened for "
										+ exam.getExamDetails().getSubject()
										+ "-"
										+ exam.getExamDetails().getTitle()
										+ " . "
										+ "You can open this book once you finish the exam.")
						.setPositiveButton("ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Logger.warn(tag, "ok clicked");
										gridAdapter = new ImageAdapter(
												getApplicationContext());
										mGrid.setAdapter(gridAdapter);
									}
								}).show();
			}
			// getCurrentExam
			/*
			 * }else if (onClick) { new AlertDialog.Builder(shelfContext)
			 * .setIcon(android.R.drawable.ic_dialog_alert)
			 * .setTitle(R.string.deleteBookTitle)
			 * .setMessage(R.string.confirmationMessage)
			 * .setPositiveButton(R.string.yes, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { deleteBook(book.getBookId()); } })
			 * .setNegativeButton(R.string.no, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { deleteFlag = false; shelfButton.setAlpha(255); } }).show(); }
			 * else {
			 * 
			 * Toast.makeText(activityContext,
			 * R.string.network_connection_unreachable,
			 * toastDisplayTime).show();
			 * 
			 * }
			 */

		}
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pearl.activities.BaseActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		serverRequests.close();
		System.runFinalizersOnExit(true);
	}

	/**
	 * The Class BooksAdapter.
	 */
	private class BooksAdapter extends ArrayAdapter<Book> {

		/** The items. */
		private final List<Book> items;

		/**
		 * Instantiates a new books adapter.
		 * 
		 * @param context
		 *            the context
		 * @param textViewResourceId
		 *            the text view resource id
		 * @param book
		 *            the book
		 */
		public BooksAdapter(Context context, int textViewResourceId,
				List<Book> book) {
			super(context, textViewResourceId, book);
			items = book;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.shelf_list_view_row, null);
			}
			final ImageView listCoverImage = (ImageView) v
					.findViewById(R.id.list_cover_image);
			final TextView bookTitleList = (TextView) v
					.findViewById(R.id.list_book_title);

			final Book book = items.get(position);
			setCoverImage(book.getCoverpage().getWebPath().trim(),
					listCoverImage, book.getBookId());

			boolean setAlpha_Exam;
			Logger.warn(tag, "OPEN BOOK - exam id is:" + examId);
			if (null != examId && !examId.equalsIgnoreCase("")) {
				// TODO set the alpha of the delete to restrict book deletion
				// while in exam
				setAlpha_Exam = true;
				getExamDetails();
				Logger.warn(tag, "OPEN BOOK - booksList size in view is"
						+ exam.getOpenBooks().size());
				for (int j = 0; j < exam.getOpenBooks().size(); j++) {
					Logger.info(tag, "OPEN BOOK - book from appdata is:"
							+ exam.getOpenBooks().get(j));
					Logger.warn(
							tag,
							"OPEN BOOK - book id in view is:"
									+ book.getBookId());
					if (book.getBookId() == Integer.parseInt(exam
							.getOpenBooks().get(j).replace("[\"", "")
							.replace("\"]", ""))) {
						setAlpha_Exam = false;
						break;
					}
				}
				Logger.warn(tag, "OPEN BOOK - alpha of book before setting is:"
						+ setAlpha_Exam);
				if (setAlpha_Exam) {
					listCoverImage.setAlpha(70);
				}
			}
			listCoverImage.setMaxHeight(110);
			listCoverImage.setMaxWidth(110);
			bookTitleList.setText(book.getMetaData().getTitle());
			return v;
		}
	}

	/**
	 * Download books list.
	 */
	private void downloadBooksList() {

		// Logger.info("ShelfActivity->Populatebookslist",
		// "In else - Downloading books list");

		booksList = new BookList();

		// Fetching the books list freshly from server
		// Toast.makeText(activityContext, R.string.loadingPage,
		// toastDisplayTime)
		// .show();

		final DownloadType downloadType = new DownloadType();
		downloadType.setType(DownloadType.BOOK_LIST);
		final String url = serverRequests.getRequestURL(
				ServerRequests.DOWNLOADED_BOOKSLIST, "" + appData.getUserId());
		// Logger.warn(tag, "URL for downloaded books is:" + url);
		final Download download = new Download(downloadType, url,
				appData.getBooksListsPath(),
				ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME);

		final DownloadManager booklistDownloadManager = new DownloadManager(
				appData, download);
		booklistDownloadManager.startDownload(new DownloadCallback() {
			@Override
			public void onFailure(String failureMessage) {
			}

			@Override
			public void onProgressUpdate(int progressPercentage) {
			}

			@Override
			public void onSuccess(String downloadedString) {
				forceRefresh = false;
				// Re-Populate the books list
				runOnUiThread(new Thread() {
					@Override
					public void run() {
						populateBookList();
						gridAdapter.notifyDataSetChanged();
					}
				});
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pearl.activities.BaseActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (null != examId) {
			detachMenuItemFromMenu(menu, HOME_MENUITEM);
			detachMenuItemFromMenu(menu, FEEDBACK_MENUITEM);
			detachMenuItemFromMenu(menu, FAQ_MENUITEM);
			detachMenuItemFromMenu(menu, SIGNOUT_MENUITEM);
			detachMenuItemFromMenu(menu, OPTIONS_MENUITEM);
			attachMenuItemToMenu(menu, BACKTOEXAM_MENUITEM);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
	 */
	@Override
	public void onNetworkAvailable() {

		// Logger.warn(tag, "In onNetworkAvailable()");
		// //Logger.warn(tag, "network available so enable the refresh button");
		onClick = true;
		networkAvailable = true;

		if (!ApplicationData.isFileExists(appData.getDownloadedBooksListXML())) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refreshShelf.setAlpha(225);
					// shelfDelete.setAlpha(225);
				}
			});
		} else {
			// Logger.warn(tag, "file exists in local");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
	 */
	@Override
	public void onNetworkUnAvailable() {

		// Logger.warn(tag, "In onNetworkUnAvailable()");
		// //Logger.warn(tag,
		// "network not available so disable the refresh button");
		onClick = false;
		networkAvailable = false;

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				refreshShelf.setAlpha(70);
				// shelfDelete.setAlpha(70);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pearl.activities.BaseActivity#getActivityType()
	 */
	@Override
	public String getActivityType() {
		// TODO Auto-generated method stub
		return "ShelfActivity";
	}

	/**
	 * The Class asyncTaskUpdateProgress.
	 */
	public class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {

		/** The progress. */
		int progress;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			refreshShelf.setClickable(true);
			refreshShelf.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progress = 0;
			progressBar.setVisibility(View.VISIBLE);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			progressBar.setProgress(values[0]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
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

	/**
	 * Check for book opening error.
	 */
	private void checkForBookOpeningError() {
		Logger.warn(tag, "in checkfor opening book error");
		Logger.warn(tag, "networkavailable status:" + networkAvailable);
		final WifiManager wfManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wfManager.isWifiEnabled()) {
			Logger.warn(tag, "SHELF - networkavailable status true");
			Toast.makeText(
					this,
					"Book is being deleted as it was not downloaded properly..",
					Toast.LENGTH_LONG).show();
			deleteBook(bookIdToDelete);
		} else {
			Toast.makeText(
					this,
					"Sorry.. We are unable to delete the book as you are not connected to internet",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Gets the exam details.
	 * 
	 * @return the exam details
	 */
	private void getExamDetails() {
		final String filePath = appData.getExamFileName(examId);

		Logger.error(tag, "OPENBOOK - filePath is " + filePath);

		// Before getting from server.. check if already exists in local
		if (ApplicationData.isFileExists(filePath)) {
			if (appData.getCurrentExam() != null) {
				exam = appData.getCurrentExam();
			} else {
				try {
					exam = ExamParser.getExam(filePath);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		} else { // If does not exists..
			// TODO Download the file
			Logger.error(tag, "OPENBOOK - Reached else");

			// populateExamDetails(examId); // -- Calling as recursive function
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (openBookExam && keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else

		if (!appData.isLoginStatus()) {
			final Intent i = new Intent(ShelfActivity.this, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Readbook timer.
	 * 
	 * @param HH
	 *            the hh
	 * @param MM
	 *            the mm
	 * @param SS
	 *            the ss
	 */
	public void readbookTimer(int HH, int MM, int SS) {
		if (HH == 00 && MM == 04 && SS == 00 && openBookExam) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					final AlertDialog.Builder _examAlert = new AlertDialog.Builder(
							activityContext);
					_examAlert.setTitle("Hurry up..");
					_examAlert.setIcon(R.drawable.exam_warning);
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
					final AlertDialog dialog = _examAlert.create();
					dialog.show();
				}
			});

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onAttachedToWindow()
	 */
	@Override
	public void onAttachedToWindow() {
		if (examId != null)
			openBookExam = true;
		// Log.i("SHELF ACTIVITY","OPEN BOOK EXAM STATUS is"+exam.isOpenBookExam());
		if (openBookExam) {
			final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
			final KeyguardLock lock = keyguardManager
					.newKeyguardLock(KEYGUARD_SERVICE);
			lock.disableKeyguard();

			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
			super.onAttachedToWindow();

		}
	}
}