package parser;

import java.util.Arrays;

public class FloatReader implements IReadable
{
	private int skipNulls(String text, int i)
	{
	    while (i < text.length() && text.charAt(i) == '0')
	    	++i;
		return i;
	}
	
	private boolean isSuffix(char c)
	{
		return c == 'F' || c == 'D';
	}
	
	private boolean isOverflowedIntFloat(String inttext, String sign, boolean isDouble)
	{
		String max = isDouble ?
			sign + "179769313486231580793728971405303415079934132710037826936173778980444968292764750946649017977587207096330286416692887910946555547851940402630657488671505820681908902000708383676273854845817711531764475730270069855571366959622842914819860834936475292719074168444365510704342711559699508093042880177904174497791" :
			sign + "340282356779733661637539395458142568447";
		if (inttext.equals(max))
			return false;//new Token("float", text, isDouble ? Double.parseDouble(inttext) : Float.parseFloat(inttext));
		if (inttext.length() > max.length())
	        return true;//new IntReader().tryGetToken(text);
		if (inttext.length() == max.length())
			for (int i = 0; i < inttext.length(); ++i)
			    if (inttext.charAt(i) > max.charAt(i))
			    	return true;//new IntReader().tryGetToken(text);
		return false;
	}
	
	private boolean isOverflowedPartFloat(String partint, boolean isDouble, int startNotNull)
	{
		return startNotNull < partint.length() && startNotNull > (partint.charAt(startNotNull) >= (isDouble ? '3' : '8') ? 1 : 0) + (isDouble ? 322 : 44);
	}
	
	private Token getTokenFloatOrDouble(String text, boolean isDouble, int endPos, int pointPos)
	{
		if (!(pointPos - 1 >= 0 && pointPos < text.length() && Character.isDigit(text.charAt(pointPos - 1)) ||
			pointPos + 1 >= 0 && pointPos + 1 < text.length() && Character.isDigit(text.charAt(pointPos + 1)) ||
			pointPos + 2 >= 0 && pointPos + 2 < text.length() && Character.isDigit(text.charAt(pointPos + 2))))
			return null;//new IntReader().tryGetToken(text);
		text = text.substring(0, endPos);
		int isSuffix = isSuffix(Character.toUpperCase(text.charAt(text.length() - 1))) ? 1 : 0;
		if (isSuffix == 1)
		{
			int i = text.length() - 2;
			for (; i > 0; --i)
				if (Character.isDigit(text.charAt(i)) || text.charAt(i) == '.')
					break;
			if (i < text.length() - 2)
			{
				text = text.substring(0, i + 1);
				isSuffix = 0;
				endPos = i + 1;
			}
		}
		//System.out.println(text + " poa");
		String inttext = pointPos == -1 ? text.substring(0, text.length() - isSuffix) : text.substring(0, pointPos);
		inttext = inttext.replaceAll("_", "");
		if (inttext.length() > 1)
		{
			int i = skipNulls(inttext, 0);
			if (i == inttext.length())
				--i;
		    inttext = inttext.substring(i);
		}
		if (isOverflowedIntFloat(inttext, Character.isDigit(text.charAt(0)) ? "" : String.valueOf(text.charAt(0)), isDouble))
			return null;//new IntReader().tryGetToken(text);
		if (pointPos != -1)
		{
			String partint = pointPos + 1 < endPos - isSuffix ? text.substring(pointPos + 1, endPos - isSuffix).replace("_", "") : "";
			//System.out.println(partint + " part " + (pointPos + 1) + " " + (endPos - isSuffix));
			if ((inttext.length() == 0 || inttext.equals("0") || inttext.equals("+") || inttext.equals("-")) && pointPos != -1)
			{
				int i = skipNulls(partint, 0);
				//System.out.println(i < partint.length() && i > (partint.charAt(i) >= (isDouble ? '3' : '8') ? 1 : 0) + (isDouble ? 322 : 44));
				if (isOverflowedPartFloat(partint, isDouble, i))
				{
					partint = partint.substring(0, i);
					//System.out.println("kk"+partint);
					for (i = pointPos + 1; i < text.length(); ++i)
						if (!(text.charAt(i) == '0' || text.charAt(i) == '_'))
							break;
					text = text.substring(0, i);
					//System.out.println("kk"+text);
				}
			}
			inttext += '.' + partint;
		}
    	//System.out.println(inttext + " ppp");
		return new Token("float", text, isDouble ? Double.parseDouble(inttext) : Float.parseFloat(inttext));
	}
	
