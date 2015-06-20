package com.jacklin.register;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jacklin.jacklinclient.R;
@SuppressWarnings("deprecation")
public class PhotoActivity extends Activity implements SurfaceHolder.Callback{
	
	private Camera camera=null;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	@SuppressWarnings("unused")
	private static Context context=null;
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		System.out.println("surfaceCreated");
		camera=Camera.open();
		try {
			camera.setPreviewDisplay(holder);
			Camera.Parameters parameters=camera.getParameters();
			if(this.getResources().getConfiguration().orientation!=Configuration.ORIENTATION_LANDSCAPE){
				parameters.set("orientation", "portrait");
				camera.setDisplayOrientation(90);
				parameters.setRotation(90);
			}else{
				parameters.set("orientation", "landscape");
				camera.setDisplayOrientation(0);
				parameters.setRotation(0);
			}
			camera.setParameters(parameters);
			camera.startPreview();
			System.out.println("camera.startpreview");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			camera.release();
			System.out.println("camera.release");
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		System.out.println("surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		System.out.println("surfaceDestroyed");
		if(camera!=null){
			camera.stopPreview();
			camera.release();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_photo);
		context=null;
		surfaceView=(SurfaceView) findViewById(R.id.surfaceview);
		surfaceHolder=surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(PhotoActivity.this);
	}

}
