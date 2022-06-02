package tests;

import org.junit.Assert;
import org.junit.Test;

import parser.*;

public class Tests
{
    @Test
    public void Comments()
    {
    	Lexer lexer = new Lexer();
    	lexer.register(new EndOfLineCommentReader());
    	lexer.register(new TraditionalCommentReader());
    	Token[] actuals = 
    		{
    			//lexer.tokenize("/")[0],
    			//lexer.tokenize("/a")[0],
    			lexer.tokenize("//\n")[0],
    			lexer.tokenize("//a")[0],
    			lexer.tokenize("//aa")[0],
    			lexer.tokenize("//aa\n//abc")[0],
    			lexer.tokenize("//aa\n//abc")[1],
    			lexer.tokenize("//aa\n//abc\n")[1],
    			lexer.tokenize("////\n////")[0],
    			lexer.tokenize("////\n////")[1],
    			lexer.tokenize("/**/")[0],
    			lexer.tokenize("/**///a")[0],
    			lexer.tokenize("/**///a")[1],
    			lexer.tokenize("/*a*/")[0],
    			lexer.tokenize("/*a*b**/")[0],
    		};
    	Token[] expected = 
    		{
    			//null,
    			//null,
    			new Token("endOfLineComment", "//\n"),
    			new Token("endOfLineComment", "//a"),
    			new Token("endOfLineComment", "//aa"),
    			new Token("endOfLineComment", "//aa\n"),
    			new Token("endOfLineComment", "//abc"),
    			new Token("endOfLineComment", "//abc\n"),
    			new Token("endOfLineComment", "////\n"),
    			new Token("endOfLineComment", "////"),
    			new Token("traditionalComment", "/**/"),
    			new Token("traditionalComment", "/**/"),
    			new Token("endOfLineComment", "//a"),
    			new Token("traditionalComment", "/*a*/"),
    			new Token("traditionalComment", "/*a*b**/"),
    		};
    	Assert.assertArrayEquals(expected, actuals);
    }
    
    @Test
    public void Test1()
    {
    	Lexer lexer = new Lexer();
    	lexer.register(new IntReader());
    	lexer.register(new IdentifierReader());
    	lexer.register(new WhitespaceReader());
    	Token[] expected = 
    		{
        			new Token("integer", "-42", -42),
        			new Token("identifier", "is"),
        			new Token("whitespace", " "),
        			new Token("identifier", "thenumber"),
    		};
    	Assert.assertEquals(expected[0].getValue(), lexer.tokenize("-42is thenumber")[0].getValue());
    }
}
