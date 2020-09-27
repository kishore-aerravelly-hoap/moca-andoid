package com.pearl.activities;

/**
 * @author spasnoor
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.baseresponse.login.AndroidUser;
import com.pearl.booklist.BookList;
import com.pearl.books.Book;
import com.pearl.epub.EpubReader;
import com.pearl.epub.TableOfContent;
import com.pearl.epub.TableOfContent.Chapter;
import com.pearl.examination.QuestionType;
import com.pearl.examination.helpers.ExamHelper;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.parsers.json.BookListParser;
import com.pearl.parsers.json.EditExamParser;
import com.pearl.parsers.json.TeacherExamParser;
import com.pearl.ui.helpers.examination.CreateQuestionUI;
import com.pearl.user.teacher.examination.Exam;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.ServerExam;
import com.pearl.user.teacher.examination.ServerExamDetails;
import com.pearl.user.teacher.examination.ServerQuestion;
import com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion;
import com.pearl.user.teacher.examination.questiontype.LongAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.MatchTheFollowingQuestion;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.user.teacher.examination.questiontype.OrderingQuestion;
import com.pearl.user.teacher.examination.questiontype.ShortAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateTestStep2Activity.
 */
public class CreateTestStep2Activity extends Activity {

	/** The user. */
	private AndroidUser user;
	
	/** The server question. */
	private ServerQuestion serverQuestion;
	
	/** The chapters list. */
	private Spinner questiontype, books, chaptersList;
	
	/** The answerlayout. */
	private LinearLayout questionlayout, marksAllotedlayout, answerlayout;
	
	/** The exam helper. */
	private ExamHelper examHelper;
	
	/** The answer star. */
	private TextView answer, answerStar;
	
	/** The server exam. */
	private ServerExam serverExam;
	
	/** The activity context. */
	private Context activityContext;
	
	/** The fetch from question bank. */
	private Button preview, add, next, menuButton, fetchFromQuestionBank;
	
	/** The help. */
	private ImageView help;
	
	/** The i. */
	private int i = 0;
	
	/** The marks alloted. */
	private EditText questionView, marksAlloted;
	
	/** The chapter list adapter. */
	private ArrayAdapter<String> questiontypeAdapter, downloadedBooksAdapter,
			chapterListAdapter;
	
	/** The app data. */
	private ApplicationData appData;
	
	/** The books list. */
	private BookList booksList;
	
	/** The selected chapter name. */
	private String testId, selectedChapterName;
	
	/** The toc list. */
	private List<String> tocList;
	
	/** The existing questions list. */
	private List<Question> questionsList, existingQuestionsList;
	
	/** The fetch_from_db. */
	private boolean flag1, fetch_from_db = false;
	
	/** The tag. */
	private final String tag = "CreateTestStep2Activity";
	
	/** The maxpoints. */
	private int count, maxpoints;
	
	/** The exam. */
	private Exam exam;
	
	/** The bundle. */
	private Bundle bundle;
	
	/** The select. */
	private int SELECT = 0;
	
	/** The true or false question. */
	private int TRUE_OR_FALSE_QUESTION = 1;
	
	/** The fill in the blank question. */
	private int FILL_IN_THE_BLANK_QUESTION = 2;
	
	/** The multiplechoice question. */
	private int MULTIPLECHOICE_QUESTION = 3;
	
	/** The short answer question. */
	private int SHORT_ANSWER_QUESTION = 4;
	
	/** The long answer question. */
	private int LONG_ANSWER_QUESTION = 5;
	
	/** The ordering question. */
	private int ORDERING_QUESTION = 6;
	
	/** The match the following question. */
	private int MATCH_THE_FOLLOWING_QUESTION = 7;
	
	/** The question. */
	private Question question;
	
	/** The edit_add. */
	private boolean edit_question, replace, edit_add = false;
	
	/** The edit_question_id. */
	private String edit_book_name, edit_chapter_name, edit_question_id,question_orderNo;
	
	/** The edit_chapter_position. */
	private int edit_book_position, edit_chapter_position;
	
	/** The handler. */
	private Handler handler;
	
	/** The chapter list local. */
	private List<String> chapterListLocal=new ArrayList<String>();
	
