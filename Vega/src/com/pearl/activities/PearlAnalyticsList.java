package com.pearl.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.analytics.exapandablelist.ExpandListChild;
import com.pearl.analytics.exapandablelist.ExpandListGroup;

public class PearlAnalyticsList extends Activity {

	private String tag = "PearlAnalyticsList";
	private List<String> analyticsList;
	private ImageView menu;
	private ArrayList<ExpandListGroup> analyticsistItems;
	private ExpandableListView analyticsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pearl_analytics_list);
		menu = (ImageView) findViewById(R.id.teacher_menu_button);
		analyticsListView = (ExpandableListView) findViewById(R.id.analyticsList);
		setAdapter();
		
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), ActionBar.class);
				startActivity(i);
			}
		});
		
		analyticsListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String name = analyticsistItems.get(groupPosition).getItems().get(childPosition).getName();
				Intent intent = null;
				if(name.equals("across sections")){
					intent = new Intent(getApplicationContext(), PassFailPercentageAcrossSectionsChart.class);
				}else if(name.equals("across sections for a subject")){
					intent = new Intent(getApplicationContext(), PassFailPercentageAcrossSectionsForASubjectActivity.class);
				} else if(name.equals("across grades")){
					intent = new Intent(getApplicationContext(), PassFailPercentageAcrossGradesChartActivity.class);
				} else if(name.equals("of a student in all subjects for an exam type")){
					intent = new Intent(getApplicationContext(), PerformanceOfStudentsInAllSubjectsActivity.class);
				} else if(name.equals("in a section")){
					intent = new Intent(getApplicationContext(), PassFailPercentageForaSectionActivity.class);
				} else if(name.equals("in a particular grade range")){
					intent = new Intent(getApplicationContext(), StudentCountForARangeInAGradeActivity.class);
				} else if(name.equals("of a student in all exam types")){
					intent = new Intent(getApplicationContext(), PerformanceOfStudentInAllExamTypesActivity.class);
				} else if(name.equals("across sections for a subject in all exams types")){
					intent = new Intent(getApplicationContext(), PerformanceComparisionOfSectionsChartActivity.class);
				} else if(name.equals("across sections for a subject and exam type")){
					intent = new Intent(getApplicationContext(), ComparativeStudyAcrossSectionsChartActivity.class);
				} else if(name.equals("of students in a section")){
					intent = new Intent(getApplicationContext(), ComaprativeStudyForaTestActivity.class);
				} else if(name.equals("year wise"))
					intent = new Intent(getApplicationContext(), YearWiseComparisionActivity.class);
				startActivity(intent);
				return false;
			}
		});
		
		analyticsListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	private void setAdapter(){
		
        analyticsistItems = SetStandardGroups();
		AnalyticsListAdapter ExpAdapter = new AnalyticsListAdapter(this, analyticsistItems);
        analyticsListView.setAdapter(ExpAdapter);
        analyticsListView.expandGroup(0);
        analyticsListView.expandGroup(1);
        analyticsListView.expandGroup(2);
        analyticsListView.expandGroup(3);
	}
	
	private class AnalyticsListAdapter extends BaseExpandableListAdapter{

		private Context context;
		private ArrayList<ExpandListGroup> groups;
		public AnalyticsListAdapter(Context context, ArrayList<ExpandListGroup> groups) {
			this.context = context;
			this.groups = groups;
		}
		
		public void addItem(ExpandListChild item, ExpandListGroup group) {
			if (!groups.contains(group)) {
				groups.add(group);
			}
			int index = groups.indexOf(group);
			ArrayList<ExpandListChild> ch = groups.get(index).getItems();
			ch.add(item);
			groups.get(index).setItems(ch);
		}
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();
			return chList.get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
				ViewGroup parent) {
			ExpandListChild child = (ExpandListChild) getChild(groupPosition, childPosition);
			if (view == null) {
				LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = infalInflater.inflate(R.layout.expandlist_child_item, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.tvChild);
			tv.setText(child.getName().toString());
			tv.setTag(child.getTag());
			// TODO Auto-generated method stub
			return view;
		}

		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();

			return chList.size();

		}

		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groups.get(groupPosition);
		}

		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groups.size();
		}

		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isLastChild, View view,
				ViewGroup parent) {
			ExpandListGroup group = (ExpandListGroup) getGroup(groupPosition);
			if (view == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(R.layout.expandlist_group_item, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.tvGroup);
			tv.setText(group.getName());
			// TODO Auto-generated method stub
			return view;
		}

		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}


		
	}
	
	private ArrayList<ExpandListGroup> SetStandardGroups(){
    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
    	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
        
    	ExpandListGroup gru1 = new ExpandListGroup();
        gru1.setName("Pass/Fail percentages");
        ExpandListChild ch1_1 = new ExpandListChild();
        ch1_1.setName("across sections");
        ch1_1.setTag(null);
        list2.add(ch1_1);
        ExpandListChild ch1_2 = new ExpandListChild();
        ch1_2.setName("across sections for a subject");
        ch1_2.setTag(null);
        list2.add(ch1_2);
        ExpandListChild ch1_3 = new ExpandListChild();
        ch1_3.setName("across grades");
        ch1_3.setTag(null);
        list2.add(ch1_3);
        ExpandListChild ch1_4 = new ExpandListChild();
        ch1_4.setName("in a section");
        ch1_4.setTag(null);
        list2.add(ch1_4);
        gru1.setItems(list2);

        list2 = new ArrayList<ExpandListChild>();
        ExpandListGroup gru2 = new ExpandListGroup();
        gru2.setName("Performance Analysis");
        ExpandListChild ch2_1 = new ExpandListChild();
        ch2_1.setName("of a student in all subjects for an exam type");
        ch2_1.setTag(null);
        list2.add(ch2_1);
        ExpandListChild ch2_2 = new ExpandListChild();
        ch2_2.setName("of a student in all exam types");
        ch2_2.setTag(null);
        list2.add(ch2_2);
        ExpandListChild ch2_4 = new ExpandListChild();
        ch2_4.setName("of students in a section");
        ch2_4.setTag(null);
        list2.add(ch2_4);
        gru2.setItems(list2);
        
        list2 = new ArrayList<ExpandListChild>();
        ExpandListGroup gru3 = new ExpandListGroup();
        gru3.setName("Student Count");
        ExpandListChild ch3_1 = new ExpandListChild();
        ch3_1.setName("in a particular grade range");
        ch3_1.setTag(null);
        list2.add(ch3_1);
        gru3.setItems(list2);
        
        list2 = new ArrayList<ExpandListChild>();
        ExpandListGroup gru4 = new ExpandListGroup();
        gru4.setName("Comparison");
        ExpandListChild ch4_1 = new ExpandListChild();
        ch4_1.setName("year wise");
        ch4_1.setTag(null);
        list2.add(ch4_1);
        ExpandListChild ch4_2 = new ExpandListChild();
        ch4_2.setName("across sections for a subject in all exams types");
        ch4_2.setTag(null);
        list2.add(ch4_2);
        ExpandListChild ch4_3 = new ExpandListChild();
        ch4_3.setName("across sections for a subject and exam type");
        ch4_3.setTag(null);
        list2.add(ch4_3);
        gru4.setItems(list2);
        
        
        list.add(gru1);
        list.add(gru2);
        list.add(gru3);
        list.add(gru4);
        
        return list;
    
	}
}
