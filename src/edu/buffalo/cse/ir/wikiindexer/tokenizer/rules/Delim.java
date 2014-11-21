package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DELIM)
public class Delim implements TokenizerRule {

	 private static final String Delim = ""; //[$%&/<=>@[]|()+~;#@]{1,}";
	 //private static final String nondigit = "";

	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		if (stream != null) {
			String token=null;
			while (stream.hasNext()) { 	
                token=stream.next();
	/*			if (token != null && token.matches(Delim)) 
					  {*/
              
					    Pattern delimPattern = Pattern.compile(Delim);
					    Matcher matchDelim = delimPattern.matcher(token);
					     if (matchDelim.find())
					      {  
					    	 // split it into two and save it					    	 
					    	 //token=token.replaceAll(Delim," ");
					    	 String [] str=token.split(token);
					    	 stream.set(str);
					    	 //String[] Split = delimPattern.split(token);
					         /*for(String s : stream) {
					         System.out.println(s);*/
						    
					    //  }
					     }
               // }
					
				}
									
		}
		
		stream.reset(); 
		
	}

}
