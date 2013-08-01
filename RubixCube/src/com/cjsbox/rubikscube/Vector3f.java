package com.cjsbox.rubikscube;

class Vector3f {
    public float x, y, z;

    public static void cross(Vector3f Result, Vector3f v1, Vector3f v2) {
        Result.x = (v1.y * v2.z) - (v1.z * v2.y);
        Result.y = (v1.z * v2.x) - (v1.x * v2.z);
        Result.z = (v1.x * v2.y) - (v1.y * v2.x);
    }
    public static float dot(Vector3f v1, Vector3f v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z + v2.z);
    }
    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    
    public static void minus(Vector3f Result, Vector3f u, Vector3f v){
        Result.x = u.x-v.x;
        Result.y = u.y-v.y;
        Result.z = u.z-v.z;
    }
    public static void addition(Vector3f Result, Vector3f u, Vector3f v){
        Result.x = u.x + v.x;
        Result.y = u.y + v.y;
        Result.z = u.z + v.z;
    }
    
    //scalar product
    public static Vector3f scalarProduct(float r, Vector3f u){
    	Vector3f Result = new Vector3f();
        Result.x = u.x * r;
        Result.y = u.y * r;
        Result.z = u.z * r;
        return Result;
    }
}