package com.yunhuwifi.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.yunhuwifi.activity.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;


public class PullDownListView extends FrameLayout implements
		GestureDetector.OnGestureListener, Animation.AnimationListener {
	public static int MAX_LENGHT = 0;
	public static final int STATE_REFRESH = 1;
	public static final int SCROLL_TO_CLOSE = 2;
	public static final int SCROLL_TO_REFRESH = 3;
	public static final double SCALE = 0.9d;
	private static final int CLOSEDELAY = 300;
	private static final int REFRESHDELAY = 300;
	private Animation mAnimationDown;
	private Animation mAnimationUp;
	private ImageView mArrow;
	private View emptyHeaderView;
	private ProgressBar mProgressBar;
	private int mState;
	private TextView mTitle,time;
	public ListView mListView;
	
	private LinearLayout header,layrefreshok,layrefresh,mFirstChild;
	private GestureDetector mDetector;
	private FlingRunnable mFlinger;
	private int mPading;
	private int mDestPading;
	private int mLastTop;
	private FrameLayout mUpdateContent;
	private OnRefreshListioner mRefreshListioner;
	private boolean listviewDoScroll = false;
	private boolean isFirstLoading = false;
	private boolean mLongPressing;
	private boolean mPendingRemoved = false;
	private String pulldowntorefresh;
	private String releasetorefresh;
	private String loading;
	Rect r = new Rect();
	private MotionEvent downEvent;
	private CheckForLongPress mPendingCheckForLongPress = new CheckForLongPress();
	private CheckForLongPress2 mPendingCheckForLongPress2 = new CheckForLongPress2();
	private float lastY;
	private boolean useempty = true;

	private class CheckForLongPress implements Runnable {
		public void run() {
			if (mListView.getOnItemLongClickListener() == null) {
			} else {
				postDelayed(mPendingCheckForLongPress2, 100);
			}
		}
	}

	private class CheckForLongPress2 implements Runnable {
		public void run() {
			mLongPressing = true;
			MotionEvent e = MotionEvent.obtain(downEvent.getDownTime(),downEvent.getEventTime()
							+ ViewConfiguration.getLongPressTimeout(),
					MotionEvent.ACTION_CANCEL, downEvent.getX(),
					downEvent.getY(), downEvent.getMetaState());
			PullDownListView.super.dispatchTouchEvent(e);
		}
	}

	class FlingRunnable implements Runnable {

		private void startCommon() {
			removeCallbacks(this);
		}

		public void run() {
			boolean noFinish = mScroller.computeScrollOffset();
			int curY = mScroller.getCurrY();
			int deltaY = curY - mLastFlingY;
			if (noFinish) {
				move(deltaY, true);
				mLastFlingY = curY;
				post(this);
			} else {
				removeCallbacks(this);
				if (mState == SCROLL_TO_CLOSE) {
					mState = -1;
				}
			}
		}

		public void startUsingDistance(int distance, int duration) {
			if (distance == 0)
				distance--;
			startCommon();
			mLastFlingY = 0;
			mScroller.startScroll(0, 0, 0, distance, duration);
			post(this);
		}

		private int mLastFlingY;
		private Scroller mScroller;

		public FlingRunnable() {
			mScroller = new Scroller(getContext());
		}
	}

	
	public interface OnRefreshListioner {
		public abstract void onRefresh();
	}
	
	public PullDownListView(Context context) {
		super(context);
		mDetector = new GestureDetector(context, this);
		mFlinger = new FlingRunnable();
		init();
		addRefreshBar();
	}
	
	public PullDownListView(Context context, AttributeSet att) {
		super(context, att);
		useempty = att.getAttributeBooleanValue(null, "useempty", true);
		mDetector = new GestureDetector(this);
		mFlinger = new FlingRunnable();
		init();
		addRefreshBar();
	}

	View view;

	private void addRefreshBar() {

		mAnimationUp = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_up);
		mAnimationUp.setAnimationListener(this);
		
		mAnimationDown = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_down);
		mAnimationDown.setAnimationListener(this);
		
		view = LayoutInflater.from(getContext()).inflate(R.layout.refresh_header,null);
		addView(view);
		
		
		mFirstChild = (LinearLayout) view;
		mUpdateContent = (FrameLayout) getChildAt(0).findViewById(R.id.flcontent);
		mArrow = new ImageView(getContext());
		FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mArrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
		mArrow.setLayoutParams(layoutparams);
		mArrow.setImageResource(R.drawable.downarrow);
		mUpdateContent.addView(mArrow);
		FrameLayout.LayoutParams layoutparams1 = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layoutparams1.gravity = Gravity.CENTER;
		mProgressBar = new ProgressBar(getContext(), null,
				android.R.attr.progressBarStyleSmallInverse);
		Drawable d=this.getResources().getDrawable(R.drawable.refreshprogress);
		d.setBounds(1,1,16,16);
		mProgressBar.setIndeterminateDrawable(d);
		mProgressBar.setIndeterminate(false);
		int i = getResources().getDimensionPixelSize(R.dimen.updatebar_padding);
		mProgressBar.setPadding(i, i, i, i);
		mProgressBar.setLayoutParams(layoutparams1);
		mUpdateContent.addView(mProgressBar);
		mTitle = (TextView) findViewById(R.id.tv_title);
		time=(TextView) findViewById(R.id.time);
		layrefreshok=(LinearLayout) findViewById(R.id.layrefreshok);
		layrefresh=(LinearLayout) findViewById(R.id.layrefresh);
		layrefresh.setVisibility(View.VISIBLE);
		layrefreshok.setVisibility(View.GONE);
	}

	public void setGone() {
		mTitle.setVisibility(View.GONE);
		mUpdateContent.setVisibility(View.GONE);
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		mListView = (ListView) getChildAt(1);

		if (useempty) {
			header = (LinearLayout) LayoutInflater.from(getContext()).inflate(
					R.layout.empty_main, null);
			mListView.addHeaderView(header);
	
		mListView.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			public void onScroll(AbsListView view, int f, int v, int t) {
			}
		});
		}
	}


	public void setEmptyHeaderView(View empty) {
		emptyHeaderView = empty;
	}

	
	public void addEmptyHeaderView() {
		header.removeAllViews();
		if (emptyHeaderView != null)
			header.addView(emptyHeaderView);
	}
	
	public void removeEmptyHeaderView() {
		if (emptyHeaderView != null)
			header.removeView(emptyHeaderView);
	}

	
	private void init() {
		MAX_LENGHT = getResources().getDimensionPixelSize(
				R.dimen.updatebar_height);
		setDrawingCacheEnabled(false);
		setBackgroundDrawable(null);
		setClipChildren(false);
		mDetector.setIsLongpressEnabled(false);
		mPading = -MAX_LENGHT;
		mLastTop = -MAX_LENGHT;
		pulldowntorefresh ="下拉刷新";
		releasetorefresh ="松开刷新";
		loading = "正在加载";
	}

	private boolean move(float deltaY, boolean auto) {
		if (deltaY > 0 && mFirstChild.getTop() == -MAX_LENGHT) {
			mPading = -MAX_LENGHT;
			return false;
		}

		if (auto) {
			if (mFirstChild.getTop() - deltaY < mDestPading) {
				deltaY = mFirstChild.getTop() - mDestPading;
			}
			mFirstChild.offsetTopAndBottom((int) -deltaY);
			mListView.offsetTopAndBottom((int) -deltaY);
			mPading = mFirstChild.getTop();
			if (mDestPading == 0 && mFirstChild.getTop() == 0
					&& mState == SCROLL_TO_REFRESH) {
				onRefresh();
			} else if (mDestPading == -MAX_LENGHT) {
			}
			invalidate();
			updateView();
			return true;
		} else {
			if (mState != STATE_REFRESH
					|| (mState == STATE_REFRESH && deltaY > 0)) {
				mFirstChild.offsetTopAndBottom((int) -deltaY);
				mListView.offsetTopAndBottom((int) -deltaY);
				mPading = mFirstChild.getTop();
			} else if (mState == STATE_REFRESH && deltaY < 0
					&& mFirstChild.getTop() <= 0) {
				if (mFirstChild.getTop() - deltaY > 0) {
					deltaY = mFirstChild.getTop();
				}
				mFirstChild.offsetTopAndBottom((int) -deltaY);
				mListView.offsetTopAndBottom((int) -deltaY);
				mPading = mFirstChild.getTop();
			}
		}
		if (deltaY > 0 && mFirstChild.getTop() <= -MAX_LENGHT) {
			mPading = -MAX_LENGHT;
			deltaY = -MAX_LENGHT - mFirstChild.getTop();
			mFirstChild.offsetTopAndBottom((int) deltaY);
			mListView.offsetTopAndBottom((int) deltaY);
			mPading = mFirstChild.getTop();
			updateView();
			invalidate();
			return false;
		}
		updateView();
		invalidate();
		return true;
	}

	private void updateView() {
		String s = "";
		if (mState != STATE_REFRESH) {
			if (mFirstChild.getTop() < 0) {
				mArrow.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.INVISIBLE);
				mTitle.setText(pulldowntorefresh);

				if (mLastTop >= 0 && mState != SCROLL_TO_CLOSE) {
					mArrow.startAnimation(mAnimationUp);
				}

			} else if (mFirstChild.getTop() > 0) {
				mTitle.setText(releasetorefresh + s);
				mProgressBar.setVisibility(View.INVISIBLE);
				mArrow.setVisibility(View.VISIBLE);

				if (mLastTop <= 0) {
					mArrow.startAnimation(mAnimationDown);
				}
			}
		}
		mLastTop = mFirstChild.getTop();
	}

	private boolean release() {
		if (listviewDoScroll) {
			listviewDoScroll = false;
			return true;
		}
		if (mFirstChild.getTop() > 0) {
			scrollToUpdate(false);
		} else {
			scrollToClose();
		}
		invalidate();
		return false;
	}

	private void scrollToClose() {
		mDestPading = -MAX_LENGHT;
		mFlinger.startUsingDistance(MAX_LENGHT, CLOSEDELAY);
	}

	public void scrollToUpdate(boolean load) {
		mState = SCROLL_TO_REFRESH;

		mDestPading = 0;
		if (load) {
			mFlinger.startUsingDistance(50, REFRESHDELAY);
			load = false;
		} else
			mFlinger.startUsingDistance(mFirstChild.getTop(), REFRESHDELAY);
	}

	private void onRefresh() {

		mState = STATE_REFRESH;
		mTitle.setText(loading);
		mProgressBar.setVisibility(View.VISIBLE);
		mArrow.setVisibility(View.INVISIBLE);
		if (mRefreshListioner != null) {
			mRefreshListioner.onRefresh();
		}
	}

	public void onRefreshComplete() {
		SimpleDateFormat date=new SimpleDateFormat("hh:mm:ss");
		String time=date.format(new Date());
//		layrefresh.setVisibility(View.GONE);
//		layrefreshok.setVisibility(View.VISIBLE);
		onRefreshComplete(time);
	}

	public void onRefreshComplete(String date) {
		time.setText("最近更新"+date);
		mState = SCROLL_TO_CLOSE;
		mArrow.setImageResource(R.drawable.downarrow);
//		layrefreshok.setVisibility(View.GONE);
//		layrefresh.setVisibility(View.VISIBLE);
		updateCommon();
		scrollToClose();
	}


	private void updateCommon() {
		if (mListView.getCount() == (mListView.getHeaderViewsCount() + mListView
				.getFooterViewsCount())) {
			Log.e("out", "数据为空");
			if (useempty)
				addEmptyHeaderView();
		} else {
			removeEmptyHeaderView();
			mListView.setFooterDividersEnabled(false);
		}
	}





	public boolean dispatchTouchEvent(MotionEvent e) {
		if (isFirstLoading) {
			return false;
		}
		int action;
		float y = e.getY();
		action = e.getAction();
		if (mLongPressing && action != MotionEvent.ACTION_DOWN) {
			return false;
		}
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			mLongPressing = true;
		}
		boolean handled = true;
		handled = mDetector.onTouchEvent(e);
		switch (action) {
		case MotionEvent.ACTION_UP:
			boolean f1 = mListView.getTop() <= e.getY()
					&& e.getY() <= mListView.getBottom();
			if (!handled && mFirstChild.getTop() == -MAX_LENGHT && f1
					|| mState == STATE_REFRESH) {
				super.dispatchTouchEvent(e);
			} else {
				//执行释放方法 
				handled = release();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			handled = release();
			super.dispatchTouchEvent(e);
			break;
		case MotionEvent.ACTION_DOWN:
			downEvent = e;
			mLongPressing = false;
			//长按的时间间隔
			postDelayed(mPendingCheckForLongPress,
					ViewConfiguration.getLongPressTimeout() + 100);
			mPendingRemoved = false;
			super.dispatchTouchEvent(e);
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaY = lastY - y;
			lastY = y;
			if (!mPendingRemoved) {
				removeCallbacks(mPendingCheckForLongPress);
				mPendingRemoved = true;
			}

			if (!handled && mFirstChild.getTop() == -MAX_LENGHT) {
				try {
					return super.dispatchTouchEvent(e);
				} catch (Exception e2) {
					e2.printStackTrace();
					return true;
				}
			} else if (handled && mListView.getTop() > 0 && deltaY < 0) {// deltaY小于0，向�?
				e.setAction(MotionEvent.ACTION_CANCEL);
				super.dispatchTouchEvent(e);
			}
			break;

		default:
			break;
		}

		return true;
	}

	public void onAnimationEnd(Animation animation) {
		int top = mFirstChild.getTop();
		if (top < 0)
			mArrow.setImageResource(R.drawable.downarrow);
		else if (top > 0)
			mArrow.setImageResource(R.drawable.uparrow);
		else {
			if (top < mLastTop) {
				mArrow.setImageResource(R.drawable.downarrow);
			} else {
				mArrow.setImageResource(R.drawable.uparrow);
			}
		}
	}

	public void onAnimationRepeat(Animation animation) {
	}

	public void onAnimationStart(Animation animation) {
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent motionevent, MotionEvent e, float f,
			float f1) {
		return false;
	}

	protected void onLayout(boolean flag, int i, int j, int k, int l) {
		int top = mPading;
		int w = getMeasuredWidth();
		mFirstChild.layout(0, top, w, top + MAX_LENGHT);

		int h = getMeasuredHeight() + mPading + MAX_LENGHT;
		mListView.layout(0, top + MAX_LENGHT, w, h);
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent curdown, MotionEvent cur, float deltaX,
			float deltaY) {
		deltaY = (float) ((double) deltaY * SCALE);
		boolean handled = false;
		boolean flag = false;
		if (mListView.getCount() == 0) {
			flag = true;
		} else {
			View c = mListView.getChildAt(0);
			if (mListView.getFirstVisiblePosition() == 0 && c != null
					&& c.getTop() == 0) {
				flag = true;
			}
		}
		if (deltaY < 0F && flag || getChildAt(0).getTop() > -MAX_LENGHT) { 
			handled = move(deltaY, false);
		} else
			handled = false;
		return handled;
	}

	public void onShowPress(MotionEvent motionevent) {
	}

	public boolean onSingleTapUp(MotionEvent motionevent) {
		return false;
	}

	public void setRefreshListioner(OnRefreshListioner RefreshListioner) {
		mRefreshListioner = RefreshListioner;
	}

}
