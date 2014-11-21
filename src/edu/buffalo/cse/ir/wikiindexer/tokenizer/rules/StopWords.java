package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;



import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.STOPWORDS)
public class StopWords implements TokenizerRule {

	
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		String stopWords[]={"a","an","and","are","as","at","be","by","for","from","do","not","this",
			"has","he","in","is","it","its","of","on","that","the","to","was","were","will","with"};
		
		if (stream != null) {
			
			String token;
			while (stream.hasNext()) { 		
				token = stream.next();
				for(int i=0;i<stopWords.length;i++)
				{
					if (token.equalsIgnoreCase(stopWords[i]))
					{
						stream.previous();
						stream.remove();
						break;
					}
				}
			}
			
		}
					
	stream.reset();       
	}

}
