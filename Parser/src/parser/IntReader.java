package parser;

public class IntReader implements IReadable
{
	private String getIntWithoutUnderline(String text)
	{
		int i = text.length() - 1;
		while (i > 0 && text.charAt(i) == '_')
			--i;
		return text.substring(0, i + 1);
	}
	
	private boolean isOverflowedDec(String text, String inttext, int countDigits, int cDigits, boolean isLong, boolean isMinus)
	{
		if (cDigits < countDigits)
		    return false;
		String max = String.valueOf(isLong ? 1L << 63 : 1 << 31);
		if (max.charAt(0) == '-')
			max = max.substring(1);
		if (inttext.endsWith(max))
		    return !isMinus;
		String endinttext =  inttext.substring(inttext.length() - cDigits);
		for (int i = 0; i < endinttext.length(); ++i)
		    if (endinttext.charAt(i) > max.charAt(i))
		        return true;
		return false;
	}
	
	private Object getUnsignedInteger(String inttext, int radix, boolean isLong, boolean isMinus)
	{
		return (isMinus ? -1 : 1) * (isLong ? Long.parseUnsignedLong(inttext, radix) : Integer.parseUnsignedInt(inttext, radix));
	}
	
	private Object getSignedInteger(String inttext, int countDelNumbers, int radix, boolean isLong, boolean isMinus)
	{
		inttext = (isMinus ? '-' : "") + inttext.substring(0, inttext.length() - countDelNumbers);
		return isLong ? Long.parseLong(inttext, radix) : (Object)Integer.parseInt(inttext, radix);
	}
	
	private Token getTokenHexOrBin(String text, String inttext, int cDigits, int countDigits, char bigbit, int radix, boolean isLong, boolean isMinus)
	{
		return new Token("integer", text, cDigits == countDigits && inttext.charAt(inttext.length() - countDigits) >= bigbit ?
			getUnsignedInteger(inttext, radix, isLong, isMinus) :
			getSignedInteger(inttext, 0, radix, isLong, isMinus));
	}

	private Token getTokenDec(String text, String inttext, int countDigits, int cDigits, boolean isLong, boolean isMinus)
	{
		return isOverflowedDec(text, inttext, countDigits, cDigits, isLong, isMinus) ?
			new Token("integer", getIntWithoutUnderline(text.substring(0, text.length() - (isLong ? 2 : 1))), getSignedInteger(inttext, 1, 10, isLong, isMinus)) :
			new Token("integer", text, getSignedInteger(inttext, 0, 10, isLong, isMinus));//getUnsignedInteger(inttext, 10, isLong, isMinus)
	}
	
	private Token getTokenOct(String text, String inttext, int countDigits, int cDigits, boolean isLong, boolean isMinus)
	{
		if (cDigits == countDigits)
		{
			char bigbit = inttext.charAt(inttext.length() - countDigits);
			if (bigbit == '4')
				inttext = (isMinus ? '-' : "") + inttext.substring(1);
			else if (bigbit > '4')
			{
				inttext = inttext.substring(0, inttext.length() - 1);
				text = getIntWithoutUnderline(text.substring(0, text.length() - 1));
			}
			else if (isLong ? bigbit == '1' : bigbit >= '2')
				return new Token("integer", text, getUnsignedInteger(inttext, 8, isLong, isMinus));
		}
		return new Token("integer", text, getSignedInteger(inttext, 0, 8, isLong, false));
	}

	private Token getTokenUnoverflowedInt(String text, int countDigits, int cDigits)
	{
		String inttext = text.replace("_", "");
		boolean isMinus = text.charAt(0) == '-';
		boolean isLong = Character.toUpperCase(inttext.charAt(inttext.length() - 1)) == 'L';
		inttext = inttext.substring(inttext.length() - cDigits - (isLong ? 1 : 0), inttext.length() - (isLong ? 1 : 0));
		switch (isLong ? countDigits >> 1 : countDigits)
		{
		case 32: return getTokenHexOrBin(text, inttext, cDigits, countDigits, '1', 2, isLong, isMinus);
		case 8: return getTokenHexOrBin(text, inttext, cDigits, countDigits, '8', 16, isLong, isMinus);
		case 11: return getTokenOct(text, inttext, countDigits, cDigits, isLong, isMinus);
		case 9:
		case 10: return getTokenDec(text, inttext, countDigits, cDigits, isLong, isMinus);
		}
		return null;
	}
	
