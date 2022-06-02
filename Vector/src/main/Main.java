package main;

import vector.Vector3D;

public class Main
{
    public static void main(String[] args)
    {
    	Vector3D vector1 = new Vector3D(1, 0, 0);
    	Vector3D vector2 = new Vector3D(1, 0, 0);
    	
    	Vector3D vector3 = vector1.add(vector2);
    	System.out.println(vector3);
    	
    	int vector4 = vector1.scalarMultiply(vector2);
    	System.out.println(vector4);
    	
    	System.out.println(vector1.equals(vector2));
    }
}