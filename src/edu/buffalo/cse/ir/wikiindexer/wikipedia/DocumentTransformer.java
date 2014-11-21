/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import src.edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument.Section;

/**
 * A Callable document transformer that converts the given WikipediaDocument object
 * into an IndexableDocument object using the given Tokenizer
 * @author nikhillo
 *
 */
public class DocumentTransformer implements Callable<IndexableDocument> {
	
	Map<INDEXFIELD, Tokenizer> token_map;
	WikipediaDocument document;
	/**
	 * Default constructor, DO NOT change
	 * @param tknizerMap: A map mapping a fully initialized tokenizer to a given field type
	 * @param doc: The WikipediaDocument to be processed
	 */
	public DocumentTransformer(Map<INDEXFIELD, Tokenizer> tknizerMap, WikipediaDocument doc) {
		//TODO: Implement this method
		token_map = tknizerMap;
		document = doc;
	}
	
	/**
	 * Method to trigger the transformation
	 * @throws TokenizerException Inc ase any tokenization error occurs
	 */
	public IndexableDocument call() throws TokenizerException 
	{	
		IndexableDocument indexdocument = new IndexableDocument();
		indexdocument.identifier = document.getTitle();
		TokenStream stream;
		Tokenizer tknzr;
		Iterator<String> iterator;
		Set<String> links = document.getLinks();
		String author = document.getAuthor();
		List<String> category = document.getCategories();
		List<Section> section = document.getSections();
		
		/*iterator = links.iterator();
		while(iterator.hasNext())
			System.out.println("Document Transformer Links WikiDoc" + iterator.next());*/
		
		if(!(links.isEmpty()))
		{
			iterator = links.iterator();
			stream = new TokenStream(iterator.next());
			while(iterator.hasNext())
				stream.append(iterator.next());
			tknzr = token_map.get(INDEXFIELD.LINK);
			stream.reset();
			tknzr.tokenize(stream);
			indexdocument.addField(INDEXFIELD.LINK, stream);
		}
		
		if(!(author.isEmpty()))
		{
			stream = new TokenStream(author);
			tknzr = token_map.get(INDEXFIELD.AUTHOR);
			tknzr.tokenize(stream);
			indexdocument.addField(INDEXFIELD.AUTHOR, stream);
		}
		
		if(!(category.isEmpty()))
		{
			iterator = category.iterator();
			stream = new TokenStream(iterator.next());
			while(iterator.hasNext())
				stream.append(iterator.next());
			tknzr = token_map.get(INDEXFIELD.CATEGORY);
			tknzr.tokenize(stream);
			indexdocument.addField(INDEXFIELD.CATEGORY, stream);
		}
		
		if(!(section.isEmpty()))
		{
			Iterator<Section> section_iterator = section.iterator();
			Section current = section_iterator.next();
			stream = new TokenStream(current.getTitle());
			stream.append(current.getText());
			while(section_iterator.hasNext())
			{
				current = section_iterator.next();
				stream.append(current.getTitle());
				stream.append(current.getText());
			}
			tknzr = token_map.get(INDEXFIELD.TERM);
			tknzr.tokenize(stream);
			//stream.print_stream();
			indexdocument.addField(INDEXFIELD.TERM, stream);
		}
		return indexdocument;
	}
	
}
