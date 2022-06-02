package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import vector.Vector3D;
import vector.Vector5D;
import vector.VectorND;

public class TestsVector5D
{
    @Test
    public void addingVectors()
    {
    	Vector5D vector1 = new Vector5D(10, 20, 30, 40, 50);
    	Vector5D vector2 = new Vector5D(40, 50, 60, 70, 80);
    	assertEquals(new Vector5D(50, 70, 90, 110, 130), vector1.add(vector2));
    }
    
    @Test(expected = NullPointerException.class)
    public void operatingWithNull()
    {
    	Vector5D vector1 = new Vector5D(10, 20, 30, 40, 50);
    	vector1.add(null);
    	vector1.sub(null);
    	vector1.scalarMultiply(null);
    }
    
    @Test
    public void subbingVectors()
    {
    	Vector5D vector1 = new Vector5D(10, 20, 30, 40, 50);
    	Vector5D vector2 = new Vector5D(40, 50, 60, 70, 80);
    	assertEquals(new Vector5D(-30, -30, -30, -30, -30), vector1.sub(vector2));
    }
    
    @Test
    public void scalarMultiplyVectors()
    {
    	Vector5D vector1 = new Vector5D(10, 20, 30, 40, 50);
    	Vector5D vector2 = new Vector5D(40, 50, 60, 70, 80);
    	assertEquals(10 * 40 + 20 * 50 + 30 * 60 + 40 * 70 + 50 * 80, vector1.scalarMultiply(vector2));
    }
    
    @Test
    public void notEqualsVectors()
    {
    	Vector5D vector1 = new Vector5D(10, 20, 30, 40, 50);
    	assertFalse(vector1.equals(new Vector5D(40, 50, 60, 70, 80)));
    	assertFalse(vector1.equals(new Vector3D(60, 70, 80)));
    	assertFalse(vector1.equals(new VectorND(10, 20, 30, 40, 50)));
    }
    
    @Test
    public void equalsVectors()
    {
    	Vector5D vector1 = new Vector5D(10, 20, 30, 40, 50);
    	Vector5D vector2 = new Vector5D(10, 20, 30, 40, 50);
    	assertTrue(vector1.equals(vector2));
    }
    
    @Test
    public void vectorToString()
    {
    	Vector5D vector1 = new Vector5D(10, 20, 30, 40, 50);
    	assertEquals("[ 10, 20, 30, 40, 50 ]", vector1.toString());
    }
}