	private TableOfContent toc = null;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_test_step_2);
		questiontype = (Spinner) findViewById(R.id.questiontype);
		questionlayout = (LinearLayout) findViewById(R.id.questionlayout);
		marksAllotedlayout = (LinearLayout) findViewById(R.id.marksAllotedlayout);
		answerlayout = (LinearLayout) findViewById(R.id.answerlayout);
		add = (Button) findViewById(R.id.add);
		next = (Button) findViewById(R.id.next);
		preview = (Button) findViewById(R.id.preview);
		fetchFromQuestionBank = (Button) findViewById(R.id.fetch_from_questionbank);
		answer = (TextView) findViewById(R.id.answer);
		answerStar = (TextView) findViewById(R.id.ansstr);
		chaptersList = (Spinner) findViewById(R.id.chapter);
		chaptersList.setEnabled(false);
		help = (ImageView) findViewById(R.id.create_test_step2_help);
		books = (Spinner) findViewById(R.id.book);
		questionView = (EditText) findViewById(R.id.questioneditText);
		marksAlloted = (EditText) findViewById(R.id.marksAlloted);
		menuButton = (Button) findViewById(R.id.examMenu);
		booksList = new BookList();
		tocList = new LinkedList<String>();
		serverExam = new ServerExam();
		serverQuestion = new ServerQuestion();
		examHelper = new ExamHelper();
		appData = (ApplicationData) getApplication();
		user = appData.getUser();
		activityContext = getApplicationContext();
		handler=new Handler();

		bundle = getIntent().getExtras();
		if (bundle != null) {
			testId = bundle.getString(VegaConstants.TEST_ID);
			edit_add = edit_question = bundle.getBoolean("edit_question");

		}
		questionlayout.setVisibility(View.GONE);
		marksAllotedlayout.setVisibility(View.GONE);
		Logger.warn(tag, "Object from bundle is:" + bundle.get("object"));

		populateExamDetails();
		bindEvents();
		setQuestionTypeAdapter();
		if (edit_question) {
			SharedPreferences sharedPref = CreateTestStep2Activity.this
					.getSharedPreferences("com.pearl.prefs", MODE_PRIVATE);
			questiontype.setEnabled(false);
			String question_obj = sharedPref.getString("QUESTION_obj", null);
			edit_question_id = sharedPref.getString("edit_question_id", null);
			question_orderNo= sharedPref.getString("question_orderNo", null);
			// String
			// edit_question_id=sharedPref.getString("edit_question_id",null);
			Gson gson = new Gson();
			question = gson.fromJson(question_obj, Question.class);

			// Log.i(getClass().getSimpleName(),
			// "MY EDITOR QUESTION OBJ IS----"+question.getq);
			String question_str = null;
			if (QuestionType.TRUE_OR_FALSE_QUESTION.toString().equals(
					question.getType())) {
				questiontype.setSelection(TRUE_OR_FALSE_QUESTION);
				TrueOrFalseQuestion tfq = gson.fromJson(question_obj,
						TrueOrFalseQuestion.class);
				serverQuestion.setType(question.getType());
				serverQuestion = EditExamParser.convertTrueOrFalseQuestion(
						serverQuestion, tfq);
				question_str = serverQuestion.getQuestion();
			}

			else if (QuestionType.FILL_IN_THE_BLANK_QUESTION.toString().equals(
					question.getType())) {
				questiontype.setSelection(FILL_IN_THE_BLANK_QUESTION);
				FillInTheBlankQuestion fib = gson.fromJson(question_obj,
						FillInTheBlankQuestion.class);
				serverQuestion.setType(question.getType());
				serverQuestion = EditExamParser.convertFillInTheBlankQuestion(
						serverQuestion, fib);
				question_str = serverQuestion.getQuestion();

			} else if (QuestionType.MULTIPLECHOICE_QUESTION.toString().equals(
					question.getType())) {
				questiontype.setSelection(MULTIPLECHOICE_QUESTION);
				MultipleChoiceQuestion mcq = gson.fromJson(question_obj,
						MultipleChoiceQuestion.class);
				serverQuestion.setType(question.getType());
				serverQuestion = EditExamParser.convertMultipleChoiceQuestion(
						serverQuestion, mcq);
				question_str = serverQuestion.getQuestion();
			} else if (QuestionType.SHORT_ANSWER_QUESTION.toString().equals(
					question.getType())) {
				questiontype.setSelection(SHORT_ANSWER_QUESTION);
				ShortAnswerQuestion saq = gson.fromJson(question_obj,
						ShortAnswerQuestion.class);
				serverQuestion.setType(question.getType());
				serverQuestion = EditExamParser.convertShortAnswerQuestion(
						serverQuestion, saq);
				question_str = serverQuestion.getQuestion();
			} else if (QuestionType.LONG_ANSWER_QUESTION.toString().equals(
					question.getType())) {
				questiontype.setSelection(LONG_ANSWER_QUESTION);
				LongAnswerQuestion laq = gson.fromJson(question_obj,
						LongAnswerQuestion.class);
				serverQuestion.setType(question.getType());
				serverQuestion = EditExamParser.convertLongAnswerQuestion(
						serverQuestion, laq);
				question_str = serverQuestion.getQuestion();
			}

			else if (QuestionType.ORDERING_QUESTION.toString().equals(
					question.getType())) {
				questiontype.setSelection(ORDERING_QUESTION);
				OrderingQuestion oq = gson.fromJson(question_obj,
						OrderingQuestion.class);
				serverQuestion.setType(question.getType());
				serverQuestion = EditExamParser.convertOrderingQuestion(
						serverQuestion, oq);
				question_str = serverQuestion.getQuestion();
			} else if (QuestionType.MATCH_THE_FOLLOWING_QUESTION.toString()
					.equals(question.getType())) {
				questiontype.setSelection(MATCH_THE_FOLLOWING_QUESTION);
				MatchTheFollowingQuestion mtf = gson.fromJson(question_obj,
						MatchTheFollowingQuestion.class);
				serverQuestion.setType(question.getType());
				serverQuestion = EditExamParser
						.convertMatchTheFollowingQuestion(serverQuestion, mtf);
				question_str = serverQuestion.getQuestion();
			}
			books.setSelection(checkBookPosition(question.getBookName()));
			marksAlloted.setText(question.getMarksAlloted() + "");
			questionView.setText(question_str);
			fetchFromQuestionBank.setVisibility(View.GONE);
			preview.setVisibility(View.INVISIBLE);
			preview.setClickable(false);
			next.setVisibility(View.INVISIBLE);
			next.setClickable(false);
			updateUI();
		} else {
			if (!ApplicationData.isFileExists(appData.getStep2FileName(testId))) {
				preview.setVisibility(View.INVISIBLE);
			} else {
				preview.setVisibility(View.VISIBLE);
				preview.setClickable(true);
				next.setVisibility(View.VISIBLE);
				next.setClickable(true);
				questiontype.setEnabled(true);
				fetchFromQuestionBank.setVisibility(View.VISIBLE);
			}
		}
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

		if (null != SelectQuestionsActivity.list && !SelectQuestionsActivity.list.isEmpty()) {
			// sharedPref=CreateTestStep2Activity.this.getSharedPreferences("com.pearl.prefs",MODE_PRIVATE);
			existingQuestionsList = new ArrayList<Question>(
					SelectQuestionsActivity.list);
			SelectQuestionsActivity.list = null;
			saveExam();
			Toast.makeText(CreateTestStep2Activity.this,
					"Questions imported successfully", Toast.LENGTH_LONG)
					.show();
		}
		if(edit_question){
			fetchFromQuestionBank.setVisibility(View.INVISIBLE);
			fetchFromQuestionBank.setClickable(false);
			preview.setVisibility(View.INVISIBLE);
			preview.setClickable(false);
			next.setVisibility(View.INVISIBLE);
			next.setClickable(false);
		}else{
			if (!ApplicationData.isFileExists(appData.getStep2FileName(testId))) {
				preview.setVisibility(View.INVISIBLE);
			} else {
				preview.setVisibility(View.VISIBLE);
				preview.setClickable(true);
				next.setVisibility(View.VISIBLE);
				next.setClickable(true);
				questiontype.setEnabled(true);
				fetchFromQuestionBank.setVisibility(View.VISIBLE);
				fetchFromQuestionBank.setClickable(true);
			}
			/*preview.setVisibility(View.VISIBLE);
			preview.setClickable(true);
			next.setVisibility(View.VISIBLE);
			next.setClickable(true);
			questiontype.setEnabled(true);
			fetchFromQuestionBank.setVisibility(View.VISIBLE);
			fetchFromQuestionBank.setClickable(true);*/
		}


	}

	 /**
 	 * Check book position.
 	 *
 	 * @param bookName the book name
 	 * @return the int
 	 */
 	private int checkBookPosition(String bookName) {
		int position = 1;
		for (int k = 0; k < booksList.getBookList().size(); k++) {

			if (bookName.equalsIgnoreCase(booksList.getBookList().get(k)
					.getMetaData().getTitle())) {
				position = k + 1;
				Collections.sort(chapterListLocal,new Comparator<String>() {

					@Override
					public int compare(String lhs, String rhs) {
						return lhs.compareTo(rhs);
					}
				});
				
				chapterListLocal=parseTOCForEdit(booksList.getBookList().get(k)
						.getBookId(),booksList.getBookList().get(k)
						.getFilename());
				chapterListAdapter = new ArrayAdapter<String>(this,
						R.layout.spinner_list_item, chapterListLocal);
				chapterListAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				chaptersList.setAdapter(chapterListAdapter);
				chapterListAdapter.notifyDataSetChanged();
			
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						Log.i(getLocalClassName(), "--------------------ONE1111111111111111111111111111111111111111");
						chaptersList.setSelection(checkChapterPosition(chapterListLocal,question
								.getChapterName()),true);
						
					}
				}, 300);
				
			
			
			}
		}
		return position;
	}

	/**
	 * Check chapter position.
	 *
	 * @param chapterList the chapter list
	 * @param chapterName the chapter name
	 * @return the int
	 */
	private int checkChapterPosition(List<String> chapterList,String chapterName) {
		int position = 0;
	
		for (int k = 0; k < chapterList.size(); k++) {

			if (chapterName.equalsIgnoreCase(chapterList.get(k).toString())) {
				position = k;
			}
		}
		return position;
	}

	/**
	 * Populate exam details.
	 */
	private void populateExamDetails() {
		upDateMethod();
		parseDownloadedBooksList();
		tocList = getChapterList(null);
		setTOCAdapter();

	}

	/**
	 * Up date method.
	 */
	public void upDateMethod() {

		String content = null;
		ServerExam serverExamStep1;
		Exam exam = new Exam();
		String title;

		long duration, startTime, endTime;
		if (ApplicationData.isFileExists(appData.getStep1FileName(testId))) {
			try {
				content = ApplicationData.readFile(appData
						.getStep1FileName(testId));
				serverExamStep1 = TeacherExamParser
						.parseJsonToSeverExamObject(content);
				if (serverExamStep1 != null) {
					title = serverExamStep1.getExam().getExamDetails()
							.getTitle();
					startTime = serverExamStep1.getExam().getExamDetails()
							.getStartTime();
					endTime = serverExamStep1.getExam().getExamDetails()
							.getEndTime();
					duration = serverExamStep1.getExam().getExamDetails()
							.getDuration();
					serverExam.setActions(serverExamStep1.getActions());
					serverExam.setTestAction(serverExamStep1.getTestAction());
					serverExam.setOpenBooksList(serverExamStep1
							.getOpenBooksList());
					serverExam.setOpenBooks1(serverExamStep1.getOpenBooks1());
					if (ApplicationData.isFileExists(appData
							.getStep2FileName(testId))) {
						content = ApplicationData.readFile(appData
								.getStep2FileName(testId));
						final ServerExam serverExamStep2 = TeacherExamParser
								.parseJsonToSeverExamObject(content);
						exam = serverExamStep2.getExam();
						serverExam.setExam(exam);
						serverExam.getExam().getExamDetails().setMaxPoints(serverExamStep2.getExam().getExamDetails().getMaxPoints());
						serverExam.getExam().getExamDetails().setTitle(title);
						serverExam.getExam().getExamDetails()
								.setStartTime(startTime);
						serverExam.getExam().getExamDetails()
								.setEndTime(endTime);
						serverExam.getExam().getExamDetails()
								.setDuration(duration);
						serverExam.getExam().setExamCreatedTeacherId(
								serverExamStep1.getExam()
										.getExamCreatedTeacherId());
						serverExam
								.getExam()
								.getExamDetails()
								.setRetakeable(
										serverExamStep1.getExam()
												.getExamDetails()
												.isRetakeable());
						serverExam.getExam().setOpenBookExam(
								serverExamStep1.getExam().isOpenBookExam());
						serverExam.getExam().setOpenBooks(
								serverExamStep1.getExam().getOpenBooks());
						writeServerExamToFile();
					}
				}

			} catch (final IOException e) {
				Logger.error(tag, e);
			} catch (final Exception e) {
				Logger.error(tag, e);
			}
		}
	}

	/**
	 * Sets the question type adapter.
	 */
	private void setQuestionTypeAdapter() {
		List<String> questionTypes = new LinkedList<String>();

		convertToQuestionType();
		questionTypes = QuestionType.getQuestionType();
		questionTypes.add(0, "Select");

		if (questionTypes != null && questionTypes.size() != 0) {
			questiontypeAdapter = new ArrayAdapter<String>(this,
					R.layout.spinner_list_item, questionTypes);
			questiontypeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			questiontype.setAdapter(questiontypeAdapter);
			questiontype.setSelection(0);
			questiontype
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							Logger.warn(tag, "Question______type:" + arg2);

							if (edit_question == false) {
								answerlayout.refreshDrawableState();
								serverQuestion = new ServerQuestion();
								Logger.warn(tag, "Question______type:"
										+ questiontype.getSelectedItem());
								serverQuestion.setType(questiontype
										.getSelectedItem().toString());
								Logger.warn(tag, "_____QuestionType is"
										+ serverQuestion.getType());
								books.setAdapter(downloadedBooksAdapter);
								chaptersList.setAdapter(chapterListAdapter);
								updateUI();
								questionView.setText("");
								marksAlloted.setText("1");
								if (!ApplicationData.isFileExists(appData
										.getStep2FileName(testId))) {
									preview.setVisibility(View.INVISIBLE);
								} else {
									preview.setVisibility(View.VISIBLE);
								}
								chaptersList.setEnabled(false);

							} else {
								// edit_question=false;
								// updateUI();
								// bundle.putBoolean("edit_question",false);
								// edit_question=false;
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
		}
	}

	/**
	 * Update ui.
	 */
	public void updateUI() {

		questionlayout.setVisibility(View.GONE);
		marksAllotedlayout.setVisibility(View.GONE);
		answer.setText("Answer");
		answerStar.setVisibility(View.VISIBLE);
		questionView.setHintTextColor(Color.parseColor("#808069"));
		String server_type = serverQuestion.getType();
		String spinnerType = (String) questiontype.getSelectedItem();
		// String type=(spinnerType==null)?server_type:spinnerType;
		String type = spinnerType;
		if (type.equals("TRUE_OR_FALSE_QUESTION")) {
			questionView.setHeight(170);
			questionlayout.setVisibility(View.VISIBLE);
			marksAllotedlayout.setVisibility(View.VISIBLE);
		} else if (type.equals("SHORT_ANSWER_QUESTION")
				|| type.equals("LONG_ANSWER_QUESTION")) {
			questionView.setHeight(170);
			questionlayout.setVisibility(View.VISIBLE);
			answerStar.setVisibility(View.INVISIBLE);
			marksAllotedlayout.setVisibility(View.VISIBLE);
		} else if (type.equals("FILL_IN_THE_BLANK_QUESTION")) {
			questionView.setHeight(170);
			questionlayout.setVisibility(View.VISIBLE);
			marksAllotedlayout.setVisibility(View.VISIBLE);
			answerStar.setVisibility(View.INVISIBLE);
			answer.setText("Note:- Text between @@ would be treated as the right answer( @@Galileo@@is the inventor of telescope. )");
		} else if (type.equals("MATCH_THE_FOLLOWING_QUESTION")) {
			questionView.setHeight(40);
			questionlayout.setVisibility(View.VISIBLE);
			marksAllotedlayout.setVisibility(View.VISIBLE);
		} else if (type.equals("MULTIPLECHOICE_QUESTION")) {
			questionView.setHeight(40);
			questionlayout.setVisibility(View.VISIBLE);
			marksAllotedlayout.setVisibility(View.VISIBLE);
		} else if (type.equals("ORDERING_QUESTION")) {
			questionView.setHeight(40);
			questionlayout.setVisibility(View.VISIBLE);
			marksAllotedlayout.setVisibility(View.VISIBLE);
		} else {
			questionlayout.setVisibility(View.GONE);
			marksAllotedlayout.setVisibility(View.GONE);
			clearLayout(answerlayout);
			// List<MultiChoiceQuestionAnswer>
			// multiChoiceQnA=LazyList.decorate(new
			// ArrayList(),FactoryUtils.instantiateFactory(MultiChoiceQuestionAnswer.class));
			// serverQuestion.setMultiChoiceQnA(new
			// List<MultiChoiceQuestionAnswer>());
		}
		clearLayout(answerlayout);
		updateCreateQuestionLayout(serverQuestion, answerlayout);
	}

	/**
	 * The listener interface for receiving questionTypeItemSelected events.
	 * The class that is interested in processing a questionTypeItemSelected
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addquestionTypeItemSelectedListener<code> method. When
	 * the questionTypeItemSelected event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see questionTypeItemSelectedEvent
	 */
	class questionTypeItemSelectedListener implements OnItemSelectedListener {
	
		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			serverQuestion.setType(questiontype.getSelectedItem().toString());
			books.setAdapter(downloadedBooksAdapter);
			chaptersList.setAdapter(chapterListAdapter);
			updateUI();
			questionView.setText("");
			marksAlloted.setText("1");
			if (!ApplicationData.isFileExists(appData.getStep2FileName(testId))) {
				preview.setVisibility(View.INVISIBLE);
			} else {
				preview.setVisibility(View.VISIBLE);
			}
			chaptersList.setEnabled(false);
		}

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	/**
	 * Bind events.
	 */
	private void bindEvents() {
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (edit_question){
					showAlertForReplacingQuesiton();
				}
				else{
					addQuestion();
				}
			}
		});

		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
			}
		});

		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final AppStatus status = new AppStatus();
				if (status.isOnline(activityContext)) {
					if (!ApplicationData.isFileExists(appData
							.getStep2FileName(testId))) {
						Toast.makeText(
								getApplicationContext(),
								"You have to add atleast one question to proceed to next step",
								Toast.LENGTH_LONG).show();
					}else if( questiontype.getSelectedItem().toString() != "Select"){
						Toast.makeText(
								getApplicationContext(),
								"Add the question and then proceed to next step",
								Toast.LENGTH_LONG).show();
				
					}
					else {
						final Intent i = new Intent(
								CreateTestStep2Activity.this,
								CreateExamStepThree.class);
						i.putExtra(VegaConstants.TEST_ID, testId);
						startActivity(i);
						// finish();
					}
				} else {
					Toast.makeText(activityContext,
							"Please connect to internet in order to proceed",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		preview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (questiontype.getSelectedItem().toString() != null
						&& questiontype.getSelectedItem().toString() == "Select") {
					final Intent i = new Intent(CreateTestStep2Activity.this,
							PreviewAddedQuestionsActivity.class);
					i.putExtra(VegaConstants.TEST_ID, testId);
					startActivity(i);
				} else {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							CreateTestStep2Activity.this);
					alertDialog.setTitle("Save Question...");
					alertDialog
							.setMessage("Do you want to save this question?");
					alertDialog.setIcon(R.drawable.answersheet);
					alertDialog.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									checkForAnswerEmptyFields();
									final boolean flag = checkForEmptyFields();
									if (!flag && !flag1) {
										serverQuestion.setQuestion(questionView
												.getText().toString());
										saveExam();
										final Intent i = new Intent(
												CreateTestStep2Activity.this,
												PreviewAddedQuestionsActivity.class);
										i.putExtra(VegaConstants.TEST_ID,
												testId);
										startActivity(i);
									} else {
										if (Question.ORDERING_QUESTION
												.equalsIgnoreCase(questiontype
														.getSelectedItem()
														.toString())
												|| Question.MULTIPLECHOICE_QUESTION
														.equalsIgnoreCase(questiontype
																.getSelectedItem()
																.toString())
												|| Question.MATCH_THE_FOLLOWING
														.equalsIgnoreCase(questiontype
																.getSelectedItem()
																.toString()))
											if (serverQuestion
													.getMultiChoiceQnA().size() < 2) {
												Toast.makeText(
														getApplicationContext(),
														"Atleast 2 choices has to be added",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(
														getApplicationContext(),
														"please make sure that you enter all the fields",
														Toast.LENGTH_SHORT)
														.show();
											}
									}
								}
							});

					alertDialog.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent i = new Intent(
											CreateTestStep2Activity.this,
											PreviewAddedQuestionsActivity.class);
									i.putExtra(VegaConstants.TEST_ID, testId);
									startActivity(i);
								}
							});

					/*
					 * alertDialog.setNeutralButton("Cancel", new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick(DialogInterface dialog, int
					 * which) { dialog.cancel(); } });
					 */

					alertDialog.show();
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

		marksAlloted.setInputType(InputType.TYPE_CLASS_NUMBER);
		marksAlloted.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				Log.d(getClass().getSimpleName(),
						"----------MARKS ALLOTED ARE---" + s.toString());
						   			    	
				marksAlloted.setTextColor(Color.parseColor("#000000"));
				
				if (s.toString().trim().length() != 0) {
					marksAlloted
							.setBackgroundResource(android.R.drawable.edit_text);
					serverQuestion.setMarksAlloted(Integer.parseInt(s
							.toString()));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		fetchFromQuestionBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppStatus.getInstance(CreateTestStep2Activity.this)
						.isOnline(CreateTestStep2Activity.this)) {
					Intent fetchQuestions = new Intent(
							CreateTestStep2Activity.this,
							SelectQuestionsActivity.class);
					fetchQuestions.putExtra(VegaConstants.TEST_ID, testId);
					startActivity(fetchQuestions);
				} else {
					Toast.makeText(CreateTestStep2Activity.this,
							"Please check your internet connection",
							Toast.LENGTH_LONG).show();
				}
			}
		});

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
				"quizzard_stpe2.txt", this);
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

	/**
	 * Parses the downloaded books list.
	 */
	private void parseDownloadedBooksList() {

		Logger.info(
				"Create test step2",
				"Populating bookslist from the path : "
						+ appData.getDownloadedBooksListXML());
		List<Book> tempBookList = new ArrayList<Book>();
		if (serverExam.getOpenBooksList() != null
				&& serverExam.getOpenBooksList().size() != 0) {
			for (int i = 0; i < serverExam.getOpenBooksList().size(); i++) {
				int bookId = Integer.parseInt(serverExam.getOpenBooksList()
						.get(i));
				Book book = appData.getBook(bookId);
				tempBookList.add(book);
				booksList.setBookList(tempBookList);
			}
		} else {
			if (ApplicationData.isFileExists(appData
					.getDownloadedBooksListXML())) {
				Logger.warn(tag, "file exists");
				Logger.warn(tag, "file exists so parse");
				final String filePath = appData.getBooksListsPath()
						+ ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME;
				try {
					booksList = BookListParser.getBooksList(filePath);
					Logger.warn(tag, "BookListSize is:"
							+ booksList.getBookList().size());
				} catch (final Exception e) {
					Logger.error(tag, "unable to parse json:" + e);
				}
			}
		}
		booksList.addBook();
		setBooksListAdapter();
	}

	/**
	 * Sets the books list adapter.
	 */
	private void setBooksListAdapter() {
		final List<String> downloadedbooks = new ArrayList<String>();
		downloadedbooks.add(0, "Select");
		Logger.warn(tag, "New List Values size are:" + downloadedbooks.size());
		for (int i = 0; i < booksList.getBookList().size(); i++) {
			final Book book = booksList.getBookList().get(i);
			downloadedbooks.add(book.getMetaData().getTitle());
			if (book.getMetaData().getTitle().equalsIgnoreCase("Default")) {
				edit_book_name = book.getMetaData().getTitle();
				edit_book_position = i + 1;
			}
		}
		downloadedBooksAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_list_item, downloadedbooks);
		downloadedBooksAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		books.setAdapter(downloadedBooksAdapter);
		books.setOnItemSelectedListener(new BookListItemSelectListerner());
	}

	/**
	 * Parses the toc.
	 *
	 * @param bookId the book id
	 * @param fileName the file name
	 */
	public void parseTOC(int bookId, String fileName) {
		TableOfContent toc = null;
		final ProgressDialog loadingDialog = new ProgressDialog(
				CreateTestStep2Activity.this);
		loadingDialog.setTitle("Please wait processing books");
		loadingDialog.setMessage("Processing " + fileName);
		loadingDialog.show();
		if (bookId != 0
				&& ApplicationData.isFileExists(appData
						.getBookFilesPath(bookId) + fileName)
		/*
		 * && !ApplicationData.isFileExists(appData
		 * .getUserBookReaderTempPath(bookId))
		 */) {
			try {
				final EpubReader epubReader = new EpubReader(
						appData.getUserBookReaderTempPath(bookId),
						appData.getBookFilesPath(bookId) + fileName);
				toc = epubReader.parseTableOfContent();			
			} catch (final Exception e) {
				Logger.error(tag, e);
				ApplicationData.deleteFolderOrFileRecursively(new File(appData
						.getBookFilesPath(bookId)));// BOOKS_DIR
				Logger.warn(tag, "Book id of the book being deleted is:"
						+ bookId);
				if (ApplicationData.isFileExists(appData
						.getUserBookReaderTempPath(bookId))) {
					ApplicationData.deleteFolderOrFileRecursively(new File(
							appData.getUserBookReaderTempPath(bookId)));
				}
				Toast.makeText(this, R.string.exam_book_partial_download, 1000)
						.show();
			}
		}
		loadingDialog.hide();
		tocList = getChapterList(toc);
		setTOCAdapter();
	}

	/**
	 * Parses the toc for edit.
	 *
	 * @param bookId the book id
	 * @param fileName the file name
	 * @return the list
	 */
	private List<String> parseTOCForEdit(int bookId, String fileName){
		
		if (bookId != 0
				&& ApplicationData.isFileExists(appData
						.getBookFilesPath(bookId) + fileName)
	) {
			try {
				final EpubReader epubReader = new EpubReader(
						appData.getUserBookReaderTempPath(bookId),
						appData.getBookFilesPath(bookId) + fileName);
				toc = epubReader.parseTableOfContent();			
			} catch (final Exception e) {
				Logger.error(tag, e);
				ApplicationData.deleteFolderOrFileRecursively(new File(appData
						.getBookFilesPath(bookId)));// BOOKS_DIR
				Logger.warn(tag, "Book id of the book being deleted is:"
						+ bookId);
				if (ApplicationData.isFileExists(appData
						.getUserBookReaderTempPath(bookId))) {
					ApplicationData.deleteFolderOrFileRecursively(new File(
							appData.getUserBookReaderTempPath(bookId)));
				}
				Toast.makeText(this, R.string.exam_book_partial_download, 1000)
						.show();
			}
		}
		return getChapterList(toc);
	}
	
	/**
	 * Gets the chapter list.
	 *
	 * @param toc the toc
	 * @return the chapter list
	 */
	private List<String> getChapterList(TableOfContent toc) {
		final List<String> chapterList = new LinkedList<String>();
		chapterList.add("Select");
		if (toc != null && toc.getChapterList() != null) {
			final Iterator<Chapter> tocList = toc.getChapterList().iterator();
			while (tocList.hasNext()) {
				chapterList.add(tocList.next().getTitle());
			}
		}
		chapterList.add("Default");
		return chapterList;
	}

	/**
	 * Sets the toc adapter.
	 */
	private void setTOCAdapter() {
				for (int i = 0; i < tocList.size(); i++) {
			Logger.warn(tag, "toc content is:" + tocList.get(i));
			if (tocList.get(i).equalsIgnoreCase("Default")) {
				edit_chapter_name = tocList.get(i);
				edit_chapter_position = i;
			}
		}
		
		chapterListAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_list_item, tocList);
		chapterListAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chaptersList.setAdapter(chapterListAdapter);
		chaptersList
				.setOnItemSelectedListener(new ChapterListItemSelectListerner());
	}

	/**
	 * Update create question layout.
	 *
	 * @param question the question
	 * @param questionLayout the question layout
	 */
	private void updateCreateQuestionLayout(ServerQuestion question,
			LinearLayout questionLayout) {
		final CreateQuestionUI ui = CreateQuestionUI.getInstance();
		ui.build(question, this, questionLayout);

	}

	/**
	 * Clear layout.
	 *
	 * @param layout the layout
	 * @return the linear layout
	 */
	private LinearLayout clearLayout(LinearLayout layout) {
		if (layout == null) {
			return new LinearLayout(activityContext);
		}
		layout.removeAllViews();

		return layout;
	}

	/**
	 * The Class BookListItemSelectListerner.
	 */
	private class BookListItemSelectListerner implements OnItemSelectedListener {

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
			if (!books.getSelectedItem().equals("Select")) {
				final int bookId = booksList.getBookList().get(position - 1)
						.getBookId();
				final String fileName = booksList.getBookList()
						.get(position - 1).getFilename();
				
				if (books.getSelectedItem() == "Default") {
					serverQuestion.setBookName("Default");
				} else {
					serverQuestion.setBookName(books.getSelectedItem().toString());
					
				}
				parseTOC(bookId, fileName);
				chaptersList.setEnabled(true);
			}
		}

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	/**
	 * The Class ChapterListItemSelectListerner.
	 */
	private class ChapterListItemSelectListerner implements
			OnItemSelectedListener {

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
			if (!chaptersList.getSelectedItem().equals("Select")) {
				selectedChapterName = chaptersList.getSelectedItem().toString();
				serverQuestion.setChapterName(selectedChapterName);
			}
		}

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	/**
	 * Check for empty fields.
	 *
	 * @return true, if successful
	 */
	private boolean checkForEmptyFields() {
    	boolean flag = false;
    	if(!edit_question){
	if ((questionView.getText().toString().trim().length()==0)
		|| books.getSelectedItem().equals("Select")
		|| chaptersList.getSelectedItem().equals("Select")
		|| marksAlloted.getText().toString().trim().equals("") 
		|| marksAlloted.getText().toString().trim().equals("0")
		|| marksAlloted.getText().toString().trim().equals("00")
		|| marksAlloted.getText().toString().trim().equals("000")) {
		flag = true;
		 return true;
	} else
	    return false;
    }else {
    	if ((questionView.getText().toString().trim().length()==0)
    			|| books.getSelectedItem().equals("Select")
    			|| chaptersList.getSelectedItem().equals("Select")
    			|| marksAlloted.getText().toString().trim().equals("") 
    			|| marksAlloted.getText().toString().trim().equals("0")
    			|| marksAlloted.getText().toString().trim().equals("00")
    			|| marksAlloted.getText().toString().trim().equals("000")) {
    		flag = true;	
    			 return true;
    		}else 
    	return false;
    }
}

	/**
	 * Check for answer empty fields.
	 */
	private void checkForAnswerEmptyFields() {
		if (Question.ORDERING_QUESTION.equalsIgnoreCase(questiontype
				.getSelectedItem().toString())) {
			// && serverQuestion.getMultiChoiceQnA() == null
			if (serverQuestion.getMultiChoiceQnA().size() < 2) {
				flag1 = true;
			} else {
				for (int i = 0; i < serverQuestion.getMultiChoiceQnA().size(); i++) {

					if ((serverQuestion.getMultiChoiceQnA().get(i)
							.getAnswerDes() == null)
							|| serverQuestion.getMultiChoiceQnA().get(i)
									.getAnswerDes().trim().length() == 0
							|| ((serverQuestion.getMultiChoiceQnA().get(i)
									.getCheckedValue() == null) || serverQuestion
									.getMultiChoiceQnA().get(i)
									.getCheckedValue().trim().length() == 0)) {
						flag1 = true;
						break;
					} else {
						flag1 = false;
					}
				}
			}
		} else if (Question.MATCH_THE_FOLLOWING.equalsIgnoreCase(questiontype
				.getSelectedItem().toString())) {
			if (serverQuestion.getMultiChoiceQnA().size() < 2) {
				flag1 = true;
			} else {
				for (int i = 0; i < serverQuestion.getMultiChoiceQnA().size(); i++) {
					if ((serverQuestion.getMultiChoiceQnA().get(i)
							.getAnswerDes() == null)
							|| serverQuestion.getMultiChoiceQnA().get(i)
									.getAnswerDes().trim().length() == 0

							|| ((serverQuestion.getMultiChoiceQnA().get(i)
									.getCheckedString() == null) || (serverQuestion
									.getMultiChoiceQnA().get(i)
									.getCheckedString().trim().length() == 0))
							|| ((serverQuestion.getMultiChoiceQnA().get(i)
									.getCheckedText() == null) || (serverQuestion
									.getMultiChoiceQnA().get(i)
									.getCheckedText().trim().length() == 0))
							|| ((serverQuestion.getMultiChoiceQnA().get(i)
									.getCheckedValue() == null) || (serverQuestion
									.getMultiChoiceQnA().get(i)
									.getCheckedValue().trim().length() == 0))) {
						flag1 = true;
						break;
					} else {
						flag1 = false;
					}
				}
			}
		} else if (Question.MULTIPLECHOICE_QUESTION
				.equalsIgnoreCase(questiontype.getSelectedItem().toString())) {
			if (serverQuestion.getMultiChoiceQnA().size() < 2) {
				flag1 = true;
			} else {
				if (serverQuestion.getHasMultipleAnswers()) {
					int a = 0;
					for (int k = 0; k < serverQuestion.getMultiChoiceQnA()
							.size(); k++) {
						if (serverQuestion.getMultiChoiceQnA().get(k)
								.getCheckedValue() != null) {
							a++;
							break;
						}

					}
					for (int j = 0; j < serverQuestion.getMultiChoiceQnA()
							.size(); j++) {
						if (serverQuestion.getMultiChoiceQnA().get(j)
								.getAnswerDes().trim().length() != 0
								&& a > 0) {
							flag1 = false;
						} else {
							flag1 = true;
							break;
						}

					}

				} else {
					int b = 0;
					for (int k = 0; k < serverQuestion.getMultiChoiceQnA()
							.size(); k++) {
						if (serverQuestion.getCheckValues() != null) {
							b++;
							break;
						}
					}
					for (int j = 0; j < serverQuestion.getMultiChoiceQnA()
							.size(); j++) {

						if (serverQuestion.getMultiChoiceQnA().get(j)
								.getAnswerDes() == null) {
							flag1 = true;
							break;
						} else {
							if (serverQuestion.getMultiChoiceQnA().get(j)
									.getAnswerDes().trim().length() != 0
									&& b > 0) {
								flag1 = false;
							} else {
								flag1 = true;
								break;
							}

						}

					}
				}
			}
		}/*
		 * else if (Question.LONG_ANSWER_QUESTION.equalsIgnoreCase(questiontype
		 * .getSelectedItem().toString())) { if (serverQuestion.getLongAnswer()
		 * == null) { flag1 = true; } else { flag1 = false; } } else if
		 * (Question.LONG_ANSWER_QUESTION.equalsIgnoreCase(questiontype
		 * .getSelectedItem().toString())) { if (serverQuestion.getShortAnswer()
		 * == null) { flag1 = true; } else { flag1 = false; } }
		 */else if (Question.FILL_IN_THE_BLANK_QUESTION
				.equalsIgnoreCase(questiontype.getSelectedItem().toString())) {
			final String s = questionView.getText().toString();
			final int count = StringUtils.countMatches(s, "@@");
			int index = 0;
			if (count != 0 && count % 2 == 0) {
				while (s.indexOf("@@", index) != -1) {
					flag1 = false;
					if (s.indexOf("@@", index) != -1) {
						index = s.indexOf("@@", index) + 2;
						final String s1 = s.substring(index, s.length());
						if (s1.contains("@") && !s1.contains("@@")) {
							flag1 = true;
							Toast.makeText(
									getApplicationContext(),
									"Invalid question,"
											+ " please refer to the note given below",
									Toast.LENGTH_LONG).show();
						}
					}
				}
			} else {
				flag1 = true;
				if (s.trim().length() != 0) {
					Toast.makeText(
							getApplicationContext(),
							"Invalid question,"
									+ " please refer to the note given below",
							Toast.LENGTH_LONG).show();
				}
			}
		} else {
			flag1 = false;
		}
	}

	/**
	 * Save exam.
	 */
	private void saveExam() {
		questionsList = new LinkedList<Question>();
		Logger.warn(tag, "Server question object is:" + serverQuestion);
		Exam exam = null;
		int maxPoints = 0, count = 0;
		String content = null;
		try {
			if (ApplicationData.isFileExists(appData.getStep1FileName(testId))
					&& !ApplicationData.isFileExists(appData
							.getStep2FileName(testId))) {
				content = ApplicationData.readFile(appData
						.getStep1FileName(testId));
			} else {
				content = ApplicationData.readFile(appData
						.getStep2FileName(testId));
			}
			if (content != null) {
				final ServerExam serverExamnew = TeacherExamParser
						.parseJsonToSeverExamObject(content);
				exam = serverExamnew.getExam();
				if (exam.getQuestions() == null) {
					serverExam.setOpenBooks1(serverExamnew.getOpenBooks1());
					serverExam.setOpenBooksList(serverExamnew
							.getOpenBooksList());
					serverExam.setStudentIdList(serverExamnew
							.getStudentIdList());
					serverExam.setStudentsList(serverExamnew.getStudentsList());
					serverExam.setTestAction(serverExamnew.getTestAction());
					final ServerExamDetails serverExamDetails = (ServerExamDetails) serverExamnew
							.getExam().getExamDetails();
					serverExam.getExam().setExamDetails(serverExamDetails);
				
				}
			} else
				throw new Exception("Exam not found");

		} catch (final IOException e) {
			Logger.error(tag, e);
		} catch (final Exception e) {
			Logger.error(tag, e);
		}

		if (exam != null) {

			List<Question> list = null;
			if (null != exam.getQuestions()) {
				list = exam.getQuestions();
				if (existingQuestionsList != null) {
					for (int foo = 0; foo < list.size(); foo++) {
						for (int bar = 0; bar < existingQuestionsList.size(); bar++) {
							if (list.get(foo)
									.getQuestion()
									.equalsIgnoreCase(
											existingQuestionsList.get(bar)
													.getQuestion())) {
								Log.d(getClass().getSimpleName(),
										"------------------------------------------------------Both are same");
							}
						}
					}
					list.addAll(existingQuestionsList);
				}

			} else {
				if (existingQuestionsList != null)
					list = new ArrayList<Question>(existingQuestionsList);
			}
			if (list != null && list.size() > 0) {
				maxPoints=0;
				for (final Question question : list) {
					
			//		maxPoints = maxPoints + question.getMarksAlloted();
					questionsList.add(question);
				}
				if (exam.getExamDetails() != null)
					exam.getExamDetails().setMaxPoints(maxPoints);
				
			} else {
				if (exam.getExamDetails() != null)
					exam.getExamDetails().setMaxPoints(maxPoints);
			}
		}

		if (questionsList.size() == 0) {
			count = 1;
		} else {
			// questionsList.remove(question.getQuestionOrderNo());
			count = questionsList.size();
			final int size = questionsList.get(count - 1).getQuestionOrderNo();
			count = size + 1;
		}
		this.count = count;
		this.maxpoints = maxPoints;
		this.exam = exam;
		addQuestionsToList(count, maxPoints, questionsList, exam);

	}

	/**
	 * Adds the questions to list.
	 *
	 * @param count the count
	 * @param maxPoints the max points
	 * @param questionsList the questions list
	 * @param exam the exam
	 */
	private void addQuestionsToList(int count, int maxPoints,
			List<Question> questionsList, Exam exam) {
		if (serverQuestion.getType().equalsIgnoreCase(
				Question.LONG_ANSWER_QUESTION)) {
			maxPoints = maxPoints + serverQuestion.getMarksAlloted();
		//	exam.getExamDetails().setMaxPoints(maxPoints);
			final Question longAnswer = examHelper
					.getLongAnswerQuestion(serverQuestion);
			Logger.warn(tag, "final long answer object is:" + longAnswer);
			if (replace)
				replaceQuestion(questionsList, replace, longAnswer);
			else{
				serverQuestion.setQuestionOrderNo(count);
				questionsList.add(longAnswer);
			}
		}
		if (serverQuestion.getType().equalsIgnoreCase(
				Question.SHORT_ANSWER_QUESTION)) {
			maxPoints = maxPoints + serverQuestion.getMarksAlloted();
			final Question shortAnswer = examHelper
					.getShortAnswerQuestion(serverQuestion);
		//	exam.getExamDetails().setMaxPoints(maxPoints);
			Logger.warn(tag, "final short answer object is:" + shortAnswer);
			if (replace)
				replaceQuestion(questionsList, replace, shortAnswer);
			else{
				serverQuestion.setQuestionOrderNo(count);
				questionsList.add(shortAnswer);
			}
		}
		if (serverQuestion.getType().equalsIgnoreCase(
				Question.FILL_IN_THE_BLANK_QUESTION)) {
			maxPoints = maxPoints + serverQuestion.getMarksAlloted();
			final FillInTheBlankQuestion fib = examHelper
					.getFillInTheBlankQuestion(serverQuestion);
		//	exam.getExamDetails().setMaxPoints(maxPoints);
			Logger.warn(tag, "final fib object is:" + fib);
			if (replace)
				replaceQuestion(questionsList, replace, fib);
			else{
				serverQuestion.setQuestionOrderNo(count);
				questionsList.add(fib);
			}
		}
		if (serverQuestion.getType().equalsIgnoreCase(
				Question.TRUE_OR_FALSE_QUESTION)) {
			maxPoints = maxPoints + serverQuestion.getMarksAlloted();
		//	exam.getExamDetails().setMaxPoints(maxPoints);
			final Question tfq = examHelper
					.getTrueOrFlseQuestion(serverQuestion);
			if (replace)
				replaceQuestion(questionsList, replace, tfq);
			else{
				serverQuestion.setQuestionOrderNo(count);
				questionsList.add(tfq);
			}
		}
		if (serverQuestion.getType().equalsIgnoreCase(
				Question.ORDERING_QUESTION)) {
			maxPoints = maxPoints + serverQuestion.getMarksAlloted();
			final Question orderingQuestion = examHelper
					.convertToOrderingQuestion(serverQuestion);
			Logger.warn(tag, "final ordering answer object is:"
					+ orderingQuestion);
	//		exam.getExamDetails().setMaxPoints(maxPoints);
			if (replace)
				replaceQuestion(questionsList, replace, orderingQuestion);
			else{
				serverQuestion.setQuestionOrderNo(count);
				questionsList.add(orderingQuestion);
			}
		}
		if (serverQuestion.getType().equalsIgnoreCase(
				Question.MULTIPLECHOICE_QUESTION)) {
			maxPoints = maxPoints + serverQuestion.getMarksAlloted();
			final Question multipleChoiceQn = examHelper
					.convertToMultipleChoiceQuestion(serverQuestion);
			Logger.warn(tag, "final multiple choice answer object is:"
					+ multipleChoiceQn);
		//	exam.getExamDetails().setMaxPoints(maxPoints);
			if (replace)
				replaceQuestion(questionsList, replace, multipleChoiceQn);
			else{
				serverQuestion.setQuestionOrderNo(count);
				questionsList.add(multipleChoiceQn);
			}
		}

		if (serverQuestion.getType().equalsIgnoreCase(
				Question.MATCH_THE_FOLLOWING)) {
			maxPoints = maxPoints + serverQuestion.getMarksAlloted();
			final Question mtf = examHelper
					.convertToMatchTheFollowingQuestion(serverQuestion);
			Logger.warn(tag, "final match the 3" + " answer object is:" + mtf);
			//exam.getExamDetails().setMaxPoints(maxPoints);
			if (replace)
				replaceQuestion(questionsList, replace, mtf);
			else{
				serverQuestion.setQuestionOrderNo(count);
				questionsList.add(mtf);
			}
		}
		int total=0;
		for (int i = 0; i < questionsList.size(); i++) {
			questionsList.get(i).setQuestionOrderNo(i + 1);
			
			total=total+questionsList.get(i).getMarksAlloted();
			
		}
		exam.getExamDetails().setMaxPoints(total);
		Logger.warn(tag, "$$$$$$$$$$$$$$$$$$$$$$$$$ Total marks are---" + total);
		exam.setQuestions(questionsList);
		Logger.warn(tag, "Questionslist size is:" + questionsList.size());
		serverExam.setExam(exam);
		final ServerExamDetails serverExamDetails = (ServerExamDetails) serverExam
				.getExam().getExamDetails();
		serverExam.getExam().setExamDetails(serverExamDetails);
		writeServerExamToFile();
	}

	/**
	 * Write server exam to file.
	 */
	private void writeServerExamToFile() {
		final ObjectMapper objectMapper = new ObjectMapper();
		String serverExamString = null;
		try {
			serverExamString = objectMapper.writeValueAsString(serverExam);
		} catch (final JsonGenerationException e) {
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		ApplicationData.writeToFile(serverExamString,
				appData.getStep2FileName(testId));
		ApplicationData.writeToFile(serverExamString,
				appData.getStep1FileName(testId));
	}

	/**
	 * Replace question.
	 *
	 * @param questionsList the questions list
	 * @param replace the replace
	 * @param quesiton the quesiton
	 */
	void replaceQuestion(List<Question> questionsList, boolean replace,
			Question quesiton) {
		if (replace) {
			int pos = 0;
			int qno=Integer.parseInt(question_orderNo);
			for (int k = 0; k < questionsList.size(); k++) {
				if (questionsList.get(k).getId().equalsIgnoreCase(edit_question_id) &&
						questionsList.get(k).getQuestionOrderNo() == qno) {
					pos = k;
					questionsList.remove(k);
				}
			}
			quesiton.setExisted(false);
			questionsList.add(pos, quesiton);
			this.replace=false;
		}
	}

	/**
	 * Convert to question type.
	 *
	 * @return the question type
	 */
	private QuestionType convertToQuestionType() {
		final ObjectMapper objMapper = new ObjectMapper();
		QuestionType questionType = null;
		try {
			questionType = objMapper
					.readValue(ApplicationData.readFile(appData
							.getQuestionTypeFileName()), QuestionType.class);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return questionType;
	}

	/**
	 * Display dialog.
	 *
	 * @param previewFlag the preview flag
	 */
	private void displayDialog(final boolean previewFlag) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				CreateTestStep2Activity.this);
		alertDialog.setTitle("Save Question...");
		alertDialog.setMessage("Do you want to save this question?");
		alertDialog.setIcon(R.drawable.answersheet);
		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						checkForAnswerEmptyFields();
						final boolean flag = checkForEmptyFields();
						if (!flag && !flag1) {
							serverQuestion.setQuestion(questionView.getText()
									.toString());
							saveExam();
							if (previewFlag) {
								final Intent i = new Intent(
										CreateTestStep2Activity.this,
										PreviewAddedQuestionsActivity.class);
								i.putExtra(VegaConstants.TEST_ID, testId);
								startActivity(i);
							} else {
								final Intent i = new Intent(
										CreateTestStep2Activity.this,
										CreateExamStepThree.class);
								i.putExtra(VegaConstants.TEST_ID, testId);
								startActivity(i);
							}

						} else {
							if (Question.ORDERING_QUESTION
									.equalsIgnoreCase(questiontype
											.getSelectedItem().toString())
									|| Question.MULTIPLECHOICE_QUESTION
											.equalsIgnoreCase(questiontype
													.getSelectedItem()
													.toString())
									|| Question.MATCH_THE_FOLLOWING
											.equalsIgnoreCase(questiontype
													.getSelectedItem()
													.toString()))
								if (serverQuestion.getMultiChoiceQnA().size() < 2) {
									Toast.makeText(
											getApplicationContext(),
											"Atleast 2 choices has to be added",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"please make sure that you enter all the fields",
											Toast.LENGTH_SHORT).show();
								}
						}
					}
				});
	}

	/**
	 * Show alert for replacing quesiton.
	 */
	private void showAlertForReplacingQuesiton() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				CreateTestStep2Activity.this);
		alertBuilder.setIcon(R.drawable.overwrite);
		alertBuilder.setTitle("Replace Question");
		// LayoutInflater
		// inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

		// alertBuilder.setView(inflater.inflate(R.id.dialoge_layout,null));
		alertBuilder
				.setMessage("Click 'Yes' to replace the existing question,'No' to create new question");
		alertBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						replace = true;
						addQuestion();

					}
				});
		alertBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						replace = false;
						addQuestion();
					}
				});
		alertBuilder.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				});

		AlertDialog alert = alertBuilder.create();
		alert.show();
	}

	/**
	 * Adds the question.
	 */
	private void addQuestion() {

		checkForAnswerEmptyFields();
		final boolean flag = checkForEmptyFields();
		if (!flag && !flag1) {

			questiontype.setAdapter(questiontypeAdapter);
			books.setAdapter(downloadedBooksAdapter);
			chaptersList.setAdapter(chapterListAdapter);
			Logger.warn(tag, "Question string is:"
					+ questionView.getText().toString());
			serverQuestion.setQuestion(questionView.getText().toString());
			saveExam();
			questionView.setText("");
			marksAlloted.setText("");
			fetchFromQuestionBank.setVisibility(View.VISIBLE);
			questiontype.setEnabled(true);
			chaptersList.setEnabled(false);
			edit_question = false;
			updateUI();

			preview.setVisibility(View.VISIBLE);
			preview.setClickable(true);
			next.setVisibility(View.VISIBLE);
			next.setClickable(true);
			questiontype.setEnabled(true);
			fetchFromQuestionBank.setVisibility(View.VISIBLE);
			fetchFromQuestionBank.setClickable(true);
		
		} else {
			if (marksAlloted == null
					|| marksAlloted.getText().toString().trim().length() == 0
					|| marksAlloted.getText().toString().equals("0")
					|| marksAlloted.getText().toString().equals("00")
					|| marksAlloted.getText().toString().equals("000")) {
				marksAlloted.setText("0");
				marksAlloted.setTextColor(Color.parseColor("#FF3300"));
			}
			if (questionView == null
					|| questionView.getText().toString().trim().length() == 0) {
				questionView.setHint("Your question here..");
				questionView.setHintTextColor(Color.parseColor("#FF3300"));
			}
			if (questiontype.getSelectedItem().toString() == "Select"
					|| books.getSelectedItem().toString() == "Select"
					|| chaptersList.getSelectedItem().toString() == "Select"
					|| marksAlloted.getText().toString().equals(null)
					|| marksAlloted.getText().toString().equals("0")
					|| marksAlloted.getText().toString().equals("00")
					|| marksAlloted.getText().toString().equals("000")
					||questionView.getText().toString().trim().length()==0 )
				Toast.makeText(
						getApplicationContext(),
						"Please make sure that you enter all the mandatory fields",
						Toast.LENGTH_LONG).show();
			if (Question.ORDERING_QUESTION.equalsIgnoreCase(questiontype
					.getSelectedItem().toString())
					|| Question.MULTIPLECHOICE_QUESTION
							.equalsIgnoreCase(questiontype.getSelectedItem()
									.toString())
					|| Question.MATCH_THE_FOLLOWING
							.equalsIgnoreCase(questiontype.getSelectedItem()
									.toString())) {
				// if(!flag ){
				if (questiontype.getSelectedItem().toString() == "Select"
						|| books.getSelectedItem().toString() == "Select"
						|| chaptersList.getSelectedItem().toString() == "Select"
						|| marksAlloted.getText().toString().equals(null)
						|| marksAlloted.getText().toString().equals("0")
						|| marksAlloted.getText().toString().equals("00")
						|| marksAlloted.getText().toString().equals("000"))/*
																		 * ||
																		 * !Question
																		 * .
																		 * FILL_IN_THE_BLANK_QUESTION
																		 * .
																		 * equalsIgnoreCase
																		 * (
																		 * questiontype
																		 * .
																		 * getSelectedItem
																		 * ().
																		 * toString
																		 * ()))
																		 */
					Toast.makeText(
							getApplicationContext(),
							"Please make sure that you enter all the mandatory fields",
							Toast.LENGTH_LONG).show();
				else if (serverQuestion.getMultiChoiceQnA().size() < 2) {
					Toast.makeText(getApplicationContext(),
							"Atleast 2 choices has to be added",
							Toast.LENGTH_SHORT).show();
				} else if (flag1) {
					Toast.makeText(getApplicationContext(),
							"Please make sure that you enter all the choices",
							Toast.LENGTH_LONG).show();
				}
				// }
			}
		}

	}
}