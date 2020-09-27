package com.pearl.ui.helpers.examination;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.afree.chart.block.CenterArrangement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.activities.CreateTestStep2Activity;
import com.pearl.examination.QuestionType;
import com.pearl.logger.Logger;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.ServerQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateQuestionUI.
 */
public class CreateQuestionUI {
    
    /** The createquestions ui. */
    private static CreateQuestionUI createquestionsUI = null;
    
    /** The server question. */
    private ServerQuestion serverQuestion;

    /** The spinner array. */
    private List<String> spinnerArray;
    
    /** The spinner array adapter. */
    private ArrayAdapter<String> spinnerArrayAdapter;

    /** The table layout. */
    private TableLayout tableLayout;
    
    /** The add choice. */
    private Button addChoice;
    
    /** The radio group. */
    private RadioGroup radioGroup=null;
    
    /** The radio. */
    private RadioButton radio=null;
    
    /** The Constant tag. */
    private static final String tag = "CreateQuestionsUI";
    
    /** The choice. */
    private String choice;
    
    /** The count. */
    private int count;
    
    /** The _question1. */
    private static Question _question1;
    
    /** The position removed. */
    private int positionRemoved = -1;

    /**
     * Gets the single instance of CreateQuestionUI.
     *
     * @return single instance of CreateQuestionUI
     */
    public static CreateQuestionUI getInstance() {
	createquestionsUI = new CreateQuestionUI();
	return createquestionsUI;
    }

    /**
     * Builds the.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     */
    public void build( ServerQuestion question, CreateTestStep2Activity activity,	LinearLayout layout) {
	count=0;
	serverQuestion=new ServerQuestion();
	Logger.info("QuestionsUI",
		"Building question Type " + question.getType());
	if (layout == null) {
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.HORIZONTAL);
	} else {
	    new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);

