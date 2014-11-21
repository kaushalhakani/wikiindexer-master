package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.PUNCTUATION)
public class Punctuation implements TokenizerRule 
{
	public void apply(TokenStream stream) throws TokenizerException 
	{
		// TODO Auto-generated method stub

	    if (stream != null)
	    {
	    	while(stream.hasNext())
	    	{
	    		String token = stream.next();
	    		token = token.trim();
	    	
	    		while(token.endsWith(".") || token.endsWith("?") || token.endsWith("!"))
	    			token = token.substring(0, token.length()-1);
	    		
		    	if(token.contains(". ") || token.contains("? ") || token.contains("! "))
		    	{
		   			token = token.replaceAll("\\. ", " ");
		   			token = token.replaceAll("\\? ", " ");
		   			token = token.replaceAll("! ", " ");
		   		}
		    	stream.previous();
		    	stream.set(token);
		    	stream.next();
	    	}
	    	//stream.print_stream();
	    	stream.reset();	
		}
	}
}