	private Token obrPoint(String text, int pointPos)
	{
		if (pointPos == text.length())
			return pointPos == 1 ? null : getTokenFloatOrDouble(text, true, text.length(), text.length() - 1);// - 1
		switch (Character.toUpperCase(text.charAt(pointPos)))
		{
		case 'E':
			return getTokenWithExp(text, pointPos - 1, pointPos + 1);
		case 'F':
			return getTokenFloatOrDouble(text, false, pointPos + 1, pointPos - 1);// - 1
		case 'D':
			return getTokenFloatOrDouble(text, true, pointPos + 1, pointPos - 1);// - 1
		default:
			if (!(Character.isDigit(text.charAt(pointPos))))
				return getTokenFloatOrDouble(text.substring(0, pointPos), true, pointPos, pointPos - 1);// - 1
			int i = pointPos--;
			while (++i < text.length())
				switch (Character.toUpperCase(text.charAt(i)))
				{
				case 'E':
					return getTokenWithExp(text, pointPos, i + 1);
				case 'F':
					return getTokenFloatOrDouble(text, false, i + 1, pointPos);
				case 'D':
					return getTokenFloatOrDouble(text, true, i + 1, pointPos);
				default:
					if (!Character.isDigit(text.charAt(i)) &&  text.charAt(i) != '_')
						return getTokenFloatOrDouble(text, true, i, pointPos);
				}
			return getTokenFloatOrDouble(text, true, text.length(), pointPos);
		}
	}
	
	private Token getOffsetedFloatToken(String t, String text, boolean isDouble, int pointPos, int startExp, int exp)
	{
		String inttext = null;
		if (pointPos == -1)
		{
			if (exp >= 0)//for example 1e2=100
			{
				int maxDigits = isDouble ? 309  : 39;
				if (startExp > maxDigits)
					return null;//new IntReader().tryGetToken(t);
				if (exp + startExp > maxDigits)
					if (exp / 10 + startExp > maxDigits)
						if (exp / 100 + startExp > maxDigits)
							return null;//new IntReader().tryGetToken(t);
						else
						{
							int i = t.length() - 1;
							for (int digits = 0, skipDigits = exp >= 100 ? 2 : 1; i >= 0; --i)
								if (digits > skipDigits)
									break;
								else if (Character.isDigit(t.charAt(i)))
									++digits;
							if (Character.toUpperCase(t.charAt(i)) != 'E')
								++i;
							t = t.substring(0, i);
							exp /= 100;
							isDouble = true;
						}
					else
					{
						int i = t.length() - 1;
						for (int digits = 0, skipDigits = exp >= 10 ? 1 : 0; i >= 0; --i)
							if (digits > skipDigits)
								break;
							else if (Character.isDigit(t.charAt(i)))
								++digits;
						if (Character.toUpperCase(t.charAt(i)) != 'E')
							++i;
						t = t.substring(0, i);
						exp /= 10;
						isDouble = true;
					}
				//System.out.println("ruru "+t);
				char[] chars = new char[exp];
				Arrays.fill(chars, '0');
				inttext =  text.substring(0, startExp) + new String(chars) + '.';
				pointPos = inttext.length() - 1;
				//System.out.println("yyy "+inttext);
				if (isOverflowedIntFloat(inttext.substring(0, pointPos), "", isDouble))
				{
					t = t.substring(0, t.length() - 1);
					inttext = inttext.substring(0, --pointPos) + '.';
				}
			}
			else if (startExp > -exp)//for example 1e-2=0.01
			{
				pointPos = startExp + exp;
				inttext =  text.substring(0, pointPos) + '.' + text.substring(pointPos, startExp);
			}
			else//for example 1111e-2=11.11
			{
				char[] chars = new char[-exp - startExp];
				Arrays.fill(chars, '0');
				inttext =  "0." + new String(chars) + text.substring(0, startExp);
				pointPos = 1;
			}
		}
		else
		{
			if (exp >= 0)
			{
				if (pointPos + exp > startExp - 1)//for example 1.1e2=110
				{
					int countSignNulls =  (pointPos + exp) - (startExp - 1);
					char[] chars = new char[countSignNulls];
					Arrays.fill(chars, '0');
					//System.out.println( text.substring(0, pointPos));
					inttext =  text.substring(0, pointPos) + text.substring(pointPos + 1, startExp) + new String(chars) + '.';
					pointPos = inttext.length() - 1;
				}
				else//for example 1.123e2=112.3
				{
					int countZelPart = pointPos + exp + 1;// - startExp - 2;
					inttext = text.substring(0, pointPos) + text.substring(pointPos + 1, countZelPart) + '.';
					pointPos = inttext.length() - 1;
					inttext += text.substring(countZelPart, startExp);
				}
			}
			else
			{
				if (pointPos <= -exp)//for exampe 1.1e-10;
				{
					int countUnsignNulls = -exp - pointPos;
					char[] chars = new char[countUnsignNulls];
					Arrays.fill(chars, '0');
					inttext = "0." + new String(chars) + text.substring(0, pointPos) + text.substring(pointPos + 1, startExp);
					pointPos = 1;
				}
				else//for example 1234.1e-1;
				{
					//System.out.println(text + " exp!");
					int countZelPart = pointPos + exp;
					inttext = text.substring(0, countZelPart) + '.';
					int tmp = pointPos;
					pointPos = inttext.length() - 1;
					inttext += text.substring(countZelPart, tmp) + text.substring(tmp + 1, startExp);
				}
			}
		}
		//System.out.println(inttext + " lol");
		Token token = getTokenFloatOrDouble(inttext, isDouble, inttext.length(), pointPos);
		return new Token(token.getType(), t, token.getValue());
	}
	
