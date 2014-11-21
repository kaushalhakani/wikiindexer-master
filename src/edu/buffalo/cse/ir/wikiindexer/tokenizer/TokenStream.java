/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * This class represents a stream of tokens as the name suggests.
 * It wraps the token stream and provides utility methods to manipulate it
 * @author nikhillo
 *
 */
public class TokenStream implements Iterator<String>
{
	public List<String> token_stream = new ArrayList<String>();
	int string_length;
	Map<String,Integer> token_map;
	public int iterator = 0;

	/**
	 * Default constructor
	 * @param bldr: THe stringbuilder to seed the stream
	 */
	public TokenStream(StringBuilder bldr) 
	{
		String temp = bldr.toString();
		new TokenStream(temp);
	}
	
	/**
	 * Overloaded constructor
	 * @param bldr: THe stringbuilder to seed the stream
	 */
	public TokenStream(String string) 
	{	
		//System.out.println("Enters constructor");
		if(string == null || string.isEmpty() == true)
			token_stream = null;
		else
		{	
			token_stream.add(string);
			string_length = token_stream.size();
			iterator = 0;
			token_map = getTokenMap();
		}
	}
	
	/**
	 * Method to append tokens to the stream
	 * @param tokens: The tokens to be appended
	 */
	public void append(String... tokens) 
	{	
		int ctr = 0;
		if(tokens != null && tokens.length > 0)
		{
			//System.out.println(tokens.length);
			for(ctr = 0; ctr < tokens.length; ctr++)
			{
				//System.out.println("Appends: " + tokens[ctr]);
				if(tokens[ctr] != null && tokens[ctr].isEmpty() != true)
					token_stream.add(tokens[ctr]);
			}
		}
		//print_stream();
		token_map = getTokenMap();
	}
	
	/**
	 * Method to retrieve a map of token to count mapping
	 * This map should contain the unique set of tokens as keys
	 * The values should be the number of occurrences of the token in the given stream
	 * @return The map as described above, no restrictions on ordering applicable
	 */
	public Map<String, Integer> getTokenMap() 
	{	
		//System.out.println("Enters Map");
		token_map = new HashMap<String, Integer>();
		if(token_stream != null)
		{	
			//System.out.println("token_stream.size() = " + token_stream.size());
			for(int i = 0; i < token_stream.size(); i++)
			{
				if(token_stream.get(i) != null)
				{
					if(token_map.containsKey(token_stream.get(i)) == true)
						token_map.put(token_stream.get(i),token_map.get(token_stream.get(i))+1);
					else
						token_map.put(token_stream.get(i),1);
				}
				else
					break;
			}
			return token_map;
		}
		else
			return null;
	}
	
	/**
	 * Method to get the underlying token stream as a collection of tokens
	 * @return A collection containing the ordered tokens as wrapped by this stream
	 * Each token must be a separate element within the collection.
	 * Operations on the returned collection should NOT affect the token stream
	 */
	public Collection<String> getAllTokens() 
	{
		//System.out.println("Get All Tokens : " + token_stream.toString());
		if(token_stream != null)
			return token_stream.subList(0,token_stream.size());
		else
			return null;
	}
	
	/**
	 * Method to query for the given token within the stream
	 * @param token: The token to be queried
	 * @return: THe number of times it occurs within the stream, 0 if not found
	 */
	public int query(String token) {
		//System.out.println(token_map.toString());
		if(token_map != null && token_map.containsKey(token) == true)
		{
			return token_map.get(token);
		}
		else
			return 0;
	}
	
	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasNext() {
		boolean has_next = false;
		
		if(token_stream != null)
		{	
			if(iterator >=0 && iterator < token_stream.size() && token_stream.get(iterator) != null )
				has_next = true;
		}	
		return has_next;
	}
	
	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasPrevious() {		
		boolean has_prev = false;
		
		if(token_stream != null)
		{	
			if(iterator > 0 && token_stream.get(iterator-1) != null)
				has_prev = true;
		}
		return has_prev;
	}
	
	/**
	 * Iterator method: Method to get the next token from the stream
	 * Callers must call the set method to modify the token, changing the value
	 * of the token returned by this method must not alter the stream
	 * @return The next token from the stream, null if at the end
	 */
	public String next() {
		//System.out.println("Enters Next : " + hasNext());
		if(hasNext() == true)
		{
			iterator++;
			//System.out.println("Next : " + token_stream.get(iterator-1));
			return token_stream.get(iterator - 1);
		}
		else
			return null;
	}
	
