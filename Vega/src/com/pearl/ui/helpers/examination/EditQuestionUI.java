package com.pearl.ui.helpers.examination;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;

import com.pearl.R;
import com.pearl.activities.CreateTestStep2Activity;
import com.pearl.examination.QuestionType;
import com.pearl.logger.Logger;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.ServerQuestion;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class EditQuestionUI.
 */
public class EditQuestionUI {
    
    /** The esitquestions ui. */
    private static EditQuestionUI esitquestionsUI = null;
    
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

    /**
     * Gets the single instance of EditQuestionUI.
     *
     * @return single instance of EditQuestionUI
     */
    public static EditQuestionUI getInstance() {
    	esitquestionsUI = new EditQuestionUI();

	return esitquestionsUI;
    }

    /**
     * Builds the.
     *
     * @param question the question
     * @param _question the _question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout build( ServerQuestion question, Question _question,CreateTestStep2Activity activity,	LinearLayout layout) {
	count=0;
	serverQuestion=new ServerQuestion();
	Logger.info("QuestionsUI",
		"Building question Type " + question.getType());
	if (layout == null) {
	    // Initialize Linear Layout
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
	if(_question!=null){
	if (QuestionType.SHORT_ANSWER_QUESTION.toString().equalsIgnoreCase(_question.getType().trim())
		) {
		return    buildShortAnswerQuestion(question, activity, layout);
	} else if(QuestionType.LONG_ANSWER_QUESTION.toString().equalsIgnoreCase(_question.getType()
		.trim())){
		return     buildLongAnswerQuestion(question, activity, layout);
	}else if (QuestionType.TRUE_OR_FALSE_QUESTION.toString().equalsIgnoreCase(_question.getType()
		.trim())) {
		return    buildTrueOrFalse(question, activity, layout);
	} else if (QuestionType.MULTIPLECHOICE_QUESTION.toString().equalsIgnoreCase(_question
		.getType().trim())) {
		return     buildMultipleChoiceQuestion(question,_question, activity, layout);
	} else if (QuestionType.ORDERING_QUESTION.toString().equalsIgnoreCase(_question.getType().trim())) {
		return   buildOrderingQuestion(question, activity, layout);
	} else if (QuestionType.MATCH_THE_FOLLOWING_QUESTION.toString().equalsIgnoreCase(_question.getType()
		.trim())) {
		return     buildMatchtheFollowing(question, activity, layout);
	} else {
	    Logger.error("QuestionsUI", "***Failed to Build question Type "
		    + question.getType());
	}
	}else if(question!=null){
		if (QuestionType.SHORT_ANSWER_QUESTION.toString().equalsIgnoreCase(question.getType().trim())
				) {
			return     buildShortAnswerQuestion(question, activity, layout);
			} else if(QuestionType.LONG_ANSWER_QUESTION.toString().equalsIgnoreCase(question.getType()
				.trim())){
				return     buildLongAnswerQuestion(question, activity, layout);
			}else if (QuestionType.TRUE_OR_FALSE_QUESTION.toString().equalsIgnoreCase(question.getType()
				.trim())) {
				return	     buildTrueOrFalse(question, activity, layout);
			} else if (QuestionType.MULTIPLECHOICE_QUESTION.toString().equalsIgnoreCase(question
				.getType().trim())) {
				return     buildMultipleChoiceQuestion(question,_question, activity, layout);
			} else if (QuestionType.ORDERING_QUESTION.toString().equalsIgnoreCase(question.getType().trim())) {
				return     buildOrderingQuestion(question, activity, layout);
			} else if (QuestionType.MATCH_THE_FOLLOWING_QUESTION.toString().equalsIgnoreCase(question.getType()
				.trim())) {
				return    buildMatchtheFollowing(question, activity, layout);
			} else {
			    Logger.error("QuestionsUI", "***Failed to Build question Type "
				    + question.getType());
			}	
	}
	return null;
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
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		860,
		android.view.ViewGroup.LayoutParams.MATCH_PARENT);

	defaultParams.setMargins(5, 10, 60, 10);

	// Create widgets view
	tableLayout = new TableLayout(activity);
	tableLayout.setScrollContainer(true);

	addChoice = new Button(activity);
	addChoice.setText("Add Choice");
	addChoice.setBackgroundResource(R.drawable.tc_button_background);
	addChoice.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

	question.setHasMultipleAnswers(true);
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
		position.setHint("order");
		position.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
		position.setInputType(InputType.TYPE_CLASS_NUMBER);
		final EditText ansDesc = new EditText(activity);
		ansDesc.setTextColor(Color.parseColor("#000000"));
		ansDesc.setWidth(685);
		ansDesc.setHeight(35);
		ansDesc.setHint("Answer text..");
		ansDesc.setText(question.getMultiChoiceQnA().get(tableRow.getId()).getAnswerDes());
		ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setPosition(tableRow.getId()+1);
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
		tableRow.addView(position, fieldparamsbutt);
		tableRow.addView(ansDesc);
		tableRow.addView(delete, fieldparams);
		tableLayout.addView(tableRow);
	    }
	});
	question.setMultiChoiceQnA(serverQuestion.getMultiChoiceQnA());
	layout.addView(tableLayout, defaultParams);
	layout.addView(addChoice);
	return layout;
    }

    /**
     * Builds the multiple choice question.
     *
     * @param question the question
     * @param _question the _question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    private LinearLayout buildMultipleChoiceQuestion(final ServerQuestion question,final Question _question,
	    final CreateTestStep2Activity activity, LinearLayout layout) {
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		1000,
		400);

	defaultParams.setMargins(10, 10, 5, 10);
	final LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	layoutParams.setMargins(30, 8, 0, 0);
	final LinearLayout.LayoutParams layoutParams1 = new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	layoutParams1.setMargins(30, 15, 0, 0);
	
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
	addChoice.setText("Add Choice");
	addChoice.setBackgroundResource(R.drawable.tc_button_background);
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
	
Handler hanlder=new Handler();
hanlder.post(new Runnable() {
	
	@Override
	public void run() {
		if(_question!=null)
		{
			//for(int i=0;i<	((MultipleChoiceQuestion)_question).getChoices().size();i++)	
//		tableLayout= questionView(activity,serverQuestion,_question);

				if(tableLayout.getChildCount() > 3){
				 //   addChoice.setVisibility(View.INVISIBLE);
				} else {
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
					    if(isChecked){
						serverQuestion.setCheckValues(String.valueOf(tableRow.getId()+1));
					    }
					    question.setCheckValues(serverQuestion.getCheckValues());
					}
				    });
				    final RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(activity, null);
				    params.setMargins(0, 5, 0, 5);
				    if(radioGroup!=null)
					radio.setLayoutParams(params);
				    radioGroup.addView(radio);
				    
				}
				final EditText ansDesc = new EditText(activity);
				ansDesc.setWidth(710);
				ansDesc.setHeight(35);
				ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
//				ansDesc.setHint("Answer text...");
				ansDesc.setText(((MultipleChoiceQuestion)_question).getChoices().get(tableRow.getId()).getDescription());
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
				TableLayout.LayoutParams fieldparams2 = new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				tableRow.addView(ansDesc);
				tableRow.addView(delete, fieldparams);
				tableLayout.addView(tableRow, fieldparams2);
				count++;
		}
		
	}
});

/*	addChoice.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if(tableLayout.getChildCount() > 3){
		    addChoice.setVisibility(View.INVISIBLE);
		} else {
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
			    if(isChecked){
				serverQuestion.setCheckValues(String.valueOf(tableRow.getId()+1));
			    }
			    question.setCheckValues(serverQuestion.getCheckValues());
			}
		    });
		    final RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(activity, null);
		    params.setMargins(0, 5, 0, 5);
		    if(radioGroup!=null)
			radio.setLayoutParams(params);
		    radioGroup.addView(radio);
		    
		}
		final EditText ansDesc = new EditText(activity);
		ansDesc.setWidth(710);
		ansDesc.setHeight(35);
		ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		ansDesc.setHint("Answer text...");
		ansDesc.setText(((MultipleChoiceQuestion)_question).getChoices().get(tableRow.getId()).getDescription());
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
					       Question mul=(Question)question;
								   MultipleChoiceQuestion mulQ=(MultipleChoiceQuestion)mul;
								   mulQ.getChoices().get(i).setTeacherSelected(true);
								  serverQuestion=((ServerQuestion)((Question)mulQ));
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
		TableLayout.LayoutParams fieldparams2 = new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tableRow.addView(ansDesc);
		tableRow.addView(delete, fieldparams);
		tableLayout.addView(tableRow, fieldparams2);
		count++;
	    }
	});*/
	RadioGroup.LayoutParams fieldparams3 = new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
														android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	fieldparams3.setMargins(40, 0, 510, 0);
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
	
	
	//Question Fields.
	LinearLayout quesFields = new LinearLayout(activity);
	quesFields.setOrientation(LinearLayout.HORIZONTAL);
	quesFields.addView(radioGroup, layoutParams);
	quesFields.addView(tableLayout, defaultParams);
	
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
    private LinearLayout buildMatchtheFollowing(final ServerQuestion question,
	    final CreateTestStep2Activity activity, LinearLayout layout) {
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		860,
		android.view.ViewGroup.LayoutParams.MATCH_PARENT);