	private boolean isDigitBased(String base, char c)
	{
		return base.equals("bin") && isBinInt(c) ||
		    base.equals("oct") && isOctInt(Character.toUpperCase(c)) ||
		    base.equals("dec") && Character.isDigit(c) ||
		    base.equals("hex") && isHexInt(c);
	}
	
	private Token getTokenLong(String text, int i, String base, int countDigits, int cDigits)
	{
	    if (cDigits == countDigits && text.length() > i + 1)
	    {
    		countDigits = (countDigits << 1) - (countDigits == 10 ? 1 : 0);
	    	int tmpcDigits = cDigits + (isDigitBased(base, text.charAt(i)) ? 1 : 0);
	    	int j = i++;
	    	while (true)
		    	if (Character.toUpperCase(text.charAt(i)) == 'L')
		    	{
		    		++i;
			    	cDigits = tmpcDigits;
		    	    break;
		    	}
		    	else if (tmpcDigits >= countDigits || !(isDigitBased(base, text.charAt(i)) || text.charAt(i) == '_') || ++i >= text.length())
		    	{
		    		countDigits = (countDigits >> 1) + (countDigits == 10 ? 1 : 0);
		    		i = j;
		    		break;
		    	}
		    	else
		    		++tmpcDigits;
	    }
		text = getIntWithoutUnderline(text.substring(0, i));
	    return cDigits == 0 ?
	    	new Token("integer", text, 0) :
	        getTokenUnoverflowedInt(text, countDigits, cDigits);
	}
	
	private int skipNulls(String text, int i)
	{
	    while (++i < text.length() && text.charAt(i) == '0');
		return i - 1;
	}
	
	private Token getTokenInt(String text, int i, String base, int countDigits)
	{
		int cDigits = 0;
		i = skipNulls(text, i);
	    while (++i < text.length())
	    	if (Character.toUpperCase(text.charAt(i)) == 'L')
	    	{
	    		++i;
	    		countDigits = (countDigits << 1) - (countDigits == 10 ? 1 : 0);
	    		break;
	    	}
	    	else if (cDigits >= countDigits)
	    		break;
	    	else if (isDigitBased(base, text.charAt(i)))
    		    ++cDigits;
	    	else if (text.charAt(i) != '_')
	    		break;
	    return getTokenLong(text, i, base, countDigits, cDigits);
	}
	
	private boolean isHexInt(char c)
	{
		return c >= 'A' && c <= 'F' || Character.isDigit(c);
	}
	
	private Token getTokenHexInt(String text, int i)
	{
		return i + 1 == text.length() || !isHexInt(Character.toUpperCase(text.charAt(i + 1))) ?
			new Token("integer", text.substring(0, i), 0) :// + (Character.toUpperCase(text.charAt(i)) == 'L' ? 1 : 0)
			getTokenInt(text, i, "hex", 8);
	}
	
	private boolean isBinInt(char c)
	{
		return c == '0' || c == '1';
	}
	
	private Token getTokenBinInt(String text, int i)
	{
		return i + 1 == text.length() || !isBinInt(text.charAt(i + 1)) ?
			new Token("integer", text.substring(0, i), 0) :// + (Character.toUpperCase(text.charAt(i)) == 'L' ? 1 : 0)
				getTokenInt(text, i, "bin", 32);
	}
	
	private boolean isOctInt(char c)
	{
		return c >= '0' && c <= '7';
	}
	
	private Token getTokenOctInt(String text, int i)
	{
		return !isOctInt(text.charAt(i)) && text.charAt(i) != '_' ?
			new Token("integer", text.substring(0, i + (Character.toUpperCase(text.charAt(i)) == 'L' ? 1 : 0)), 0) :
				getTokenInt(text, i - 1, "oct", 11);
	}
	
	public Token tryGetToken(String text)
	{
		int i = 0;
		if (text.length() == 0 ||
			!(Character.isDigit(text.charAt(0)) || text.length() > 1 && (text.charAt(0) == '-' || text.charAt(0) == '+') && Character.isDigit(text.charAt(++i))))
			return null;
		if (text.charAt(i) == '0' && text.length() > i + 1)
			switch (Character.toUpperCase(text.charAt(++i)))
			{
		    case 'X': return getTokenHexInt(text, i);
		    case 'B': return getTokenBinInt(text, i);
		    default: return getTokenOctInt(text, i);
			}
		return getTokenInt(text, i - 1, "dec", 10);
	}
}