	/**
	 * Iterator method: Method to get the previous token from the stream
	 * Callers must call the set method to modify the token, changing the value
	 * of the token returned by this method must not alter the stream
	 * @return The next token from the stream, null if at the end
	 */
	public String previous() {
		//System.out.println("Iterator : " + iterator);
		if(hasPrevious() == true)
		{
			iterator--;
			//System.out.println("Previous returns : " + token_stream.get(iterator) + " and Current position of iterator is : " + iterator);
			return token_stream.get(iterator);
		}
			
		else
			return null;
	}
	
	/**
	 * Iterator method: Method to remove the current token from the stream
	 */
	public void remove() 
	{
		if(token_stream != null)
		{
			if(iterator >= 0 && iterator < token_stream.size())
			{
				if(token_map.containsKey(token_stream.get(iterator)) == true)
				{
					if(token_map.get(token_stream.get(iterator)) == 1)
						token_map.remove(token_stream.get(iterator));
					else
						token_map.put(token_stream.get(iterator),token_map.get(token_stream.get(iterator))-1);
				}
				token_stream.remove(iterator);
			}
		}
	}
	
	/**
	 * Method to merge the current token with the previous token, assumes whitespace
	 * separator between tokens when merged. The token iterator should now point
	 * to the newly merged token (i.e. the previous one)
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithPrevious() {
		if(hasPrevious() == true)
		{
			//System.out.println("Merge With Previous : " + iterator);
			if(iterator < token_stream.size())
			{
				token_stream.set(iterator-1, token_stream.get(iterator-1) + " " + token_stream.get(iterator));
				remove();
				iterator--;
				getTokenMap();
				return true;
			}
			else 
				return false;
		}
		else	
			return false;
	}
	
	/**
	 * Method to merge the current token with the next token, assumes whitespace
	 * separator between tokens when merged. The token iterator should now point
	 * to the newly merged token (i.e. the current one)
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithNext() {
		if(hasNext() == true)
		{
			if(iterator < token_stream.size()-1)
			{
				token_stream.set(iterator, token_stream.get(iterator) + " " + token_stream.get(iterator+1));
				iterator++;
				remove();
				iterator--;
				getTokenMap();
				return true;
			}
			else
				return false;
		}
		else	
			return false;
	}
	
	/**
	 * Method to replace the current token with the given tokens
	 * The stream should be manipulated accordingly based upon the number of tokens set
	 * It is expected that remove will be called to delete a token instead of passing
	 * null or an empty string here.
	 * The iterator should point to the last set token, i.e, last token in the passed array.
	 * @param newValue: The array of new values with every new token as a separate element within the array
	 */
	public void set(String... newValue)
	{
		if(token_stream != null)
		{
			int loop_size = token_stream.size() - iterator;
			String[] temp_stream = new String[loop_size];
			
			
			for(int i = 0; i < loop_size - 1; i++)
			{
				temp_stream[i] = token_stream.remove(iterator + 1);
			}
				
			if(!(newValue == null))
				append(newValue);
			if(!(temp_stream == null))
				append(temp_stream);
			//System.out.println(token_stream.toString());
			
			if(!(newValue == null || newValue[0] == null || newValue[0].isEmpty() == true))
				remove();		//removes current element
			
			//System.out.println(token_stream.toString());
			iterator = iterator + newValue.length - 1;
			getTokenMap();
		}
	}
	
	/**
	 * Iterator method: Method to reset the iterator to the start of the stream
	 * next must be called to get a token
	 */
	public void reset() {
		if(token_stream != null)
			iterator = 0;
	}
	
	/**
	 * Iterator method: Method to set the iterator to beyond the last token in the stream
	 * previous must be called to get a token
	 */
	public void seekEnd() {
		if(token_stream != null)
			iterator = token_stream.size();
	}
	
	/**
	 * Method to merge this stream with another stream
	 * @param other: The stream to be merged
	 */
	public void merge(TokenStream other) {
		if(other != null)
		{		
			if(other.token_stream != null)
			{
				List<String> new_stream = other.token_stream;
				if(token_stream != null)
					append(new_stream.toArray(new String[token_stream.size()]));
				else
					token_stream = other.token_stream;
			}
		}
	}
	
	public void print_stream()
	{
		Iterator<String> itr = token_stream.iterator();
		while(itr.hasNext() == true)
			System.out.println("Element : " + itr.next());
	}
	
	/*public void removeEmpty()
	{
		for(int i = 0; i < token_stream.size();)
		{
			if(token_stream.get(i).isEmpty() == true)
				token_stream.remove(i);
			else
				i++;
		}
	}*/
}
