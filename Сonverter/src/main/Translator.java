package main;

import java.util.HashMap;
import parser.Token;

public class Translator
{
	private HashMap<String, ILanguage> languages;
	
    Translator()
    {
    	languages = new HashMap<String, ILanguage>();
    }
    
    public void register(String languageStr, ILanguage language)
    {
    	languages.put(languageStr, language);
    }
    
    public Token[] translate(String srcLanguageStr, String distLanguageStr, String src)
    {
    	ILanguage srcLanguage = languages.get(srcLanguageStr);
    	ILanguage distLanguage = languages.get(distLanguageStr);
    	Token tree = srcLanguage.getTreeFromString(src);
    	return distLanguage.getTokensFromTree(tree);
    }
}
