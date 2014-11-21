package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.WHITESPACE)
public class WhiteSpace implements TokenizerRule 
{
	//private static final String Whitespace = "\\s{1,}";
	
	public void apply(TokenStream stream) throws TokenizerException 
	{	
		if (stream != null) 
		{
			while(stream.hasNext())
			{
				String token = stream.next();
				token = token.trim();
				token = token.replaceAll("(\\s)+", " ");
				
				String[] tokens = token.split(" ");
				for(int i = 0; i < tokens.length; i++)
					tokens[i] = tokens[i].replaceAll("\\s", "");
				
				stream.previous();
				stream.set(tokens);
				stream.next();
			}
		}
	}
}

