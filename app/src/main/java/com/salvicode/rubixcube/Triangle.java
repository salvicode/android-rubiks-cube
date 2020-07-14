package com.salvicode.rubixcube;

import java.util.Arrays;

public class Triangle {
    public Vector3f V0;
    public Vector3f V1;
    public Vector3f V2;
 
    public Triangle(Vector3f V0, Vector3f V1, Vector3f V2){
        this.V0 =V0;
        this.V1 = V1;
        this.V2 = V2;
    }
 
    
    private static final float SMALL_NUM =  0.00000001f; // anything that avoids division overflow
 
 
    // intersectRayAndTriangle(): intersect a ray with a 3D triangle
//    Input:  a ray R, and a triangle T
//    Output: *I = intersection point (when it exists)
//    Return: -1 = triangle is degenerate (a segment or point)
//             0 = disjoint (no intersect)
//             1 = intersect in unique point I1
//             2 = are in the same plane
    public static int intersectRayAndTriangle(Ray R, Triangle T, Vector3f I)
    {
       	Vector3f    u, v, n;             // triangle vectors
        Vector3f    dir, w0, w;          // ray vectors
        float     r, a, b;             // params to calc ray-plane intersect
        // get triangle edge vectors and plane normal
        u = new Vector3f();
        v = new Vector3f();
        n = new Vector3f();
        Vector3f.minus(u, T.V1, T.V0);
        Vector3f.minus(v, T.V2, T.V0);
        Vector3f.cross(n, u, v);             // cross product
 
        if (Arrays.equals(new float[]{n.x, n.y, n.z}, new float[]{0.0f,0.0f,0.0f})){           // triangle is degenerate
            return -1;                 // do not deal with this case
        }
        dir = new Vector3f();
        w0 = new Vector3f();
        w = new Vector3f();
        Vector3f.minus(dir, R.P1, R.P0);             // ray direction vector
        Vector3f.minus(w0, R.P0 , T.V0);
        a = - Vector3f.dot(n,w0);
        b =  Vector3f.dot(n,dir);
        if (Math.abs(b) < SMALL_NUM) {     // ray is parallel to triangle plane
            if (a == 0){                // ray lies in triangle plane
                return 2;
            }else{
                return 0;             // ray disjoint from plane
            }
        }
 
        // get intersect point of ray with triangle plane
        r = a / b;
        if (r < 0.0f){                   // ray goes away from triangle
            return 0;                  // => no intersect
        }
        // for a segment, also test if (r > 1.0) => no intersect
 
        Vector3f tempI = new Vector3f();
        Vector3f.addition(tempI, R.P0,  Vector3f.scalarProduct(r, dir));           // intersect point of ray and plane
        I.x = tempI.x;
        I.y = tempI.y;
        I.z = tempI.z;
 
        // is I inside T?
        float    uu, uv, vv, wu, wv, D;
        uu =  Vector3f.dot(u,u);
        uv =  Vector3f.dot(u,v);
        vv =  Vector3f.dot(v,v);
        Vector3f.minus(w, I, T.V0);
        wu =  Vector3f.dot(w,u);
        wv = Vector3f.dot(w,v);
        D = (uv * uv) - (uu * vv);
 
        // get and test parametric coords
        float s, t;
        s = ((uv * wv) - (vv * wu)) / D;
        if (s < 0.0f || s > 1.0f)        // I is outside T
            return 0;
        t = (uv * wu - uu * wv) / D;
        if (t < 0.0f || (s + t) > 1.0f)  // I is outside T
            return 0;
 
        return 1;                      // I is in T
    }
}
