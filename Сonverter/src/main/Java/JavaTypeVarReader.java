package main.Java;

import parser.*;

public class JavaTypeVarReader implements IReadable
{
	private final String[][] types =
	{
			{ "int", "Integer" },
			{ "long", "Long" },
			{ "String", "String" },
			{ "float", "Float" },
			{ "double", "Double" },
			{ "boolean", "Boolean" },
	};
	
	public Token tryGetToken(String text)
    {
		Token whitespaces = new WhitespaceReader().tryGetToken(text);
		if (whitespaces != null)
			text = text.substring(whitespaces.getText().length());
		for (String[] type : types)
		{
			Token typeToken = new WordReader(type[0], true).tryGetToken(text);
			String typeVarTextHalf = whitespaces == null ? "" : whitespaces.getText();
			if (typeToken != null)
				return new Token("TypeVar", typeVarTextHalf + typeToken.getText(), type[1]);
		}
		return null;
    }
}
