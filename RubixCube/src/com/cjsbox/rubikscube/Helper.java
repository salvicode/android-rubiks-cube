package com.cjsbox.rubikscube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Helper {

	/**
	 * convert degrees to radians based on ios code from: http://blog.nova-box.com/2010/05/iphone-ray-picking-glunproject-sample.html
	 * @param deg
	 * @return
	 */
	public static double degToRadians(float deg){
		return deg/ 180.0 * Math.PI;
	}

	/**
	 * create a Floatbuffer for a given Array
	 * @param array
	 * @return
	 */
	public static FloatBuffer createFloatBuffer(float[] array)
	{
		final int floatSize = Float.SIZE / 8;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * floatSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);
		return floatBuffer;
	}

	/**
	 * Convert the 4D input into 3D space (or something like that, otherwise the gluUnproject values are incorrect)
	 * @param v 4D input
	 * @return 3D output
	 * @author http://stackoverflow.com/users/1029225/mh
	 */
	public static float[] fixW(float[] v) {
	    float w = v[3];
	    for(int i = 0; i < 4; i++)
	        v[i] = v[i] / w;
	    return v;
	}

	public static boolean hitWithTriangle(float a[], float b[], float c[], float start[], float end[])
	{
		float _ray[] = new float[6];
		_ray[0] = end[0] - start[0];
		_ray[1] = end[1] - start[1];
		_ray[2] = end[2] - start[2];
		_ray[3] = start[1]*end[2] - end[1]*start[2];
		_ray[4] = start[2]*end[0] - end[2]*start[0];
		_ray[5] = start[0]*end[1] - end[0]*start[1];

		float _e01[] = new float[6];
		float _e12[] = new float[6];
		float _e20[] = new float[6];
		_e01[0] = b[0] - a[0];
		_e01[1] = b[1] - a[1];
		_e01[2] = b[2] - a[2];
		_e01[3] = a[1]*b[2] - b[1]*a[2];
		_e01[4] = a[2]*b[0] - b[2]*a[0];
		_e01[5] = a[0]*b[1] - b[0]*a[1];
		float pd0 = _e01[0]*_ray[3] + _e01[1]*_ray[4] + _e01[2]*_ray[5] + _e01[3]*_ray[0] + _e01[4]*_ray[1] + _e01[5]*_ray[2];
		_e12[0] = c[0] - b[0];
		_e12[1] = c[1] - b[1];
		_e12[2] = c[2] - b[2];
		_e12[3] = b[1]*c[2] - c[1]*b[2];
		_e12[4] = b[2]*c[0] - c[2]*b[0];
		_e12[5] = b[0]*c[1] - c[0]*b[1];
		float pd1 = _e12[0]*_ray[3] + _e12[1]*_ray[4] + _e12[2]*_ray[5] + _e12[3]*_ray[0] + _e12[4]*_ray[1] + _e12[5]*_ray[2];
		_e20[0] = a[0] - c[0];
		_e20[1] = a[1] - c[1];
		_e20[2] = a[2] - c[2];
		_e20[3] = c[1]*a[2] - a[1]*c[2];
		_e20[4] = c[2]*a[0] - a[2]*c[0];
		_e20[5] = c[0]*a[1] - a[0]*c[1];
		float pd2 = _e20[0]*_ray[3] + _e20[1]*_ray[4] + _e20[2]*_ray[5] + _e20[3]*_ray[0] + _e20[4]*_ray[1] + _e20[5]*_ray[2];
		return pd0 < 0 && pd1 < 0 && pd2 < 0;
	}
	/**
	 * check if a given point in space collides with a given object center  with a given radius
	 * based on ios code from: http://blog.nova-box.com/2010/05/iphone-ray-picking-glunproject-sample.html
	 * @param point point to check for collision with object x,y,z
	 * @param center center of the object x,y,z
	 * @param radius
	 * @return true on collision, false on no collision
	 */
	public static Boolean poinSphereCollision(float [] point, float[] center, float radius){

		return ( (point[0] - center[0]) * (point[0] - center[0]) +
				(point[1] - center[1]) * (point[1] - center[1]) +
				(point[2] - center[2]) * (point[2] - center[2]) < (radius * radius));
			}

}
