package parser;

public class TraditionalCommentReader extends BaseReader
{
	public TraditionalCommentReader()
	{
		putTransition('/', '*');
		putTransition('*', '/');
		putTransition(null, '*');
		setTypeComment("traditionalComment");
	}
}