	defaultParams.setMargins(30, 10, 35, 10);

	// Create widgets view
	tableLayout = new TableLayout(activity);
	addChoice = new Button(activity);
	addChoice.setText("Add Choice");
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
	for(int i=0;i<question.getMultiChoiceQnA().size();i++)
	addChoice.performClick();
	
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
		checkedValue.setWidth(50);
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
		ansDesc.setWidth(320);
		ansDesc.setTextColor(Color.parseColor("#000000"));
		ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		ansDesc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		ansDesc.setHeight(35);
		ansDesc.setText(question.getMultiChoiceQnA().get(tableRow.getId()).getAnswerDes());
		ansDesc.setHint("Answer text..");
		//ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200)});
		//ansDesc.setPadding(15, 0, 10, 0);
		final EditText checkedString = new EditText(activity);
		checkedString.setWidth(70);
		checkedString.setInputType(InputType.TYPE_CLASS_NUMBER);
		checkedString.setTextColor(Color.parseColor("#000000"));
		checkedString.setHeight(35);
		checkedString.setText(question.getMultiChoiceQnA().get(tableRow.getId()).getCheckedString());
		checkedString.setHint("order");
		//checkedString.setPadding(30, 0, 30, 0);
		checkedString.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1)});
		final EditText checkedText = new EditText(activity);
		checkedText.setWidth(320);
		checkedText.setTextColor(Color.parseColor("#000000"));
		checkedText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
		checkedText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		checkedText.setHeight(35);
		checkedText.setText(question.getMultiChoiceQnA().get(tableRow.getId()).getCheckedText());
		checkedText.setHint("Answer text..");
		//checkedText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200)});
		//checkedText.setPadding(20, 0, 10, 0);
		serverQuestion.getMultiChoiceQnA().get(tableRow.getId()).setPosition(tableRow.getId());
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
	});
	layout.addView(tableLayout, defaultParams);
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

	defaultParams.setMargins(20, 16, 5, 30);

	final RadioGroup radioGroup = new RadioGroup(activity);
	final RadioGroup.LayoutParams rgparams = new RadioGroup.LayoutParams(activity, null);
	rgparams.setMargins(10, 0, 30, 5);
	radioGroup.setOrientation(LinearLayout.HORIZONTAL);

	
	final RadioButton trueradiobutton = new RadioButton(activity);
	radioGroup.addView(trueradiobutton, rgparams);
	trueradiobutton.setText("TRUE");
	trueradiobutton.setTextColor(Color.parseColor("#000000"));
	trueradiobutton.setChecked(question.getTruefalseAnswer());
	trueradiobutton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		question.setTruefalseAnswer(true);
	    }
	});


	final RadioButton falseradiobutton = new RadioButton(activity);
	radioGroup.addView(falseradiobutton, rgparams);
	falseradiobutton.setText("FALSE");
	falseradiobutton.setChecked(question.getTruefalseAnswer());
	question.setTruefalseAnswer(false);
	falseradiobutton.setTextColor(Color.parseColor("#000000"));
	falseradiobutton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		question.setTruefalseAnswer(false);
	    }
	});
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
	    // Initialize Linear Layout.
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
	answerEditText.setText(question.getShortAnswer());
	answerEditText.setGravity(Gravity.TOP);

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
	answerEditText.setText(question.getLongAnswer());
	answerEditText.setGravity(Gravity.TOP);
	
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

    
    /**
     * Question view.
     *
     * @param activity the activity
     * @param question the question
     * @param _question the _question
     * @return the table layout
     */
    TableLayout questionView(final CreateTestStep2Activity activity,final ServerQuestion question,Question _question ){

		if(tableLayout.getChildCount() > 3){
		 //   addChoice.setVisibility(View.INVISIBLE);
		} else {
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
			    if(isChecked){
				serverQuestion.setCheckValues(String.valueOf(tableRow.getId()+1));
			    }
			    question.setCheckValues(serverQuestion.getCheckValues());
			}
		    });
		    final RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(activity, null);
		    params.setMargins(0, 5, 0, 5);
		    if(radioGroup!=null)
			radio.setLayoutParams(params);
		    radioGroup.addView(radio);
		    
		}
		final EditText ansDesc = new EditText(activity);
		ansDesc.setWidth(710);
		ansDesc.setHeight(35);
		ansDesc.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100)});
//		ansDesc.setHint("Answer text...");
		ansDesc.setText(((MultipleChoiceQuestion)_question).getChoices().get(tableRow.getId()).getDescription());
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
		TableLayout.LayoutParams fieldparams2 = new TableLayout.LayoutParams(650, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tableRow.addView(ansDesc);
		tableRow.addView(delete, fieldparams);
		tableLayout.addView(tableRow, fieldparams2);
		count++;
	    return tableLayout;
    }
}
