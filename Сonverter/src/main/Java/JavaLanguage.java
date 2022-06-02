package main.Java;

import parser.Lexer;
import main.ILanguage;

public class JavaLanguage extends ILanguage
{
    public JavaLanguage()
    {
    	beginReader = new JavaBeginReader();
    	//beginWriter = new JavaBeginWriter();
    }
	
	protected void register(Lexer lexer)
	{
		lexer.register(new JavaBeginBracketReader());
		lexer.register(new JavaVarReader());
		lexer.register(new JavaInitReader());
		lexer.register(new JavaWordReader(";"));
		lexer.register(new JavaIfReader());
		lexer.register(new JavaForReader());
		lexer.register(new JavaMethodsReader());
		lexer.register(new JavaEndBracketReader());
	}
}