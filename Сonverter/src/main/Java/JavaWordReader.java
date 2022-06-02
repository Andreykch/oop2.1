package main.Java;

import parser.*;

public class JavaWordReader implements IReadable
{
	private final String word;
	
	public JavaWordReader(String word)
	{
		this.word = word;
	}
	
	public Token tryGetToken(String text)
    {
		Token whitespaces = new WhitespaceReader().tryGetToken(text);
		if (whitespaces != null)
			text = text.substring(whitespaces.getText().length());
		Token wordToken = new WordReader(word, true).tryGetToken(text);
		String wordTextHalf = whitespaces == null ? "" : whitespaces.getText();
		return wordToken != null ?
			new Token("AnyWord", wordTextHalf + wordToken.getText()) :
			null;
    }
}
