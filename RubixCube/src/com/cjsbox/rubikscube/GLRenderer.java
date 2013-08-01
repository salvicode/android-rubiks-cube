package com.cjsbox.rubikscube;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

import com.cjsbox.rubikscube.matrix.MatrixGrabber;

public class GLRenderer implements GLSurfaceView.Renderer
{
	private static final String LOG_TAG = GLRenderer.class.getSimpleName();
	public MCube cube;
	public static GLRenderer instance;
	private float cubeX = 0.0f;
	private float cubeY = 0.0f;
	private float cubeZ = 0.0f;
	public float tx, ty; // Touch coords
	private float sHeight;

	//-------------------ARCBALL--------------------------
	private float [] mModelview = new float[16];
	private float [] mProjection = new float[16];
    private Matrix4f LastRot = new Matrix4f();
    private Matrix4f ThisRot = new Matrix4f();
    private final Object matrixLock = new Object();
    private float[] matrix = new float[16];
    private CArcBall arcBall = new CArcBall(640.0f, 480.0f);  // NEW: ArcBall Instance
    private int [] mViewport = new int[4];
    private GLTextures textures;
    private Context context;
	//-----------------------------------------------------

	// constructor
	public GLRenderer(float width, float height, Context context)
	{
		super();
        // Start Of User Initialization
        LastRot.setIdentity();                                // Reset Rotation
        ThisRot.setIdentity();                                // Reset Rotation
        ThisRot.get(matrix);
        arcBall.setBounds(width, height);
        this.context = context;
        instance = this;
	}
    void reset() {
        synchronized(matrixLock) {
            LastRot.setIdentity();   // Reset Rotation
            ThisRot.setIdentity();   // Reset Rotation
        }
    }

