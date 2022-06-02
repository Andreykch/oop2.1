package vector;

public class VectorND extends VectorBase implements IVector<VectorND>
{
	public VectorND(int... coordinates)
	{
		super(coordinates);
	}

	@Override
    public VectorND add(VectorND vector)
    {
    	return (VectorND)super.add(vector);
    }

	@Override
    public VectorND sub(VectorND vector)
    {
    	return (VectorND)super.sub(vector);
    }

	@Override
    public int scalarMultiply(VectorND vector)
    {
    	return super.scalarMultiply(vector);
    }

	@Override
	public VectorBase getInstance()
	{
		return new VectorND();
	}
}
