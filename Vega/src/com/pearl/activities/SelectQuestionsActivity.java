package com.pearl.activities;
/**
 * @author sahitya
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.examination.QuestionType;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.ExamParserHelper;
import com.pearl.ui.helpers.examination.QuestionListFromDB;
import com.pearl.ui.models.StatusType;
import com.pearl.user.teacher.examination.ExistingQuestion;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion;
import com.pearl.user.teacher.examination.questiontype.LongAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.MatchTheFollowingQuestion;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.user.teacher.examination.questiontype.OrderingQuestion;
import com.pearl.user.teacher.examination.questiontype.ShortAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectQuestionsActivity.
 */
public class SelectQuestionsActivity extends Activity {

/** The question type spinner. */
private Spinner gradeSpinner,subjectSpinner,questionTypeSpinner;

/** The questions list view. */
private ListView questionsListView;

/** The question_bank_menu. */
private Button import_questions,question_bank_menu;;

/** The hashmap. */
private HashMap<Integer, Question> hashmap=new HashMap<Integer, Question>();

/** The list. */
public static List<Question> list;

/** The _server requests. */
private ServerRequests _serverRequests;

/** The data from response. */
private List<List<String>> dataFromResponse;

/** The question_type_list. */
private List<String> gradeList, subjectList,question_type_list;

/** The app data. */
private ApplicationData appData;

/** The response. */
private BaseResponse response;

/** The tag. */
private String tag = null;

/** The handler. */
private Handler handler;

/** The i. */
private int i;

/** The selected type. */
private String  selectedGrade,selectedSubject,selectedType=null;

/** The fetch button. */
private Button fetchButton; 

/** The existing_questions_final. */
private  List<ExistingQuestion> existing_questions_final=null;

/** The help. */
private ImageView fetch, help;

/** The list_final. */
private  List<Question> list_final=new ArrayList<Question>();


    
    	
    	
    	/* (non-Javadoc)
	     * @see android.app.Activity#onCreate(android.os.Bundle)
	     */
	    @SuppressWarnings("unchecked")
	@Override
    	    protected void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.select_questions_layout);
    		questionsListView=(ListView)findViewById(R.id.select_questions_list);
    		gradeSpinner=(Spinner)findViewById(R.id.select_questions_grade);
    		subjectSpinner=(Spinner)findViewById(R.id.select_questions_subject);
    		questionTypeSpinner=(Spinner)findViewById(R.id.select_questions_type);
    		import_questions=(Button)findViewById(R.id.import_questions);
    		fetch=(ImageView)findViewById(R.id.fetch_questions);
    		question_bank_menu=(Button)findViewById(R.id.question_bank_menu);
    		_serverRequests = new ServerRequests(SelectQuestionsActivity.this);
    		appData = (ApplicationData) getApplication();
    		handler=new Handler(Looper.getMainLooper());
    		ShowProgressBar.showProgressBar("Please wait", requestToServerForGrades(ServerRequests.TEACHER_GRADE_AND_SUBJECT_LIST), SelectQuestionsActivity.this); ;
    		gradeSpinner.setEnabled(false);
    		questionTypeSpinner.setEnabled(false);
    		fetch.setVisibility(View.GONE);
    		help = (ImageView) findViewById(R.id.edit_question_help);
    		
    		question_bank_menu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				Intent i=new Intent(SelectQuestionsActivity.this, ActionBar.class);
				startActivity(i);
					
				}
			});
    		
    		import_questions.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					list=new ArrayList<Question>( hashmap.values());
					if(!list.isEmpty()){
					String id=getIntent().getExtras().getString(VegaConstants.TEST_ID); 
					Intent toSecondStep=new Intent(SelectQuestionsActivity.this,CreateTestStep2Activity.class);
					toSecondStep.putExtra(VegaConstants.TEST_ID, id);
					startActivity(toSecondStep);
			
					}else{
						Toast.makeText(SelectQuestionsActivity.this, "Download at least one question in order to proceed",Toast.LENGTH_LONG).show();
						
					}
					
				}
			});
    		
    		help.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showDialog();
				}
			});
    		
    		gradeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

    		    @Override
    		    public void onItemSelected(AdapterView<?> arg0, View view,
    			    int position, long id) {
    			  selectedGrade=gradeList.get(position).toString();
    				gradeSpinner.setEnabled(true);
    			
    		    }

    		    @Override
    		    public void onNothingSelected(AdapterView<?> arg0) {
    			
    		    }
    		});
    		
    		subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    		    @Override
    		    public void onItemSelected(AdapterView<?> arg0, View arg1,
    		            int arg2, long arg3) {
    		     selectedSubject=subjectList.get(arg2).toString();
    		     questionTypeSpinner.setEnabled(true);
    		        
    		    }
    		    @Override
    		    public void onNothingSelected(AdapterView<?> arg0) {
    		        
    		    }
    		});
    		
    		questionTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    		    @Override
    		    public void onItemSelected(AdapterView<?> arg0, View arg1,
    		            int arg2, long arg3) {
    		        selectedType=question_type_list.get(arg2).toString();
    		        fetch.setVisibility(View.VISIBLE);
    		        
    		    }
    		    @Override
    		    public void onNothingSelected(AdapterView<?> arg0) {
    		        
    		    }
    		});
    	    }
    
    	   
        /**
         * Fetch on click.
         *
         * @param v the v
         */
        public void fetchOnClick(View v) {
        	handler.post(new Runnable() {
    			
    			@Override
    			public void run() {
    				ShowProgressBar.showProgressBar("Fetching Questions  from server", requestToServerForQuestions(), SelectQuestionsActivity.this);
    				
    			}
    		});
    	
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
			final List<String> list = HelpParser.getHelpContent("quizzard_question_bank.txt", this);
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
    	   
   	    /**
   	     * Request to server for grades.
   	     *
   	     * @param url the url
   	     * @return the int
   	     */
   	    int requestToServerForGrades(String url) {

    			final StringParameter teacherId = new StringParameter("teacherId",
    				appData.getUserId());

    			final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
    			params.add(teacherId);

    			final PostRequestHandler request = new PostRequestHandler(
    				_serverRequests.getRequestURL(url, ""), params,
    				new DownloadCallback() {

    				    ObjectMapper objMapper = new ObjectMapper();

    				    @Override
    				    public void onSuccess(String downloadedString) {

    					try {
    					    response = objMapper.readValue(downloadedString,
    						    BaseResponse.class);

    					    if (response.getStatus().equals(StatusType.FAILURE)) {
    					    	handler.post(new Runnable() {
    								
    								@Override
    								public void run() {
    									Toast.makeText(getApplicationContext(),
    											"No Data Available", Toast.LENGTH_LONG).show();
    									
    								}
    							});
    						
    					    }
    					    getGradeAndSubjectFromResponse();
    					} catch (final Exception e) {
    					    Logger.error(tag, e);
    					}

    				    }

    				    @Override
    				    public void onProgressUpdate(int progressPercentage) {

    				    }

    				    @Override
    				    public void onFailure(String failureMessage) {

    				    }
    				});
    			request.request();
    			return 100;
    		    }

    		    /**
    		     * Gets the grade and subject from response.
    		     *
    		     * @return the grade and subject from response
    		     */
    		    @SuppressWarnings("unchecked")
    		    void getGradeAndSubjectFromResponse() {
    			dataFromResponse = (List<List<String>>) response.getData();
    			gradeList = dataFromResponse.get(0);
    			subjectList = dataFromResponse.get(1);
    			Log.i(tag, "MY GRADE IS" + gradeList + " and subject list is "
    				+ subjectList);
    			if(ShowProgressBar.progressBar != null)
    				ShowProgressBar.progressBar.dismiss();
    			handler.post(new Runnable() {
    			    
    			    @Override
    			    public void run() {
    				setGrade();
    			    }
    			});
    				
    			
    			 
    		    }
    		    
    		    /**
    		     * Sets the grade.
    		     */
    		    private void setGrade(){
    			ArrayAdapter<String> gradeAdapter=new ArrayAdapter<String>(SelectQuestionsActivity.this,android.R.layout.simple_dropdown_item_1line,gradeList);
    			gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    			gradeSpinner.setAdapter(gradeAdapter);
    			ArrayAdapter<String> subjectAdapter=new ArrayAdapter<String>(SelectQuestionsActivity.this,android.R.layout.simple_dropdown_item_1line,subjectList);
    			subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    			subjectSpinner.setAdapter(subjectAdapter);
    			question_type_list=QuestionType.getQuestionType();
    			ArrayAdapter<String> question_type_listAdapter=new ArrayAdapter<String>(SelectQuestionsActivity.this,android.R.layout.simple_dropdown_item_1line,question_type_list);
    			question_type_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    			questionTypeSpinner.setAdapter(question_type_listAdapter);
    		    }
    		    
    		    /**
    		     * Request to server for questions.
    		     *
    		     * @return the int
    		     */
    		    private int requestToServerForQuestions() {

    				final StringParameter grade = new StringParameter("grade",selectedGrade
    					);
    				final StringParameter subject = new StringParameter("subject",selectedSubject
    					);
    				
    				final StringParameter questionType = new StringParameter("questionType",selectedType
    					);
    				

    				final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
    				params.add(grade);
    				params.add(subject);
    				params.add(questionType);

    				final PostRequestHandler request = new PostRequestHandler(
    					_serverRequests.getRequestURL(ServerRequests.FETCH_FROM_QUESTION_BANK, ""), params,
    					new DownloadCallback() {

    					    ObjectMapper objMapper = new ObjectMapper();
    					    @SuppressWarnings("unchecked")
    					    @Override
    					    public void onSuccess(String downloadedString) {

    					try {
    					    BaseResponse br = objMapper.readValue(
    						    downloadedString, BaseResponse.class);
    					
    					    List<ExistingQuestion> eq_list=(List<ExistingQuestion>)br.getData();
    					 existing_questions_final=new ArrayList<ExistingQuestion>();
    					    for (Iterator<ExistingQuestion> iterator = eq_list.iterator(); iterator
    						    .hasNext();) {
    						 Object obj=iterator.next();
    						 ExistingQuestion existing_questons=new ExistingQuestion();
    						Set existingQuestionMapSet = ((LinkedHashMap<Object, Object>)obj).entrySet();				
    						for (Iterator iterator2 = existingQuestionMapSet.iterator(); iterator2.hasNext();) {
    						    Object obj1=iterator2.next();
    						    Entry<Object,Object> entry=(Entry<Object, Object>)obj1;
    						    existing_questons=parseExistingQuestion(entry, existing_questons);
    						  
    						}
    						existing_questions_final.add(existing_questons);
    					    }
    					
    					    if (SelectQuestionsActivity.this.response.getStatus()
    						    .equals(StatusType.FAILURE)) {
    						Toast.makeText(getApplicationContext(),
    							"No grades", Toast.LENGTH_LONG).show();
    					    }
    					
    					    Log.d(getClass().getSimpleName(),"MY FINAL EXISTING LIST IS"+existing_questions_final);
    					    handler.post(new Runnable() {

    						@Override
    						public void run() {
    						//    q_list=existing_questions_final;
    							QuestionsAdapter adapter = null;
    						    if(!existing_questions_final.isEmpty()){
    						    	 adapter=new QuestionsAdapter(SelectQuestionsActivity.this,android.R.layout.simple_selectable_list_item, existing_questions_final);
    					    		questionsListView.setAdapter(adapter);
    					    		
    					    		adapter.notifyDataSetChanged();
    					    		ShowProgressBar.progressBar.dismiss();
    						    }else
    						    {
    						    	questionsListView.setAdapter(null);
    						    	Toast.makeText(SelectQuestionsActivity.this, "No Question for this criteria", Toast.LENGTH_LONG).show();
    						    ShowProgressBar.progressBar.dismiss();
    						    }
    						}
    					    });

    					} catch (final Exception e) {
    						    Logger.error(tag, e);
    						}

    					    }

    					    @Override
    					    public void onProgressUpdate(int progressPercentage) {

    					    }

    					    @Override
    					    public void onFailure(String failureMessage) {

    					    }
    					});
    				request.request();
    				return 100;
    			    }
    		    
    		    /**
    		     * Parses the existing question.
    		     *
    		     * @param entry the entry
    		     * @param existing_questons the existing_questons
    		     * @return the existing question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private ExistingQuestion parseExistingQuestion(Entry<Object, Object> entry,ExistingQuestion existing_questons ) throws JsonParseException, JsonMappingException, IOException, JSONException
    		    {
    			  if(entry.getKey().toString().equalsIgnoreCase("id"))
    				    existing_questons.setId(entry.getValue().toString());
    			    else if(entry.getKey().toString().equalsIgnoreCase("grade"))
    				    existing_questons.setGrade(entry.getValue().toString());
    			    else if(entry.getKey().toString().equalsIgnoreCase("section"))
    				    existing_questons.setSection(entry.getValue().toString());
    			    else if(entry.getKey().toString().equalsIgnoreCase("subject"))
    				    existing_questons.setSubject(entry.getValue().toString());
    			    else if(entry.getKey().toString().equalsIgnoreCase("chapter"))
    				    existing_questons.setChapter(entry.getValue().toString());
    			    else if(entry.getKey().toString().equalsIgnoreCase("questionType"))
    				    existing_questons.setQuestionType(entry.getValue().toString());
    			    else if(entry.getKey().toString().equalsIgnoreCase("marks"))
    				    existing_questons.setMarks(Integer.parseInt(entry.getValue().toString()));
    			    else if(entry.getKey().toString().equalsIgnoreCase("question")) {
    			    	    Object list=entry.getValue();
    			    	existing_questons= checkForQuestionType(existing_questons,list);
    			    	}
    			    else if(entry.getKey().toString().equalsIgnoreCase("bookName"))
    				    existing_questons.setBookName(entry.getValue().toString());
    			  return existing_questons;
    		    }
    		    
    		    /**
    		     * Check for question type.
    		     *
    		     * @param existing_questons the existing_questons
    		     * @param list the list
    		     * @return the existing question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private ExistingQuestion checkForQuestionType(ExistingQuestion existing_questons, Object list) throws JsonParseException, JsonMappingException, IOException, JSONException 
    		    {
    		 	   if(selectedType.equalsIgnoreCase(QuestionType.TRUE_OR_FALSE_QUESTION.toString()))
    			    	existing_questons.setQuestion(parseQuestionObjectToTFQ(list));
    		 	   else if(selectedType.equalsIgnoreCase(QuestionType.FILL_IN_THE_BLANK_QUESTION.toString()))
    		 	      existing_questons.setQuestion(parseQuestionObjectToFIB(list));
    		 	  else if(selectedType.equalsIgnoreCase(QuestionType.MULTIPLECHOICE_QUESTION.toString()))
    		 	      existing_questons.setQuestion(parseQuestionObjectToMCQ(list));
    		 	 else if(selectedType.equalsIgnoreCase(QuestionType.SHORT_ANSWER_QUESTION.toString()))
    			      existing_questons.setQuestion(parseQuestionObjectToSAQ(list));
    		 	else if(selectedType.equalsIgnoreCase(QuestionType.LONG_ANSWER_QUESTION.toString()))
    			      existing_questons.setQuestion(parseQuestionObjectToLAQ(list));
    		 	else if(selectedType.equalsIgnoreCase(QuestionType.ORDERING_QUESTION.toString()))
    			      existing_questons.setQuestion(parseQuestionObjectToOQ(list));
    		 	else if(selectedType.equalsIgnoreCase(QuestionType.MATCH_THE_FOLLOWING_QUESTION.toString()))
    			      existing_questons.setQuestion(parseQuestionObjectToMTF(list));
    		 	   
    		 	  return existing_questons; 
    		    }
    		    
    		    /**
    		     * Parses the question object to tfq.
    		     *
    		     * @param list the list
    		     * @return the question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private Question parseQuestionObjectToTFQ(Object list) throws JsonParseException, JsonMappingException, IOException, JSONException {
    			 
    		    	ObjectMapper objMapper=new ObjectMapper();
    				String json=   objMapper.writeValueAsString(list);
    				TrueOrFalseQuestion qtn=ExamParserHelper.convertToTrueOrFalseQuestion(new JSONObject(json));
    				    Log.i(getClass().getSimpleName(), "MY TFQ QUESTIONS A ARE"+qtn);
    				    list_final.add(qtn);
    				    return qtn;
    		    }
    		    
    		    /**
    		     * Parses the question object to fib.
    		     *
    		     * @param list the list
    		     * @return the question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private Question parseQuestionObjectToFIB(Object list) throws JsonParseException, JsonMappingException, IOException, JSONException {
    			 
    			    ObjectMapper objMapper=new ObjectMapper();
    				String json=   objMapper.writeValueAsString(list);
    			
    				    FillInTheBlankQuestion queston_obj=	ExamParserHelper.convertToFillInTheBlankQuestion(new JSONObject(json));
    				    Log.i(getClass().getSimpleName(), "MY FIB QUESTIONS A ARE"+queston_obj);
    				    list_final.add(queston_obj);
    				    return queston_obj;
    		}
    		    
    		    /**
    		     * Parses the question object to mcq.
    		     *
    		     * @param list the list
    		     * @return the question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private Question parseQuestionObjectToMCQ(Object list) throws JsonParseException, JsonMappingException, IOException, JSONException {
    			 
    			    ObjectMapper objMapper=new ObjectMapper();
    				String json=   objMapper.writeValueAsString(list);
    			
    				    MultipleChoiceQuestion queston_obj=	ExamParserHelper.convertToMultipleChoiceQuestion(new JSONObject(json));
    				    Log.i(getClass().getSimpleName(), "MY MCQ QUESTIONS A ARE"+queston_obj);
    				    list_final.add(queston_obj);
    				    return queston_obj;
    		}
    		    
    		    /**
    		     * Parses the question object to saq.
    		     *
    		     * @param list the list
    		     * @return the question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private Question parseQuestionObjectToSAQ(Object list) throws JsonParseException, JsonMappingException, IOException, JSONException {
    			 
    			    ObjectMapper objMapper=new ObjectMapper();
    				String json=     objMapper.writeValueAsString(list);
    				
    				    ShortAnswerQuestion queston_obj=ExamParserHelper.convertToShortAnswerQuestion(new JSONObject(json));
    				    Log.i(getClass().getSimpleName(), "MY SAQ QUESTIONS A ARE"+queston_obj);
    				    list_final.add(queston_obj);
    				    return queston_obj;
    		}
    		    
    		    /**
    		     * Parses the question object to laq.
    		     *
    		     * @param list the list
    		     * @return the question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private Question parseQuestionObjectToLAQ(Object list) throws JsonParseException, JsonMappingException, IOException, JSONException {
    			 
    			    ObjectMapper objMapper=new ObjectMapper();
    				String json=    objMapper.writeValueAsString(list);
    			
    				    LongAnswerQuestion queston_obj=ExamParserHelper.convertToLongAnswerQuestion(new JSONObject(json));
    				    Log.i(getClass().getSimpleName(), "MY LAQ QUESTIONS A ARE"+queston_obj);
    				    list_final.add(queston_obj);
    				    return queston_obj;
    		}
    		    
    		    /**
    		     * Parses the question object to oq.
    		     *
    		     * @param list the list
    		     * @return the question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private Question parseQuestionObjectToOQ(Object list) throws JsonParseException, JsonMappingException, IOException, JSONException {
    			 
    			    ObjectMapper objMapper=new ObjectMapper();
    				String json=      objMapper.writeValueAsString(list);
    				    OrderingQuestion queston_obj=ExamParserHelper.convertToOrderingQuestion(new JSONObject(json));
    				    Log.i(getClass().getSimpleName(), "MY OQ QUESTIONS A ARE"+queston_obj);
    				    list_final.add(queston_obj);
    				    return queston_obj;
    		}
    		    
    		    /**
    		     * Parses the question object to mtf.
    		     *
    		     * @param list the list
    		     * @return the question
    		     * @throws JsonParseException the json parse exception
    		     * @throws JsonMappingException the json mapping exception
    		     * @throws IOException Signals that an I/O exception has occurred.
    		     * @throws JSONException the jSON exception
    		     */
    		    private Question parseQuestionObjectToMTF(Object list) throws JsonParseException, JsonMappingException, IOException, JSONException {
    			 
    			    ObjectMapper objMapper=new ObjectMapper();
    				String json=     objMapper.writeValueAsString(list);
    				    MatchTheFollowingQuestion queston_obj=ExamParserHelper.convertToOMatchTheFollowingQuestion(new JSONObject(json));
    				    Log.i(getClass().getSimpleName(), "MY TFQ QUESTIONS A ARE"+queston_obj);
    				    list_final.add(queston_obj);
    				    return queston_obj;
    		}
    		    
    	/**
	     * The Class QuestionsAdapter.
	     */
	    private class QuestionsAdapter extends ArrayAdapter<ExistingQuestion>{
    	    
    	    /** The list. */
    	    List<ExistingQuestion> list=new ArrayList<ExistingQuestion>();
    	    
    	    /** The check status. */
    	    boolean[] checkStatus;
    	    
    	    /**
    	     * Instantiates a new questions adapter.
    	     *
    	     * @param context the context
    	     * @param textViewResourceId the text view resource id
    	     * @param objects the objects
    	     */
    	    public QuestionsAdapter(Context context, int textViewResourceId,
		    List<ExistingQuestion> objects) {
		super(context, textViewResourceId, objects);
		list=objects;
		checkStatus=new boolean[list.size()];
	    }
    	 
	    /* (non-Javadoc)
    	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
    	 */
    	@Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	ViewHolder holder=null;
	    	View view=convertView;
		//if(view==null) { 
			  holder=new ViewHolder();
			    LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    view= convertView=inflater.inflate(R.layout.select_questions_saq,null);
			  
			    holder.layout=(LinearLayout)view.findViewById(R.id.db_question_layout);
			     holder.checkbox=(CheckBox)view.findViewById(R.id.question_checkBox);
			     view.setTag(holder);
			     String selectedType=  list.get(position).getQuestionType();
			       QuestionListFromDB questionUI=new QuestionListFromDB();
		 	   if(selectedType.equalsIgnoreCase(QuestionType.TRUE_OR_FALSE_QUESTION.toString()))
			    	questionUI.buildTrueOrFalse(list.get(position).getQuestion(), SelectQuestionsActivity.this,  holder.layout);
		 	   else if(selectedType.equalsIgnoreCase(QuestionType.FILL_IN_THE_BLANK_QUESTION.toString()))
		 	      questionUI.buildFillInTheBlanksQuestion(list.get(position).getQuestion(), SelectQuestionsActivity.this,  holder.layout);
		 	  else if(selectedType.equalsIgnoreCase(QuestionType.MULTIPLECHOICE_QUESTION.toString()))
		 	     questionUI.buildTrueOrFalse(list.get(position).getQuestion(), SelectQuestionsActivity.this,  holder.layout);
		 	 else if(selectedType.equalsIgnoreCase(QuestionType.SHORT_ANSWER_QUESTION.toString()))
		 	    questionUI.buildShortAnswerQuestion(list.get(position).getQuestion(), SelectQuestionsActivity.this,  holder.layout);
		 	else if(selectedType.equalsIgnoreCase(QuestionType.LONG_ANSWER_QUESTION.toString()))
		 	   questionUI.buildLongAnswerQuestion(list.get(position).getQuestion(), SelectQuestionsActivity.this,  holder.layout);
		 	else if(selectedType.equalsIgnoreCase(QuestionType.ORDERING_QUESTION.toString()))
		 	   questionUI.buildOrderingQuestion(list.get(position).getQuestion(), SelectQuestionsActivity.this,  holder.layout);
		 	else if(selectedType.equalsIgnoreCase(QuestionType.MATCH_THE_FOLLOWING_QUESTION.toString()))
		 	   questionUI.buildMatchtheFollowing(list.get(position).getQuestion(), SelectQuestionsActivity.this,  holder.layout);
	/*	}else{
			holder=(ViewHolder)view.getTag();
		}
	*/		     
		
		holder.checkbox.setChecked(checkStatus[position]);
		
		holder.checkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((CheckBox)v).isChecked()){
					checkStatus[position]=true;
					hashmap.put(position, list.get(position).getQuestion());
					if(import_questions.getVisibility()==View.GONE){
						import_questions.setVisibility(View.VISIBLE);
					}

				}else{
					checkStatus[position]=false;
					hashmap.remove(position);
				}
			}
		});
					     
		 	   return view;
	    }
    	}
    	
	/**
	 * The Class ViewHolder.
	 */
	private class ViewHolder {
		
		/** The checkbox. */
		private CheckBox checkbox;
		
		/** The layout. */
		private LinearLayout layout;
    	}
    	}
    	
    
