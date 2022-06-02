package parser;

public class WordReader implements IReadable
{
	private final String word;
	private final boolean ignoreCase;
	
	public WordReader(String word, boolean ignoreCase)
	{
		this.word = ignoreCase ? word.toUpperCase() : word;
		this.ignoreCase = ignoreCase;
	}

	public WordReader(String word)
	{
		this(word, false);
	}

	public Token tryGetToken(String input)
	{
		return ignoreCase && input.toUpperCase().startsWith(word) ||
			!ignoreCase && input.startsWith(word) ?
			new Token("keyword", input.substring(0, word.length())) : null;
	}
}