    void startDrag( Point2f MousePt ) {
        synchronized(matrixLock) {
            LastRot.set( ThisRot );  // Set Last Static Rotation To Last Dynamic One
        }
        arcBall.click( MousePt );    // Update Start Vector And Prepare For Dragging
    }
    void drag( Point2f MousePt )       // Perform Motion Updates Here
    {
        Quat4f ThisQuat = new Quat4f();

        // Update End Vector And Get Rotation As Quaternion
        arcBall.drag( MousePt, ThisQuat);
        synchronized(matrixLock) {
            ThisRot.setRotation(ThisQuat);  // Convert Quaternion Into Matrix3fT
            ThisRot.mul( ThisRot, LastRot); // Accumulate Last Rotation Into This One
        }
    }

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		float lightAmbient[] = {1.0f, 1.0f, 1.0f, 1.0f};
		float lightDiffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
		float matSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
		float matShines[] = {5.0f};
		float matAmbient[] = {1.0f,1.0f, 1.0f, 1.0f};
		float matDiffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
		float lightPost[] = {0.0f, 5.0f, 10.0f, 1.0f};
	      gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);  // Set color's clear-value to black
	      gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
	      gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
	      gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
	      gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
	      gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
			// enable smooth shading

	    gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, matSpecular, 0);
	    gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, matShines, 0);
	    gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, matAmbient, 0);
	 //   gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, matDiffuse, 0);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPost, 0);
	    gl.glEnable(GL10.GL_LIGHTING);
	    gl.glEnable(GL10.GL_LIGHT0);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
	  //  gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
	    textures = new GLTextures(gl, context);
	    textures.add(R.drawable.whiteside);
	    textures.add(R.drawable.yellowside);
	    textures.add(R.drawable.blueside);
	    textures.add(R.drawable.greenside);
	    textures.add(R.drawable.redside);
	    textures.add(R.drawable.orangeside);
	    textures.loadTextures();
	      createCube(3);
	      cube.startRandomRotating(30);
	}

	 @Override
	 public void onSurfaceChanged(GL10 gl, int width, int height)
	 {
	      if (height == 0) height = 1;   // To prevent divide by zero

	      aspect = (float)width / height;
	      nScreenWidth = width;
	      nScreenHeight = height;
	      // Set the viewport (display area) to cover the entire window
	      gl.glViewport(0, 0, width, height);
			if (gl instanceof GL11){
				gl.glGetIntegerv(GL11.GL_VIEWPORT, mViewport, 0);
			}

	      arcBall.setBounds((float) width, (float) height);
	        gl.glMatrixMode(GL10.GL_PROJECTION);
	        gl.glLoadIdentity();
	        GLU.gluPerspective(gl, fFOV, aspect, fNear, fFar);
	        gl.glMatrixMode(GL10.GL_MODELVIEW);
	        gl.glLoadIdentity();
	        GLU.gluLookAt(gl, posCamera[0], posCamera[1], posCamera[2], posLook[0], posLook[1], posLook[2], 0, 0, 1);
	}

	 public void createCube(int size)
	 {
		 cube = new MCube(size, textures);
	 }

	public void mix() {
		// glrenderer.mix();
		cube.startRandomRotating(10 * cube.getCubeSize());
	}

	public void cancelMove() {
		// glrenderer.cancelMove();
		cube.cancelMoves(1);
	}

	public void setCubeDim(int dim) {
		createCube(dim);
	}

	float[] cl = new float[4];
	 public int nScreenWidth, nScreenHeight;
	 public float fFOV = 45f;
	 public float aspect;
	 public float fNear = 0.1f;
	 public float fFar = 100.0f;
	 public float [] posCamera = new float[4];
	 public float [] posLook = new float[4];
	 float [] xxx;
	private void drawScene(GL10 gl, int mode)
	{
        synchronized(matrixLock) {
            ThisRot.get(matrix);
        }
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

	    gl.glLoadIdentity();                  // Reset The Current Modelview Matrix
		mg.getCurrentState(gl);
	    gl.glTranslatef(0.0f, 0.0f, -2.8f * cube.getCubeSize());

		gl.glMultMatrixf(matrix, 0);        // NEW: Apply Dynamic Transform
		gl.glTranslatef(cubeX, cubeY, cubeZ);

		gl.glRotatef(30.0f, 1.0f, 1.0f, 0.0f);
		cube.drawCube(gl, 9999);
	}

	private boolean isDragged = false;

	public void handleTouch(MotionEvent event)
	{
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			selectParts(event.getX(), event.getY());
			if (selectedParts.size() == 0)
			{
				startDrag( new Point2f( event.getX(), event.getY() ) );
				isDragged = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isDragged)
				selectParts(event.getX(), event.getY());
			else
				drag( new Point2f( event.getX(), event.getY() ) );
			break;
		case MotionEvent.ACTION_UP:
			isDragged = false;
			deselectParts();
			break;
		}
	}

	public void deselectParts()
	{
		for (Part p : cube.getParts())
		{
			p.setSelected(false);
		}
		selectedParts.clear();
	}

	Vector<Integer> selectedParts = new Vector<Integer>();
	public boolean selectParts(float x, float y)
	{
		float [] ray = getViewRay(x, y);
		if (cube.getStatus() != 'N') return false;
		Vector<Part> parts = cube.getParts();
		Part selectedPart = null;
		int selId = -1;
		for (int i = 0; i < parts.size(); i++)
		{
			Part p = parts.get(i);
				if (collision(p, ray))
				{
					//pps.add(p);
					if (selectedPart == null)
					{
						selectedPart = p;
						selId = i;
						break;
					} else
					{
						if (Math.round(p.z * 100000) > Math.round(selectedPart.z * 100000))
						{
							selectedPart = p;
							selId = i;
						}
					}
				}
		}
		if (selectedPart!=null)
		{
			if (!selectedPart.isSelected() && !selectedParts.contains(selId))
			{
		//		Log.i("selected part is: ", String.valueOf(selId));
				selectedParts.add(selId);
				if (selectedParts.size() >= 2)
				{
					if (cube.checkSidesForRotating(selectedParts))
					{
						deselectParts();
					}
				}
			}
		}

		return selectedPart != null;
	}
	float[] collisionPoint = { 0f, 0f, 0f };
	float[] collisionPointS = { 0f, 0f, 0f };
	float[] collisionPointE = {0.0f, 0.0f, 0.0f};
	private static int RAY_ITERATIONS = 1000;
	public boolean collision(Part p, float [] rayVector) {
		float length = rayVector[3];

		collisionPointS[0] = rayVector[0] * length / RAY_ITERATIONS * 0;
		collisionPointS[1] = rayVector[1] * length / RAY_ITERATIONS * 0;
		collisionPointS[2] = rayVector[2] * length / RAY_ITERATIONS * 0;
//
		collisionPointE[0] = rayVector[0] * length / RAY_ITERATIONS * RAY_ITERATIONS;
		collisionPointE[1] = rayVector[1] * length / RAY_ITERATIONS * RAY_ITERATIONS;
		collisionPointE[2] = rayVector[2] * length / RAY_ITERATIONS * RAY_ITERATIONS;

		return Helper.hitWithTriangle(p.polygon[0], p.polygon[1], p.polygon[2], collisionPointS, collisionPointE)
				|| Helper.hitWithTriangle(p.polygon[1], p.polygon[2], p.polygon[3], collisionPointS, collisionPointE)
				|| Helper.hitWithTriangle(p.polygon[2], p.polygon[3], p.polygon[0], collisionPointS, collisionPointE)
				|| Helper.hitWithTriangle(p.polygon[3], p.polygon[0], p.polygon[1], collisionPointS, collisionPointE);
	}

	public float distance(float[] a, float[] b)
	{
		float abx = b[0] - a[0];
		float aby = b[1] - a[1];
		float abz = b[2] - a[2];
		float distance = (float) Math.sqrt(abx*abx+aby*aby+abz*abz);

		return distance;
	}
	MatrixGrabber mg = new MatrixGrabber();

	public float[] getViewRay(float x, float y)
	{
	    // view port
	    int[] viewport = { 0, 0, GLRenderer.instance.nScreenWidth, GLRenderer.instance.nScreenHeight };

	    // far eye point
		float [] nearPoint = {0f, 0f, 0f, 0f};
		float [] farPoint = {0f, 0f, 0f, 0f};
		float [] rayVector = {0f, 0f, 0f, 0f};
		y = GLRenderer.instance.nScreenHeight - y;

		//Retreiving position projected on near plane
		GLU.gluUnProject(x, y, -1f, mg.mModelView, 0, mg.mProjection, 0, viewport, 0, nearPoint, 0);

		//Retreiving position projected on far plane
		GLU.gluUnProject(x, y, 1f, mg.mModelView, 0, mg.mProjection, 0, viewport, 0, farPoint, 0);

		// extract 3d Coordinates put of 4d Coordinates
		nearPoint = Helper.fixW(nearPoint);
		farPoint = Helper.fixW(farPoint);
		//Processing ray vector
		rayVector[0] = farPoint[0] - nearPoint[0];
		rayVector[1] = farPoint[1] - nearPoint[1];
		rayVector[2] = farPoint[2] - nearPoint[2];

		// calculate ray vector length
		float rayLength = (float) Math.sqrt((rayVector[0] * rayVector[0]) + (rayVector[1] * rayVector[1]) + (rayVector[2] * rayVector[2]));

		//normalizing ray vector
		rayVector[0] /= rayLength;
		rayVector[1] /= rayLength;
		rayVector[2] /= rayLength;
		rayVector[3] = rayLength;

	    return rayVector;
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		drawScene(gl, GL10.GL_MODELVIEW);
	}
}