	    layout.setOrientation(LinearLayout.HORIZONTAL);
	    layout.setGravity(0);
	}

	if (Question.SHORT_ANSWER_QUESTION.equalsIgnoreCase(question.getType().trim())
		) {
	     buildShortAnswerQuestion(question, activity, layout);
	} else if(Question.LONG_ANSWER_QUESTION.equalsIgnoreCase(question.getType().trim())){
	    buildLongAnswerQuestion(question, activity, layout);
	}else if (Question.TRUE_OR_FALSE_QUESTION.equalsIgnoreCase(question.getType().trim())) {
	    buildTrueOrFalse(question, activity, layout);
	} else if (Question.MULTIPLECHOICE_QUESTION.equalsIgnoreCase(question.getType().trim())) {
		
	   buildMultipleChoiceQuestion(question, activity, layout);
	} else if (Question.ORDERING_QUESTION.equalsIgnoreCase(question.getType().trim())) {
	   buildOrderingQuestion(question, activity, layout);
	} else if (Question.MATCH_THE_FOLLOWING.equalsIgnoreCase(question.getType().trim())) {
	   buildMatchtheFollowing(question, activity, layout);
	} else {
	    Logger.error("QuestionsUI", "***Failed to Build question Type "
		    + question.getType());
	}
	
    }


    /**
     * Builds the ordering question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    private LinearLayout buildOrderingQuestion(final ServerQuestion question,
	    final CreateTestStep2Activity activity, LinearLayout layout) {
    	
    	final double screenInches = screenSizes(activity);
	// Initialize default parameters
	LinearLayout.LayoutParams defaultParams;
	/*defaultParams = new LinearLayout.LayoutParams(860,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	defaultParams.setMargins(5, 10, 60, 10);*/

	// Create widgets view
	tableLayout = new TableLayout(activity);
	tableLayout.setScrollContainer(true);

	addChoice = new Button(activity);
	addChoice.setText("Add");
	addChoice.setGravity(Gravity.CENTER);
	addChoice.setBackgroundResource(R.drawable.tc_button_background);
	addChoice.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT));

	question.setHasMultipleAnswers(true);
	
	
	
	if(null!=question.getMultiChoiceQnA() ||!question.getMultiChoiceQnA().isEmpty()){
		
		for(int i=0;i<question.getMultiChoiceQnA().size();i++){

			// TODO Auto-generated method stub
			if(tableLayout.getChildCount() > 3){
			    addChoice.setVisibility(View.GONE);
			} else {
			    addChoice.setVisibility(View.VISIBLE);
			}
			
			final TableRow tableRow = new TableRow(activity);
			count=tableLayout.getChildCount()>0?tableLayout.getChildCount():0;
			tableRow.setId(count);
			final EditText position = new EditText(activity);
			position.setWidth(70);
			position.setHeight(35);
			position.setTextColor(Color.parseColor("#000000"));
			position.setHint("order");
			position.setText((question.getMultiChoiceQnA().get(i).getCheckedValue()==null)?i+1+"":question.getMultiChoiceQnA().get(i).getCheckedValue());
			position.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
			position.setInputType(InputType.TYPE_CLASS_NUMBER);
			final EditText ansDesc = new EditText(activity);
			ansDesc.setTextColor(Color.parseColor("#000000"));
			ansDesc.setWidth(685);
			ansDesc.setHeight(35);
			ansDesc.setHint("Answer text..");
			ansDesc.setText(question.getMultiChoiceQnA().get(i).getAnswerDes());
			ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
			serverQuestion.getMultiChoiceQnA().get(i).setPosition(question.getMultiChoiceQnA().get(i).getPosition());
			  serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(question.getMultiChoiceQnA().get(i).getAnswerDes());
			  serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(question.getMultiChoiceQnA().get(i).getCheckedValue());
			  
			  if(screenInches <=8 && screenInches>5){
				  position.setWidth(70);
				  position.setHeight(35);
				  ansDesc.setWidth(585);
				  ansDesc.setHeight(35);
			  }else if(screenInches<=5){
				  position.setWidth(50);
				  position.setHeight(15);
				  position.setTextSize(12);
				  ansDesc.setWidth(275);
				  ansDesc.setHeight(15);
				  ansDesc.setTextSize(12);
			  }

			ansDesc.addTextChangedListener(new TextWatcher() {
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    }
			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count,
				    int after) {
			    }
			    @Override
			    public void afterTextChanged(Editable s) {
				Logger.warn(tag, "ORD - ans desc:"+s.toString());
				if(s.toString().length() == 100){
					Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
				}
				if(s.toString().equals(""))
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(null);
				else
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(s.toString().trim());
			    }
			});
			
			position.addTextChangedListener(new TextWatcher() {

			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    }

			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count,
				    int after) {
			    }

			    @Override
			    public void afterTextChanged(Editable s) {
				Logger.warn(tag, "ORD - position:"+s.toString());
				if(s.toString().equals(""))
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(null);
				else
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(s.toString().trim());
			    }
			});
			final ImageView delete = new ImageView(activity);
			delete.setImageResource(R.drawable.delete_book);
			delete.setOnClickListener(new OnClickListener() {
			    @Override
			    public void onClick(View v) {
				// TODO Auto-generated method stub
				tableLayout.removeView(tableRow);
				if (serverQuestion.getMultiChoiceQnA() != null && serverQuestion.getMultiChoiceQnA().size()>0){
				    Logger.info(tag, "ORD - position to delete is:"+tableLayout.getChildCount());
				    final int id=tableRow.getId();
				    serverQuestion.getMultiChoiceQnA().remove(id);
				    Logger.info(tag,serverQuestion.getMultiChoiceQnA()+"");
				}
				if(tableLayout.getChildCount() > 0){
				    for(int i=0;i<tableLayout.getChildCount();i++){
					final TableRow tr=(TableRow)tableLayout.getChildAt(i);
					tr.setId(i);
					final EditText checkValue=(EditText)tr.getChildAt(0);
					if(checkValue.getText().toString().equals(""))
					    serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(null);
					else
					    serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(checkValue.getText().toString().trim());

					final EditText answerDesc=(EditText)tr.getChildAt(1);
					if(answerDesc.getText().toString().equals(""))
					    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
					else 
					    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(answerDesc.getText().toString().trim());
					serverQuestion.getMultiChoiceQnA().get(i).setPosition(i+1);
				    }
				}
				addChoice.setVisibility(View.VISIBLE);
			    }
			});
			TableRow.LayoutParams fieldparams = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			fieldparams.setMargins(20, 5, 0, 0);
			TableRow.LayoutParams fieldparamsbutt = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			fieldparamsbutt.setMargins(25, 0, 0, 0);
			if(screenInches < 5){
				fieldparams.setMargins(20, 5, 0, 0);
				fieldparamsbutt.setMargins(5, 0, 0, 0);
			}
			tableRow.addView(position, fieldparamsbutt);
			tableRow.addView(ansDesc);
			tableRow.addView(delete, fieldparams);
			tableLayout.addView(tableRow);
		    
		}
		
	}
	addChoice.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		if(tableLayout.getChildCount() > 3){
		    addChoice.setVisibility(View.GONE);
		} else {
		    addChoice.setVisibility(View.VISIBLE);
		}
		
		final TableRow tableRow = new TableRow(activity);
		count=tableLayout.getChildCount()>0?tableLayout.getChildCount():0;
		tableRow.setId(count);
		final EditText position = new EditText(activity);
		position.setWidth(70);
		position.setHeight(35);
		position.setTextColor(Color.parseColor("#000000"));
		position.setHint("Order");
		position.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
		position.setInputType(InputType.TYPE_CLASS_NUMBER);
		final EditText ansDesc = new EditText(activity);
		ansDesc.setTextColor(Color.parseColor("#000000"));
		ansDesc.setWidth(685);
		ansDesc.setHeight(35);
		ansDesc.setHint("Answer text..");
		ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setPosition(tableRow.getId()+1);
		if(screenInches <=8 && screenInches>5){
			  position.setWidth(70);
			  position.setHeight(35);
			  ansDesc.setWidth(585);
			  ansDesc.setHeight(35);
		  }else if(screenInches<=5){
			  position.setWidth(50);
			  position.setHeight(15);
			  position.setTextSize(12);
			  ansDesc.setWidth(275);
			  ansDesc.setHeight(15);
			  ansDesc.setTextSize(12);
		  }
		

		ansDesc.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
		    }
		    @Override
		    public void afterTextChanged(Editable s) {
			Logger.warn(tag, "ORD - ans desc:"+s.toString());
			if(s.toString().length() > 100){
				Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
			}
			if(s.toString().equals(""))
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(null);
			else
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(s.toString().trim());
		    }
		});
		
		position.addTextChangedListener(new TextWatcher() {

		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
		    }

		    @Override
		    public void afterTextChanged(Editable s) {
			Logger.warn(tag, "ORD - position:"+s.toString());
			if(s.toString().equals(""))
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(null);
			else
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(s.toString().trim());
		    }
		});
		final ImageView delete = new ImageView(activity);
		delete.setImageResource(R.drawable.delete_book);
		delete.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			// TODO Auto-generated method stub
			tableLayout.removeView(tableRow);
			if (serverQuestion.getMultiChoiceQnA() != null && serverQuestion.getMultiChoiceQnA().size()>0){
			    Logger.info(tag, "ORD - position to delete is:"+tableLayout.getChildCount());
			    final int id=tableRow.getId();
			    serverQuestion.getMultiChoiceQnA().remove(id);
			    Logger.info(tag,serverQuestion.getMultiChoiceQnA()+"");
			}
			if(tableLayout.getChildCount() > 0){
			    for(int i=0;i<tableLayout.getChildCount();i++){
				final TableRow tr=(TableRow)tableLayout.getChildAt(i);
				tr.setId(i);
				final EditText checkValue=(EditText)tr.getChildAt(0);
				if(checkValue.getText().toString().equals(""))
				    serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(null);
				else
				    serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(checkValue.getText().toString().trim());

				final EditText answerDesc=(EditText)tr.getChildAt(1);
				if(answerDesc.getText().toString().equals(""))
				    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
				else 
				    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(answerDesc.getText().toString().trim());
				serverQuestion.getMultiChoiceQnA().get(i).setPosition(i+1);
			    }
			}
			addChoice.setVisibility(View.VISIBLE);
		    }
		});
		TableRow.LayoutParams fieldparams = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		fieldparams.setMargins(25, 5, 0, 0);
		TableRow.LayoutParams fieldparamsbutt = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		fieldparamsbutt.setMargins(25, 0, 0, 0);
		if(screenInches < 5){
			fieldparams.setMargins(20, 5, 0, 0);
			fieldparamsbutt.setMargins(5, 0, 0, 0);
		}
		tableRow.addView(position, fieldparamsbutt);
		tableRow.addView(ansDesc);
		tableRow.addView(delete, fieldparams);
		tableLayout.addView(tableRow);
	    }
	});
	question.setMultiChoiceQnA(serverQuestion.getMultiChoiceQnA());
	final ScrollView sv = new ScrollView(activity);
	if(screenInches <= 5){
		  sv.addView(tableLayout);
		  defaultParams = new LinearLayout.LayoutParams(400,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		  defaultParams.setMargins(15, 10, 15, 10);
		  layout.addView( sv, defaultParams);
	  }else{
		  defaultParams = new LinearLayout.LayoutParams(860,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		  defaultParams.setMargins(5, 10, 50, 10);
		  layout.addView(tableLayout, defaultParams);
	  }
	layout.addView(addChoice);
	return layout;
    }

  /**
   * Builds the multiple choice question.
   *
   * @param question the question
   * @param activity the activity
   * @param layout the layout
   * @return the linear layout
   */
  synchronized  private LinearLayout buildMultipleChoiceQuestion(final ServerQuestion question,
	    final CreateTestStep2Activity activity, LinearLayout layout) {
	  
	  LinearLayout.LayoutParams defaultParams=null;
  
	  final double screenInches=screenSizes(activity);
	final LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	
	final LinearLayout.LayoutParams layoutParams1 = new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	
	final RadioButton yesrb = new RadioButton(activity);
	final RadioButton norb = new RadioButton(activity);
	yesrb.setText("YES");
	norb.setText("NO");
	yesrb.setEnabled(false);
	norb.setEnabled(false);
	yesrb.setTextColor(Color.parseColor("#000000"));
	norb.setTextColor(Color.parseColor("#000000"));
	norb.setChecked(true);
	choice = "no";
	
	final List<String> objects = new ArrayList<String>();
	radioGroup=new RadioGroup(activity);
	tableLayout = new TableLayout(activity);
	addChoice = new Button(activity);
	addChoice.setText("Add");
	addChoice.setTextSize(15);
	addChoice.setBackgroundResource(R.drawable.tc_button_background);
	addChoice.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	new TableRow(activity);
	
	
	yesrb.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			objects.removeAll(objects);
			serverQuestion.getMultiChoiceQnA().removeAll(serverQuestion.getMultiChoiceQnA());
			if(radioGroup !=null)
			    radioGroup.removeAllViews();
			tableLayout.removeAllViews();
			addChoice.setVisibility(View.VISIBLE);
			choice = "yes";
			norb.setChecked(false);
		}
	});
    
	norb.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			objects.removeAll(objects);
			serverQuestion.getMultiChoiceQnA().removeAll(serverQuestion.getMultiChoiceQnA());
			if(radioGroup !=null)
			    radioGroup.removeAllViews();
			tableLayout.removeAllViews();
			addChoice.setVisibility(View.VISIBLE);
			choice = "no";
		}
	});
	if(null!=question.getMultiChoiceQnA() ||!question.getMultiChoiceQnA().isEmpty()){
	for(int  i=0;i<question.getMultiChoiceQnA().size();i++){
	Log.i(tag,"----------------------------------------------"+question.getMultiChoiceQnA().get(i).getAnswerDes());

	if(tableLayout.getChildCount() > 3){
	    addChoice.setVisibility(View.INVISIBLE);
	} else {
		Log.i(tag,"----------------------------------------------"+question.getType());
	    addChoice.setVisibility(View.VISIBLE);
	}
	final TableRow tableRow = new TableRow(activity);
	
	count=tableLayout.getChildCount()>0?tableLayout.getChildCount():0;
	tableRow.setId(count);
	serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setPosition(tableRow.getId()+1);
	if(choice == "yes"){
		
	}else{

	    radio=new RadioButton(activity);
	    radio.setId(tableRow.getId());
	    question.setHasMultipleAnswers(false);
	    if(radio.getId()==Integer.parseInt((question.getCheckValues()==null)?"0":question.getCheckValues())){
	    	radio.setChecked(true);
	    }
	   
	    serverQuestion.setCheckValues(question.getCheckValues());
	  
	    radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				RadioButton radio = null;
				if(radioGroup!=null && radioGroup.getChildCount() > 0){
				    for(int i=0;i<radioGroup.getChildCount();i++){
					radio=(RadioButton) radioGroup.getChildAt(i);
					if( serverQuestion.getCheckValues() != null)
						if(Integer.parseInt(serverQuestion.getCheckValues()) == (radio.getId() + 1) ){
							/*radio.setChecked(false);
							radio.clearFocus();*/
							break;
						}
				    }
				}
				Logger.warn(tag, "tabel row id after adding is: "+(tableRow.getId()+1));
				if(isChecked){
					//radio.setChecked(true);
					serverQuestion.setCheckValues(String.valueOf(tableRow.getId()+1));
			    }
			    question.setCheckValues(serverQuestion.getCheckValues());
			}
		    });
	 
	    final RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(activity, null);
	    params.setMargins(0, 0, 0, 0);
	    if(radioGroup!=null)
		radio.setLayoutParams(params);
	    radioGroup.addView(radio);
	}
	final EditText ansDesc = new EditText(activity);
	ansDesc.setId(tableRow.getId()+1);
	ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
	
	ansDesc.setText(question.getMultiChoiceQnA().get(i).getAnswerDes());
	serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(question.getMultiChoiceQnA().get(i).getAnswerDes());
	ansDesc.setTextColor(Color.parseColor("#000000"));
	ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	ansDesc.addTextChangedListener(new TextWatcher() {
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    }
	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
	    }
	    @Override
	    public void afterTextChanged(Editable s) {
		if(s.toString().length() == 100)
			Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
	    	if(s.toString().equals(""))
		    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(null);
		else
		    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(s.toString().trim());
	    }
	});
	final ImageView delete = new ImageView(activity);
	delete.setImageResource(R.drawable.delete_book);
	delete.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method sdfstub
		tableLayout.removeView(tableRow);
		serverQuestion.setCheckValues(null);
		question.setCheckValues(null);
		if(radioGroup!=null && radioGroup.getChildCount() > 0){
		    for(int i=0;i<radioGroup.getChildCount();i++){
			final RadioButton radio=(RadioButton) radioGroup.getChildAt(i);
			if(radio.getId()==tableRow.getId())
			    radioGroup.removeView(radio);
		    }
		}
		if (serverQuestion.getMultiChoiceQnA() != null && serverQuestion.getMultiChoiceQnA().size()>0){
		    Logger.info(tag, "position to remove is:"+tableLayout.getChildCount());
		    serverQuestion.getMultiChoiceQnA().remove(tableRow.getId());
		}
		if(tableLayout.getChildCount()>0){
		    for(int i=0;i<tableLayout.getChildCount();i++){
			final TableRow tr=(TableRow)tableLayout.getChildAt(i);
			tr.setId(i);
			if(choice == "yes"){
			    final CheckBox check=(CheckBox)tr.getChildAt(0);
			    serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(check.isChecked()?"on":null);
			    final EditText ed1=(EditText)tr.getChildAt(1);
			    if(ed1.getText().toString().equals(""))
				serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
			    else
				serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(ed1.getText().toString().trim());
			}else{
			    if(radioGroup!=null && radioGroup.getChildCount()>0){
				final RadioButton radio=(RadioButton)radioGroup.getChildAt(i);
				radio.setId(i);
				if(radio.isChecked()){
				    /*   Question mul=(Question)question;
							   MultipleChoiceQuestion mulQ=(MultipleChoiceQuestion)mul;
							   mulQ.getChoices().get(i).setTeacherSelected(true);
							  serverQuestion=((ServerQuestion)((Question)mulQ));*/
				    serverQuestion.setCheckValues(String.valueOf(i+1));
				    question.setCheckValues(serverQuestion.getCheckValues());
				}
				final EditText ed1=(EditText)tr.getChildAt(0);
				if(ed1.getText().toString().equals(""))
				    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
				else	
				    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(ed1.getText().toString().trim());
			    }
			}
			serverQuestion.getMultiChoiceQnA().get(i).setPosition(i+1);
		    }
		}
		addChoice.setVisibility(View.VISIBLE);
	    }
	});
	TableRow.LayoutParams fieldparams = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	fieldparams.setMargins(20, 5, 0, 0);
	TableLayout.LayoutParams fieldparams2 ;
	fieldparams2= new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	if(screenInches>8){
		ansDesc.setWidth(710);
		ansDesc.setHeight(35);
		fieldparams2= new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	}else if(screenInches>5 && screenInches<=8){
		ansDesc.setWidth(710);
		ansDesc.setHeight(25);
		ansDesc.setTextSize(15);
		fieldparams2= new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	}else{
		ansDesc.setWidth(310);
		ansDesc.setHeight(20);
		ansDesc.setTextSize(15);
		fieldparams2= new TableLayout.LayoutParams(300, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	}
	tableRow.addView(ansDesc);
	tableRow.addView(delete, fieldparams);
	tableLayout.addView(tableRow, fieldparams2);
	count++;
    
	}
	}
	addChoice.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if(tableLayout.getChildCount() > 3){
		    addChoice.setVisibility(View.INVISIBLE);
		} else {
			Log.i(tag,"----------------------------------------------"+question.getType());
		    addChoice.setVisibility(View.VISIBLE);
		}
		final TableRow tableRow = new TableRow(activity);
		
		count=tableLayout.getChildCount()>0?tableLayout.getChildCount():0;
		tableRow.setId(count);
		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setPosition(tableRow.getId()+1);
		if(choice == "yes"){
		    question.setHasMultipleAnswers(true);
		    final CheckBox chckbox=new CheckBox(activity);
		    chckbox.setId(tableRow.getId());
		    chckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			    if(isChecked){
				serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue("on");		
				question.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue("on");
			    }
			    else{
				serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(null);
				question.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(null);
			    }
			}
		    });
		    tableRow.addView(chckbox);
		}else{
			
		    radio=new RadioButton(activity);
		    radio.setId(tableRow.getId());
		    question.setHasMultipleAnswers(false);
		    radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				RadioButton radio = null;
				if(radioGroup!=null && radioGroup.getChildCount() > 0){
				    for(int i=0;i<radioGroup.getChildCount();i++){
					radio=(RadioButton) radioGroup.getChildAt(i);
					if( serverQuestion.getCheckValues() != null)
						if(Integer.parseInt(serverQuestion.getCheckValues()) == (radio.getId() + 1) ){
							radio.setChecked(false);
							break;
						}
				    }
				}
				if(isChecked){
					//radio.setChecked(true);
					serverQuestion.setCheckValues(String.valueOf(tableRow.getId()+1));
			    }
			    question.setCheckValues(serverQuestion.getCheckValues());
			}
		    });
		    final RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(activity, null);
		    params.setMargins(0, 0, 0, 0);
		    if(radioGroup!=null)
			radio.setLayoutParams(params);
		    radioGroup.addView(radio);
		}
		final EditText ansDesc = new EditText(activity);
		ansDesc.setId(tableRow.getId()+1);
		ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		ansDesc.setHint("Answer text..");
		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(question.getMultiChoiceQnA().get(tableRow.getId()).getAnswerDes());

		//serverQuestion.getMultiChoiceQnA().get(0).setAnswerDes((((MultipleChoiceQuestion)question_obj).getChoices().get(0).getDescription()));
	
		
		ansDesc.setText(question.getMultiChoiceQnA().get(tableRow.getId()).getAnswerDes());
		ansDesc.setTextColor(Color.parseColor("#000000"));
		ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		ansDesc.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
		    }
		    @Override
		    public void afterTextChanged(Editable s) {
			if(s.toString().length() > 100)
				Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
		    	if(s.toString().equals(""))
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(null);
			else
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(s.toString().trim());
		    }
		});
		final ImageView delete = new ImageView(activity);
		delete.setImageResource(R.drawable.delete_book);
		delete.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			// TODO Auto-generated method sdfstub
			tableLayout.removeView(tableRow);
			if(positionRemoved != -1 && serverQuestion != null && serverQuestion.getCheckValues() != null 
					&& positionRemoved == Integer.parseInt(serverQuestion.getCheckValues())){
				final RadioButton radio=(RadioButton) radioGroup.getChildAt(Integer.parseInt(serverQuestion.getCheckValues()));
				if(radio!=null)
				radio.setChecked(false);
			}
				radioGroup.clearCheck();
			serverQuestion.setCheckValues(null);
			question.setCheckValues(null);
			if(radioGroup!=null && radioGroup.getChildCount() > 0){
			    for(int i=0;i<radioGroup.getChildCount();i++){
				final RadioButton radio=(RadioButton) radioGroup.getChildAt(i);
				if(radio.getId()==tableRow.getId()){
					positionRemoved = radio.getId() + 1;
					radio.setChecked(false);
					radioGroup.removeView(radio);
					radioGroup.removeViewInLayout(radio);
				}
			    }
			}
			if (serverQuestion.getMultiChoiceQnA() != null && serverQuestion.getMultiChoiceQnA().size()>0){
			    Logger.info(tag, "position to remove is:"+tableLayout.getChildCount());
			    //positionRemoved = tableLayout.getChildCount();
			    serverQuestion.getMultiChoiceQnA().remove(tableRow.getId());
			}
			if(tableLayout.getChildCount()>0){
			    for(int i=0;i<tableLayout.getChildCount();i++){
				final TableRow tr=(TableRow)tableLayout.getChildAt(i);
				tr.setId(i);
				if(choice == "yes"){
				    final CheckBox check=(CheckBox)tr.getChildAt(0);
				    serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(check.isChecked()?"on":null);
				    final EditText ed1=(EditText)tr.getChildAt(1);
				    if(ed1.getText().toString().equals(""))
					serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
				    else
					serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(ed1.getText().toString().trim());
				}else{
				    if(radioGroup!=null && radioGroup.getChildCount()>0){
					final RadioButton radio=(RadioButton)radioGroup.getChildAt(i);
					radio.setId(i);
					if(radio.isChecked()){
					    /*   Question mul=(Question)question;
								   MultipleChoiceQuestion mulQ=(MultipleChoiceQuestion)mul;
								   mulQ.getChoices().get(i).setTeacherSelected(true);
								  serverQuestion=((ServerQuestion)((Question)mulQ));*/
					    serverQuestion.setCheckValues(String.valueOf(i+1));
					    question.setCheckValues(serverQuestion.getCheckValues());
					}
					final EditText ed1=(EditText)tr.getChildAt(0);
					if(ed1.getText().toString().equals(""))
					    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
					else	
					    serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(ed1.getText().toString().trim());
				    }
				}
				serverQuestion.getMultiChoiceQnA().get(i).setPosition(i+1);
			    }
			}
			addChoice.setVisibility(View.VISIBLE);
		    }
		});
		TableRow.LayoutParams fieldparams = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		fieldparams.setMargins(20, 5, 0, 0);
		TableLayout.LayoutParams fieldparams2;
		
		if(screenInches>8){
			ansDesc.setWidth(710);
			ansDesc.setHeight(35);
			fieldparams2= new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		}else if(screenInches > 5 && screenInches<=8){
			ansDesc.setWidth(710);
			ansDesc.setHeight(25);
			ansDesc.setTextSize(15);
			fieldparams2= new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		}else{
			ansDesc.setWidth(210);
			ansDesc.setHeight(20);
			ansDesc.setTextSize(12);
			radio.setButtonDrawable(R.drawable.radio_selector);
			fieldparams2= new TableLayout.LayoutParams(200, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		tableRow.addView(ansDesc);
		tableRow.addView(delete, fieldparams);
		tableLayout.addView(tableRow, fieldparams2);
		count++;
	    }
	});
	
	/*if(ques==null){
	TableRow tabRow=(TableRow) tableLayout.findViewById(count);
	EditText ansdesc=(EditText)tabRow.findViewById(tabRow.getId()+1);
	ansdesc.setText(((MultipleChoiceQuestion)_question1).getChoices().get(tabRow.getId()).getDescription());
	}*/
	RadioGroup.LayoutParams fieldparams3 = new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
														android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	//Multiple choice yes or selection layout.
	
	LinearLayout spinnerrow = new LinearLayout(activity);
	spinnerrow.setOrientation(LinearLayout.HORIZONTAL);
	TextView multiple = new TextView(activity);
	multiple.setText(" Do you have multiple answers:      ");
	multiple.setTextColor(Color.parseColor("#000000"));
	RadioGroup spinnerRadiogroup = new RadioGroup(activity);
	spinnerRadiogroup.setOrientation(LinearLayout.HORIZONTAL);
	
	spinnerRadiogroup.addView(yesrb);
	spinnerRadiogroup.addView(norb, fieldparams3);
	spinnerrow.addView(multiple);
	spinnerrow.addView(spinnerRadiogroup);
	spinnerrow.addView(addChoice);
	
	
	if(screenInches>8){
	   	 defaultParams = new LinearLayout.LayoutParams(1000,400);
	   	 defaultParams.setMargins(10, 10, 5, 10);
	   	 fieldparams3.setMargins(40, 0, 510, 0);
	   	 layoutParams.setMargins(30, 8, 0, 0);
	   	 layoutParams1.setMargins(30, 15, 0, 0);
    }else if(screenInches >5 && screenInches<=8){
	   	 defaultParams = new LinearLayout.LayoutParams(800,300);
	   	 defaultParams.setMargins(7, 7, 5, 7);
	   	 fieldparams3.setMargins(30, 0, 310, 0);
	   	 layoutParams.setMargins(30, 8, 0, 0);
	   	 layoutParams1.setMargins(30, 8, 0, 0);
	   	 yesrb.setButtonDrawable(R.drawable.radio_selector);
	   	 norb.setButtonDrawable(R.drawable.radio_selector);
	   	 multiple.setTextSize(14);
	   	 addChoice.setTextSize(14);
	   	 yesrb.setTextSize(14);
	   	 norb.setTextSize(14);
    }else{
	   	 defaultParams = new LinearLayout.LayoutParams(400,200);
	   	 defaultParams.setMargins(5, 5, 5, 5);
	   	 fieldparams3.setMargins(5, 0, 70, 0);
	   	 layoutParams.setMargins(10, 5, 0, 0);
	   	 layoutParams1.setMargins(10, 5, 0, 0);
	   	 multiple.setTextSize(12);
	   	 addChoice.setTextSize(12);
	   	 yesrb.setButtonDrawable(R.drawable.radio_selector);
	   	 norb.setButtonDrawable(R.drawable.radio_selector);
	   	 yesrb.setTextSize(12);
	   	 norb.setTextSize(12);
    }
	
	
	
	//Question Fields.
	LinearLayout quesFields = new LinearLayout(activity);
	quesFields.setOrientation(LinearLayout.HORIZONTAL);
	ScrollView sv =new ScrollView(activity);
	ScrollView sv1 =new ScrollView(activity);
	if(screenInches>5){
		quesFields.addView(radioGroup, layoutParams);
		quesFields.addView(tableLayout, defaultParams);
	}else{
		sv1.addView(radioGroup);
		quesFields.addView(sv1, layoutParams);
		sv.addView(tableLayout);
		quesFields.addView(sv, defaultParams);
	}
		
	layout.setOrientation(LinearLayout.VERTICAL);
	layout.addView(spinnerrow, layoutParams1);
	layout.addView(quesFields);
	
	question.setMultiChoiceQnA(serverQuestion.getMultiChoiceQnA());
	question.setCheckValues(serverQuestion.getCheckValues());
	return layout;
    }

    /**
     * Builds the matchthe following.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    private LinearLayout buildMatchtheFollowing(ServerQuestion question,
	    final CreateTestStep2Activity activity, LinearLayout layout) {
	final LinearLayout.LayoutParams  defaultParams ;
	
	final double screenSize= screenSizes(activity);

	// Create widgets view
	tableLayout = new TableLayout(activity);
	addChoice = new Button(activity);
	addChoice.setText("Add");
	addChoice.setTextSize(15);
	addChoice.setBackgroundResource(R.drawable.tc_button_background);
	addChoice.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

	question.setHasMultipleAnswers(true);
	final LinearLayout optionsLayout = new LinearLayout(activity);
	final LinearLayout.LayoutParams optionsParams = new LinearLayout.LayoutParams(300, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	optionsLayout.setOrientation(LinearLayout.HORIZONTAL);
	final TextView leftOptions = new TextView(activity);
	leftOptions.setText("A");
	leftOptions.setTextColor(Color.parseColor("#000000"));
	final TextView rightOptions = new TextView(activity);
	rightOptions.setText("B");
	optionsLayout.addView(leftOptions, optionsParams);
	optionsLayout.addView(rightOptions, optionsParams);
	rightOptions.setTextColor(Color.parseColor("#000000"));

	if(null!=question.getMultiChoiceQnA() ||!question.getMultiChoiceQnA().isEmpty()){
		for(int i=0;i<question.getMultiChoiceQnA().size();i++){

			if(tableLayout.getChildCount() > 3){
			    addChoice.setVisibility(View.GONE);
			} else {
			    addChoice.setVisibility(View.VISIBLE);
			}
			count=tableLayout.getChildCount()>0?tableLayout.getChildCount():0;
			final TableRow tableRow = new TableRow(activity);
			tableRow.setId(count);
			final EditText checkedValue = new EditText(activity);
			checkedValue.setText(String.valueOf(tableLayout.getChildCount() +1));
			checkedValue.setEnabled(false);
			checkedValue.setLongClickable(false);
			checkedValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
			checkedValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			if(checkedValue != null && checkedValue.toString().trim().equals(""))
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(null);
			else
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(checkedValue.getText().toString().trim());
			checkedValue.setTextColor(Color.parseColor("#000000"));
			//checkedValue.setPadding(15, 0, 10, 0);
			//checkedValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
			final EditText ansDesc = new EditText(activity);
			
			ansDesc.setTextColor(Color.parseColor("#000000"));
			ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
			ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			ansDesc.setHint("Answer text..");
			ansDesc.setText(question.getMultiChoiceQnA().get(i).getAnswerDes());
			serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(question.getMultiChoiceQnA().get(i).getAnswerDes());
			//ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200)});
			//ansDesc.setPadding(15, 0, 10, 0);
			final EditText checkedString = new EditText(activity);
			checkedString.setInputType(InputType.TYPE_CLASS_NUMBER);
			checkedString.setTextColor(Color.parseColor("#000000"));
			checkedString.setHint("order");
			checkedString.setText(question.getMultiChoiceQnA().get(i).getCheckedString());
			//checkedString.setPadding(30, 0, 30, 0);
			  serverQuestion.getMultiChoiceQnA().get(i).setCheckedString(question.getMultiChoiceQnA().get(i).getCheckedString());
			checkedString.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
			final EditText checkedText = new EditText(activity);
			checkedText.setTextColor(Color.parseColor("#000000"));
			checkedText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
			checkedText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			checkedText.setHint("Answer text..");
			checkedText.setText(question.getMultiChoiceQnA().get(i).getCheckedText());
			 serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedText(question.getMultiChoiceQnA().get(i).getCheckedText());
			//checkedText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200)});
			//checkedText.setPadding(20, 0, 10, 0);
			serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setPosition(tableRow.getId());
			
			if(screenSize > 8){
				checkedValue.setWidth(50);
				checkedValue.setHeight(35);
				ansDesc.setWidth(320);
				ansDesc.setHeight(35);
				checkedString.setWidth(70);
				checkedString.setHeight(35);
				checkedText.setWidth(320);
				checkedText.setHeight(35);
			}else if(screenSize <=8 && screenSize >5){
				checkedValue.setWidth(40);
				checkedValue.setHeight(25);
				ansDesc.setWidth(250);
				ansDesc.setHeight(25);
				checkedString.setWidth(40);
				checkedString.setHeight(25);
				checkedText.setWidth(250);
				checkedText.setHeight(25);
				checkedValue.setTextSize(16);
				ansDesc.setTextSize(16);
				checkedString.setTextSize(16);
				checkedText.setTextSize(16);
			}else{
				checkedValue.setWidth(40);
				checkedValue.setHeight(20);
				ansDesc.setWidth(200);
				ansDesc.setHeight(20);
				checkedString.setWidth(40);
				checkedString.setHeight(20);
				checkedText.setWidth(200);
				checkedText.setHeight(20);
				checkedValue.setTextSize(12);
				ansDesc.setTextSize(12);
				checkedString.setTextSize(12);
				checkedText.setTextSize(12);
			}
			/**
			 * Listeners are added for fields.
			 */
			/** For First Filed*/
			checkedValue.addTextChangedListener(new TextWatcher() {
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    }
			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count,
				    int after) {
			    }
			    @Override
			    public void afterTextChanged(Editable s) {
			    }
			});

			/** For Second Filed*/
			ansDesc.addTextChangedListener(new TextWatcher() {
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    }
			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count,
				    int after) {
			    }
			    @Override
			    public void afterTextChanged(Editable s) {
			    	if(s.toString().length() == 100)
			    		Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
			    	if(s.toString().equals(""))
			    		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(null);
			    	else
			    		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(s.toString().trim());
			    }
			});

			/** For Third Filed*/

			checkedString.addTextChangedListener(new TextWatcher() {
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    }
			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count,
				    int after) {
			    }
			    @Override
			    public void afterTextChanged(Editable s) {
				if(s.toString().equals(""))
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedString(null);
				else
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedString(s.toString().trim());
			    }
			});
			/** For Fourth Filed*/
			checkedText.addTextChangedListener(new TextWatcher() {
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			    }

			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count,
				    int after) {
			    }

			    @Override
			    public void afterTextChanged(Editable s) {
			    	if(s.toString().length() == 100)
			    		Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
				if(s.toString().equals(""))
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedText(null);
				else
				    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedText(s.toString().trim());
			    }
			});

			final ImageView delete = new ImageView(activity);
			delete.setImageResource(R.drawable.delete_book);
			delete.setOnClickListener(new OnClickListener() {
			    @Override
			    public void onClick(View v) {
				// TODO Auto-generated method stub
				tableLayout.removeView(tableRow);
				if (serverQuestion.getMultiChoiceQnA() != null && serverQuestion.getMultiChoiceQnA().size()>0){
				    serverQuestion.getMultiChoiceQnA().remove(tableLayout.getChildCount());
				}
				for(int i=0;i<tableLayout.getChildCount();i++){
				    final TableRow tr=(TableRow)tableLayout.getChildAt(i);
				    tr.setId(i);
				    EditText checkValue=(EditText)tr.getChildAt(0);
				    checkValue.setText(String.valueOf(i+1));
				    if(checkValue.getText().toString().equals("")){
						serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(null);
				    } else {
				    	serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(checkValue.getText().toString().trim());
				    }
				    final EditText answerDesc=(EditText)tr.getChildAt(1);
				    if(answerDesc.getText().toString().equals(""))
					serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
				    else
					serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(answerDesc.getText().toString().trim());

				    final EditText checkedString=(EditText)tr.getChildAt(2);
				    if(checkedString.getText().toString().equals(""))
					serverQuestion.getMultiChoiceQnA().get(i).setCheckedString(null);
				    else
					serverQuestion.getMultiChoiceQnA().get(i).setCheckedString(checkedString.getText().toString().trim());

				    final EditText checkedText=(EditText)tr.getChildAt(3);
				    if(checkedText.getText().toString().equals(""))
					serverQuestion.getMultiChoiceQnA().get(i).setCheckedText(null);
				    else
					serverQuestion.getMultiChoiceQnA().get(i).setCheckedText(checkedText.getText().toString().trim());
				    serverQuestion.getMultiChoiceQnA().get(i).setPosition(i+1);
				}

				addChoice.setVisibility(View.VISIBLE);
			    }
			});
			TableRow.LayoutParams fieldparams = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			fieldparams.setMargins(20, 5, 0, 0);
			tableRow.addView(checkedValue);
			tableRow.addView(ansDesc);
			tableRow.addView(checkedString);
			tableRow.addView(checkedText);
			tableRow.addView(delete, fieldparams);
			tableLayout.addView(tableRow);
		    
			
			
			
			
		}
		
		
		
	}
	
	
	
	
	
	addChoice.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if(tableLayout.getChildCount() > 3){
		    addChoice.setVisibility(View.GONE);
		} else {
		    addChoice.setVisibility(View.VISIBLE);
		}
		count=tableLayout.getChildCount()>0?tableLayout.getChildCount():0;
		final TableRow tableRow = new TableRow(activity);
		tableRow.setId(count);
		final EditText checkedValue = new EditText(activity);
		checkedValue.setText(String.valueOf(tableLayout.getChildCount() +1));
		checkedValue.setEnabled(false);
		checkedValue.setLongClickable(false);
		checkedValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
		checkedValue.setInputType(InputType.TYPE_CLASS_NUMBER);
		if(checkedValue != null && checkedValue.toString().trim().equals(""))
		    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(null);
		else
		    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedValue(checkedValue.getText().toString().trim());
		checkedValue.setTextColor(Color.parseColor("#000000"));
		checkedValue.setHeight(35);
		//checkedValue.setPadding(15, 0, 10, 0);
		//checkedValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
		final EditText ansDesc = new EditText(activity);
		ansDesc.setTextColor(Color.parseColor("#000000"));
		ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		ansDesc.setHint("Answer text..");
		//ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200)});
		//ansDesc.setPadding(15, 0, 10, 0);
		final EditText checkedString = new EditText(activity);
		checkedString.setInputType(InputType.TYPE_CLASS_NUMBER);
		checkedString.setTextColor(Color.parseColor("#000000"));
		checkedString.setHint("order");
		//checkedString.setPadding(30, 0, 30, 0);
		InputFilter checkedStringFilter = new InputFilter() {
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				Pattern p = Pattern.compile("[^0[6-9]]");
				Matcher m = p.matcher(source);
				if(!m.find()){
					Toast.makeText(activity, "Only 1 to 5 numbers are allowed", Toast.LENGTH_SHORT).show();
					return "";					
				}
				else
					return source;
			}
		};
		  checkedString.setFilters(new InputFilter[] { checkedStringFilter, new InputFilter.LengthFilter(1)});
		//checkedString.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
		final EditText checkedText = new EditText(activity);
		checkedText.setTextColor(Color.parseColor("#000000"));
		checkedText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		checkedText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		checkedText.setHint("Answer text..");
		//checkedText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200)});
		//checkedText.setPadding(20, 0, 10, 0);
		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setPosition(tableRow.getId());
		
		if(screenSize > 8){
			checkedValue.setWidth(50);
			checkedValue.setHeight(35);
			ansDesc.setWidth(320);
			ansDesc.setHeight(35);
			checkedString.setWidth(70);
			checkedString.setHeight(35);
			checkedText.setWidth(320);
			checkedText.setHeight(35);
		}else if(screenSize <=8 && screenSize >5){
			checkedValue.setWidth(40);
			checkedValue.setHeight(25);
			ansDesc.setWidth(250);
			ansDesc.setHeight(25);
			checkedString.setWidth(40);
			checkedString.setHeight(25);
			checkedText.setWidth(250);
			checkedText.setHeight(25);
			checkedValue.setTextSize(16);
			ansDesc.setTextSize(16);
			checkedString.setTextSize(16);
			checkedText.setTextSize(16);
		}else{
			checkedValue.setWidth(40);
			checkedValue.setHeight(20);
			ansDesc.setWidth(120);
			ansDesc.setHeight(20);
			checkedString.setWidth(40);
			checkedString.setHeight(20);
			checkedText.setWidth(120);
			checkedText.setHeight(20);
			checkedValue.setTextSize(12);
			ansDesc.setTextSize(12);
			checkedString.setTextSize(12);
			checkedText.setTextSize(12);
		}
		/**
		 * Listeners are added for fields.
		 */
		/** For First Filed*/
		checkedValue.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
		    }
		    @Override
		    public void afterTextChanged(Editable s) {
		    }
		});

		/** For Second Filed*/
		ansDesc.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
		    }
		    @Override
		    public void afterTextChanged(Editable s) {
		    	if(s.toString().length() > 100)
		    		Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
		    	if(s.toString().equals(""))
		    		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(null);
		    	else
		    		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setAnswerDes(s.toString().trim());
		    }
		});

		/** For Third Filed*/

		checkedString.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
		    }
		    @Override
		    public void afterTextChanged(Editable s) {
			if(s.toString().equals(""))
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedString(null);
			else
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedString(s.toString().trim());
		    }
		});
		/** For Fourth Filed*/
		checkedText.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
			    int after) {
		    }

		    @Override
		    public void afterTextChanged(Editable s) {
		    	if(s.toString().length() > 100)
		    		Toast.makeText(activity, "Choice can't exceed max length of 100", Toast.LENGTH_LONG).show();
			if(s.toString().equals(""))
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedText(null);
			else
			    serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setCheckedText(s.toString().trim());
		    }
		});

		final ImageView delete = new ImageView(activity);
		delete.setImageResource(R.drawable.delete_book);
		delete.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			// TODO Auto-generated method stub
			tableLayout.removeView(tableRow);
			if (serverQuestion.getMultiChoiceQnA() != null && serverQuestion.getMultiChoiceQnA().size()>0){
			    serverQuestion.getMultiChoiceQnA().remove(tableLayout.getChildCount());
			}
			for(int i=0;i<tableLayout.getChildCount();i++){
			    final TableRow tr=(TableRow)tableLayout.getChildAt(i);
			    tr.setId(i);
			    EditText checkValue=(EditText)tr.getChildAt(0);
			    checkValue.setText(String.valueOf(i+1));
			    if(checkValue.getText().toString().equals("")){
					serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(null);
			    } else {
			    	serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(checkValue.getText().toString().trim());
			    }
			    final EditText answerDesc=(EditText)tr.getChildAt(1);
			    if(answerDesc.getText().toString().equals(""))
				serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(null);
			    else
				serverQuestion.getMultiChoiceQnA().get(i).setAnswerDes(answerDesc.getText().toString().trim());

			    final EditText checkedString=(EditText)tr.getChildAt(2);
			    if(checkedString.getText().toString().equals(""))
				serverQuestion.getMultiChoiceQnA().get(i).setCheckedString(null);
			    else
				serverQuestion.getMultiChoiceQnA().get(i).setCheckedString(checkedString.getText().toString().trim());

			    final EditText checkedText=(EditText)tr.getChildAt(3);
			    if(checkedText.getText().toString().equals(""))
				serverQuestion.getMultiChoiceQnA().get(i).setCheckedText(null);
			    else
				serverQuestion.getMultiChoiceQnA().get(i).setCheckedText(checkedText.getText().toString().trim());
			    serverQuestion.getMultiChoiceQnA().get(i).setPosition(i+1);
			}

			addChoice.setVisibility(View.VISIBLE);
		    }
		});
		TableRow.LayoutParams fieldparams = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		fieldparams.setMargins(20, 5, 0, 0);
		tableRow.addView(checkedValue);
		tableRow.addView(ansDesc);
		tableRow.addView(checkedString);
		tableRow.addView(checkedText);
		tableRow.addView(delete, fieldparams);
		tableLayout.addView(tableRow);
	    }
	});
	ScrollView svScrollView = new ScrollView(activity);
	if(screenSize <=5){
		defaultParams = new LinearLayout.LayoutParams(400,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		defaultParams.setMargins(10, 5, 15, 5);
		svScrollView.addView(tableLayout);
		layout.addView(svScrollView, defaultParams);
	}else if(screenSize >5 && screenSize <=8){
		defaultParams = new LinearLayout.LayoutParams(800,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		defaultParams.setMargins(20, 10, 25, 10);
		layout.addView(tableLayout, defaultParams);
	}else{
		defaultParams = new LinearLayout.LayoutParams(860,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		defaultParams.setMargins(30, 10, 35, 10);
		layout.addView(tableLayout, defaultParams);
	}
	layout.addView(addChoice);
	question.setMultiChoiceQnA(serverQuestion.getMultiChoiceQnA());
	return layout;
    }


    /**
     * Builds the true or false.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    private LinearLayout buildTrueOrFalse(final ServerQuestion question,
	    CreateTestStep2Activity activity, LinearLayout layout) {

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setGravity(0);
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	
	
	 DisplayMetrics dm = new DisplayMetrics();
     ((Activity)activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
     double sx = Math.pow(dm.widthPixels/dm.xdpi,2);
     double sy = Math.pow(dm.heightPixels/dm.ydpi,2);
     double screenInches = Math.sqrt(sx+sy);


	final RadioGroup radioGroup = new RadioGroup(activity);
	final RadioGroup.LayoutParams rgparams = new RadioGroup.LayoutParams(activity, null);
	
	radioGroup.setOrientation(LinearLayout.HORIZONTAL);

	
	final RadioButton trueradiobutton = new RadioButton(activity);
	radioGroup.addView(trueradiobutton, rgparams);
	trueradiobutton.setText("TRUE");
	trueradiobutton.setTextColor(Color.parseColor("#000000"));
	
	
	trueradiobutton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		question.setTruefalseAnswer(true);
	    }
	});


	final RadioButton falseradiobutton = new RadioButton(activity);
	radioGroup.addView(falseradiobutton, rgparams);
	falseradiobutton.setText("FALSE");
	falseradiobutton.setChecked(true);
//	question.setTruefalseAnswer(false);
	falseradiobutton.setTextColor(Color.parseColor("#000000"));
	falseradiobutton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		question.setTruefalseAnswer(false);
	    }
	});
	
	if(question.getTruefalseAnswer()){
		trueradiobutton.setChecked(true);
		falseradiobutton.setChecked(false);
	}else{
		trueradiobutton.setChecked(false);
		falseradiobutton.setChecked(true);
	}
		
	if(screenInches>5){
		defaultParams.setMargins(20, 16, 5, 30);
		rgparams.setMargins(10, 0, 30, 5);
	}else{
		defaultParams.setMargins(10, 8, 5, 15);
		rgparams.setMargins(10, 0, 15, 5);
		falseradiobutton.setTextSize(12);
		trueradiobutton.setTextSize(12);
		trueradiobutton.setButtonDrawable(R.drawable.radio_selector);
		falseradiobutton.setButtonDrawable(R.drawable.radio_selector);
		trueradiobutton.setPadding(5, 5, 5, 5);
		falseradiobutton.setPadding(5, 5, 5, 5);
	}
	// Add to layout
	layout.addView(radioGroup, defaultParams);

	return layout;
    }

    /**
     * Builds the short answer question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    private LinearLayout buildShortAnswerQuestion(final ServerQuestion question,
	    CreateTestStep2Activity activity, LinearLayout layout) {

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.MATCH_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.MATCH_PARENT);

	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setGravity(0);
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.MATCH_PARENT);

	defaultParams.setMargins(50, 10, 5, 30);

	// Create Answer Edit Text view
	final EditText answerEditText = new EditText(activity);
	answerEditText.setId(3);
	answerEditText.setMaxLines(100);
	answerEditText.setVerticalScrollBarEnabled(false);
	answerEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
	answerEditText.setSingleLine(false);
	answerEditText.setHint("Your answer here..");
	answerEditText.setGravity(Gravity.TOP);

	if(question.getShortAnswer()!=null){
		answerEditText.setText(question.getShortAnswer());
	}
	
	
	
	answerEditText.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
		final StringBuffer stringBuffer = new StringBuffer();
		if(answerEditText.getText().equals(null) && answerEditText.getText().equals("")){
		    stringBuffer.append(answerEditText.getText());
		}else
		    stringBuffer.append(answerEditText.getText().toString());
		question.setShortAnswer(stringBuffer);
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	    }
	});

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		800,
		android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	answerParams.setMargins(30, 5, 45, 5);

	// Add to layout
	layout.addView(answerEditText, answerParams);

	return layout;
    }

    /**
     * Builds the long answer question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    private LinearLayout buildLongAnswerQuestion(final ServerQuestion question,
	    CreateTestStep2Activity activity, LinearLayout layout) {

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.MATCH_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.MATCH_PARENT);

	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setGravity(0);
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.MATCH_PARENT);

	defaultParams.setMargins(10, 10, 5, 30);

	// Create Answer Edit Text view
	final EditText answerEditText = new EditText(activity);
	answerEditText.setId(3);
	answerEditText.setMaxLines(100);
	answerEditText.setVerticalScrollBarEnabled(false);
	answerEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
	answerEditText.setSingleLine(false);
	answerEditText.setHint("Your answer here..");
	answerEditText.setGravity(Gravity.TOP);
	
	if(question.getLongAnswer()!=null){
		answerEditText.setText(question.getLongAnswer());
	}
	answerEditText.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
		final StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(answerEditText.getText().toString());
		question.setLongAnswer(stringBuffer);
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	    }
	});

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
			800,
			android.view.ViewGroup.LayoutParams.FILL_PARENT);
		answerParams.setMargins(30, 5, 45, 5);
	// Add to layout
	layout.addView(answerEditText, answerParams);

	return layout;
    }

    /**
     * Layout params.
     *
     * @param fillParent the fill parent
     * @param matchParent the match parent
     * @return the layout params
     */
    private LayoutParams LayoutParams(int fillParent, int matchParent) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * Gets the update linear lay out.
     *
     * @param layOut the lay out
     * @param question the question
     * @return the update linear lay out
     */
    private LinearLayout getUpdateLinearLayOut(LinearLayout layOut,ServerQuestion question){
	if(question.getType().equals(QuestionType.MULTIPLECHOICE_QUESTION.name()))
	{
	    final Spinner spinner=(Spinner)layOut.getChildAt(0);
	    final TableLayout table= (TableLayout) layOut.getChildAt(2);
	    final RadioGroup radioGroup=(RadioGroup)layOut.getChildAt(1);
	    for(int i=0;i<table.getChildCount();i++){
		final TableRow tr=(TableRow)table.getChildAt(i);
		if(spinner.getSelectedItem().equals("YES")){
		    final CheckBox check=(CheckBox)tr.getChildAt(0);
		    question.getMultiChoiceQnA().get(i).setCheckedValue(check.isChecked()?"on":null);
		}else{
		    if(radioGroup!=null && radioGroup.getChildCount()>0){
			final RadioButton radio=(RadioButton)radioGroup.getChildAt(i);
			if(radio.isChecked())
			    question.setCheckValues(String.valueOf(i));
		    }
		}
		final EditText ed1=(EditText)tr.getChildAt(1);
		question.getMultiChoiceQnA().get(i).setAnswerDes(ed1.getText().toString().trim());
		question.getMultiChoiceQnA().get(i).setPosition(i);
	    }
	}else if(question.getType().equals(Question.MATCH_THE_FOLLOWING))
	{
	    final TableLayout table=(TableLayout) layOut.getChildAt(0);
	    for(int i=0;i<table.getChildCount();i++){
		final TableRow tr=(TableRow)table.getChildAt(i);
		final EditText checkValue=(EditText)tr.getChildAt(0);
		question.getMultiChoiceQnA().get(i).setCheckedValue(checkValue.getText().toString().trim());

		final EditText answerDesc=(EditText)tr.getChildAt(1);
		question.getMultiChoiceQnA().get(i).setAnswerDes(answerDesc.getText().toString().trim());

		final EditText checkedString=(EditText)tr.getChildAt(2);
		question.getMultiChoiceQnA().get(i).setCheckedString(checkedString.getText().toString().trim());

		final EditText checkedText=(EditText)tr.getChildAt(3);
		question.getMultiChoiceQnA().get(i).setCheckedText(checkedText.getText().toString().trim());
		question.getMultiChoiceQnA().get(i).setPosition(i);
	    }
	}else if(question.getType().equals(QuestionType.ORDERING_QUESTION.name())){
	    final TableLayout table=(TableLayout) layOut.getChildAt(0);
	    for(int i=0;i<table.getChildCount();i++){
		final TableRow tr=(TableRow)table.getChildAt(i);

		final EditText checkValue=(EditText)tr.getChildAt(0);
		question.getMultiChoiceQnA().get(i).setCheckedValue(checkValue.getText().toString().trim());

		final EditText answerDesc=(EditText)tr.getChildAt(1);
		question.getMultiChoiceQnA().get(i).setAnswerDes(answerDesc.getText().toString().trim());
		question.getMultiChoiceQnA().get(i).setPosition(i);
	    }
	}

	return null;
    }
    
    public Double screenSizes(Context activity){
    	DisplayMetrics dm = new DisplayMetrics();
	     ((Activity)activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
	     double sx = Math.pow(dm.widthPixels/dm.xdpi,2);
	     double sy = Math.pow(dm.heightPixels/dm.ydpi,2);
	     final double screenInches = Math.sqrt(sx+sy);
	     return screenInches;

    }

}
