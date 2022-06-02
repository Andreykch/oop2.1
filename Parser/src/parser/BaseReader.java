package parser;

import java.util.HashMap;

public abstract class BaseReader implements IReadable
{
	private HashMap<Character, Character> tableTransitions = new HashMap<Character, Character>();
	private String typeComment;
	
	protected void setTypeComment(String typeComment)
	{
		this.typeComment = typeComment;
	}
	
	protected void putTransition(Character key, Character value)
	{
		tableTransitions.put(key, value);
	}
	
	public Token tryGetToken(String text)
	{
		if (!(text.length() > 1 &&
			tableTransitions.get(text.charAt(0)) != null &&
			tableTransitions.get(text.charAt(0)) == text.charAt(1) &&
			tableTransitions.get(null) == text.charAt(1) &&
			tableTransitions.get(text.charAt(1)) == text.charAt(0)))
			return null;
		int i = 2;
		while (i < text.length())
			if (text.charAt(i) == text.charAt(0) ||
			    tableTransitions.get(text.charAt(i)) == null)
				++i;
			else
				if (++i < text.length() &&
					tableTransitions.get(text.charAt(i - 1)) == tableTransitions.get(tableTransitions.get(text.charAt(i))))
					break;
		if (i < text.length() &&
			text.charAt(i) != text.charAt(i - 1) &&
			tableTransitions.get(text.charAt(i)) != null &&
			text.charAt(i - 1) == tableTransitions.get(text.charAt(i)))
			++i;
		return new Token(typeComment, text.substring(0, i));
	}
}
