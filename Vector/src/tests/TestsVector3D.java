package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import vector.Vector3D;
import vector.Vector5D;
import vector.VectorND;

public class TestsVector3D
{
    @Test
    public void addingVectors()
    {
    	Vector3D vector1 = new Vector3D(10, 20, 30);
    	Vector3D vector2 = new Vector3D(40, 50, 60);
    	assertEquals(new Vector3D(50, 70, 90), vector1.add(vector2));
    }
    
    @Test(expected = NullPointerException.class)
    public void operatingWithNull()
    {
    	Vector3D vector1 = new Vector3D(10, 20, 30);
    	vector1.add(null);
    	vector1.sub(null);
    	vector1.scalarMultiply(null);
    }
    
    @Test
    public void subbingVectors()
    {
    	Vector3D vector1 = new Vector3D(10, 20, 30);
    	Vector3D vector2 = new Vector3D(40, 50, 60);
    	assertEquals(new Vector3D(-30, -30, -30), vector1.sub(vector2));
    }
    
    @Test
    public void scalarMultiplyVectors()
    {
    	Vector3D vector1 = new Vector3D(10, 20, 30);
    	Vector3D vector2 = new Vector3D(40, 50, 60);
    	assertEquals(10 * 40 + 20 * 50 + 30 * 60, vector1.scalarMultiply(vector2));
    }
    
    @Test
    public void notEqualsVectors()
    {
    	Vector3D vector1 = new Vector3D(10, 20, 30);
    	assertFalse(vector1.equals(new Vector3D(40, 50, 60)));
    	assertFalse(vector1.equals(new Vector5D(40, 50, 60, 70, 80)));
    	assertFalse(vector1.equals(new VectorND(10, 20, 30)));
    }
    
    @Test
    public void equalsVectors()
    {
    	Vector3D vector1 = new Vector3D(10, 20, 30);
    	Vector3D vector2 = new Vector3D(10, 20, 30);
    	assertTrue(vector1.equals(vector2));
    }
    
    @Test
    public void vectorToString()
    {
    	Vector3D vector1 = new Vector3D(10, 20, 30);
    	assertEquals("[ 10, 20, 30 ]", vector1.toString());
    }
}