	private Token getTokenWithExp(String text, int pointPos, int expPos)
	{
		//System.out.println(text + " exp!");
		if (expPos >= text.length())
		{
			text = text.substring(0, --expPos);
			return getTokenFloatOrDouble(text, true, expPos, pointPos);//new IntReader().tryGetToken(text);
		}
		else if (expPos - 2 >= 0 && !Character.isDigit(text.charAt(expPos - 2)))
		{
			for (expPos -= 2; expPos >= 0; --expPos)
				if (Character.isDigit(text.charAt(expPos)) || text.charAt(expPos) == '.')
					break;
			text = text.substring(0, ++expPos);
			return getTokenFloatOrDouble(text, true, expPos, pointPos);//new IntReader().tryGetToken(text);
		}
		expPos += expPos < text.length() && (text.charAt(expPos) == '-' || text.charAt(expPos) == '+') ? 1 : 0;
		expPos = skipNulls(text, expPos);//float k=1e2_9f;
		expPos -= expPos == text.length() || !Character.isDigit(text.charAt(expPos)) ? 1 : 0;
		//System.out.println(text.charAt(expPos) + " starts ExpPos");
		int endPos = expPos;
		int digits = 0;
		for (; endPos < text.length(); ++endPos)
			if (digits > 2)
			{
				if (isSuffix(Character.toUpperCase(text.charAt(endPos))))
					++endPos;
				break;
			}
			else if (text.charAt(endPos) != '_')
				if (Character.isDigit(text.charAt(endPos)))
					++digits;
				else
				{
					if (isSuffix(Character.toUpperCase(text.charAt(endPos))))
						++endPos;
					break;
				}
		if (digits == 0)
			return getTokenFloatOrDouble(text, true, expPos - (text.charAt(expPos) == '-' || text.charAt(expPos) == '+' ? 1 : 0), pointPos);
		text = text.substring(0, endPos);
		//System.out.println(text);
		boolean isDouble = !(endPos - 1 < text.length() && Character.toUpperCase(text.charAt(endPos - 1)) == 'F');
		//System.out.println(isDouble);
		boolean isMinus = text.charAt(0) == '-';
		String inttext = text.replace("_", "");
		int k = skipNulls(inttext, isMinus ? 1 : 0);
		//System.out.println(inttext.charAt(k) + " pid");
		if (inttext.charAt(k) == '.')
		{
			inttext = '0' + inttext.substring(isMinus || text.charAt(0) == '+' ? 1 : 0);
			k = 0;
		}
		inttext = (isMinus ? '-' : "") + inttext.substring(k, inttext.length() - (isSuffix(Character.toUpperCase(inttext.charAt(inttext.length() - 1))) ? 1 : 0));
		pointPos = inttext.indexOf('.');
		expPos = inttext.indexOf('e', pointPos);
		//System.out.println(inttext + " " + pointPos + " " + expPos + " " + Integer.parseInt(inttext.substring(expPos + 1)));
		return getOffsetedFloatToken(text, inttext, isDouble, pointPos, expPos, Integer.parseInt(inttext.substring(expPos + 1)));
	}
	
