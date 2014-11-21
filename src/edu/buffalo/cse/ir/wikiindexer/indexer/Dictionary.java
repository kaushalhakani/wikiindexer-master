/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author nikhillo
 * An abstract class that represents a dictionary object for a given index
 */
public abstract class Dictionary implements Writeable {
	
	Map<String, Integer> dictionary = new TreeMap<String, Integer>();
	Map<String, Integer> file_dict = new TreeMap<String, Integer>();
	protected Properties idxProps;
	INDEXFIELD index_field;
	int ctr = 1;
	
	File file;
	FileWriter fw = null;
	BufferedWriter bw = null;
	FileReader fr = null;
	BufferedReader br = null;
	
	
	public Dictionary (Properties props, INDEXFIELD field) {
		//TODO Implement this method
		idxProps = props;
		index_field = field;
				
		if(index_field == INDEXFIELD.TERM)
			file = new File(idxProps.getProperty("root.dir") + idxProps.getProperty("term_dictionary"));
		else if(index_field == INDEXFIELD.AUTHOR)
			file = new File(idxProps.getProperty("root.dir") + idxProps.getProperty("author_dictionary"));
		else if(index_field == INDEXFIELD.LINK)
		{
			String temp = this.getClass().toString();
			if(temp.contains("LocalDictionary"))
				file = new File(idxProps.getProperty("root.dir") + idxProps.getProperty("link_dictionary"));
			else
				file = new File(idxProps.getProperty("root.dir") + idxProps.getProperty("document_dictionary"));
		}
		else if(index_field == INDEXFIELD.CATEGORY)
			file = new File(idxProps.getProperty("root.dir") + idxProps.getProperty("category_dictionary"));
		
		if (!file.exists()) 
		{	
			try 
			{
				file.createNewFile();
			}
			catch (IOException e) 
			{
				System.out.println(field + " Dictionary not created!");
				e.printStackTrace();
			}
		}
	}
	
	public void open_reader()
	{
		try 
		{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			br.mark(0);
		} catch (IOException e) {
			System.out.println("File Writer not Working!");
			e.printStackTrace();
		}
	}
	
	public void close_reader()
	{
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void open_writer()
	{
		try 
		{
			fw = new FileWriter(file,true);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			System.out.println("File Writer not Working!");
			e.printStackTrace();
		}
	}
	
	public void close_writer()
	{
		try 
		{
			bw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see src.edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException 
	{
		open_writer();
		String towrite = "";
		try 
		{
			file_dict = readFromDisk();
			
			Iterator<Entry<String, Integer>> itr = dictionary.entrySet().iterator();
			while(itr.hasNext())
			{
				Entry<String, Integer> row = itr.next();
				if(!(file_dict.containsKey(row.getKey())))
				{
					towrite = towrite.concat("\n" + row.getKey() + " @ " + ctr);
					ctr++;
				}
				else
					continue;
			}
			//System.out.println("To Write : " + towrite);
			bw.write(towrite);
			bw.flush();
			close_writer();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see src.edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() {
		// TODO Implement this method
		try {
			writeToDisk();
			close_writer();
			dictionary.clear();
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to check if the given value exists in the dictionary or not
	 * Unlike the subclassed lookup methods, it only checks if the value exists
	 * and does not change the underlying data structure
	 * @param value: The value to be looked up
	 * @return true if found, false otherwise
	 */
	public boolean exists(String value) 
	{		
		return dictionary.containsKey(value);
	}
	
	/**
	 * MEthod to lookup a given string from the dictionary.
	 * The query string can be an exact match or have wild cards (* and ?)
	 * Must be implemented ONLY AS A BONUS
	 * @param queryStr: The query string to be searched
	 * @return A collection of ordered strings enumerating all matches if found
	 * null if no match is found
	 */
	public Collection<String> query(String queryStr) 
	{
		Collection<String> result = null;
		try 
		{
			writeToDisk();
			file_dict = readFromDisk();
			result = file_dict.keySet();
			Iterator<String> itr = result.iterator();
			while(itr.hasNext())
			{
				String temp = itr.next();
				if(!(temp.matches(queryStr)))
					result.remove(temp);
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * Method to get the total number of terms in the dictionary
	 * @return The size of the dictionary
	 */
	public int getTotalTerms() {
		return dictionary.size();
	}
	
	
	
	public Map<String, Integer> readFromDisk() throws IOException
	{
		Map<String, Integer> file_dict1 = new TreeMap<String, Integer>();
		open_reader();
		while(true)
		{
			String temp1 = br.readLine();
			if(temp1 != null)
			{
				if(temp1.isEmpty())
					continue; 
				else
				{
					String[] temp = temp1.split(" @ ");
					int value = Integer.parseInt(temp[1]);
					file_dict1.put(temp[0].trim(), value);
				}
			}
			else
				break;
		}
		
		close_reader();
		return file_dict1;
	}
	
	/*public void print_map(Map<String, Integer> map)
	{
		Iterator<Entry<String, Integer>> map_itr = map.entrySet().iterator();
		while(map_itr.hasNext())
		{
			Entry<String, Integer> temp = map_itr.next();
			System.out.println(temp.getKey() + " @ " +temp.getValue());
		}
	}*/
}
