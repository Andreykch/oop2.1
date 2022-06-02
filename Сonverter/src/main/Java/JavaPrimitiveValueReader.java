package main.Java;

import parser.*;

public class JavaPrimitiveValueReader implements IReadable
{
	private final IReadable[] readers =
	    {
			new FloatReader(),
			new IntReader(),
			new StringReader(),
			new CharacterReader(),
			new IdentifierReader(),
	    };
	
	private Token getTokenValue(String text)
	{
		Token value = null;
		int len = 0;
        for (IReadable reader : readers)
        {
        	Token tmp = reader.tryGetToken(text);
        	if (tmp != null && len < tmp.getText().length())
        	{
        		len = tmp.getText().length();
        		value = tmp;
        	}
        }
        return value;
	}
	
	public Token tryGetToken(String text)
    {
		Token whitespaces = new WhitespaceReader().tryGetToken(text);
		if (whitespaces != null)
			text = text.substring(whitespaces.getText().length());
		Token value = getTokenValue(text);
		String valueTextHalf = whitespaces == null ? "" : whitespaces.getText();
		return value != null ?
			new Token("PrimitiveValue", valueTextHalf + value.getText(), new Token[] { value }) :
			null;
    }
}
