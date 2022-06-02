package main.Pascal;

import parser.Lexer;
import main.ILanguage;

public class PascalLanguage extends ILanguage
{
    public PascalLanguage()
    {
    	//beginReader = new PascalBeginReader();
    	beginWriter = new PascalBeginWriter();
    }
	
	/*protected void register(Lexer lexer)
	{
		lexer.register(new JavaBeginBracketReader());
		lexer.register(new JavaVarReader());
		lexer.register(new JavaArithmOpsReader());
		lexer.register(new JavaWordReader(";"));
		lexer.register(new JavaIfReader());
		lexer.register(new JavaForReader());
		lexer.register(new JavaMethodsReader());
		lexer.register(new JavaEndBracketReader());
	}*/
}