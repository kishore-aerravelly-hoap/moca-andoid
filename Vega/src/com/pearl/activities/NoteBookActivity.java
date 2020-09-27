package com.pearl.activities;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.android.ui.HorizontalListView;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.books.NoteBook;
import com.pearl.books.SubjectList;
import com.pearl.books.pages.NoteBookPage;
import com.pearl.database.config.NoteBookConfig;
import com.pearl.database.handlers.NoteBookHandler;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.parsers.json.SubjectsListParser;
import com.pearl.search.NoteBookSearchResult;
import com.pearl.ui.models.RoleType;
import com.pearl.ui.models.Subject;
import com.pearl.util.DateFormat;

// TODO: Auto-generated Javadoc
/**
 * The Class NoteBookActivity.
 */
public class NoteBookActivity extends FragmentActivity implements GestureActionHandler {
	
	/** The v. */
	View v;
	
	/** The note book adapter. */
	NoteBookAdapter noteBookAdapter;
    
    /** The delete. */
    ImageView noteBookSearchIcon, right, left,delete;
    
    /** The notes. */
    EditText notes;
    
    /** The google search. */
    Button googleSearch;
    
    /** The dictionary button. */
    Button dictionaryButton;
    
    /** The auto save. */
    TextView autoSave;
    
    /** The list view date. */
    TextView listViewDate;
    
    /** The list desc. */
    TextView listDesc;
    
    /** The search list desc. */
    TextView searchListDesc;
    
    /** The subject_name. */
    TextView subject_name;
    
    /** The add page. */
    Button addPage;
    
    /** The menu button. */
    Button menuButton;
    
    /** The previous. */
    Button next,previous;
    
    /** The note book list. */
    ListView noteBookList;
    
    /** The search result list. */
    ListView searchResultList;
    
    /** The notes search block. */
    LinearLayout notesSearchBlock;
    
    /** The note book search. */
    SearchView noteBookSearch;
    
    /** The progress dialog. */
    ProgressDialog progressDialog;
    
    /** The help. */
    ImageView help;
    
    /** The notes layout. */
    RelativeLayout notesLayout;
    
    /** The notebook_refresh. */
    ImageView notebook_refresh;
    
    /** The notes text watcher. */
    private TextWatcher notesTextWatcher;
    
    /** The Subject_description. */
    private TextView Subject_description;
    
    /** The subjects list view. */
    HorizontalListView subjectsListView;
    
    /** The tag. */
    private String tag=getClass().getSimpleName();
    
    /** The notes text. */
    private String notesText = "";
    
    /** The nn. */
    private String date, nn;
    
    /** The db date. */
    private String dbDate;
    
    /** The status. */
    private String status = "active";
    
    /** The subject. */
    private String subject = "";
    
    /** The user time format. */
    private String userTimeFormat;
    
    /** The search term on textchange. */
    private String searchTermOnTextchange;
    
    /** The search term. */
    private String searchTerm;
    
    /** The auto save time. */
    long autoSaveTime;
    
    /** The i. */
    int i = 0;
    
    /** The formatted time. */
    private String formattedTime = null;
    
    /** The bundle. */
    private Bundle bundle;
    
    /** The user id. */
    private String userId;
    
    /** The notes id. */
    private int notesId;
    
    /** The notes cursor1. */
    private Cursor notesCursor1;
    
    /** The note book config. */
    private NoteBookConfig noteBookConfig;
    
    /** The vega config. */
    private VegaConfiguration vegaConfig;
    
    /** The note book search result. */
    private NoteBookSearchResult noteBookSearchResult;
    
    /** The notes list. */
    private List<NoteBook> notesList = new ArrayList<NoteBook>();
    
    /** The search list. */
    private List<NoteBookPage> searchList = new ArrayList<NoteBookPage>();
    
    /** The Constant NOTEBOOK. */
    private static final String NOTEBOOK = "NOTEBOOK";
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The activity. */
    private Context activity;
    
    /** The subject list. */
    private SubjectList subjectList;
    
    /** The str. */
    private String str;
    
    /** The ctx. */
    private Context ctx;
    
    /** The h. */
    public static Handler h;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The loading dialog. */
    private ProgressDialog loadingDialog; 
    
