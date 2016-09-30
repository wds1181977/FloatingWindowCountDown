package com.example.github.floatingwindowcountdown;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowSmallView extends LinearLayout implements CountDownAnimation.CountDownListener{


	/**
	 * 倒计时计数
	 */
	public static final  int COUNT=5;
	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int windowViewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int windowViewHeight;

	/**
	 * 记录系统状态栏的高度
	 */
	private static int statusBarHeight;

	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;

	/**
	 * 小悬浮窗的布局
	 */
	private ViewGroup smallWindowLayout;



	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;



	
	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int viewHeight;

	/**
	 * 记录当前手指是否按下
	 */
	private boolean isPressed;
	Context mContext;
	SoundPool  mSoundPool;
	   private final SparseIntArray mResourceToSoundId = new SparseIntArray();
	private CountDownAnimation countDownAnimation;
	public FloatWindowSmallView(Context context) {
		super(context);
		mContext=context;

		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		smallWindowLayout = (LinearLayout) findViewById(R.id.small_window_layout);
		viewWidth = smallWindowLayout.getLayoutParams().width;
		viewHeight = smallWindowLayout.getLayoutParams().height;
		windowViewWidth = smallWindowLayout.getLayoutParams().width;
		windowViewHeight = smallWindowLayout.getLayoutParams().height;
		smallWindowLayout.setVisibility(View.VISIBLE);
		TextView percentView = (TextView) findViewById(R.id.percent);
		Animation mCountDownAnim= AnimationUtils.loadAnimation(mContext, R.anim.count_down_exit);
	    mSoundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);

		countDownAnimation = new CountDownAnimation(percentView, getStartCount(),mCountDownAnim);
		countDownAnimation.setCountDownListener(this);
		startCountDownAnimation();
	}
	
    /**
     * Load the sound from a resource.
     */
    public void loadSound(int resourceId) {
        int soundId = mSoundPool.load(mContext, resourceId, 1/* priority */);
        mResourceToSoundId.put(resourceId, soundId);
    }

	@Override
	public void onCountDownEnd(CountDownAnimation animation) {
		//Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
		cancelCountDownAnimation();
       	if(FlogatingWindowManager.isWindowShowing()){
    		FlogatingWindowManager.removeSmallWindow(mContext);
    
    	}
	}


	private void startCountDownAnimation() {

		countDownAnimation.setStartCount(getStartCount());

		countDownAnimation.start();
	}

	private void cancelCountDownAnimation() {
		countDownAnimation.cancel();
	}

	private int getStartCount() {
		return COUNT;
	}



	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}



	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}



}
