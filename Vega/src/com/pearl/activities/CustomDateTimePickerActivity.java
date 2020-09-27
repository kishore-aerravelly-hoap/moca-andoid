package com.pearl.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ViewSwitcher;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomDateTimePickerActivity.
 */
public class CustomDateTimePickerActivity implements OnClickListener{
    
    /** The date picker. */
    private DatePicker datePicker;
    
    /** The time picker. */
    private TimePicker timePicker;
    
    /** The view switcher. */
    private ViewSwitcher viewSwitcher;


    /** The cancel. */
    private final int SET_DATE = 100, SET_TIME = 101, SET = 102, CANCEL = 103;

    /** The btn_cancel. */
    private Button btn_setDate, btn_setTime, btn_set, btn_cancel;

    /** The calendar_date. */
    private Calendar calendar_date = null;
    
    /** The fromdate. */
    private final Calendar fromdate;
    
    /** The to date. */
    private final Calendar toDate;

    /** The activity. */
    private final Activity activity;

    /** The i custom date time listener. */
    private ICustomDateTimeListener iCustomDateTimeListener = null;

    /** The dialog. */
    private final Dialog dialog;

    /** The is24 hour view. */
    private boolean is24HourView = true;
    
    /** The is auto dismiss. */
    private final boolean isAutoDismiss = true;

    /** The selected minute. */
    private int selectedHour, selectedMinute;

    /**
     * Instantiates a new custom date time picker activity.
     *
     * @param a the a
     * @param fromdate the fromdate
     * @param toDate the to date
     * @param customDateTimeListener the custom date time listener
     */
    public CustomDateTimePickerActivity(Activity a,Calendar fromdate,Calendar toDate,
	    ICustomDateTimeListener customDateTimeListener) {
	Log.w("", "from date in constructor is:"+fromdate);
	activity = a;
	iCustomDateTimeListener = customDateTimeListener;
	this.fromdate = fromdate;
	this.toDate = toDate;
	dialog = new Dialog(activity);
	dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	dialog.setOnDismissListener(new OnDismissListener() {
	    @Override
	    public void onDismiss(DialogInterface dialog) {
		resetData();
	    }
	});

	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	final View dialogView = getDateTimePickerLayout();
	dialog.setContentView(dialogView);
    }

