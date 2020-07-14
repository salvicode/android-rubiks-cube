package com.salvicode.rubixcube;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.salvicode.rubixcube.matrix.MatrixGrabber;


public class Part implements Comparable<Part> {
	private Rectangle rect;
	private boolean selected = false;
	public float [] matrix;
	public float x;
	public float y;
	public float z;
	private int id;
	private MatrixGrabber mg = new MatrixGrabber();
	public float [][] polygon = new float[4][3];
    private FloatBuffer   mCoordsBuffer;
	public boolean isSelected()
	{
		return selected;
	}
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	public void setRectangle(Rectangle rect)
	{
		this.rect = rect;
	}
	public Part(float[] _color, int id, Rectangle rect)
	{
		color = _color;
		matrix = new float[16];
		this.rect = rect;
		//rect = new Rectangle(quad, color);
		cl[0] = 0.5f;
		cl[1] = 0.5f;
		cl[2] = 0.5f;
		cl[3] = 0.5f;

		 float textureCoords[] = {
					0.0f, 1.0f,
					1.0f, 1.0f,
					1.0f, 0.0f,
					0.0f, 0.0f,
		 };
		 mCoordsBuffer = Helper.createFloatBuffer(textureCoords);
	}
	float[] cl = new float[4];

	public void setColor(float[] color)
	{
		this.color = color;
	}
	private float[] color;
//	public Vector sides; // contain sides-owners
	private float[] quad =
	{
				0.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f
	};

	public void draw(GL10 gl)
	{
		draw(gl, 0);
	}

	private void processCoords(GL10 gl)
	{

		mg.getCurrentState(gl);
		matrix = mg.mModelView;

		x = 0.5f*matrix[0]+0.5f*matrix[4]+0.0f*matrix[8]+matrix[12];
		y = 0.5f*matrix[1]+0.5f*matrix[5]+0.0f*matrix[9]+matrix[13];
		z = 0.5f*matrix[2]+0.5f*matrix[6]+0.0f*matrix[10]+matrix[14];

		polygon[0][0] = 0.0f*matrix[0]+0.0f*matrix[4]+0.0f*matrix[8]+matrix[12];
		polygon[0][1] = 0.0f*matrix[1]+0.0f*matrix[5]+0.0f*matrix[9]+matrix[13];
		polygon[0][2] = 0.0f*matrix[2]+0.0f*matrix[6]+0.0f*matrix[10]+matrix[14];

		polygon[1][0] = 1.0f*matrix[0]+0.0f*matrix[4]+0.0f*matrix[8]+matrix[12];
		polygon[1][1] = 1.0f*matrix[1]+0.0f*matrix[5]+0.0f*matrix[9]+matrix[13];
		polygon[1][2] = 1.0f*matrix[2]+0.0f*matrix[6]+0.0f*matrix[10]+matrix[14];

		polygon[2][0] = 1.0f*matrix[0]+1.0f*matrix[4]+0.0f*matrix[8]+matrix[12];
		polygon[2][1] = 1.0f*matrix[1]+1.0f*matrix[5]+0.0f*matrix[9]+matrix[13];
		polygon[2][2] = 1.0f*matrix[2]+1.0f*matrix[6]+0.0f*matrix[10]+matrix[14];

		polygon[3][0] = 0.0f*matrix[0]+1.0f*matrix[4]+0.0f*matrix[8]+matrix[12];
		polygon[3][1] = 0.0f*matrix[1]+1.0f*matrix[5]+0.0f*matrix[9]+matrix[13];
		polygon[3][2] = 0.0f*matrix[2]+1.0f*matrix[6]+0.0f*matrix[10]+matrix[14];
	}

	public void draw(GL10 gl, int mode)
	{
		gl.glPushMatrix();
		processCoords(gl);
		//gl.glTranslatef(0.1f, 0.1f, 0.05f);
		//gl.glScalef(0.8f, 0.8f, 1.0f);
	//	gl.glColor4f(color[0], color[1], color[2], color[3]);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		if (!selected)
			rect.draw(gl);
		gl.glPopMatrix();
	}
	@Override
	public int compareTo(Part arg0) {
		return arg0.z < z ? -1 : 1;
	}
}
