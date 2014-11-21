/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.Properties;

import src.edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.*;
/**
 * Factory class to instantiate a Tokenizer instance
 * The expectation is that you need to decide which rules to apply for which field
 * Thus, given a field type, initialize the applicable rules and create the tokenizer
 * @author nikhillo
 *
 */
public class TokenizerFactory {
	//private instance, we just want one factory
	private static TokenizerFactory factory;
	
	//properties file, if you want to read soemthing for the tokenizers
	private static Properties props;
	
	/**
	 * Private constructor, singleton
	 */
	private TokenizerFactory() {
	}
	
	/**
	 * MEthod to get an instance of the factory class
	 * @return The factory instance
	 */
	public static TokenizerFactory getInstance(Properties idxProps) {
		factory = null;
		if (factory == null) {
			factory = new TokenizerFactory();
			props = idxProps;
		}
		
		return factory;
	}
	
	/**
	 * Method to get a fully initialized tokenizer for a given field type
	 * @param field: The field for which to instantiate tokenizer
	 * @return The fully initialized tokenizer
	 * @throws TokenizerException 
	 */
	public Tokenizer getTokenizer(INDEXFIELD field) throws Exception
	{
		Tokenizer token = new Tokenizer();
		switch(field)
		{
			case LINK:
				token = new Tokenizer();
				break;
			case AUTHOR:
				token = new Tokenizer();
				break;
			case CATEGORY:
				token = new Tokenizer(new Capitalization(), new Accents(), new SpecialChars()); 
				break;
			case TERM:
				token = new Tokenizer(new Punctuation(), new WhiteSpace(), new Capitalization(), new Apostrophe(), new Hyphen(), new Accents(), new SpecialChars(), new Numbers());
				break;
		}
		return token;
	}
}