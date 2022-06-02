package vector;

public class Vector3D extends VectorBase implements IVector<Vector3D>
{
	public Vector3D(int x, int y, int z)
	{
		super(x, y, z);
	}

	@Override
    public Vector3D add(Vector3D vector)
    {
		return (Vector3D)super.add(vector);
    }

	@Override
    public Vector3D sub(Vector3D vector)
    {
    	return (Vector3D)super.sub(vector);
    }

	@Override
    public int scalarMultiply(Vector3D vector)
    {
    	return super.scalarMultiply(vector);
    }
    
    @Override
	protected Vector3D getInstance()
    {
    	return new Vector3D(0, 0, 0);
    }
}
