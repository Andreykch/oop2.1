package main.Java;

import parser.*;

public class JavaNameVarReader implements IReadable
{
	JavaNameVarReader() { }
	
	public Token tryGetToken(String text)
    {
		Token whitespaces = new WhitespaceReader().tryGetToken(text);
		if (whitespaces != null)
			text = text.substring(whitespaces.getText().length());
		Token name = new IdentifierReader().tryGetToken(text);
		String nameTextHalf = whitespaces == null ? "" : whitespaces.getText();
		return name != null ?
			new Token("NameVar", nameTextHalf + name.getText(), name.getText()) :
			null;
    }
}
