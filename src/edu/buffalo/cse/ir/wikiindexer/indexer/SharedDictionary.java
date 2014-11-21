/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.indexer;

import java.util.Map;
import java.util.Properties;

/**
 * @author nikhillo
 * This class represents a subclass of a Dictionary class that is
 * shared by multiple threads. All methods in this class are
 * synchronized for the same reason.
 */
public class SharedDictionary extends Dictionary {
	
	Map<String, Integer> dict;
	
	/**
	 * Public default constructor
	 * @param props: The properties file
	 * @param field: The field being indexed by this dictionary
	 */
	public SharedDictionary(Properties props, INDEXFIELD field) {
		super(props, field);
		// TODO Add more code here if needed
	}
	
	/**
	 * Method to lookup and possibly add a mapping for the given value
	 * in the dictionary. The class should first try and find the given
	 * value within its dictionary. If found, it should return its
	 * id (Or hash value). If not found, it should create an entry and
	 * return the newly created id.
	 * @param value: The value to be looked up
	 * @return The id as explained above.
	 */
	public synchronized int lookup(String value) 
	{
		int id = -1;
		if(dictionary.containsKey(value))
			id = dictionary.get(value);
		else
		{
			dictionary.put(value, dictionary.size()+1);
			id = dictionary.size();
		}
		
		/*Map<String, Integer> dict = null;
		int id = -1;
		try 
		{
			dict = readFromDisk();
			//print_map(dict);
			
			System.out.println(dict.containsKey("test"));
			
			if(dict.containsKey(value))
				id = dict.get(value);
			else
			{
				System.out.println("Enters Else : " + value);
				
				dictionary.put(value, dict.size()+1);
				writeToDisk();
				id = dict.size()+1;
			}
		} 
		catch (IOException | IndexerException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return id;
	}

}
