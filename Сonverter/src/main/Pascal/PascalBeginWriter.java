package main.Pascal;

import java.util.ArrayList;

import parser.*;

public class PascalBeginWriter implements IWriteable
{
	public PascalBeginWriter() { }
	
	public void getVars(Token root, ArrayList<Token> tokens)
	{
    	for (Token token : (Token[])root.getValue())
    		if (token.getType().equals("Root"))
    			getVars(token, tokens);
    		else if (token.getType().equals("For"))
    		{
    			Token[] childrenFor = (Token[])token.getValue();
    			getVars(new Token("", "", new Token[] { childrenFor[0] }), tokens);
    			if (childrenFor.length == 4)
        			getVars(childrenFor[3], tokens);
    		}
    		else if (token.getType().equals("If"))
    		{
    			Token[] childrenIf = (Token[])token.getValue();
    			if (childrenIf.length == 2)
        			getVars(childrenIf[1], tokens);
    		}
    		else if (token.getType().equals("Var"))
    		{
    			tokens.add(new Token("keyword", "var"));
    			tokens.add(new Token("whitespace", " "));
    			Token[] fields = (Token[])token.getValue();
    			tokens.add(fields[1]);
    			tokens.add(new Token("whitespace", " "));
    			tokens.add(new Token("keyword", ":"));
    			tokens.add(new Token("whitespace", " "));
    			String value = fields[0].getValue().toString();
    			if (value.equals("Integer"))
    			    tokens.add(new Token("TypeVar", "integer"));
    			else if (value.equals("Float"))
    			    tokens.add(new Token("TypeVar", "single"));
    			else if (value.equals("Double"))
    			    tokens.add(new Token("TypeVar", "double"));
    			else if (value.equals("String"))
    			    tokens.add(new Token("TypeVar", "string"));
    			else if (value.equals("Long"))
    			    tokens.add(new Token("TypeVar", "longint"));
    			tokens.add(new Token("keyword", ";"));
    			tokens.add(new Token("whitespace", "\n"));
    		}
	}
	
	private void addEnd(ArrayList<Token> tokens, String padding, String endLine)
	{
		tokens.add(new Token("whitespace", padding));
		tokens.add(new Token("", "end"));
		tokens.add(new Token("keyword", endLine));
		tokens.add(new Token("whitespace", "\n"));
	}
	
	private void addValue(ArrayList<Token> tokens, Token value)
	{
		Token[] args = (Token[])value.getValue();
		/*Token[] subArgs = (Token[])args[0].getValue();
		if (subArgs.length == 1)
			tokens.add(args[0]);
		else
		{
			tokens.add(new Token("keyword", "("));
			addValue(tokens, value);
			tokens.add(new Token("keyword", ")"));
		}*/
		if (value.getType().equals("PrimitiveValue"))
			tokens.add(args[0]);
		else if (value.getType().equals("PostfixIncrement"))
		{
			tokens.add(new Token("keyword", "inc"));
			tokens.add(new Token("keyword", "("));
			tokens.add(((Token[])args[0].getValue())[0]);
			tokens.add(new Token("keyword", ")"));
		}
		else if (value.getType().equals("PostfixDecrement"))
		{
			tokens.add(new Token("keyword", "dec"));
			tokens.add(new Token("keyword", "("));
			tokens.add(((Token[])args[0].getValue())[0]);
			tokens.add(new Token("keyword", ")"));
		}
		else if (value.getType().equals("UnaryMinus"))
		{
			tokens.add(new Token("keyword", "-"));
			tokens.add(((Token[])args[0].getValue())[0]);
		}
		else if (value.getType().equals("UnaryPlus"))
		{
			tokens.add(new Token("keyword", "+"));
			tokens.add(((Token[])args[0].getValue())[0]);
		}
		else if (args.length == 2)
		{
			tokens.add(((Token[])args[0].getValue())[0]);
			tokens.add(new Token("whitespace", " "));
			if (value.getType().equals("Plus"))
				tokens.add(new Token("keyword", "+"));
			else if (value.getType().equals("Minus"))
				tokens.add(new Token("keyword", "-"));
			else if (value.getType().equals("Divide"))
				tokens.add(new Token("keyword", "/"));
			else if (value.getType().equals("Multiple"))
				tokens.add(new Token("keyword", "*"));
			else if (value.getType().equals("Mod"))
				tokens.add(new Token("keyword", "mod"));
			else if (value.getType().equals("Xor"))
				tokens.add(new Token("keyword", "xor"));
			tokens.add(new Token("whitespace", " "));
			tokens.add(((Token[])args[1].getValue())[0]);
		}
	}
	
