package vector;

interface IVector<T>
{
	T add(T vector);
	T sub(T vector);
	int scalarMultiply(T vector);
	boolean equals(Object obj);
	String toString();
}