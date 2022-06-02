package main.Java;

import parser.*;

public class JavaCompareReader implements IReadable
{
	JavaCompareReader() { }
	
	private Object[] getTypeCompare(String text)
	{
		Token operator = new JavaWordReader("<").tryGetToken(text);
		String typeCompare = null;
		if (operator != null)
		{
			Token operator2 = new JavaWordReader("<=").tryGetToken(text);
			if (operator2 != null)
			{
				operator = operator2;
				typeCompare = "CompareLessOrEqual";
			}
			else
				typeCompare = "CompareLess";
		}
		else if ((operator = new JavaWordReader(">").tryGetToken(text)) != null)
		{
			Token operator2 = new JavaWordReader(">=").tryGetToken(text);
			if (operator2 != null)
			{
				operator = operator2;
				typeCompare = "CompareThanOrEqual";
			}
			else
				typeCompare = "CompareThan";
		}
		else if ((operator = new JavaWordReader("==").tryGetToken(text)) != null)
			typeCompare = "CompareEqual";
		else
			return null;
		return typeCompare != null ? new Object[] { operator, typeCompare } : null;
	}
	
	public Token tryGetToken(String text)
    {
		Token arg1 = new JavaPrimitiveValueReader().tryGetToken(text);
		if (arg1 != null)
			text = text.substring(arg1.getText().length());
		else
			return null;
		Object[] compareFields = getTypeCompare(text);
		if (compareFields == null)
			return null;
		String typeCompare = (String)compareFields[1];
		Token operator = (Token)compareFields[0];
		text = text.substring(operator.getText().length());
		Token arg2 = new JavaPrimitiveValueReader().tryGetToken(text);
		if (arg2 != null)
			text = text.substring(arg2.getText().length());
		else
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(arg1.getText());
		sb.append(operator.getText());
		sb.append(arg2.getText());
		Token[] children = { arg1, arg2 };
		return new Token(typeCompare, sb.toString(), children);
    }
}