	private boolean checkNameVarInForUnaryOp(Token arg, String incOrDec, String nameVar1)
	{
		String typeOp = arg.getType();
		Token[] argsOp = (Token[])arg.getValue();
		Token[] primitiveVar = (Token[])argsOp[0].getValue();
		String nameVar2 = primitiveVar[0].getValue().toString();
		return (typeOp.equals("Prefix" + incOrDec) ||
			typeOp.equals("Postfix" + incOrDec)) &&
			nameVar2.equals(nameVar1);
	}
	
	private boolean checkNameVarInForBinaryPlusOp(Token arg, String nameVar1)
	{
		if (!arg.getType().equals("Equal"))
			return false;
		Token[] argsInit = (Token[])arg.getValue();
		Token[] primitiveVarInit = (Token[])argsInit[0].getValue();
		String nameVarInit1 = primitiveVarInit[0].getValue().toString();
		if (!nameVarInit1.toString().equals(nameVar1))
			return false;
		Token valueVar = (Token)argsInit[1].getValue();
		if (!valueVar.getType().equals("Plus"))
			return false;
		Token[] plusArgs = (Token[])valueVar.getValue();
		Token[] firstPrimitive = (Token[])plusArgs[0].getValue();
		Token[] secondPrimitive = (Token[])plusArgs[1].getValue();
		String firstName = firstPrimitive[0].getValue().toString();
		String secondName = secondPrimitive[0].getValue().toString();
		return firstName.equals(nameVar1) && secondName.equals("1") ||
			secondName.equals(nameVar1) && firstName.equals("1");
	}
	
	private boolean checkNameVarInForBinaryMinusOp(Token arg, String nameVar1)
	{
		if (!arg.getType().equals("Equal"))
			return false;
		Token[] argsInit = (Token[])arg.getValue();
		Token[] primitiveVarInit = (Token[])argsInit[0].getValue();
		String nameVarInit1 = primitiveVarInit[0].getValue().toString();
		if (!nameVarInit1.toString().equals(nameVar1))
			return false;
		Token valueVar = (Token)argsInit[1].getValue();
		if (!valueVar.getType().equals("Minus"))
			return false;
		Token[] plusArgs = (Token[])valueVar.getValue();
		Token[] firstPrimitive = (Token[])plusArgs[0].getValue();
		Token[] secondPrimitive = (Token[])plusArgs[1].getValue();
		String firstName = firstPrimitive[0].getValue().toString();
		String secondName = secondPrimitive[0].getValue().toString();
		return firstName.equals(nameVar1) && secondName.equals("1");
	}
	
	private void addMinusToNumber(ArrayList<Token> tokens, Token[] primitive)
	{
		tokens.add(primitive[0]);
		tokens.add(new Token("whitespace", " "));
		tokens.add(new Token("keyword", "-"));
		tokens.add(new Token("whitespace", " "));
		tokens.add(new Token("integer", "1", 1));
	}
	
	private boolean addEndNumber(ArrayList<Token> tokens, Token arg, Token[] primitive, String compare)
	{
		if (arg.getType().equals("Compare" + compare + "OrEqual"))
			tokens.add(primitive[0]);
		else if (arg.getType().equals("Compare" + compare))
			addMinusToNumber(tokens, primitive);
		else
			return false;
		return true;
	}
	
