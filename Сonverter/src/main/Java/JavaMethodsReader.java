package main.Java;

import parser.*;

public class JavaMethodsReader implements IReadable
{
	JavaMethodsReader() { }
	
	private String[][] methods()
	{
		return new String[][]
	        {
				{ "System.out.println", "WriteLine" },
				{ "System.out.print", "Write" },
			};
	}
	
	public Token tryGetToken(String text)
	{
		Token whitespaces = new WhitespaceReader().tryGetToken(text);
		if (whitespaces != null)
			text = text.substring(whitespaces.getText().length());
		Token methodToken = null;
		String methodUniversal = null;
		for (String[] method : methods())
		{
			methodToken = new WordReader(method[0], true).tryGetToken(text);
			if (methodToken != null)
			{
				methodUniversal = method[1];
				break;
			}
		}
		if (methodToken == null)
			return null;
		text = text.substring(methodToken.getText().length());
		Token beginRoundBracket = new JavaWordReader("(").tryGetToken(text);
		if (beginRoundBracket == null)
			return null;
		text = text.substring(beginRoundBracket.getText().length());
		Token arg = new JavaValueReader().tryGetToken(text);
		if (arg == null)
			return null;
		text = text.substring(arg.getText().length());
		Token endRoundBracket = new JavaWordReader(")").tryGetToken(text);
		if (endRoundBracket == null)
			return null;
		text = text.substring(endRoundBracket.getText().length());
		Token endline = new JavaWordReader(";").tryGetToken(text);
		if (endline == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(whitespaces == null ? "" : whitespaces.getText());
		sb.append(methodToken.getText());
		sb.append(beginRoundBracket.getText());
		sb.append(arg.getText());
		sb.append(endRoundBracket.getText());
		sb.append(endline.getText());
		Token[] pair = { new Token(methodUniversal, null), arg };
		return new Token("Method", sb.toString(), pair);
	}
}
