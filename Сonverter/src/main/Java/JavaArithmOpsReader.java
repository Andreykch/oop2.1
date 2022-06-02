package main.Java;

import parser.*;

public class JavaArithmOpsReader implements IReadable
{
	JavaArithmOpsReader() { }
	
	private Object[] getUnaryPrefixOperation(String text)
	{
		Token operator = new JavaWordReader("++").tryGetToken(text);
		String typeOperation = operator != null ? "PrefixIncrement" :
			(operator = new JavaWordReader("--").tryGetToken(text)) != null ? "PrefixDecrement" :
			(operator = new JavaWordReader("-").tryGetToken(text)) != null ? "UnaryMinus" :
			(operator = new JavaWordReader("+").tryGetToken(text)) != null ? "UnaryPlus" :
			null;
		return typeOperation != null ? new Object[] { operator, typeOperation } : null;
	}
	
	private Object[] getUnaryPostfixOperation(String text)
	{
		Token operator = new JavaWordReader("++").tryGetToken(text);
		String typeOperation = operator != null ? "PostfixIncrement" :
			(operator = new JavaWordReader("--").tryGetToken(text)) != null ? "PostfixDecrement" :
			null;
		return typeOperation != null ? new Object[] { operator, typeOperation } : null;
	}

	private Object[] getBinaryOperation(String text)
	{
		Token operator = new JavaWordReader("+").tryGetToken(text);
		String typeOperation = operator != null ? "Plus" :
			(operator = new JavaWordReader("-").tryGetToken(text)) != null ? "Minus" :
			(operator = new JavaWordReader("/").tryGetToken(text)) != null ? "Divide" :
			(operator = new JavaWordReader("*").tryGetToken(text)) != null ? "Multiple" :
			(operator = new JavaWordReader("^").tryGetToken(text)) != null ? "Xor" :
			(operator = new JavaWordReader("|").tryGetToken(text)) != null ? "Or" :
			(operator = new JavaWordReader("&").tryGetToken(text)) != null ? "And" :
			(operator = new JavaWordReader("%").tryGetToken(text)) != null ? "Mod" :
			//(operator = new JavaWordReader("=").tryGetToken(text)) != null ? "Equal" :
			null;
		return typeOperation != null ? new Object[] { operator, typeOperation } : null;
	}
	
	public Token tryGetToken(String text)
    {
		if (new JavaWordReader("for").tryGetToken(text) != null)
			return null;
		Object[] operationInfo = getUnaryPrefixOperation(text);
		if (operationInfo != null)
			text = text.substring(((Token)operationInfo[0]).getText().length());
		Token arg1 = new JavaPrimitiveValueReader().tryGetToken(text);
		if (arg1 != null)
		{
			if (operationInfo != null)
			{
				Token operator = (Token)operationInfo[0];
				String typeOperatation = (String)operationInfo[1];
				Token[] children = { arg1 };
				return new Token(typeOperatation,
					operator.getText() + arg1.getText(), children);
			}
		}
		else
			return null;
		text = text.substring(arg1.getText().length());
		operationInfo = getUnaryPostfixOperation(text);
		if (operationInfo != null)
		{
			Token operator = (Token)operationInfo[0];
			String typeOperatation = (String)operationInfo[1];
			Token[] children = { arg1 };
			return new Token(typeOperatation,
				operator.getText() + arg1.getText(), children);
		}
		operationInfo = getBinaryOperation(text);
		if (operationInfo == null)
			return null;
		text = text.substring(((Token)operationInfo[0]).getText().length());
		Token arg2 = new JavaPrimitiveValueReader().tryGetToken(text);//new JavaPrimitiveValueReader().tryGetToken(text);
		if (arg2 == null)
			return null;
		//text = text.substring(arg2.getText().length());
		//Token endLineToken = new JavaWordReader(";").tryGetToken(text);
		Token operator = ((Token)operationInfo[0]);
		String typeOperation = ((String)operationInfo[1]);
		StringBuilder sb = new StringBuilder();
		sb.append(arg1.getText());
		sb.append(operator.getText());
		sb.append(arg2.getText());
		//if (endLineToken != null)
		//	sb.append(';');
		Token[] children = { arg1, arg2 };
		return new Token(typeOperation, sb.toString(), children);
    }
}