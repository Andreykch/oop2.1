package main.Java;

import parser.*;

public class JavaEndBracketReader implements IReadable
{
	JavaEndBracketReader() { }
	
	public Token tryGetToken(String text)
	{
		Token whitespaces = new WhitespaceReader().tryGetToken(text);
		if (whitespaces != null)
			text = text.substring(whitespaces.getText().length());
		Token bracket = new WordReader("}").tryGetToken(text);
		String halfText = whitespaces == null ? "" : whitespaces.getText();
		return bracket != null ?
		    new Token("", halfText + text, halfText + bracket.getText()) :
		    null;
	}
}