	private boolean checkNameVarInForCompare(ArrayList<Token> tokens, Token arg, String nameVar1, boolean incVar)
	{
		Token[] compareArgs = (Token[])arg.getValue();
		Token[] firstPrimitive = (Token[])compareArgs[0].getValue();
		Token[] secondPrimitive = (Token[])compareArgs[1].getValue();
		String fistName = firstPrimitive[0].getValue().toString();
		String secondName = secondPrimitive[0].getValue().toString();
		boolean isFist = fistName.equals(nameVar1);
		if (!isFist && !secondName.equals(nameVar1))
			return false;
		if (arg.getType().equals("CompareEqual"))
			if (isFist)
				tokens.add(secondPrimitive[0]);
			else
				tokens.add(firstPrimitive[0]);
		else
			return incVar ?
				isFist ? addEndNumber(tokens, arg, secondPrimitive, "Less") :
					addEndNumber(tokens, arg, firstPrimitive, "Than") :
				isFist ? addEndNumber(tokens, arg, secondPrimitive, "Than") :
					addEndNumber(tokens, arg, firstPrimitive, "Less");
		return true;
	}

	private void addFor(ArrayList<Token> tokens, Token token, String padding)
	{
		Token[] args = (Token[])token.getValue();
		tokens.add(new Token("whitespace", padding));
		tokens.add(new Token("keyword", "for"));
		tokens.add(new Token("whitespace", " "));
		String nameVar = null;
		if (args[0].getType().equals("Var"))
		{
			Token[] argsVar = (Token[])args[0].getValue();
			if (argsVar.length == 2 ||
				!argsVar[0].getValue().equals("Integer") &&
				!argsVar[0].getValue().equals("Long"))
			{
				tokens.clear();
				return;
			}
			tokens.add(argsVar[1]);
			tokens.add(new Token("whitespace", " "));
			tokens.add(new Token("keyword", ":="));
			tokens.add(new Token("whitespace", " "));
			addValue(tokens, (Token)argsVar[2].getValue());
			nameVar = argsVar[1].getValue().toString();
		}
		else if (args[0].getType().equals("Initialize"))
		{
			Token[] argsVar = (Token[])args[0].getValue();
			addValue(tokens, argsVar[0]);
			tokens.add(new Token("whitespace", " "));
			tokens.add(new Token("keyword", ":="));
			tokens.add(new Token("whitespace", " "));
			addValue(tokens, (Token)argsVar[1].getValue());
			nameVar = ((Token[])argsVar[0].getValue())[0].getValue().toString();
			for (int i = 0; i < tokens.size(); ++i)
				if (tokens.get(i).getType().equals("Root"))
					break;
				else if (tokens.get(i).getValue().toString().equals(nameVar))
				{
					i += 4;
					if (!tokens.get(i).getValue().toString().equals("longint") &&
						!tokens.get(i).getValue().toString().equals("integer"))
					{
						tokens.clear();
						return;
					}
					break;
				}
		}
		else
		{
			tokens.clear();
			return;
		}
		tokens.add(new Token("whitespace", " "));
		boolean incVar = checkNameVarInForUnaryOp(args[2], "Increment", nameVar) ||
			checkNameVarInForBinaryPlusOp(args[2], nameVar);
		if (incVar)
			tokens.add(new Token("keyword", "to"));
		else if (checkNameVarInForUnaryOp(args[2], "Decrement", nameVar) ||
			checkNameVarInForBinaryMinusOp(args[2], nameVar))
			tokens.add(new Token("keyword", "downto"));
		else
		{
			tokens.clear();
			return;
		}
		tokens.add(new Token("whitespace", " "));
		if (!checkNameVarInForCompare(tokens, args[1], nameVar, incVar))
		{
			tokens.clear();
			return;
		}
		tokens.add(new Token("whitespace", " "));
		tokens.add(new Token("keyword", "do"));
		tokens.add(new Token("whitespace", "\n"));
		int i = tokens.size();
		if (args.length == 4)
			if (args[3].getType().equals("Root"))
			{
				tokens.add(new Token("whitespace", padding));
				tokens.add(new Token("Root", "begin"));
				tokens.add(new Token("whitespace", "\n"));
				getOps(args[3], tokens, padding + "  ");
				addEnd(tokens, padding, ";");
			}
			else
			{
				Token subRoot = new Token("Root", args[3].getText(), new Token[] { args[3] });
				getOps(subRoot, tokens, padding + "  ");
			}
		else if (args.length == 3)
		{
			tokens.add(new Token("whitespace", padding));
			tokens.add(new Token("Root", "begin"));
			tokens.add(new Token("whitespace", "\n"));
			addEnd(tokens, padding, ";");
		}
		for (; i < tokens.size(); ++i)
			if (tokens.get(i).getType().equals("identifier"))
			{
				String nameVar1 = tokens.get(i).getValue().toString();
				if (!nameVar1.equals(nameVar))
					continue;
				String whitespace = tokens.get(++i).getValue().toString();
				if (!whitespace.equals(" "))
					continue;
				String equal = tokens.get(++i).getValue().toString();
				if (equal.equals(":="))
				{
					tokens.clear();
					return;
				}
			}
	}
	