	private Token getTokenDecFloat(String text, int i)
	{
		int cDigits = 0;
		for (i = skipNulls(text, i); i < text.length() && cDigits <= 309; ++i)
			if (text.charAt(i) != '_')
				if (Character.isDigit(text.charAt(i)))
					++cDigits;
				else
					switch (Character.toUpperCase(text.charAt(i++)))
					{
					case '.':
						return obrPoint(text, i);
					case 'E':
						return getTokenWithExp(text, -1, i);
					case 'F':
						return getTokenFloatOrDouble(text, false, i, -1);
					case 'D':
						return getTokenFloatOrDouble(text, true, i, -1);
					default:
						return null;//new IntReader().tryGetToken(text);
					}
		return null;//cDigits > 0 || i == text.length() ? new IntReader().tryGetToken(text) : null;
	}
	
	public Token tryGetToken(String text)
	{
		if (text.length() == 0)
			return null;
		int i = 0;
		if (text.charAt(0) == '+' || text.charAt(0) == '-')
			++i;
		if (text.length()  > i && (Character.isDigit(text.charAt(i)) || text.charAt(i) == '.'))
			return getTokenDecFloat(text, i);
		/*else if (text.length()  > i + 1)
			if (text.charAt(i) == '0' && text.charAt(i + 1) == 'x')
			{
				
			}*/
		return null;
	}
}
//double s=0.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001e100;
		//double h=1111111111111111111111111111111111111111111111.;
		//double f = 1.d;
		//float k=9223372036854775807L;
		//float f = 3e38f;
		//float l=3.40282356e38f;
		//float y=340282356779733661637539395458142568447f;
		//(1-2^(-25))*2^(2^7)-1
		//float y=3.4028235f;
		//float s=340282346638528859811704183484516925440f;
		//float s=34028235677973366163753939545814900000f;
		///////////3.4028234663852885981170418348452e+38
		//float f = 100000000000000000000000000000000000000f;
		//float f = 100000000000000000000000000000000000000.4f;
		//double k=100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000d;
		//double k=100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.9;
		//double k=100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.9d;
		//double j=179769313486231580793728971405303415079934132710037826936173778980444968292764750946649017977587207096330286416692887910946555547851940402630657488671505820681908902000708383676273854845817711531764475730270069855571366959622842914819860834936475292719074168444365510704342711559699508093042880177904174497791d;
		//(1- 2^(-54))*2^(2^10)-1
		//double h=100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000d;
		//float k=999999999;
		//float g=0x4;
		//double g=0x999999p999;
		//double f=99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999d;
		//double f=0099999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999.999d;
		//double f=0__099999___999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999.999d;
		//float f = 99999999999999999999999999999999999999f;
		//float fa = 1e-1f;
		//float fa = 1e+1f;
		//float f = 0.e1f;
		//float f = .0e1f;
		//float f = 0;
		//double f = 0.e1;
		//float f=0.0e0f;
		//float f=0.e0f;
		//float f=.0e0f;
		//float f=.0e0f;
		//float 