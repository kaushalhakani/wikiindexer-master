package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RuleClass(className = RULENAMES.NUMBERS)
public class Numbers implements TokenizerRule 
{

	String TIME = "([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";
	String YEAR="^([0-9]{4})(0[1-9]|1[012])((0?[1-9])|[12][0-9]|3[01])";
	String TIMESTAMP = "([0-9]{4})(0[1-9]|1[012])((0?[1-9])|[12][0-9]|3[01])\\s([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";
	String PATTERN = "^(\\d{1,})[^a-zA-Z]{0,}\\d{1,}";
	String PATTERN1 = "\\d{1,}\\,{0,}\\.{0,}";
	
	public void apply(TokenStream stream) throws TokenizerException 
	{
		if (stream != null) 
		{
			String token;// = "The App Store offered more than apps by Apple and third parties.";
			while(stream.hasNext()) 
			{		
				token = stream.next();
				if (!(token.matches(TIME)||token.matches(YEAR)||token.matches(TIMESTAMP)))
				{
					Pattern datePattern = Pattern.compile(PATTERN);
				    Matcher matchDatePattern = datePattern.matcher(token);
				    if (matchDatePattern.find())
				    {
				    	Pattern date1 = Pattern.compile(PATTERN1);
						Matcher matchDate1 = date1.matcher(token);
				    	if (matchDate1.find())
				    	{
				    		token=token.replaceAll(PATTERN1,"");
				    		//System.out.println(token);
				    		stream.previous();
				    		if(token.isEmpty())
				    			stream.remove();
				    		else
				    			stream.set(token);
				    		stream.next();
				    	}
				    }
				}
			}
		}
		stream.reset();
	}
}
