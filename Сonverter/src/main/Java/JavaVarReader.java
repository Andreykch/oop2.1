package main.Java;

import parser.*;

public class JavaVarReader implements IReadable
{
	JavaVarReader() { }

	private final IReadable[] readersWithInit1 =
		{
			new JavaTypeVarReader(),
			new JavaNameVarReader(),
			new JavaWordReader("="),
			new JavaValueReader(),
			new JavaWordReader(";")
		};
	private final IReadable[] readersWithoutInit =
		{
			new JavaTypeVarReader(),
			new JavaNameVarReader(),
			new JavaWordReader(";")
		};
	
	private Token[] getVarFields(String text, IReadable[] readers)
	{
		Token[] fields = new Token[readers.length];
		for (int i = 0; i < readers.length; ++i)
		{
			fields[i] = readers[i].tryGetToken(text);
			if (fields[i] == null)
				return null;
			text = text.substring(fields[i].getText().length());
		}
		return fields;
	}
	
	public Token tryGetToken(String text)
    {
		Token[] varFields = getVarFields(text, readersWithInit1);
		Token[] children;
		if (varFields != null)
			children = new Token[] { varFields[0], varFields[1], varFields[3] };
		else if ((varFields = getVarFields(text, readersWithoutInit)) != null)
			children = new Token[] { varFields[0], varFields[1] };
		else
			return null;
		StringBuilder sb = new StringBuilder();
		for (Token field : varFields)
			sb.append(field.getText());
		return new Token("Var", sb.toString(), children);
    }
}
