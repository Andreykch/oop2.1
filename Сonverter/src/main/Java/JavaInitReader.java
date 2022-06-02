package main.Java;

import parser.*;

public class JavaInitReader implements IReadable
{
	JavaInitReader() { }
	
	public Token tryGetToken(String text)
    {
		Token equal = new JavaEqualReader().tryGetToken(text);
		if (equal == null)
			return null;
		text = text.substring(equal.getText().length());
		Token endLineToken = new JavaWordReader(";").tryGetToken(text);
		if (endLineToken == null)
			return null;
		Token[] children = (Token[])equal.getValue();
		return new Token("Initialize", equal.getText() + ';', children);
    }
}