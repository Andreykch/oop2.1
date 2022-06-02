package main.Java;

import parser.*;

public class JavaBeginReader implements IReadable
{
	JavaBeginReader() { }

	public Token tryGetToken(String text)
	{
		String[] names = { "public", "class", "Main", "{", "public", "static",
				"void", "main", "(", "String", "[", "]", "args", ")" };
		for (String name : names)
		{
			Token whitespaces = new WhitespaceReader().tryGetToken(text);
			if (whitespaces != null)
				text = text.substring(whitespaces.getText().length());
			Token keyword = new WordReader(name, true).tryGetToken(text);
			if (keyword == null)
				return null;
			text = text.substring(keyword.getText().length());
		}
		return new JavaBeginBracketReader().tryGetToken(text);
	}
}
