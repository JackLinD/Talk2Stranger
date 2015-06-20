package com.jacklin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	public MyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Path clipPath = new Path(); 
		int w = this.getWidth(); 
		int h = this.getHeight(); 
		clipPath.addRoundRect(new RectF(0, 0, w, h), 30.0f, 30.0f, Path.Direction.CW); 
		canvas.clipPath(clipPath); 
		/*
		Rect rec=canvas.getClipBounds();
		rec.bottom--;
		rec.right--;
		Paint paint=new Paint();
		paint.setColor(Color.GRAY);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		canvas.drawRect(rec, paint);*/
		/*Drawable drawable=getDrawable();
		if(null!=drawable){
			Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
			Bitmap b=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Config.ARGB_8888);
			Canvas canvas1=new Canvas(b);
			int color= 0xff424242;
			Rect rect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
			Paint paint=new Paint();
			paint.setAntiAlias(true);
			canvas1.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			int x=bitmap.getWidth();
			canvas1.drawCircle(x/2, x/2, x/2, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas1.drawBitmap(bitmap, rect, rect,paint);
					
			Rect rectSrc=new Rect(0,0,b.getWidth(),b.getHeight());
			Rect rectDest=new Rect(0,0,getWidth(),getHeight());
			paint.reset();
			canvas.drawBitmap(b, rectSrc, rectDest,paint);
		}else{*/
			super.onDraw(canvas);
//		}
	}

	
}
