package com.jacklin.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
/**
 * 图片资源与字符串相互转换的工具类
 * @author john
 *
 */
public class PhotoUtils {
	public static String DrawableToString(Drawable drawable){
		/*if(drawable==null){
			return "";
		}
		String drawableToString=null;
		//图片资源转为字符串
		
		Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity()!=PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
		int size=bitmap.getWidth()*bitmap.getHeight()*4;
		ByteArrayOutputStream baos=new ByteArrayOutputStream(size);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] imagedata=baos.toByteArray();
		drawableToString=Base64.encodeToString(imagedata, Base64.DEFAULT);
		
		return drawableToString;*/
		if(drawable == null)
		{
			return "";
		}
		BitmapDrawable bd = (BitmapDrawable)drawable;
		Bitmap bmp = bd.getBitmap();
		if(bmp == null)
			return "";
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		//压缩图片
		bmp.compress(Bitmap.CompressFormat.PNG, 60, stream);
		byte[] b = stream.toByteArray();
		// 将图片流以字符串形式存储下来
		return Base64Coder.encodeLines(b);
	}
	
	@SuppressWarnings("deprecation")
	public static Drawable StringToDrawable(String encodeStr){
		/*if(string.equals("")||string==null){
			return null;
		}
		Drawable stringToDrawable=null;
		//字符串转为图片资源
		byte[] img=Base64.decode(string.getBytes(), Base64.DEFAULT);
		Bitmap bitmap =BitmapFactory.decodeByteArray(img, 0, img.length);
		stringToDrawable=new BitmapDrawable(bitmap);
		
		return stringToDrawable;*/
		if(encodeStr==null || encodeStr.equals(""))
		{
			return null;
		}
		//Base64Coder解码
		ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decodeLines(encodeStr));
		Bitmap dBitmap = BitmapFactory.decodeStream(in);
		Drawable drawable = new BitmapDrawable(dBitmap);
		return drawable;
	}
}
