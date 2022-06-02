package parser;

public class IdentifierReader implements IReadable
{
	public Token tryGetToken(String text)
	{
		if (text.length() == 0 || !Character.isJavaIdentifierStart(text.charAt(0)))
			return null;
		int i = 0;
		while (i < text.length() && Character.isJavaIdentifierPart(text.charAt(i)))
			++i;
		return new Token("identifier", text.substring(0, i));
	}
}