    /**
     * Gets the date time picker layout.
     *
     * @return the date time picker layout
     */
    public View getDateTimePickerLayout() {
	final LinearLayout.LayoutParams linear_match_wrap = new LinearLayout.LayoutParams(
		LayoutParams.MATCH_PARENT,
		LayoutParams.MATCH_PARENT);
	final LinearLayout.LayoutParams linear_wrap_wrap = new LinearLayout.LayoutParams(
		LayoutParams.WRAP_CONTENT,
		LayoutParams.WRAP_CONTENT);
	final FrameLayout.LayoutParams frame_match_wrap = new FrameLayout.LayoutParams(
		LayoutParams.MATCH_PARENT,
		327);

	final LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(
		0, 62, 1.0f);

	final LinearLayout linear_main = new LinearLayout(activity);
	linear_main.setLayoutParams(linear_match_wrap);
	linear_main.setOrientation(LinearLayout.VERTICAL);
	linear_main.setGravity(Gravity.CENTER);

	final LinearLayout linear_child = new LinearLayout(activity);
	linear_child.setLayoutParams(linear_wrap_wrap);
	linear_child.setOrientation(LinearLayout.VERTICAL);
	linear_main.setGravity(Gravity.CENTER);

	final LinearLayout linear_top = new LinearLayout(activity);
	linear_top.setLayoutParams(linear_match_wrap);

	btn_setDate = new Button(activity);
	btn_setDate.setLayoutParams(button_params);
	btn_setDate.setText("Set Date");
	btn_setDate.setId(SET_DATE);
	btn_setDate.setOnClickListener(this);

	btn_setTime = new Button(activity);
	btn_setTime.setLayoutParams(button_params);
	btn_setTime.setText("Set Time");
	btn_setTime.setId(SET_TIME);
	btn_setTime.setOnClickListener(this);

	linear_top.addView(btn_setDate);
	linear_top.addView(btn_setTime);

	viewSwitcher = new ViewSwitcher(activity);
	viewSwitcher.setLayoutParams(frame_match_wrap);

	datePicker = new DatePicker(activity);
	timePicker = new TimePicker(activity);
	timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {


	    @Override
	    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		Log.w("tag", "Time - Set the time");	
		final Calendar calendar = Calendar.getInstance();
		final int currenthour = calendar.get(Calendar.HOUR_OF_DAY);
		final int currentminute = calendar.get(Calendar.MINUTE);
		final int currentdate = calendar.get(Calendar.DATE);
		Log.w("", "current date is:"+currentdate);
		selectedHour = hourOfDay;
		selectedMinute = minute;

		if(selectedHour < currenthour || selectedMinute < currentminute)
		{
		    //Toast.makeText(activity, "Select current time", 1000).show();
		}else  {

		}
	    }
	});

	viewSwitcher.addView(timePicker);
	viewSwitcher.addView(datePicker);

	final LinearLayout linear_bottom = new LinearLayout(activity);
	linear_match_wrap.topMargin = 6;
	linear_bottom.setLayoutParams(linear_match_wrap);

	btn_set = new Button(activity);
	btn_set.setLayoutParams(button_params);
	btn_set.setText("Set");
	btn_set.setId(SET);
	btn_set.setOnClickListener(this);

	btn_cancel = new Button(activity);
	btn_cancel.setLayoutParams(button_params);
	btn_cancel.setText("Cancel");
	btn_cancel.setId(CANCEL);
	btn_cancel.setOnClickListener(this);

	linear_bottom.addView(btn_set);
	linear_bottom.addView(btn_cancel);

	linear_child.addView(linear_top);
	linear_child.addView(viewSwitcher);
	linear_child.addView(linear_bottom);

	linear_main.addView(linear_child);

	return linear_main;
    }

    /**
     * Show dialog.
     *
     * @param type the type
     */
    public void showDialog(final String type) {
	if (!dialog.isShowing()) {
	    Log.w("tag", "Date - dialog is not being shown so build the dialog");
	    if (calendar_date == null)
		calendar_date = Calendar.getInstance();

	    selectedHour = calendar_date.get(Calendar.HOUR_OF_DAY);
	    selectedMinute = calendar_date.get(Calendar.MINUTE);

	    timePicker.setIs24HourView(is24HourView);
	    timePicker.setCurrentHour(selectedHour);
	    timePicker.setCurrentMinute(selectedMinute);

	    //datePicker.setMinDate(System.currentTimeMillis());
	    datePicker.init(calendar_date.get(Calendar.YEAR), calendar_date.get(Calendar.MONTH), calendar_date.get(Calendar.DATE),
		    new OnDateChangedListener() {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {

		    final Calendar calendar = Calendar.getInstance();
		    calendar.set(year, monthOfYear, dayOfMonth);
		    Log.w("", "date is:"+dayOfMonth);
		    Log.w("", "year is:"+year);
		    Log.w("", "month is:"+monthOfYear);
		    if(type.equalsIgnoreCase("fromdate")){
			if(calendar.before(Calendar.getInstance())){
			    // Toast.makeText(activity, "Select today's date", 10000).show();
			} else {

			}
		    }else if(type.equalsIgnoreCase("todate")){
			Log.w("","fromdate is" +fromdate);
			if(calendar.before(Calendar.getInstance())  || calendar.before(fromdate) ){
			    // Toast.makeText(activity, "Select today's date", 10000).show();

			} else {

			}
		    }
		}
	    });
	    datePicker.updateDate(calendar_date.get(Calendar.YEAR),calendar_date.get(Calendar.MONTH),
		    calendar_date.get(Calendar.DATE));
	    //datePicker.setMinDate(calendar_date.get(Calendar.DAY_OF_MONTH | Calendar.MONTH | Calendar.YEAR));

	    dialog.show();
	    btn_setDate.performClick();
	}
    }

    /*public void setAutoDismiss(boolean isAutoDismiss) {
		this.isAutoDismiss = isAutoDismiss;
	}*/

    /*public void dismissDialog() {
		if (!dialog.isShowing())
			dialog.dismiss();
	}*/

    /**
     * Sets the date.
     *
     * @param calendar the new date
     */
    public void setDate(Calendar calendar) {
	if (calendar != null)
	    calendar_date = calendar;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(Date date) {
	if (date != null) {
	    calendar_date = Calendar.getInstance();
	    calendar_date.setTime(date);
	}
    }

    /**
     * Sets the date.
     *
     * @param year the year
     * @param month the month
     * @param day the day
     */
    public void setDate(int year, int month, int day) {
	if (month < 12 && month >= 0 && day < 32 && day >= 0 && year > 100
		&& year < 3000) {
	    calendar_date = Calendar.getInstance();
	    calendar_date.set(year, month, day);
	}
    }

    /**
     * Sets the time in24 hour format.
     *
     * @param hourIn24Format the hour in24 format
     * @param minute the minute
     */
    public void setTimeIn24HourFormat(int hourIn24Format, int minute) {
	if (hourIn24Format < 24 && hourIn24Format >= 0 && minute >= 0
		&& minute < 60) {
	    if (calendar_date == null)
		calendar_date = Calendar.getInstance();

	    calendar_date.set(calendar_date.get(Calendar.YEAR),
		    calendar_date.get(Calendar.MONTH),
		    calendar_date.get(Calendar.DAY_OF_MONTH), hourIn24Format,
		    minute);

	    is24HourView = true;
	}
    }

    /**
     * Sets the time in12 hour format.
     *
     * @param hourIn12Format the hour in12 format
     * @param minute the minute
     * @param isAM the is am
     */
    public void setTimeIn12HourFormat(int hourIn12Format, int minute,
	    boolean isAM) {
	if (hourIn12Format < 13 && hourIn12Format > 0 && minute >= 0
		&& minute < 60) {
	    if (hourIn12Format == 12)
		hourIn12Format = 0;

	    if (!isAM) {
	    }

	    if (calendar_date == null)
		calendar_date = Calendar.getInstance();

	    calendar_date.set(calendar_date.get(Calendar.YEAR),
		    calendar_date.get(Calendar.MONTH),
		    calendar_date.get(Calendar.DAY_OF_MONTH), hourIn12Format,
		    minute);

	    is24HourView = false;
	}
    }

    /**
     * Sets the 24 hour format.
     *
     * @param is24HourFormat the new 24 hour format
     */
    public void set24HourFormat(boolean is24HourFormat) {
	is24HourView = is24HourFormat;
    }

    /**
     * The listener interface for receiving ICustomDateTime events.
     * The class that is interested in processing a ICustomDateTime
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addICustomDateTimeListener<code> method. When
     * the ICustomDateTime event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ICustomDateTimeEvent
     */
    public interface ICustomDateTimeListener {
	
	/**
	 * On set.
	 *
	 * @param dialog the dialog
	 * @param calendarSelected the calendar selected
	 * @param dateSelected the date selected
	 * @param year the year
	 * @param monthFullName the month full name
	 * @param monthShortName the month short name
	 * @param monthNumber the month number
	 * @param date the date
	 * @param weekDayFullName the week day full name
	 * @param weekDayShortName the week day short name
	 * @param hour24 the hour24
	 * @param hour12 the hour12
	 * @param min the min
	 * @param sec the sec
	 * @param AM_PM the am pm
	 */
	public void onSet(Dialog dialog, Calendar calendarSelected,
		Date dateSelected, int year, String monthFullName,
		String monthShortName, int monthNumber, int date,
		String weekDayFullName, String weekDayShortName, int hour24,
		int hour12, int min, int sec, String AM_PM);


	/**
	 * On cancel.
	 */
	public void onCancel();
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case SET_DATE:
	    btn_setTime.setEnabled(true);
	    btn_setDate.setEnabled(false);
	    viewSwitcher.showNext();
	    break;

	case SET_TIME:
	    btn_setTime.setEnabled(false);
	    btn_setDate.setEnabled(true);
	    viewSwitcher.showPrevious();
	    break;

	case SET:
	    if (iCustomDateTimeListener != null) {
		final int month = datePicker.getMonth();
		final int year = datePicker.getYear();
		final int day = datePicker.getDayOfMonth();

		calendar_date.set(year, month, day, selectedHour,
			selectedMinute);

		iCustomDateTimeListener.onSet(dialog, calendar_date,
			calendar_date.getTime(), calendar_date
			.get(Calendar.YEAR),
			getMonthFullName(calendar_date.get(Calendar.MONTH)),
			getMonthShortName(calendar_date.get(Calendar.MONTH)),
			calendar_date.get(Calendar.MONTH), calendar_date
			.get(Calendar.DAY_OF_MONTH),
			getWeekDayFullName(calendar_date
				.get(Calendar.DAY_OF_WEEK)),
				getWeekDayShortName(calendar_date
					.get(Calendar.DAY_OF_WEEK)), calendar_date
					.get(Calendar.HOUR_OF_DAY),
					getHourIn12Format(calendar_date
						.get(Calendar.HOUR_OF_DAY)), calendar_date
						.get(Calendar.MINUTE), calendar_date
						.get(Calendar.SECOND), getAMPM(calendar_date));
	    }
	    if (dialog.isShowing() && isAutoDismiss)
		dialog.dismiss();
	    break;

	case CANCEL:
	    if (iCustomDateTimeListener != null)
		iCustomDateTimeListener.onCancel();
	    if (dialog.isShowing())
		dialog.dismiss();
	    break;
	}
    }

    /**
     * Convert date.
     *
     * @param date the date
     * @param fromFormat the from format
     * @param toFormat the to format
     * @return the string
     */
    public static String convertDate(String date, String fromFormat,
	    String toFormat) {
	try {
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromFormat);
	    final Date d = simpleDateFormat.parse(date);
	    final Calendar calendar = Calendar.getInstance();
	    calendar.setTime(d);

	    simpleDateFormat = new SimpleDateFormat(toFormat);
	    simpleDateFormat.setCalendar(calendar);
	    date = simpleDateFormat.format(calendar.getTime());

	} catch (final Exception e) {
	    e.printStackTrace();
	}

	return date;
    }

    /**
     * Gets the month full name.
     *
     * @param monthNumber the month number
     * @return the month full name
     */
    private String getMonthFullName(int monthNumber) {
	final Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.MONTH, monthNumber);

	final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
	simpleDateFormat.setCalendar(calendar);
	final String monthName = simpleDateFormat.format(calendar.getTime());

	return monthName;
    }

    /**
     * Gets the month short name.
     *
     * @param monthNumber the month number
     * @return the month short name
     */
    private String getMonthShortName(int monthNumber) {
	final Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.MONTH, monthNumber);

	final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
	simpleDateFormat.setCalendar(calendar);
	final String monthName = simpleDateFormat.format(calendar.getTime());

	return monthName;
    }

    /**
     * Gets the week day full name.
     *
     * @param weekDayNumber the week day number
     * @return the week day full name
     */
    private String getWeekDayFullName(int weekDayNumber) {
	final Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);

	final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
	simpleDateFormat.setCalendar(calendar);
	final String weekName = simpleDateFormat.format(calendar.getTime());

	return weekName;
    }

    /**
     * Gets the week day short name.
     *
     * @param weekDayNumber the week day number
     * @return the week day short name
     */
    private String getWeekDayShortName(int weekDayNumber) {
	final Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);

	final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE");
	simpleDateFormat.setCalendar(calendar);
	final String weekName = simpleDateFormat.format(calendar.getTime());

	return weekName;
    }

    /**
     * Gets the hour in12 format.
     *
     * @param hour24 the hour24
     * @return the hour in12 format
     */
    private int getHourIn12Format(int hour24) {
	int hourIn12Format = 0;

	if (hour24 == 0)
	    hourIn12Format = 12;
	else if (hour24 <= 12)
	    hourIn12Format = hour24;
	else
	    hourIn12Format = hour24 - 12;

	return hourIn12Format;
    }

    /**
     * Gets the ampm.
     *
     * @param calendar the calendar
     * @return the ampm
     */
    private String getAMPM(Calendar calendar) {
	final String ampm = calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM"
		: "PM";
	return ampm;
    }

    /**
     * Reset data.
     */
    private void resetData() {
	calendar_date = null;
	is24HourView = true;
    }
}


