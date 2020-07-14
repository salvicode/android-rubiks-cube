package com.salvicode.rubixcube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Line {
	private FloatBuffer vertexBuffer;  // Buffer for vertex-array
	private float[] color;
	private int[] colorInt;
	public Line(float[] vertices, float[] color)
	{
	      ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
	      vbb.order(ByteOrder.nativeOrder()); // Use native byte order
	      vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
	      vertexBuffer.put(vertices);         // Copy data into buffer
	      vertexBuffer.position(0);           // Rewind
	      setColor(color);
	}
	public Line(float[] vertices, int[] color)
	{
	      ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
	      vbb.order(ByteOrder.nativeOrder()); // Use native byte order
	      vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
	      vertexBuffer.put(vertices);         // Copy data into buffer
	      vertexBuffer.position(0);           // Rewind
	      setColor(color);
	}
	public void setColor(int[] color)
	{
		this.colorInt = color;
	}
	public void setColor(float[] color)
	{
		this.color = color;
	}
	public void draw(GL10 gl)
	{
	      gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
	      gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
	      gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)
	      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	      // Set the color for each of the faces
	      gl.glColor4f(color[0], color[1], color[2], color[3]);
	      // Draw the primitive from the vertex-array directly
	      gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);
	      gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	      gl.glDisable(GL10.GL_CULL_FACE);
	}
}