    /** The handler. */
    private Handler handler;
    
    /** The idnumber. */
    private int idnumber = 0;
    
    /** The sbj. */
    private String sbj = "";
    
    /** The clip board. */
    private ClipboardManager clipBoard;
    
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_book);

		serverRequests = new ServerRequests(this);

		bundle = this.getIntent().getExtras();

		noteBookConfig = new NoteBookConfig(this);
		vegaConfig = new VegaConfiguration(this);
		appData = (ApplicationData) getApplication();

		addPage = (Button) findViewById(R.id.add_notes);
		delete = (ImageView) findViewById(R.id.del_notes);
		notes = (EditText) findViewById(R.id.noteBookText);
		next = (Button) findViewById(R.id.nxt);
        previous = (Button) findViewById(R.id.prev);
		
		noteBookList = (ListView) findViewById(R.id.noteBookList);
		subjectsListView = (HorizontalListView) findViewById(R.id.Subject_selection_Horizontal_list);

		googleSearch = (Button) findViewById(R.id.notesGoogleSearchButton);
		dictionaryButton = (Button) findViewById(R.id.notesDictionaryButton);

		searchResultList = (ListView) findViewById(R.id.noteBookSearchResults);

		noteBookSearch = (SearchView) findViewById(R.id.noteBookSearch);
		Subject_description = (TextView) findViewById(R.id.Subject_description);
		handler=new Handler(); 
				
		help = (ImageView) findViewById(R.id.notebook_help);
		noteBookSearch.setSubmitButtonEnabled(true);

		clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		
		searchTerm = "";
		ctx = this;
		noteBookSearchResult = new NoteBookSearchResult();
		subject_name = (TextView) findViewById(R.id.Subject_name);
		activity = getApplicationContext();
		notebook_refresh = (ImageView) findViewById(R.id.notebook_refresh);
		menuButton = (Button) findViewById(R.id.NoteBook_menuButton);
		loadingDialog = new ProgressDialog(activity);
		loadingDialog.setMessage("Loading..");
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(false);
		bindEvents();
		deleteEmptyNotes();
		checkIfSubjectExistsInDB();
		notes.addTextChangedListener(notesTextWatcher);
		bundle = this.getIntent().getExtras();

		if (bundle != null) {
			notesId = bundle.getInt("NoteBookId");
		}
		userId = appData.getUserId();
		try {
			userTimeFormat = vegaConfig.getValue(VegaConstants.DATE_FORMAT);
		} catch (InvalidConfigAttributeException e) {
			Logger.info(tag, e.toString());
		}
		SimpleDateFormat currentTime = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zz yyyy");
		date = currentTime.format(new Date());
		if (ApplicationData.isFileExists(appData.getSubjectsListFile())) {
			parseSubjectsList();
			if (subjectList != null && subjectList.getSubjectList() != null
					&& subjectList.getSubjectList().size() != 0) {
				NoteBookActivity.this.subject = subjectList.getSubjectList()
						.get(0).getSubjectName();
				
				subject_name.setText(" "+subject);
			}
		} else {
			downloadSubjectsList();
		}
		getNotesFromDatabase();
		appData.setActivityName(tag);
		
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
	}
	
	/**
	 * Bind events.
	 */
	private void bindEvents(){
		
		previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(idnumber > 0 && idnumber <= notesList.size()){	
				if(idnumber >= notesList.size())
				idnumber = notesList.size()-1;
				idnumber--;
				deleteEmptyNotes();
				noteBookList.setItemChecked(idnumber, true);
				
				openNoteBook(idnumber);
				highlightClickedItem(idnumber);
				} else {
					Toast.makeText(getBaseContext(), "No more books to open", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(idnumber < 0)
				idnumber = -1;
			if(idnumber < notesList.size()-1 && idnumber >= -1){
				idnumber++;
				deleteEmptyNotes();
				noteBookList.setItemChecked(idnumber, true);
				
				openNoteBook(idnumber);
				highlightClickedItem(idnumber);
			}else {
					Toast.makeText(getBaseContext(), "No more books to open", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		notesTextWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				sbj = subject;
				String n = notes.getText().toString().trim();
				if (n.length() != 0 && n!= "" && notesId != 0 ) {
					addPage.setEnabled(true);
					notesCursor1 = noteBookConfig.fetchNotes(notesId);
					startManagingCursor(notesCursor1);
					if (notesCursor1.moveToFirst()) {
						do {
							dbDate = notesCursor1.getString(notesCursor1
									.getColumnIndexOrThrow(NoteBookHandler.DATE));
							if (!notesCursor1
									.getString(
											notesCursor1
													.getColumnIndexOrThrow(NoteBookHandler.NOTES_TEXT))
									.equalsIgnoreCase(n)) {
								updateNotes(n, notesId, getCurrentTime());
							}
						} while (notesCursor1.moveToNext());
					}
					getNotesFromDatabase();
				} else if (n.trim().length() != 0) {
					notesId = insertNotes(n);
					getNotesFromDatabase();
				} 
				
				if(notes.getText().toString().trim().length() != 0){
				    if(clipBoard!=null &&  clipBoard.getText()!=null && clipBoard.getText().toString()!=null) {
					nn = clipBoard.getText().toString();
					
				for(int j = 0; j<notesList.size(); j++){
					NoteBook noteBook = notesList.get(j);
					if(notesId == noteBook.getId()){
						highlightClickedItem(j);
						delete.setVisibility(View.VISIBLE);
						break;
					}
				  }
					}
				}
				if(nn != null && nn.trim() != "" && nn.trim().equals(clipBoard.getText().toString().trim())
		                            && notes.getText().toString().trim().length() == 0){
	if (notesId != 0 && notes.getText().toString().trim().length() == 0){
		delete(notesId);
		notesId = 0;
		getNotesFromDatabase();

	}
}
				
			}
		};
		
		clipBoard.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {
			
			@Override
			public void onPrimaryClipChanged() {
				// TODO Auto-generated method stub
				if (notesId != 0 && notes.getText().toString().trim().length() == 0){
					delete(notesId);
					notesId = 0;
					getNotesFromDatabase();
				}
			    Toast.makeText(getApplicationContext(), clipBoard.getText().toString(), Toast.LENGTH_LONG).show();
			}
		});
		
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(NoteBookActivity.this);
				alertDialog.setTitle("Delete");
				alertDialog.setIcon(R.drawable.warning_f);
				alertDialog.setMessage("Are you sure you want to Delete this notes?");
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
				notes.setText("");
				if (notes.getText().length() == 0 && notesId != 0) {
					delete(notesId);
					notesId = 0;
					getNotesFromDatabase();
					if(!searchList.isEmpty()){
                        	search(str);					
					}
				}
				if(notesList.size() != 0){
				if(idnumber >= notesList.size()-1){
					
					highlightClickedItem(0);
					openNoteBook(0);
				}else{
					
					highlightClickedItem(idnumber);
					openNoteBook(idnumber);
				}
			  }
			}
		});
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				alertDialog.show();
			}
		});
		
		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent newActivity = new Intent(getApplicationContext(),
						ActionBar.class);
				startActivity(newActivity);
			}
		});

		addPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addPage.setEnabled(false);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInputFromWindow(addPage.getWindowToken(), 0, 0);
				
				nn = "";
				notes.setText("");
				getNotesFromDatabase();
				notesId = insertNotes("");
				idnumber = 0;
				noteBookList.refreshDrawableState();
			}
		});

		noteBookSearch.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(
							noteBookSearch.getWindowToken(), 0);
				}
				searchTerm=query;
				if(searchTerm.trim().length() == 0){
					searchResultList.setVisibility(View.GONE);
				} else {
					search(searchTerm.trim());
					searchResultList.setVisibility(View.VISIBLE);
				}
				
				return false;
			}

			@Override
			public boolean onQueryTextChange(String queryString) {
				searchTermOnTextchange = queryString;
				str = queryString; 
				if (queryString.length() <= 2) {
					googleSearch.setVisibility(View.INVISIBLE);
					dictionaryButton.setVisibility(View.INVISIBLE);
					
				} else {
					googleSearch.setVisibility(View.VISIBLE);
					dictionaryButton.setVisibility(View.VISIBLE);
				}
				searchTerm=queryString;
				if(searchTerm.trim().length() == 0){
					searchResultList.setVisibility(View.GONE);
				} else {
					search(searchTerm.trim());
					searchResultList.setVisibility(View.VISIBLE);
				}
				
				return false;
			}
		});
		
		dictionaryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(noteBookSearch.getQuery().toString().trim().length()==0) {
					noteBookSearch.setQueryHint("Enter keyw");
				}
				loadingDialog.show();

				String url = "http://api-pub.dictionary.com/v001?vid=lw8mk3hzte4nw6mtvtsnp0rnh9ivu55f1d2lhkkbyp&q="
						+ URLEncoder.encode(searchTermOnTextchange)
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
								loadingDialog.hide();

								Intent intent = new Intent();
								intent.setClass(getApplicationContext(),
										DictionaryDialogActivity.class);
								intent.putExtra("SearchString", searchTerm);
								startActivity(intent);
							}

							@Override
							public void onProgressUpdate(int progressPercentage) {
							}

							@Override
							public void onFailure(String failureMessage) {
								loadingDialog.hide();
								Toast.makeText(activity,
										R.string.Unable_to_reach_pearl_server,
										Toast.LENGTH_LONG).show();
							}
						});
			}
		});
		
		googleSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "http://www.google.co.in/search?q="
						+ URLEncoder.encode(searchTerm);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		

		notebook_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (AppStatus.getInstance(ctx).isOnline(ctx)) {

					try {
						downloadSubjectsList();
						checkIfSubjectExistsInDB();
					} catch (Exception e) {
						Logger.error(tag, e);
					}
				} else {
					Toast.makeText(activity,
							R.string.network_connection_unreachable,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});

		/*notes.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DEL ) {
					if (notesId != 0 && notes.getText().toString().trim().length() == 0){
						delete(notesId);
						notesId = 0;
						getNotesFromDatabase();
					}
				}
				return false;
			}
		});*/
		
		/*next.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			List<NoteBook> notesList=new LinkedList<NoteBook>();
			notesList = noteBookConfig.fetchNext(notesId, subject, date);
			for(Iterator<NoteBook> ite=notesList.iterator();ite.hasNext();)
			{
				
				notes.setText(notesList.get(0).getNotesDescription()
						.toString());
				notes.setSelection(notes.getText().length());
				notesId = notesList.get(0).getId();
				date = notesList.get(0).getDate().toString();
			}
			}
			
	});*/
	/*saveNotes.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
		notesText = notes.getText().toString();
		if (notesText.length() != 0 || notesText.trim().length() != 0) {
			updateNotes(notesText, notesId, getCurrentTime());
			getNotesFromDatabase();
		}
	}
});*/
	
	}
	
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
			final List<String> list = HelpParser.getHelpContent("notebook.txt", this);
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
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_DEL ) {
		if (notesId != 0 && notes.getText().toString().trim().length() == 0){
			delete(notesId);
			notesId = 0;
			getNotesFromDatabase();
		}
	    return true;}
	    else
	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Highlight clicked item.
	 *
	 * @param pos the pos
	 */
	private void highlightClickedItem(final int pos){
		noteBookList.setItemChecked(pos, true);
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<noteBookList.getChildCount();i++) {
				    View v1=noteBookList.getChildAt(i);
				    if(i==pos) {
						v1.setBackgroundColor(Color.parseColor("#dfdc8d"));
						idnumber = i;
				    }else {
					    v1.setBackgroundColor(Color.parseColor("#c2bd36"));
				    }
				}
			}
		});
	}
	
	/**
	 * Open note book.
	 *
	 * @param positionn the positionn
	 */
	private void openNoteBook(int positionn){
		
		NoteBook noteBook = notesList.get(positionn);
		notesId = noteBook.getId();
		try {
			vegaConfig.setValue(VegaConstants.HISTORY_NOTEBOOK_ID,
					notesId);
		} catch (ColumnDoesNoteExistsException e) {
			Logger.info(tag, e.toString());
		}
		notesCursor1 = noteBookConfig.fetchNotes(notesId);
		startManagingCursor(notesCursor1);
		if (notesCursor1.moveToFirst()) {
			do {
				notes.setText(notesCursor1.getString(notesCursor1
						.getColumnIndexOrThrow(NoteBookHandler.NOTES_TEXT)));
				notes.setSelection(notes.getText().length());
			} while (notesCursor1.moveToNext());
		}
		dbDate = noteBook.getDate().toString();
		date = dbDate;
	}
	
	/**
	 * Check if subject exists in db.
	 */
	private void checkIfSubjectExistsInDB() {
		try {
			if (vegaConfig.getValue(VegaConstants.HISTORY_SUBJECT)
					.equalsIgnoreCase("null")) {
				subject_name.setText(" "+subject);
			} else {
				try {
					subject_name.setText(" "+vegaConfig
							.getValue(VegaConstants.HISTORY_SUBJECT));
					subject = vegaConfig
							.getValue(VegaConstants.HISTORY_SUBJECT);
				} catch (InvalidConfigAttributeException e) {
					e.printStackTrace();
				}
			}
		} catch (InvalidConfigAttributeException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * View d elete.
	 */
	private void viewDElete(){
	    if(notesList.isEmpty()){
		    delete.setVisibility(View.INVISIBLE);
		    next.setVisibility(View.GONE);
		    previous.setVisibility(View.GONE);
	    }else{
	    	delete.setVisibility(View.INVISIBLE);
	      	next.setVisibility(View.VISIBLE);
			previous.setVisibility(View.VISIBLE);
	    }
	    }
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	public void onResume() {

		super.onResume();
		if (!appData.isLoginStatus()) {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
			finish();
		}
		
		noteBookConfig = new NoteBookConfig(this);
		getNotesFromDatabase();
		for(int l=0; l<noteBookList.getChildCount(); l++){
		noteBookList.getChildAt(l);
		
		}
		if(!notesList.isEmpty()){
			deleteEmptyNotes();
			
			highlightClickedItem(idnumber);
			openNoteBook(idnumber);
		}
	}

	/**
	 * Insert notes.
	 *
	 * @param notesText the notes text
	 * @return the int
	 */
	private int insertNotes(String notesText) {
		this.notesText = notesText;
		notesId = noteBookConfig.insertNotes(userId + "", subject,
			notesText, status, date);
		return notesId;
	}

	/**
	 * Update notes.
	 *
	 * @param notesText the notes text
	 * @param id the id
	 * @param date the date
	 */
	private void updateNotes(String notesText, int id, String date) {
		this.notesText = notesText;
		try {
			noteBookConfig.update(id, notesText, date);
		} catch (ColumnDoesNoteExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
     * Delete.
     *
     * @param notesId the notes id
     */
    private void delete(int notesId) {
    	noteBookConfig.delete(notesId);
    }
    
    /**
     * Delete empty notes.
     */
    private void deleteEmptyNotes(){
    	String noteeee = null;
    	getNotesFromDatabase();
    	if(notesList.size() != 0){
    	for(int i = 0; i <= notesList.size()-1; i++){
    		NoteBook noteBook = notesList.get(i);
			notesId = noteBook.getId();
			notesCursor1 = noteBookConfig.fetchNotes(notesId);
			startManagingCursor(notesCursor1);
			if (notesCursor1.moveToFirst()) {
				do {
					 noteeee = notesCursor1.getString(notesCursor1
							.getColumnIndexOrThrow(NoteBookHandler.NOTES_TEXT));
				} while (notesCursor1.moveToNext());
			}
            if(noteeee == null || noteeee.trim().length() == 0)
            	delete(notesId);
                getNotesFromDatabase();
    	}
    	}
    }
    
    /**
     * Gets the notes from database.
     *
     * @return the notes from database
     */
    private int getNotesFromDatabase() {
		int notesListsize = 0;
		notesList = noteBookConfig.fetchNotesForSubject(subject);
		notesListsize = notesList.size();
		viewDElete();
		Logger.info(tag, "NOTEBOOK - notes list form DB" + notesList.size()
				+ " for subject " + subject + " is:" + notesList.size());

		Collections.sort(notesList, new NoteBook());
		noteBookAdapter = new NoteBookAdapter(this,
				android.R.layout.simple_selectable_list_item, notesList);
		noteBookList.setAdapter(noteBookAdapter);
		for(int l=0; l<noteBookList.getChildCount(); l++){
			v = noteBookList.getChildAt(l);
		}
		noteBookAdapter.notifyDataSetChanged();
		
		noteBookList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, final int position,
					long id) {
				deleteEmptyNotes();
				
				highlightClickedItem(position);
                openNoteBook(position);  
			}
		});
		return notesListsize;
	}
    
    /**
     * The Class NoteBookAdapter.
     */
    private class NoteBookAdapter extends ArrayAdapter<NoteBook> {
		
		/** The items. */
		private List<NoteBook> items;

		/**
		 * Instantiates a new note book adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param notes the notes
		 */
		public NoteBookAdapter(Context context, int textViewResourceId,
				List<NoteBook> notes) {
			super(context, textViewResourceId, notes);
			this.items = notes;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return position;
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
				v = vi.inflate(R.layout.note_book_list_row, null);
			}
			listDesc = (TextView) v.findViewById(R.id.noteBookListText);
			listViewDate = (TextView) v.findViewById(R.id.noteBookDate);
			NoteBook noteBook = items.get(position);
			if (noteBook.getNotesDescription().length() > 20) {
				listDesc.setText(noteBook.getNotesDescription()
						.substring(0, 12) + "...");
			} else {
				listDesc.setText(noteBook.getNotesDescription());
			}
			dbDate = noteBook.getDate().toString();
			listViewDate.setText(DateFormat.getFormattedTime(dbDate,
					userTimeFormat, NOTEBOOK));
			return v;
		}
	}

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
	public void onPause() {
		super.onPause();
		try {
			vegaConfig.setValue(VegaConstants.HISTORY_SUBJECT, sbj);
		} catch (ColumnDoesNoteExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	private String getCurrentTime() {
		SimpleDateFormat currentTime = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zz yyyy");
		return currentTime.format(new Date());
	}

	/**
	 * Search.
	 *
	 * @param searchString the search string
	 */
	private void search(String searchString) {
		searchList.clear();
		searchList = noteBookConfig.fetchSearchString(searchString, subject);
		progressDialog = ProgressDialog.show(this, "", "Searching.. for '"
				+ searchTerm + "'", true);
		SearchResultAdapter searchResultAdapter = new SearchResultAdapter(
				this, android.R.layout.simple_selectable_list_item,
				searchList);
		searchResultList.setAdapter(searchResultAdapter);
		searchResultAdapter.notifyDataSetChanged();
		if (searchList.size() != 0) {
			searchResultList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					deleteEmptyNotes();
					NoteBookPage noteBookSearch = searchList.get(position);
					notesId = noteBookSearch.getId();
					notesCursor1 = noteBookConfig.fetchNotes(notesId);
					startManagingCursor(notesCursor1);
					if (notesCursor1.moveToFirst()) {
						do {
							List<Integer[]> searchIndexlist = noteBookSearchResult.getSearchTermIndex(
									notesCursor1
											.getString(notesCursor1
													.getColumnIndexOrThrow(NoteBookHandler.NOTES_TEXT)),
									searchTerm);
							
							notes.setText(notesCursor1.getString(notesCursor1
									.getColumnIndexOrThrow(NoteBookHandler.NOTES_TEXT)));
							notes.setSelection(notes.getText().length());
							Spannable str = notes.getText();
							for (int i = 0; i < searchIndexlist.size(); i++) {
								int startIndex = searchIndexlist.get(i)[0];
								int endIndex = searchIndexlist.get(i)[1];
								str.setSpan(
										new BackgroundColorSpan(Color
												.parseColor("#8fbc8f")),
										startIndex, endIndex,
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								str.setSpan(new StyleSpan(
										android.graphics.Typeface.ITALIC),
										startIndex, endIndex,
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}

						} while (notesCursor1.moveToNext());
					}
					for(int j = 0; j<notesList.size(); j++){
						NoteBook noteBook = notesList.get(j);
						if(notesId == noteBook.getId()){
							
							highlightClickedItem(j);
							break;
						}
					}
				}
			});
		} else {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Search Results for '" + searchTerm + "'")
					.setMessage("No results found with given search keyword")
					.setPositiveButton("OK", null).show();
		}
		progressDialog.hide();
	}
	
	/**
	 * The Class SearchResultAdapter.
	 */
	private class SearchResultAdapter extends ArrayAdapter<NoteBookPage> {
		
		/** The items. */
		private List<NoteBookPage> items;

		/**
		 * Instantiates a new search result adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param searchNotes the search notes
		 */
		public SearchResultAdapter(Context context, int textViewResourceId,
				List<NoteBookPage> searchNotes) {
			super(context, textViewResourceId, searchNotes);
			for (int i = 0; i < searchNotes.size(); i++)
				this.items = searchNotes;
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
				v = vi.inflate(R.layout.notebook_search_list_row, null);
			}
			searchListDesc = (TextView) v
					.findViewById(R.id.notebook_search_result_description);
			NoteBookPage noteBookResult = items.get(position);
			if (noteBookResult != null) {
				String desc = noteBookResult.getNotes().toString() + "...";
				searchListDesc.setText(Html.fromHtml(desc.replace(searchTerm,
						"<i><b>" + searchTerm + "</b></i>")));
			}
			return v;
		}
	}

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToNextPage()
     */
    @Override
	public void moveToNextPage() {
		List<NoteBook> swipeNotesList = new ArrayList<NoteBook>();
		swipeNotesList = noteBookConfig.fetchNext(notesId, subject, date);
		for (int i = 0; i < swipeNotesList.size(); i++)
		Collections.sort(swipeNotesList, new NoteBook());
		for (int i = 0; i < swipeNotesList.size(); i++)
		if (swipeNotesList.size() != 0) {
			notes.setText(swipeNotesList.get(0).getNotesDescription()
					.toString());
			notes.setSelection(notes.getText().length());
			notesId = swipeNotesList.get(0).getId();
			date = swipeNotesList.get(0).getDate().toString();
		} else {
			Toast.makeText(this, R.string.there_are_no_notes_for_this_Subject,
					Toast.LENGTH_SHORT).show();
		}
	}

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToPreviousPage()
     */
    @Override
	public void moveToPreviousPage() {
		List<NoteBook> swipeNotesList = noteBookConfig.fetchPrevious(notesId,
				subject, date);
		for (int i = 0; i < swipeNotesList.size(); i++)
		Collections.sort(swipeNotesList, new NoteBook());
		for (int i = 0; i < swipeNotesList.size(); i++)
		if (swipeNotesList.size() != 0) {
			notes.setText(swipeNotesList.get(0).getNotesDescription()
					.toString());
			notes.setSelection(notes.getText().length());
			notesId = swipeNotesList.get(0).getId();
			date = swipeNotesList.get(0).getDate().toString();
		} else {
			Toast.makeText(activity,
					R.string.there_are_no_notes_for_this_Subject,
					Toast.LENGTH_SHORT).show();
		}
	}

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#scroll()
     */
    @Override
    public void scroll() {
    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleHeaderFooterVisibility()
     */
    @Override
	public void toggleHeaderFooterVisibility() {
		// notes.setFocusableInTouchMode(true);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(notes, InputMethodManager.SHOW_FORCED);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.SHOW_FORCED);
		notes.requestFocus();
		notes.setSelected(true);
	}

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#onJustTouch()
     */
    @Override
    public void onJustTouch() {

    }


	/**
	 * Inflate subjects list.
	 *
	 * @param subjectList the subject list
	 */
	public void inflateSubjectsList(final List<Subject> subjectList) {
		final subjectListArrayAdapter subjectsAdapter = new subjectListArrayAdapter(
				activity, android.R.layout.simple_selectable_list_item,
				subjectList);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				subjectsListView.setAdapter(subjectsAdapter);
				int k = 0;
				for (Subject subjects : subjectList) {
					if(subject.equalsIgnoreCase(subjects.getSubjectName())){
						break;
					}else{
						k++;
					}
				}
				highlightClickedSubject(k);
			}
		});
		subjectsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				idnumber = 0;
				searchResultList.setAdapter(null);
				highlightClickedSubject(position);
				subject = subjectList.get(position).getSubjectName();
				deleteEmptyNotes();
				highlightClickedItem(idnumber);
				notes.removeTextChangedListener(notesTextWatcher);
				if (notesList.size() == 0) {
					notes.setText("");
					notesId = 0;
					Toast.makeText(v.getContext(), R.string.notesAlert,
							Toast.LENGTH_SHORT).show();
				}
				else{
					notes.setText(notesList.get(0).getNotesDescription());
					notesId = notesList.get(0).getId();
				}
				notes.addTextChangedListener(notesTextWatcher);
				Subject_description.setVisibility(View.VISIBLE);
				subject_name.setText(" "+ subject);
				subject_name.setVisibility(View.VISIBLE);
				try {
					vegaConfig.setValue(VegaConstants.HISTORY_SUBJECT, subject);
				} catch (ColumnDoesNoteExistsException e) {
					Logger.warn(tag, e.toString());
					/*
					 * ToastMessageForExceptions(R.string.
					 * COLUMN_DOES_NOT_EXIST_NOTICEBOARDACTIVITY);
					 * backToDashboard();
					 */
				}
			}
		});
	}

	/**
	 * The Class subjectListArrayAdapter.
	 */
	class subjectListArrayAdapter extends ArrayAdapter<Subject> {
		
		/** The item. */
		List<Subject> item;

		/**
		 * Instantiates a new subject list array adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param subject the subject
		 */
		public subjectListArrayAdapter(Context context, int textViewResourceId,
				List<Subject> subject) {
			super(context, textViewResourceId, subject);
			this.item = subject;
			NoteBookActivity.this.subject = subject.get(0).getSubjectName();
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.subject_selection_list_row, null);
			}
			TextView subject_list = (TextView) view
					.findViewById(R.id.Subjects_list_row);
			final Subject subject = item.get(position);
			String subjectName = subject.getSubjectName();
			subject_list.setText(subjectName);
			return view;
		}
	}

	/**
	 * Download subjects list.
	 */
	private void downloadSubjectsList() {
		String url = serverRequests.getRequestURL(ServerRequests.SUBJECTS_LIST,
				appData.getUserId());
		Download download = new Download(url, appData.getSubjectListPath(),
				ApplicationData.SUBJECTS_LIST_FILE);
		DownloadManager dm = new DownloadManager(appData, download);
		dm.startDownload(new DownloadCallback() {

			@Override
			public void onSuccess(String downloadedString) {
				parseSubjectsList();
				if (subjectList != null && subjectList.getSubjectList() != null
						&& subjectList.getSubjectList().size() != 0) {
					NoteBookActivity.this.subject = subjectList
							.getSubjectList().get(0).getSubjectName();
				}
				checkIfSubjectExistsInDB();
			}

			@Override
			public void onProgressUpdate(int progressPercentage) {

			}

			@Override
			public void onFailure(String failureMessage) {
				Toast.makeText(activity, R.string.Unable_to_reach_pearl_server,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Parses the subjects list.
	 */
	private void parseSubjectsList() {
		String content = "";
		try {
			content = ApplicationData.readFile(appData.getSubjectsListFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		subjectList = SubjectsListParser.getSubjectsList(content, this);
		if (subjectList != null && subjectList.getSubjectList() != null
				&& subjectList.getSubjectList().size() != 0) {
			subject = subjectList.getSubjectList().get(0).getSubjectName();
			inflateSubjectsList(subjectList.getSubjectList());
		} else {
			if(appData.getUser().getUserType()
								.equalsIgnoreCase(RoleType.PRINCIPLE.name()) || appData
						.getUser().getUserType()
						.equalsIgnoreCase(RoleType.HOMEROOMTEACHER.name())
						||appData.getUser().getUserType().equalsIgnoreCase(RoleType.TEACHER.name()))
			Toast.makeText(activity,
					"You are teacher,, make notes only for your subject",
					Toast.LENGTH_LONG).show();
		}
	}

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleBookmarksNotesListVisibility()
     */
    @Override
    public void toggleBookmarksNotesListVisibility() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleTableOfcontentsListVisibility()
     */
    @Override
    public void toggleTableOfcontentsListVisibility() {
	// TODO Auto-generated method stub

    }
    
    private void highlightClickedSubject(final int pos){
		subjectsListView.setSelection(pos);
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<subjectsListView.getChildCount();i++) {
				    View v1=subjectsListView.getChildAt(i);
				    if(i==pos) {
						v1.setBackgroundColor(Color.parseColor("#dfdc8d"));
				    }else {
					    v1.setBackgroundColor(Color.parseColor("#c2bd36"));
				    }
				}
			}
		});
	}
}