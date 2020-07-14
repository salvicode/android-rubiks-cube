package com.salvicode.rubixcube;

import javax.microedition.khronos.opengles.GL;

import com.salvicode.rubixcube.matrix.MatrixGrabber;
import com.salvicode.rubixcube.matrix.MatrixTrackingGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GLView extends GLSurfaceView {
	private static final String LOG_TAG = GLView.class.getSimpleName();
	private GLRenderer glrenderer;
	private float x0 = 0.0f;
	private float y0 = 0.0f;
	private float x1 = 0.0f;
	private float y1 = 0.0f;

	public GLView(Context context, AttributeSet attr)
	{
		super(context, attr);
		if (glrenderer != null) return;
		glrenderer = new GLRenderer(getResources().getDisplayMetrics().widthPixels,
				getResources().getDisplayMetrics().heightPixels, context);
		setRenderer(glrenderer);
		setGLWrapper(new GLSurfaceView.GLWrapper() {

			@Override
			public GL wrap(GL gl) {
				return new MatrixTrackingGL(gl);
			}
		});
	}

	public GLView(Context context)
	{
		super(context);
		if (glrenderer != null) return;
		glrenderer = new GLRenderer(getResources().getDisplayMetrics().widthPixels,
				getResources().getDisplayMetrics().heightPixels, context);
		setRenderer(glrenderer);
		setGLWrapper(new GLSurfaceView.GLWrapper() {

			@Override
			public GL wrap(GL gl) {
				return new MatrixTrackingGL(gl);
			}
		});
	}

	public void mix()
	{
		glrenderer.mix();
	}

	public void cancelMove()
	{
		glrenderer.cancelMove();
	}

	public void setCubeDim(int dim)
	{
		glrenderer.setCubeDim(dim);
	}

	public boolean onTouchEvent(final MotionEvent event)
	{
		glrenderer.handleTouch(event);
		return true;
	}
}
