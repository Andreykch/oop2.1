package main.Java;

import parser.*;

public class JavaEqualReader implements IReadable
{
	JavaEqualReader() { }
	
	public Token tryGetToken(String text)
    {
		Token arg1 = new JavaPrimitiveValueReader().tryGetToken(text);
		if (arg1 == null)
			return null;
		text = text.substring(arg1.getText().length());
		Token equal = new JavaWordReader("=").tryGetToken(text);
		if (equal == null)
			return null;
		text = text.substring(equal.getText().length());
		Token arg2 = new JavaValueReader().tryGetToken(text);
		if (arg2 == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(arg1.getText());
		sb.append(equal.getText());
		sb.append(arg2.getText());
		Token[] children = { arg1, arg2 };
		return new Token("Equal", sb.toString(), children);
    }
}