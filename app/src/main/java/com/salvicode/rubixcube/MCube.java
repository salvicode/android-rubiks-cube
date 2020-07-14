package com.salvicode.rubixcube;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class MCube {

	private int mode; // GL_SELECT - selection mode
	private float speed; // speed of rotating
	private float rangle; // current angle of rotation
	private float sizeOfQuad;
	private float sizeGl; // the size of +side projection on any axis
	private char status; //  N - normal status, R - rotating, A - autorotating,  C - canceling moves
	private boolean isClockWise;
	private int rotSide;
	private int rotatedSideOwner; // side owner
	private int rotaredSideFace; // second is the face
	private int sizeOfCube;
	private Vector<Integer> sides;
	private Vector<Part> parts;
	private Vector< Vector<Float> > dxyz; // translating of parts. has size (HZ,3)
	private Vector< Vector<Integer> > faceFirst; // second - number of parts which make the whole cube
	private Vector< Vector<Integer> > faceSecond; // ---- //
	private Vector< Vector<Integer> > faceThird;  // ---- //
	private Vector< Vector<Pair<Integer, Integer>> > swaps; // swaping parts during i rotating
	private Vector< Pair<Integer, Integer> > oSides; // rotating sides that contain i-part
	private Vector< Pair<Integer, Boolean> > movingSides;
	private Vector< Pair<Integer, Boolean> > madeMoves;
	private Rectangle rect;
    private GLTextures textures;
	public Vector<Part> getParts()
	{
		return parts;
	}
	private void fillOSides(int size)
	{
		oSides = new Vector<Pair<Integer, Integer>>();
		//----------------White [0] - side--------------//
		for (int i = 0; i < size; i ++)
			for (int j = 0; j < size; j++)
			{
				oSides.add(new Pair(j, size + i));
			}
		//----------------Red [1] - side--------------//
		for (int i = 0; i < size; i ++)
			for (int j = 0; j < size; j++)
			{
				oSides.add(new Pair(size + i, size * 2 + size - j - 1));
			}
		//--------------Yellow [2] - side-------------//
		for (int i = 0; i < size; i ++)
			for (int j = 0; j < size; j++)
			{
				oSides.add(new Pair(size - j - 1, size + i));
			}
		//--------------Orange [3] - side-------------//
		for (int i = 0; i < size; i ++)
			for (int j = 0; j < size; j++)
			{
				oSides.add(new Pair(size + i, size * 2 + j));
			}
		//--------------Green [4] - side-------------//
		for (int i = 0; i < size; i ++)
			for (int j = 0; j < size; j++)
			{
				oSides.add(new Pair(j, size * 2 + size - i - 1));
			}
		//--------------Blue [5] - side-------------//
		for (int i = 0; i < size; i ++)
			for (int j = 0; j < size; j++)
			{
				oSides.add(new Pair(j, size * 2 + i));
			}
	}
	private void fillDXYZ(int size)
	{
		sizeOfQuad = 1.0f;
		sizeGl = (sizeOfQuad * size) / 2.0f;
		// Fill White side[0]
		float dx = 0.0f, dy = 0.0f, dz = sizeGl;
		Vector<Float> txyz = new Vector<Float>(3); // temporary
		dxyz = new Vector<Vector<Float>>();
		// now we fill the distance of translation for every quad;
		// White Side
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dx = -sizeGl; dx <= sizeGl - sizeOfQuad; dx += sizeOfQuad)
			{
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Red Side
		dx = -sizeGl;
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dz = -sizeGl; dz <= sizeGl - sizeOfQuad; dz += sizeOfQuad)
			{
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Yellow Side
		dz = -sizeGl;
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dx = sizeGl; dx >= -sizeGl + sizeOfQuad; dx -= sizeOfQuad)
			{
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Orange Side
		dx = sizeGl;
		for (dy = sizeGl - sizeOfQuad; dy >= -sizeGl; dy -= sizeOfQuad)
			for (dz = sizeGl; dz >= -sizeGl + sizeOfQuad; dz -= sizeOfQuad)
			{
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Green Side
		dy = sizeGl;
		for (dz = -sizeGl + sizeOfQuad; dz <= sizeGl; dz += sizeOfQuad)
			for (dx = -sizeGl; dx <= sizeGl - sizeOfQuad; dx += sizeOfQuad)
			{
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
		// Blue side
		dy = -sizeGl;
		for (dz = sizeGl - sizeOfQuad; dz >= -sizeGl; dz -= sizeOfQuad)
			for (dx = -sizeGl; dx <= sizeGl - sizeOfQuad; dx += sizeOfQuad)
			{
				txyz.add(0, dx);
				txyz.add(1, dy);
				txyz.add(2, dz);
				dxyz.add(new Vector<Float>(txyz));
				txyz.clear();
			}
	}

	private void fillParts(int size)
	{
		parts = new Vector<Part>();
		Rectangle rectWhite =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.WHITE));
		int id = 1;
		for (int i = 0; i < size * size; i ++)
			parts.add(new Part(MyOpenGL.white, id++, rectWhite));
		Rectangle rectBlue =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.BLUE));
		for (int i = 0; i < size * size; i ++)
			parts.add(new Part(MyOpenGL.red, id++, rectBlue));
		Rectangle rectYellow =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.YELLOW));
		for (int i = 0; i < size * size; i ++)
			parts.add(new Part(MyOpenGL.yellow, id++, rectYellow));
		Rectangle rectGreen =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.GREEN));
		for (int i = 0; i < size * size; i ++)
			parts.add(new Part(MyOpenGL.orange, id++, rectGreen));
		Rectangle rectRed =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.RED));
		for (int i = 0; i < size * size; i ++)
			parts.add(new Part(MyOpenGL.green, id++, rectRed));
		Rectangle rectOrange =
				new Rectangle(quad, textures.getTextureIdforResource(GLRenderer.ORANGE));
		for (int i = 0; i < size * size; i ++)
			parts.add(new Part(MyOpenGL.blue, id++, rectOrange));
	}
	private void createFaces(int size)
	{
		createFirstFace(size);
		createSecondFace(size);
		createThirdFace(size);
	}
	private void createFirstFace(int size)
	{
		faceFirst = new Vector<Vector<Integer>>();
		Vector<Integer> tmp = new Vector<Integer>();
		for (int i = 0; i < size * size; i ++)
		{
			tmp.add(size * size + i);
		}
	    for (int j = 0; j < size; j ++)
		{
			for (int i = 0; i < size; i ++)
			{
				tmp.add(size * i + j);
				tmp.add(j + 4 * size * size + size * i);
				tmp.add(j + 5 * size * size + size * i);
				tmp.add(2 * size * size + size * i + size - 1 - j);
			}
			if (j != size - 1)
			{
				faceFirst.add(new Vector<Integer>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < size * size; i ++)
		{
			tmp.add(3 * size * size + i);
		}
		faceFirst.add(new Vector<Integer>(tmp));
		tmp.clear();
	}
	private void createSecondFace(int size)
	{
		Vector<Integer> tmp = new Vector<Integer>();
		faceSecond = new Vector< Vector<Integer> >();
		for (int i = 0; i < size * size; i ++)
		{
			tmp.add(4 * size * size + i);
		}
		for (int j = 0; j < size; j ++)
		{
			for (int i = 0; i < size; i ++)
			{
				tmp.add(size * j + i);
			    tmp.add(size * size + size * j + i);
				tmp.add(2 * size * size + size * j + i);
				tmp.add(3 * size * size + size * j + i);
			}
			if (j != size - 1)
			{
				faceSecond.add(new Vector<Integer>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < size * size; i ++)
		{
			tmp.add(5 * size * size + i);
		}
		faceSecond.add(new Vector<Integer>(tmp));
		tmp.clear();
	}
	private void createThirdFace(int size)
	{
		Vector<Integer> tmp = new Vector<Integer>();
		faceThird = new Vector< Vector<Integer> >();
		for (int i = 0; i < size * size; i ++)
		{
			tmp.add(i);
		}
		for (int j = 0; j < size; j ++)
		{
			for (int i = 0; i < size; i ++)
			{
				tmp.add(4 * size * size + size * (size - j - 1) + i);
			    tmp.add(3 * size * size + size * i + j);
				tmp.add(size * size + size - 1 - j + size * i);
				tmp.add(5 * size * size + size * j + i);
			}
			if (j != size - 1)
			{
				faceThird.add(new Vector<Integer>(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < size * size; i ++)
		{
			tmp.add(2 * size * size + i);
		}
		faceThird.add(new Vector<Integer>(tmp));
		tmp.clear();
	}
	private void createFirstTypeSwap(int size)
	{
		Vector< Pair<Integer, Integer> > tmp = new Vector< Pair<Integer, Integer> >();
		//-----------------First type of rotating swaps-----------------//
		//first we should rotate matrix of the main side on 90 degrees
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
			{
				tmp.add(
					new Pair(1 * size * size + size * j + i,
					1 * size * size + size * i + size - j - 1)
					);
			}
		// then swap middle parts
		for (int j = 0; j < size; j++)
		{
			for (int i = 0; i < size; i++)
			{
				tmp.add(
					new Pair(4 * size * size + i * size + j,
					0 * size * size + i * size + j)
					);
				tmp.add(new Pair(0 * size * size + i * size + j,
					5 * size * size + i * size + j)
					);
				tmp.add(new Pair(5 * size * size + i * size + j,
					2 * size * size + size * size - 1 - i * size - j)
					);
				tmp.add(new Pair(2 * size * size + size * size - 1 - i * size - j,
					4 * size * size + i * size + j)
					);

			}
			if (j != size - 1)
			{
				swaps.add(new Vector< Pair<Integer, Integer>>(tmp));
				tmp.clear();
			}
		}
		// then swap last one
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
			{
				tmp.add(
					new Pair(3 * size * size + size * i + size - j - 1,
					3 * size * size + size * j + i)
					);
			}
		swaps.add(new Vector< Pair<Integer, Integer> >(tmp));
		tmp.clear();
	}
	private void createSecondTypeSwap(int size)
	{
		Vector< Pair<Integer, Integer> > tmp = new Vector< Pair<Integer, Integer> >();
		//-----------------Second type of rotating swaps-----------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
			{
				tmp.add(
					new Pair(4 * size * size + size * i + size - j - 1,
					4 * size * size + size * j + i)
					);
			}
		for (int j = 0; j < size; j ++)
		{
			for (int i = 0; i < size; i ++)
			{
				tmp.add(new Pair(0 * size * size + i + j * size,
					3 * size * size + i + j * size)
					);
				tmp.add(new Pair(3 * size * size + i + j * size,
					2 * size * size + i + j * size)
					);
				tmp.add(new Pair(2 * size * size + i + j * size,
					1 * size * size + i + j * size)
					);
				tmp.add(new Pair(1 * size * size + i + j * size,
					0 * size * size + i + j * size)
					);
			}
			if (j != size - 1)
			{
				swaps.add(new Vector< Pair<Integer, Integer> >(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
			{
				tmp.add(
					new Pair(5 * size * size + size * j + i,
					5 * size * size + size * i + size - j - 1)
					);
			}
		swaps.add(new Vector< Pair<Integer, Integer> >(tmp));
		tmp.clear();
	}
	private void createThirdTypeSwap(int size)
	{
		Vector< Pair<Integer, Integer> > tmp = new Vector< Pair<Integer, Integer> >();
		//-----------------Third type of rotating swaps-----------------//
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
			{
				tmp.add(
					new Pair(0 * size * size + size * i + size - j - 1,
					0 * size * size + size * j + i)
					);
			}
		for (int j = 0; j < size; j ++)
		{
			for (int i = 0; i < size; i ++)
			{
				tmp.add(new Pair(3 * size * size + i * size + j,
					4 * size * size + (size - j - 1) * size + i)
					);
				/*
				 I didn`t really understand why i need swap(),
				 but without it it works incorrectly
				*/
				tmp.add(new Pair(5 * size * size + j * size + size - i - 1,
					3 * size * size + i * size + j)
					);
				tmp.add(new Pair(1 * size * size + (size - i - 1) * size + size - j - 1,
					5 * size * size + j * size + size - i - 1)
					);
				tmp.add(new Pair(4 * size * size + (size - j - 1) * size + i,
					1 * size * size + (size - i - 1) * size + size - j - 1)
					);
			}
			if (j != size - 1)
			{
				swaps.add(new Vector< Pair<Integer, Integer> >(tmp));
				tmp.clear();
			}
		}
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
			{
				tmp.add(
					new Pair(2 * size * size + size * j + i,
					2 * size * size + size * i + size - j - 1)
					);
			}
		swaps.add(new Vector< Pair<Integer, Integer> >(tmp));
		tmp.clear();
	}
	private void createSwaps(int size)
	{
		swaps = new Vector< Vector<Pair<Integer, Integer>> >();
		createFirstTypeSwap(size);
		createSecondTypeSwap(size);
		createThirdTypeSwap(size);
	}
	private void drawType_1(GL10 gl)
	{
		int nOfSide = 0;
		int curSide = rotSide % sizeOfCube;
		for (int i = 0; i < faceFirst.size(); i ++)
		{
			if (i == curSide)
			{
				gl.glPushMatrix();
				gl.glRotatef(rangle, 1.0f, 0.0f, 0.0f);
			}
			for (int j = 0; j < faceFirst.get(i).size(); j ++)
			{
				gl.glPushMatrix();
				int current = faceFirst.get(i).get(j);
				gl.glTranslatef( dxyz.get(current).get(0), dxyz.get(current).get(1), dxyz.get(current).get(2) );
				nOfSide = current / sizeOfCube / sizeOfCube;
				gl.glRotatef(calcRX(nOfSide), 1.0f, 0.0f, 0.0f);
				gl.glRotatef(calcRY(nOfSide), 0.0f, 1.0f, 0.0f);
				gl.glRotatef(calcRZ(nOfSide), 0.0f, 0.0f, 1.0f);
				parts.get(current).draw(gl);
				gl.glPopMatrix();
			}
			if (i == curSide)
			{
				if (isClockWise) rangle += speed;else rangle -= speed;
				if (Math.abs(rangle) >= 90)
				{
					endRotate();
					if (status == 'A' || status == 'C')
					{
						if (movingSides.size() != 0)
							beginRotate(movingSides.get(movingSides.size() - 1).getFirst(),
									movingSides.get(movingSides.size() - 1).getSecond());
						else
							status = 'N';
					}
				}
				gl.glPopMatrix();
			}
		}
	}
	private void drawType_2(GL10 gl)
	{
		int nOfSide = 0;
		int curSide = rotSide % sizeOfCube;
		for (int i = 0; i < faceSecond.size(); i ++)
		{
			if (i == curSide)
			{
				gl.glPushMatrix();
				gl.glRotatef(rangle, 0.0f, 1.0f, 0.0f);
			}
			for (int j = 0; j < faceSecond.get(i).size(); j ++)
			{
				gl.glPushMatrix();
				int current = faceSecond.get(i).get(j);
				gl.glTranslatef(dxyz.get(current).get(0), dxyz.get(current).get(1),
						dxyz.get(current).get(2));
				nOfSide = current / sizeOfCube / sizeOfCube;
				gl.glRotatef(calcRX(nOfSide), 1.0f, 0.0f, 0.0f);
				gl.glRotatef(calcRY(nOfSide), 0.0f, 1.0f, 0.0f);
				gl.glRotatef(calcRZ(nOfSide), 0.0f, 0.0f, 1.0f);
				parts.get(current).draw(gl);
				gl.glPopMatrix();
			}
			if (i == curSide)
			{
				if (isClockWise) rangle += speed;else rangle -= speed;
				if (Math.abs(rangle) >= 90)
				{
					endRotate();
					if (status == 'A' || status == 'C')
					{
						if (movingSides.size() != 0)
							beginRotate(movingSides.get(movingSides.size() - 1).getFirst(),
									movingSides.get(movingSides.size() - 1).getSecond());
						else
							status = 'N';
					}
				}
				gl.glPopMatrix();
			}
		}
	}
	private void drawType_3(GL10 gl)
	{
		int nOfSide = 0;
		int curSide = rotSide % sizeOfCube;
		for (int i = 0; i < faceThird.size(); i ++)
		{
			if (i == curSide)
			{
				gl.glPushMatrix();
				gl.glRotatef(rangle, 0.0f, 0.0f, 1.0f);
			}
			for (int j = 0; j < faceThird.get(i).size(); j ++)
			{
				gl.glPushMatrix();
				int current = faceThird.get(i).get(j);
				gl.glTranslatef(dxyz.get(current).get(0), dxyz.get(current).get(1),
						dxyz.get(current).get(2));
				nOfSide = current / sizeOfCube / sizeOfCube;
				gl.glRotatef(calcRX(nOfSide), 1.0f, 0.0f, 0.0f);
				gl.glRotatef(calcRY(nOfSide), 0.0f, 1.0f, 0.0f);
				gl.glRotatef(calcRZ(nOfSide), 0.0f, 0.0f, 1.0f);
				parts.get(current).draw(gl);
				gl.glPopMatrix();
			}
			if (i == curSide)
			{
				if (isClockWise) rangle += speed;else rangle -= speed;
				if (Math.abs(rangle) >= 90)
				{
					endRotate();
					if (status == 'A' || status == 'C')
					{
						if (movingSides.size() != 0)
							beginRotate(movingSides.get(movingSides.size() - 1).getFirst(),
									movingSides.get(movingSides.size() - 1).getSecond());
						else
							status = 'N';
					}
				}
				gl.glPopMatrix();
			}
		}
	}
	private void endRotate()
	{
		if (status != 'A' && status != 'C') status='N';
		if (status != 'C')
		{
			madeMoves.add(new Pair(rotSide, isClockWise));
		}
		rangle=0;
		rotate(rotSide, isClockWise);
		rotSide=-1;
	}
	private void rotate(int rotSide, boolean isClockWise)
	{
		Vector<Part> partsTmp = new Vector<Part>(parts);
		for (int i = 0; i < swaps.get(rotSide).size(); i++)
		{
			if (isClockWise)
			{
				parts.removeElementAt(swaps.get(rotSide).get(i).getSecond());
				parts.add(swaps.get(rotSide).get(i).getSecond(),
						partsTmp.get(swaps.get(rotSide).get(i).getFirst()));
			} else
			{
				parts.removeElementAt(swaps.get(rotSide).get(i).getFirst());
				parts.add(swaps.get(rotSide).get(i).getFirst(),
						partsTmp.get(swaps.get(rotSide).get(i).getSecond()));
				//parts[swaps[rotSide][i].first] = partsTmp[swaps[rotSide][i].second];
			}
		}
	}
	private boolean isClockWiseDirection(Vector<Integer> setOfSides, int choosenSide)
	{
		if (choosenSide >= 0 && choosenSide <= sizeOfCube - 1)
		{
			if (sides.get(setOfSides.get(0)) == sides.get(setOfSides.get(1)))
			{
				if (sides.get(setOfSides.get(0)) != 2) return (setOfSides.get(0) < setOfSides.get(1));
				else return (setOfSides.get(0) > setOfSides.get(1));
			} else
			{
				if ((sides.get(setOfSides.get(0)) == 0 && sides.get(setOfSides.get(1)) == 5)
					|| (sides.get(setOfSides.get(0)) == 5 && sides.get(setOfSides.get(1)) == 2)
					|| (sides.get(setOfSides.get(0)) == 2 && sides.get(setOfSides.get(1)) == 4)
					|| (sides.get(setOfSides.get(0)) == 4 && sides.get(setOfSides.get(1)) == 0)
					) return true; else return false;
			}
		}
		if (choosenSide >= sizeOfCube  && choosenSide <= 2 * sizeOfCube - 1)
		{
			if (sides.get(setOfSides.get(0)) == sides.get(setOfSides.get(1)))
			{
				if (sides.get(setOfSides.get(0)) != 2) return (setOfSides.get(0) < setOfSides.get(1));
				else return (setOfSides.get(0) < setOfSides.get(1));
			} else
			{
				if ((sides.get(setOfSides.get(0)) == 0 && sides.get(setOfSides.get(1)) == 3)
					|| (sides.get(setOfSides.get(0)) == 3 && sides.get(setOfSides.get(1)) == 2)
					|| (sides.get(setOfSides.get(0)) == 2 && sides.get(setOfSides.get(1)) == 1)
					|| (sides.get(setOfSides.get(0)) == 1 && sides.get(setOfSides.get(1)) == 0)
					) return true; else return false;
			}
		}
		if (choosenSide >= 2 * sizeOfCube && choosenSide <= 3 * sizeOfCube - 1)
		{
			if (sides.get(setOfSides.get(0)) == sides.get(setOfSides.get(1)))
			{
				if (sides.get(setOfSides.get(0)) != 1 && sides.get(setOfSides.get(0)) != 5)
					return (setOfSides.get(0) > setOfSides.get(1));
				else
					return (setOfSides.get(0) < setOfSides.get(1));
			} else
			{
				if ((sides.get(setOfSides.get(0)) == 4 && sides.get(setOfSides.get(1)) == 3)
					||(sides.get(setOfSides.get(0)) == 3 && sides.get(setOfSides.get(1)) == 5)
					||(sides.get(setOfSides.get(0)) == 5 && sides.get(setOfSides.get(1)) == 1)
					||(sides.get(setOfSides.get(0)) == 1 && sides.get(setOfSides.get(1)) == 4)
					) return false; else return true;
			}
		}
		return true;
	}
	/* =================public declarations====================== */
	public int getCubeSize()
	{
		return sizeOfCube;
	}
	private float[] quad =
	{
				0.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				1.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f

	};
	public MCube(int size, GLTextures textures)
	{
		this.textures = textures;
		sizeOfCube = size;
		status = 'N';
		speed = 10.0f;
		mode = GL10.GL_MODELVIEW;

		//int current = 0; // current number of quadrat
		// Put numbers for each quad
		sides = new Vector<Integer>();
		for (int i = 0; i < 6; i ++)
		{
		//	Vector<?> tmp = new Vector();
			for (int j = 0; j < sizeOfCube * sizeOfCube; j++)
			{
				//tmp.add(current++);
				sides.add(i);
			}
		//	sidesIDColors.push_back(tmp);
			//tmp.clear();
		}
		madeMoves = new Vector< Pair<Integer, Boolean> >();
		madeMoves.clear();
		movingSides = new Vector< Pair<Integer, Boolean> >();
		movingSides.clear();
		// fill parts with parameters
		createFaces(size);
		createSwaps(size);
		fillParts(size);
		fillDXYZ(size);
		fillOSides(size);
	}
	private float calcRX(int side)
	{
		float rx = 0.0f;
		float ry = 0.0f;
		float rz = 0.0f;
		switch (side)
		{
		case 0: // white
			break;
		case 1: // red
			ry = -90.0f;
			break;
		case 2: // yellow
			ry = 180.0f;
			break;
		case 3: // orange
			ry = 90.0f;
			break;
		case 4: // green
			rx = -90.0f;
			break;
		case 5: // blue
			rx = 90.0f;
			break;
		}
		return rx;
	}
	private float calcRY(int side)
	{
		float rx = 0.0f;
		float ry = 0.0f;
		float rz = 0.0f;
		switch (side)
		{
		case 0: // white
			break;
		case 1: // red
			ry = -90.0f;
			break;
		case 2: // yellow
			ry = 180.0f;
			break;
		case 3: // orange
			ry = 90.0f;
			break;
		case 4: // green
			rx = -90.0f;
			break;
		case 5: // blue
			rx = 90.0f;
			break;
		}
		return ry;
	}
	private float calcRZ(int side)
	{
		float rx = 0.0f;
		float ry = 0.0f;
		float rz = 0.0f;
		switch (side)
		{
		case 0: // white
			break;
		case 1: // red
			ry = -90.0f;
			break;
		case 2: // yellow
			ry = 180.0f;
			break;
		case 3: // orange
			ry = 90.0f;
			break;
		case 4: // green
			rx = -90.0f;
			break;
		case 5: // blue
			rx = 90.0f;
			break;
		}
		return rz;
	}

	public void drawCube(GL10 gl, int mode)
	{
		setMode(mode);
		int nOfSide = 0;
		if (status == 'N')
		{
			for (int i = 0; i < dxyz.size(); i ++)
			{
				gl.glPushMatrix();
				gl.glTranslatef(dxyz.elementAt(i).elementAt(0), dxyz.elementAt(i).elementAt(1),
						dxyz.elementAt(i).elementAt(2));
				nOfSide = i / sizeOfCube / sizeOfCube;
				gl.glRotatef(calcRX(nOfSide), 1.0f, 0.0f, 0.0f);
				gl.glRotatef(calcRY(nOfSide), 0.0f, 1.0f, 0.0f);
				gl.glRotatef(calcRZ(nOfSide), 0.0f, 0.0f, 1.0f);
				parts.elementAt(i).draw(gl, mode);
				gl.glPopMatrix();
			}
		}
		if (status == 'R' || status == 'A' || status == 'C')
		{
			int hzside = rotSide / sizeOfCube;
			switch (hzside)
			{
			case 0:
				drawType_1(gl);
				break;
			case 1:
				drawType_2(gl);
				break;
			case 2:
				drawType_3(gl);
				break;
			}
		}
	}
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	public boolean isSelected(int id)
	{
		return parts.get(id).isSelected();
	}
	public void selectPart(int id)
	{
		parts.get(id).setSelected(true);
	}
	public void deselectPart(int id)
	{
		parts.get(id).setSelected(false);
	}
	public void beginRotate(int side, boolean isClockWise)
	{
		if (status != 'R')
		{
			if (status == 'A')
			{
				status = 'A';
				movingSides.remove(movingSides.size() - 1);
			} else
				if (status == 'C')
				{
					status = 'C';
					movingSides.remove(movingSides.size() - 1);
				} else
				{
					status = 'R';
				}
			rangle = 0;
			rotSide = side;
			this.isClockWise = isClockWise;
		}
	}

	public boolean checkCollision(float tx, float ty)
	{
		return true;
	}

	public boolean checkSidesForRotating(Vector<Integer> setOf)
	{
		Vector<Integer> setOfSides = new Vector<Integer>(setOf);
		////////
		// first int is element, another pair is i of element and its number

		if (setOfSides.size() < 2 || status != 'N') return false;

		int f = -1, s = -1;
		float minD = 999.0f;
		for (int i = 0; i < setOfSides.size() - 1; i++)
		{
			for (int j = i + 1; j < setOfSides.size(); j++)
			{
				Part p1 = parts.get(i);
				Part p2 = parts.get(j);
				float d = Math.abs(p1.z - p2.z);
				if (d < minD)
				{
					minD = d;
					f = setOfSides.get(i);
					s = setOfSides.get(j);
				}
			}
		}
	//	Log.i("f&&s", String.format("%d,%d",f,s));
		if (f == -1) return false;
		setOfSides.clear();
		setOfSides.add(f);
		setOfSides.add(s);
		Map<Integer, Integer> mp = new HashMap<Integer, Integer>();
		int maxSide = 0, iSide = -1;
		for (int i = 0; i < setOfSides.size(); i ++)
		{
			if (mp.get( oSides.get(setOfSides.get(i)).getFirst() ) != null)
			{
				mp.put( oSides.get(setOfSides.get(i)).getFirst(), mp.get(oSides.get(setOfSides.get(i)).getFirst()) + 1 );
			} else
			{
				mp.put(oSides.get(setOfSides.get(i)).getFirst(), 1);
			}
			if (mp.get( oSides.get(setOfSides.get(i)).getSecond() ) != null)
			{
				mp.put( oSides.get(setOfSides.get(i)).getSecond(), mp.get(oSides.get(setOfSides.get(i)).getSecond()) + 1 );
			} else
			{
				mp.put(oSides.get(setOfSides.get(i)).getSecond(), 1);
			}
			/*
			 * mp[oSides[ setOfSides[i] ].first]++;
				mp[oSides[ setOfSides[i] ].second]++;*/
		}
		Iterator<Entry<Integer, Integer>> iter = mp.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry<Integer, Integer> mEntry = (Map.Entry<Integer, Integer>)iter.next();
			if (mEntry.getValue() > maxSide)
			{
				maxSide = mEntry.getValue();
				iSide = mEntry.getKey();
			}
		}
		/*for (map<int, int>::iterator it = mp.begin(); it != mp.end(); it++)
		{
			if (it->second > maxSide)
			{
				maxSide = it->second;
				iSide = it->first;
			}
		}*/
		for (int i = setOfSides.size() - 1; i >= 0; i--)
		{
			if (oSides.get(setOfSides.get(i)).getFirst() != iSide
				&& oSides.get(setOfSides.get(i)).getSecond() != iSide)
				setOfSides.remove(i);
		}
		Log.i("Side", String.valueOf(iSide));
		if (setOfSides.size() >= 2)
		{
			beginRotate(iSide, isClockWiseDirection(setOfSides, iSide));
		}
		return true;
	}
	public char getStatus()
	{
		return status;
	}

	public void startRandomRotating(int numberOfMoves)
	{
		status = 'A';
		movingSides = new Vector< Pair<Integer, Boolean> >();
		movingSides.clear();
		for (int i = 0; i < numberOfMoves; i++)
		{
			int curS = (int) (Math.random() * (sizeOfCube * 3 - 1));
			int hz = (int)(Math.random() * 2);
			boolean res = (hz == 1);
			movingSides.add( new Pair(curS, res));
		}
		beginRotate(movingSides.lastElement().getFirst(), movingSides.lastElement().getSecond());
	}
	public void cancelMoves(int number)
	{
		if (madeMoves.size() != 0 && status == 'N')
		{
			status = 'C';
			int k = madeMoves.size();
			for (int i = k - number; i < k; i ++)
			{
				movingSides.add(new Pair(madeMoves.get(i).getFirst(), !madeMoves.get(i).getSecond()));
				madeMoves.remove(madeMoves.size() - 1);
			}
			beginRotate(movingSides.lastElement().getFirst(), movingSides.lastElement().getSecond());
		}
	}
}

