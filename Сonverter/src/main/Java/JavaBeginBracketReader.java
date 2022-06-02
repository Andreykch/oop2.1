package main.Java;

import java.util.Arrays;

import parser.*;

public class JavaBeginBracketReader extends JavaLanguage implements IReadable
{
	JavaBeginBracketReader() { }
	
	public Token tryGetToken(String text)
	{
		Token whitespaces = new WhitespaceReader().tryGetToken(text);
		if (whitespaces != null)
			text = text.substring(whitespaces.getText().length());
		Token beginBracket = new WordReader("{").tryGetToken(text);
		if (beginBracket == null)
			return null;
		text = text.substring(beginBracket.getText().length());
		Lexer lexer = new Lexer();
		register(lexer);
		Token[] children = lexer.tokenize(text);
		if (children == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(whitespaces == null ? "" : whitespaces.getText());
		sb.append(beginBracket.getText());
		Token last = children[children.length - 1];
		children = Arrays.copyOf(children, children.length - 1);
		for (Token token : children)
			sb.append(token.getText());
		sb.append(last.getValue());
		return new Token("Root", sb.toString(), children);
	}
}
