package com.pearl.activities;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.parsers.json.TeacherExamParser;
import com.pearl.user.teacher.examination.ExamDetails;
import com.pearl.user.teacher.examination.ServerExamDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class UnderConstructionTestsActivity.
 */
public class UnderConstructionTestsActivity extends BaseActivity {

	/** The app data. */
	private ApplicationData appData;
	
	/** The test list view. */
	private ListView testListView;
	
	/** The exam list. */
	private List<ServerExamDetails> examList;
	
	/** The no_tests. */
	private TextView no_tests;
	
	/** The create new. */
	private Button createNew;
	
	/** The exam id. */
	private String examId;
	
	/** The help. */
	private ImageView menuButton, help;
	
	/** The i. */
	private int i = 0;
	
	/** The student list adapter. */
	private TestsListAdapter studentListAdapter;

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.under_construction_test_list);
		appData = (ApplicationData) getApplication();
		testListView = (ListView) findViewById(R.id.under_construction_test_list);
		help = (ImageView) findViewById(R.id.underconstruction_help);
		createNew = (Button) findViewById(R.id.create_test);
		menuButton = (ImageView) findViewById(R.id.menu_underConstruction);
		no_tests = (TextView) findViewById(R.id.no_under_construction_tests);
		testListView.setVisibility(View.INVISIBLE);
		createNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(activityContext,
						CreateTestStep1Activity.class);
				intent.putExtra(VegaConstants.TEST_ID, "");
				startActivity(intent);
			}
		});
		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent i = new Intent(
						UnderConstructionTestsActivity.this, ActionBar.class);
				startActivity(i);
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
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent("quizzard_create_test.txt", this);
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

			final Intent login = new Intent(this, LoginActivity.class);
			startActivity(login);
			Logger.info(tag,
					"LOGIN status if shelf..." + appData.isLoginStatus());
			finish();

		}
		getUnderConstructionTestsList();
	}

	/**
	 * Gets the under construction tests list.
	 *
	 * @return the under construction tests list
	 */
	private void getUnderConstructionTestsList() {
		//Collections.sort(Database.arrayList, new CustomComparator());
		if (ApplicationData.isFileExists(appData.getTestsListFileName())) {
			try {
				final String underconstructionList = ApplicationData
						.readFile(appData.getTestsListFileName());
				examList = TeacherExamParser
						.getExamsList(underconstructionList);
				populateTestsList();

			} catch (final IOException e) {
				Logger.error(tag, e);
			}
		} else
			Logger.warn(tag, "File doesnot exists");
	}

	/**
	 * Populate tests list.
	 */
	private void populateTestsList() {
		Collections.sort(examList, new Comparator<ExamDetails>() {

			@Override
			public int compare(ExamDetails statedate1, ExamDetails statedate2) {
				
				return String.valueOf(statedate2.getStartTime()).compareTo(String.valueOf(statedate1.getStartTime()));
			}
		});
		if (!examList.isEmpty()) {
			studentListAdapter = new TestsListAdapter(
					this, android.R.layout.simple_selectable_list_item,
					examList);
			testListView.setVisibility(View.VISIBLE);
			no_tests.setVisibility(View.GONE);
			testListView.setAdapter(studentListAdapter);
		} else {
			testListView.setVisibility(View.INVISIBLE);
			no_tests.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * The Class TestsListAdapter.
	 */
	private class TestsListAdapter extends ArrayAdapter<ServerExamDetails> {
		
		/** The item. */
		List<ServerExamDetails> item;

		/**
		 * Instantiates a new tests list adapter.
		 *
		 * @param context the context
		 * @param textViewResourceId the text view resource id
		 * @param item the item
		 */
		public TestsListAdapter(Context context, int textViewResourceId,
				List<ServerExamDetails> item) {
			super(context, textViewResourceId, item);
			this.item = item;
			Logger.warn(tag, "item size is:" + item.size());
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			TextView testName = null, date = null;
			Button edit, delete;
			if (view == null) {
				final LayoutInflater inflater = (LayoutInflater) this
						.getContext().getSystemService(
								Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(
						R.layout.under_construction_test_list_row, null);

			}

			final ExamDetails examDetails = item.get(position);
			testName = (TextView) view
					.findViewById(R.id.under_construction_test_name);
			date = (TextView) view
					.findViewById(R.id.underconstruction_test_schedule);
			edit = (Button) view.findViewById(R.id.underconstruction_edit_test);
			delete = (Button) view
					.findViewById(R.id.underconstruction_delete_test);
			testName.setText(examDetails.getTitle());
			if(examDetails.getStartTime() != 0){
				Date d = new Date(examDetails.getStartTime());
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,''yy HH:mm");
				Logger.warn(tag, "formatted date is:"+sdf.format(d));
				date.setText(sdf.format(d));				
			}else
				date.setText("");
			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					examId = examList.get(position).getExamId();
					final Intent intent = new Intent(activityContext,
							CreateTestStep1Activity.class);
					intent.putExtra(VegaConstants.TEST_ID, examId);
					startActivity(intent);
				}
			});

			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setDialog(position);
				}
			});
			return view;
		}
	}

	/**
	 * The Class ViewHolder.
	 */
	public class ViewHolder {
		
		/** The date. */
		TextView testName, date;
		
		/** The delete. */
		Button edit, delete;
	}
	
	/**
	 * Write exam details to file.
	 *
	 * @param examList the exam list
	 */
	private void writeExamDetailsToFile(List<ServerExamDetails> examList){
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(examList);
		} catch (JsonGenerationException e) {
			Logger.error(tag, e);
		} catch (JsonMappingException e) {
			Logger.error(tag, e);
		} catch (IOException e) {
			Logger.error(tag, e);
		}
		Logger.warn(tag, "file pat is: "+appData.getTestsListFileName());
		ApplicationData.writeToFile("{\"serverExamDetailsList\":"+json+"}", appData.getTestsListFileName());
	}

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#getActivityType()
	 */
	@Override
	public String getActivityType() {
		return tag;
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
	
	/**
	 * Sets the dialog.
	 *
	 * @param position the new dialog
	 */
	private void setDialog(final int position){
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.attendace_sibmit_dialog);
		RelativeLayout layout = (RelativeLayout)dialog.findViewById(R.id.dialoge_layout);
		TextView text = (TextView)dialog.findViewById(R.id.text);
		Button yes = (Button)dialog.findViewById(R.id.dialogue_yes);
		Button no = (Button)dialog.findViewById(R.id.dialogue_no);
		layout.setBackgroundResource(R.drawable.tc_mainbg);
		text.setText(R.string.underconstruction_delete_test_message);
		yes.setText("Yes");
		no.setText("No");
		yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				examList.remove(position);
				studentListAdapter.notifyDataSetChanged();
				if(examList.isEmpty())
					no_tests.setVisibility(View.VISIBLE);
				writeExamDetailsToFile(examList);
				dialog.dismiss();
			}
		});
		no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}
