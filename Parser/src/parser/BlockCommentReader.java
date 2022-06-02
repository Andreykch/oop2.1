package parser;

public class BlockCommentReader implements IReadable
{
	public BlockCommentReader()
	{
	}

	public Token tryGetToken(String text)
	{
		if (text.length() == 0 || text.charAt(0) != '{')
			return null;
		int i = 1;
		while (i < text.length() && text.charAt(i) != '}')
			++i;
		return new Token("blockComment", text.substring(0, i == text.length() ? i : i + 1));
	}
}