	private void addIf(ArrayList<Token> tokens, Token token, String padding)
	{
		Token[] args = (Token[])token.getValue();
		tokens.add(new Token("whitespace", padding));
		tokens.add(new Token("keyword", "if"));
		Token[] compareArgs = (Token[])args[0].getValue();
		for (Token op : (Token[])compareArgs[0].getValue())
		{
			tokens.add(new Token("whitespace", " "));
			tokens.add(op);
		}
		tokens.add(new Token("whitespace", " "));
		if (args[0].getType().equals("CompareThan"))
			tokens.add(new Token("keyword", ">"));
		else if (args[0].getType().equals("CompareLess"))
			tokens.add(new Token("keyword", "<"));
		else if (args[0].getType().equals("CompareEqual"))
			tokens.add(new Token("keyword", "="));
		if (args[0].getType().equals("CompareThanOrEqual"))
			tokens.add(new Token("keyword", ">="));
		if (args[0].getType().equals("CompareLessOrEqual"))
			tokens.add(new Token("keyword", "<="));
		for (Token op : (Token[])compareArgs[1].getValue())
		{
			tokens.add(new Token("whitespace", " "));
			tokens.add(op);
		}
		tokens.add(new Token("whitespace", " "));
		tokens.add(new Token("keyword", "then"));
		tokens.add(new Token("whitespace", "\n"));
		if (args.length == 2)
			if (args[1].getType().equals("Root"))
			{
				tokens.add(new Token("whitespace", padding));
				tokens.add(new Token("Root", "begin"));
				tokens.add(new Token("whitespace", "\n"));
				getOps(args[1], tokens, padding + "  ");
				addEnd(tokens, padding, ";");
			}
			else
			{
				Token subRoot = new Token("Root", args[1].getText(), new Token[] { args[1] });
				getOps(subRoot, tokens, padding + "  ");
			}
		else if (args.length == 1)
		{
			tokens.add(new Token("whitespace", padding));
			tokens.add(new Token("Root", "begin"));
			tokens.add(new Token("whitespace", "\n"));
			addEnd(tokens, padding, ";");
		}
	}
	
	private void addDefAndInitVar(ArrayList<Token> tokens, Token token, String padding)
	{
		Token[] fields = (Token[])token.getValue();
		if (fields.length != 3)
			return;
		tokens.add(new Token("whitespace", padding));
		tokens.add(fields[1]);
		tokens.add(new Token("whitespace", " "));
		tokens.add(new Token("keyword", ":="));
		tokens.add(new Token("whitespace", " "));
		boolean isString = fields[0].getValue().equals("String");
		if (isString)
			tokens.add(new Token("keyword", "'"));
		addValue(tokens, (Token)fields[2].getValue());
		if (isString)
			tokens.add(new Token("keyword", "'"));
		tokens.add(new Token("keyword", ";"));
		tokens.add(new Token("whitespace", "\n"));
	}
	
