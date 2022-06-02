package parser;

public class StringReader implements IReadable
{
	public StringReader()
	{
	}

	public Token tryGetToken(String text)
	{
		if (text.length() == 0 || text.charAt(0) != '"')
			return null;
		int i = 1;
		while (i < text.length())
			if (text.charAt(i) == '"')
				return new Token("string", text.substring(0, i + 1), text.substring(1, i));
			else if (text.charAt(i) == '\\')
				i += 2;
			else
				++i;
		return null;
	}
}