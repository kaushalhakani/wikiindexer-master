/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.indexer;

import java.util.Map;
import java.util.Properties;

/**
 * @author nikhillo
 * This class represents a subclass of a Dictionary class that is
 * local to a single thread. All methods in this class are
 * assumed thread safe for the same reason.
 */
public class LocalDictionary extends Dictionary {
	
	Map<String, Integer> dict;
	
	/**
	 * Public default constructor
	 * @param props: The properties file
	 * @param field: The field being indexed by this dictionary
	 */
	public LocalDictionary(Properties props, INDEXFIELD field) {
		super(props, field);
		// TODO Auto-generated constructor stub
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
	public int lookup(String value)
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
			
			if(dict.containsKey(value))
				id = dict.get(value);
			else
			{
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
	
	/*public static void main(String[] args)
	{
		Properties props = null;
		LocalDictionary ld;
		String filename = "C:\\Users\\Kaushal\\Documents\\Education\\SemesterI\\wikiindexer-master\\files\\properties.config";
		try 
		{
			props = FileUtil.loadProperties(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ld = new LocalDictionary(props, INDEXFIELD.TERM);
		
		ld.dictionary.put("test9", 1);
		ld.dictionary.put("test2", 3);
		ld.dictionary.put("test3", 1);
		ld.dictionary.put("test1", 6);
		ld.dictionary.put("test7", 1);
		ld.dictionary.put("test5", 8);
		ld.dictionary.put("test6", 9);
		ld.dictionary.put("test8", 4);
		try {
			ld.writeToDisk();
			ld.cleanUp();
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}
