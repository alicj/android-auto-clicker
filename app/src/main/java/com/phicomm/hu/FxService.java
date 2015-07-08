package com.phicomm.hu;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.commons.lang.time.StopWatch;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class FxService extends Service 
{

	//定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;
	
	Button mFloatStart;
    Button mFloatDrag;
    Button mFloatSetting;
    Button mFloatStop;
    Button mFloatCap;
    Boolean click = false;

    private static final String TAG = "FxService";
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "oncreat");
		createFloatView();
        //Toast.makeText(FxService.this, "create FxService", Toast.LENGTH_LONG);		
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		//获取WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		//设置window type
		wmParams.type = LayoutParams.TYPE_PHONE; 
		//设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.OPAQUE;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = 
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
          LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
          ;
        
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/
        
        //设置悬浮窗口长宽数据  
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        
        Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
        Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
        Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
        Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());      
        
        //浮动窗口按钮
        mFloatStart = (Button)mFloatLayout.findViewById(R.id.start_id);
        mFloatDrag = (Button) mFloatLayout.findViewById(R.id.drag_id);
//        mFloatSetting = (Button) mFloatLayout.findViewById(R.id.setting_id);
//        mFloatStop = (Button) mFloatLayout.findViewById(R.id.stop_id);
        mFloatCap = (Button) mFloatLayout.findViewById(R.id.cap_id);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatStart.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mFloatStart.getMeasuredHeight() / 2);

        //设置监听浮动窗口的触摸移动
        mFloatDrag.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mFloatLayout.getMeasuredWidth() / 2;
                //Log.i(TAG, "Width/2--->" + mFloatStart.getMeasuredWidth()/2);
//				Log.i(TAG, "RawX" + event.getRawX());
//				Log.i(TAG, "X" + event.getX());
                //25为状态栏的高度
                wmParams.y = (int) event.getRawY() - mFloatLayout.getMeasuredHeight() -25;
                // Log.i(TAG, "Width/2--->" + mFloatStart.getMeasuredHeight()/2);
//	            Log.i(TAG, "RawY" + event.getRawY());
//	            Log.i(TAG, "Y" + event.getY());
                //刷新
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });

        mFloatStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked on 'Start'");
//                float x = mFloatLayout.getMeasuredWidth() / 2;
//                float y = mFloatLayout.getMeasuredHeight() / 2;
                float x = 10;
                float y = 10;
                for (int i = 0; i <  10; i ++) {
                    autoClick2(x, y);
                }
            }
        });

//        mFloatStop.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                click = false;
//            }
//        });

        mFloatCap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FxService.this, "Setting clicked", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "clicked on 'Setting'");
                screenCap();
            }
        });
	}
	
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mFloatLayout != null)
		{
			mWindowManager.removeView(mFloatLayout);
		}
	}

	private void autoClick(float x, float y) {
		// Obtain MotionEvent object
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
//		x = 0.0f;
//		y = 0.0f;
		// List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
		int metaState = 0;
		MotionEvent keyDown = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );

        MotionEvent keyUp = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        Log.i(TAG, x + ", " + y);

		// Dispatch touch event to view
		mFloatLayout.getRootView().dispatchTouchEvent(keyDown);
        mFloatLayout.getRootView().dispatchTouchEvent(keyUp);
        Log.i(TAG, "autoClick");
	}

    private void autoClick2(float x, float y) {
        StopWatch sw = new StopWatch();
        sw.start();
        double startTime = sw.getTime();
        try {
			Process process = Runtime.getRuntime().exec("su", null, null);
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			String cmd = "/system/bin/input tap " + x + " " + y + "\n";
			os.writeBytes(cmd);
			os.writeBytes("exit\n");
			os.flush();
			os.close();
			process.waitFor();
		} catch(Exception e) {
			Log.i(TAG, e.toString());

		}
        double endTime = sw.getTime();
        double runTime = endTime - startTime;
        Log.i(TAG, Double.toString(runTime));
    }

    private void screenCap() {
        StopWatch sw = new StopWatch();
        sw.start();
        double startTime = sw.getTime();
        try {
            Process sh = Runtime.getRuntime().exec("su", null, null);

            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + "/sdcard/autoClicker/img.png").getBytes("ASCII"));
            os.flush();

            os.close();
            sh.waitFor();
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        double endTime = sw.getTime();
        double runTime = endTime - startTime;
        Log.i(TAG, Double.toString(runTime));
    }
}