	private void addOnlyInitVar(ArrayList<Token> tokens, Token token, String padding)
	{
		Token[] args = (Token[])token.getValue();
		tokens.add(new Token("whitespace", padding));
		Token nameVar = ((Token[])args[0].getValue())[0];
		tokens.add(nameVar);
		tokens.add(new Token("whitespace", " "));
		tokens.add(new Token("keyword", ":="));
		tokens.add(new Token("whitespace", " "));
		boolean isString = false;
		for (int i = 0; i < tokens.size(); ++i)
			if (tokens.get(i).getType().equals("Root"))
				break;
			else if (tokens.get(i).getValue().toString().equals(nameVar.getValue().toString()))
			{
				i += 4;
				isString = tokens.get(i).getValue().toString().equals("string");
				break;
			}
		if (isString)
			tokens.add(new Token("keyword", "'"));
		addValue(tokens, (Token)args[1].getValue());
		if (isString)
			tokens.add(new Token("keyword", "'"));
		tokens.add(new Token("keyword", ";"));
		tokens.add(new Token("whitespace", "\n"));
	}
	
	private void addMethod(ArrayList<Token> tokens, Token token, String padding)
	{
		Token[] pair = (Token[])token.getValue();
		String typeMethod = pair[0].getType();
		tokens.add(new Token("whitespace", padding));
		if (typeMethod.equals("WriteLine"))
		{
			tokens.add(new Token("keyword", "writeln"));
			tokens.add(new Token("keyword", "("));
			addValue(tokens, (Token)pair[1].getValue());
			tokens.add(new Token("keyword", ")"));
			tokens.add(new Token("keyword", ";"));
			tokens.add(new Token("whitespace", "\n"));
		}
		else if (typeMethod.equals("Write"))
		{
			tokens.add(new Token("keyword", "write"));
			tokens.add(new Token("keyword", "("));
			addValue(tokens, (Token)pair[1].getValue());
			tokens.add(new Token("keyword", ")"));
			tokens.add(new Token("keyword", ";"));
			tokens.add(new Token("whitespace", "\n"));
		}
	}
	
	private void addRoot(ArrayList<Token> tokens, Token token, String padding)
	{
		tokens.add(new Token("whitespace", padding));
		tokens.add(new Token("Root", "begin"));
		tokens.add(new Token("whitespace", "\n"));
		getOps(token, tokens, padding + "  ");
		addEnd(tokens, padding, ";");
	}
    
	private void getOps(Token root, ArrayList<Token> tokens, String padding)
	{
    	for (Token token : (Token[])root.getValue())
    		if (tokens.size() == 0)
    			return;
    		else if (token.getType().equals("Root"))
    			addRoot(tokens, token, padding);
    		else if (token.getType().equals("Method"))
    			addMethod(tokens, token, padding);
    		else if (token.getType().equals("Initialize"))
    			addOnlyInitVar(tokens, token, padding);
    		else if (token.getType().equals("Var"))
    			addDefAndInitVar(tokens, token, padding);
    		else if (token.getType().equals("If"))
    			addIf(tokens, token, padding);
    		else if (token.getType().equals("For"))
    			addFor(tokens, token, padding);
	}

	public Token[] tryGetTokens(Token root)
	{
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token("ProgramName", "program Main;\n"));
		getVars(root, tokens);
		tokens.add(new Token("Root", "begin"));
		tokens.add(new Token("keyword", "\n"));
		getOps(root, tokens, "  ");
		if (tokens.size() == 0)
			return null;
		addEnd(tokens, "", ".");
		System.out.print("\n\n\n");
		for (Token h : tokens)
			System.out.print(h.getValue());
		return tokens.toArray(new Token[tokens.size()]);
	}
}
