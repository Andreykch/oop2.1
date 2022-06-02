package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import vector.Vector3D;
import vector.Vector5D;
import vector.VectorND;

public class TestsVectorND
{
    @Test
    public void addingVectors()
    {
    	VectorND vector1 = new VectorND(10);
    	VectorND vector2 = new VectorND(40);
    	assertEquals(new VectorND(50), vector1.add(vector2));
    }

    @Test
    public void addingEmptyVectors()
    {
    	VectorND vector1 = new VectorND();
    	VectorND vector2 = new VectorND();
    	assertEquals(new VectorND(), vector1.add(vector2));
    }
    
    @Test(expected = NullPointerException.class)
    public void operatingWithNull()
    {
    	VectorND vector1 = new VectorND(10);
    	vector1.add(null);
    	vector1.sub(null);
    	vector1.scalarMultiply(null);
    }
    
    @Test
    public void subbingVectors()
    {
    	VectorND vector1 = new VectorND(10);
    	VectorND vector2 = new VectorND(40);
    	assertEquals(new VectorND(-30), vector1.sub(vector2));
    }

    @Test
    public void subbingEmptyVectors()
    {
    	VectorND vector1 = new VectorND();
    	VectorND vector2 = new VectorND();
    	assertEquals(new VectorND(), vector1.sub(vector2));
    }
    
    @Test
    public void scalarMultiplyVectors()
    {
    	VectorND vector1 = new VectorND(10);
    	VectorND vector2 = new VectorND(40);
    	assertEquals(10 * 40, vector1.scalarMultiply(vector2));
    }

    @Test
    public void scalarMultiplyEmptyVectors()
    {
    	VectorND vector1 = new VectorND();
    	VectorND vector2 = new VectorND();
    	assertEquals(0, vector1.scalarMultiply(vector2));
    }
    
    @Test
    public void notEqualsVectors()
    {
    	VectorND vector1 = new VectorND(10, 20, 30, 40, 50);
    	assertFalse(vector1.equals(new Vector5D(40, 50, 60, 70, 80)));
    	assertFalse(vector1.equals(new Vector3D(60, 70, 80)));
    	assertFalse(vector1.equals(new VectorND(10, 20, 30, 40)));
    }
    
    @Test
    public void equalsVectors()
    {
    	VectorND vector1 = new VectorND(10);
    	VectorND vector2 = new VectorND(10);
    	assertTrue(vector1.equals(vector2));
    	assertTrue(new VectorND().equals(new VectorND()));
    }
    
    @Test
    public void vectorToString()
    {
    	VectorND vector1 = new VectorND(10);
    	assertEquals("[ 10 ]", vector1.toString());
    	VectorND vector2 = new VectorND();
    	assertEquals("[]", vector2.toString());
    }
}
