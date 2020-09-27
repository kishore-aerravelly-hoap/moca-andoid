package com.pearl.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.NetworkInfo.State;
import android.net.ParseException;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.booklist.BookList;
import com.pearl.books.Book;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.parsers.json.BookListParser;
import com.pearl.parsers.json.ExamCategoryParser;
import com.pearl.parsers.json.TeacherExamParser;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.CommonUtility;
import com.pearl.ui.models.ExamCategory;
import com.pearl.user.teacher.examination.Exam;
import com.pearl.user.teacher.examination.ExamDetails;
import com.pearl.user.teacher.examination.ServerExam;
import com.pearl.user.teacher.examination.ServerExamDetails;
import com.pearl.util.SeedGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateTestStep1Activity.
 */
public class CreateTestStep1Activity extends Activity {

	/** The from date picker. */
	private CustomDateTimePickerActivity fromDatePicker;
	
	/** The to date picker. */
	private CustomDateTimePickerActivity toDatePicker;
	
	/** The from date. */
	private EditText testName, duration, toDate, fromDate;
	
	/** The submit. */
	private Button submit;
	
	/** The i. */
	private int i = 0;
	
	/** The Constant tag. */
	private static final String tag = "CreateTestStep1Activity";
	
	/** The from date cal. */
	private Calendar fromDateCal;
	
	/** The to date cal. */
	private Calendar toDateCal;
	
	/** The exam category. */
	private Spinner examCategory;
	
	/** The grid view. */
	private GridView gridView;
	
	/** The selected from date. */
	private Calendar selectedFromDate;
	
	/** The set fromdate. */
	private long setFromdate;
	
	/** The set todate. */
	private long setTodate;
	
	/** The set duration. */
	private long setDuration;
	
	/** The se. */
	public ServerExam se;
	
	/** The exam details. */
	public ExamDetails examDetails;
	
	/** The open books selected. */
	private boolean openBooksSelected;
	
	/** The chk all. */
	private CheckBox openbook, repeattest, chkAll;
	
	/** The listofbooks. */
	private TextView listofbooks;
	
	/** The context. */
	private Activity context;
	
	/** The text1. */
	private CheckedTextView text1;
	
	/** The menu button. */
	private Button menuButton;
	
	/** The year. */
	private int year;
	
	/** The help. */
	private ImageView help;
	
	/** The strfromdate. */
	private String strfromdate;
	
	/** The selected exam category. */
	private String strtodate, selectedExamCategory;;
	
	/** The app data. */
	private ApplicationData appData;
	
	/** The date2. */
	private Date date1, date2;
	
	/** The Constant TO_DATE. */
	private static final String TO_DATE = "todate";
	
	/** The Diff duration. */
	private long DiffDuration;
	
	/** The Constant FROM_DATE. */
	private static final String FROM_DATE = "fromdate";
	
	/** The selected books. */
	private List<String> selectedBooks;
	// String[] selectedBooks;
	/** The booksadpt. */
	private ImageAdapter booksadpt;
	
	/** The books list. */
	private BookList booksList;
	
	/** The checkfor all. */
	private boolean checkforAll = false;
	
	/** The test id. */
	private String testId = null;
	
	/** The exam category list. */
	List<ExamCategory> examCategoryList = null;
	
