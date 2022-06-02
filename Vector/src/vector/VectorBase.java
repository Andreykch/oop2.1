package vector;

abstract class VectorBase
{
	private int[] coordinates;
	
    public VectorBase(int... coordinates)
    {
    	this.coordinates = coordinates.clone();
    }
    
    protected abstract VectorBase getInstance();
    
    private VectorBase getNewVector(VectorBase vector, int sign)
    {
    	VectorBase newVector = vector.getInstance();
    	newVector.coordinates = coordinates.clone();
    	for (int i = 0; i < coordinates.length; ++i)
    		newVector.coordinates[i] += sign * vector.coordinates[i];
    	return newVector;
    }
    
    protected VectorBase add(VectorBase vector)
    {
		return getNewVector(vector, 1);
    }

    protected VectorBase sub(VectorBase vector)
    {
		return getNewVector(vector, -1);
    }

    protected int scalarMultiply(VectorBase vector)
    {
    	int scalarMultiply = 0;
    	for (int i = 0; i < coordinates.length; ++i)
    		scalarMultiply += coordinates[i] * vector.coordinates[i];
    	return scalarMultiply;
    }

    @Override
    public int hashCode()
    {
    	int hash = 0;
    	for (int i = 0; i < coordinates.length; ++i)
    		hash = hash * 31 + coordinates[i];
        return hash;
    }
    
    @Override
    public boolean equals(Object obj)
    {
    	if (!obj.getClass().equals(getClass()) || obj.hashCode() != hashCode())
            return false;
    	VectorBase vector = (VectorBase)obj;
    	for (int i = 0; i < coordinates.length; ++i)
    		if (coordinates[i] != vector.coordinates[i])
    			return false;
    	return true;
    }
    
    @Override
    public String toString()
    {
    	StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (coordinates.length > 0)
        {
        	sb.append(" ");
            for(int i = 0; i < coordinates.length;++i)
               sb.append(coordinates[i]).append(", ");
            sb.deleteCharAt(sb.length() - 2);
        }
        return sb.append("]").toString();
    }
}