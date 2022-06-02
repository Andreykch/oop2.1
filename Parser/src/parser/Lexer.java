package parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer
{
	private List<IReadable> readers = new ArrayList<IReadable>();
	
	private Token getToken(String text)
	{
		Token token = null;
		int len = 0;
        for (IReadable reader : readers)
        {
        	Token tmp = reader.tryGetToken(text);
        	if (tmp == null)
        		continue;
        	if (len < tmp.getText().length())
        	{
        		len = tmp.getText().length();
        		token = tmp;
        	}
        }
        return token;
	}
	
	public Token[] tokenize(String text)
	{
        List<Token> tokens = new ArrayList<Token>();
        while (text.length() != 0)
        {
        	Token token = getToken(text);
        	if (token == null)
        		return null;
    		tokens.add(token);
    		text = text.substring(token.getText().length());
        }
        return tokens.toArray(new Token[0]);
	}
	
	public void register(IReadable reader)
	{
		readers.add(reader);
	}
}