	/** The actions. */
	private List<String> actions = new LinkedList<String>();

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.warn(tag, "in oncreate");
		setContentView(R.layout.activity_create_test_step1);
		gridView = (GridView) findViewById(R.id.listview);
		help = (ImageView) findViewById(R.id.create_test_step1_help);
		testName = (EditText) findViewById(R.id.testName_step1);
		testName.requestFocus();
		toDate = (EditText) findViewById(R.id.toDate);
		fromDate = (EditText) findViewById(R.id.fromDate);
		fromDate.setLongClickable(false);
		toDate.setLongClickable(false);
		duration = (EditText) findViewById(R.id.duration);
		openbook = (CheckBox) findViewById(R.id.openbook);
		listofbooks = (TextView) findViewById(R.id.listofbooks);
		submit = (Button) findViewById(R.id.btn);
		chkAll = (CheckBox) findViewById(R.id.chkAll);
		menuButton = (Button) findViewById(R.id.examMenu);
		repeattest = (CheckBox) findViewById(R.id.repeattest);
		examCategory = (Spinner) findViewById(R.id.exam_category);
		selectedBooks = new LinkedList<String>();
		context = this;
		// test_id=SeedGenerator.getNextSeed();
		appData = (ApplicationData) getApplication();
		text1 = (CheckedTextView) findViewById(android.R.layout.simple_list_item_multiple_choice);
		final Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			testId = bundle.getString(VegaConstants.TEST_ID);
			Logger.warn("", "test id from bundle is:" + testId);
			if (testId.equals(null) || testId.equalsIgnoreCase("")) {
				testId = SeedGenerator.getNextSeed();
			}
		} else
			Logger.warn("", "bundle is null");
		Logger.warn("", "testid is:" + testId);
		getExamCatgory();
		inflateExamDetails();
		if (se == null) {
			se = new ServerExam();
			final Exam exam = new Exam();
			examDetails = new ServerExamDetails();
			examDetails.setExamId(testId);
			exam.setId(SeedGenerator.getNextSeed());
			exam.setExamDetails(examDetails);
			se.setExam(exam);

		}

		final InputFilter[] filters = new InputFilter[1];
		/*
		 * filters[0] = new InputFilter(){
		 * 
		 * @Override public CharSequence filter(CharSequence source, int start,
		 * int end, Spanned dest, int dstart, int dend) { if (end > start) {
		 * 
		 * char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g',
		 * 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
		 * 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
		 * 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
		 * 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@',
		 * '.', '_', '#', '$', '%', '&', '*', '-', '+', '(', ')', '!', '"',
		 * '\'', ':', ';', '/', '?', ',', '~', '`', '|', '/', '^', '<', '>',
		 * '{', '}', '[', ']', '=', '�', '�', '�', '.', '�', '�'}; for (int
		 * index = start; index < end; index++) { if (!new
		 * String(acceptedChars).contains(String.valueOf(source.charAt(index))))
		 * { return ""; } } } return null; }
		 * 
		 * };
		 */
		filters[0] = new InputFilter.LengthFilter(35);
		testName.setFilters(filters);

		fromDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fromDatePicker = new CustomDateTimePickerActivity(
						context,
						Calendar.getInstance(),
						toDateCal,
						new CustomDateTimePickerActivity.ICustomDateTimeListener() {
							@Override
							public void onSet(Dialog dialog,
									Calendar calendarSelected,
									Date dateSelected, int year,
									String monthFullName,
									String monthShortName, int monthNumber,
									int date, String weekDayFullName,
									String weekDayShortName, int hour24,
									int hour12, int min, int sec, String AM_PM) {
								final String pattern = "dd/MM/yyyy,HH:mm";
								selectedFromDate = calendarSelected;
								final Calendar calendarCurrentDate = Calendar
										.getInstance();
								final int currentDate = calendarCurrentDate
										.get(Calendar.DATE);
								final int currentMin = calendarCurrentDate
										.get(Calendar.MINUTE);
								final int currentHour = calendarCurrentDate
										.get(Calendar.HOUR_OF_DAY);

								Log.w("", "calendarSelected" + calendarSelected);
								Log.w("", "date " + currentDate
										+ " current min " + currentMin
										+ " hour" + currentHour);
								fromDate.setText("");
								fromDate.setHintTextColor(Color
										.parseColor("#FF3300"));
								date1 = dateSelected;
								CreateTestStep1Activity.this.year = year;
								if (dateSelected.getTime() >= calendarCurrentDate
										.getTime().getTime()) {
									monthNumber++;
									String m = monthNumber+"";
									m = m.length() == 1 ? ("0"+monthNumber): ""+monthNumber;
									String d = calendarSelected
											.get(Calendar.DAY_OF_MONTH) +"";
									d = (d.length() == 1) ? "0"+d : d;  	
									fromDate.setText(""
											+ d
											+ "/"
											+ (m)
											+ "/"
											+ year
											+ ","
											+ hour24
											+ ":"
											+ (String.valueOf(min).length() > 1 ? min
													: "0" + min) + " ");
									strfromdate = fromDate.getText().toString();
									se.getExam().getExamDetails()
											.setStartTime(setFromdate);
								
									Log.i("-----", "============ "
											+ setFromdate);
									try {
										date1 = getDate(strfromdate, pattern);
										toDate.setText(CommonUtility.formatTime(
												new Date(
														date1.getTime() + 60 * 1000 * 60),
												pattern));
										final String tempDate = toDate
												.getText().toString();
										if (date1 != null) {
											setFromdate = date1.getTime();
										}
										if (toDate.getText() != null
												&& !tempDate.isEmpty())
											;
										{
											date2 = getDate(tempDate, pattern);
											setTodate = date2.getTime();
										}
										if (dateComparision(date1, date2)) {
											doFunction();
										}
									} catch (final Exception e) {

									}

								} else {
									fromDate.setHint("Select correct date and time");
									fromDate.setHintTextColor(Color
											.parseColor("#FF3300"));
									date1 = null;
									duration.setText("");
								}
							}

							@Override
							public void onCancel() {

							}
						});
				fromDatePicker.set24HourFormat(true);
				fromDatePicker.setDate(Calendar.getInstance());
				fromDatePicker.showDialog(FROM_DATE);
			}
		});

		toDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (strfromdate == null || fromDate != null
						&& fromDate.getText().toString().isEmpty())

				{
					toDate.setText("");
					toDate.setHint("Please select from date");
					toDate.setHintTextColor(Color.parseColor("#FF3300"));

				} else {
					Log.w("",
							"from date before making a cal to change to date is:"
									+ strfromdate);
					toDatePicker = new CustomDateTimePickerActivity(
							context,
							fromDateCal,
							toDateCal,
							new CustomDateTimePickerActivity.ICustomDateTimeListener() {
								@Override
								public void onSet(Dialog dialog,
										Calendar calendarSelected,
										Date dateSelected, int year,
										String monthFullName,
										String monthShortName, int monthNumber,
										int date, String weekDayFullName,
										String weekDayShortName, int hour24,
										int hour12, int min, int sec,
										String AM_PM) {
									final String pattern = "dd/MM/yyyy,HH:mm";

									final Calendar calendarCurrentDate = Calendar
											.getInstance();
									calendarCurrentDate.get(Calendar.YEAR);
									calendarCurrentDate.get(Calendar.MONTH);
									calendarCurrentDate.get(Calendar.DATE);
									calendarCurrentDate.get(Calendar.MINUTE);
									calendarCurrentDate
											.get(Calendar.HOUR_OF_DAY);
									monthNumber++;
									String m = monthNumber +"";
									
									m = m.length() == 1? "0"+monthNumber : ""+monthNumber;
									toDate.setText("");
									String d = calendarSelected
											.get(Calendar.DAY_OF_MONTH) +"";
									d = (d.length() == 1) ? "0"+d : d;  
									final String toDateString = ""
											+ d
											+ "/"
											+ (m)
											+ "/"
											+ year
											+ ","
											+ hour24
											+ ":"
											+ (String.valueOf(min).length() > 1 ? min
													: "0" + min) + " ";
									try {
										date2 = getDate(toDateString, pattern);
									} catch (final Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									if (calendarSelected
											.before(selectedFromDate)
											|| date1.compareTo(date2) == 0) {
										toDate.setHint("Select correct date and time");
										toDate.setHintTextColor(Color
												.parseColor("#FF3300"));
										date2 = null;
										duration.setText("");

									} else {

										toDate.setText(""
												+ d
												+ "/"
												+ (m)
												+ "/"
												+ year
												+ ","
												+ hour24
												+ ":"
												+ (String.valueOf(min).length() > 1 ? min
														: "0" + min) + " ");
										strtodate = toDate.getText().toString();

										try {
											date2 = getDate(strtodate, pattern);
											if (date2 != null) {
												setTodate = date2.getTime();
											}
										} catch (final Exception e) {

										}

										doFunction();
									}
								}

								@Override
								public void onCancel() {
								}

							});
					toDatePicker.set24HourFormat(true);

					toDatePicker.setDate(Calendar.getInstance());
					toDatePicker.showDialog(TO_DATE);
					// }
				}
			}
		});

		openbook.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (buttonView.isChecked()) {
					if (!actions.contains(buttonView.getText().toString()))
						actions.add(buttonView.getText().toString());
					checkforAll = checkforAll ? !checkforAll : checkforAll;
					if (!checkforAll) {
						if (!setVisibleAllCheckBoxLayOuts()) {
							Toast.makeText(context, "No Books Available",
									Toast.LENGTH_SHORT).show();
							buttonView.setChecked(!buttonView.isChecked());
							if (actions.contains(buttonView.getText()
									.toString()))
								actions.remove(buttonView.getText().toString());
						}
					}

					final OnItemClickListener itemClickListener = new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long id) {

						}
					};
					gridView.setOnItemClickListener(itemClickListener);
				} else {
					if (actions.contains(buttonView.getText().toString()))
						actions.remove(buttonView.getText().toString());

					if (booksList != null) {
						clearAllSelectedBooks();
						booksadpt.notifyDataSetChanged();
						chkAll.setChecked(false);
						chkAll.setText("Check All");
						gridView.setVisibility(View.VISIBLE);
						gridView.setAdapter(booksadpt);
						gridView.setVisibility(View.GONE);
						listofbooks.setVisibility(View.GONE);
						chkAll.setVisibility(View.GONE);
					}
				}
			}
		});
		chkAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (chkAll.isChecked()) {
					 //chkAll.setChecked(false);
					openBooksSelected = true;
					gridView.setAdapter(booksadpt);
					chkAll.setText("Check All");
				} else {
					clearAllSelectedBooks();
					openBooksSelected = false;
					booksadpt = new ImageAdapter(context,
							android.R.layout.simple_selectable_list_item,
							booksList.getBookList());
					gridView.setAdapter(booksadpt);
					chkAll.setText("Check All");

				}

			}

		});
		menuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				final Intent localIntent = new Intent(getApplicationContext(),
						ActionBar.class);
				startActivity(localIntent);
				menuButton.invalidate();

			}

		});

		repeattest.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg0.isChecked()) {
					if (!actions.contains(arg0.getText().toString()))
						actions.add(arg0.getText().toString());

				} else {
					if (actions.contains(arg0.getText().toString()))
						actions.remove(arg0.getText().toString());
				}

			}

		});

		testName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		
		final InputFilter[] filter = new InputFilter[1];
		
		  filter[0] = new InputFilter(){
		  
	
		@Override
		public CharSequence filter(CharSequence source, int start,
			int end, Spanned dest, int dstart, int dend) { if (end > start) {
				 
				  char[] acceptedChars = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',}; for (int
				  index = start; index < end; index++) { if (!new
				  String(acceptedChars).contains(String.valueOf(source.charAt(index))))
				  { return ""; } } } return null; }
		  
		  };
		 
		
		duration.setFilters(filter);
		
		
		
		duration.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (!s.toString().isEmpty()) {
					final long num = Long.parseLong(s.toString()) * 1000 * 60;
					if (num > 0 && num <= DiffDuration) {
						setDuration = num;
					} else {
						duration.setHint("Enter valid duration");
						//duration.setHintTextColor(Color.parseColor("#FF3300"));
						duration.setText("");
						duration.setHintTextColor(Color.parseColor("#FF3300"));
					}
				} else {

				}
			}
		});

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (testName.getText().toString().trim().length() == 0
						|| fromDate.getText().toString().isEmpty()
						|| toDate.getText().toString().isEmpty()
						|| duration.getText().toString().isEmpty()
						|| openbook.isChecked() && selectedBooks.size() < 1 || compareFromDateWithCurrentDate(fromDate.getText().toString())
						|| examCategory.getSelectedItem().equals("Select")
						|| compareDatesWithCurrentDate()) {
					String message = "";
					if(testName.getText().toString().trim().length() == 0){
						testName.setHint("Enter test name");
						testName.setHintTextColor(Color.parseColor("#FF3300"));
					}
					
					if(fromDate.getText().toString().isEmpty()){
						fromDate.setHint("Set start date");
						fromDate.setHintTextColor(Color.parseColor("#FF3300"));
					}
					if(toDate.getText().toString().isEmpty()){
						toDate.setHint("Set end date");
						toDate.setHintTextColor(Color.parseColor("#FF3300"));
					}
					if(compareFromDateWithCurrentDate(fromDate.getText().toString())){
						fromDate.setText("");
						fromDate.setHint("Set start date");
						fromDate.setHintTextColor(Color.parseColor("#FF3300"));
					}
					if(compareDatesWithCurrentDate()) {
					    toDate.setText("");
						toDate.setHint("Set end date ");
						toDate.setHintTextColor(Color.parseColor("#FF3300"));
					}
					
					/*if(!fromDate.getText().toString().isEmpty() && !toDate.getText().toString().isEmpty()){
						toDate.setText("");
						toDate.setHint("Set end date");
						toDate.setHintTextColor(Color.parseColor("#FF3300"));
					}*/
					
					if(openbook.isChecked() && selectedBooks.size() < 1){
						if(message.length() > 0)
							message = message+ ", "+"Open books list";
						else
							message = "Open books list";
						
					}
					if(duration.getText().toString().isEmpty()){
						duration.setHint("Set duration");
						duration.setHintTextColor(Color.parseColor("#FF5050"));
					}
					if(message.length() > 1){
						message =  message + " need to be filled in order to proceed to next step";		
						Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
					}else
						Toast.makeText(getApplicationContext(), "Please make sure that you enter all the choices", Toast.LENGTH_LONG).show();
					
				}else {
					setData();
					if (writeToFile()) {
						final Intent step1 = new Intent(context,
								CreateTestStep2Activity.class);
						step1.putExtra(VegaConstants.TEST_ID, testId);
						startActivity(step1);
					}
				}
			}
		});
		
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
	}
	
	/**
	 * Show dialog.
	 */
	private void showDialog(){
		i = 0;
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent("quizzard_step1.txt", context);
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

	/*
	 * public void setParams(){ testName=test }
	 */

	/**
	 * Sets the data.
	 */
	public void setData() {
		if (se != null && se.getExam() != null) {
			se.getExam().setExamCreatedTeacherId(appData.getUserId());
			final ServerExamDetails se1 = new ServerExamDetails();
			se1.setTitle(testName.getText().toString().trim());
			se1.setStartTime(setFromdate);
			se1.setEndTime(setTodate);
			se1.setBeginDate("");
			se1.setCompleteDate("");
			se1.setExamId(testId);
			String data = parseAcademicYear();
			se1.setAcademicYear(data);
			se1.setDuration(setDuration / (1000 * 60));
			se1.setExamCategory(selectedExamCategory);
			final Date fromDate = new Date(setFromdate);
			se1.setStartDate(fromDate);
			se1.setEndDate(new Date(setTodate));
			if (actions.contains("Repeat Test"))
				se1.setRetakeable(true);
			se.setActions(actions);

			se.setTestAction(!actions.isEmpty() ? actions.toString()
					.replace("[", "").replace("]", "") : "");
			se.getExam().setExamDetails(se1);
			se.setOpenBooksList(selectedBooks != null
					&& !selectedBooks.isEmpty() ? selectedBooks : null);
			se.setOpenBooks1(selectedBooks != null && !selectedBooks.isEmpty() ?
			/*
			 * (se.getOpenBooks1()!=null && !se.getOpenBooks1().isEmpty())?
			 * se.getOpenBooks1()+","+selectedBooks.toString().replace("[",
			 * "").replace("]", ""):
			 */
			selectedBooks.toString().replace("[", "").replace("]", "") : null);
			se.getExam().setOpenBookExam(openbook.isChecked());
			se.getExam()
					.setOpenBooks(
							selectedBooks != null && !selectedBooks.isEmpty() ? selectedBooks
									: null);
		}
	}
	
	/**
	 * Parses the academic year.
	 *
	 * @return the string
	 */
	private String parseAcademicYear(){
		ObjectMapper mapper = new ObjectMapper();
		String data = "";
		try {
			String data1 = ApplicationData.readFile(appData.getAcademicYearDetailsFileName());
			BaseResponse response = mapper.readValue(data1, BaseResponse.class);
			data = response.getData();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
 
	
	
	/**
	 * Gets the date.
	 *
	 * @param date the date
	 * @param pattern the pattern
	 * @return the date
	 * @throws Exception the exception
	 */
	public Date getDate(String date, String pattern) throws Exception {
		final SimpleDateFormat dateFormate = new SimpleDateFormat(pattern);
		try {
			return dateFormate.parse(date);
		} catch (final Exception e) {
			throw e;
		}
	}

	/**
	 * Do function.
	 */
	public void doFunction() {

		try {
			long durationToset;

			if (date1.getTime() > date2.getTime())
				durationToset = date1.getTime() - date2.getTime();
			else
				durationToset = date2.getTime() - date1.getTime();

			final Date d = new Date();
			d.setTime(durationToset);
			final long time = d.getTime();
			setDuration = time;
			DiffDuration = time;
			se.getExam().getExamDetails().setDuration(time);
			duration.setText(String.valueOf(time / (1000 * 60)));
		} catch (final ParseException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	// AdapterClass
	/**
	 * The Class ImageAdapter.
	 */
	public class ImageAdapter extends ArrayAdapter<Book> {
		
		/** The m inflater. */
		private LayoutInflater mInflater;
		
		/** The context. */
		private final Context context;
		
		/** The books list. */
		private final List<Book> booksList;
		
		/** The check box state. */
		boolean[] checkBoxState;

		/**
		 * Instantiates a new image adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param booksList the books list
		 */
		public ImageAdapter(Context context, int textViewResourceId,
				List<Book> booksList) {
			super(context, textViewResourceId);
			this.context = context;
			this.booksList = booksList;
			checkBoxState = new boolean[booksList.size()];
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return booksList.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = new View(context);
				mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(
						R.layout.create_test_step1_list_row, null);
				holder.holderCheckbox = (CheckBox) convertView
						.findViewById(R.id.openBookCheckBox);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.holderCheckbox.setId(position);
			holder.holderCheckbox.setText(booksList.get(position).getMetaData()
					.getTitle());
			final String value = String.valueOf(booksList.get(position)
					.getBookId());
			Logger.warn("CreateTestStep1Activity", "inflaate - selectedBooks "+selectedBooks);
			if (chkAll.isChecked()) {
				holder.holderCheckbox.setChecked(true);
				if (!selectedBooks.contains(value))
					selectedBooks.add(value);
				//checkBoxState[position] = true;
				for(int i=0; i<booksList.size(); i++)
					checkBoxState[i] = true;
			} else{
				//checkBoxState[position] = false;
				boolean flag = false;
				if (se.getOpenBooksList() != null
						&& !se.getOpenBooksList().isEmpty()
						&& selectedBooks.contains(value)) {
					flag = se.getOpenBooksList().contains(value);
					
				}
				
				holder.holderCheckbox.setChecked(flag);
			}

			holder.holderCheckbox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// String str=BookValues[position];
					if (((CheckBox) v).isChecked()) {
						checkBoxState[position] = true;
						if (!selectedBooks.contains(String.valueOf(booksList
								.get(position).getBookId())))
							selectedBooks.add(String.valueOf(booksList.get(
									position).getBookId()));
						boolean checkFlag = true;
						for(int i=0; i<checkBoxState.length; i++){
							if(!checkBoxState[i])
								checkFlag = false;
						}
						if(checkFlag && !chkAll.isChecked())
							chkAll.setChecked(true);
					} else {
						
						checkBoxState[position] = false;
						if (selectedBooks.contains(String.valueOf(booksList
								.get(position).getBookId())))
							selectedBooks.remove(String.valueOf(booksList.get(
									position).getBookId()));
						//selectedBooks.add(booksList.get(position));
						if(chkAll.isChecked() && selectedBooks.size() != CreateTestStep1Activity.this.booksList.getBookList().size() 
								&& selectedBooks.size()>0){
							chkAll.setChecked(false);
							chkAll.setText("Check All");
						}/*else{
							chkAll.setChecked(true);
						}*/
					}
				}
			});
			holder.id = position;
			return convertView;
		}
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {
		
		/** The holder checkbox. */
		CheckBox holderCheckbox;
		
		/** The id. */
		int id;
	}

	/**
	 * Populate book list.
	 */
	private void populateBookList() {
		if (!ApplicationData.isFileExists(appData.getDownloadedBooksListXML())) {
			Logger.warn("tag", "file doesnot exists so download");
		}

		Logger.warn("tag", "file exists so parse");
		final String filePath = appData.getBooksListsPath()
				+ ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME;
		try {
			booksList = BookListParser.getBooksList(filePath);

			booksadpt = new ImageAdapter(context,
					android.R.layout.simple_selectable_list_item,
					booksList.getBookList());

			gridView.setAdapter(booksadpt);
		} catch (final Exception e) {
			Logger.error("tag", "unable to parse json:" + e);

		}
	}

	/**
	 * Inflate exam details.
	 */
	private void inflateExamDetails() {
		if (ApplicationData.isFileExists(appData.getStep1FileName(testId))) {
			String content = null;
			try {
				content = ApplicationData.readFile(appData
						.getStep1FileName(testId));
				if (content != null) {
					se = TeacherExamParser.parseJsonToSeverExamObject(content);
					if (se != null && se.getExam() != null
							&& se.getExam().getExamDetails() != null) {
						testName.setText(se.getExam().getExamDetails()
								.getTitle());
						selectedFromDate = Calendar.getInstance();
						setFromdate = se.getExam().getExamDetails()
								.getStartTime();
						setTodate = se.getExam().getExamDetails().getEndTime();
						if (setTodate != 0 && setFromdate != 0) {
							date1 = new Date(setFromdate);
							date2 = new Date(setTodate);
						}
						if(se.getExam().getExamDetails().getExamCategory() != null || se.getExam().getExamDetails().getExamCategory() != ""){
							int position = 0;
							for(int i=0; i<examCategoryList.size(); i++){
								if(examCategoryList.get(i).getExamType().equals(se.getExam().getExamDetails().getExamCategory()))
									position = i;
							}
							examCategory.setSelection(position);
						}
						DiffDuration = date1.getTime() > date2.getTime() ? date1
								.getTime() - date2.getTime()
								: date2.getTime() - date1.getTime();
						selectedFromDate.setTime(date1);
						fromDate.setText(strfromdate = CommonUtility
								.formatTime(date1, "dd/MM/yyyy,HH:mm"));
						toDate.setText(strtodate = CommonUtility.formatTime(
								date2, "dd/MM/yyyy,HH:mm"));
						final long time = se.getExam().getExamDetails()
								.getDuration();
						setDuration = time * 1000 * 60;
						actions = se.getActions();
						duration.setText(String.valueOf(setDuration
								/ (1000 * 60)));
						if (se.getTestAction().indexOf(
								repeattest.getText().toString()) != -1)
							repeattest.setChecked(true);
						if (se.getTestAction().indexOf(
								openbook.getText().toString()) != -1)
							openbook.setChecked(true);
						isAllBooksChecked();
					}
				} else
					return;
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final Exception e) {
				Logger.error(tag, e);
				e.printStackTrace();
			}

		}
	}

	/**
	 * Generate under construction tests list.
	 *
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void generateUnderConstructionTestsList()
			throws JsonProcessingException, IOException {
		List<ServerExamDetails> examList = null;
		if (ApplicationData.isFileExists(appData.getTestsListFileName())) {
			String content = null;
			try {
				content = ApplicationData.readFile(appData
						.getTestsListFileName());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			examList = TeacherExamParser.getExamsList(content);
		} else {
			Logger.warn(tag, "file doesnot exists");
			examList = new LinkedList<ServerExamDetails>();
			if (se != null && se.getExam() != null
					&& se.getExam().getExamDetails() != null) {
				final ServerExamDetails serverExamDetails = (ServerExamDetails) se
						.getExam().getExamDetails();
				examList.add(serverExamDetails);
			}
		}

		boolean newId = true;
		final Iterator<ServerExamDetails> iterator = examList.iterator();
		int position = 0;
		int i = 0;
		while (iterator.hasNext()) {
			position++;
			final ExamDetails examDetails = iterator.next();
			examDetails.getExamId();
			if (examDetails.getExamId().equals(testId)) {
				Logger.warn("", "list doessnt contain id:" + testId);
				i = position;
				newId = false;
			}
		}
		if (newId) {
			try {
				examList.add((ServerExamDetails) se.getExam().getExamDetails());
			} catch (final Exception e) {
				Log.e(tag, "ERROR");
			}

		} else {
			final ServerExamDetails serverExamDetails = examList.get(i - 1);
			serverExamDetails.setTitle(testName.getText().toString());
			serverExamDetails.setStartTime(setFromdate);
			serverExamDetails.setEndTime(setTodate);
			serverExamDetails.setBeginDate("");
			serverExamDetails.setCompleteDate("");
			serverExamDetails.setExamId(testId);
			serverExamDetails.setDuration(setDuration / (1000 * 60));
			final Date fromDate = new Date(setFromdate);
			serverExamDetails.setStartDate(fromDate);
			serverExamDetails.setEndDate(new Date(setTodate));
			if (actions.contains("Repeat Test"))
				serverExamDetails.setRetakeable(true);
		}
		final ObjectMapper om = new ObjectMapper();
		final String examListJson = om.writeValueAsString(examList);
		ApplicationData.writeToFile("{\"serverExamDetailsList\":"
				+ examListJson + "}", appData.getTestsListFileName());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (!appData.isLoginStatus()) {

			final Intent login = new Intent(this, LoginActivity.class);
			startActivity(login);
			Logger.info(tag,
					"LOGIN status if shelf..." + appData.isLoginStatus());
			finish();

		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		Logger.warn(tag, "in pause");
		setData();
		writeToFile();
		try {
			if (se != null && se.getExam() != null
					&& se.getExam().getExamDetails() != null) {
				if (se.getExam().getExamDetails().getTitle() != null
						&& !se.getExam().getExamDetails().getTitle().equals(""))
					generateUnderConstructionTestsList();
			} else
				Logger.warn(tag,
						"not saving the test as server exam object is null");
		} catch (final JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Checks if is all checked.
	 *
	 * @return true, if is all checked
	 */
	private boolean isAllChecked() {
		boolean flag = false;
		if (openbook.isChecked()) {

			if (se.getOpenBooksList() != null
					&& !se.getOpenBooksList().isEmpty()) {
				selectedBooks = se.getOpenBooksList();
			}
			setVisibleAllCheckBoxLayOuts();
			if (selectedBooks.size() == booksList.getBookList().size()) {
				chkAll.setChecked(true);
				openBooksSelected = true;
				chkAll.setText("Check All");
				flag = true;
			}
			//else  change it t checkALL and set the checked as false
			else if(selectedBooks.size()!=booksList.getBookList().size() && (booksList.getBookList().size() >=1)){
				chkAll.setChecked(false);
				openBooksSelected = true;
				chkAll.setText("Check All");
				flag = false;
			}

		}
		return flag;
	}
	
	

	/**
	 * Checks if is all books checked.
	 */
	private void isAllBooksChecked() {
		if (!checkforAll)
			checkforAll = isAllChecked();
	}

	/**
	 * Clear all selected books.
	 */
	private void clearAllSelectedBooks() {
		if (selectedBooks != null && !selectedBooks.isEmpty())
			selectedBooks.clear();
	}

	/**
	 * Sets the visible all check box lay outs.
	 *
	 * @return true, if successful
	 */
	public boolean setVisibleAllCheckBoxLayOuts() {
		boolean flag = false;
		populateBookList();
		if (booksList != null && booksList.getBookList() != null
				&& !booksList.getBookList().isEmpty()) {
			listofbooks.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.VISIBLE);
			chkAll.setVisibility(View.VISIBLE);
			flag = !flag;
		}
		return flag;
	}

	/**
	 * Date comparision.
	 *
	 * @param date1 the date1
	 * @param date2 the date2
	 * @return true, if successful
	 */
	private boolean dateComparision(Date date1, Date date2) {
		return date1 != null && date2 != null ? date1.compareTo(date2) < 0 ? date2
				.compareTo(date1) >= 0 : date1.compareTo(date2) < 0
				: !(date1 != null && date2 != null);

	}

	/**
	 * Sets the duration foucs.
	 */
	private void setDurationFoucs() {
		duration.setFocusable(true);
	}

	/**
	 * Un set duration focus.
	 */
	private void unSetDurationFocus() {
		duration.setFocusable(false);
	}

	/**
	 * Write to file.
	 *
	 * @return true, if successful
	 */
	private boolean writeToFile() {
		final ObjectMapper mapper = new ObjectMapper();
		String str = "";
		try {
			Logger.warn(tag, "exam id before writing to file is: "
					+ se.getExam().getExamDetails().getExamId());
			str = mapper.writeValueAsString(se);

		} catch (final JsonGenerationException e) {
			e.printStackTrace();
			return false;
		} catch (final JsonMappingException e) {
			e.printStackTrace();
			return false;
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		} catch (final Exception e) {
			return false;
		}

		ApplicationData.writeToFile(str, appData.getStep1FileName(testId));
		String content = "";
		try{
			if(ApplicationData.isFileExists(appData
				    .getStep2FileName(testId))){
				content = ApplicationData.readFile(appData
						.getStep2FileName(testId));
				final ServerExam serverExamnew = TeacherExamParser
						.parseJsonToSeverExamObject(content);
				final ServerExamDetails se1 = new ServerExamDetails();
				final Date fromDate = new Date(setFromdate);
				se1.setStartDate(fromDate);
				se1.setEndDate(new Date(setTodate));
				se1.setMaxPoints(serverExamnew.getExam().getExamDetails().getMaxPoints());
				serverExamnew.getExam().setExamDetails(se1);
				serverExamnew.getExam().getExamDetails().setExamCategory(se.getExam().getExamDetails().getExamCategory());
				serverExamnew.getExam().getExamDetails().setStartTime(se.getExam().getExamDetails().getStartTime());
				serverExamnew.getExam().getExamDetails().setEndTime(se.getExam().getExamDetails().getEndTime());
				serverExamnew.getExam().getExamDetails().setDuration(se.getExam().getExamDetails().getDuration());
				serverExamnew.getExam().getExamDetails().setExamId(testId);
			    str = mapper.writeValueAsString(serverExamnew);
				ApplicationData.writeToFile(str, appData.getStep2FileName(testId));
				    }	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * Compare dates with current date.
	 *
	 * @return true, if successful
	 */
	private boolean compareDatesWithCurrentDate(){
		try {
			date2 = getDate(toDate.getText().toString(), "dd/MM/yyyy,HH:mm");
			return (new Date().after(date2))?true:false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean compareFromDateWithCurrentDate(String date){
		try {
			date1= getDate(fromDate.getText().toString(), "dd/MM/yyyy,HH:mm");
			return (new Date().after(date1))?true:false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sets the exam catgory.
	 *
	 * @param examCategoryList the new exam catgory
	 */
	private void setExamCatgory(List<ExamCategory> examCategoryList){
		List<String> list = new ArrayList<String>();
		for(int i=0; i<examCategoryList.size(); i++){
			list.add(examCategoryList.get(i).getExamType());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_list_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		examCategory.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		examCategory.setOnItemSelectedListener(new ExamCategoryListItemSelectedListener());
	}
	
	/**
	 * Gets the exam catgory.
	 *
	 * @return the exam catgory
	 */
	private void getExamCatgory(){
		String content = "";
		if(ApplicationData.isFileExists(appData.getTestCategoryFileName())){
			try {
				content  = ExamCategoryParser.ParseJson(appData.getTestCategoryFileName());
				examCategoryList = ExamCategoryParser.getExamCategory(content);
				ExamCategory category = new ExamCategory();
				category.setExamType("Select");
				category.setId("0");
				examCategoryList.add(0, category);
				setExamCatgory(examCategoryList);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * The listener interface for receiving examCategoryListItemSelected events.
	 * The class that is interested in processing a examCategoryListItemSelected
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addExamCategoryListItemSelectedListener<code> method. When
	 * the examCategoryListItemSelected event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ExamCategoryListItemSelectedEvent
	 */
	private class ExamCategoryListItemSelectedListener implements OnItemSelectedListener{

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> adapter, View v, int position,
				long id) {
			selectedExamCategory = examCategoryList.get(position).getExamType();
		}

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
}
