package com.jacklin.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class MyCanvas extends View 
{
	private final float TOUCH_TOLERANCE = 4;
	
	private Paint mPaint;
	private Path mPath;
	private Bitmap mBitmap;
	private Paint mBitmapPaint; 
	private Canvas mCanvas;
	private float mPosX;
	private float mPosY;
	
	private DrawPath mDrawPath;
	private List<DrawPath> mSavePath;
	private int mOldSize = 0;
	private ArrayList<Integer> mOldSizes;
	private String mImagePath;
	
	
	private ArrayList<Integer> mOperationType = new ArrayList<Integer>();
	
	public MyCanvas(Context context, AttributeSet attr, int defStyle)
	{
		super(context, attr, defStyle);
		init();
	}
	public MyCanvas(Context context, AttributeSet attr)
	{
		super(context,attr);
		init();
	}
	
	public MyCanvas(Context context)
	{
		super(context);
		init();
	}
	
	private void init()
	{ 
	    mBitmapPaint = new Paint(Paint.DITHER_FLAG);
	    mOldSizes = new ArrayList<Integer>();
	    mSavePath = new ArrayList<DrawPath>();
	    mImagePath = initPath();
	}
	
	private String initPath()
	{
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		if(path == null)
		{
			return null;
		}
		path += "/jacklin/game";
		File imageFile = new File(path);
		if(!imageFile.exists() )
		{
			imageFile.mkdir();
		}
		return path;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
	}
	
	@Override
	public void onDraw(Canvas canvas) 
	{
		canvas.drawColor(0xFFFFFFFF); //���ƻ�������ɫ
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		if(mPath != null)
		{
			canvas.drawPath(mPath, mPaint);	
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				mPath = new Path();
				mDrawPath = new DrawPath();
				mPath.moveTo(x, y);
				mDrawPath.paint = new Paint(mPaint);
				mDrawPath.path	= mPath;
				mPosX = x;
				mPosY = y;
				postInvalidate();
				
				break;
			case MotionEvent.ACTION_MOVE:
				float dx = Math.abs(x - mPosX);
				float dy = Math.abs(y - mPosY);
				if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
				{
					mPath.lineTo(x, y);
					mPosX = x;
					mPosY = y;
				}
				postInvalidate();
				
				break;
			case MotionEvent.ACTION_UP:
				mPath.lineTo(mPosX, mPosY);
				mCanvas.drawPath(mPath, mPaint); 
				mSavePath.add(mDrawPath);
				mPath = null;
				int type = OperationType.ADD;
				mOperationType.add(type);
				postInvalidate();
				
				break;
		}
		return true;
	}
	
	public void setPaint(Paint paint)
	{
		mPaint = paint;
	}
	
	public void saveImage()
	{
		String imageName = UUID.randomUUID().toString();
		imageName += ".png";
		imageName = mImagePath + "/" + imageName;
		File file = new File(imageName);
		FileOutputStream out = null;
		try 
		{
			file.createNewFile();
			out = new FileOutputStream(file);
			mBitmap.compress(CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			Toast.makeText(getContext(), "����ɹ�", Toast.LENGTH_SHORT).show();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void cancel() //����
	{
		int count = mOperationType.size();
		if (count == 0)
		{
			return;
		}
		int type = mOperationType.get(count-1);
		mOperationType.remove(count - 1);
		
		int size = 0;
		if (type == OperationType.ADD)
		{
			size = mSavePath.size();
			mSavePath.remove(size -1);
			int width = mCanvas.getWidth();
			int height = mCanvas.getHeight();
			mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mBitmap);
		}
		else if (type == OperationType.CLEAR)
		{
			size = mOldSizes.size();
			if (size - 2 < 0)
			{
				mOldSize = 0;
				mOldSizes.remove(size-1);
			}
			else
			{
				mOldSize = mOldSizes.get(size-2);
				mOldSizes.remove(size-1);
			}
		}
		
		size = mSavePath.size();
		DrawPath temp;
		for (int i = mOldSize; i < size; i++)
		{
			temp = mSavePath.get(i);
			mCanvas.drawPath(temp.path, temp.paint);
		}
		postInvalidate();
	}
	
	public void clearImage()
	{	
		if (mOldSize == mSavePath.size())
		{
			return;
		}
		int width = mCanvas.getWidth();
		int height = mCanvas.getHeight();
		mOldSize = mSavePath.size(); 
		mOldSizes.add(mOldSize);
		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);	
		int type = OperationType.CLEAR;
		mOperationType.add(type);
		postInvalidate();
	}
	
	public int getTypeCount()
	{
		return mOperationType.size();
	}
	
	private class DrawPath
	{
		public Path path;
		public Paint paint;
	}
	
	private class OperationType
	{
		public static final int ADD = 1;
		public static final int CLEAR = 2;
	}
	
	@SuppressWarnings("deprecation")
	public Drawable getDraw(){
		Drawable drawable=new BitmapDrawable(mBitmap);
		return drawable;
	}
}
