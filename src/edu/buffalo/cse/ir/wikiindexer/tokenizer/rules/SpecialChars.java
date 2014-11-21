/*
 *Haven't removed special character "-" because Hyphen.java already takes care of it. 
 */
package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.SPECIALCHARS)
public class SpecialChars implements TokenizerRule 
{
	/*private static final String specialChars="~";*/	

	public void apply(TokenStream stream) throws TokenizerException
	{
		if (stream != null) 
		{	
			String token;
			String[] token1 = null;
			while (stream.hasNext()) 
			{ 		
				token = stream.next();
				token = token.replaceAll("~","");
				token = token.replaceAll("#","");
				token = token.replaceAll("\\$","");
				token = token.replaceAll("%","");
				token = token.replaceAll("&","");
				token = token.replaceAll("\\(","");
				token = token.replaceAll("\\)","");
				token = token.replaceAll(":","");
				token = token.replaceAll(";","");
				token = token.replaceAll("\\+","");
				token = token.replaceAll("\\<","");
				token = token.replaceAll("\\>","");
				token = token.replaceAll("_","");
				token = token.replaceAll("\\\\","");
				token = token.replaceAll("/","");
				token = token.replaceAll("=","");
				token = token.replaceAll("\\|","");
				
				token = token.replaceAll("@","#");
				token = token.replaceAll("\\*","#");
				token = token.replaceAll("\\^","#");
				
				token = token.replaceAll("( )+", " ");
				token = token.trim();
				token1 = token.split("#");
				
				stream.previous();
				if(token1[0].isEmpty())
					stream.remove();
				else
				{
					stream.set(token1);
					stream.next();
				}
			}
			//stream.print_stream();
			stream.reset();
		}
	}	
}
