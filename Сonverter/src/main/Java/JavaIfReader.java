package main.Java;

import parser.*;

public class JavaIfReader implements IReadable
{
	private final IReadable[] readersIf =
		{
			new JavaWordReader("if"),
			new JavaWordReader("("),
			new JavaCompareReader(),
			new JavaWordReader(")"),
		};
	private final IReadable[] readersBodyIf =
		{
			new JavaBeginBracketReader(),
		    new JavaInitReader(),
		    new JavaMethodsReader(),
		};
	
	private Object[] getIfFields(String text)
	{
		Token[] fields = new Token[readersIf.length + 1];
		int i = 0;
		for (; i < readersIf.length; ++i)
			if ((fields[i] = readersIf[i].tryGetToken(text)) == null)
				return null;
			else
				text = text.substring(fields[i].getText().length());
		fields[i] = new JavaWordReader(";").tryGetToken(text);
		if (fields[i] != null)
			return new Object[] { fields, new Token[] { fields[2] } };
		for (IReadable reader : readersBodyIf)
			if ((fields[i] = reader.tryGetToken(text)) != null)
				return new Object[]	{ fields, new Token[] { fields[2], fields[i] } };
		Token ifWord = new JavaWordReader("if").tryGetToken(text);
		if (ifWord != null)
			if ((fields[i] = new JavaIfReader().tryGetToken(text)) != null)
				return new Object[]	{ fields, new Token[] { fields[2], fields[i] } };
		Token forWord = new JavaWordReader("for").tryGetToken(text);
		if (forWord != null)
			if ((fields[i] = new JavaForReader().tryGetToken(text)) != null)
				return new Object[]	{ fields, new Token[] { fields[2], fields[i] } };
		return null;
	}
	
	public Token tryGetToken(String text)
    {
		Object[] pair = getIfFields(text);
		if (pair == null)
			return null;
		Token[] fields = (Token[])pair[0];
		Token[] children = (Token[])pair[1];
		StringBuilder sb = new StringBuilder();
		for (Token field : fields)
			sb.append(field.getText());
		return new Token("If", sb.toString(), children);
    }
}
