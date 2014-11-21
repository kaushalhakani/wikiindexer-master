/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.Map;
import java.util.TreeMap;

import src.edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;

/**
 * A simple map based token view of the transformed document
 * @author nikhillo
 *
 */
public class IndexableDocument {
	Map<INDEXFIELD, TokenStream> indexable_map = new TreeMap<INDEXFIELD, TokenStream>(); 
	String identifier;
	private static int document_counter = -1;
	final int document_id;
	/**
	 * Default constructor
	 */
	public IndexableDocument() {
		document_counter++;
		document_id = document_counter;
	}
	
	/**
	 * MEthod to add a field and stream to the map
	 * If the field already exists in the map, the streams should be merged
	 * @param field: The field to be added
	 * @param stream: The stream to be added.
	 */
	public void addField(INDEXFIELD field, TokenStream stream) {
		indexable_map.put(field, stream);
	}
	
	/**
	 * Method to return the stream for a given field
	 * @param key: The field for which the stream is requested
	 * @return The underlying stream if the key exists, null otherwise
	 */
	public TokenStream getStream(INDEXFIELD key) {
		TokenStream stream;
		stream = indexable_map.get(key);
		return stream;
	}
	
	/**
	 * Method to return a unique identifier for the given document.
	 * It is left to the student to identify what this must be
	 * But also look at how it is referenced in the indexing process
	 * @return A unique identifier for the given document
	 */
	public String getDocumentIdentifier() {
		//TODO: Implement this method
		return identifier;
	}
	
}
