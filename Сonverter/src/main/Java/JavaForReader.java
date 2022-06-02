package main.Java;

import parser.*;

public class JavaForReader implements IReadable
{
	private final IReadable[] readersFor1 =
		{
			new JavaWordReader("for"),
			new JavaWordReader("("),
			new JavaVarReader(),
			new JavaCompareReader(),
			new JavaWordReader(";"),
			new JavaArithmOpsReader(),
			new JavaWordReader(")"),
		};
	private final IReadable[] readersFor2 =
		{
			new JavaWordReader("for"),
			new JavaWordReader("("),
			new JavaVarReader(),
			new JavaCompareReader(),
			new JavaWordReader(";"),
			new JavaEqualReader(),
			new JavaWordReader(")"),
		};
	private final IReadable[] readersFor3 =
		{
			new JavaWordReader("for"),
			new JavaWordReader("("),
			new JavaInitReader(),
			new JavaCompareReader(),
			new JavaWordReader(";"),
			new JavaArithmOpsReader(),
			new JavaWordReader(")"),
		};
	private final IReadable[] readersFor4 =
		{
			new JavaWordReader("for"),
			new JavaWordReader("("),
			new JavaInitReader(),
			new JavaCompareReader(),
			new JavaWordReader(";"),
			new JavaEqualReader(),
			new JavaWordReader(")"),
		};
	private final IReadable[] readersBodyFor =
		{
			new JavaBeginBracketReader(),
			new JavaMethodsReader(),
			new JavaInitReader(),
		};
	private final IReadable[][] readersBeginFor =
		{
			readersFor1,
			readersFor2,
			readersFor3,
			readersFor4,
		};
	
	private Object[] getForFields(String text, IReadable[] readers)
	{
		Token[] fields = new Token[readers.length + 1];
		int i = 0;
		for (; i < readers.length; ++i)
			if ((fields[i] = readers[i].tryGetToken(text)) == null)
				return null;
			else
				text = text.substring(fields[i].getText().length());
		fields[i] = new JavaWordReader(";").tryGetToken(text);
		if (fields[i] != null)
			return new Object[]
				{
					fields,
					new Token[] { fields[2], fields[3], fields[5] }
				};
		for (IReadable reader : readersBodyFor)
			if ((fields[i] = reader.tryGetToken(text)) != null)
				return new Object[]
					{
						fields,
						new Token[] { fields[2], fields[3], fields[5], fields[i] }
					};
		Token forWord = new JavaWordReader("for").tryGetToken(text);
		if (forWord != null)
			if ((fields[i] = new JavaForReader().tryGetToken(text)) != null)
				return new Object[]
					{
						fields,
						new Token[] { fields[2], fields[3], fields[5], fields[i] }
					};
		Token ifWord = new JavaWordReader("if").tryGetToken(text);
		if (ifWord != null)
			if ((fields[i] = new JavaIfReader().tryGetToken(text)) != null)
				return new Object[]
					{
						fields,
						new Token[] { fields[2], fields[3], fields[5], fields[i] }
					};
		return null;
	}
	
	public Token tryGetToken(String text)
    {
		Object[] pair = null;
		for (IReadable[] readers : readersBeginFor)
			if ((pair = getForFields(text, readers)) != null)
			    break;
		if (pair == null)
			return null;
		Token[] forFields = (Token[])pair[0];
		Token[] children = (Token[])pair[1];
		if (children == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (Token field : forFields)
			sb.append(field.getText());
		return new Token("For", sb.toString(), children);
    }
}
