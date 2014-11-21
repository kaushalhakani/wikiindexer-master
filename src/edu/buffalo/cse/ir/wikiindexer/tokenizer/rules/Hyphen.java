package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;


import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.HYPHEN)
public class Hyphen implements TokenizerRule {

	private static String alphanum="^[a-zA-Z0-9]*\\-+[a-zA-Z0-9]*$";
	private static final String hyphen1="([a-zA-Z0-9]*\\-+[a-zA-Z]*[0-9]+)";
	private static final String hyphen2="([a-zA-Z]*[0-9]+\\-+[a-zA-Z0-9]*)";
	private static final String hyphen3="\\-+";
	
	public void apply(TokenStream stream) throws TokenizerException
	{
		if (stream != null) 
		{
			String token;
			while (stream.hasNext()) 
			{
				token = stream.next();
				token = token.trim();
				
				if (token.matches(alphanum))
				{
					if(token.matches(hyphen3))
					{
						stream.previous();
						stream.remove();
					}
					if (token.matches(hyphen1)==true ||  token.matches(hyphen2)==true)
						continue;
				    else
				    {
				    	token = token.replace("-", " ");
				    	token = token.trim();
				    	stream.previous();
						stream.set(token);
					}
				}
			}
		}
		stream.reset();
	}

}
