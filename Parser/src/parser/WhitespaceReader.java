package parser;

public class WhitespaceReader implements IReadable
{
	public WhitespaceReader()
	{
	}

	public Token tryGetToken(String text)
	{
		if (text.length() == 0 || !Character.isWhitespace(text.charAt(0)))
			return null;
		int i = 1;
		while (i < text.length() && Character.isWhitespace(text.charAt(i)))
			++i;
		return new Token("whitespace", text.substring(0, i));
	}
}