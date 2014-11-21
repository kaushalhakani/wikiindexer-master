package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import java.text.Normalizer;
import java.util.regex.Pattern;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.ACCENTS)
public class Accents implements TokenizerRule {

	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

           if (stream != null) 
           {
			String token;
			while (stream.hasNext()) 
			{ 
				token = stream.next();
				token = Normalizer.normalize(token, Normalizer.Form.NFKD);
				//token=token.toUpperCase();
				while(!(Normalizer.isNormalized(token,Normalizer.Form.NFKC)))
				{ 
					Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
					token = pattern.matcher(token).replaceAll("");
					token = Normalizer.normalize(token, Normalizer.Form.NFC);
				}
				stream.previous();
				stream.set(token);				
				stream.next();
			}
			stream.reset();
		}
		
	}

}
