package com.cjsbox.rubikscube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Rectangle {
	private FloatBuffer vertexBuffer;  // Buffer for vertex-array
	private float[] color;
	private int textureId;
	private FloatBuffer   mCoordsBuffer;
	public Rectangle(float[] vertices, float[] color)
	{
	      ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
	      vbb.order(ByteOrder.nativeOrder()); // Use native byte order
	      vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
	      vertexBuffer.put(vertices);         // Copy data into buffer
	      vertexBuffer.position(0);           // Rewind
	      setColor(color);
	}
	public Rectangle(float[] vertices, int textureId)
	{
		this(vertices);

		this.textureId = textureId;
        float textureCoords[] = {
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f,
		};
		mCoordsBuffer = Helper.createFloatBuffer(textureCoords);
	}
	public Rectangle(float[] vertices)
	{
	      ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
	      vbb.order(ByteOrder.nativeOrder()); // Use native byte order
	      vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
	      vertexBuffer.put(vertices);         // Copy data into buffer
	      vertexBuffer.position(0);           // Rewind
	      setColor(color);
	}
	public void setColor(float[] color)
	{
		this.color = color;
	}
	public void draw(GL10 gl)
	{
//	      gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
	//	gl.glClearColor(1.0f,1.0f, 1.0f, 1.0f);
	   //   gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
	   //   gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)
	  //  	gl.glEnable(GL10.GL_BLEND);
	    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	      gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCoordsBuffer);
	      // Set the color for each of the faces

	      // Draw the primitive from the vertex-array directly
	      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	      gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
	    	gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    	gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    //	gl.glDisable(GL10.GL_BLEND);
	   //   gl.glDisable(GL10.GL_CULL_FACE);
	}
}
