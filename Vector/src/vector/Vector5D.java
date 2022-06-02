package vector;

public class Vector5D extends VectorBase implements IVector<Vector5D>
{
	public Vector5D(int x1, int x2, int x3, int x4, int x5)
	{
		super(x1, x2, x3, x4, x5);
	}

	@Override
    public Vector5D add(Vector5D vector)
    {
    	return (Vector5D)super.add(vector);
    }

	@Override
    public Vector5D sub(Vector5D vector)
    {
    	return (Vector5D)super.sub(vector);
    }

	@Override
    public int scalarMultiply(Vector5D vector)
    {
    	return super.scalarMultiply(vector);
    }

	@Override
	protected Vector5D getInstance()
	{
		return new Vector5D(0, 0, 0, 0, 0);
	}
}
