package main;

import parser.*;

public abstract class ILanguage
{
	protected IReadable beginReader;
	protected IWriteable beginWriter;
	
	public Token getTreeFromString(String src)
	{
		return beginReader.tryGetToken(src);
	}

	public Token[] getTokensFromTree(Token tree)
	{
		return beginWriter.tryGetTokens(tree);
	}
}