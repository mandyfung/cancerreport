package com.example.cancerreport;

import com.example.cancerreport.mockdata.MockData;
import com.example.cancerreport.util.SystemUiHider;
import com.jjoe64.graphview.GraphView;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Main extends Activity {
	private float ratingValue;
	private String hoursSleptValue;
	private String incidentsValue;
	private String bodyWeightValue;
	private String exerciseValue;
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = false;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("DIDIGETHERECREATE");
		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		/*contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}

		});
		*/

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		//findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

		setUp(contentView);

	}

	private void setUp(View contentView){
		setDate();
		addListenerOnRatingBar();
		addListenerOnHoursSlept();
		addListenerOnExerciseHours();
		addListenerOnIncidents();
		addListenerOnBodyWeight();
		addListenerOnButton();
		generateGraphs(contentView);

	}
	private void setDate(){
		long date = System.currentTimeMillis();

		TextView tv = (TextView) findViewById(R.id.dateID);
		String dt;
		Date cal = (Date) Calendar.getInstance().getTime();
		dt = cal.toLocaleString();
		tv.setText(dt.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		String dateString = sdf.format(date);
		tv.setText(dateString);

		RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
		rb.setRating(ratingValue);

		TextView tv1 = (TextView) findViewById(R.id.hoursEntry);
		tv1.setText(hoursSleptValue);
		TextView tv2 = (TextView) findViewById(R.id.IncidentsEntry);
		tv2.setText(incidentsValue);
		TextView tv3 = (TextView) findViewById(R.id.BodyWeightEntry);
		tv3.setText(bodyWeightValue);
		TextView tv4 = (TextView) findViewById(R.id.physicalActivityEntry);
		tv4.setText(exerciseValue);
	}
	private void generateGraphs(final View contentView) {
		Button generateGraphBtn = (Button) findViewById(R.id.graphID);
		final GraphView graph = new GraphView(this);
		final TextView tv = new TextView(this);
		final LinearLayout layout = new LinearLayout(this);
		final Button backBtn = new Button(this);
		View.OnClickListener handler = new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				ReportGenerator reportGenerator = new ReportGenerator(MockData.getXPoints(), MockData.getYPoints());
				reportGenerator.createGraph(graph);
				graph.setTitle("Comfort Level");
				graph.setScaleX((float)0.8);
				graph.setScaleY((float)0.8);
				layout.addView(graph);

				View.OnClickListener backBtnHandler = new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						setContentView(R.layout.activity_fullscreen);
						setUp(contentView);
					}
				};
				graph.setOnClickListener(backBtnHandler);
				setContentView(layout);
			}
			
		};
		generateGraphBtn.setOnClickListener(handler);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	private RatingBar ratingBar;
	private TextView txtRatingValue;
	//private float ratingValue;

	public void addListenerOnRatingBar() {
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		//txtRatingValue = (TextView)
		//ratingValue = ratingBar.getRating();

		//if rating value is changed,
		//display the current rating value in the result (textview) automatically
		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
										boolean fromUser) {

				//txtRatingValue.setText(String.valueOf(rating));
				System.out.println(rating);
				ratingValue = rating;
			}
		});
	}

	public void addListenerOnHoursSlept() {
		final EditText ratingBar = (EditText) findViewById(R.id.hoursEntry);
		final String[] YourTextString = new String[1];
		ratingBar.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				YourTextString[0] = ratingBar.getText().toString();
				System.out.println(YourTextString[0]);
				hoursSleptValue = YourTextString[0];
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
		});
	}

	public void addListenerOnIncidents() {
		final EditText ratingBar = (EditText) findViewById(R.id.IncidentsEntry);
		final String[] YourTextString = new String[1];
		ratingBar.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				YourTextString[0] = ratingBar.getText().toString();
				System.out.println(YourTextString[0]);
				incidentsValue = YourTextString[0];
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
		});
	}

	public void addListenerOnBodyWeight() {
		final EditText ratingBar = (EditText) findViewById(R.id.BodyWeightEntry);
		final String[] YourTextString = new String[1];
		ratingBar.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				YourTextString[0] = ratingBar.getText().toString();
				System.out.println(YourTextString[0]);
				bodyWeightValue = YourTextString[0];
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
		});
	}

	public void addListenerOnExerciseHours() {
		final EditText ratingBar = (EditText) findViewById(R.id.physicalActivityEntry);
		final String[] YourTextString = new String[1];
		ratingBar.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				YourTextString[0] = ratingBar.getText().toString();
				System.out.println(YourTextString[0]);
				exerciseValue = YourTextString[0];
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
		});
	}

	public void addListenerOnButton() {
		Button btnSubmit = (Button) findViewById(R.id.dummy_button);

		//if click on me, then display the current rating value.
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(Main.this)
						.setTitle("Saved Sucessfully")
						.setMessage("Your status has been successfully saved today.")
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// continue with save
							}
						})
						.setIcon(android.R.drawable.ic_dialog_info)
						.create()
						.show();

			}

		});

	}
}
