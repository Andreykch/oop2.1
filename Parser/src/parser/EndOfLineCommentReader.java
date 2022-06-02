package parser;

public class EndOfLineCommentReader extends BaseReader
{
	public EndOfLineCommentReader()
	{
		putTransition('/', '/');
		putTransition(null, '/');
		putTransition('\n', '/');
		setTypeComment("endOfLineComment");
	}
}