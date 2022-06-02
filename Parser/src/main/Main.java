package main;

import parser.*;

public class Main
{
    public static void main(String[] args)
    {
    	IReadable[] reders =
    	    {
    	        //new WordReader("123abc", true),
    	    	new BlockCommentReader(),
    	    	new EndOfLineCommentReader(),
    	    	new TraditionalCommentReader(),
    	    	new StringReader(),
    	    	new CharacterReader(),
    	        new FloatReader(),
    	        new IntReader(),
    	        new WhitespaceReader(),
    	        new WordReader("{"),
    	        new WordReader("}"),
    	        new WordReader("("),
    	        new WordReader(")"),
    	        new WordReader("["),
    	        new WordReader("]"),
    	        new WordReader("."),
    	        new WordReader("\""),
    	        new WordReader("'"),
    	        new WordReader(";"),
    	        new WordReader(":"),
    	        new IdentifierReader(),
    	    };
    	Lexer lexer = new Lexer();
    	for (IReadable reader : reders)
    	    lexer.register(reader);
    	//Token[] tokens = lexer.tokenize("//aaa/*c*/");
    	//Token[] tokens = lexer.tokenize("/\na");
    	//Token[] tokens = lexer.tokenize("k32\nA");
    	//Token[] tokens = lexer.tokenize("k32/\nA");
    	//Token[] tokens = lexer.tokenize("/k32\nA");
    	//Token[] tokens = lexer.tokenize("/k32/\nA");
    	//Token[] tokens = lexer.tokenize("//k32\nAbcd");
    	//Token[] tokens = lexer.tokenize("//k32\n\nAbcd");
    	//Token[] tokens = lexer.tokenize("//k32/\nA");
    	//Token[] tokens = lexer.tokenize("//k32//\nA");
    	//Token[] tokens = lexer.tokenize("//k32///\nA");
    	//Token[] tokens = lexer.tokenize("//k32////\nA");
    	//Token[] tokens = lexer.tokenize("///k32\nA");
    	//Token[] tokens = lexer.tokenize("///k32/\nA");
    	//Token[] tokens = lexer.tokenize("///k32//\nA");
    	//Token[] tokens = lexer.tokenize("///k32///\nA");
    	//Token[] tokens = lexer.tokenize("///k32////\nA");
    	//Token[] tokens = lexer.tokenize("////k32\nA");
    	//Token[] tokens = lexer.tokenize("////k32/\nA");
    	//Token[] tokens = lexer.tokenize("////k32//\nA");
    	//Token[] tokens = lexer.tokenize("////k32///\nA");
    	//Token[] tokens = lexer.tokenize("////k32////\nA");
    	//Token[] tokens = lexer.tokenize("/*k32*/Abcd");
    	//Token[] tokens = lexer.tokenize("/*k32*/*Abcd");
    	//Token[] tokens = lexer.tokenize("//*k32*/A");
    	//Token[] tokens = lexer.tokenize("///*k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32*//A");
    	//Token[] tokens = lexer.tokenize("/*k32*///A");
    	//Token[] tokens = lexer.tokenize("/**k32*/A");
    	//Token[] tokens = lexer.tokenize("/***k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32**/A");
    	//Token[] tokens = lexer.tokenize("/*k32***/A");
    	//Token[] tokens = lexer.tokenize("/*/k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32/*/A");
    	//Token[] tokens = lexer.tokenize("/**k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32**/A");
    	//Token[] tokens = lexer.tokenize("/*/*k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32/**/A");
    	//Token[] tokens = lexer.tokenize("/*/*/k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32/*/*/A");
    	//Token[] tokens = lexer.tokenize("/**/k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32*/*/A");
    	//Token[] tokens = lexer.tokenize("/**//k32*/A");
    	//Token[] tokens = lexer.tokenize("/*k32*//*/A");
    	//Token[] tokens = lexer.tokenize("*/k32*//*/A");
    	//Token[] tokens = lexer.tokenize("**/k32*//*/A");
    	//Token[] tokens = lexer.tokenize("*/*/k32*//*/A");
    	//Token[] tokens = lexer.tokenize("//");
    	//Token[] tokens = lexer.tokenize("/*");
    	//Token[] tokens = lexer.tokenize("/\n");
    	//Token[] tokens = lexer.tokenize("\n/");
    	//Token[] tokens = lexer.tokenize("\n/k32*//*/A");
    	//Token[] tokens = lexer.tokenize("/\nk32*//*/A");
    	//Token[] tokens = lexer.tokenize("\n/k32*//*/A");
    	Token[] tokens = lexer.tokenize("henumber456");
    	Token[] tokens2 = lexer.tokenize("public class Main\n"
+"{\n"
+"    public static void main(String[] args)\n"
+"    {\n"
+"        {\n"
/*+"    	System.out.print(\"Input first language: \");\n"
+"        }\n"
+"    }\n"
+"}"*/
);
    	if (tokens == null)
    		System.out.println("null");
    	else
        	for (Token token : tokens)
                System.out.println(token.getText() + ' '+token.getValue()+' '+token.getType());
    }
}